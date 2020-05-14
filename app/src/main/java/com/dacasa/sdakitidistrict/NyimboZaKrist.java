package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.dacasa.sdakitidistrict.Adapters.Adapter;
import com.dacasa.sdakitidistrict.Models.SharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NyimboZaKrist extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;

    boolean isDark=false;
    ConstraintLayout rootlayout;
    EditText searchInput;

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyimbo_za_krist);

        //get the list of songs titles and contents in string array

        final String[] titles = getResources().getStringArray(R.array.songs_title);
        final String[] contents = getResources().getStringArray(R.array.songs_content);




        rootlayout =findViewById(R.id.root_layout);

        searchInput = findViewById(R.id.search_input);



        recyclerView = findViewById(R.id.songListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,titles,contents);
        recyclerView.setAdapter(adapter);


        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence , int i, int i1, int i2) {

                adapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });







    }
}
