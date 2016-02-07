package edu.rosehulman.pughck.simplecrypto.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.User;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

/**
 *
 */
public class SettingsFragment extends Fragment {

    //    private OnFragmentInteractionListener mListener;
    private Firebase mFirebase;
    private User mUser;
    private Query mQuery;

    public SettingsFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        mFirebase = new Firebase(Constants.FIREBASE_USERS_URL);
//        Log.d("CHILD", mFirebase.child(mFirebase.getAuth().getUid()).toString());
        mQuery = mFirebase.orderByKey().equalTo(mFirebase.getAuth().getUid());
//        Log.d("HELP", mQuery.toString()+"");
        mQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("DATA", dataSnapshot.toString());
                mUser = (User) dataSnapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("CHANGED", dataSnapshot.toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // TODO
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        Log.d("USER", mFirebase.getAuth().getUid());
        Button changeNameButton = (Button) rootView.findViewById(R.id.user_change);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Log.d("CLICKED", "Clicked " + ((Button) v).getText().toString());
                DialogFragment df = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
//                        Log.d("FIREBASE", mFirebase.child(mFirebase.getAuth().getUid()).toString());
//                        Log.d("USER", mUser.getFistName());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        View view = getActivity().getLayoutInflater().inflate(R.layout
                                .account_name_change_dialog, null, false);
                        builder.setView(view);
                        final EditText fName = (EditText) view.findViewById(R.id.first_name_edit);
                        fName.setText(mUser.getFistName());
                        final EditText lName = (EditText) view.findViewById(R.id.last_name_edit);
                        lName.setText(mUser.getLastName());
                        final EditText userName = (EditText) view.findViewById(R.id.username_edit);
                        userName.setText(mUser.getUsername());

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String firstN = fName.getText().toString();
                                String lastN = lName.getText().toString();
                                String uName = userName.getText().toString();
                                mUser.setFistName(firstN);
                                mUser.setLastName(lastN);
                                mUser.setUsername(uName);
                                Log.d("WHERE", mFirebase.getAuth().getUid());
                                mFirebase.child(mFirebase.getAuth().getUid()).setValue(mUser);
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, null);

                        return builder.create();
                    }
                };
                df.show(getActivity().getSupportFragmentManager(), "edit");

            }
        });

        // TODO

        return rootView;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {

        super.onDetach();

//        mListener = null;
    }

}
