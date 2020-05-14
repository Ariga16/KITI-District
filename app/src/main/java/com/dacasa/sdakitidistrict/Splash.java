package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dacasa.sdakitidistrict.Activities.HomeActivity;
import com.dacasa.sdakitidistrict.Activities.MainActivity;
import com.dacasa.sdakitidistrict.Models.SharedPref;

public class Splash extends AppCompatActivity {
    //night mode
    SharedPref sharedPref;

private TextView tv;

private TextView tvPower;

private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //night mode
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //full screen layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.iv);
        tvPower = (TextView) findViewById(R.id.tvPower);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        tvPower.startAnimation(myanim);
        final Intent i = new Intent(this, MainActivity.class);

        Thread timer =new Thread(){
            public void run () {
                try {
                    sleep(6000);
                } catch (InterruptedException e){
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
