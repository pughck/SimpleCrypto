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

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.Constants;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.SavedStringModel;

/**
 * Created by pughck on 1/26/2016.
 */
public class SavedStringsAdapter extends RecyclerView.Adapter<SavedStringsAdapter.ViewHolder> {

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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mString;

        public ViewHolder(View itemView) {

            super(itemView);

            mString = (TextView) itemView.findViewById(R.id.string);
        }
    }

    private class SavedStringsChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            SavedStringModel string = dataSnapshot.getValue(SavedStringModel.class);
            string.setKey(dataSnapshot.getKey());

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