package com.jamirodev.agenda_online.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jamirodev.agenda_online.Objects.Note;
import com.jamirodev.agenda_online.R;
import com.jamirodev.agenda_online.ViewHolder.ViewHolder_Important_Note;

import java.util.Objects;

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

    Dialog dialog;

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

        dialog = new Dialog(Important_Notes_Activity.this);

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
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Note>().setQuery(My_Users.child(user.getUid()).child("Important Notes").orderByChild("date_note"), Note.class).build();
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


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_note = getItem(position).getId_note();
//                        VIEWS
                        Button DeleteNote, DeleteNoteCancel;
//                        DESIGN CONNECTION
                        dialog.setContentView(R.layout.dialog_box_delete_important_note);
//                        INIT VIEWS
                        DeleteNote = dialog.findViewById(R.id.DeleteNote);
                        DeleteNoteCancel = dialog.findViewById(R.id.DeleteNoteCancel);

                        DeleteNote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                Toast.makeText(Important_Notes_Activity.this, "Deleted note", Toast.LENGTH_SHORT).show();
                                Delete_Important_Note(id_note);
                                dialog.dismiss();
                            }
                        });
                        
                        DeleteNoteCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(Important_Notes_Activity.this, "Operation cancelled by user", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                        
                    }

                });
                return viewHolder_important_note;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Important_Notes_Activity.this, LinearLayoutManager.VERTICAL, false);


        RVImportantNotes.setLayoutManager(linearLayoutManager);
        RVImportantNotes.setAdapter(firebaseRecyclerAdapter);


    }

    private void Delete_Important_Note(String id_note) {
        if (user == null) {
            Toast.makeText(Important_Notes_Activity.this, "", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(Objects.requireNonNull(firebaseAuth.getUid())).child("Important Notes").child(id_note)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Important_Notes_Activity.this, "Note its no longer important", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Important_Notes_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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








