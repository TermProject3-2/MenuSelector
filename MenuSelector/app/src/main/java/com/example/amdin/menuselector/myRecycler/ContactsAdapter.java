package com.example.amdin.menuselector.myRecycler;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.amdin.menuselector.MyMenu.MenuDetailActivity;
import com.example.amdin.menuselector.R;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private String id;
    private List<Contact> mContacts;
    private Context mContext;
    private boolean buttonOn;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView menuNameView;
        public ImageButton imageButton;
        public TextView likeNumView;
        public TextView personalPreferenceView;
        public TextView priceView;
        private ContactsAdapter mContacts;

        private Context context;


        public ViewHolder(Context context, View itemView, ContactsAdapter contacts) {
            super(itemView);

            menuNameView = (TextView) itemView.findViewById(R.id.menuName);
            imageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            likeNumView =  (TextView) itemView.findViewById(R.id.likeNum);
            personalPreferenceView = (TextView) itemView.findViewById(R.id.personalPreference);
            priceView = (TextView) itemView.findViewById(R.id.price);

            this.context = context;
            this.mContacts = contacts;
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Toast.makeText(context, menuNameView.getText() + Integer.toString(position), Toast.LENGTH_SHORT).show();
            mContacts.removeItem(position);

        }
    }

    public ContactsAdapter(Context context, List<Contact> contacts, String id) {
        mContacts = contacts;
        mContext = context;
        this.id = id;
    }


    private Context getContext() {
        return mContext;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        ViewHolder viewHolder = new ViewHolder(context, contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder viewHolder, final int position) {
        Contact contact = mContacts.get(position);

        final TextView menuNameView  = viewHolder.menuNameView;
        final ImageButton imageButton = viewHolder.imageButton;
        TextView likeNumView  = viewHolder.likeNumView;
        TextView personalPreferenceView = viewHolder.personalPreferenceView;
        TextView priceView = viewHolder.priceView;

        final String MenuPosition = "" + contact.getMenuNum();
        final String MenuName = contact.getMenuName();
        final Bitmap MenuImageBitMap = contact.getImageBitmap();
        final String LikeNum = "" + contact.getLikeNum();
        final String Preference = contact.getPersonalPreference();
        final String price = "" + contact.getPrice();

        menuNameView.setText(MenuName);
        if(MenuImageBitMap != null)
             imageButton.setImageBitmap(MenuImageBitMap);
        likeNumView.setText(LikeNum);
        personalPreferenceView.setText(Preference);
        priceView.setText(price);



            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("MenuPosition : " + MenuPosition);
                    Intent intent = new Intent(mContext, MenuDetailActivity.class);
                    intent.putExtra("MenuPosition", MenuPosition);
                    intent.putExtra("MenuName", MenuName);
                    intent.putExtra("MenuImageBitMap", MenuImageBitMap);
                    intent.putExtra("LikeNum", LikeNum);
                    intent.putExtra("Preference", Preference);
                    intent.putExtra("Price", price);
                    intent.putExtra("Id", id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

    }


    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public void removeItem(int p) {
        mContacts.remove(p);
        notifyItemRemoved(p);

    }

}
