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
import android.widget.TextView;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.ciphers.CaesarCipher;
import edu.rosehulman.pughck.simplecrypto.ciphers.ICipher;
import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.BasicAlphabet;

/**
 *
 */
public class AboutFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AboutFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // TODO delete textview / encryption stuff
        TextView deleteThis = (TextView) view.findViewById(R.id.about_content);
        String temp = "Hello90";
        ICipher tempCipher = new CaesarCipher(1, new BasicAlphabet());
        String tempEncrypt = tempCipher.encrypt(temp) + "\n" + tempCipher.decrypt(tempCipher.encrypt(temp));
        deleteThis.setText(tempEncrypt);

        Button send_feedback = (Button) view.findViewById(R.id.send_feedback_button);
        send_feedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // send email
                Log.d("TTT", "clicked send feedback button");
            }
        });

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

//        mListener = null;
    }

    /**
     *
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}