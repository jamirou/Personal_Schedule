package com.jamirodev.agenda_online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    Button SignOff;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SignOff = findViewById(R.id.SignOff);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        SignOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitApp();
            }
        });

    }
    private void ExitApp() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
    }
}