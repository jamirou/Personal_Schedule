package com.jamirodev.agenda_online.Configuration;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.jamirodev.agenda_online.R;

public class Configuration_Activity extends AppCompatActivity {

    TextView Uid_delete, DeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Configurations");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InitVariables();
        GetUid();
    }

    private void InitVariables() {
        Uid_delete = findViewById(R.id.Uid_delete);
        DeleteAccount = findViewById(R.id.DeleteNote);
    }

    private void GetUid(){
        String uid = getIntent().getStringExtra("Uid");
        Uid_delete.setText(uid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}