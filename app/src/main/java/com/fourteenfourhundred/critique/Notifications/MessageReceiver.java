package com.fourteenfourhundred.critique.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.fourteenfourhundred.critique.UI.Activities.HomeActivity;
import com.fourteenfourhundred.critique.critique.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessageReceiver extends FirebaseMessagingService {

    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 6578;

    public MessageReceiver() {
        super();
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

       //
        // Log.e("GOT NOT","GOT NOTE");

        NotificationHandler.newPost(this,data.get("title"), data.get("body"));
    }


}
