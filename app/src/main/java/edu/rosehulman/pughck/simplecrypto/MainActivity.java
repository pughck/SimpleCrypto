package edu.rosehulman.pughck.simplecrypto;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

import edu.rosehulman.pughck.simplecrypto.fragments.AboutFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.LoginFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.OnLoginListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            TextView abTv = (TextView) findViewById(R.id.action_bar_text);
            abTv.setText(Html.fromHtml(getString(R.string.title_bar)));
        } else {
            Log.e(Constants.error, "Problem finding the action bar");
        }

        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);

        // TODO remove and add logout
        firebase.unauth();

        if (firebase.getAuth() == null || isExpired(firebase.getAuth())) {
            // go to login
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new LoginFragment(), Constants.login_fragment_tag);
            ft.commit();
        } else {
            // go to main menu
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new MenuFragment());
            ft.commit();
        }
    }

    private boolean isExpired(AuthData authData) {

        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }

    // For login fragment
    @Override
    public void onLogin(String email, String password) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new MenuFragment());
        ft.commit();
    }

    @Override
    public void onGoogleLogin() {

        // Log user in with Google Account
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                String emailAddress = account.getEmail();

                getGoogleOAuthToken(emailAddress);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getGoogleOAuthToken(final String emailAddress) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {

                String token = null;
                try {
                    String scope = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(MainActivity.this, emailAddress, scope);
                } catch (IOException transientEx) {
                /* Network or server error */
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                /* We probably need to ask for permissions, so start the intent if there is none pending */
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, MainActivity.REQUEST_CODE_GOOGLE_SIGN_IN);
                } catch (GoogleAuthException authEx) {
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {

                if (token == null) {
                    showLoginError(errorMessage);
                } else {
                    onGoogleLoginWithToken(token);
                }
            }
        };

        task.execute();
    }

    private void onGoogleLoginWithToken(String oAuthToken) {

        // Log user in with Google OAuth Token
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.authWithOAuthToken(Constants.google, oAuthToken, new MyAuthResultHandler());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(Constants.error, "Connection Failed");
    }

    class MyAuthResultHandler implements Firebase.AuthResultHandler {

        @Override
        public void onAuthenticated(AuthData authData) {

            // switch to main menu fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new MenuFragment());
            ft.commit();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {

            showLoginError(firebaseError.getMessage());
        }
    }

    private void showLoginError(String message) {

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentByTag(Constants.login_fragment_tag);

        loginFragment.onLoginError(message);
    }

    // For main menu fragment
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.messaging:
                // messaging fragment
                Log.d("TTT", "messaging");
                return;

            case R.id.writer:
                //writer fragment
                Log.d("TTT", "writer");
                return;

            case R.id.scheme_library:
                // scheme library fragment
                Log.d("TTT", "scheme library");
                return;

            case R.id.saved_strings:
                //saved strings fragment
                Log.d("TTT", "saved strings");
                return;

            case R.id.lessons:
                // lessons fragment
                Log.d("TTT", "lessons");
                return;

            case R.id.settings:
                // settings fragment
                Log.d("TTT", "settings");
                return;

            case R.id.about:
                // about fragment
                Log.d("TTT", "about");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AboutFragment());
                ft.addToBackStack(Constants.menu_added);
                ft.commit();

                return;

            default:
                Log.e(Constants.error, "invalid main menu click");
        }
    }
}
