package edu.rosehulman.pughck.simplecrypto.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.adapters.SavedStringsAdapter;

/**
 *
 */
public class SavedStringsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private SavedStringsAdapter mAdapter;

    public SavedStringsFragment() {

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
        RecyclerView view = (RecyclerView) inflater
                .inflate(R.layout.fragment_saved_strings, container, false);
        mAdapter = new SavedStringsAdapter();
        view.setAdapter(mAdapter);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.setHasFixedSize(true);

        return view;
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
