package com.pkasemer.worshipcenterdowntown;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pkasemer.worshipcenterdowntown.HelperClasses.SharedPrefManager;
import com.pkasemer.worshipcenterdowntown.Models.User;

public class PushNotificationService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();

            final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Heads Up Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            User user = SharedPrefManager.getInstance(this).getUser();

            Notification.Builder notification =
                    new Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(user.getFname()+", "+ text)
                            .setStyle(new Notification.BigTextStyle()
                                    .bigText(user.getFname()+ ", "+ text))
                            .setSmallIcon(R.drawable.ic_worship_notification)
                            .setAutoCancel(true);
            int NOTIFICATION_ID = (int) System.currentTimeMillis();
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification.build());

        }

        super.onMessageReceived(remoteMessage);



    }
}
