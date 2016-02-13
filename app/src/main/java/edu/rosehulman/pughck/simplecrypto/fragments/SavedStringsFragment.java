package edu.rosehulman.pughck.simplecrypto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.utilities.SwipeCallback;
import edu.rosehulman.pughck.simplecrypto.adapters.SavedStringsAdapter;

/**
 *
 */
public class SavedStringsFragment extends Fragment {

    public SavedStringsFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_saved_strings, container, false);

        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.saved_strings_recycler_view);
        SavedStringsAdapter adapter = new SavedStringsAdapter();
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(getContext()));
        rView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new SwipeCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rView);

        return rootView;
    }
}
