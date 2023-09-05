package com.jamirodev.agenda_online.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jamirodev.agenda_online.R;

public class ViewHolderContact extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolderContact.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderContact.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolderContact(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });
    }

    public void SetDataContact(Context context,
                               String id_contact,
                               String uid_contact,
                               String name,
                               String lastname,
                               String mail,
                               String phone,
                               String age,
                               String home,
                               String image) {

        ImageView Image_C_Item;
        TextView Id_C_Item, Uid_C_Item, Names_C_Item, LastName_C_Item, Mail_C_Item, Phone_C_Item, Age_C_Item, Home_C_Item;

        Image_C_Item = mView.findViewById(R.id.Image_C_Item);
        Id_C_Item = mView.findViewById(R.id.Id_C_Item);
        Uid_C_Item = mView.findViewById(R.id.Uid_C_Item);
        Names_C_Item = mView.findViewById(R.id.Names_C_Item);
        LastName_C_Item = mView.findViewById(R.id.Last_Name_Profile);
        Mail_C_Item = mView.findViewById(R.id.Mail_C_Item);
        Phone_C_Item = mView.findViewById(R.id.Phone_C_Item);
        Age_C_Item = mView.findViewById(R.id.Age_C_Item);
        Home_C_Item = mView.findViewById(R.id.Home_C_Item);

        Id_C_Item.setText(id_contact);
        Uid_C_Item.setText(uid_contact);
        Names_C_Item.setText(name);
        LastName_C_Item.setText(lastname);
        Mail_C_Item.setText(mail);
        Phone_C_Item.setText(phone);
        Age_C_Item.setText(age);
        Home_C_Item.setText(home);

        try {

            Glide.with(context).load(image).placeholder(R.drawable.contactuser).into(Image_C_Item);

        }catch (Exception e) {
            Glide.with(context).load(R.drawable.contactuser).into(Image_C_Item);
        }

    }


}
