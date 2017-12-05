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
    private Bitmap bmp,resizedBmp;
    private TextView textPrice , textFoodName, textFoodLikeNum;
    private String foodPrice, foodName, foodImgUri,preference ;
    private Button selectbtn;
    private int foodLikeNum;

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
        Log.d("sdjang","button click!");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("sdjang","get Key get Key");
                int menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                likeMenuNum = new Vector<>();
                for(int i=0; i<menuCount; i++){
                    if(dataSnapshot.child("UserList").child(id).child("preference").child(""+i).exists())
                        likeMenuNum.add("menu"+i);
                }
                String rannum = likeMenuNum.get((int)(Math.random()*likeMenuNum.size()));
                foodName = dataSnapshot.child(rannum).child("MenuName").getValue().toString();
                foodPrice = dataSnapshot.child(rannum).child("Price").getValue().toString();
                foodImgUri = dataSnapshot.child(rannum).child("ImageURI").getValue().toString();
                foodLikeNum = Integer.parseInt(dataSnapshot.child(rannum).child("LikeNum").getValue().toString());
                extractionImageFromStorage(foodName, foodImgUri,"Like",foodLikeNum,getApplicationContext());

                textFoodName.setText(foodName);
                textPrice.setText(foodPrice);
                textFoodLikeNum.setText(foodLikeNum+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        try {
            Thread.sleep(2000);
        }catch(InterruptedException e){

        }
        */
        //textPrice.setText(foodPrice);

        if(selectFlag) {
            selectbtn.setText("reselect");
            selectFlag = false;
        }
    }
}
