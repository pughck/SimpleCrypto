package edu.rosehulman.pughck.simplecrypto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.MessageModel;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 * Created by pughck on 2/8/2016.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private String mConversationKey;

    private List<MessageModel> mMessages;

    public ConversationAdapter(String conversationKey) {

        mConversationKey = conversationKey;

        // TODO firebase

        mMessages = new ArrayList<>();
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

        // TODO color etc... based on user
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

        private TextView mMessage;

        public ViewHolder(final View itemView) {

            super(itemView);

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
