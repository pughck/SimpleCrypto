package edu.rosehulman.pughck.simplecrypto.background;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.rosehulman.pughck.simplecrypto.MainActivity;
import edu.rosehulman.pughck.simplecrypto.R;
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
                Log.d(Constants.background_service_message, firebase.getAuth().getUid()+ "");
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
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        int icon = R.drawable.default_profile;
        Notification n = new Notification.Builder(this).setContentTitle("My message")
                .setContentText("Subject").setSmallIcon(R.drawable.default_profile)
                .setContentIntent(contentIntent).setAutoCancel(true).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0,n);
    }
}
