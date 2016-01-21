package edu.rosehulman.pughck.simplecrypto.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.User;

/**
 *
 */
public class CreateAccountFragment extends Fragment {

    private CreateAccountListener mListener;

    public CreateAccountFragment() {

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
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof CreateAccountListener) {
            mListener = (CreateAccountListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();

        mListener = null;
    }

    /**
     *
     */
    public interface CreateAccountListener {

        void onCreateAccount(User user);
    }
}
