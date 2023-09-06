package com.jamirodev.agenda_online.Contacts;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.jamirodev.agenda_online.Profile.Profile_User_Activity;
import com.jamirodev.agenda_online.R;

public class Update_Contact_Activity extends AppCompatActivity {

    ImageView Image_C_U, Update_Image_C_U, Update_Phone_C_U;
    TextView Id_Contact_Update, Uid_Contact_Update, Phone_Contact_Update;
    EditText Name_Contact_Update, Lastname_Contact_Update, Mail_Contact_Update, Age_Contact_Update, Home_Contact_Update;
    Button Update_Contact_Update;
    String id_c, uid_user, name_c, lastname_C, mail_c, phone_c, age_c, home_c;
    Dialog dialog_Establish_phone;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        InitViews();
        RecoverData();
        SetDataRecovered();
        GetImage();

        Update_Phone_C_U.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establish_Phone_User();
            }
        });

        Update_Contact_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_Contact_Info();
            }
        });

        Update_Image_C_U.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Update_Contact_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    SelectGalleryImage();
                }
                else {
                    RequestPermissionGallery.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

    }

    private void InitViews() {
        Image_C_U = findViewById(R.id.Image_C_U);
        Update_Image_C_U = findViewById(R.id.Update_Image_C_U);
        Update_Phone_C_U = findViewById(R.id.Update_Phone_C_U);
        Id_Contact_Update = findViewById(R.id.Id_Contact_Update);
        Uid_Contact_Update = findViewById(R.id.Uid_Contact_Update);
        Phone_Contact_Update = findViewById(R.id.Phone_Contact_Update);
        Name_Contact_Update = findViewById(R.id.Name_Contact_Update);
        Lastname_Contact_Update = findViewById(R.id.Lastname_Contact_Update);
        Mail_Contact_Update = findViewById(R.id.Mail_Contact_Update);
        Age_Contact_Update = findViewById(R.id.Age_Contact_Update);
        Home_Contact_Update = findViewById(R.id.Home_Contact_Update);
        Update_Contact_Update = findViewById(R.id.Update_Contact_Update);

        dialog_Establish_phone = new Dialog(Update_Contact_Activity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    private void RecoverData(){
        Bundle bundle = getIntent().getExtras();
        id_c = bundle.getString("id_c");
        uid_user = bundle.getString("uid_user");
        name_c = bundle.getString("name_c");
        lastname_C = bundle.getString("lastname_c");
        mail_c = bundle.getString("mail_c");
        phone_c = bundle.getString("phone_c");
        age_c = bundle.getString("age_c");
        home_c = bundle.getString("home_c");
    }

    private void SetDataRecovered() {
        Id_Contact_Update.setText(id_c);
        Uid_Contact_Update.setText(uid_user);
        Name_Contact_Update.setText(name_c);
        Lastname_Contact_Update.setText(lastname_C);
        Mail_Contact_Update.setText(mail_c);
        Phone_Contact_Update.setText(phone_c);
        Age_Contact_Update.setText(age_c);
        Home_Contact_Update.setText(home_c);
    }

    private void GetImage() {
        String image_c = getIntent().getStringExtra("image_c");
        try {

            Glide.with(getApplicationContext()).load(image_c).placeholder(R.drawable.contactuser).into(Image_C_U);


        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Establish_Phone_User() {
        CountryCodePicker ccp;
        EditText Establish_Phone;
        Button Btn_Accept_phone;

        dialog_Establish_phone.setContentView(R.layout.box_set_phone);

        ccp = dialog_Establish_phone.findViewById(R.id.ccp);
        Establish_Phone = dialog_Establish_phone.findViewById(R.id.Establish_Phone);
        Btn_Accept_phone = dialog_Establish_phone.findViewById(R.id.Btn_Accept_phone);

        Btn_Accept_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country_code = ccp.getSelectedCountryCodeWithPlus();
                String phone = Establish_Phone.getText().toString();
                String code_country_phone = country_code+phone;

                if (!phone.equals("")){
                    Phone_Contact_Update.setText(code_country_phone);
                    dialog_Establish_phone.dismiss();
                }else {
                    Toast.makeText(Update_Contact_Activity.this, "Add phone number", Toast.LENGTH_SHORT).show();
                    dialog_Establish_phone.dismiss();
                }
            }
        });

        dialog_Establish_phone.show();
        dialog_Establish_phone.setCanceledOnTouchOutside(true);
    }

    private void Update_Contact_Info() {
        String NameUpdate = Name_Contact_Update.getText().toString().trim();
        String LastnameUpdate = Lastname_Contact_Update.getText().toString().trim();
        String MailUpdate=  Mail_Contact_Update.getText().toString().trim();
        String PhoneUpdate = Phone_Contact_Update.getText().toString().trim();
        String AgeUpdate = Age_Contact_Update.getText().toString().trim();
        String HomeUpdate = Home_Contact_Update.getText().toString().trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

        Query query = databaseReference.child(user.getUid()).child("Contacts").orderByChild("id_contact").equalTo(id_c);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    ds.getRef().child("name").setValue(NameUpdate);
                    ds.getRef().child("lastname").setValue(LastnameUpdate);
                    ds.getRef().child("mail").setValue(MailUpdate);
                    ds.getRef().child("phone").setValue(PhoneUpdate);
                    ds.getRef().child("age").setValue(AgeUpdate);
                    ds.getRef().child("home").setValue(HomeUpdate);
                }
                Toast.makeText(Update_Contact_Activity.this, "Updated information", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SelectGalleryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Image_C_U.setImageURI(imageUri);
                    }else {
                        Toast.makeText(Update_Contact_Activity.this, "Canceled by user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private  ActivityResultLauncher<String> RequestPermissionGallery = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    SelectGalleryImage();
                }else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

}