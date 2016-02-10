package edu.rosehulman.pughck.simplecrypto.adapters;

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.MessageModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 * Created by pughck on 2/8/2016.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private Firebase mMessagesRef;

    private String mConversationKey;
    private String myUid;

    private List<MessageModel> mMessages;

    public ConversationAdapter(String conversationKey) {

        mConversationKey = conversationKey;

        mMessages = new ArrayList<>();

        mMessagesRef = new Firebase(Constants.FIREBASE_CONVERSATIONS_URL
                + "/" + mConversationKey + Constants.FIREBASE_MESSAGES_URL);

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

                // TODO fix this
                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                String key = dataSnapshot.getKey();

                for (MessageModel md : mMessages) {
                    if (key.equals(md.getKey())) {
                        md = message;

                        notifyDataSetChanged();

                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                MessageModel message = dataSnapshot.getValue(MessageModel.class);

                mMessages.remove(message);

                notifyDataSetChanged();
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
            holder.mMessage.setBackgroundColor(Color.CYAN);
        } else {
            holder.mMessage.setBackgroundColor(Color.RED);

            // TODO doesn't actually work
            int width = holder.mWrapper.getMeasuredWidth();
            int textWidth = holder.mMessage.getMeasuredWidth();

            holder.mWrapper.setPadding(width - textWidth, 0, 0, 0);
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

        public View mWrapper;
        private TextView mMessage;

        public ViewHolder(final View itemView) {

            super(itemView);

            mWrapper = itemView.findViewById(R.id.message_view_wrapper);

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
