package edu.rosehulman.pughck.simplecrypto.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 * Created by pughck on 2/8/2016.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private Context mContext;

    private Firebase mMessagesRef;

    private String myUid;

    private ConversationModel mConversation;

    private List<MessageModel> mMessages;

    public ConversationAdapter(Context context, String conversationKey) {

        mContext = context;

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
            holder.mCard.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            holder.mMessage.setTextColor(Color.WHITE);

            holder.mWrapper.setGravity(Gravity.START);
        } else {
            holder.mCard.setBackgroundColor(ContextCompat.getColor(mContext, R.color.purple));
            holder.mMessage.setTextColor(Color.BLACK);

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
        private View mCard;
        private TextView mMessage;

        public ViewHolder(final View itemView) {

            super(itemView);

            mWrapper = (LinearLayout) itemView.findViewById(R.id.message_view_wrapper);

            mCard = itemView.findViewById(R.id.message_card_view);

            mMessage = (TextView) itemView.findViewById(R.id.message);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // TODO decrypt message ??
                }
            });
        }
    }
}
