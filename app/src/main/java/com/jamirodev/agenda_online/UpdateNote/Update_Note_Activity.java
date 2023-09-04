package com.jamirodev.agenda_online.UpdateNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.AddNote.Add_Note_Activity;
import com.jamirodev.agenda_online.R;

import java.util.Calendar;

public class Update_Note_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView Id_Note_Update, Uid_User_Update, Mail_User_Update, Date_Register_Update, Date_Update, State_Update, New_State;
    EditText Title_Update, Description_Update;
    Button Btn_calendar_Update;

    //    DECLARE STRINGS TO STORE THEM
    String id_note_R, uid_user_R, mail_user_R, date_register_R, title_R, description_R, date_R, state_R;
    ImageView Task_Ended, Task_Incomplete;
    Spinner Spinner_state;
    int day, month, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Update note");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitViews();
        DataRecover();
        DataSet();
        CheckNoteStatus();
        Spinner_Status();

        Btn_calendar_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDate();
            }
        });
    }

    private void InitViews() {
        Id_Note_Update = findViewById(R.id.Id_Note_Update);
        Uid_User_Update = findViewById(R.id.Uid_User_Update);
        Mail_User_Update = findViewById(R.id.Mail_User_Update);
        Date_Register_Update = findViewById(R.id.Date_Register_Update);
        Date_Update = findViewById(R.id.Date_Update);
        State_Update = findViewById(R.id.State_Update);
        Title_Update = findViewById(R.id.Title_Update);
        Description_Update = findViewById(R.id.Description_Update);
        Btn_calendar_Update = findViewById(R.id.Btn_calendar_Update);

        Task_Ended = findViewById(R.id.Task_Ended);
        Task_Incomplete = findViewById(R.id.Task_Incomplete);

        Spinner_state = findViewById(R.id.Spinner_State);
        New_State = findViewById(R.id.New_State);
    }

    private void DataRecover() {
        Bundle intent = getIntent().getExtras();

        assert intent != null;
        id_note_R = intent.getString("id_note");
        uid_user_R = intent.getString("uid_user");
        mail_user_R = intent.getString("mail_user");
        date_register_R = intent.getString("date_register");
        title_R = intent.getString("title");
        description_R = intent.getString("description");
        date_R = intent.getString("date_note");
        state_R = intent.getString("state");

    }

    private void DataSet() {
        Id_Note_Update.setText(id_note_R);
        Uid_User_Update.setText(uid_user_R);
        Mail_User_Update.setText(mail_user_R);
        Date_Register_Update.setText(date_register_R);
        Date_Update.setText(date_R);
        State_Update.setText(state_R);
        Title_Update.setText(title_R);
        Description_Update.setText(description_R);
    }

    private void CheckNoteStatus() {
        String state_note = State_Update.getText().toString();

        if (state_note.equals("Not finished")) {
            Task_Incomplete.setVisibility(View.VISIBLE);
        }
        if (state_note.equals("finished")) {
            Task_Ended.setVisibility(View.VISIBLE);
        }
    }

    private void SelectDate() {
        final Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Update_Note_Activity.this, new DatePickerDialog.OnDateSetListener() {
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
                Date_Update.setText(dayFormated + "/" + monthFormated + "/" + YearSelected);

            }
        }
                , year, month, day);
        datePickerDialog.show();
    }

    private void Spinner_Status() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Notes_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_state.setAdapter(adapter);
        Spinner_state.setOnItemSelectedListener(this);
    }

    private void UpdateNoteDB() {
        String titleUpdate = Title_Update.getText().toString();
        String descriptionUpdate = Description_Update.getText().toString();
        String dateUpdate = Date_Update.getText().toString();
        String stateUpdate = New_State.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Published notes");

        Query query = databaseReference.orderByChild("id_note").equalTo(id_note_R);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("title").setValue(titleUpdate);
                    ds.getRef().child("description").setValue(descriptionUpdate);
                    ds.getRef().child("date_note").setValue(dateUpdate);
                    ds.getRef().child("state").setValue(stateUpdate);
                }
                Toast.makeText(Update_Note_Activity.this, "Note updated", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String ACTUAL_STATE = State_Update.getText().toString();

        String Position_1 = adapterView.getItemAtPosition(1).toString();

        String state_selected = adapterView.getItemAtPosition(i).toString();
        New_State.setText(state_selected);

        if (ACTUAL_STATE.equals("finished")){
            New_State.setText(Position_1);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_update, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Update_Note_BD) {
            UpdateNoteDB();
//            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}