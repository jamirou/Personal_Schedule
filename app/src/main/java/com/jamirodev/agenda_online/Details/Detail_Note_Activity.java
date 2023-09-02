package com.jamirodev.agenda_online.Details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.R;

import java.util.HashMap;
import java.util.Objects;

public class Detail_Note_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Button Btn_Important;
    TextView Id_Note_Detail, Uid_User_Detail, Mail_User_Detail, Title_Detail, Description_Detail,
            Date_Registration_Detail, Date_Note_Detail, State_Note_Detail;

    //    DECLARE STRINGS TO STORE THEM
    String id_note_R, uid_user_R, mail_user_R, date_register_R, title_R, description_R, date_R, state_R;

    boolean CheckImportantNote = false;
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
        CheckImportantNote();

        Btn_Important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckImportantNote) {
                    Delete_Important_Note();
                }else{
                    Add_Important_Notes();
                }
            }
        });
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
        Btn_Important = findViewById(R.id.Btn_Important);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
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
    private void Add_Important_Notes() {
        if (user == null){
            //IF USER IS NULL
            Toast.makeText(Detail_Note_Activity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
        }else {
            //GET DATA FROM LAST ACTIVITY
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



            HashMap<String, String> Note_Important = new HashMap<>();
            Note_Important.put("id_note", id_note_R);
            Note_Important.put("uid_user", uid_user_R);
            Note_Important.put("mail_user", mail_user_R);
            Note_Important.put("date_actual_hour", date_register_R);
            Note_Important.put("title", title_R);
            Note_Important.put("description", description_R);
            Note_Important.put("date_note", date_R);
            Note_Important.put("state", state_R);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Important Notes").child(id_note_R)
                    .setValue(Note_Important)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detail_Note_Activity.this, "Added as important", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detail_Note_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void Delete_Important_Note() {
        if (user == null) {
            Toast.makeText(Detail_Note_Activity.this, "", Toast.LENGTH_SHORT).show();
        }else {
            Bundle intent = getIntent().getExtras();
            assert intent != null;
            id_note_R = intent.getString("id_note");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Important Notes").child(id_note_R)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detail_Note_Activity.this, "Note deleted from important", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detail_Note_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void CheckImportantNote() {
        if (user == null) {
            Toast.makeText(Detail_Note_Activity.this, "An error has ocurred", Toast.LENGTH_SHORT).show();
        }else {
            Bundle intent = getIntent().getExtras();
            assert intent != null;
            id_note_R = intent.getString("id_note");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Important Notes").child(id_note_R)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            CheckImportantNote = snapshot.exists();
                            if (CheckImportantNote) {
                                String important = "Important";
                                Btn_Important.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_note_important, 0, 0);
                                Btn_Important.setText(important);
                            }else {
                                String no_important = "Not important";
                                Btn_Important.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_note_not_important, 0, 0);
                                Btn_Important.setText(no_important);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}