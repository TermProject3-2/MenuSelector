package com.example.amdin.menuselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewAnimator;

public class SettingActivity extends AppCompatActivity {
    Switch switchAlarm;
    TimePicker timePicker;
    LinearLayout onoffLayout,timePickerLayout,messageOnoffLayout,alarmMessageSetLayout;
    EditText editMesageView;
    TextView alarmMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        timePicker = (TimePicker)findViewById(R.id.timePicker);
        switchAlarm = (Switch)findViewById(R.id.switchAlarm);
        onoffLayout = (LinearLayout)findViewById(R.id.onOffLayout);
        timePickerLayout = (LinearLayout)findViewById(R.id.timePickerLayout);
        messageOnoffLayout = (LinearLayout)findViewById(R.id.messageOnoffLayout);
        alarmMessageSetLayout = (LinearLayout)findViewById(R.id.alarmMessageSetLayout);
        editMesageView = (EditText)findViewById(R.id.editMesageView);
        alarmMessage = (TextView)findViewById(R.id.AlarmMessage);

        timePickerLayout.setVisibility(View.GONE);
        alarmMessageSetLayout.setVisibility(View.GONE);

        //onoffLayout.bringToFront();
        onoffLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timePickerLayout.getVisibility()==View.GONE) {
                    //Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                    //timePicker.startAnimation(anim);
                    timePickerLayout.setVisibility(View.VISIBLE);
                }
                else {
                    timePickerLayout.setVisibility(View.GONE);
                    //Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                    //timePicker.setAnimation(anim);

                }
            }
        });
        messageOnoffLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmMessageSetLayout.getVisibility()==View.GONE)
                    alarmMessageSetLayout.setVisibility(View.VISIBLE);
                else
                    alarmMessageSetLayout.setVisibility(View.GONE);
            }
        });



    }
    public void timesettingBtn(View v){
        timePickerLayout.setVisibility(View.GONE);
    }
    public void onClickSetMessage(View v){
        String Message = editMesageView.getText().toString();
        alarmMessage.setText(Message);
        editMesageView.setText("");
        alarmMessageSetLayout.setVisibility(View.GONE);
    }
}
