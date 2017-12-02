package com.example.amdin.menuselector.myRecycler;

/**
 * Created by amdin on 2017-11-22.
 */

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class Contact {
    private String menuName;
    private String personalPreference;
    private Bitmap ImageBitmap;
    private int likeNum;

    public Contact(String mName, String personalPreference, Bitmap ImageBitmap, int likeNum) {
        this.menuName = mName;
        this.personalPreference = personalPreference;
        this.ImageBitmap = ImageBitmap;
        this.likeNum = likeNum;

    }


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPersonalPreference() {
        return personalPreference;
    }

    public void setPersonalPreference(String personalPreference) {
        this.personalPreference = personalPreference;
    }

    public Bitmap getImageBitmap() {
        return ImageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        ImageBitmap = imageBitmap;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }


}
