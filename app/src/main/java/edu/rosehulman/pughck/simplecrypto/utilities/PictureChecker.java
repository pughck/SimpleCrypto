package edu.rosehulman.pughck.simplecrypto.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
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
            boolean image = contentType.startsWith("image/");
            Log.d("PICTURE_URL", "valid picture");
            return image;
        } catch (IOException e) {
            return false;
        }
    }
}
