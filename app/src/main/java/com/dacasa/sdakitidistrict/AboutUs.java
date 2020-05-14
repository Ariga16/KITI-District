package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dacasa.sdakitidistrict.Activities.Email;
import com.dacasa.sdakitidistrict.Activities.RegisterActivity;
import com.dacasa.sdakitidistrict.Fragments.LoginActivity;
import com.dacasa.sdakitidistrict.Models.SharedPref;

public class AboutUs extends AppCompatActivity {
    private CardView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //night mode
        final SharedPref sharedPref;
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.darktheme);
            //restartApp();
            //getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.blue_gradient));
            //actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.BackgroundLight));
        } else setTheme(R.style.AppTheme);
        //restartApp();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = findViewById(R.id.BtnFeedback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutUs.this, Email.class));
            }
        });


    }
}
