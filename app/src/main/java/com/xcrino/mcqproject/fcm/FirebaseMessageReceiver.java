package com.xcrino.mcqproject.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.activity.HomeActivity;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //handle when receive notification via data event
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));

        }

        //handle when receive notification
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private RemoteViews getCustomDesign(String title, String message){
        RemoteViews remoteViews=new RemoteViews(getApplicationContext().getPackageName(), R.layout.notificatoin_layout);
        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.message,message);
        remoteViews.setImageViewResource(R.id.icon,R.drawable.logo_white);
        return remoteViews;

    }

    public void showNotification(String title, String message) {
//        Intent intent = new Intent(this, HomeActivity.class);
//        String channel_id = "Quizsthan";
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
//                .setAutoCancel(true).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setOnlyAlertOnce(true).setContentIntent(pendingIntent);
//
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
////            builder=builder.setContent(getCustomDesign(title,message));
//        }
//        else{
//            builder=builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.logo_white);
//        }
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Quizsthan", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setSound(uri, null);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        notificationManager.notify(0, builder.build());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "Quizsthan");
        Intent ii = new Intent(getApplicationContext(), HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText(verseurl);
        bigText.setBigContentTitle(title);
//        bigText.setSummaryText(message);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.logo_white);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Quizsthan";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    title,
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }
}
