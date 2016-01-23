package edu.rosehulman.pughck.simplecrypto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.pughck.simplecrypto.R;

/**
 *
 */
public class CryptoLessonsFragment extends Fragment {

    public CryptoLessonsFragment() {

        // Required empty public constructor
    }

    // TODO remove ??
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_crypto_lessons, container, false);

        // TODO

        return rootView;
    }
}
