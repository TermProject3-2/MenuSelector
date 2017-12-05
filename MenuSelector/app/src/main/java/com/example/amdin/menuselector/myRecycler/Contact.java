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
    private int price;

    public Contact(String mName, String personalPreference, Bitmap ImageBitmap, int likeNum, int price) {
        this.menuName = mName;
        this.personalPreference = personalPreference;
        this.ImageBitmap = ImageBitmap;
        this.likeNum = likeNum;
        this.price =price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
