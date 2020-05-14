package com.dacasa.sdakitidistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dacasa.sdakitidistrict.Activities.BibleView;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.Commoners.P;
import com.dacasa.sdakitidistrict.POJOS.Feed;
import com.dacasa.sdakitidistrict.POJOS.User;
import com.dacasa.sdakitidistrict.POJOS.Verse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class AddFeed extends AppCompatActivity {

    EditText title,message;
    TextView add_verse,add_image;
    View image_view,verse_view;
    ImageView image;
    TextView verse,book;
    DatabaseReference database;
    FirebaseUser user;
    StorageReference storage;
    Bible bible;
    Bitmap bitmap;
    Button submit;
    AppCompatRadioButton toParish;
    ProgressDialog progressDialog;
    private String key = null;
    private boolean verse_of_the_day;
    int b = -1,c = -1,f = -1,t = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);
        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        bible = new Bible(this);
        Intent i = getIntent();
        verse_of_the_day = i.getBooleanExtra("verse_of_the_day",false);
        initUI();
    }

    public void initUI(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title = (EditText)findViewById(R.id.title);
        message = (EditText)findViewById(R.id.message);

        add_verse = (TextView)findViewById(R.id.add_verse);
        add_image = (TextView)findViewById(R.id.add_image);
        book = (TextView)findViewById(R.id.book);
        verse = (TextView)findViewById(R.id.verse);

        toParish = (AppCompatRadioButton)findViewById(R.id.toParish);

        image_view = findViewById(R.id.image_view);
        verse_view = findViewById(R.id.verse_view);
        image = (ImageView)findViewById(R.id.image);

        submit = (Button)findViewById(R.id.submit);

        add_verse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BibleView.class);
                intent.putExtra("forResult",true);
                startActivityForResult(intent,99);
            }
        });
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Choose image"), 50);
            }
        });
        verse_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new AlertDialog.Builder(getBaseContext()).setItems(new String[]{"Remove Scripture"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        verse_view.setVisibility(View.GONE);
                        add_verse.setText("Add Scripture");
                        c = -1;
                        b = -1;
                        t = -1;
                        f = -1;
                    }
                }).create();
                d.show();
            }
        });
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = null;
                image_view.setVisibility(View.GONE);
                add_image.setText("Add Image");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        if (verse_of_the_day){
            message.setVisibility(View.GONE);
            title.setText("Verse of the day");
            add_image.setVisibility(View.GONE);
            title.setEnabled(false);
        }
    }


    public void submit(){
        if (!validated())return;
        showProgress("Uploading...");
        database.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (key == null)
                        key = database.child("feed").child(user.getParish()).push().getKey();
                    database.child("feed").child(user.getParish()).child(key).setValue(getFeed()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (bitmap != null) {
                                uploadImage();
                            } else {
                                hideProgress();
                                finish();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadImage(){
        storage.child("images").child(key).putBytes(P.bytes(bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storage.child("thumbs").child(key).putBytes(P.bytes(bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        hideProgress();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        hideProgress();
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddFeed.this, "Failed to upload image, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public Feed getFeed(){
        return new Feed(
                key,
                message.getText().toString().trim(),
                title.getText().toString().trim(),
                user.getDisplayName(),
                b,c,f,t,
                System.currentTimeMillis(),
                getType(),
                null,
                toParish.isChecked()
        );
    }


    public int getType(){
        int type = P.MESSAGE;
        if (verse_view.getVisibility() == View.VISIBLE && image_view.getVisibility() == View.VISIBLE){
            return P.VERSE_IMAGE_MESSAGE;
        }
        if (verse_view.getVisibility() == View.VISIBLE && message.getVisibility() == View.GONE){
            return P.VERSE_ONLY;
        }
        if (verse_view.getVisibility() == View.VISIBLE){
            return P.VERSE_MESSAGE;
        }
        if (bitmap != null){
            return P.MESSAGE_IMAGE;
        }
        return type;
    }

    private boolean validated(){
        boolean ok = true;
        if (TextUtils.isEmpty(title.getText().toString())){
            title.setError("Required");
            ok = false;
        }
        if (TextUtils.isEmpty(message.getText().toString()) && !verse_of_the_day){
            message.setError("Required");
            ok = false;
        }
        return ok;
    }

    public void addVerse(Bundle i){
        b = i.getInt("b",1); int ch = i.getInt("c",1),from = i.getInt("from",1), to = i.getInt("to",1);
        c = ch; f = from; t = to;
        verse_view.setVisibility(View.VISIBLE);
        book.setText(bible.bookName(b) + ": ch: " + ch + " v:" + from + "-" + to);
        StringBuilder s = new StringBuilder();
        List<Verse> vs = bible.getVerses(b,ch,from,to);
        for (Verse ve:vs){
            s.append(ve.getVerse()+"\n");
        }
        verse.setText(s.toString());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99){
            if (resultCode == RESULT_OK){
                add_verse.setText("Change Verse");
                addVerse(data.getExtras());
            }
        }else if (requestCode == 50){
            Uri selected = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selected, filePath, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            String loaded_photo_path = cursor.getString(columnIndex);
            cursor.close();
            bitmap = P.decodedBitmap(loaded_photo_path, 300, 300,70);
            if (bitmap != null) {
                add_image.setText("Change Image");
                image.setImageBitmap(bitmap);
                image_view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showProgress(String message){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing())progressDialog.show();
    }



    public void hideProgress(){
        if (progressDialog != null && progressDialog.isShowing())progressDialog.dismiss();
    }

}



