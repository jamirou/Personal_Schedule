package com.jamirodev.agenda_online.ImportantNotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jamirodev.agenda_online.Details.Detail_Note_Activity;
import com.jamirodev.agenda_online.ListNote.List_Notes_Activity;
import com.jamirodev.agenda_online.Objects.Note;
import com.jamirodev.agenda_online.R;
import com.jamirodev.agenda_online.UpdateNote.Update_Note_Activity;
import com.jamirodev.agenda_online.ViewHolder.ViewHolder_Important_Note;
import com.jamirodev.agenda_online.ViewHolder.ViewHolder_Note;

public class Important_Notes_Activity extends AppCompatActivity {

    RecyclerView RVImportantNotes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference My_Users;
    DatabaseReference Important_Notes;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Note, ViewHolder_Important_Note> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Note> firebaseRecyclerOptions;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_notes);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Important notes");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        RVImportantNotes = findViewById(R.id.RVImportantNotes);
        RVImportantNotes.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        My_Users = firebaseDatabase.getReference("Users");
        Important_Notes = firebaseDatabase.getReference("Important Notes");

        CheckUser();
    }

    private void CheckUser() {
        if (user == null) {
            Toast.makeText(Important_Notes_Activity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
        }else {
            ListImportantNotes();
        }
    }

    private void ListImportantNotes() {
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Note>().setQuery(My_Users.child(user.getUid()).child("Important Notes"), Note.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Note, ViewHolder_Important_Note>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Important_Note viewHolder_important_note, int position, @NonNull Note note) {
                viewHolder_important_note.SetData(
                        getApplicationContext(),
                        note.getId_note(),
                        note.getUid_user(),
                        note.getMail_user(),
                        note.getDate_actual_hour(),
                        note.getTitle(),
                        note.getDescription(),
                        note.getDate_note(),
                        note.getState()
                );
            }
            @NonNull
            @Override
            public ViewHolder_Important_Note onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_important_note, parent, false);
                ViewHolder_Important_Note viewHolder_important_note = new ViewHolder_Important_Note(view);
                viewHolder_important_note.setOnClickListener(new ViewHolder_Important_Note.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id_note = getItem(position).getId_note();
                        String uid_user = getItem(position).getUid_user();
                        String mail_user = getItem(position).getMail_user();
                        String date_register = getItem(position).getDate_actual_hour();
                        String title = getItem(position).getTitle();
                        String description = getItem(position).getDescription();
                        String date_note = getItem(position).getDate_note();
                        String state = getItem(position).getState();

                        //SEND DATA TO DETAIL ACTIVITY
                        Intent intent = new Intent(Important_Notes_Activity.this, Detail_Note_Activity.class);
                        intent.putExtra("id_note", id_note);
                        intent.putExtra("uid_user", uid_user);
                        intent.putExtra("mail_user", mail_user);
                        intent.putExtra("date_register", date_register);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        intent.putExtra("date_note", date_note);
                        intent.putExtra("state", state);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }

                });
                return viewHolder_important_note;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Important_Notes_Activity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        RVImportantNotes.setLayoutManager(linearLayoutManager);
        RVImportantNotes.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStart() {
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}








