package com.jamirodev.agenda_online.ListNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

    Dialog dialog;

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
        dialog = new Dialog(List_Notes_Activity.this);
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

                        String id_note = getItem(position).getId_note();

//                        DECLARE VIEWS
                        Button CD_Delete, CD_Update;

//                        CONNECT VIEWS WITH DESIGN
                        dialog.setContentView(R.layout.dialog_options);
                        CD_Delete = dialog.findViewById(R.id.CD_Delete);
                        CD_Update = dialog.findViewById(R.id.CD_Update);

                        CD_Delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DeleteNote(id_note);
                                dialog.dismiss();
                            }
                        });
                        CD_Update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(List_Notes_Activity.this, "Update note", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
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

    private void DeleteNote(String idNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(List_Notes_Activity.this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                DELETE NOTE FROM DB
                Query query = DATA_BASE.orderByChild("id_note").equalTo(idNote);
//                HERE COULD BE AN ERROR IN ID NOTE
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(List_Notes_Activity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(List_Notes_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(List_Notes_Activity.this, "Note not deleted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
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