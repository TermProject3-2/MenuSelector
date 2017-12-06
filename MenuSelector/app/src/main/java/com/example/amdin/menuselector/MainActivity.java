package com.example.amdin.menuselector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
        System.out.println("Main id t : " +id );

        Log.d("sdjang","MainActivity.onCreate");
        AlarmHATT alarmHATT =  new AlarmHATT(getApplicationContext(),id);
        alarmHATT.Alarm();
    }

    //학식 보기 버튼
    public void onButtonDisplay(View v){

        Intent intent = new Intent(getApplicationContext(),DisplayActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("pass", pass);
                startActivity(intent);
    }

    //학식 뽑기 버튼
    public void onButtonSelect(View v){
        Intent intent = new Intent(getApplicationContext(),SelectActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("pass", pass);
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
        intent.putExtra("pass", pass);
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
        }



        public void Alarm() {
            //if(alarmOnoff) {

            try{
                Thread.sleep(300);
            }catch(InterruptedException e){

            }
                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intentalarm = new Intent("com.example.amdin.menuselector.ALARM_START");
                //id를 만들어서 넣어주쟈
                PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intentalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기

                calendar.set( 11, 45, 0);
                //calendar.set(alarmhours, alarmmin, 0);

                //Toast.makeText(getApplicationContext(), "alarmsetting hours = "+ alarmhours + "min = " +alarmmin, Toast.LENGTH_LONG).show();
                //알람 예약
                am.cancel(sender);
                Log.d("sdjang","Alarm set");
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                am.cancel(sender);
            //}
        }
    }

}
