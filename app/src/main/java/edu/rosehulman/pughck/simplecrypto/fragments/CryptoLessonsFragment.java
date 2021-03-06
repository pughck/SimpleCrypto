package edu.rosehulman.pughck.simplecrypto.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.pughck.simplecrypto.utilities.DocUtils;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.Doc;

/**
 *
 */
public class CryptoLessonsFragment extends Fragment {

    private List<Doc> mDocs;

    public CryptoLessonsFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_crypto_lessons, container, false);

        this.mDocs = DocUtils.loadDocs(rootView.getContext());

        List<Button> lessonButtons = new ArrayList<>();
        lessonButtons.add((Button) rootView.findViewById(R.id.affine_lessons));
        lessonButtons.add((Button) rootView.findViewById(R.id.caesar_lessons));
        lessonButtons.add((Button) rootView.findViewById(R.id.rsa_lessons));
        lessonButtons.add((Button) rootView.findViewById(R.id.vigenere_lessons));
        lessonButtons.add((Button) rootView.findViewById(R.id.inverse_lessons));

        for (int i = 0; i < lessonButtons.size(); i++) {
            final int index = i;

            Button b = lessonButtons.get(i);
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    launchDialogFragment(Html.fromHtml(mDocs.get(index).getText()));
                }
            });
        }

        return rootView;
    }

    private void launchDialogFragment(final Spanned spanned) {

        DialogFragment df = new DialogFragment() {

            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle b) {

                setRetainInstance(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(spanned);

                return builder.create();
            }

            @Override
            public void onDestroyView() {

                if (getDialog() != null && getRetainInstance()) {
                    getDialog().setDismissMessage(null);
                }

                super.onDestroyView();
            }
        };

        df.show(getFragmentManager(), "lessons");
    }
}
