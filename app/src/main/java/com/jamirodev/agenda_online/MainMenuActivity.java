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

public class MainMenuActivity extends AppCompatActivity {

    Button SignOut;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView NamesMain, MailMain;
    ProgressBar ProgressBarData;

    DatabaseReference Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Online schedule");

        NamesMain = findViewById(R.id.NamesMain);
        MailMain = findViewById(R.id.MailMain);
        ProgressBarData = findViewById(R.id.ProgressBarData);

        Users = FirebaseDatabase.getInstance().getReference("Users");

        SignOut = findViewById(R.id.SignOut);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

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
        if (user!= null){
            DataLoad();
        }else {
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
                    NamesMain.setVisibility(View.VISIBLE);
                    MailMain.setVisibility(View.VISIBLE);

                    //GET DATA
                    String names = ""+snapshot.child("Names").getValue();
                    String mail = ""+snapshot.child("Gmail").getValue();

                    //SET DATA ON TEXTVIEW
                    NamesMain.setText(names);
                    MailMain.setText(mail);

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