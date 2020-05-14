package com.dacasa.sdakitidistrict.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dacasa.sdakitidistrict.Adapters.CommentAdapter;
import com.dacasa.sdakitidistrict.Models.Comment;
import com.dacasa.sdakitidistrict.Models.SharedPref;
import com.dacasa.sdakitidistrict.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost,imgUserPost,imgCurrentUser,backimg;
    TextView txtPostDesc,txtPostDateName,txtPostTitle;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;

    CommentAdapter commentAdapter;
    List<Comment> listComment;

    ProgressBar progressBar;


    static String COMMRNT_KEY = "Comment" ;


    //night mode
    SharedPref sharedPref;

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
        setContentView(R.layout.activity_post_detail);

        // ini Views
        RvComment = findViewById(R.id.rv_comment);
        imgPost =findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);

        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);
        backimg = findViewById(R.id.backimg);

        // progressbar on comment display
        progressBar = findViewById(R.id.progressbar);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //add comment button click listener
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!editTextComment.getText().toString().isEmpty()) {
                   btnAddComment.setVisibility(View.INVISIBLE);
                   DatabaseReference commentReference = firebaseDatabase.getReference(COMMRNT_KEY).child(PostKey).push();
                   String comment_content = editTextComment.getText().toString();
                   String uid = firebaseUser.getUid();
                   String uname = firebaseUser.getDisplayName();
                   if (firebaseUser.getPhotoUrl() !=null){

                       String uimg = firebaseUser.getPhotoUrl().toString();

                       Comment comment = new Comment(comment_content,uid,uimg,uname);

                       commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               showMessage("comment added");
                               editTextComment.setText("");
                               btnAddComment.setVisibility(View.VISIBLE);
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               showMessage("fail to add comment :" +e.getMessage());
                           }
                       });


                   }else {

                       String uimg = getIntent().getExtras().getString("userImg");
                       //String uimg = Integer.toString(R.drawable.rounduserphoto);
                       Comment comment = new Comment(comment_content,uid,uimg,uname);
                       commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               showMessage("comment added");
                               editTextComment.setText("");
                               btnAddComment.setVisibility(View.VISIBLE);
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               showMessage("fail to add comment :" +e.getMessage());
                           }
                       });


                   }


               }
               else {
                   showMessage("Please Type A comment before Adding");
               }


               }
        });


        //  bind all data into those views
        // get post data
        // send post detail data to this activity first ...

        //get post data

        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String userpostImage = getIntent().getExtras().getString("userPhoto");

        if (userpostImage != null) {

            Glide.with(this).load(userpostImage).into(imgUserPost);
        }
        else
            Glide.with(this).load(R.mipmap.ic_launcher_h_foreground).into(imgUserPost);

        String postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);


        //set comment user image

        if (firebaseUser.getPhotoUrl() != null) {
            Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);
        }
        else
            Glide.with(this).load(R.drawable.rounduserphoto).into(imgCurrentUser);

        // get post id
        PostKey = getIntent().getExtras().getString("postKey");

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);


        // ini RecyclerView comment
        iniRvComment();


    }

    private void iniRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMRNT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listComment = new ArrayList<>();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot snap:dataSnapshot.getChildren()){

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);

                }

                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showMessage(String message) {

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd MMM  hh.mm a",calendar).toString();
        return date;
        
    }


}
