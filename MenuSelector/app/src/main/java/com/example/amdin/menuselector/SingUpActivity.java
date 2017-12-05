package com.example.amdin.menuselector;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingUpActivity extends AppCompatActivity {

    private TextView textId;
    private TextView textPass;
    private TextView textPassConfirm;
    private EditText editId;
    private EditText editPass;
    private EditText editConfirm;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private int menuCount;
    private String stId;
    private String stPass;
    private String stConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();
        textId = (TextView)findViewById(R.id.textId);
        textPass = (TextView)findViewById(R.id.textPass);
        textPassConfirm = (TextView)findViewById(R.id.textPassConfirm);
        editId = (EditText) findViewById(R.id.editId);
        editPass = (EditText) findViewById(R.id.editPass);
        editConfirm = (EditText) findViewById(R.id.editConfirm);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

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


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("MenuList");

        myRef.child("MenuCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = Integer.parseInt(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener!= null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void onButtonSignUp(View v){
        stId = editId.getText().toString();
        stPass = editPass.getText().toString();
        stConfirm = editConfirm.getText().toString();

        if(stId != "" && stPass != null) {
            if(stConfirm.equals(stPass))
                 createAccount(stId, stPass);
            else {
                Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다. ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean isValidPasswd(String target) {
        Pattern p = Pattern.compile("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)");

        Matcher m = p.matcher(target);
        if (m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
            return true;
        }else{
            return false;
        }
    }

    private boolean isValidEmail(String target) {
        if(target == null || TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    private void createAccount(String email, String password){
        if(!isValidEmail(email)){
            Log.e("createErrorEmail", "createAccount: 사용 불가능한 이메일 ");
            Toast.makeText(getApplicationContext(), "Email is not valid",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValidPasswd(password)){
            Log.e("createErrorPassword", "createAccount: 사용 불가능한 비밀번호 ");
            Toast.makeText(getApplicationContext(), "Password is not valid",
                    Toast.LENGTH_SHORT).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("create User", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef= firebaseDatabase.getReference("MenuList");

                            HashMap<String, Object> postv = new HashMap<String, Object>();
                            StringTokenizer st = new StringTokenizer(stId,".");
                            stId = st.nextToken();

                            for(int i = 0; i < menuCount; i++){
                                postv.put("alarm","off");
                                postv.put("alarmtext","");
                                postv.put("alarmhours",0);
                                postv.put("alarmmin",0);
                            }
                            myRef.child("UserList").child(stId).setValue(postv);

                            Toast.makeText(getApplicationContext(), "Authentication success",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(getParentActivityIntent());
                        }
                    }
                });
    }

}
