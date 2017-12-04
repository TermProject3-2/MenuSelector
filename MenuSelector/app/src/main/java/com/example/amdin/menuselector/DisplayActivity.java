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
import java.util.HashMap;


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

        HashMap<String, Object> postValues = new HashMap<>();
        postValues.put("id", id);
        postValues.put("pass", pass);

/*
//데이터 삽입용 코드
        System.out.println("hello id :" + id);
        HashMap<String, Object> postv = new HashMap<String, Object>();
        for(int i = 0; i < 20; i++)
            postv.put("menu"+i, "Nomal");
        myRef.child("UserList").child("t").setValue(postv);


 myRef.child("UserList").child(id).child("preference").child("1").setValue("1");
        myRef.child("UserList").child(id).child("preference").child("3").setValue("3");
        myRef.child("UserList").child(id).child("preference").child("5").setValue("5");

        for(int i = 0; i < 20; i++) {
            String key = myRef.child("menu" + i).getKey();
            HashMap<String, Object> postValues = new HashMap<>();
            postValues.put("MenuNumber", ""+i );
            postValues.put("MenuName", "menu"+i);
            postValues.put("ImageURI", "gs://today-menu-selector.appspot.com/menu2.bmp");
            postValues.put("LikeNum", "0");
            postValues.put("Preference", "Nomal");
            myRef.child(key).setValue(postValues);
        }
*/
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());
                preference = new String[menuCount];
                System.out.println("menuCount! : " + menuCount);

                myRef.child("UserList").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < menuCount; i++) {
                            System.out.println("UserListChanged!!!!!!!!!! : " + menuCount);
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

        // 처음 초기화를 위해 한번만 menuCount와 각 Contact를 만들고
        // 리사이클러뷰에 어댑터를 set( contact가 만들어지기 전에 set하면 안되기 때문에 setAdapter는 contacts가 만들어질때마다 불린다.)
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = Integer.parseInt(dataSnapshot.child("MenuCount").getValue().toString());

                for(int i = 0; i <  menuCount; i++) {
                    int menuNum = Integer.parseInt(dataSnapshot.child("menu"+i).child("MenuNumber").getValue().toString());
                    String menuName = dataSnapshot.child("menu"+i).child("MenuName").getValue().toString();
                    String imageURI = dataSnapshot.child("menu"+i).child("ImageURI").getValue().toString();
                    int likeNum = Integer.parseInt(dataSnapshot.child("menu"+i).child("LikeNum").getValue().toString());

                    if(preference[i] != null)
                        extractionImageFromStorage(menuName, imageURI,"Like", likeNum, getApplicationContext());
                    else
                        extractionImageFromStorage(menuName, imageURI,"Normal", likeNum, getApplicationContext());
                    Log.d("Data Change for oneTime", "Success to read value.");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Data Change for oneTime", "Failed to read value.");
            }
        });


        // 메뉴개수에 변화가 있을경우 모든메뉴의 리스너를 다시 달아준다.
        // 리스너(DB의 데이터가 변화할 경우 notify하는 기능을 넣은 리스너)를 추가
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(int i = 0; i <  menuCount; i++) {
                    myRef.child("menu"+i).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String menuName = dataSnapshot.child("MenuName").getValue().toString();
                            String imageURI = dataSnapshot.child("ImageURI").getValue().toString();
                            int likeNum = Integer.parseInt(dataSnapshot.child("LikeNum").getValue().toString());
                            int menuNum = Integer.parseInt(dataSnapshot.child("MenuNumber").getValue().toString());

                            if(preference[menuNum] != null)
                                notifyToAdapter(menuNum, menuName,imageURI, "Like", likeNum);
                            else
                                notifyToAdapter(menuNum, menuName,imageURI, "Normal", likeNum);
                            // HashMap<String, Object> preference = (HashMap<String, Object>)preferenceMap.get("menu"+menuNum);
                            //notifyToAdapter(menuNum, menuName,imageURI, preference.get("preference").toString(), likeNum);
                            Log.d("Data Chane Every Time:", "Success to read value.");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("Data Chane:", "Failed to read value.");
                        }
                    });

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("menu count", "Failed to read value.");
            }
        });

    }


       public  void notifyToAdapter(final int menuNum, final String menuName, String imageURI, final String preference, final int likeNum){
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

                   Contact contact = new Contact(menuName, preference, resizedBmp, likeNum);

                   if(contacts != null)
                       contacts.set(menuNum, contact);
                   else
                       System.out.println("!!!!!!!!!!!!!!! contacts is null !!!!!!!!!!!!!");
                   if(adapter != null)
                       adapter.notifyItemChanged(menuNum);
                   else
                       System.out.println("!!!!!!!!!!!!!!! adapter is null !!!!!!!!!!!!!");
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception exception) {
                   Log.d("Noify : Image load", "getBytes Failed");
               }
           });
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

                if(contacts == null)
                    contacts = new ArrayList<Contact>();
                Contact contact = new Contact(menuName, preference, resizedBmp, likeNum);

                contacts.add(contact);

                if(adapter == null)
                    adapter = new ContactsAdapter(context, contacts, id);

                rvContacts.setAdapter(adapter);
                StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rvContacts.setLayoutManager(gridLayoutManager);

                RecyclerView.ItemDecoration itemDecoration = new
                        MarginItemDecoration(4);
                rvContacts.addItemDecoration(itemDecoration);
                    // optimizations if all item views are of the same height and width for significantly smoother scrolling:
                rvContacts.setHasFixedSize(true);

            }
         }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("extraction : Image load", "getBytes Failed");
            }
         });

       }

    }
