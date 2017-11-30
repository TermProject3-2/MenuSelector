package com.example.amdin.menuselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.amdin.menuselector.myRecycler.Contact;
import com.example.amdin.menuselector.myRecycler.ContactsAdapter;
import com.example.amdin.menuselector.myRecycler.MarginItemDecoration;
import com.example.amdin.menuselector.urIStorage.MenuImageURI;
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
import java.util.HashMap;

public class DisplayActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;
    private FirebaseStorage mFirebaseStorage;
    private MenuImageURI menuImageURI;
    private Bitmap bmp;
    private RecyclerView rvContacts;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        menuImageURI = new MenuImageURI();
        mFirebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        contacts = new ArrayList<Contact>();

        final DatabaseReference myRef = firebaseDatabase.getReference("MenuList");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int menuNum = Integer.parseInt(dataSnapshot.child("MenuNum").getValue().toString());
                // menuNum이 메뉴개수를 나타냄 메뉴를 추가함에 따라 menuNum 증가 를 구현해야함
                for(int i = 0; i < menuNum; i++) {
                    String menuName = dataSnapshot.child("menu" + i).child("MenuName").getValue().toString();
                    String imageURI = dataSnapshot.child("menu" + i).child("ImageURI").getValue().toString();
                    int likeNum = Integer.parseInt(dataSnapshot.child("menu" + i).child("LikeNum").getValue().toString());
                    extractionImageFromStorage(menuName, imageURI, likeNum, getApplicationContext());
                }
                Log.d("Data Chane:", "Success to read value.");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Data Chane:", "Failed to read value.");
            }
        });
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    */
       public Bitmap extractionImageFromStorage(final String menuName, String imageURI, final int likeNum, final Context context) {

           StorageReference storageRef = mFirebaseStorage.getReferenceFromUrl
                (imageURI);

           storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("Image load", "getBytes Success");
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                Contact contact = new Contact(menuName, true,
                        bmp, likeNum);

                contacts.add(contact);

                ContactsAdapter adapter = new ContactsAdapter(context, contacts);
                // adapter.notifyItemChanged(0);
                rvContacts.setAdapter(adapter);

                StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvContacts.setLayoutManager(gridLayoutManager);

                RecyclerView.ItemDecoration itemDecoration = new
                        MarginItemDecoration(4);
                rvContacts.addItemDecoration(itemDecoration);

                // optimizations if all item views are of the same height and width for significantly smoother scrolling:
                rvContacts.setHasFixedSize(true);

                adapter.notifyItemInserted(0);

                if(bmp != null)
                    Log.d("BMP instance", "BMP = NOT NULL");
            }
         }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Image load", "getBytes Failed");
                bmp = null;

            }
         });
           if(bmp == null)
               Log.d("BMP return", "BMP = NULL");

         return  bmp;
       }

    }
