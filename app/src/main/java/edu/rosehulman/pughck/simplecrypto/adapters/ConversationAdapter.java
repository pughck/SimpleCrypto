package edu.rosehulman.pughck.simplecrypto.adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.ciphers.ICipher;
import edu.rosehulman.pughck.simplecrypto.models.ConversationModel;
import edu.rosehulman.pughck.simplecrypto.models.MessageModel;
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;
import edu.rosehulman.pughck.simplecrypto.models.SavedStringModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 *
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private FragmentActivity mActivity;

    private Firebase mMessagesRef;

    private String myUid;
    private ConversationModel mConversation;
    private List<MessageModel> mMessages;

    public ConversationAdapter(FragmentActivity activity, String conversationKey,
                               final RecyclerView rView) {

        mActivity = activity;

        // cipher
        Firebase conversationRef = new Firebase(Constants.FIREBASE_CONVERSATIONS_URL
                + "/" + conversationKey);
        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mConversation = dataSnapshot.getValue(ConversationModel.class);
                String encryption = mConversation.getEncryption();

                Firebase schemeRef = new Firebase(Constants.FIREBASE_SCHEMES_URL + "/" + encryption);
                schemeRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);
                        ICipher cipher = scheme.convertToCipher();

                        mConversation.setCipher(cipher);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        Log.e(Constants.error, firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });

        mMessages = new ArrayList<>();

        mMessagesRef = conversationRef.child(Constants.FIREBASE_MESSAGES_URL);

        myUid = mMessagesRef.getAuth().getUid();

        mMessagesRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                message.setKey(dataSnapshot.getKey());

                mMessages.add(message);

                notifyDataSetChanged();

                rView.scrollToPosition(mMessages.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();

                MessageModel newMessage = dataSnapshot.getValue(MessageModel.class);

                for (int i = 0; i < mMessages.size(); i++) {
                    MessageModel message = mMessages.get(i);
                    if (message.getKey().equals(key)) {
                        message.setValues(newMessage);

                        notifyItemChanged(i);

                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();

                for (MessageModel message : mMessages) {
                    if (key.equals(message.getKey())) {
                        mMessages.remove(message);

                        notifyDataSetChanged();

                        return;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                // not used
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });
    }

    public void sendMessage(String message) {

        String encryptedMessage = mConversation.getCipher().encrypt(message);

        MessageModel messageModel = new MessageModel(mMessagesRef.getAuth().getUid(), encryptedMessage);

        mMessagesRef.push().setValue(messageModel);
    }

    public void showSavedStrings(final EditText message) {

        Firebase savedStringsRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + myUid + Constants.FIREBASE_SAVED_STRINGS);
        savedStringsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> strings = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    SavedStringModel string = child.getValue(SavedStringModel.class);

                    if (string.getEncryption().equals(mConversation.getEncryption())) {
                        strings.add(string.getString());
                    }
                }

                DialogFragment df = new DialogFragment() {

                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstance) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setItems(strings.toArray(new String[strings.size()]),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        message.setText(
                                                mConversation.getCipher().decrypt(strings.get(which)));

                                        dialog.dismiss();
                                    }
                                });

                        return builder.create();
                    }
                };

                df.show(mActivity.getSupportFragmentManager(), "show saved strings");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }

        });
    }

    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationAdapter.ViewHolder holder, int position) {

        MessageModel message = mMessages.get(position);

        holder.mMessage.setText(message.getMessage());

        if (message.getUser().equals(myUid)) {
            holder.mMessage.setTextColor(Color.WHITE);
            holder.mMessage.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimaryDark));

            holder.mWrapper.setGravity(Gravity.START);
        } else {
            holder.mMessage.setTextColor(Color.BLACK);
            holder.mMessage.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.purple));

            holder.mWrapper.setGravity(Gravity.END);
        }
    }

    @Override
    public int getItemCount() {

        return mMessages.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        String key = mMessages.get(position).getKey();

        mMessagesRef.child(key).removeValue();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mWrapper;
        private TextView mMessage;

        private boolean showPlain;

        public ViewHolder(final View itemView) {

            super(itemView);

            mWrapper = (LinearLayout) itemView.findViewById(R.id.message_view_wrapper);
            mMessage = (TextView) itemView.findViewById(R.id.message);

            showPlain = true;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    MessageModel message = mMessages.get(getAdapterPosition());

                    if (showPlain) {
                        ICipher cipher = mConversation.getCipher();
                        mMessage.setText(cipher.decrypt(message.getMessage()));
                    } else {
                        mMessage.setText(message.getMessage());
                    }

                    showPlain = !showPlain;
                }
            });
        }
    }
}
