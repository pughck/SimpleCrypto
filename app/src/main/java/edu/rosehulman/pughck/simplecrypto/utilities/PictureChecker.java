package edu.rosehulman.pughck.simplecrypto.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Links used: <br/>
 * <a>http://stackoverflow.com/questions/6343166/android-os-networkonmainthreadexception</a>
 * <br/>
 * <a>http://stackoverflow.com/questions/3453641/detect-if-specified-url-is-an-image-in-android</a>
 *
 *
 * Created by gateslm on 2/13/2016.
 */
public class PictureChecker extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        String picture = params[0];
        try {
            URLConnection connection = new URL(picture)
                    .openConnection();
            String contentType = connection.getHeaderField
                    ("Content-Type");
            return contentType.startsWith("image/");
//            Log.d("PICTURE_URL", "valid picture");
        } catch (IOException e) {
            Log.d("PICTURE_URL", "Not valid picture");
            return false;
        }
    }
}
