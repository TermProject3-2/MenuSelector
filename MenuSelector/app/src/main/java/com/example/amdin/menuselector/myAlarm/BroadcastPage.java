package com.example.amdin.menuselector.myAlarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.amdin.menuselector.LoginActivity;
import com.example.amdin.menuselector.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by owner on 2017-12-11.
 */

public class BroadcastPage extends BroadcastReceiver {
    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private DatabaseReference myRef;
    private String id, htmlinfo,htmlget;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationmanager;
    private String htmlPageUrl = "https://hansung.ac.kr/web/www/life_03_01_t1";

    @Override
    public void onReceive(Context context, Intent intent) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("MenuList").child("UserList");
        id = intent.getStringExtra("id");
        htmlinfo = intent.getStringExtra("htmlinfo");
        this.context = context;

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();


    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();


                //테스트1
                Elements titles = doc.select("div.journal-content-article");
                htmlget = new String();

                for (Element e : titles) {

                    htmlget += e.text().trim() + "\n";
                }
                myRef.child("pageinfo").child("pagetext").setValue(htmlget);
                if(!htmlinfo.equals(htmlget))
                    myRef.child("pageinfo").child("pageonchange").setValue("changed");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }
}
