package com.example.amdin.menuselector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    TextView textId;
    TextView textPass;
    EditText editId;
    EditText editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textId = (TextView)findViewById(R.id.textId);
        textPass = (TextView)findViewById(R.id.textPass);
        editId = (EditText)findViewById(R.id.editId);
        editPass = (EditText)findViewById(R.id.editPass);
    }


    public void onButtonLogin(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("id",editId.getText().toString());
        intent.putExtra("pass",editPass.getText().toString());
        startActivity(intent);

    }
}
