package com.infopad.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.infopad.app.R;
import com.infopad.app.models.Note;
import com.infopad.app.services.NoteRepository;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private NoteRepository noteRepository;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        noteRepository = new NoteRepository();
        
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        
        adapter.setOnNoteClickListener(note -> {
            Intent intent = new Intent(HomeActivity.this, NoteActivity.class);
            intent.putExtra("NOTE_ID", note.getId());
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_CONTENT", note.getContent());
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, NoteActivity.class));
        });

        loadNotes();
    }

    private void loadNotes() {
        try {
            noteRepository.getNotesQuery().addSnapshotListener((value, error) -> {
                if (error != null) {
                    Toast.makeText(this, "Error loading notes", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null) {
                    List<Note> notes = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Note note = doc.toObject(Note.class);
                        if (note != null) {
                            note.setId(doc.getId());
                            notes.add(note);
                        }
                    }
                    adapter.setNotes(notes);
                }
            });
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Please Sign in again", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_templates) {
            startActivity(new Intent(this, TemplateActivity.class));
            return true;
        } else if (id == R.id.action_scan_qr) {
            startActivity(new Intent(this, QRScanActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
