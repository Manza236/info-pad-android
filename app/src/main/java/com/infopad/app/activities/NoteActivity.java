package com.infopad.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.infopad.app.R;
import com.infopad.app.services.NoteRepository;

public class NoteActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private NoteRepository noteRepository;
    private String noteId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnShareQR = findViewById(R.id.btnShareQR);

        noteRepository = new NoteRepository();

        if (getIntent().hasExtra("NOTE_ID")) {
            noteId = getIntent().getStringExtra("NOTE_ID");
            etTitle.setText(getIntent().getStringExtra("NOTE_TITLE"));
            etContent.setText(getIntent().getStringExtra("NOTE_CONTENT"));
        }

        btnSave.setOnClickListener(v -> saveNote());
        
        btnShareQR.setOnClickListener(v -> {
            Intent intent = new Intent(NoteActivity.this, QRShareActivity.class);
            String qrData = etTitle.getText().toString() + "|||" + etContent.getText().toString();
            intent.putExtra("QR_DATA", qrData);
            startActivity(intent);
        });
    }

    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Note is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (noteId == null) {
            noteRepository.addNote(title, content)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show());
        } else {
            noteRepository.updateNote(noteId, title, content)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show());
        }
    }
}
