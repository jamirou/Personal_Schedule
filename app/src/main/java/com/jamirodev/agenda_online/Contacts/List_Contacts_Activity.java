package com.jamirodev.agenda_online.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jamirodev.agenda_online.Objects.Contact;
import com.jamirodev.agenda_online.R;
import com.jamirodev.agenda_online.ViewHolder.ViewHolderContact;

public class List_Contacts_Activity extends AppCompatActivity {

    RecyclerView RVContacts;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference DB_Users;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Contact, ViewHolderContact> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Contact> firebaseRecyclerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Contacts");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        RVContacts = findViewById(R.id.RVContacts);
        RVContacts.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DB_Users = firebaseDatabase.getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        ListContacts();
    }

    private void ListContacts() {
        Query query = DB_Users.child(user.getUid()).child("Contacts").orderByChild("Name");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(query, Contact.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contact, ViewHolderContact>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContact viewHolderContact, int position, @NonNull Contact contact) {
                viewHolderContact.SetDataContact(
                        getApplicationContext(),
                        contact.getId_contact(),
                        contact.getUid_contact(),
                        contact.getName(),
                        contact.getLastname(),
                        contact.getMail(),
                        contact.getPhone(),
                        contact.getAge(),
                        contact.getHome(),
                        contact.getImage()
                );
            }

            @NonNull
            @Override
            public ViewHolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
                ViewHolderContact viewHolderContact = new ViewHolderContact(view);
                viewHolderContact.setOnClickListener(new ViewHolderContact.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(List_Contacts_Activity.this, "On item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(List_Contacts_Activity.this, "On item log click", Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolderContact;
            }
        };

        RVContacts.setLayoutManager(new GridLayoutManager(List_Contacts_Activity.this, 2 ));
        firebaseRecyclerAdapter.startListening();
        RVContacts.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}