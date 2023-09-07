package com.jamirodev.agenda_online.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jamirodev.agenda_online.Objects.Note;
import com.jamirodev.agenda_online.R;
import com.jamirodev.agenda_online.ViewHolder.ViewHolder_Note;
//import com.jamirodev.agenda_online.Notes.Detail_Note_Activity;
//import com.jamirodev.agenda_online.Notes.Update_Note_Activity;

public class List_Notes_Activity extends AppCompatActivity {

    RecyclerView rvNotes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference DB_Users;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Note, ViewHolder_Note> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Note> options;

    Dialog dialog, dialog_filter;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Notes");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        dialog_filter = new Dialog(List_Notes_Activity.this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DB_Users = firebaseDatabase.getReference("Users");
        dialog = new Dialog(List_Notes_Activity.this);
        ListNotesUsers();
    }

    private void ListNotesUsers() {
        //CONSULT
        Query query = DB_Users.child(user.getUid()).child("Published notes").orderByChild("date_note");
        options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
                ViewHolder_Note viewHolder_note = new ViewHolder_Note(view);
                viewHolder_note.setOnClickListener(new ViewHolder_Note.ClickListener() {
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
                        Intent intent = new Intent(List_Notes_Activity.this, Detail_Note_Activity.class);
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

//                        GET SELECTED NOTE DATA
                        String id_note = getItem(position).getId_note();
                        String uid_user = getItem(position).getUid_user();
                        String mail_user = getItem(position).getMail_user();
                        String date_register = getItem(position).getDate_actual_hour();
                        String title = getItem(position).getTitle();
                        String description = getItem(position).getDescription();
                        String date_note = getItem(position).getDate_note();
                        String state = getItem(position).getState();


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
//                                Toast.makeText(List_Notes_Activity.this, "Update note", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(List_Notes_Activity.this, Update_Note_Activity.class));
                                Intent intent = new Intent(List_Notes_Activity.this, Update_Note_Activity.class);
                                intent.putExtra("id_note", id_note);
                                intent.putExtra("uid_user", uid_user);
                                intent.putExtra("mail_user", mail_user);
                                intent.putExtra("date_register", date_register);
                                intent.putExtra("title", title);
                                intent.putExtra("description", description);
                                intent.putExtra("date_note", date_note);
                                intent.putExtra("state", state);
                                startActivity(intent);
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
                Query query = DB_Users.child(user.getUid()).child("Published notes").orderByChild("id_note").equalTo(idNote);
//                HERE COULD BE AN ERROR IN ID NOTE
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
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


    private void Empty_Notes_Register() {
        AlertDialog.Builder builder = new AlertDialog.Builder(List_Notes_Activity.this);
        builder.setTitle("Empty Notes");
        builder.setMessage("Do you want to delete all notes?");

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                DELETE ALL NOTES
                Query query = DB_Users.child(user.getUid()).child("Published notes");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(List_Notes_Activity.this, "All notes have been removed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(List_Notes_Activity.this, "Operation canceled by user", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty_all_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Empty_all_notes) {
            Empty_Notes_Register();
        }

        if (item.getItemId() == R.id.Filter_Notes){
            FilterNotes();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void FilterNotes() {
        Button All_Notes, Finished_notes, Notes_NotFinished;

        dialog_filter.setContentView(R.layout.dialog_box_filter_notes);

        All_Notes = dialog_filter.findViewById(R.id.All_Notes);
        Finished_notes = dialog_filter.findViewById(R.id.Finished_notes);
        Notes_NotFinished = dialog_filter.findViewById(R.id.Notes_NotFinished);

        All_Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(List_Notes_Activity.this, "All notes", Toast.LENGTH_SHORT).show();
                dialog_filter.dismiss();
            }
        });
        
        Finished_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(List_Notes_Activity.this, "Finished notes", Toast.LENGTH_SHORT).show();
                dialog_filter.dismiss();
            }
        });
        
        Notes_NotFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(List_Notes_Activity.this, "Notes not finished", Toast.LENGTH_SHORT).show();
                dialog_filter.dismiss();
            }
        });

        dialog_filter.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}