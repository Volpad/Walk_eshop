package com.example.andreas.walk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.andreas.walk_eshop.R;

import java.util.Map;

public class MyNotificationManager {

    private Context ctx;

    public static final int NOTIFICATION_ID = 13548;
    public MyNotificationManager(Context ctx){
        this.ctx = ctx;
    }

    public void showNotification(String from, String notification,String key, String value,int counter, Map getData, Intent intent){





        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, counter, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        counter ++;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);

        Notification mNotification = builder
                .setSmallIcon(R.drawable.ic_notif)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentText(notification)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                .build();

        mNotification.flags |=Notification.FLAG_AUTO_CANCEL;

        Log.d("fromnotif ", from);
        Log.d("bodynotif ", notification);
        Log.d("KeyNotif ", key);
        Log.d("ValueNotif ", value);
        Log.d("dataNotif ", getData.toString());


        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setSmallIcon(R.drawable.ic_notif);
        builder.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher));


            notificationManager.notify(NOTIFICATION_ID, mNotification);



    }

}
