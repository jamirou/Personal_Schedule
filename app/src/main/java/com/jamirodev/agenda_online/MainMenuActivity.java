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
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.Configuration.Configuration_Activity;
import com.jamirodev.agenda_online.Notes.Add_Note_Activity;
import com.jamirodev.agenda_online.Contacts.List_Contacts_Activity;
import com.jamirodev.agenda_online.Notes.Important_Notes_Activity;
import com.jamirodev.agenda_online.Notes.List_Notes_Activity;
import com.jamirodev.agenda_online.Profile.Profile_User_Activity;

import java.text.SimpleDateFormat;
import java.util.Date;

/** @noinspection ALL*/
public class MainMenuActivity extends AppCompatActivity {

    Button AddNotes, ListNotes, Important, Contacts, About, SignOut;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ImageView Image_user;
    TextView UidMain, NamesMain, MailMain;
    Button MainAccountStatus;
    ProgressBar ProgressBarData;
    ProgressDialog progressDialog;
    LinearLayoutCompat Linear_Names, Linear_Mail, Linear_Verification;
    DatabaseReference Users;
    Dialog dialog_verified_account, dialog_Info, dialog_Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");

        Image_user = findViewById(R.id.Image_user);
        UidMain = findViewById(R.id.UidMain);
        NamesMain = findViewById(R.id.NamesMain);
        MailMain = findViewById(R.id.MailMain);
        MainAccountStatus = findViewById(R.id.MainAccountStatus);
        ProgressBarData = findViewById(R.id.ProgressBarData);
        dialog_verified_account = new Dialog(this);
        dialog_Info = new Dialog(this);
        dialog_Date = new Dialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere...");
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
                Toast.makeText(MainMenuActivity.this, "Notas", Toast.LENGTH_SHORT).show();
            }
        });
        Important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, Important_Notes_Activity.class));
                Toast.makeText(MainMenuActivity.this, "Notas importantes", Toast.LENGTH_SHORT).show();
            }
        });
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainMenuActivity.this, "Contacts", Toast.LENGTH_SHORT).show();
                String Uid_user = UidMain.getText().toString();
                Intent intent = new Intent(MainMenuActivity.this, List_Contacts_Activity.class);
                intent.putExtra("Uid", Uid_user);
                startActivity(intent);
            }
        });

        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Information();
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
        builder.setTitle("Verificar cuenta")
                .setMessage("Presiona confirmar para enviar un correo de confirmacion a tu gmal"
                +user.getEmail())
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SendMailForVerification();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainMenuActivity.this, "Operacion cancelada", Toast.LENGTH_SHORT).show();
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
        String Verified = "Verificado";
        String Not_Verified = "No verificado";
        if (user.isEmailVerified()){
            MainAccountStatus.setText(Verified);
            MainAccountStatus.setBackgroundColor(Color.rgb(255, 255, 255));
        }else {
            MainAccountStatus.setText(Not_Verified);
            MainAccountStatus.setBackgroundColor(Color.rgb(252, 200, 209));

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

    private void Information() {

        ImageView facebook, instagram;
        Button CloseInfo;

        dialog_Info.setContentView(R.layout.dialog_box_information);
        CloseInfo = dialog_Info.findViewById(R.id.CloseInfo);
        facebook = dialog_Info.findViewById(R.id.facebook);
        instagram = dialog_Info.findViewById(R.id.instagram);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pfacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100089372131465"));
                startActivity(Pfacebook);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Pinstagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/jam3312_/"));
                startActivity(Pinstagram);
            }
        });

        CloseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_Info.dismiss();
            }
        });
        dialog_Info.show();
        dialog_Info.setCanceledOnTouchOutside(false);
    }

    private void DisplayDate() {
        TextView Today;
        Button Btn_close;

        dialog_Date.setContentView(R.layout.dialog_box_date);

        Today = dialog_Date.findViewById(R.id.Today);
        Btn_close = dialog_Date.findViewById(R.id.Btn_close);

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy");
        String DATE = simpleDateFormat.format(date);
        Today.setText(DATE);

        Btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_Date.dismiss();
            }
        });

        dialog_Date.show();
        dialog_Date.setCanceledOnTouchOutside(false);
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
                    String image = "" + snapshot.child("Profile_img").getValue();

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

                    GetImage(image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetImage(String image) {
        try {
            Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.user).into(Image_user);
        }catch (Exception e){
            Glide.with(getApplicationContext()).load(R.drawable.user).into(Image_user);

        }
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
        if (item.getItemId() == R.id.Calendar){
            DisplayDate();
        }
        if (item.getItemId() == R.id.Configuration){
            String uid_user = UidMain.getText().toString();

            Intent intent = new Intent(MainMenuActivity.this, Configuration_Activity.class);
            intent.putExtra("Uid", uid_user);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void ExitApp() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
        Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
    }
}