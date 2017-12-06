package com.example.amdin.menuselector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private String id,pass;
    private HashMap<String, Object> preferenceMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        pass = intent.getStringExtra("pass");

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
        private int alarmhours, alarmmin;
        private boolean alarmOnoff;
        private String alarmText;
        private StringTokenizer st;
        String tid;
        public AlarmHATT(Context context,String id) {
            st = new StringTokenizer(id,".");
            this.context = context;
            tid = st.nextToken();
            /*
            myRef.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("UserList").child(tid).child("alarm").getValue().toString().equals("on")){
                        alarmOnoff = true;

                    }

                    else {
                        alarmOnoff = false;
                    }
                    alarmText = dataSnapshot.child("UserList").child(tid).child("alarmtext").getValue().toString();
                    alarmhours = Integer.parseInt(dataSnapshot.child("UserList").child(tid).child("alarmhours").getValue().toString());
                    alarmmin = Integer.parseInt(dataSnapshot.child("UserList").child(tid).child("alarmmin").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }



        public void Alarm() {
            //if(alarmOnoff) {

            try{
                Thread.sleep(300);
            }catch(InterruptedException e){

            }
                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intentalarm = new Intent(context,BroadcastD.class);
                intentalarm.putExtra("id",id);
                //id를 만들어서 넣어주쟈
                PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기

                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),
                        17,20,0);
                //calendar.set(alarmhours, alarmmin, 0);
                //알람 예약
                Log.d("sdjang",""+calendar.getTimeInMillis());
                Log.d("sdjang","Year = "+calendar.get(Calendar.YEAR));
                Log.d("sdjang","Month = "+ calendar.get(Calendar.MONTH));
                Log.d("sdjang","Day = "+ calendar.get(Calendar.DAY_OF_MONTH));
                Log.d("sdjang","hour = "+ calendar.get(Calendar.HOUR_OF_DAY));
                Log.d("sdjang","min = "+ calendar.get(Calendar.MINUTE));
                Log.d("sdjang","second = "+ calendar.get(Calendar.SECOND));
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

            //}
        }
    }

}
