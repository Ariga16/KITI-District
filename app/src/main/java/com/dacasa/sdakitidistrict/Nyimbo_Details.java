package com.dacasa.sdakitidistrict;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dacasa.sdakitidistrict.Activities.BibleView;
import com.dacasa.sdakitidistrict.Activities.RegisterActivity;
import com.dacasa.sdakitidistrict.Models.SharedPref;

public class Nyimbo_Details extends AppCompatActivity {
    TextView songContent;
    // set dark mode
    SharedPref sharedPref;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nyimbo_detail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_bible) {

            Intent intent = new Intent(getApplicationContext(),BibleView.class);
            startActivity(intent);

            return true;

        }

        return false;

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set dark theme
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyimbo__details);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        songContent = findViewById(R.id.tvContentOfSong);
        Intent i = getIntent();
        String title = i.getStringExtra("title of song");
        String content = i.getStringExtra("content of song");
        // animation
        //songContent.setAnimation(AnimationUtils.loadAnimation(Nyimbo_Details.this, R.anim.fade_transition_animation));

        //set the appbar title as song title
        getSupportActionBar().setTitle(title);

        //set content of the song to textview
        songContent.setText(content);
        songContent.setMovementMethod(new ScrollingMovementMethod());
        //enable back arrow to main activity or recyclerview
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


    }
}
