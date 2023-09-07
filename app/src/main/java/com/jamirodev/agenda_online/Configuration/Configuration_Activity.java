package com.jamirodev.agenda_online.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.MainMenuActivity;
import com.jamirodev.agenda_online.R;

public class Configuration_Activity extends AppCompatActivity {

    TextView Uid_delete, DeleteAccount;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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

        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteUserAuth();
            }
        });
    }

    private void DeleteUserAuth() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Configuration_Activity.this);
        alertDialog.setTitle("are you sure?");
        alertDialog.setMessage("Your account will be permanently deleted");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DeleteUserDB();
                            Intent intent = new Intent(Configuration_Activity.this, MainMenuActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Configuration_Activity.this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Configuration_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Configuration_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Configuration_Activity.this, "Canceled by user", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.create().show();
    }

    private void DeleteUserDB() {
        String uid_delete = Uid_delete.getText().toString();
        Query query = databaseReference.child(uid_delete);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toast.makeText(Configuration_Activity.this, "Account deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitVariables() {
        Uid_delete = findViewById(R.id.Uid_delete);
        DeleteAccount = findViewById(R.id.DeleteAccount);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
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