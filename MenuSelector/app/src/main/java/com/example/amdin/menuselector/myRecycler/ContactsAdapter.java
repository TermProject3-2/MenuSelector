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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView menuNameView;
        public ImageButton imageButton;
        public TextView likeNumView;
        public TextView personalPreferenceView;
        private ContactsAdapter mContacts;

        private Context context;


        public ViewHolder(Context context, View itemView, ContactsAdapter contacts) {
            super(itemView);

            menuNameView = (TextView) itemView.findViewById(R.id.menuName);
            imageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            likeNumView =  (TextView) itemView.findViewById(R.id.likeNum);
            personalPreferenceView = (TextView) itemView.findViewById(R.id.personalPreference);

            this.context = context;
            this.mContacts = contacts;

            //messageButton.setOnClickListener(this);  클릭 리스너가 삭제인데 우리는 여기다가 이미지를 달고, 이 레이아웃에 클릭 리스너를 달거야
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Toast.makeText(context, menuNameView.getText() + Integer.toString(position), Toast.LENGTH_SHORT).show();
            mContacts.removeItem(position);

        }
    }


    private List<Contact> mContacts;
    private Context mContext;


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
        ImageButton imageButton = viewHolder.imageButton;
        TextView likeNumView  = viewHolder.likeNumView;
        TextView personalPreferenceView = viewHolder.personalPreferenceView;

        final String MenuPosition = "" + position;
        final String MenuName = contact.getMenuName();
        final Bitmap MenuImageBitMap = contact.getImageBitmap();
        final String LikeNum = "" + contact.getLikeNum();
        final String Preference = contact.getPersonalPreference();

        menuNameView.setText(MenuName);
        imageButton.setImageBitmap(MenuImageBitMap);
        likeNumView.setText(LikeNum);
        personalPreferenceView.setText(Preference);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("MenuPosition : " + MenuPosition);
                Intent intent = new Intent(getContext(), MenuDetailActivity.class);
                intent.putExtra("MenuPosition", MenuPosition);
                intent.putExtra("MenuName", MenuName);
                intent.putExtra("MenuImageBitMap", MenuImageBitMap);
                intent.putExtra("LikeNum", LikeNum);
                intent.putExtra("Preference", Preference);
                intent.putExtra("Id", id);
                System.out.println("Null Check " + MenuName + " , " + LikeNum + " , " + Preference + " , ");
                getContext().startActivity(intent);
            }
        });


        //Button button = viewHolder.messageButton; 버튼 에다가 딜리트를 달아주는 작업 우리는 여기에다가 이미지를 달거야
        //button.setText("Delete");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public void removeItem(int p) {
        mContacts.remove(p);
        notifyItemRemoved(p);

    }

}
