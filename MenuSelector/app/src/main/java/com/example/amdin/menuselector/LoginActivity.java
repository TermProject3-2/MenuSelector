package com.example.amdin.menuselector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amdin.menuselector.myAlarm.BroadcastD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    TextView textId;
    TextView textPass;
    EditText editId;
    EditText editPass;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        textId = (TextView)findViewById(R.id.textId);
        textPass = (TextView)findViewById(R.id.textPass);
        editId = (EditText)findViewById(R.id.editId);
        editPass = (EditText)findViewById(R.id.editPass);
        new AlarmHATT(getApplicationContext()).Alarm();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("login success", "onAuthStateChanged: singned_in" + user.getUid());
                }else{
                    /*
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    Log.d("login failed", "onAuthStateChanged: singned_out");
                    */
                }

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener!= null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void onButtonLogin(View v){
        // 로그인 메소드 만들고 if문 추가할것!!!!!!
        String email = ((EditText)findViewById(R.id.editId)).getText().toString();
        String password = ((EditText)findViewById(R.id.editPass)).getText().toString();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                }
                else if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void onButtonSignUp(View v){
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent);
    }


    public class AlarmHATT {
        private Context context;
        private AlarmManager am;
        public AlarmHATT(Context context) {
            this.context = context;
        }

        public void Alarm() {
            am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(LoginActivity.this, BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(LoginActivity.this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 18, 19, 0);
            //알람 예약
            am.cancel(sender);

            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            am.cancel(sender);
        }
    }
}
