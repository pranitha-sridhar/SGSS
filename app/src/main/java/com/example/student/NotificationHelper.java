package com.example.student;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static void displayNotification(Context context, String title, String body) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, HomeM.CHANNEL_ID).setSmallIcon(R.drawable.ic_create).setContentTitle(title).setContentText(body).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());

    }
}
