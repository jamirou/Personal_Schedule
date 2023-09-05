package com.jamirodev.agenda_online.Contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.jamirodev.agenda_online.Objects.Contact;
import com.jamirodev.agenda_online.Profile.Profile_User_Activity;
import com.jamirodev.agenda_online.R;

public class Add_Contact_Activity extends AppCompatActivity {

    TextView Uid_User_C, Phone_C;
    EditText Names_C, Lastname_C, Mail_C, Age_C, Home_C;
    ImageView Edit_Phone_C;
    Button Btn_Save_Contact;
    Dialog dialog_Establish_phone;
    DatabaseReference DB_Users;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        InitVariables();
        GetUidUser();

        Edit_Phone_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establish_Phone_Contact();
            }
        });

        Btn_Save_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddContact();
            }
        });
    }

    private void InitVariables() {
        Uid_User_C = findViewById(R.id.Uid_User_C);
        Phone_C = findViewById(R.id.Phone_C);
        Names_C = findViewById(R.id.Names_C);
        Lastname_C = findViewById(R.id.Lastname_C);
        Mail_C = findViewById(R.id.Mail_C);
        Age_C = findViewById(R.id.Age_C);
        Home_C = findViewById(R.id.Home_C);
        Edit_Phone_C = findViewById(R.id.Edit_Phone_C);
        Btn_Save_Contact = findViewById(R.id.Btn_Save_Contact);

        dialog_Establish_phone = new Dialog(Add_Contact_Activity.this);
        DB_Users = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    private void GetUidUser() {
        String UidRecovered = getIntent().getStringExtra("Uid");
        Uid_User_C.setText(UidRecovered);
    }

    private void Establish_Phone_Contact() {
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
                    Phone_C.setText(code_country_phone);
                    dialog_Establish_phone.dismiss();
                }else {
                    Toast.makeText(Add_Contact_Activity.this, "Add phone number", Toast.LENGTH_SHORT).show();
                    dialog_Establish_phone.dismiss();
                }
            }
        });

        dialog_Establish_phone.show();
        dialog_Establish_phone.setCanceledOnTouchOutside(true);
    }

    private void AddContact() {
        String uid = Uid_User_C.getText().toString();
        String names = Names_C.getText().toString();
        String lastname = Lastname_C.getText().toString();
        String mail = Mail_C.getText().toString();
        String phone = Phone_C.getText().toString();
        String age = Age_C.getText().toString();
        String home = Home_C.getText().toString();

        String id_contact = DB_Users.push().getKey();

        if (!uid.equals("") && !names.equals("")){
            Contact contact = new Contact(
                    id_contact,
                    uid,
                    names,
                    lastname,
                    mail,
                    phone,
                    age,
                    home,
                    "");

//            ESTABLISH DB NAME WHERE CONTACTS WILL BE SAVED
            String Name_DB = "Contacts";
            assert id_contact != null;
            DB_Users.child(user.getUid()).child(Name_DB).child(id_contact).setValue(contact);
            Toast.makeText(this, "Added contact", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
        }
    }

}