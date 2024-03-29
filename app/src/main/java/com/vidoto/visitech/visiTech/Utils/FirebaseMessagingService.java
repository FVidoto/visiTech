package com.vidoto.visitech.visiTech.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.vidoto.visitech.visiTech.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public String deviceTokenFirebase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "uMe_Channel_1";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "uMe Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // retrieve title and notification body from index.js function through remote
        String from_sender_id = remoteMessage.getData().get("from_sender_id");
        String click_action = remoteMessage.getData().get("click_action");
        String notificationTitle = remoteMessage.getData().get("title");
        String notificationBody = remoteMessage.getData().get("body");


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setTicker("infbox.com.bd")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody);



        // clicking notification goes to sender profile
        Intent intent = new Intent(click_action);
        intent.putExtra("visitUserId", from_sender_id);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(pendingIntent); // end


        int mNotificationId = (int) System.currentTimeMillis();
        notificationManager.notify(mNotificationId, notificationBuilder.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        deviceTokenFirebase = s;
        // Do whatever you want with your token nowSDA
        // i.e. store it on SharedPreferences or DB
        // or directly send it to server
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }



}
