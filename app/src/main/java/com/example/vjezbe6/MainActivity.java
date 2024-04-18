package com.example.vjezbe6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "note-db").build();
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        loadNotes();
    }

    private void saveNote() {
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            Note note = new Note();
            note.text = text;
            new InsertNoteTask().execute(note);
            editText.setText(""); loadNotes();
        }
    }
    private void loadNotes() {
        new LoadNotesTask().execute();
    }

    private class InsertNoteTask extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            appDatabase.noteDao().insert(notes[0]);
            return null;
        }
    }
    private class LoadNotesTask extends AsyncTask<Void, Void,
            List<Note>> {
        @Override
        protected List<Note> doInBackground(Void... voids) {
            return appDatabase.noteDao().getAll();
        }
        @Override
        protected void onPostExecute(List<Note> notes) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Note note : notes) {
//stringBuilder.append(note.text);
//stringBuilder.append("\n");
                stringBuilder.insert(0, "\n\n");
                stringBuilder.insert(0, note.text);
            }
            textView.setText(stringBuilder.toString());
        }
    }


}