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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.StringTokenizer;

public class LoginActivity extends AppCompatActivity {
    TextView textId;
    TextView textPass;
    EditText editId;
    EditText editPass;
    String email;
    String password;

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
        email = ((EditText)findViewById(R.id.editId)).getText().toString();
        password = ((EditText)findViewById(R.id.editPass)).getText().toString();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                }
                else if(task.isSuccessful()){

                    StringTokenizer st = new StringTokenizer(email,".");
                    email= st.nextToken();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("id",email);
                    startActivity(intent);

                }
            }
        });
    }
    public void onButtonSignUp(View v){
        Intent intent = new Intent(getApplicationContext(), SingUpActivity.class);
        startActivity(intent);
    }

}
