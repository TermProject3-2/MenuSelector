package com.example.amdin.menuselector.urIStorage;

/**
 * Created by irvin on 2017-11-27.
 */

public class MenuImageURI {
    String [] urI;

    public MenuImageURI(){
        urI = new String[]{"gs://today-menu-selector.appspot.com/menu1.bmp", "gs://today-menu-selector.appspot.com/menu2.bmp",
                "gs://today-menu-selector.appspot.com/menu3.bmp", "gs://today-menu-selector.appspot.com/menu4.bmp" };
    }

    public String[] getUrI() {
        return urI;
    }

    public void setUrI(String[] urI) {
        this.urI = urI;
    }
}
