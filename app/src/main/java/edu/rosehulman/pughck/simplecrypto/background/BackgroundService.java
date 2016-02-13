package edu.rosehulman.pughck.simplecrypto.background;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 *
 * https://gist.github.com/vikrum/6170193
 *
 *
 * helper methods.
 */
public class BackgroundService extends Service {

    private Firebase firebase = new Firebase(Constants.FIREBASE_URL);
    private ValueEventListener listener;

    public BackgroundService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifyUser(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(Constants.error, "Error in background services activity");
                Log.e(Constants.error, "Error message: " + firebaseError.getMessage());

            }
        };
        firebase.addValueEventListener(this.listener);
    }

    private void notifyUser(DataSnapshot dataSnapshot) {
        Log.d(Constants.background_service_message, "Got a snapshot");
        Log.d(Constants.background_service_message, dataSnapshot.toString());
    }
}
