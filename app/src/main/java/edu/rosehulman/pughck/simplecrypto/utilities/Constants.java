package edu.rosehulman.pughck.simplecrypto.utilities;

/**
 * Created by pughck on 1/18/2016.
 */
public class Constants {

    public static final String error = "ERROR";

    public static final String login_added = "LOGIN FRAGMENT ADDED";
    public static final String menu_added = "MENU FRAGMENT ADDED";
    public static final String conversations_added = "MESSAGING FRAGMENT ADDED";

    public static final String login_fragment_tag = "login";
    public static final String create_account_fragment_tag = "create_account";

    public static final String FIREBASE_URL = "https://csse483-simple-crypto.firebaseio.com";
    public static final String FIREBASE_USERS_URL = FIREBASE_URL + "/users";
    public static final String FIREBASE_SCHEMES_URL = FIREBASE_URL + "/schemes";
    public static final String FIREBASE_SAVED_STRINGS = "/saved_strings";
    public static final String FIREBASE_USER_CONVERSATIONS = "/conversations";
    public static final String FIREBASE_CONVERSATIONS_URL = FIREBASE_URL + "/conversations";

    public static final String google = "google";

    public static final String feedback_subject = "SimpleCrypto Feedback";
    public static final String[] send_to = new String[]
            {"pughck@rose-hulman.edu", "gateslm@rose-hulman.edu"};
    public static final String[] cc_to = new String[]{};
    public static final String[] bcc_to = new String[]{};
}
