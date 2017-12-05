package com.example.amdin.menuselector.AccessDB;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;

/**
 * Created by irvin on 2017-12-02.
 */

public class ChangeDB {

    public void changeDataForDB(DatabaseReference myRef, int position, String menuName, String imageURI, String likeNum, String preference, String id){

        String key = myRef.child("menu" + position).getKey();

        //주의 여기서 Nomal과 위치와 글자가 일치하는 문자열을 넣을경우 성공처리 해버림
        if(! menuName.equals("x"))
            myRef.child(key).child("MenuName").setValue(menuName);
        if(! imageURI.equals("x"))
            myRef.child(key).child("ImageURI").setValue(imageURI);

        if(likeNum.equals("+")){
            myRef.child("menu"+position).child("LikeNum").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    int LikeNum = Integer.parseInt(mutableData.getValue(String.class));
                    LikeNum++;
                    mutableData.setValue(""+LikeNum);
                    System.out.println("+ LikeNum test ! : " + LikeNum);
                    return Transaction.success(mutableData);
                }
                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.d("Transaction result", "Transaction:onComplete:" + databaseError);
                }
            });
        }
        else if (likeNum.equals("-")){
            myRef.child(key).child("LikeNum").runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    int LikeNum = Integer.parseInt(mutableData.getValue(String.class));
                    LikeNum--;
                    mutableData.setValue(""+LikeNum);
                    System.out.println("- LikeNum test ! : " + LikeNum);
                    return Transaction.success(mutableData);
                }
                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.d("Transaction result", "Transaction:onComplete:" + databaseError);
                }
            });
        }

        if(preference.equals("Like"))
            myRef.child("UserList").child(id).child("preference").child("" + position).setValue("" + position);
        else if(preference.equals("Normal"))
            myRef.child("UserList").child(id).child("preference").child("" + position).removeValue();

    }
}
