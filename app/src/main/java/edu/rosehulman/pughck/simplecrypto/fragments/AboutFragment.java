package edu.rosehulman.pughck.simplecrypto.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.rosehulman.pughck.simplecrypto.Constants;
import edu.rosehulman.pughck.simplecrypto.R;

/**
 *
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        Button send_feedback = (Button) view.findViewById(R.id.send_feedback_button);
        send_feedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // launch text dialog fragment and send email
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("message/rfc822");

                email.putExtra(Intent.EXTRA_EMAIL, Constants.send_to);
                email.putExtra(Intent.EXTRA_CC, Constants.cc_to);
                email.putExtra(Intent.EXTRA_BCC, Constants.bcc_to);

                email.putExtra(Intent.EXTRA_SUBJECT, Constants.feedback_subject);

                email.putExtra(Intent.EXTRA_TEXT, "replace with your feedback");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        return view;
    }
}
