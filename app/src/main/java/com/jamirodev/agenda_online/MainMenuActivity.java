package com.jamirodev.agenda_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.jamirodev.agenda_online.AddNote.Add_Note_Activity;
import com.jamirodev.agenda_online.ImportantNotes.Important_Notes_Activity;
import com.jamirodev.agenda_online.ListNote.List_Notes_Activity;
import com.jamirodev.agenda_online.Profile.Profile_User_Activity;

/** @noinspection ALL*/
public class MainMenuActivity extends AppCompatActivity {

    Button AddNotes, ListNotes, Important, Contacts, About, SignOut;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView UidMain, NamesMain, MailMain;
    Button MainAccountStatus;
    ProgressBar ProgressBarData;
    ProgressDialog progressDialog;
    LinearLayoutCompat Linear_Names, Linear_Mail, Linear_Verification;
    DatabaseReference Users;
    Dialog dialog_verified_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");

        UidMain = findViewById(R.id.UidMain);
        NamesMain = findViewById(R.id.NamesMain);
        MailMain = findViewById(R.id.MailMain);
        MainAccountStatus = findViewById(R.id.MainAccountStatus);
        ProgressBarData = findViewById(R.id.ProgressBarData);
        dialog_verified_account = new Dialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        Linear_Names = findViewById(R.id.Linear_Names);
        Linear_Mail = findViewById(R.id.Linear_Mail);
        Linear_Verification = findViewById(R.id.Linear_Verification);


        Users = FirebaseDatabase.getInstance().getReference("Users");

        AddNotes = findViewById(R.id.AddNotes);
        ListNotes = findViewById(R.id.ListNotes);
        Important = findViewById(R.id.Important);
        Contacts = findViewById(R.id.Contacts);
        About = findViewById(R.id.About);
        SignOut = findViewById(R.id.SignOut);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        MainAccountStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()){
                    //IF THE ACCOUNT IS VERIFIED
                    //Toast.makeText(MainMenuActivity.this, "Verified account", Toast.LENGTH_SHORT).show();
                    AnimationVerifiedAccount();
                }else {
                    //IF THE ACCOUNT IS NOT VERIFIED
                    VerifyEmailAccount();
                }
            }
        });

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
        Important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, Important_Notes_Activity.class));
                Toast.makeText(MainMenuActivity.this, "Important notes", Toast.LENGTH_SHORT).show();
            }
        });
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenuActivity.this, "Contacts", Toast.LENGTH_SHORT).show();
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

    private void VerifyEmailAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify account")
                .setMessage("Press Confirm to send the verification to the email: "
                +user.getEmail())
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendMailForVerification();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainMenuActivity.this, "Operation canceled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void SendMailForVerification() {
        progressDialog.setMessage("sending confirmation to your inbox "+ user.getEmail());
        progressDialog.show();

        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //SUCCESSFULLY
                        progressDialog.dismiss();
                        Toast.makeText(MainMenuActivity.this, "Please check the account in your inbox "+ user.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //FAILED CONFIRMATION
                        Toast.makeText(MainMenuActivity.this, "An error has occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CheckAccountVerification() {
        String Verified = "Verified";
        String Not_Verified = "Not verified";
        if (user.isEmailVerified()){
            MainAccountStatus.setText(Verified);
            MainAccountStatus.setBackgroundColor(Color.rgb(255, 255, 255));
        }else {
            MainAccountStatus.setText(Not_Verified);
            MainAccountStatus.setBackgroundColor(Color.rgb(244, 206, 115));

        }
    }

    private void AnimationVerifiedAccount() {
        Button Understood_Verified;

        dialog_verified_account.setContentView(R.layout.verified_account_dialog);

        Understood_Verified = dialog_verified_account.findViewById(R.id.Understood_Verified);

        Understood_Verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_verified_account.dismiss();
            }
        });
        dialog_verified_account.show();
        dialog_verified_account.setCanceledOnTouchOutside(false);
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

        CheckAccountVerification();

        Users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if user exist
                if (snapshot.exists()) {
                    //hide progressbar
                    ProgressBarData.setVisibility(View.GONE);
                    //SHOWING TEXTVIEW
                    //MailMain.setVisibility(View.VISIBLE);
                    Linear_Names.setVisibility(View.VISIBLE);
                    Linear_Mail.setVisibility(View.VISIBLE);
                    Linear_Verification.setVisibility(View.VISIBLE);

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
                    Important.setEnabled(true);
                    Contacts.setEnabled(true);
                    About.setEnabled(true);
                    SignOut.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Profile_User){
            startActivity(new Intent(MainMenuActivity.this,Profile_User_Activity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void ExitApp() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
    }
}