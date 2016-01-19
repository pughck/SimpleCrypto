package edu.rosehulman.pughck.simplecrypto.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.rosehulman.pughck.simplecrypto.Constants;
import edu.rosehulman.pughck.simplecrypto.R;

/**
 *
 */
public class MenuFragment extends Fragment {

    private View.OnClickListener mClickListener;

    public MenuFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof View.OnClickListener) {
            mClickListener = (View.OnClickListener) context;
        } else {
            Log.e(Constants.error, "context must be a click listener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();

        mClickListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        Button messaging = (Button) view.findViewById(R.id.messaging);
        messaging.setOnClickListener(mClickListener);

        Button writer = (Button) view.findViewById(R.id.writer);
        writer.setOnClickListener(mClickListener);

        Button scheme_library = (Button) view.findViewById(R.id.scheme_library);
        scheme_library.setOnClickListener(mClickListener);

        Button saved_strings = (Button) view.findViewById(R.id.saved_strings);
        saved_strings.setOnClickListener(mClickListener);

        Button lessons = (Button) view.findViewById(R.id.lessons);
        lessons.setOnClickListener(mClickListener);

        Button settings = (Button) view.findViewById(R.id.settings);
        settings.setOnClickListener(mClickListener);

        Button about = (Button) view.findViewById(R.id.about);
        about.setOnClickListener(mClickListener);

        return view;
    }
}
