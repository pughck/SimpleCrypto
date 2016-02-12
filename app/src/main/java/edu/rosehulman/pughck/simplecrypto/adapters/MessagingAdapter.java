package edu.rosehulman.pughck.simplecrypto.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rosehulman.pughck.simplecrypto.fragments.ConversationFragment;
import edu.rosehulman.pughck.simplecrypto.models.ConversationModel;
import edu.rosehulman.pughck.simplecrypto.models.MessagesModel;
import edu.rosehulman.pughck.simplecrypto.models.UserModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.utilities.GetProfilePicTask;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 * Created by pughck on 2/07/2016.
 */
public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private FragmentActivity mActivity;

    private List<MessagesModel> mConversations;

    private Map<String, ViewHolder> mConversationsMap;

    Map<Firebase, ConversationListener> mListeners;

    public MessagingAdapter(FragmentActivity activity) {

        mActivity = activity;

        // TODO
        // have the firebase be from the conversations using a query if possible so that we can see the updates
        // query would need to be based on user1 or user2 or list of conversations that updates
        // may cause issues with deleting them...

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        Firebase userConversationsRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + firebase.getAuth().getUid() + Constants.FIREBASE_USER_CONVERSATIONS);
        userConversationsRef.addChildEventListener(new MessagingChildEventListener());

        mConversations = new ArrayList<>();
        mConversationsMap = new HashMap<>();
        mListeners = new HashMap<>();
    }

    public void onBackPressed() {

        // update notifications count
        String uid = new Firebase(Constants.FIREBASE_URL).getAuth().getUid();
        for (MessagesModel model : mConversations) {
            new Firebase(Constants.FIREBASE_USERS_URL + "/" + uid
                    + "/" + Constants.FIREBASE_USER_CONVERSATIONS + "/" + model.getKey())
                    .setValue(model);
        }
    }

    @Override
    public MessagingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messaging_row_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessagingAdapter.ViewHolder holder, int position) {

        MessagesModel messagesModel = mConversations.get(position);

        holder.mUser.setText(messagesModel.getUsername());

        int notifications = messagesModel.getNotifications();
        holder.mNotificationView.setVisibility(notifications == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.mNotificationView.setText(mActivity.getString(R.string.message_notification, notifications));

        Firebase conversationRef = new Firebase(Constants.FIREBASE_CONVERSATIONS_URL
                + "/" + messagesModel.getConversation());
        for (Firebase ref : mListeners.keySet()) {
            if (ref.toString().equals(conversationRef.toString())) {
                ref.removeEventListener(mListeners.get(ref));

                break;
            }
        }

        ConversationListener conversationListener = new ConversationListener();
        mListeners.put(conversationRef, conversationListener);
        conversationRef.addValueEventListener(conversationListener);

        mConversationsMap.remove(messagesModel.getConversation());
        mConversationsMap.put(messagesModel.getConversation(), holder);

        // find user and get pic
        Firebase userRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + messagesModel.getUid());
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                String profilePicUrl = user.getProfilePic();

                if (profilePicUrl.isEmpty()) {
                    holder.mUserPic.setImageResource(R.drawable.default_profile);
                } else {
                    new GetProfilePicTask(holder.mUserPic).execute(profilePicUrl);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });
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

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        Firebase conversationRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + firebase.getAuth().getUid()
                + Constants.FIREBASE_USER_CONVERSATIONS + "/" + key);

        conversationRef.removeValue();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mUserPic;
        private TextView mUser;
        private TextView mNotificationView;

        public ViewHolder(final View itemView) {

            super(itemView);

            mUserPic = (ImageView) itemView.findViewById(R.id.user_image);
            mUser = (TextView) itemView.findViewById(R.id.user);
            mNotificationView = (TextView) itemView.findViewById(R.id.notification_view);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    MessagesModel messagesModel = mConversations.get(getAdapterPosition());

                    // clear notifications
                    messagesModel.setNotifications(0);
                    mNotificationView.setVisibility(View.INVISIBLE);

                    // remove listeners
                    for (Firebase ref : mListeners.keySet()) {
                        ref.removeEventListener(mListeners.get(ref));
                    }

                    // update notifications count
                    String uid = new Firebase(Constants.FIREBASE_URL).getAuth().getUid();
                    for (MessagesModel model : mConversations) {
                        new Firebase(Constants.FIREBASE_USERS_URL + "/" + uid
                                + "/" + Constants.FIREBASE_USER_CONVERSATIONS + "/" + model.getKey())
                                .setValue(model);
                    }

                    // go to that conversation fragment
                    FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                    Fragment fragment = ConversationFragment.newInstance(messagesModel.getConversation());
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

            MessagesModel messagesModel = dataSnapshot.getValue(MessagesModel.class);
            messagesModel.setKey(dataSnapshot.getKey());

            mConversations.add(messagesModel);

            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String key = dataSnapshot.getKey();

            MessagesModel newConversation = dataSnapshot.getValue(MessagesModel.class);

            for (int i = 0; i < mConversations.size(); i++) {
                MessagesModel conversation = mConversations.get(i);
                if (conversation.getKey().equals(key)) {
                    conversation.setValues(newConversation);

                    notifyItemChanged(i);

                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            // TODO update map ??

            String key = dataSnapshot.getKey();

            for (MessagesModel conversation : mConversations) {
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

    private class ConversationListener implements ValueEventListener {

        private boolean mIgnore;

        public ConversationListener() {

            mIgnore = true;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (mIgnore) {
                mIgnore = false;

                return;
            }

            ConversationModel conversation = dataSnapshot.getValue(ConversationModel.class);
            conversation.setKey(dataSnapshot.getKey());

            ViewHolder holder = mConversationsMap.get(dataSnapshot.getKey());

            if (holder != null) {

                MessagesModel messagesModel = mConversations.get(holder.getAdapterPosition());

                messagesModel.incrementNotifications();

                notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

            Log.e(Constants.error, firebaseError.getMessage());
        }
    }
}
