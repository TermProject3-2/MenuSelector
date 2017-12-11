package com.example.amdin.menuselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

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

public class DisplayActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts;
    private FirebaseStorage mFirebaseStorage;
    private Bitmap bmp;
    private RecyclerView rvContacts;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private int menuCount;
    private ContactsAdapter adapter;

    private  String[] preference;
    private String id;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        mFirebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("MenuList");

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");
        pass = intent.getExtras().getString("pass");



        /*
        int pr = 3000;
        int menuC = 10;
        myRef.child("MenuCount").setValue(menuC+"")
        for(int i = 0; i < menuC; i++) {
            HashMap<String, Object> posts = new HashMap<>();
            posts.put("MenuNumber", ""+i );
            posts.put("MenuName", "menu"+i);
            posts.put("ImageURI", "gs://today-menu-selector.appspot.com/menu" + (i+1) +".bmp");
            posts.put("LikeNum", "0");
            posts.put("Price", (pr + (i*100))+"");
            myRef.child("menu"+i).setValue(posts);
        }
        */

        // 아래의 A.코드가 이 리스너보다 위에 있어도 이 리스너의 preference가 먼저 참조 되는경우가 있다.
        // 처음 초기화를 위해 한번만 menuCount와 각 Contact를 만들고
        // 리사이클러뷰에 어댑터를 set( contact가 만들어지기 전에 set하면 안되기 때문에 setAdapter는 contacts가 만들어질때마다 불린다.)
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                preference = new String[menuCount];
                for(int i = 0; i <  menuCount; i++) {

                    if (dataSnapshot.child("UserList").child(id).child("preference").child("" + i).exists()){
                        preference[i] = dataSnapshot.child("UserList").child(id).child("preference").child(""+i).getValue().toString();
                    }
                    else {
                        preference[i] = null;
                    }

                    if(dataSnapshot.child("menu"+i).exists()) { //메뉴 키가 존재할때만 데이터생성
                        int menuNum = Integer.parseInt(dataSnapshot.child("menu" + i).child("MenuNumber").getValue().toString());
                        String menuName = dataSnapshot.child("menu" + i).child("MenuName").getValue().toString();
                        String imageURI = dataSnapshot.child("menu" + i).child("ImageURI").getValue().toString();
                        int likeNum = Integer.parseInt(dataSnapshot.child("menu" + i).child("LikeNum").getValue().toString());
                        int price = Integer.parseInt(dataSnapshot.child("menu" + i).child("Price").getValue().toString());

                        if (preference[i] != null)
                            extractionImageFromStorage(i, menuName, imageURI, "Like", likeNum, price, getApplicationContext());
                        else
                            extractionImageFromStorage(i, menuName, imageURI, "Normal", likeNum, price, getApplicationContext());
                        Log.d("Data Change for oneTime", "Success to read value.");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Data Change for oneTime", "Failed to read value.");
            }
        });

        // A.  선호도 변화시 preference 배열을 다시 받기 위해
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                preference = new String[menuCount];

                myRef.child("UserList").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < menuCount; i++) {
                            if (dataSnapshot.child("preference").child("" + i).exists()){
                                preference[i] = dataSnapshot.child("preference").child(""+i).getValue().toString();
                            }
                            else
                                preference[i] = null;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        // myRef에 리스너를 달았기 때문에 UserList의 선호도 변화에도 실행이 되어 선호도 변화를 적용할 수 있다.
        // 메뉴개수에 변화가 있을경우 모든메뉴의 리스너를 다시 달아준다.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i <  menuCount; i++) {
                    if(dataSnapshot.child("menu"+i).exists()) { //메뉴 키가 존재할때만 데이터 변화 적용
                        myRef.child("menu" + i).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String menuName = dataSnapshot.child("MenuName").getValue().toString();
                                String imageURI = dataSnapshot.child("ImageURI").getValue().toString();
                                int likeNum = Integer.parseInt(dataSnapshot.child("LikeNum").getValue().toString());
                                int price = Integer.parseInt(dataSnapshot.child("Price").getValue().toString());
                                int menuNum = Integer.parseInt(dataSnapshot.child("MenuNumber").getValue().toString());

                                if (preference[menuNum] != null) {
                                    notifyToAdapter(menuNum, menuName, imageURI, "Like", likeNum, price);
                                }
                                else
                                    notifyToAdapter(menuNum, menuName, imageURI, "Normal", likeNum, price);

                                Log.d("Data Chane Every Time:", "Success to read value.");
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Data Chane:", "Failed to read value.");
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("menu count", "Failed to read value.");
            }
        });
    }

    //이미지 URI의 변경으로 인한 사진변경은 즉시반영 안함. 이전 액티비티를 갔다가 다시 돌아올때 적용
    public  void notifyToAdapter(final int menuNum, final String menuName, String imageURI, final String preference, final int likeNum, int price){
         if(contacts != null) {
             Contact contact = contacts.get(menuNum);
             contact.setLikeNum(likeNum);
             contact.setMenuName(menuName);
             contact.setPersonalPreference(preference);
             contact.setPrice(price);

             contacts.set(menuNum, contact);

             if (adapter != null)
                 adapter.notifyItemChanged(menuNum);
         }
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

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                float widthScale  = options.outWidth / display.widthPixels;
                float heightScale = options.outHeight / display.heightPixels;
                float scale =  widthScale > heightScale ? widthScale : heightScale;

                if(scale >= 8) {
                    options.inSampleSize = 8;
                }
                else if(scale >= 6) {
                    options.inSampleSize = 6;
                }
                else if(scale >= 4) {
                    options.inSampleSize = 4;
                }
                else if(scale >= 2) {
                    options.inSampleSize = 2;
                }
                else
                    options.inSampleSize = 1;
                options.inJustDecodeBounds = false;

                bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, 270, 300, true);

                // 비트맵이 생성된게 먼저 contacts안에 들어갈경우 실제 contact의 포지션과 생성하려는 순서의 문제가 생기기 때문에
                // 미리 contacts를 메뉴개수만큼 만들어놓고 set을 사용하여 실질적으로 삽입
                if(contacts == null) {
                    contacts = new ArrayList<Contact>();
                    for(int i = 0; i < menuCount; i++)
                       contacts.add(new Contact("menu", "Normal", null, 0, 0, i));
                }
                Contact contact = new Contact(menuName, preference, resizedBmp, likeNum, price, menuNum);
                contacts.set(menuNum, contact);

                if(adapter == null)
                    adapter = new ContactsAdapter(context, contacts, id, true);

                rvContacts.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                rvContacts.setLayoutManager(linearLayoutManager);

            }
         }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("extraction : Image load", "getBytes Failed");
            }
         });

       }

    }
