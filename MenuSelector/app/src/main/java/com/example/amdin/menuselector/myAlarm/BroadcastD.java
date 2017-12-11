package com.example.amdin.menuselector.myAlarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.amdin.menuselector.LoginActivity;
import com.example.amdin.menuselector.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Vector;

/**
 * Created by owner on 2017-11-27.
 */

public class BroadcastD extends BroadcastReceiver {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage mFirebaseStorage;
    private String id;
    private Bitmap resizedBmp;
    private String alarmText;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationmanager;
    private String foodName;
    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");
        id = intent.getStringExtra("id");
        notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.eat2)
                .setContentTitle("학식 뭐 먹지?")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                alarmText = dataSnapshot.child("UserList").child(id).child("alarmtext").getValue().toString();
                if (alarmText.equals("")) {
                    Vector<String> likeMenuNum = new Vector<>();
                    for (int i = 0; i < menuCount; i++) {
                        if (dataSnapshot.child("UserList").child(id).child("preference").child("" + i).exists()) {
                            likeMenuNum.add("menu" + i);
                        }
                    }
                    String rannum = likeMenuNum.get((int) (Math.random() * likeMenuNum.size()));
                    foodName = dataSnapshot.child(rannum).child("MenuName").getValue().toString();
                    String foodImgUri = dataSnapshot.child(rannum).child("ImageURI").getValue().toString();
                    extractionImageFromStorage(foodName, foodImgUri);
                }
                else{
                    mBuilder.setContentText(alarmText);
                    notificationmanager.notify(0, mBuilder.build());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void extractionImageFromStorage(final String menuName, String imageURI) {
        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl
                (imageURI);

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Image load", "getBytes Success");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                options.inSampleSize = 1;
                options.inJustDecodeBounds = false;

                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                resizedBmp = Bitmap.createScaledBitmap(bmp, 50  , 50, true);
                mBuilder.setContentText("오늘은 '" + foodName + "' 먹는 날~")
                        .setLargeIcon(resizedBmp);
                notificationmanager.notify(0, mBuilder.build());
            }
        });

    }
}
