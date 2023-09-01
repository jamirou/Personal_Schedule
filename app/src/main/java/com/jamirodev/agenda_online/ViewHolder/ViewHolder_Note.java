package com.jamirodev.agenda_online.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jamirodev.agenda_online.R;

public class ViewHolder_Note extends RecyclerView.ViewHolder {
    View mView;
    private ViewHolder_Note.ClickListener mClickListener;
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ViewHolder_Note.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public ViewHolder_Note(@NonNull View itemView) {
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

    public void SetData(Context context, String id_note, String uid_user, String mail_user,
                        String date_hour_register, String title, String description,
                        String date_note, String state) {
        //SET VIEWS
        TextView Id_note_Item, Uid_User_Item, Mail_User_Item, Date_register_hour_Item, Title_Item,
                Description_Item, Date_Item, State_Item;

        ImageView Task_Ended_Item, Task_Incomplete_Item;


        //CONNECT WITH ITEM
        Id_note_Item = mView.findViewById(R.id.Id_note_Item);
        Uid_User_Item = mView.findViewById(R.id.Uid_User_Item);
        Mail_User_Item = mView.findViewById(R.id.Mail_User_Item);
        Date_register_hour_Item = mView.findViewById(R.id.Date_register_hour_Item);
        Title_Item = mView.findViewById(R.id.Title_Item);
        Description_Item = mView.findViewById(R.id.Description_Item);
        Date_Item = mView.findViewById(R.id.Date_Item);
        State_Item = mView.findViewById(R.id.State_Item);

        Task_Ended_Item = mView.findViewById(R.id.Task_Ended_Item);
        Task_Incomplete_Item = mView.findViewById(R.id.Task_Incomplete_Item);

        //SET INFO ON ITEM
        Id_note_Item.setText(id_note);
        Uid_User_Item.setText(uid_user);
        Mail_User_Item.setText(mail_user);
        Date_register_hour_Item.setText(date_hour_register);
        Title_Item.setText(title);
        Description_Item.setText(description);
        Date_Item.setText(date_note);
        State_Item.setText(state);

        //MANAGE STATUS COLOR
        if (state.equals("finished")){
            Task_Ended_Item.setVisibility(View.VISIBLE);
        }else {
            Task_Incomplete_Item.setVisibility(View.VISIBLE);
        }

    }
}
