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
import com.example.amdin.menuselector.SelectActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Vector;

/**
 * Created by owner on 2017-11-27.
 */

public class BroadcastD extends BroadcastReceiver {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage mFirebaseStorage;
    String id;
    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");
        id = intent.getStringExtra("id");
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
/*
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                Vector<String> likeMenuNum = new Vector<>();
                for (int i = 0; i < menuCount; i++) {
                    if (dataSnapshot.child("UserList").child(id).child("preference").child("" + i).exists()) {
                        likeMenuNum.add("menu" + i);
                    }
                }

                String rannum = likeMenuNum.get((int) (Math.random() * likeMenuNum.size()));
                String foodName = dataSnapshot.child(rannum).child("MenuName").getValue().toString();
                String foodImgUri = dataSnapshot.child(rannum).child("ImageURI").getValue().toString();
                extractionImageFromStorage(foodName, foodImgUri,"Like",foodLikeNum,getApplicationContext());

                try {
                    Thread.sleep(1300);
                }catch(InterruptedException e){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */






        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
        .setContentTitle("학식 뭐 먹지?")
        .setContentText("Notification Message")
        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true);

       notificationmanager.notify(0,mBuilder.build());

    }
}
