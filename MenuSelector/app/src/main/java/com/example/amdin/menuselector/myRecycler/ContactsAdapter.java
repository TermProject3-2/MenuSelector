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

    public ContactsAdapter(Context context, List<Contact> contacts, String id, boolean buttonOn) {
        mContacts = contacts;
        mContext = context;
        this.id = id;
        this.buttonOn = buttonOn;
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

        final String MenuPosition = "" + contact.getMenuNum(); // position을 써버리면 메뉴번호가 아니라 순서에 의해 메뉴를 정의하기 때문에
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


        if(buttonOn) {
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
