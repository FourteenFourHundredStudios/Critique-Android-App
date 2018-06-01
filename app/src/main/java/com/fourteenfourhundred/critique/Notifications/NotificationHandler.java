package com.fourteenfourhundred.critique.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;

import java.util.Random;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class NotificationHandler {

    public static void makeChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Critique";
            String description = "Critique notifications";
            NotificationChannel channel = new NotificationChannel("critique_notifications", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setLightColor(R.color.colorPrimary);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static void newPost(Context context, String title, String content){
        makeChannel(context);

        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);



        NotificationCompat.Builder notification = new NotificationCompat.Builder(context,"critique_notifications");
        notification.setSmallIcon(R.drawable.arrow_up)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_up))
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_VIBRATE | DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH);

        notification.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }

}
