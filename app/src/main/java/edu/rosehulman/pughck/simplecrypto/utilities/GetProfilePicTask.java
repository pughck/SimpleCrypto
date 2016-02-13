package edu.rosehulman.pughck.simplecrypto.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class GetProfilePicTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView mImageView;

    public GetProfilePicTask(ImageView imageView) {

        mImageView = imageView;
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

        mImageView.setImageBitmap(image);
    }
}
