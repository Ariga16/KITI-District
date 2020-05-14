package com.dacasa.sdakitidistrict.Commoners;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dacasa.sdakitidistrict.Activities.BibleView;
import com.dacasa.sdakitidistrict.Activities.kjvBibleActivity;

public class PrimaryActivity extends AppCompatActivity {

    public void openInBible(int b,int c,int f,int t){
        if (!P.bibleAvailable()){
            Toast.makeText(this, "Please download the bible", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, kjvBibleActivity.class);
            startActivity(intent);
            return;
        }
        Toast.makeText(this, "Flipping pages...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BibleView.class);
        intent.putExtra("book",b);
        intent.putExtra("chapter",c);
        intent.putExtra("verse_from",f);
        intent.putExtra("verse_to",t);
        startActivity(intent);
    }


}
