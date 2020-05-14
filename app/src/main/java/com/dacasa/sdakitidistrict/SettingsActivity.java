package com.dacasa.sdakitidistrict;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.viewpager.widget.ViewPager;

import com.dacasa.sdakitidistrict.Activities.LoginDepartment;
import com.dacasa.sdakitidistrict.Activities.Login_church;
import com.dacasa.sdakitidistrict.Activities.RegisterActivity;
import com.dacasa.sdakitidistrict.Adapters.PagerAdapter;
import com.dacasa.sdakitidistrict.Commoners.LoginListener;
import com.dacasa.sdakitidistrict.Commoners.NonSwipeableViewPager;
import com.dacasa.sdakitidistrict.Fragments.ToolsFragment;
import com.dacasa.sdakitidistrict.POJOS.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity implements PagerAdapter.PageListener, LoginListener {

    PagerAdapter pagerAdapter;

    // login department
    List<String> groups = new ArrayList<>();
    LoginDepartment loginDepartment;
    NonSwipeableViewPager viewPager;
    User user;
    // login church
    DatabaseReference database;
    FirebaseAuth mAuth;
    FirebaseUser baseUser;
    Login_church loginChurch;
    SharedPreferences settings;
    RegisterActivity registerActivity;
    ToolsFragment toolsFragment;

    FirebaseAuth auth;
    private Intent HomeActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        HomeActivity = new Intent(this, com.dacasa.sdakitidistrict.Splash.class);

        initUI();


    }

    public void initUI(){
        viewPager = (NonSwipeableViewPager)findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),this);
        toolsFragment = new ToolsFragment();
        loginChurch = new Login_church();
        loginDepartment = new LoginDepartment();

        // added comments taken to login church thus this is pos Zero
        pagerAdapter.addFrag(toolsFragment,"tools");
        pagerAdapter.addFrag(loginChurch, "CHOOSE CHURCH");
        //when commented,one is directed to login
        // without comments one is directed to departments
        pagerAdapter.addFrag(loginDepartment, "CHOOSE GROUPS");
        //pagerAdapter.addFrag(toolsFragment,"tools");
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("pos",1));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) loginChurch.animate();
                if (position == 3) loginChurch.animate();
                if (position == 4) loginDepartment.animate();
                Log.e("THE USER",user != null?user.toString():"user is null");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    //direct to church departments
    private void directDepartments() {
        Intent homeActivity = new Intent(getApplicationContext(), LoginDepartment.class);
        startActivity(homeActivity);
        finish();

    }

    // to splash activity
    private void updatetoSplash() {
        startActivity(HomeActivity);
        finish();
    }


    // direct to splash activity
    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), Splash.class);
        startActivity(homeActivity);
        finish();
    }







    // add user login church to database

    public void addUserToDatabase(){
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    settings.edit().putString("parish", user.getParish()).commit();
                    settings.edit().putString("church", user.getChurch()).commit();
                    settings.edit().putString("district", user.getDistrict()).commit();
                    finish();
                } else {
                    if (user.getParish() == null || user.getChurch() == null || user.getDistrict() == null) {
                        viewPager.setCurrentItem(3);
                        loginChurch.updateObject(user);
                    }else if (groups.isEmpty()) {

                        viewPager.setCurrentItem(4);
                        return;
                    }else {
                        uploadUserGroups();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // login church upload user groups

    public void uploadUserGroups(){
        Map<String,Object> data = new HashMap<>();
        data.put("users/" , user);
        for (String s:groups){
            data.put("groups/"+"/"+(s+"_"+user.getChurch()),(s+"_"+user.getChurch()));
        }
        database.updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                settings.edit().putString("parish", user.getParish()).commit();
                settings.edit().putString("church", user.getChurch()).commit();
                settings.edit().putString("district", user.getDistrict()).commit();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



    @Override
    public void pageCount(int size) {
    }

   // @Override
    public void signUpEmail(final User user,String password){
        this.user = user;
    }




    //@Override
    public void signInEmail(String email, String password) {

    }

    //@Override
    public boolean pageLoginSignUp() {
        return false;
    }

    //@Override
    public boolean pageZeroProceed(boolean validated) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() +1);
        return false;
    }

    //@Override
    public boolean pageZeroGoogle() {
        return false;
    }

   // @Override
    public boolean pageZeroSignIn() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() -1);
        return false;
    }

    public boolean pageThreeProceed(boolean validated, List<String> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        //addUserToDatabase();
        //updateUI();
        //finish();
        updatetoSplash();
        return validated;

    }

    @Override
    public boolean pageTwoProceed(User user) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        this.user = user;
        //direct departments
        //directDepartments();
        loginDepartment.updateObject(user, groups);

//        }
        return true;

    }



}