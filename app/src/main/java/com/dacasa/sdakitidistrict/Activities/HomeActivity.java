package com.dacasa.sdakitidistrict.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dacasa.sdakitidistrict.CustomCalenderView;
import com.dacasa.sdakitidistrict.Models.SharedPref;
import com.dacasa.sdakitidistrict.R;

public class HomeActivity extends AppCompatActivity {

    //night mode
    SharedPref sharedPref;


    CustomCalenderView customCalenderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //night mode

        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.darktheme);
            //restartApp();
            //getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.blue_gradient));
            //actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.BackgroundLight));
        } else setTheme(R.style.AppTheme);
        //restartApp();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        customCalenderView = findViewById(R.id.custom_calender_view);

    }
}
