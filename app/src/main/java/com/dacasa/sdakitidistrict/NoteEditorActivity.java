package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.dacasa.sdakitidistrict.Models.SharedPref;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set dark theme
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1){

            editText.setText(Notes.notes.get(noteId));

        } else {

            Notes.notes.add("");
            noteId = Notes.notes.size() -1;
            Notes.arrayAdapter.notifyDataSetChanged();


        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Notes.notes.set(noteId, String.valueOf(charSequence));
                Notes.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.dacasa.sdakitidistrict", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(Notes.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
