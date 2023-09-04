package com.jamirodev.agenda_online.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hbb20.CountryCodePicker;
import com.jamirodev.agenda_online.MainMenuActivity;
import com.jamirodev.agenda_online.R;

import java.util.Calendar;
import java.util.HashMap;

public class Profile_User_Activity extends AppCompatActivity {
    ImageView ProfileImage;
    TextView Email_Profile, Phone_Profile, Uid_Profile, Birthdate_Profile;
    EditText Names_Profile, Last_Name_Profile, Age_Profile,
            Home_Profile, College_Profile, Profession_Profile;
    ImageView Edit_Phone, Edit_Date;

    Button Save_Data;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Users;
    Dialog dialog_Establish_phone;
    int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InitVariables();
        Edit_Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establish_Phone_User();
            }
        });

        Edit_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Open_Calendar();
            }
        });

        Save_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });
    }

    private void InitVariables() {
        ProfileImage = findViewById(R.id.ProfileImage);
        Email_Profile = findViewById(R.id.Email_Profile);
        Uid_Profile = findViewById(R.id.Uid_Profile);
        Names_Profile = findViewById(R.id.Names_Profile);
        Last_Name_Profile = findViewById(R.id.Last_Name_Profile);
        Age_Profile = findViewById(R.id.Age_Profile);
        Phone_Profile = findViewById(R.id.Phone_Profile);
        Home_Profile = findViewById(R.id.Home_Profile);
        College_Profile = findViewById(R.id.College_Profile);
        Profession_Profile = findViewById(R.id.Profession_Profile);
        Birthdate_Profile = findViewById(R.id.Birthdate_Profile);

        Edit_Phone = findViewById(R.id.Edit_Phone);
        Edit_Date = findViewById(R.id.Edit_Date);

        dialog_Establish_phone = new Dialog(Profile_User_Activity.this);

        Save_Data = findViewById(R.id.Save_Data);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Users = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void ReadingData() {
        Users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    GET DATA
                    String uid = "" + snapshot.child("uid").getValue();
                    String name = "" + snapshot.child("Names").getValue();
                    String lastname = "" + snapshot.child("Last name").getValue();
                    String mail = "" + snapshot.child("Gmail").getValue();
                    String age = "" + snapshot.child("Age").getValue();
                    String phone = "" + snapshot.child("Phone").getValue();
                    String home = "" + snapshot.child("Home").getValue();
                    String college = "" + snapshot.child("College").getValue();
                    String profession = "" + snapshot.child("Profession").getValue();
                    String birthrate = "" + snapshot.child("Birthdate").getValue();
                    String image_profile = "" + snapshot.child("Profile_img").getValue();

                    //SET DATA
                    Uid_Profile.setText(uid);
                    Names_Profile.setText(name);
                    Last_Name_Profile.setText(lastname);
                    Email_Profile.setText(mail);
                    Age_Profile.setText(age);
                    Phone_Profile.setText(phone);
                    Home_Profile.setText(home);
                    College_Profile.setText(college);
                    Profession_Profile.setText(profession);
                    Birthdate_Profile.setText(birthrate);

                    Load_Image(image_profile);
                } else {
                    Toast.makeText(Profile_User_Activity.this, "Waiting for data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile_User_Activity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Load_Image(String imageProfile) {
        try {
            //IMAGE IS DISPLAYED WHEN FETCHED FROM FIREBASE
            Glide.with(getApplicationContext()).load(imageProfile).placeholder(R.drawable.user).into(ProfileImage);

        } catch (Exception e) {
            //IF THE FIREBASE IMAGE DOESN'T LOAD USE THIS OTHER
            Glide.with(getApplicationContext()).load(R.drawable.user).into(ProfileImage);
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
                    Phone_Profile.setText(code_country_phone);
                    dialog_Establish_phone.dismiss();
                }else {
                    Toast.makeText(Profile_User_Activity.this, "Add phone number", Toast.LENGTH_SHORT).show();
                    dialog_Establish_phone.dismiss();
                }
            }
        });

        dialog_Establish_phone.show();
        dialog_Establish_phone.setCanceledOnTouchOutside(true);
    }

    private void Open_Calendar() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Profile_User_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YearSelected, int MonthSelected, int DaySelected) {

                String dayFormatted, monthFormatted;

                //OBTAIN DAY
                if (DaySelected < 10) {
                    dayFormatted = "0" + String.valueOf(DaySelected);
                } else {
                    dayFormatted = String.valueOf(DaySelected);
                }

                //OBTAIN MONTH
                int Month = MonthSelected + 1;
                if (Month < 10) {
                    monthFormatted = "0" + String.valueOf(Month);
                } else {
                    monthFormatted = String.valueOf(Month);
                }

                //SET DATE IN TV
                Birthdate_Profile.setText(dayFormatted + "/" + monthFormatted + "/" + YearSelected);

            }
        }
                , year, month, day);
        datePickerDialog.show();
    }

    private void UpdateData() {
        String U_Name = Names_Profile.getText().toString().trim();
        String U_LastName = Last_Name_Profile.getText().toString().trim();
        String U_Age = Age_Profile.getText().toString().trim();
        String U_Phone = Phone_Profile.getText().toString().trim();
        String U_Home = Home_Profile.getText().toString().trim();
        String U_College = College_Profile.getText().toString().trim();
        String U_Profession = Profession_Profile.getText().toString().trim();
        String U_Birthdate = Birthdate_Profile.getText().toString().trim();

        HashMap<String, Object> update_data = new HashMap<>();
        update_data.put("Names", U_Name);
        update_data.put("Last name", U_LastName);
        update_data.put("Age", U_Age);
        update_data.put("Phone", U_Phone);
        update_data.put("Home", U_Home);
        update_data.put("College", U_College);
        update_data.put("Profession", U_Profession);
        update_data.put("Birthdate", U_Birthdate);

        Users.child(user.getUid()).updateChildren(update_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Profile_User_Activity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile_User_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void CheckLogin() {
        if (user != null) {
            ReadingData();
        } else {
            startActivity(new Intent(Profile_User_Activity.this, MainMenuActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        CheckLogin();
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}