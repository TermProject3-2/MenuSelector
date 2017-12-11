package com.example.amdin.menuselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.amdin.menuselector.myRecycler.Contact;
import com.example.amdin.menuselector.myRecycler.ContactsAdapter;
import com.example.amdin.menuselector.myRecycler.MarginItemDecoration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LikeFoodListActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference myRef;
    private int menuCount;
    private String[] preference;
    private ArrayList<Contact> contacts;
    private ContactsAdapter adapter;
    private String id;
    private RecyclerView likeFoodContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_food_list);

        likeFoodContacts = (RecyclerView) findViewById(R.id.likeFoodContacts);
        mFirebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                preference = new String[menuCount];

                for (int i = 0; i < menuCount; i++) {
                    if (dataSnapshot.child("UserList").child(id).child("preference").child("" + i).exists()) {
                        preference[i] = dataSnapshot.child("UserList").child(id).child("preference").child("" + i).getValue().toString();
                    } else {
                        preference[i] = null;
                    }

                    if (dataSnapshot.child("menu" + i).exists()) { //메뉴 키가 존재할때만 데이터생성
                        int menuNum = Integer.parseInt(dataSnapshot.child("menu" + i).child("MenuNumber").getValue().toString());
                        String menuName = dataSnapshot.child("menu" + i).child("MenuName").getValue().toString();
                        String imageURI = dataSnapshot.child("menu" + i).child("ImageURI").getValue().toString();
                        int likeNum = Integer.parseInt(dataSnapshot.child("menu" + i).child("LikeNum").getValue().toString());
                        int price = Integer.parseInt(dataSnapshot.child("menu" + i).child("Price").getValue().toString());

                        if (preference[i] != null)
                            extractionImageFromStorage(i, menuName, imageURI, "Like", likeNum, price, getApplicationContext());
                        Log.d("Data Change for oneTime", "Success to read value.");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Data Change for oneTime", "Failed to read value.");
            }
        });

    }


    public void extractionImageFromStorage(final int menuNum, final String menuName, String imageURI, final String preference, final int likeNum, final int price, final Context context) {

        final DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);

        StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl
                (imageURI);

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Image load", "getBytes Success");

                Bitmap bmp;

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

                // 비트맵이 생성된게 먼저 contacts안에 들어갈경우 실제 contact의 포지션과 생성하려는 순서의 문제가 생기기 때문에 set메소드를 사용
                if (contacts == null)
                    contacts = new ArrayList<Contact>();

                Contact contact = new Contact(menuName, preference, resizedBmp, likeNum, price, menuNum);
                contacts.add(contact);

                if (adapter == null)
                    adapter = new ContactsAdapter(context, contacts, id, false);

                likeFoodContacts.setAdapter(adapter);
                StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                likeFoodContacts.setLayoutManager(gridLayoutManager);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("extraction : Image load", "getBytes Failed");
            }
        });
    }
}