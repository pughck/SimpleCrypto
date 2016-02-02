package edu.rosehulman.pughck.simplecrypto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.ciphers.ICipher;
import edu.rosehulman.pughck.simplecrypto.models.SavedSchemeModel;
import edu.rosehulman.pughck.simplecrypto.models.SavedStringModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

/**
 *
 */
public class CryptoWriterFragment extends Fragment {

    private List<SavedSchemeModel> mPossibleSchemes;
    private List<String> mPossibleSchemesNames;

    private boolean mEncrypt;

    public CryptoWriterFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mPossibleSchemes = new ArrayList<>();

        mPossibleSchemesNames = new ArrayList<>();
        populatePossibleSchemes();

        mEncrypt = true;
    }

    private void populatePossibleSchemes() {

        Firebase schemesRef = new Firebase(Constants.FIREBASE_SCHEMES_URL);
        Query mySchemes = schemesRef.orderByChild("uid").equalTo(schemesRef.getAuth().getUid());
        mySchemes.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);

                mPossibleSchemes.add(scheme);

                String name = scheme.getName();
                mPossibleSchemesNames.add(name);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // not used - shouldn't be relevant
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                SavedSchemeModel scheme = dataSnapshot.getValue(SavedSchemeModel.class);

                mPossibleSchemes.remove(scheme);

                String name = scheme.getName();
                mPossibleSchemesNames.remove(name);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_crypto_writer, container, false);

        final EditText input = (EditText) rootView.findViewById(R.id.top_input);
        final TextView output = (TextView) rootView.findViewById(R.id.bottom_output);

        // TODO fix how this is done
        final Spinner chooseScheme = (Spinner) rootView.findViewById(R.id.writer_drop_down);
        chooseScheme.setAdapter(new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mPossibleSchemesNames));

        ToggleButton encryptDecrypt = (ToggleButton) rootView.findViewById(R.id.writer_toggle_button);
        encryptDecrypt.setTextOn(getContext().getString(R.string.toggle_button_encrypt));
        encryptDecrypt.setTextOff(getContext().getString(R.string.toggle_button_decrypt));
        encryptDecrypt.setChecked(true);
        encryptDecrypt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mEncrypt = !mEncrypt;
            }
        });

        Button runCipher = (Button) rootView.findViewById(R.id.run_button);
        runCipher.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("TTT", mPossibleSchemesNames.toString());

                return;

                // grab text, enccrypt or decrypt, put in output view
                /*String inputText = input.getText().toString();

                String name = (String) chooseScheme.getSelectedItem();
                SavedSchemeModel scheme = mPossibleSchemes.get(mPossibleSchemesNames.indexOf(name));

                ICipher cipher = scheme.convertToCipher();

                String outputText;
                if (mEncrypt) {
                    outputText = cipher.encrypt(inputText);
                } else {
                    outputText = cipher.decrypt(inputText);
                }

                output.setText(outputText);*/
            }
        });

        Button store = (Button) rootView.findViewById(R.id.store_text_button);
        store.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Firebase savedStringRef = new Firebase(
                        Constants.FIREBASE_USERS_URL + "/" +
                                new Firebase(Constants.FIREBASE_URL).getAuth().getUid() +
                                Constants.FIREBASE_SAVED_STRINGS);

                String name = (String) chooseScheme.getSelectedItem();
                SavedSchemeModel scheme = mPossibleSchemes.get(mPossibleSchemesNames.indexOf(name));
                String encryptionKey = scheme.getKey();

                SavedStringModel string = new SavedStringModel();
                string.setString(output.getText().toString());
                string.setEncryption(encryptionKey);
            }
        });

        // TODO what is this for?
        Button export = (Button) rootView.findViewById(R.id.export_button);

        return rootView;
    }
}
