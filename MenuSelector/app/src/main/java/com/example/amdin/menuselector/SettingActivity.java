package com.example.amdin.menuselector;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.amdin.menuselector.myAlarm.BroadcastPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.StringTokenizer;

public class SettingActivity extends AppCompatActivity {
    private Switch switchAlarm;
    private TimePicker timePicker;
    private LinearLayout onoffLayout,timePickerLayout,messageOnoffLayout,alarmMessageSetLayout;
    private EditText editMesageView;
    private TextView alarmMessage;
    private String id,pageonchange, htmlinfo,htmlget;
    private Button updatebtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String htmlPageUrl = "https://hansung.ac.kr/web/www/life_03_01_t1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        id = getIntent().getStringExtra("id");
        pageonchange = getIntent().getStringExtra("pageonchange");
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        switchAlarm = (Switch) findViewById(R.id.switchAlarm);
        onoffLayout = (LinearLayout) findViewById(R.id.onOffLayout);
        timePickerLayout = (LinearLayout) findViewById(R.id.timePickerLayout);
        messageOnoffLayout = (LinearLayout) findViewById(R.id.messageOnoffLayout);
        alarmMessageSetLayout = (LinearLayout) findViewById(R.id.alarmMessageSetLayout);
        editMesageView = (EditText) findViewById(R.id.editMesageView);
        alarmMessage = (TextView) findViewById(R.id.AlarmMessage);
        updatebtn = (Button)findViewById(R.id.setting_updatebtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");

        timePickerLayout.setVisibility(View.GONE);
        alarmMessageSetLayout.setVisibility(View.GONE);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("UserList").child(id).child("alarm").getValue().toString().equals("on")) {
                    switchAlarm.setChecked(true);
                } else {
                    switchAlarm.setChecked(false);
                }
                if (!dataSnapshot.child("UserList").child(id).child("alarmtext").getValue().toString().equals(""))
                    alarmMessage.setText(dataSnapshot.child("UserList").child(id).child("alarmtext").getValue().toString());
                pageonchange = dataSnapshot.child("UserList").child("pageinfo").child("pageonchange").getValue().toString();
                if( (id.equals("test@gmail") || id.equals("vs@gmail")) && pageonchange.equals("changed")){
                    updatebtn.setVisibility(View.VISIBLE);
                }
                htmlinfo = dataSnapshot.child("UserList").child("pageinfo").child("pagetext").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*
        stb = new StringBuilder();
        while(st.hasMoreTokens())
            stb.append(st.nextToken());
        id = stb.toString();
        */ //이건 이름 입력을 어떻게 데이터베이스에 할지~

        /*
        HashMap<String, Object> postv = new HashMap<String, Object>();
        for(int i = 0; i < 20; i++)
            postv.put("menu"+i, "Normal");
        postv.put("alarm","off");
        postv.put("alarmtext","");
        postv.put("alarmhours",0);
        postv.put("alarmmin",0);
        myRef.child("UserList").child(id).setValue(postv);
        */ //data 삽입용 코드

        switchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchAlarm.isChecked()) {
                    myRef.child("UserList").child(id).child("alarm").setValue("on");
                } else {
                    myRef.child("UserList").child(id).child("alarm").setValue("off");
                }
            }
        });


        onoffLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerLayout.getVisibility() == View.GONE) {
                    //Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                    //timePicker.startAnimation(anim);
                    timePickerLayout.setVisibility(View.VISIBLE);
                } else {
                    timePickerLayout.setVisibility(View.GONE);
                    //Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                    //timePicker.setAnimation(anim);
                }
            }
        });

        messageOnoffLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmMessageSetLayout.getVisibility() == View.GONE)
                    alarmMessageSetLayout.setVisibility(View.VISIBLE);
                else
                    alarmMessageSetLayout.setVisibility(View.GONE);
            }
        });

    }
    public void onUpdateClick(View v){
        updatebtn.setVisibility(View.GONE);
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

    }
    public void onClickLogout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent logoutIntent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(logoutIntent);
    }
    public void timesettingBtn(View v){
        timePickerLayout.setVisibility(View.GONE);
    }
    public void onClickSetMessage(View v){
        String message = editMesageView.getText().toString();
        alarmMessage.setText(message);
        myRef.child("UserList").child(id).child("alarmtext").setValue(message);
        editMesageView.setText("");
        alarmMessageSetLayout.setVisibility(View.GONE);
    }

    public void onClickTimeSetting(View v){

        myRef.child("UserList").child(id).child("alarmhours").setValue(timePicker.getHour());
        myRef.child("UserList").child(id).child("alarmmin").setValue(timePicker.getMinute());
        timePickerLayout.setVisibility(View.GONE);
         //이부분은 minsdk가 23 부터이기 때문에 최종본에서는 minsdk를 23 이상으로 할것
    }







    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();


                //테스트1
                Elements titles = doc.select("div.journal-content-article");
                htmlget = new String();
                System.out.println("---------------------setting----------------------");
                for (Element e : titles) {
                    System.out.println("title: " + e.text());
                    htmlget += e.text().trim() + "\n";
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(!htmlinfo.equals(htmlget) || pageonchange.equals("changed")) {
                System.out.println("htmlinfo = "+ htmlinfo);
                System.out.println("htmlget = "+ htmlget);
                System.out.println("in setting activity");
                myRef.child("UserList").child("pageinfo").child("pageonchange").setValue("unchanged");
                myRef.child("UserList").child("pageinfo").child("pagetext").setValue(htmlget);
            }
            Toast.makeText(SettingActivity.this, "급식표를 업데이트 했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
