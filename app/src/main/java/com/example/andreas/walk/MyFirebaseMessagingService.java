package com.example.andreas.walk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.andreas.walk_eshop.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.example.andreas.walk.MyNotificationManager.NOTIFICATION_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static int counter = 0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        //  String Title = remoteMessage.getData().get("title").toString();

        // notifyUser(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());




            notifyUser(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), remoteMessage.getData().get("key"), remoteMessage.getData().get("value"), counter, remoteMessage.getData());

            Log.d("getData", remoteMessage.getData().toString());


    }


    public void notifyUser(String from, String notification,String key,String value,int counter , Map getData){

        Intent intent =new Intent (getApplicationContext(), Drawer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("key",key);
        intent.putExtra("value",value);


        MyNotificationManager myNotificationManager = new MyNotificationManager(getApplicationContext());
        myNotificationManager.showNotification(from, notification, key, value, counter, getData, intent);

    }




}
