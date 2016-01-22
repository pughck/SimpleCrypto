package edu.rosehulman.pughck.simplecrypto.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.User;

/**
 *
 */
public class CreateAccountFragment extends Fragment {

    private CreateAccountListener mListener;

    private EditText mPasswordView;
    private EditText mEmailView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mUsernameView;

    private View mCreateAccountForm;
    private View mProgressSpinner;

    private boolean mCreatingAccount;

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
        View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);

        mEmailView = (EditText) rootView.findViewById(R.id.email);
        mPasswordView = (EditText) rootView.findViewById(R.id.password);
        mFirstNameView = (EditText) rootView.findViewById(R.id.first_name);
        mLastNameView = (EditText) rootView.findViewById(R.id.last_name);
        mUsernameView = (EditText) rootView.findViewById(R.id.username);

        mCreateAccountForm = rootView.findViewById(R.id.create_account_form);
        mProgressSpinner = rootView.findViewById(R.id.create_account_progress);

        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == EditorInfo.IME_ACTION_NEXT) {
                    mPasswordView.requestFocus();

                    return true;
                }

                return false;
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == EditorInfo.IME_ACTION_NEXT) {
                    mFirstNameView.requestFocus();

                    return true;
                }

                return false;
            }
        });

        mFirstNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == EditorInfo.IME_ACTION_NEXT) {
                    mLastNameView.requestFocus();

                    return true;
                }

                return false;
            }
        });

        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == EditorInfo.IME_NULL) {
                    createAccount();

                    return true;
                }

                return false;
            }
        });

        final View createButton = rootView.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createAccount();
            }
        });

        return rootView;
    }

    public void createAccount() {

        if (mCreatingAccount) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String username = mUsernameView.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.invalid_password));
            focusView = mPasswordView;
            cancelLogin = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.field_required));
            focusView = mEmailView;
            cancelLogin = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.invalid_email));
            focusView = mEmailView;
            cancelLogin = true;
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
            mCreatingAccount = true;

            if (TextUtils.isEmpty(username)) {
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                    username = email.split("@")[0].trim();
                } else {
                    username = firstName + " " + lastName;
                }
            }

            User user = new User(email, firstName, lastName, username, Uri.EMPTY.toString());

            mListener.onCreateAccount(user, password);

            hideKeyboard();
        }
    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
    }

    public void onCreateAccountError(String message) {

        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.create_account_error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();

        mPasswordView.setText("");

        showProgress(false);

        mCreatingAccount = false;
    }

    private void showProgress(boolean show) {

        mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
        mCreateAccountForm.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
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

        void onCreateAccount(User user, String password);
    }
}