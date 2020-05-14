package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class Lesson1 extends AppCompatActivity {

    private static Button button_sbm;
    private static EditText url_text;
    private static WebView browser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson1);
        openUrl();
    }

    public void openUrl(){
        button_sbm = (Button)findViewById(R.id.openurlBtn);
        url_text = (EditText)findViewById(R.id.etmissionlink);
        browser = (WebView)findViewById(R.id.wvMission);

        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = url_text.getText().toString();
                        browser.getSettings().setLoadsImagesAutomatically(true);
                        browser.getSettings().setJavaScriptEnabled(true);
                        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                        browser.loadUrl(url);
                    }
                }
        );




    }







}


