package com.jamirodev.agenda_online.Contacts;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jamirodev.agenda_online.R;

public class Detail_Contact_Activity extends AppCompatActivity {
    ImageView Image_Contact_D;
    TextView Id_Contact_D, Uid_User_D, Name_Contact_D, Lastname_Contact_D, Mail_Contact_D, Age_Contact_D, Phone_Contact_D, Home_Contact_D;

    String id_c, uid_user, name_c, lastname_c, mail_c, age_c, phone_c, home_c;
    Button Call_C, Message_C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Contact detail");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InitVariables();
        RecoverDataContact();
        SetDataContact();
        GetImageContact();

        Call_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Detail_Contact_Activity.this,
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    CallContact();
                }else{
                    CallPermissionRequest.launch(Manifest.permission.CALL_PHONE);
                }
            }
        });

        Message_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Detail_Contact_Activity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    SendMessage();
                }else{
                    MessagePermissionRequest.launch(Manifest.permission.SEND_SMS);
                }
            }
        });
    }

    private void InitVariables() {
        Image_Contact_D = findViewById(R.id.Image_Contact_D);
        Id_Contact_D = findViewById(R.id.Id_Contact_D);
        Uid_User_D = findViewById(R.id.Uid_User_D);
        Name_Contact_D = findViewById(R.id.Name_Contact_D);
        Lastname_Contact_D = findViewById(R.id.Lastname_Contact_D);
        Mail_Contact_D = findViewById(R.id.Mail_Contact_D);
        Age_Contact_D = findViewById(R.id.Age_Contact_D);
        Phone_Contact_D = findViewById(R.id.Phone_Contact_D);
        Home_Contact_D = findViewById(R.id.Home_Contact_D);

        Call_C = findViewById(R.id.Call_C);
        Message_C = findViewById(R.id.Message_C);
    }

    private void RecoverDataContact(){
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        id_c = bundle.getString("id_c");
        uid_user = bundle.getString("uid_user");
        name_c = bundle.getString("name_c");
        lastname_c = bundle.getString("lastname_c");
        mail_c = bundle.getString("mail_c");
        phone_c = bundle.getString("phone_c");
        age_c = bundle.getString("age_c");
        home_c = bundle.getString("home_c");
    }

    private void SetDataContact() {
        Id_Contact_D.setText(id_c);
        Uid_User_D.setText(uid_user);
        Name_Contact_D.setText("Name: "+name_c);
        Lastname_Contact_D.setText("Last Name: "+lastname_c);
        Mail_Contact_D.setText("Mail: "+mail_c);
        Phone_Contact_D.setText(phone_c);
        Age_Contact_D.setText("Age: "+age_c);
        Home_Contact_D.setText("Home: "+home_c);
    }

    private void GetImageContact(){
        String image = getIntent().getStringExtra("image_c");

        try {

            Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.contactuser).into(Image_Contact_D);

        }catch (Exception e){
            Toast.makeText(this, "Waiting Image", Toast.LENGTH_SHORT).show();

        }
    }

    private void CallContact() {
        String phone = Phone_Contact_D.getText().toString();
        if (!phone.equals("")){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+phone));
            startActivity(intent);
        }else {
            Toast.makeText(this, "There's no phone to call", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendMessage(){
        String phone = Phone_Contact_D.getText().toString();
        if (!phone.equals("")){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+phone));
            intent.putExtra("sms_body", "");
            startActivity(intent);
        }else {
            Toast.makeText(this, "There's no phone to text", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<String> CallPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    CallContact();
                }else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String> MessagePermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    SendMessage();
                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}