package com.jamirodev.agenda_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.AddNote.Add_Note_Activity;
import com.jamirodev.agenda_online.ArchivedNotes.Archived_Notes_Activity;
import com.jamirodev.agenda_online.ListNote.List_Notes_Activity;
import com.jamirodev.agenda_online.Profile.Profile_User_Activity;

public class MainMenuActivity extends AppCompatActivity {

    Button AddNotes, ListNotes, Archived, Profile, About, SignOut;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView UidMain, NamesMain, MailMain;
    ProgressBar ProgressBarData;

    DatabaseReference Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Online schedule");

        UidMain = findViewById(R.id.UidMain);
        NamesMain = findViewById(R.id.NamesMain);
        MailMain = findViewById(R.id.MailMain);
        ProgressBarData = findViewById(R.id.ProgressBarData);

        Users = FirebaseDatabase.getInstance().getReference("Users");

        AddNotes = findViewById(R.id.AddNotes);
        ListNotes = findViewById(R.id.ListNotes);
        Archived = findViewById(R.id.Archived);
        Profile = findViewById(R.id.Profile);
        About = findViewById(R.id.About);
        SignOut = findViewById(R.id.SignOut);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        AddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //OBTAIN TXTVIEW INFO
                String uid_user = UidMain.getText().toString();
                String email_user = MailMain.getText().toString();

                //NEXT ACTIVITY
                Intent intent = new Intent(MainMenuActivity.this, Add_Note_Activity.class);
                intent.putExtra("Uid", uid_user);
                intent.putExtra("Gmail", email_user);
                startActivity(intent);
            }
        });
        ListNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, List_Notes_Activity.class));
                Toast.makeText(MainMenuActivity.this, "List Notes", Toast.LENGTH_SHORT).show();
            }
        });
        Archived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, Archived_Notes_Activity.class));
                Toast.makeText(MainMenuActivity.this, "Archived Notes", Toast.LENGTH_SHORT).show();
            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, Profile_User_Activity.class));
                Toast.makeText(MainMenuActivity.this, "User profile", Toast.LENGTH_SHORT).show();
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenuActivity.this, "About", Toast.LENGTH_SHORT).show();
            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitApp();
            }
        });

    }

    @Override
    protected void onStart() {
        CheckLogin();
        super.onStart();
    }

    private void CheckLogin() {
        if (user != null) {
            DataLoad();
        } else {
            startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
            finish();
        }
    }

    private void DataLoad() {
        Users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if user exist
                if (snapshot.exists()) {
                    //hide progressbar
                    ProgressBarData.setVisibility(View.GONE);
                    //SHOWING TEXTVIEW
                    UidMain.setVisibility(View.VISIBLE);
                    NamesMain.setVisibility(View.VISIBLE);
                    MailMain.setVisibility(View.VISIBLE);

                    //GET DATA
                    String uid = "" + snapshot.child("uid").getValue();
                    String names = "" + snapshot.child("Names").getValue();
                    String mail = "" + snapshot.child("Gmail").getValue();

                    //SET DATA ON TEXTVIEW
                    UidMain.setText(uid);
                    NamesMain.setText(names);
                    MailMain.setText(mail);

                    //ENABLE MENU BUTTONS
                    AddNotes.setEnabled(true);
                    ListNotes.setEnabled(true);
                    Archived.setEnabled(true);
                    Profile.setEnabled(true);
                    About.setEnabled(true);
                    SignOut.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ExitApp() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
    }
}