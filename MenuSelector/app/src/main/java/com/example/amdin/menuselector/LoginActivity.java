package com.example.amdin.menuselector;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("id",editId.getText().toString());
        intent.putExtra("pass",editPass.getText().toString());
        startActivity(intent);
    }

    public void onButtonSignUp(View v){
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent);
    }
}
