package com.example.amdin.menuselector.AccessDB;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

/**
 * Created by irvin on 2017-12-02.
 */

public class ChangeDB {

    public void changeDataForDB(DatabaseReference myRef, int position, String menuName, String imageURI, String likeNum, String preference){

        String key = myRef.child("menu" + position).getKey();

        //주의 여기서 Nomal과 위치와 글자가 일치하는 문자열을 넣을경우 성공처리 해버림
        if(! menuName.equals("x"))
            myRef.child(key).child("MenuName").setValue(menuName);
        if(! imageURI.equals("x"))
            myRef.child(key).child("ImageURI").setValue(imageURI);
        if(! likeNum.equals("x"))
            myRef.child(key).child("LikeNum").setValue(likeNum);
        if(! preference.equals("x"))
            myRef.child(key).child("Preference").setValue(preference);

    }
}
