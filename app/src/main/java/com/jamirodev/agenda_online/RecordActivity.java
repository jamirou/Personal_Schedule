package com.jamirodev.agenda_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/** @noinspection deprecation*/
public class RecordActivity extends AppCompatActivity {

    EditText NameEt, GmailEt, PasswordEt, ConfirmPasswordEt;
    Button RegisterUser;
    TextView HaveAccount;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    //
    String name = "", gmail = "", password = "", confirmpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Register");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        NameEt = findViewById(R.id.NameEt);
        GmailEt = findViewById(R.id.GmailEt);
        PasswordEt = findViewById(R.id.PasswordEt);
        ConfirmPasswordEt = findViewById(R.id.ConfirmPasswordEt);
        RegisterUser = findViewById(R.id.RegisterUser);
        HaveAccount = findViewById(R.id.HaveAccount);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RecordActivity.this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);

        RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateData();
            }
        });

        HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordActivity.this, LoginActivity.class));
            }
        });
    }

    private void ValidateData() {
        name = NameEt.getText().toString();
        gmail = GmailEt.getText().toString();
        password = PasswordEt.getText().toString();
        confirmpassword = ConfirmPasswordEt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Pon tu nombre por favor", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(gmail).matches()) {
            Toast.makeText(this, "Por favor pon tu correo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor pon una contraseña", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmpassword)) {
            Toast.makeText(this, "Confirma la contraseña", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "Las contraseñas no cinciden", Toast.LENGTH_SHORT).show();
            
        } else {
            CreateAccount();
        }
    }

    private void CreateAccount() {
        progressDialog.setMessage(getString(R.string.creating_your_account));
        progressDialog.show();

        //CREATE USER IN FIREBASE
        firebaseAuth.createUserWithEmailAndPassword(gmail, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //
                        SaveInformation();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RecordActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SaveInformation() {
        progressDialog.setMessage("Saving your information");
        progressDialog.dismiss();

        //Get actual user info
        String uid = firebaseAuth.getUid();

        HashMap<String, String> Data = new HashMap<>();
        Data.put("uid", uid);
        Data.put("Gmail", gmail);
        Data.put("Names", name);
        Data.put("Password", password);

        Data.put("Last name", "");
        Data.put("Age", "");
        Data.put("Phone", "");
        Data.put("Home", "");
        Data.put("College", "");
        Data.put("Profession", "");
        Data.put("Birthdate", "");
        Data.put("Profile_img", "");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        assert uid != null;
        databaseReference.child(uid)
                .setValue(Data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RecordActivity.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RecordActivity.this, MainMenuActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RecordActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}