package edu.rosehulman.pughck.simplecrypto.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.lang.reflect.Array;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.adapters.MessagingAdapter;
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;
import edu.rosehulman.pughck.simplecrypto.models.UserModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;

/**
 *
 */
public class CryptoMessagingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CryptoMessagingFragment() {

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
        final View rootView = inflater.inflate(R.layout.fragment_crypto_messaging, container, false);

        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.messaging_recycler_view);
        MessagingAdapter adapter = new MessagingAdapter(getActivity());
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(getContext()));
        rView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new SwipeCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rView);

        Button createButton = (Button) rootView.findViewById(R.id.new_conversation_button);
        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment df = new DialogFragment() {

                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstance) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        View view = getActivity().getLayoutInflater().inflate(
                                R.layout.new_message_dialog_view,
                                null,
                                false);

                        builder.setView(view);

                        ArrayAdapter<String> possibleUsers =
                                new UsersArrayAdapter(getContext(), R.layout.drop_down_view);
                        populatePossibleUsers(possibleUsers);

                        AutoCompleteTextView username = (AutoCompleteTextView)
                                view.findViewById(R.id.other_user);
                        username.setAdapter(possibleUsers);

                        EditText message = (EditText) view.findViewById(R.id.initial_message);
                        // TODO enable / disable ok button based on this or equivalent

                        ArrayAdapter<SavedSchemeModel> possibleSchemes =
                                new SchemesArrayAdapter(getContext(), R.layout.drop_down_view);
                        populatePossibleSchemes(possibleSchemes);

                        Spinner chooseScheme = (Spinner) view.findViewById(R.id.new_message_scheme_drop_down);
                        chooseScheme.setAdapter(possibleSchemes);
                        // TODO add on item selected listener ??

                        builder.setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        // TODO
                                    }
                                });

                        builder.setNegativeButton(android.R.string.cancel, null);

                        return builder.create();
                    }
                };

                df.show(getActivity().getSupportFragmentManager(), "new conversation");
            }
        });

        return rootView;
    }

    private void populatePossibleUsers(final ArrayAdapter<String> possibleUsers) {

        Firebase usersRef = new Firebase(Constants.FIREBASE_USERS_URL);
        usersRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                UserModel user = dataSnapshot.getValue(UserModel.class);
                user.setKey(dataSnapshot.getKey());

                possibleUsers.add(user.getUsername());

                possibleUsers.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // not used
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                possibleUsers.remove(user.getUsername());

                possibleUsers.notifyDataSetChanged();
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

    private class UsersArrayAdapter extends ArrayAdapter<String> {

        public UsersArrayAdapter(Context context, int resource) {

            super(context, resource);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView user = (TextView) getLayoutInflater(null)
                    .inflate(R.layout.drop_down_view, parent, false);

            user.setText(getItem(position));

            return user;
        }
    }

    private void populatePossibleSchemes(final ArrayAdapter<SavedSchemeModel> possibleSchemes) {

        Firebase schemesRef = new Firebase(Constants.FIREBASE_SCHEMES_URL);
        Query mySchemes = schemesRef.orderByChild("uid").equalTo(schemesRef.getAuth().getUid());
        mySchemes.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);
                scheme.setKey(dataSnapshot.getKey());

                possibleSchemes.add(scheme);

                possibleSchemes.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // not used - shouldn't be relevant
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);

                possibleSchemes.remove(scheme);

                possibleSchemes.notifyDataSetChanged();
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

    private class SchemesArrayAdapter extends ArrayAdapter<SavedSchemeModel> {

        public SchemesArrayAdapter(Context context, int resource) {

            super(context, resource);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView schemeName = (TextView) getLayoutInflater(null)
                    .inflate(R.layout.drop_down_view, parent, false);

            schemeName.setText(getItem(position).getName());

            return schemeName;
        }
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

        mListener = null;
    }

    /**
     *
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
