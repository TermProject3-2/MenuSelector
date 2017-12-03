package com.example.amdin.menuselector.MyMenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amdin.menuselector.AccessDB.ChangeDB;
import com.example.amdin.menuselector.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//manifest에서 팝업형식의 액티비티로 테마 지정
public class MenuDetailActivity extends AppCompatActivity {
    private TextView menuNameView;
    private TextView likeNumView;
    private ImageView menuImageView;
    private ImageButton preferenceButton;
    private Button recommandButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private int menuNum;

    private int menuPosition;
    private boolean recommandOnOf;
    private String menuName;
    private Bitmap menuImageBitMap;
    private String likeNum;
    private String preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        recommandOnOf = false;
        menuNum = 0; // menuNum 읽어오기 실패를 대비해서 일단 0으로 초기화
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");

        menuNameView = (TextView)findViewById(R.id.menuNameInDetail);
        likeNumView = (TextView)findViewById(R.id.likeNumInDeatail);
        menuImageView = (ImageView)findViewById(R.id.menuImageInDetail);
        preferenceButton = (ImageButton)findViewById(R.id.preferenceButton);
        recommandButton = (Button) findViewById(R.id.recommandButton);

        Intent intent = getIntent();

        menuPosition = Integer.parseInt(intent.getExtras().getString("MenuPosition"));
        menuName = intent.getExtras().getString("MenuName");
        menuImageBitMap = (Bitmap)intent.getParcelableExtra("MenuImageBitMap");
        likeNum = intent.getExtras().getString("LikeNum");
        preference = intent.getExtras().getString("Preference");

        menuNameView.setText(menuName);
        menuImageView.setImageBitmap(menuImageBitMap);
        likeNumView.setText(likeNum);

        if(preference.equals("Like"))
            preferenceButton.setImageResource(R.drawable.active_like_butoon);
        else
            preferenceButton.setImageResource(R.drawable.nomal_like_button);


        preferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(preference.equals("Nomal")) {
                    new ChangeDB().changeDataForDB(myRef, menuPosition, "x", "x", "x", "Like");
                    preference = "Like"; //preference 객체는 전달받은 복사값일 뿐 실제 DB의 preference의 레퍼런스가 아니다.
                }
                else {
                    new ChangeDB().changeDataForDB(myRef, menuPosition, "x", "x", "x", "Nomal");
                    preference = "Nomal";
                }
                if(preference.equals("Like"))
                    preferenceButton.setImageResource(R.drawable.active_like_butoon);
                else
                    preferenceButton.setImageResource(R.drawable.nomal_like_button);
            }
        });

        recommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recommandOnOf)
                    new ChangeDB().changeDataForDB(myRef, menuPosition, "x", "x", "x", "Like");

            }
        });



    }

}
