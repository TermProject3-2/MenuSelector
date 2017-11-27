package com.example.amdin.menuselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.amdin.menuselector.myRecycler.Contact;
import com.example.amdin.menuselector.myRecycler.ContactsAdapter;
import com.example.amdin.menuselector.myRecycler.MarginItemDecoration;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {
    ArrayList<Contact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(this, contacts);

        rvContacts.setAdapter(adapter);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(gridLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new
                MarginItemDecoration(20);
        rvContacts.addItemDecoration(itemDecoration);

        // optimizations if all item views are of the same height and width for significantly smoother scrolling:
        rvContacts.setHasFixedSize(true);

        // Add a new contact
        contacts.add(0, new Contact("Barney", true));
        // Notify the adapter that an item was inserted at position 0
        adapter.notifyItemInserted(0);
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    */
}
