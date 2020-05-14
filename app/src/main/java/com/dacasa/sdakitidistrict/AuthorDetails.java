package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.dacasa.sdakitidistrict.Models.SharedPref;

public class AuthorDetails extends AppCompatActivity {
    TextView storyContent;
    // set dark mode
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set dark theme
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_author_details);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storyContent = findViewById(R.id.tvContentOfStory);
        Intent i = getIntent();
        String title = i.getStringExtra("title of story");
        String content = i.getStringExtra("content of story");
        // animation
        //songContent.setAnimation(AnimationUtils.loadAnimation(Nyimbo_Details.this, R.anim.fade_transition_animation));

        //set the appbar title as song title
        getSupportActionBar().setTitle(title);

        //set content of the song to textview
        storyContent.setText(content);
        storyContent.setMovementMethod(new ScrollingMovementMethod());


    }
}
