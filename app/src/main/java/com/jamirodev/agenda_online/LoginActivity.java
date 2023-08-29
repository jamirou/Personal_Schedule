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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @noinspection deprecation
 */
public class LoginActivity extends AppCompatActivity {

    EditText MailLogin, PassLogin;
    Button Btn_login;
    TextView NewUserTXT;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    //    DATA VALIDATION
    String mail = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        MailLogin = findViewById(R.id.MailLogin);
        PassLogin = findViewById(R.id.PassLogin);
        Btn_login = findViewById(R.id.Btn_login);
        NewUserTXT = findViewById(R.id.NewUserTXT);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataValidation();
            }
        });
        NewUserTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RecordActivity.class));
            }
        });
    }

    private void DataValidation() {
        mail = MailLogin.getText().toString();
        password = PassLogin.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, "Invalid mail", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        } else {
            LoginUser();
        }

    }

    private void LoginUser() {
        progressDialog.setMessage(getString(R.string.logging_in));
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                            Toast.makeText(LoginActivity.this, "Welcome back " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Check if your mail and password are correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}