package edu.rosehulman.pughck.simplecrypto.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.UserModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

/**
 *
 */
public class SettingsFragment extends Fragment {

    private UserModel mUser;

    public SettingsFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button changeNameButton = (Button) rootView.findViewById(R.id.user_name_change);
        changeUserInfo(changeNameButton);
        Button profilePictureChange = (Button) rootView.findViewById(R.id.user_picture_change);
        changeProfilePictureLink(profilePictureChange);

        return rootView;
    }

    private void changeProfilePictureLink(Button profilePictureChange) {
        final Firebase userRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + new Firebase(Constants.FIREBASE_URL).getAuth().getUid());
        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUser = dataSnapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });

        profilePictureChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    DialogFragment df = new DialogFragment() {
                        @NonNull
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            View view = getActivity().getLayoutInflater().inflate(R.layout
                                    .account_profile_picture_change_dialog, null, false);
                            builder.setView(view);
                            final EditText pictureEdit = (EditText) view.findViewById(R.id.picture_edit);
                            pictureEdit.setText(mUser.getProfilePic());
//                            if(!pictureEdit.getText().toString().contains(".jpg") || !pictureEdit
//                                    .getText().toString().contains(".png")){
//                                Log.d("CONTAINS", pictureEdit.getText().toString() + "");
//                                pictureEdit.setError(getContext().getString(R.string.picture_link_invalid));
//                            }
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mUser.setProfilePic(pictureEdit.getText().toString());
                                    userRef.setValue(mUser);
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, null);

                            return builder.create();
                        }
                    };
                    df.show(getActivity().getSupportFragmentManager(), "edit profile picture");
                }
            }
        });

    }

    private void changeUserInfo(Button changeNameButton) {

        final Firebase userRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + new Firebase(Constants.FIREBASE_URL).getAuth().getUid());

        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUser = dataSnapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });

        changeNameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mUser != null) {
                    DialogFragment df = new DialogFragment() {

                        @NonNull
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {

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

                                    mUser.setFistName(fName.getText().toString());
                                    mUser.setLastName(lName.getText().toString());
                                    mUser.setUsername(userName.getText().toString());

                                    userRef.setValue(mUser);
                                }
                            });

                            builder.setNegativeButton(android.R.string.cancel, null);

                            return builder.create();
                        }
                    };

                    df.show(getActivity().getSupportFragmentManager(), "edit user info");
                }
            }
        });
    }
}
