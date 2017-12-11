package com.example.amdin.menuselector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.amdin.menuselector.myAlarm.BroadcastD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private String id;
    private String alarmOnoff, alarmhours,alarmmin;
    private HashMap<String, Object> preferenceMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
<<<<<<< HEAD
        alarmOnoff = intent.getStringExtra("alarm");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("sdjang","onResume 실행");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef= firebaseDatabase.getReference("MenuList");
        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(alarmOnoff.equals("on")) {
                    alarmhours = dataSnapshot.child("UserList").child(id).child("alarmhours").getValue().toString();
                    alarmmin = dataSnapshot.child("UserList").child(id).child("alarmmin").getValue().toString();
                    while(true){
                        if(!alarmhours.equals("") && !alarmmin.equals(""))
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }) ;
        /*
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e){

        }
        */
        /*
        int i=0;
        while(i<50000){
            i++;
        }
        */

<<<<<<< HEAD

        
=======
>>>>>>> 500b8a0d38bef495a1e04be25b7f30c6d79dcace
=======
        Log.d("sdjang","reunme   alarmhour"+alarmhours);
        Log.d("sdjang","resume   alarmmin"+alarmmin);
>>>>>>> parent of db59219... JeongHaeCheol
        AlarmHATT alarmHATT =  new AlarmHATT(getApplicationContext(),id);

        alarmHATT.Alarm();
    }

    //학식 보기 버튼
    public void onButtonDisplay(View v){

        Intent intent = new Intent(getApplicationContext(),DisplayActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //학식 뽑기 버튼
    public void onButtonSelect(View v){
        Intent intent = new Intent(getApplicationContext(),SelectActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //학식설정 버튼
    public void onButtonFoodSetting(View v){
        Intent intent = new Intent(getApplicationContext(),LikeFoodListActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //환경설정 버튼
    public void onButtonSetting(View v) {
        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    public class AlarmHATT {
        private Context context;
        private AlarmManager am;
        private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        private DatabaseReference myRef= firebaseDatabase.getReference("MenuList");
        private StringTokenizer st;
        public AlarmHATT(Context context,String id) {
            this.context = context;
        }

        public void Alarm() {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef= firebaseDatabase.getReference("MenuList");
            myRef.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    alarmOnoff = dataSnapshot.child("UserList").child(id).child("alarm").getValue().toString();
                    alarmhours = dataSnapshot.child("UserList").child(id).child("alarmhours").getValue().toString();
                    alarmmin = dataSnapshot.child("UserList").child(id).child("alarmmin").getValue().toString();
                    if(alarmOnoff.equals("on")) {
                        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intentalarm = new Intent(context,BroadcastD.class);
                        intentalarm.putExtra("id",id);
                        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                        Calendar ccalendar = Calendar.getInstance();
                        Calendar calendar = (Calendar)ccalendar.clone();
                        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),Integer.parseInt(alarmhours), Integer.parseInt(alarmmin), 0);
                        if(ccalendar.after(calendar)){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24*60*60*1000, sender);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            }) ;

        }
    }

}
