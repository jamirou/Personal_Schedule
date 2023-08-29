package com.jamirodev.agenda_online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Charge_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        firebaseAuth = FirebaseAuth.getInstance();

        int Time = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* startActivity(new Intent(Charge_Activity.this, MainActivity.class));
                finish();*/
                VerifyUser();
            }
        }, Time);
    }

    private void VerifyUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(Charge_Activity.this, MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(Charge_Activity.this, MainMenuActivity.class));
            finish();
        }
    }
}