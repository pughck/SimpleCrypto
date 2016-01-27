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
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;

/**
 * Created by pughck on 1/26/2016.
 */
public class SchemeLibraryAdapter extends RecyclerView.Adapter<SchemeLibraryAdapter.ViewHolder> {

    private List<SavedSchemeModel> mSchemes;

    public SchemeLibraryAdapter() {

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        Firebase schemeLibraryRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + firebase.getAuth().getUid() + Constants.FIREBASE_SAVED_SCHEMES);
        schemeLibraryRef.addChildEventListener(new SchemeLibraryChildEventListener());

        mSchemes = new ArrayList<>();
    }

    @Override
    public SchemeLibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_scheme_row_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SchemeLibraryAdapter.ViewHolder holder, int position) {

        SavedSchemeModel schemeModel = mSchemes.get(position);

        holder.mSchemeName.setText(schemeModel.getName());
    }

    @Override
    public int getItemCount() {

        return mSchemes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mSchemeName;

        public ViewHolder(View itemView) {

            super(itemView);

            mSchemeName = (TextView) itemView.findViewById(R.id.scheme_name);
        }
    }

    private class SchemeLibraryChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);
            scheme.setKey(dataSnapshot.getKey());

            mSchemes.add(scheme);

            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            String key = dataSnapshot.getKey();

            SavedSchemeModel newScheme = dataSnapshot.getValue(SavedSchemeModel.class);

            for (int i = 0; i < mSchemes.size(); i++) {
                SavedSchemeModel scheme = mSchemes.get(i);
                if (scheme.getKey().equals(key)) {
                    scheme.setValues(newScheme);

                    notifyItemChanged(i);

                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            String key = dataSnapshot.getKey();

            for (SavedSchemeModel string : mSchemes) {
                if (key.equals(string.getKey())) {
                    mSchemes.remove(string);

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
