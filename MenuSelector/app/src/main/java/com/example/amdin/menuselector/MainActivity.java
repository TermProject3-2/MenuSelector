package com.example.amdin.menuselector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.amdin.menuselector.myAlarm.BroadcastD;
import com.example.amdin.menuselector.myAlarm.BroadcastPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private String id;
    private Context context;
    private String alarmOnoff, alarmhours,alarmmin, htmlinfo, pageonchange;
    private String htmlPageUrl = "https://hansung.ac.kr/web/www/life_03_01_t1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
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
        intent.putExtra("pageonchange",pageonchange);
        startActivity(intent);

    }


    public class AlarmHATT {
        private Context context;
        private AlarmManager am,amMenuChange;
        private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        private DatabaseReference myRef= firebaseDatabase.getReference("MenuList");
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
                    htmlinfo = dataSnapshot.child("UserList").child("pageinfo").child("pagetext").getValue().toString();
                    pageonchange = dataSnapshot.child("UserList").child("pageinfo").child("pageonchange").getValue().toString();
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

                    amMenuChange = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intentalarm = new Intent(context,BroadcastPage.class);
                    intentalarm.putExtra("id",id);
                    intentalarm.putExtra("htmlinfo",htmlinfo);
                    PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar ccalendar = Calendar.getInstance();
                    Calendar calendar = (Calendar)ccalendar.clone();
                    calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),3,16, 0);
                    //calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),Calendar.HOUR_OF_DAY,Calendar.MINUTE, Calendar.SECOND+15);
                    if(ccalendar.after(calendar)){
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                    }
                    if(id.equals("test@gmail") || id.equals("vs@gmail")) {
                        amMenuChange.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, sender);
                    }
                    else
                        amMenuChange.cancel(sender);
                    /*
                    if(id.equals("test@gmail") || id.equals("vs@gmail")) {
                        if (dataSnapshot.child("UserList").child("pageinfo").child("pageonchange").getValue().toString().equals("changed"))
                            Toast.makeText(context, "학식 게시판이 업데이트 되었습니다. 확인 해 주세요", Toast.LENGTH_LONG).show();
                    }
                    */
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            }) ;

        }
    }

}