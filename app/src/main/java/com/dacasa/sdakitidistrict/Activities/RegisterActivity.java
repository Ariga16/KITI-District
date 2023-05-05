package com.dacasa.sdakitidistrict.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dacasa.sdakitidistrict.Commoners.LoginListener;
import com.dacasa.sdakitidistrict.POJOS.User;
import com.dacasa.sdakitidistrict.R;
import com.dacasa.sdakitidistrict.SettingsActivity;
import com.dacasa.sdakitidistrict.Splash;
import com.dacasa.sdakitidistrict.login.LoginFormState;
import com.dacasa.sdakitidistrict.login.LoginViewModel;
import com.dacasa.sdakitidistrict.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUrl ;

    private EditText userEmail,userPassword,userPassword2,userName;
    private ProgressBar loadingProgress;
    private Button regBtn;
    private TextView userLogin,gender;
    private RadioButton radioButtonM,radioButtonF;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // view model
        final LoginViewModel loginViewModel;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //view model
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // view model
        final EditText password2EditText = findViewById(R.id.regPassword2);
        final EditText usernameEditText = findViewById(R.id.regName);
        final EditText usermailEditText = findViewById(R.id.regMail);
        final EditText passwordEditText = findViewById(R.id.regPassword);

        // view model
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                regBtn.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() !=null) {
                    userEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getUsernameError() != null) {
                    userEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    userPassword.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getPasswordError() !=null) {
                    userPassword2.setError(getString(loginFormState.getPasswordError()));
                }

            }
        });

        // view model
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(userEmail.getText().toString(),userPassword2.getText().toString(),userName.getText().toString(),
                        userPassword.getText().toString());
            }
        };

        password2EditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        usermailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usermailEditText.getText().toString(),password2EditText.getText().toString(),usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });






        //inu views

        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        userName = findViewById(R.id.regName);
        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.regBtn);
        loadingProgress.setVisibility(View.INVISIBLE);
        userLogin = findViewById(R.id.tvUserLogin);
        gender = findViewById(R.id.tvGender);
        radioButtonM = findViewById(R.id.RBmale);
        radioButtonF = findViewById(R.id.RBfemale);

        //buttons animation
        userName.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        userEmail.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        userPassword.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        userPassword2.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));

        // anim
        regBtn.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        userLogin.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        gender.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        radioButtonF.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));
        radioButtonM.setAnimation(AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.mytransition));




        mAuth = FirebaseAuth.getInstance();


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              regBtn.setVisibility(View.INVISIBLE);
              loadingProgress.setVisibility(View.VISIBLE);
              final String email = userEmail.getText().toString();
              final String password = userPassword.getText().toString();
              final String password2 = userPassword2.getText().toString();
              final String name = userName.getText().toString();

              if (email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(password2)) {

                  //something goes wrong:all fields must be filled
                  //we need to display an error message
                  showMessage("Please Verify all fields");
                  regBtn.setVisibility(View.VISIBLE);
                  loadingProgress.setVisibility(View.INVISIBLE);


              }
              else {
                  //everything is ok and all fields are filled now we can start creating user account
                  //CreateUserAccount method will try to create the user if the email is valid

                  CreateUserAccount(email,name,password);


              }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        ImgUserPhoto = findViewById(R.id.regUserPhoto);

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT>=22) {

                    checkAndRequestForPermission();

                }
                else
                {
                   openGallery();
                }


            }
        });




    }

    private void CreateUserAccount(String email, final String name, String password) {

        //this method creates user account with specific email and password

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //user account created successfully.Account Created
                            showMessage("Signing In.....");
                            //after we create user account we need to update his profile picture and name

                            //now we check if the picked image is null or not
                            if (pickedImgUrl != null){

                                updateUserInfo(name ,pickedImgUrl,mAuth.getCurrentUser());

                            }else
                                updateUserInfoWithoutPhoto(name,mAuth.getCurrentUser());

                        }
                        else
                        {

                            //account creation failed
                            showMessage("account creation failed" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });


    }

    //update user photo and name
    private void updateUserInfo(final String name, Uri pickedImgUrl, final FirebaseUser currentUser) {


        //first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUrl.getLastPathSegment());
        imageFilePath.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //image uploaded successfully
                //now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //uri contain user image uri

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully.Register complete
                                            showMessage("One More Step");
                                            updateUI();

                                        }

                                    }
                                });


                    }
                });

            }
        });


    }

    private void updateUserInfoWithoutPhoto(final String name, final FirebaseUser currentUser) {


        //first we need to upload user photo to firebase storage and get url

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully.Register Complete
                                            showMessage("One More Step");
                                            updateUI();


                                        }

                                    }
                                });

    }





    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(homeActivity);
        finish();
    }

    //simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image!

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);



    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();


            }
            else
            {

               ActivityCompat.requestPermissions(RegisterActivity.this,
                                                  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                  PReqCode);

            }

        }
        else
            openGallery();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){

            //the user has successfully picked an image
            //we need to save its reference to a Uri variable
            pickedImgUrl = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUrl);

        }


    }

    // animate SettingsActivity/login
    public void animate(){

    }



}
