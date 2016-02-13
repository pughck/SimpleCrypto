package edu.rosehulman.pughck.simplecrypto.adapters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;

/**
 *
 */
public class SchemeLibraryAdapter extends RecyclerView.Adapter<SchemeLibraryAdapter.ViewHolder>
        implements SwipeCallback.ItemTouchHelperAdapter {

    private FragmentActivity mActivity;

    private List<SavedSchemeModel> mSchemes;

    public SchemeLibraryAdapter(FragmentActivity activity) {

        mActivity = activity;

        Firebase schemeLibraryRef = new Firebase(Constants.FIREBASE_SCHEMES_URL);
        Query mySchemes = schemeLibraryRef.orderByChild("uid")
                .equalTo(schemeLibraryRef.getAuth().getUid());
        mySchemes.addChildEventListener(new SchemeLibraryChildEventListener());

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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        String key = mSchemes.get(position).getKey();

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
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mSchemeName;

        public ViewHolder(final View itemView) {

            super(itemView);

            mSchemeName = (TextView) itemView.findViewById(R.id.scheme_name);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // show details
                    DialogFragment df = new DialogFragment() {

                        @NonNull
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstance) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle(mSchemeName.getText().toString());
                            builder.setMessage(mSchemes.get(getAdapterPosition()).getInfoString());

                            return builder.create();
                        }
                    };

                    df.show(mActivity.getSupportFragmentManager(), "details");
                }
            });
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
