package com.example.amdin.menuselector.myAlarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.amdin.menuselector.LoginActivity;
import com.example.amdin.menuselector.MainActivity;
import com.example.amdin.menuselector.R;

/**
 * Created by owner on 2017-11-27.
 */

public class BroadcastD extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i("sdjang","broadcast on");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
        .setContentTitle("Notification")
        .setContentText("Notification Message")
        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);
        Toast.makeText(context, "알람알람", Toast.LENGTH_SHORT).show();
       notificationmanager.notify(0,mBuilder.build());



        /* Notification.Builder builder = new Notification.Builder(context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.mipmap.ic_launcher).setTicker("HETT").setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setContentTitle("푸쉬 제목");
        builder.setContentText("푸쉬내용");
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        notificationmanager.notify(1, builder.build());
        */
    }
}
