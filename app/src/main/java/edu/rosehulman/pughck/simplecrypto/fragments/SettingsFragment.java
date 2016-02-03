package edu.rosehulman.pughck.simplecrypto.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

/**
 *
 */
public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Firebase mFirebase;

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
        Log.d("USER", mFirebase.getAuth().getUid());
        Button changeNameButton = (Button) rootView.findViewById(R.id.user_change);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Log.d("CLICKED", "Clicked " + ((Button) v).getText().toString());
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
