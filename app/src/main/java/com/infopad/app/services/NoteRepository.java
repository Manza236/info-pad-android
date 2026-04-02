package com.infopad.app.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.infopad.app.models.Note;

import java.util.HashMap;
import java.util.Map;

public class NoteRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private CollectionReference getNotesCollection() {
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            return db.collection("users").document(uid).collection("notes");
        }
        throw new IllegalStateException("User not logged in");
    }

    public Task<Void> addNote(String title, String content) {
        Note note = new Note(title, content);
        return getNotesCollection().document().set(note);
    }

    public Query getNotesQuery() {
        return getNotesCollection().orderBy("createdAt", Query.Direction.DESCENDING);
    }

    public Task<Void> updateNote(String id, String title, String content) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("content", content);
        updates.put("updatedAt", FieldValue.serverTimestamp());
        return getNotesCollection().document(id).update(updates);
    }

    public Task<Void> deleteNote(String id) {
        return getNotesCollection().document(id).delete();
    }
}
