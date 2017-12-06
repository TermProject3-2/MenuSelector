package com.example.amdin.menuselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class SelectActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage mFirebaseStorage;
    private ImageView mImg;
    private String id;
    private Bitmap bmp;
    private TextView textPrice , textFoodName, textFoodLikeNum;
    private String foodPrice, foodName, foodImgUri, preName ;
    private Button selectbtn;
    private int foodLikeNum;
    private CheckBox checkLike;

    private boolean selectFlag = true;
    private Vector<String> likeMenuNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        textFoodName = (TextView)findViewById(R.id.select_foodName);
        textPrice = (TextView)findViewById(R.id.select_price);
        textFoodLikeNum = (TextView)findViewById(R.id.select_likenum);
        selectbtn = (Button)findViewById(R.id.select_selectbtn);
        mImg = (ImageView) findViewById(R.id.select_image);
        checkLike = (CheckBox)findViewById(R.id.select_likecheck);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");
        Toast.makeText(SelectActivity.this, id, Toast.LENGTH_SHORT).show();


    }

    public void extractionImageFromStorage(final String menuName, String imageURI, final String preference, final int likeNum, final Context context) {

        final DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl
                (imageURI);

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Image load", "getBytes Success");

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                float widthScale = options.outWidth / display.widthPixels;
                float heightScale = options.outHeight / display.heightPixels;
                float scale = widthScale > heightScale ? widthScale : heightScale;

                if (scale >= 8) {
                    options.inSampleSize = 8;
                } else if (scale >= 6) {
                    options.inSampleSize = 6;
                } else if (scale >= 4) {
                    options.inSampleSize = 4;
                } else if (scale >= 2) {
                    options.inSampleSize = 2;
                } else
                    options.inSampleSize = 1;
                options.inJustDecodeBounds = false;

                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, 270, 300, true);
                mImg.setImageBitmap(resizedBmp);
            }
        });
    }


    public void onSelectClick(View v){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                likeMenuNum = new Vector<>();
                if(checkLike.isChecked()) {
                    for (int i = 0; i < menuCount; i++) {
                        if (dataSnapshot.child("UserList").child(id).child("preference").child("" + i).exists()) {
                            likeMenuNum.add("menu" + i);
                        }
                    }
                    if(likeMenuNum.size()<3){
                        Toast.makeText(SelectActivity.this, "좋아요를 누른 메뉴가 3개 미만입니다. 조금만 더 눌러 주세요~", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(!checkLike.isChecked()){
                    for (int i = 0; i < menuCount; i++) {
                            likeMenuNum.add("menu" + i);
                    }
                }


                String rannum;
                while(true) {
                    rannum = likeMenuNum.get((int) (Math.random() * likeMenuNum.size()));

                    if(selectFlag || !rannum.equals(preName)) {
                        break;
                    }
                }
                foodName = dataSnapshot.child(rannum).child("MenuName").getValue().toString();
                foodPrice = dataSnapshot.child(rannum).child("Price").getValue().toString();
                foodImgUri = dataSnapshot.child(rannum).child("ImageURI").getValue().toString();
                foodLikeNum = Integer.parseInt(dataSnapshot.child(rannum).child("LikeNum").getValue().toString());
                extractionImageFromStorage(foodName, foodImgUri,"Like",foodLikeNum,getApplicationContext());

                try {
                    Thread.sleep(1300);
                }catch(InterruptedException e){

                }
                textFoodName.setText(foodName);
                textPrice.setText(foodPrice+"원");
                textFoodLikeNum.setText(foodLikeNum+"");
                preName = new String();
                preName = foodName.trim();
                if(selectFlag) {
                    selectbtn.setText("reselect");
                    selectFlag = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
