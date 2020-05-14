package com.dacasa.sdakitidistrict.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dacasa.sdakitidistrict.R;

public class Email extends AppCompatActivity {

    private EditText ouremail,subject,message;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ouremail = findViewById(R.id.popup_title);
        subject = findViewById(R.id.popup_description);
        message = findViewById(R.id.descriptionmail);
        send = findViewById(R.id.BtnSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ourSubjectFeedback = subject.getText().toString();
                String ourMessageFeedback = message.getText().toString();
                String ourEmail = ouremail.getText().toString();
                String [] email_divide = ourEmail.split(",");
                Intent send = new Intent(Intent.ACTION_SEND);
                send.putExtra(Intent.EXTRA_EMAIL, email_divide);
                send.putExtra(Intent.EXTRA_SUBJECT, ourSubjectFeedback);
                send.putExtra(Intent.EXTRA_TEXT, ourMessageFeedback);
                send.setType("message/rfc822");
                send.setPackage("com.google.android.gm");
                startActivity(send);


            }
        });




    }
}
