package com.jamirodev.agenda_online.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamirodev.agenda_online.R;

public class List_Contacts_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Add_contact) {
            String Uid_recovered = getIntent().getStringExtra("Uid");
            Intent intent = new Intent(List_Contacts_Activity.this, Add_Contact_Activity.class);
            intent.putExtra("Uid", Uid_recovered);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}