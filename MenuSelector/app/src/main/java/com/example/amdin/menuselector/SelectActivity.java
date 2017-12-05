package com.example.amdin.menuselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class SelectActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef,myLikeRef;
    private FirebaseStorage mFirebaseStorage;
    private Image mImg;
    private String id;
    private Bitmap bmp;
    private TextView textPrice , textFoodName;
    private String foodPrice, foodName, foodImgUri,preference;
    private Button selectbtn;
    private int foodLikeNum;

    private boolean selectFlag = true;
    private Vector<Integer> likeMenuNum;
    private HashMap<String,Integer> likeMenuHash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        textFoodName = (TextView)findViewById(R.id.textFoodName);
        textPrice = (TextView)findViewById(R.id.textPrice);
        selectbtn = (Button)findViewById(R.id.selectbtn);


        likeMenuHash = new HashMap<>();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");
        myLikeRef = firebaseDatabase.getReference("MenuList").child("UserList");



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
            }
        });
    }




    public void onSelectClick(View v){

        myLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likeMenuNum = new Vector<>();
                likeMenuHash = (HashMap<String,Integer>)dataSnapshot.child(id).child("preference").getValue();
                Set keySet = likeMenuHash.keySet();
                Iterator<?> it = likeMenuHash.keySet().iterator();
                while(it.hasNext())
                    likeMenuNum.add(likeMenuHash.get(it.next()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int rannum = likeMenuNum.get(((int)Math.random()*likeMenuNum.size()));
                foodName = dataSnapshot.child("menu"+rannum).child("MenuName").getValue().toString();
                foodPrice = dataSnapshot.child("menu"+rannum).child("MenuPrice").getValue().toString();
                foodImgUri = dataSnapshot.child("menu"+rannum).child("ImageURI").getValue().toString();
                foodLikeNum = Integer.parseInt(dataSnapshot.child("menu"+rannum).child("LikeNum").getValue().toString());
                extractionImageFromStorage(foodName, foodImgUri,"Like",foodLikeNum,getApplicationContext());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        textFoodName.setText(foodName);
        textPrice.setText(foodPrice);
        if(selectFlag) {
            selectbtn.setText("reselect");
            selectFlag = false;
        }
    }
}
