package com.jamirodev.agenda_online.ListNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamirodev.agenda_online.Objects.Note;
import com.jamirodev.agenda_online.R;
import com.jamirodev.agenda_online.ViewHolder.ViewHolder_Note;

public class List_Notes_Activity extends AppCompatActivity {

    RecyclerView rvNotes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference DATA_BASE;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Note, ViewHolder_Note> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Note> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notes");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DATA_BASE = firebaseDatabase.getReference("Published notes");
        ListNotesUsers();
    }

    private void ListNotesUsers() {
        options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(DATA_BASE, Note.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Note, ViewHolder_Note>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Note viewHolder_note, int position, @NonNull Note note) {
                viewHolder_note.SetData(
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
            public ViewHolder_Note onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent,false);
                ViewHolder_Note viewHolder_note = new ViewHolder_Note(view);
                viewHolder_note.setOnClickListener(new ViewHolder_Note.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(List_Notes_Activity.this, "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(List_Notes_Activity.this, "on item long click", Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder_note;
            }
        };

        linearLayoutManager = new LinearLayoutManager(List_Notes_Activity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        rvNotes.setLayoutManager(linearLayoutManager);
        rvNotes.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}