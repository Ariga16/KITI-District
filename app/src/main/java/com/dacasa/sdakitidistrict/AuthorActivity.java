package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dacasa.sdakitidistrict.Adapters.Adapter;
import com.dacasa.sdakitidistrict.Adapters.AuthorAdapter;
import com.dacasa.sdakitidistrict.Models.SharedPref;

public class AuthorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AuthorAdapter authorAdapter;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.darktheme);
            //restartApp();
            //getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.blue_gradient));
            //actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.BackgroundLight));
        } else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String [] titles = getResources().getStringArray(R.array.stories_titles);
        final String [] contents = getResources().getStringArray(R.array.story_content);

        recyclerView = findViewById(R.id.StoriesListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        authorAdapter = new AuthorAdapter(this,titles,contents);
        recyclerView.setAdapter(authorAdapter);


    }
}
