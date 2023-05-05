package com.dacasa.sdakitidistrict.Activities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.dacasa.sdakitidistrict.AboutUs;
import com.dacasa.sdakitidistrict.Adapters.PageAdapter;
import com.dacasa.sdakitidistrict.Adapters.PostAdapter;
import com.dacasa.sdakitidistrict.Adapters.SliderPagerAdapter;
import com.dacasa.sdakitidistrict.AuthorActivity;
import com.dacasa.sdakitidistrict.Commoners.Bible;
import com.dacasa.sdakitidistrict.Commoners.P;
import com.dacasa.sdakitidistrict.Commoners.PrimaryActivity;
import com.dacasa.sdakitidistrict.Fragments.ChurchCalenderFragment;
import com.dacasa.sdakitidistrict.Fragments.ChurchLevelFragment;
import com.dacasa.sdakitidistrict.Fragments.HomeFragment;
import com.dacasa.sdakitidistrict.Fragments.LessonFragment;
import com.dacasa.sdakitidistrict.Fragments.LoginActivity;
import com.dacasa.sdakitidistrict.Fragments.MissionFragment;
import com.dacasa.sdakitidistrict.Fragments.NotesFragment;
import com.dacasa.sdakitidistrict.Fragments.SlideshowFragment;
import com.dacasa.sdakitidistrict.Fragments.ToolsFragment;
import com.dacasa.sdakitidistrict.Lesson1;
import com.dacasa.sdakitidistrict.MainCalender;
import com.dacasa.sdakitidistrict.Mission;
import com.dacasa.sdakitidistrict.Models.Post;
import com.dacasa.sdakitidistrict.Models.SharedPref;
import com.dacasa.sdakitidistrict.Notes;
import com.dacasa.sdakitidistrict.Nyimbo_splash;
import com.dacasa.sdakitidistrict.POJOS.Verse;
import com.dacasa.sdakitidistrict.R;
import com.dacasa.sdakitidistrict.Setting;
import com.dacasa.sdakitidistrict.SettingsActivity;
import com.dacasa.sdakitidistrict.Slide;
import com.dacasa.sdakitidistrict.StreamingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dacasa.sdakitidistrict.R.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,PostAdapter.FeedInteractor {

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAddPost;
    ImageView popupUserImage,popupPostImage,popupAddBtn;
    // add verse
    StorageReference storageReference;
    Bible bible;
    TextView popupTitle,popupDescription,add_verse,verse,book;
    View verse_view;
    int b = -1,c = -1,f = -1,t = -1;
    ProgressBar popupClickProgress,contentProgress;

    MenuItem menuSetting;
    BottomNavigationView btm_view;

    // night mode
    SharedPref sharedPref;

    //Tablayout
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem firstItem,secondItem,thirdItem,fourthItem;
    PagerAdapter adapter;

    // add scripture
    PrimaryActivity primaryActivity;
    PostAdapter postAdapter;
    PostAdapter.FeedInteractor feedInteractor;

    // slider
    private List<Slide> lstslides;
    private ViewPager sliderpager;
    private TabLayout indicator;


    private FirebaseAuth firebaseAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private Uri pickedImgUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // slider
        indicator = findViewById(R.id.indicator);


        // add verse
        storageReference = FirebaseStorage.getInstance().getReference();
        bible = new Bible(this);

        //night mode
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()) {
            setTheme(style.darktheme);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);


        btm_view = findViewById(R.id.bottom_view);
        btm_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == id.homehome){

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,new ChurchLevelFragment());
                    fragmentTransaction.commit();

                }else if (menuItem.getItemId() == id.homeevents){

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,new MainCalender());
                    fragmentTransaction.commit();


                }else if (menuItem.getItemId() == id.homenyimbo){

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,new NotesFragment());
                    fragmentTransaction.commit();


                } else if (menuItem.getItemId() == id.homeBible){

                    Intent intent = new Intent(getBaseContext(), kjvBibleActivity.class);
                    startActivity(intent);
            }


                return false;
            }
        });


        /*
        // slider
        sliderpager = findViewById(R.id.sliderPager);
        indicator = findViewById(R.id.indicator);

        lstslides = new ArrayList<>();
        lstslides.add(new Slide(drawable.sda_logo,""));
        lstslides.add(new Slide(drawable.ic_undraw_candidate_ubwv,""));
        lstslides.add(new Slide(drawable.sda_logo,""));
        lstslides.add(new Slide(drawable.ic_undraw_candidate_ubwv,""));
        SliderPagerAdapter sliderPagerAdapter = new SliderPagerAdapter(this,lstslides);
        //sliderpager.setAdapter(sliderPagerAdapter);

        //setup timer
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainActivity.SliderTimer(),4000,6000);
        indicator.setupWithViewPager(sliderpager,true);
         */


        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        // ini

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // ini popup
        iniPopup();
        setupPopupImageClick();




        FloatingActionButton fab = (FloatingActionButton) findViewById(id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });


        DrawerLayout drawer = findViewById(id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        // set Church level fragment as the default one

        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChurchLevelFragment()).commit();


        //TabLayout
        pager = findViewById(id.viewpager);
        mTabLayout = findViewById(id.tabLayout);
        firstItem = findViewById(id.firstitem);
        secondItem = findViewById(id.secondItem);
        thirdItem = findViewById(id.thirdItem);
        fourthItem = findViewById(id.fourthItem);

        //content progressbar
        contentProgress = (ProgressBar)findViewById(id.pbcontainer);

        adapter = new PageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mTabLayout.getTabCount());
        pager.setAdapter(adapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                contentProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                contentProgress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout){
            @Override
            public void onPageSelected(int position) {
                contentProgress.setVisibility(View.GONE);
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                contentProgress.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });

    }


    // slider

    /*
    class SliderTimer extends TimerTask {

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem()<lstslides.size()-1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1);
                    }
                    else
                        sliderpager.setCurrentItem(0);
                }
            });

        }
    }

     */



    private void setupPopupImageClick() {

        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here when image clicked we need to open gallery
                //before we open gallery we need to check if our app has the access
                //we did this before register activity i'm just going to copy the code

                checkAndRequestForPermission();

            }
        });


    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(MainActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();


            }
            else
            {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);

            }

        }
        else
            openGallery();
    }


    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image!

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }


    //when user picked an image/chose verse
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            //the user has successfully picked an image
            //we need to save its reference to a Uri variable
            pickedImgUrl = data.getData() ;
            popupPostImage.setImageURI(pickedImgUrl);
        }else if (requestCode == 99){
            if (resultCode == RESULT_OK){
                add_verse.setText("Change Verse");
                addVerse(data.getExtras());
            }
        }

    }

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupUserImage = popAddPost.findViewById(id.popup_user_image);
        popupPostImage = popAddPost.findViewById(id.popup_img);
        popupTitle =popAddPost.findViewById(id.popup_title);
        popupDescription = popAddPost.findViewById(id.popup_description);
        popupAddBtn = popAddPost.findViewById(id.popup_add);
        popupClickProgress = popAddPost.findViewById(id.popup_progressBar);
        // add verse
        add_verse = popAddPost.findViewById(R.id.add_verse);
        book = popAddPost.findViewById(R.id.book);
        verse = popAddPost.findViewById(R.id.verse);
        verse_view = popAddPost.findViewById(id.verse_view);
        storageReference = FirebaseStorage.getInstance().getReference();

        // load Current user profile

        Glide.with(MainActivity.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        //add feed
        add_verse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),BibleView.class);
                intent.putExtra("forResult",true);
                startActivityForResult(intent,99);
            }
        });

        // verse view
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


        // add post click listener

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                //we need to test all input fields (title and description) and post image

                if (!popupTitle.getText().toString().isEmpty()
                    && !popupDescription.getText().toString().isEmpty()
                    &&pickedImgUrl !=null ) {

                    //everything is okay no empty or nuill value
                    //TODO Create Post Object and add it to firebase database

                    // add verse




                    //first we need to upload post image
                    //access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUrl.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   String imageDownloadLink = uri.toString();
                                   // create post Object

                                   if (currentUser.getPhotoUrl() != null) {

                                       Post post = new Post(popupTitle.getText().toString(),
                                               popupDescription.getText().toString(),
                                               imageDownloadLink,
                                               currentUser.getUid(),
                                               currentUser.getPhotoUrl().toString(),currentUser.getDisplayName(),b,c,f,t);

                                       //Add post to firebase

                                       addPost(post);
                                   }
                                   else {

                                      // Post post = new Post(popupTitle.getText().toString(),
                                        //       popupDescription.getText().toString(),
                                          //     imageDownloadLink,
                                            //   currentUser.getUid(),
                                              // null);

                                       showMessage("Access Denied");
                                       popupAddBtn.setVisibility(View.VISIBLE);
                                       popupClickProgress.setVisibility(View.INVISIBLE);



                                       //Add post to firebase

                                       // when login without photo,one cannot post
                                       //addPost(post);

                                   }

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   //something goes wrong uploading picture

                                   showMessage(e.getMessage());
                                   popupClickProgress.setVisibility(View.INVISIBLE);
                                   popupAddBtn.setVisibility(View.VISIBLE);

                               }
                           });

                        }
                    });

                }
                else {
                    showMessage("Please Verify all input fields and choose Post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        //get post unique ID and update post key
        String key = myRef.getKey();
        post.setPostKey(key);

        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();

            }
        });

    }

    private void showMessage(String message) {

        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(id.action_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button so long
        //as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.nav_Logout) {

            FirebaseAuth.getInstance().signOut();

            GoogleSignIn.getClient(this,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                    .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   // startActivity(new Intent(view.getContext(),LoginActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,"LogOut Failed.",Toast.LENGTH_SHORT).show();
                }
            });



            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item){
        //Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_My_District){

            Intent intent = new Intent(getBaseContext(), StreamingActivity.class);
            startActivity(intent);

            //getSupportActionBar().setTitle("SDA KITI District ");
            //getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChurchLevelFragment()).commit();

        } else if (id == R.id.nav_Church_Calender){

            //getSupportActionBar().setTitle("calender");
            //getSupportFragmentManager().beginTransaction().replace(R.id.container,new ChurchCalenderFragment()).commit();

            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_Nyimbo_Za_Kristo){

            Intent intent = new Intent(getBaseContext(), Nyimbo_splash.class);
            startActivity(intent);

        }else if (id == R.id.nav_Mission){

            Intent intent = new Intent(getBaseContext(), Mission.class);
            startActivity(intent);

        }else if (id == R.id.nav_Lesson){

            Intent intent = new Intent(getBaseContext(), Lesson1.class);
            startActivity(intent);

        }else if (id == R.id.nav_Notes){

            Intent intent = new Intent(getBaseContext(), Notes.class);
            startActivity(intent);

        }else if (id == R.id.nav_Bible){

            Intent intent = new Intent(getBaseContext(), kjvBibleActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_Aboutus){

            Intent intent = new Intent(getBaseContext(), AboutUs.class);
            startActivity(intent);

        }else if (id == R.id.nav_Logout){

            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();

        }else if (id == R.id.nav_Setting){

            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);

        }else if (id == R.id.nav_Authors){

            Intent intent = new Intent(getBaseContext(), AuthorActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void updateNavHeader (){

        NavigationView navigationView = (NavigationView) findViewById(id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(id.nav_username);
        TextView navUserMail = headerView.findViewById(id.nav_user_mail);
        ImageView navUserPhot = headerView.findViewById(id.nav_user_photo);


        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
        }else
            Glide.with(this).load(R.mipmap.ic_launcher_h_foreground).into(navUserPhot);

        //nav user photo anim
        navUserPhot.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_scale_animation));
        navUsername.setAnimation(AnimationUtils.loadAnimation(this, anim.fade_transition_animation));
        navUserMail.setAnimation(AnimationUtils.loadAnimation(this, anim.fade_transition_animation));

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
    public void onScriptureClicked(Post feed) {
        openInBible(feed.getBook(),feed.getChapter(),feed.getFrom_verse(),feed.getTo_verse());

    }

    private void openInBible(int book, int chapter, int verse_from, int verse_to) {
        if (!P.bibleAvailable()){
            Toast.makeText(this, "Please download the bible", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,kjvBibleActivity.class);
            startActivity(intent);
            return;
        }
        Toast.makeText(this, "Flipping pages...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,BibleView.class);
        intent.putExtra("book",book);
        intent.putExtra("chapter",chapter);
        intent.putExtra("verse_from",verse_from);
        intent.putExtra("verse_to",verse_to);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }
}
