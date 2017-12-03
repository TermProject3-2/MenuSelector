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
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    String id,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        pass = intent.getStringExtra("pass");
        Log.d("sdjang","before");
        AlarmHATT alarmHATT =  new AlarmHATT(getApplicationContext(),id);
        alarmHATT.Alarm();
        Toast.makeText(this, "dd", Toast.LENGTH_SHORT).show();
        Log.d("sdjang","after");




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
        startActivity(intent);
    }

    //학식설정 버튼
    public void onButtonFoodSetting(View v){
        Intent intent = new Intent(getApplicationContext(),FoodSettingActivity.class);
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
                        Log.d("sdjang","alarm on");
                        alarmOnoff = true;

                    }

                    else {
                        Log.d("sdjang","alarm off");
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
            Log.d("sdjang","Alarm() in");
            if(alarmOnoff) {
                Log.d("sdjang","alarmOnoff==true");
                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intentalarm = new Intent(MainActivity.this, BroadcastD.class);

                PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intentalarm, 0);
                Calendar calendar = Calendar.getInstance();
                //알람시간 calendar에 set해주기

                //calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 3, 1, 0);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), alarmhours, alarmmin, 0);
                Log.d("sdjang","alarm set");
                Toast.makeText(getApplicationContext(), "alarmsetting hours = "+ alarmhours + "min = " +alarmmin, Toast.LENGTH_LONG).show();
                //알람 예약
                am.cancel(sender);

                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                am.cancel(sender);
            }
        }
    }

}
