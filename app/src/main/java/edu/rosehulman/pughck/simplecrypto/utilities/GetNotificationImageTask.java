package edu.rosehulman.pughck.simplecrypto.utilities;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class GetNotificationImageTask extends AsyncTask<String, Void, Bitmap> {

    private NotificationImageCallback mCallback;

    private Notification.Builder mNotification;

    public GetNotificationImageTask(NotificationImageCallback callback,
                                    Notification.Builder notification) {

        mCallback = callback;

        mNotification = notification;
    }

    @Override
    protected Bitmap doInBackground(String... urlStrings) {

        String urlString = urlStrings[0];

        InputStream in = null;
        try {
            in = new java.net.URL(urlString).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(in);
    }

    @Override
    protected void onPostExecute(Bitmap image) {

        super.onPostExecute(image);

        mCallback.setNotificationImage(image, mNotification);
    }

    public interface NotificationImageCallback {

        void setNotificationImage(Bitmap image, Notification.Builder notification);
    }
}
