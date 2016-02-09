package edu.rosehulman.pughck.simplecrypto;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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
import edu.rosehulman.pughck.simplecrypto.fragments.CreateAccountFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.CryptoLessonsFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.CryptoMessagingFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.LoginFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.CryptoWriterFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.MenuFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.SavedStringsFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.SchemeLibraryFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.SettingsFragment;
import edu.rosehulman.pughck.simplecrypto.models.UserModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.OnLoginListener,
        CreateAccountFragment.CreateAccountListener,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 1;

    private Firebase mFirebaseRef;
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

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        if (mFirebaseRef.getAuth() == null || isExpired(mFirebaseRef.getAuth())) {
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

    @Override
    public void onCreateAccount(final UserModel user, final String password) {

        // auth user and push user to firebase and login / go to main menu
        mFirebaseRef.createUser(user.getEmail(), password, new Firebase.ResultHandler() {

            @Override
            public void onSuccess() {

                // authenticate and switch to main menu
                mFirebaseRef.authWithPassword(user.getEmail(), password, new MyAuthResultHandler(user));
            }

            @Override
            public void onError(FirebaseError firebaseError) {

                showCreateAccountError(firebaseError.getMessage());
            }
        });
    }

    private void showCreateAccountError(String message) {

        CreateAccountFragment createAccountFragment = (CreateAccountFragment)
                getSupportFragmentManager()
                        .findFragmentByTag(Constants.create_account_fragment_tag);

        createAccountFragment.onCreateAccountError(message);
    }

    // For login fragment
    @Override
    public void onLogin(String email, String password) {

        // authenticate user
        mFirebaseRef.authWithPassword(email, password, new MyAuthResultHandler());
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
                if (account != null) {
                    Uri photo = account.getPhotoUrl();

                    UserModel user = new UserModel(account.getEmail(), "", "",
                            account.getDisplayName(),
                            photo != null ? photo.toString() : Uri.EMPTY.toString());

                    getGoogleOAuthToken(user);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getGoogleOAuthToken(final UserModel user) {

        AsyncTask<Void, Void, UserWithToken> task = new AsyncTask<Void, Void, UserWithToken>() {

            String errorMessage = null;

            @Override
            protected UserWithToken doInBackground(Void... params) {

                String token = null;
                try {
                    String scope = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(MainActivity.this, user.getEmail(), scope);
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

                return new UserWithToken(user, token);
            }

            @Override
            protected void onPostExecute(UserWithToken uwt) {

                if (uwt == null) {
                    showLoginError(errorMessage);
                } else {
                    onGoogleLoginWithToken(uwt);
                }
            }
        };

        task.execute();
    }

    private void onGoogleLoginWithToken(UserWithToken uwt) {

        // Log user in with Google OAuth Token
        mFirebaseRef.authWithOAuthToken(Constants.google,
                uwt.getToken(),
                new MyAuthResultHandler(uwt.getUser()));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(Constants.error, "Connection Failed");
    }

    private void showLoginError(String message) {

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentByTag(Constants.login_fragment_tag);

        loginFragment.onLoginError(message);
    }

    private class UserWithToken {

        private UserModel user;
        private String token;

        public UserWithToken(UserModel u, String t) {

            user = u;
            token = t;
        }

        public UserModel getUser() {

            return user;
        }

        public String getToken() {

            return token;
        }
    }

    class MyAuthResultHandler implements Firebase.AuthResultHandler {

        private UserModel user;

        public MyAuthResultHandler() {

            this(null);
        }

        public MyAuthResultHandler(UserModel u) {

            user = u;
        }

        @Override
        public void onAuthenticated(final AuthData authData) {

            // create user in forge if does not exists
            if (user != null) {
                final Firebase userRef = new Firebase(Constants.FIREBASE_USERS_URL
                        + "/" + authData.getUid());
                ValueEventListener userCheckListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() == null) {

                            // add user to firebase (push)
                            userRef.setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        // does nothing
                    }
                };

                userRef.addListenerForSingleValueEvent(userCheckListener);
            }

            // clear backstack
            int nEntries = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < nEntries; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }

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

    // For main menu fragment
    @Override
    public void onClick(View v) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (v.getId()) {

            case R.id.messaging:
                // messaging fragment
                fragment = new CryptoMessagingFragment();
                break;

            case R.id.writer:
                //writer fragment
                fragment = new CryptoWriterFragment();
                break;

            case R.id.scheme_library:
                // scheme library fragment
                fragment = new SchemeLibraryFragment();
                break;

            case R.id.saved_strings:
                //saved strings fragment
                fragment = new SavedStringsFragment();
                break;

            case R.id.lessons:
                // lessons fragment
                fragment = new CryptoLessonsFragment();
                break;

            case R.id.settings:
                // settings fragment
                fragment = new SettingsFragment();
                break;

            case R.id.about:
                // about fragment
                fragment = new AboutFragment();
                break;

            case R.id.logout:
                // logout  button
                mFirebaseRef.unauth();

                fragment = new LoginFragment();
                break;

            default:
                Log.e(Constants.error, "invalid main menu click");
                return;
        }

        ft.replace(R.id.fragment_container, fragment);
        if (v.getId() != R.id.logout) {
            ft.addToBackStack(Constants.menu_added);
        }
        ft.commit();
    }
}
