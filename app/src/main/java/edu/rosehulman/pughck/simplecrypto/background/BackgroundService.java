package edu.rosehulman.pughck.simplecrypto.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.rosehulman.pughck.simplecrypto.MainActivity;
import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.MessageModel;
import edu.rosehulman.pughck.simplecrypto.models.MessagesModel;
import edu.rosehulman.pughck.simplecrypto.models.UserModel;
import edu.rosehulman.pughck.simplecrypto.utilities.Constants;
import edu.rosehulman.pughck.simplecrypto.utilities.GetNotificationImageTask;

/**
 * https://gist.github.com/vikrum/6170193
 * http://developer.android.com/training/run-background-service/create-service.html
 */
public class BackgroundService extends Service implements GetNotificationImageTask.NotificationImageCallback {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }

    public BackgroundService() {

        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        Firebase.setAndroidContext(this);

        setUpMessagingListener(new Firebase(Constants.FIREBASE_URL).getAuth().getUid());
    }

    // listen on this users messages in conversations section to update notifications
    // then other listeners watch for notification count change and update UI accordingly
    private void setUpMessagingListener(final String myUid) {

        final Firebase userRef = new Firebase(Constants.FIREBASE_USERS_URL
                + "/" + myUid + Constants.FIREBASE_USER_CONVERSATIONS);
        userRef.addChildEventListener(new ChildEventListener() {

            private List<MessagesModel> mConversations = new ArrayList<>();

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                final MessagesModel messagesModel = dataSnapshot.getValue(MessagesModel.class);

                messagesModel.setKey(dataSnapshot.getKey());

                mConversations.add(messagesModel);

                // set up listener on actual conversation
                final Firebase conversationRef = new Firebase(Constants.FIREBASE_CONVERSATIONS_URL
                        + "/" + messagesModel.getConversation() + Constants.FIREBASE_MESSAGES_URL);

                final Set<String> currentMessageKeys = new HashSet<>();

                conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            currentMessageKeys.add(child.getKey());
                        }

                        conversationRef.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                // update notifications count if from other user
                                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                                message.setKey(dataSnapshot.getKey());

                                if (!(message.getUser().equals(myUid)
                                        || currentMessageKeys.contains(message.getKey()))) {

                                    messagesModel.incrementNotifications();

                                    userRef.child(messagesModel.getKey()).setValue(messagesModel);

                                    notifyUser(message.getUser(), message.getMessage());
                                }

                                currentMessageKeys.add(message.getKey());
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                // ignore
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                // ignore
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                // not used
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                                Log.e(Constants.error, firebaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        Log.e(Constants.error, firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();

                MessagesModel newMessagesModel = dataSnapshot.getValue(MessagesModel.class);

                for (MessagesModel messagesModel : mConversations) {
                    if (key.equals(messagesModel.getKey())) {
                        messagesModel.setValues(newMessagesModel);

                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();

                for (MessagesModel messagesModel : mConversations) {
                    if (key.equals(messagesModel.getKey())) {
                        mConversations.remove(messagesModel);

                        return;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                // not used;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });
    }


    private void notifyUser(String userUid, final String message) {

        Firebase userRef = new Firebase(Constants.FIREBASE_USERS_URL + "/" + userUid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                Context context = getApplicationContext();

                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                Notification.Builder notification = new Notification.Builder(BackgroundService.this)
                        .setContentTitle("New CryptoMessage")
                        .setContentText(user.getUsername() + ": " + message)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true);

                // service
                new GetNotificationImageTask(BackgroundService.this, notification)
                        .execute(user.getProfilePic());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.e(Constants.error, firebaseError.getMessage());
            }
        });
    }

    @Override
    public void setNotificationImage(Bitmap image, Notification.Builder notification) {

        notification.setLargeIcon(image);
        Notification n = notification.build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
