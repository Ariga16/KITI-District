package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dacasa.sdakitidistrict.Fragments.NyimboZaKristoFragment;
import com.dacasa.sdakitidistrict.Models.SharedPref;

public class Nyimbo_splash extends AppCompatActivity {

    SharedPref sharedPref;

    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set dark theme
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyimbo_splash);
        tv = (TextView) findViewById(R.id.tvNyimbo);
        iv = (ImageView) findViewById(R.id.ivLogo);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent i = new Intent(this, NyimboZaKrist.class);
        Thread timer = new Thread() {
            public void run () {
                try {
                 sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();

                }
            }
        };
                 timer.start();

    }
}
