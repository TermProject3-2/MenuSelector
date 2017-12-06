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
    private String alarmOnoff, alarmhours,alarmmin;
    private HashMap<String, Object> preferenceMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
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
        int i=0;
        while(i<50000){
            i++;
        }

        Log.d("sdjang","reunme   alarmhour"+alarmhours);
        Log.d("sdjang","resume   alarmmin"+alarmmin);
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
        String tid;
        public AlarmHATT(Context context,String id) {
            st = new StringTokenizer(id,".");
            this.context = context;
            tid = st.nextToken();


        }



        public void Alarm() {
            if(alarmOnoff.equals("on")) {

                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intentalarm = new Intent(context,BroadcastD.class);
                intentalarm.putExtra("id",id);
                //id를 만들어서 넣어주쟈
                PendingIntent sender = PendingIntent.getBroadcast(context, 0, intentalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기

                long a, b;
                a = calendar.getTimeInMillis();
                Log.d("sdjang","alarm min= "+a);
                Log.d("sdjang","alarmhour"+alarmhours);
                Log.d("sdjang","alarmmin"+alarmmin);
                //calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),18,18,0);
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),Integer.parseInt(alarmhours), Integer.parseInt(alarmmin), 0);


                b = calendar.getTimeInMillis();
                Log.d("sdjang","after set = "+b);
                long c = b-a;
                Log.d("sdjang","getTimeMills - ="+c );//이게 양수가 나와야되

                //알람 예약
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),24*60*60*1000, sender);

            }
        }
    }

}
