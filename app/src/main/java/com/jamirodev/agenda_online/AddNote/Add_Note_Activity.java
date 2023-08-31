package com.jamirodev.agenda_online.AddNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamirodev.agenda_online.Objects.Note;
import com.jamirodev.agenda_online.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add_Note_Activity extends AppCompatActivity {
    TextView Uid_User, Mail_User, Date_ActualHour, Date, State;
    EditText Title, Description;
    Button Btn_calendar;
    int day, month, year;
    DatabaseReference DB_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitVar();
        ObtainData();
        Obtain_Date_Hour();

        Btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Note_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int YearSelected, int MonthSelected, int DaySelected) {

                        String dayFormated, monthFormated;

                        //OBTAIN DAY
                        if (DaySelected < 10) {
                            dayFormated = "0" + String.valueOf(DaySelected);
                        } else {
                            dayFormated = String.valueOf(DaySelected);
                        }

                        //OBTAIN MONTH
                        int Month = MonthSelected + 1;
                        if (Month < 10) {
                            monthFormated = "0" + String.valueOf(Month);
                        } else {
                            monthFormated = String.valueOf(Month);
                        }

                        //SET DATE IN TV
                        Date.setText(dayFormated + "/" + monthFormated + "/" + YearSelected);

                    }
                }
                        , year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void InitVar() {
        Uid_User = findViewById(R.id.Uid_User);
        Mail_User = findViewById(R.id.Mail_User);
        Date_ActualHour = findViewById(R.id.Date_ActualHour);
        Date = findViewById(R.id.Date);
        State = findViewById(R.id.State);

        Title = findViewById(R.id.Title);
        Description = findViewById(R.id.Description);
        Btn_calendar = findViewById(R.id.Btn_calendar);

        DB_Firebase = FirebaseDatabase.getInstance().getReference();
    }

    private void ObtainData() {
        String uid_recovered = getIntent().getStringExtra("Uid");
        String mail_recovered = getIntent().getStringExtra("Gmail");

        Uid_User.setText(uid_recovered);
        Mail_User.setText(mail_recovered);
    }

    private void Obtain_Date_Hour() {
        String Date_Hour_Register = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis());
        Date_ActualHour.setText(Date_Hour_Register);
    }

    private void Add_Note(){

        //DATA OBTAIN
        String uid_user = Uid_User.getText().toString();
        String mail_user = Mail_User.getText().toString();
        String date_hour_actual = Date_ActualHour.getText().toString();
        String title = Title.getText().toString();
        String description = Description.getText().toString();
        String date = Date.getText().toString();
        String state = State.getText().toString();

        //VALIDATE DATA
        if (!uid_user.equals("") && !mail_user.equals("") && !date_hour_actual.equals("") &&
                !title.equals("") && !description.equals("") && !date.equals("") && !state.equals("")) {
            Note note = new Note(mail_user+"/"+date_hour_actual,
                    uid_user,
                    mail_user,
                    date_hour_actual,
                    title,
                    description,
                    date,
                    state);

            String Note_user = DB_Firebase.push().getKey();
            //SET THE NAME OF THE DB
            String Name_DB = "Published notes";

            DB_Firebase.child(Name_DB).child(Note_user).setValue(note);
            Toast.makeText(this, "Successfully added note", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        else {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Add_NoteDB) {
            Add_Note();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}