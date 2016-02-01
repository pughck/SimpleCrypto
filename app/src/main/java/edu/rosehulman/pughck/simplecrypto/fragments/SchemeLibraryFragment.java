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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.Constants;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.adapters.SchemeLibraryAdapter;
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;

/**
 *
 */
public class SchemeLibraryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private SchemeLibraryAdapter mAdapter;

    public SchemeLibraryFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_scheme_library, container, false);

        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.scheme_library_recycler_view);
        mAdapter = new SchemeLibraryAdapter(getActivity());
        rView.setAdapter(mAdapter);
        rView.setLayoutManager(new LinearLayoutManager(getContext()));
        rView.setHasFixedSize(true);

        Button createButton = (Button) rootView.findViewById(R.id.create_scheme_button);
        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // dialog fragment with custom view to create scheme
                Log.d("TTT", "create scheme button clicked");

                DialogFragment df = new DialogFragment() {

                    @NonNull
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstance) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        View view = getActivity().getLayoutInflater().inflate(
                                R.layout.create_scheme_dialog_view,
                                null,
                                false);

                        builder.setView(view);

                        final EditText name = (EditText) view.findViewById(R.id.create_scheme_name);

                        final TextView key1Prompt = (TextView) view.findViewById(R.id.create_scheme_key1_prompt);
                        final TextView key2Prompt = (TextView) view.findViewById(R.id.create_scheme_key2_prompt);

                        final EditText key1 = (EditText) view.findViewById(R.id.create_scheme_key1);
                        final EditText key2 = (EditText) view.findViewById(R.id.create_scheme_key2);

                        key1Prompt.setVisibility(View.INVISIBLE);
                        key2Prompt.setVisibility(View.INVISIBLE);
                        key1.setVisibility(View.INVISIBLE);
                        key2.setVisibility(View.INVISIBLE);

                        final List<String> entries = new ArrayList<>();

                        final Spinner choose = (Spinner) view.findViewById(R.id.create_scheme_drop_down);
                        choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {
                                    key1Prompt.setVisibility(View.VISIBLE);
                                    key1Prompt.setText(R.string.key1_prompt_caesar);
                                    key1.setVisibility(View.VISIBLE);

                                    key2Prompt.setVisibility(View.INVISIBLE);
                                    key2.setVisibility(View.INVISIBLE);
                                } else {
                                    key1Prompt.setVisibility(View.VISIBLE);
                                    key1Prompt.setText(R.string.key1_prompt_affine);
                                    key1.setVisibility(View.VISIBLE);

                                    key2Prompt.setVisibility(View.VISIBLE);
                                    key2Prompt.setText(R.string.key2_prompt_affine);
                                    key2.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                                // does nothing
                            }
                        });

                        builder.setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String nameVal = name.getText().toString();
                                        String typeVal = choose.getSelectedItem().toString().toLowerCase();
                                        String key1Val = key1.getText().toString();
                                        String key2Val = key2.getText().toString();

                                        SavedSchemeModel newModel = new SavedSchemeModel();
                                        newModel.setName(nameVal);
                                        newModel.setType(typeVal);
                                        newModel.setKey1(key1Val);
                                        newModel.setKey2(key2Val);

                                        Firebase firebase = new Firebase(Constants.FIREBASE_SCHEMES_URL);

                                        newModel.setUid(firebase.getAuth().getUid());

                                        firebase.push().setValue(newModel);
                                    }
                                });

                        builder.setNegativeButton(android.R.string.cancel, null);

                        return builder.create();
                    }
                };

                df.show(getActivity().getSupportFragmentManager(), "create scheme");
            }
        });

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
