package edu.rosehulman.pughck.simplecrypto.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.fragments.ConversationFragment;
import edu.rosehulman.pughck.simplecrypto.models.ConversationModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 * Created by pughck on 2/07/2016.
 */
public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private FragmentActivity mActivity;

    private List<ConversationModel> mConversations;

    public MessagingAdapter(FragmentActivity activity) {

        mActivity = activity;

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        Firebase userConversationsRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + firebase.getAuth().getUid() + Constants.FIREBASE_USER_CONVERSATIONS);
        userConversationsRef.addChildEventListener(new MessagingChildEventListener());

        mConversations = new ArrayList<>();
    }

    @Override
    public MessagingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messaging_row_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagingAdapter.ViewHolder holder, int position) {

        ConversationModel conversationModel = mConversations.get(position);

        holder.mUser.setText(conversationModel.getUser());
    }

    @Override
    public int getItemCount() {

        return mConversations.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        String key = mConversations.get(position).getKey();

        // TODO

        /*
        final Firebase schemeRef = new Firebase(Constants.FIREBASE_SCHEMES_URL + "/" + key);

        final Firebase savedStringsRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + schemeRef.getAuth().getUid() + Constants.FIREBASE_SAVED_STRINGS);
        Query savedStringsQuery = savedStringsRef.orderByChild("encryption").equalTo(key);
        savedStringsQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    DialogFragment df = new DialogFragment() {

                        @NonNull
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstance) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle(mActivity.getString(R.string.delete_scheme_warning));
                            builder.setMessage(mActivity.getString(R.string.delete_scheme_warning_message));

                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    schemeRef.removeValue();

                                    Map<String, Map<String, String>> values = (Map) dataSnapshot.getValue();
                                    for (String key : values.keySet()) {
                                        savedStringsRef.child(key).removeValue();
                                    }
                                }
                            });

                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    notifyDataSetChanged();
                                }
                            });

                            return builder.create();
                        }
                    };

                    df.show(mActivity.getSupportFragmentManager(), "warning");
                } else {
                    schemeRef.removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });
        */
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUser;

        public ViewHolder(final View itemView) {

            super(itemView);

            mUser = (TextView) itemView.findViewById(R.id.user);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // go to that conversation fragment
                    String conversationKey = mConversations.get(getAdapterPosition())
                            .getConversation();

                    FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                    Fragment fragment = ConversationFragment.newInstance(conversationKey);
                    ft.replace(R.id.fragment_container, fragment);
                    ft.addToBackStack(Constants.conversations_added);
                    ft.commit();
                }
            });
        }
    }

    private class MessagingChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            ConversationModel conversationModel = dataSnapshot.getValue(ConversationModel.class);
            conversationModel.setKey(dataSnapshot.getKey());

            mConversations.add(conversationModel);

            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String key = dataSnapshot.getKey();

            ConversationModel newConversation = dataSnapshot.getValue(ConversationModel.class);

            for (int i = 0; i < mConversations.size(); i++) {
                ConversationModel conversation = mConversations.get(i);
                if (conversation.getKey().equals(key)) {
                    conversation.setValues(newConversation);

                    notifyItemChanged(i);

                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String key = dataSnapshot.getKey();

            for (ConversationModel conversation : mConversations) {
                if (key.equals(conversation.getKey())) {
                    mConversations.remove(conversation);

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
    }
}
