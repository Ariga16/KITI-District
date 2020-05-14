package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.dacasa.sdakitidistrict.Models.SharedPref;

public class Setting extends AppCompatActivity {
    private Switch myswitch;

    //set Dark Theme
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

        setContentView(R.layout.activity_setting);

        // settings
        getSupportFragmentManager()
                .beginTransaction()
                // put the comments
                .replace(R.id.settings, new SettingsActivity2.SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        myswitch=(Switch)findViewById(R.id.myswitch);
        if (sharedPref.loadNightModeState()==true){
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sharedPref.setNightModeState(true);
                    restartApp();
                }
                else {
                   sharedPref.setNightModeState(false);
                    restartApp();
                }
            }
        });
    }

    public void restartApp () {
        Intent i = new Intent(getApplicationContext(),Setting.class);
        startActivity(i);
        finish();


    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


}
