package edu.rosehulman.pughck.simplecrypto.adapters;

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
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.Constants;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.SwipeCallback;
import edu.rosehulman.pughck.simplecrypto.ciphers.ICipher;
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;
import edu.rosehulman.pughck.simplecrypto.models.SavedStringModel;

/**
 * Created by pughck on 1/26/2016.
 */
public class SavedStringsAdapter extends RecyclerView.Adapter<SavedStringsAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private Firebase savedStringsRef;

    private List<SavedStringModel> mSavedStrings;

    public SavedStringsAdapter() {

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        savedStringsRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + firebase.getAuth().getUid() + Constants.FIREBASE_SAVED_STRINGS);
        savedStringsRef.addChildEventListener(new SavedStringsChildEventListener());

        mSavedStrings = new ArrayList<>();
    }

    @Override
    public SavedStringsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_string_row_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedStringsAdapter.ViewHolder holder, int position) {

        SavedStringModel stringModel = mSavedStrings.get(position);

        holder.mString.setText(stringModel.getString());
    }

    @Override
    public int getItemCount() {

        return mSavedStrings.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        String key = mSavedStrings.get(position).getKey();

        Firebase firebase = savedStringsRef.child(key);
        firebase.removeValue();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mString;

        private boolean showPlain;

        public ViewHolder(View itemView) {

            super(itemView);

            mString = (TextView) itemView.findViewById(R.id.string);

            showPlain = false;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    SavedStringModel string = mSavedStrings.get(getAdapterPosition());

                    if (showPlain) {
                        ICipher cipher = string.getCipher();
                        mString.setText(cipher.decrypt(string.getString()));
                    } else {
                        mString.setText(string.getString());
                    }

                    showPlain = !showPlain;
                }
            });
        }
    }

    private class SavedStringsChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            final SavedStringModel string = dataSnapshot.getValue(SavedStringModel.class);
            string.setKey(dataSnapshot.getKey());

            // get scheme and create cipher
            Firebase schemeRef = new Firebase(Constants.FIREBASE_SCHEMES_URL + "/" + string.getEncryption());
            schemeRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);
                    ICipher cipher = scheme.convertToCipher();

                    string.setCipher(cipher);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                    Log.e(Constants.error, firebaseError.getMessage());
                }
            });

            mSavedStrings.add(string);

            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String key = dataSnapshot.getKey();

            SavedStringModel newString = dataSnapshot.getValue(SavedStringModel.class);

            for (int i = 0; i < mSavedStrings.size(); i++) {
                SavedStringModel string = mSavedStrings.get(i);
                if (string.getKey().equals(key)) {
                    string.setValues(newString);

                    notifyItemChanged(i);

                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String key = dataSnapshot.getKey();

            for (SavedStringModel string : mSavedStrings) {
                if (key.equals(string.getKey())) {
                    mSavedStrings.remove(string);

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
