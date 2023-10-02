package com.jamirodev.agenda_online.UpdatePassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.LoginActivity;
import com.jamirodev.agenda_online.R;

import java.util.HashMap;
import java.util.Objects;

public class Update_Password_Activity extends AppCompatActivity {

    TextView CurrentPassword;
    EditText ETCurrentPassword, ETNewPassword, ETRNewPassword;
    Button Btn_update_password;
    DatabaseReference DB_Users;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Change password");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InitVariables();
        DataLecture();

        Btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Actual_password = ETCurrentPassword.getText().toString().trim();
                String New_Password = ETNewPassword.getText().toString().trim();
                String RNewPassword = ETRNewPassword.getText().toString().trim();

                if (Actual_password.equals("")) {
                    ETCurrentPassword.setError("Fill the field");
                } else if (New_Password.equals("")) {
                    ETNewPassword.setError("Fill the field");
                } else if (RNewPassword.equals("")) {
                    ETRNewPassword.setError("Fill the field");
                } else if (!New_Password.equals(RNewPassword)) {
                    Toast.makeText(Update_Password_Activity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else if (New_Password.length() < 6) {
                    ETNewPassword.setError("Password must be greater than or equal to 6 characters");
                } else {
                    Update_Password(Actual_password, New_Password);
                }
            }
        });
    }

    private void Update_Password(String actualPassword, String newPassword) {
        progressDialog.show();
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Por favor espere");

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), actualPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        String New_pass = ETNewPassword.getText().toString().trim();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("Password", New_pass);
                                        DB_Users = FirebaseDatabase.getInstance().getReference("Users");
                                        DB_Users.child(user.getUid()).updateChildren(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(Update_Password_Activity.this, "Cambios aplicados", Toast.LENGTH_SHORT).show();
                                                        firebaseAuth.signOut();
                                                        Intent intent = new Intent(Update_Password_Activity.this, LoginActivity.class)
                                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(Update_Password_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Update_Password_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Update_Password_Activity.this, "The password is wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void InitVariables() {
        CurrentPassword = findViewById(R.id.CurrentPassword);
        ETCurrentPassword = findViewById(R.id.ETCurrentPassword);
        ETNewPassword = findViewById(R.id.ETNewPassword);
        ETRNewPassword = findViewById(R.id.ETRNewPassword);
        Btn_update_password = findViewById(R.id.Btn_update_password);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(Update_Password_Activity.this);

    }

    private void DataLecture() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pass = "" + snapshot.child("Password").getValue();
                CurrentPassword.setText(pass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}