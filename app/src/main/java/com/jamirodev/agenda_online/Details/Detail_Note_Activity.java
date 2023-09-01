package com.jamirodev.agenda_online.Details;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.jamirodev.agenda_online.R;

public class Detail_Note_Activity extends AppCompatActivity {

    TextView Id_Note_Detail, Uid_User_Detail, Mail_User_Detail, Title_Detail, Description_Detail,
            Date_Registration_Detail, Date_Note_Detail, State_Note_Detail;

    //    DECLARE STRINGS TO STORE THEM
    String id_note_R, uid_user_R, mail_user_R, date_register_R, title_R, description_R, date_R, state_R;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Note detail");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        InitViews();
        DataRecover();
        SetDataRecovered();
    }

    private void InitViews() {
        Id_Note_Detail = findViewById(R.id.Id_Note_Detail);
        Uid_User_Detail = findViewById(R.id.Uid_User_Detail);
        Mail_User_Detail = findViewById(R.id.Mail_User_Detail);
        Title_Detail = findViewById(R.id.Title_Detail);
        Description_Detail = findViewById(R.id.Description_Detail);
        Date_Registration_Detail = findViewById(R.id.Date_Registration_Detail);
        Date_Note_Detail = findViewById(R.id.Date_Note_Detail);
        State_Note_Detail = findViewById(R.id.State_Note_Detail);
    }
    private void DataRecover() {
        Bundle intent = getIntent().getExtras();

        assert intent != null;
        id_note_R = intent.getString("id_note");
        uid_user_R = intent.getString("uid_user");
        mail_user_R = intent.getString("mail_user");
        date_register_R = intent.getString("date_register");
        title_R = intent.getString("title");
        description_R = intent.getString("description");
        date_R = intent.getString("date_note");
        state_R = intent.getString("state");

    }
    private void SetDataRecovered() {
        Id_Note_Detail.setText(id_note_R);
        Uid_User_Detail.setText(uid_user_R);
        Mail_User_Detail.setText(mail_user_R);
        Date_Registration_Detail.setText(date_register_R);
        Title_Detail.setText(title_R);
        Description_Detail.setText(description_R);
        Date_Note_Detail.setText(date_R);
        State_Note_Detail.setText(state_R);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}