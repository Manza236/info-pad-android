package com.infopad.app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.infopad.app.R;
import android.widget.Button;

public class TemplateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        Button btnMeeting = findViewById(R.id.btnMeetingTemplate);
        Button btnTodo = findViewById(R.id.btnTodoListTemplate);

        btnMeeting.setOnClickListener(v -> createTemplate("Meeting Notes", "Attendees:\n\nAgenda:\n\nAction Items:\n"));
        btnTodo.setOnClickListener(v -> createTemplate("Daily To-Do", "- [ ] Task 1\n- [ ] Task 2\n- [ ] Task 3"));
    }

    private void createTemplate(String title, String content) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("NOTE_TITLE", title);
        intent.putExtra("NOTE_CONTENT", content);
        startActivity(intent);
        finish();
    }
}
