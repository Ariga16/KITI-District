package com.dacasa.sdakitidistrict.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dacasa.sdakitidistrict.Activities.MainActivity;
import com.dacasa.sdakitidistrict.Activities.RegisterActivity;
import com.dacasa.sdakitidistrict.R;
import com.dacasa.sdakitidistrict.SettingsActivity;
import com.dacasa.sdakitidistrict.Splash;
import com.dacasa.sdakitidistrict.login.LoginFormState;
import com.dacasa.sdakitidistrict.login.LoginViewModel;
import com.dacasa.sdakitidistrict.login.LoginViewModelFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {


    int RC_SIGN_IN=264;
    SignInButton googlesigninbtn;
    String TAG="Google Sign In";
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress,progressBar;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private ImageView loginPhoto;
    private TextView userRegistration,or;


    //haerulmuttaqin
    TextView text;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final LoginViewModel loginViewModel;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //full screen layout
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // view model
        final EditText usernameEditText = findViewById(R.id.login_mail);
        final EditText passwordEditText = findViewById(R.id.login_password);




        // Configure Google Sign In
        googlesigninbtn=findViewById(R.id.signinBtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("591844897062-6u7e31piasmodb6pndn9r3h2tmv932ns.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mAuth=FirebaseAuth.getInstance();

        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);


        //googlesignin btn animation
        googlesigninbtn.setAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_scale_animation));

        // added 1.32AM
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount !=null || mAuth.getCurrentUser() !=null) {
            startActivity(new Intent(getApplicationContext(),Splash.class));
            finish();
        }



        googlesigninbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        // view model
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                btnLogin.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    userMail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    userPassword.setError(getString(loginFormState.getPasswordError()));
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
                loginViewModel.loginDataChanged(userMail.getText().toString(),
                        userPassword.getText().toString(), userPassword.getText().toString(), userPassword.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), passwordEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });











        //haeurulmuttuqin
        //text = findViewById(R.id.text);

        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        or = findViewById(R.id.tvor);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.login_progress);
        progressBar = findViewById(R.id.progress_google);
        userRegistration = findViewById(R.id.tvRegister);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this, com.dacasa.sdakitidistrict.Splash.class);
        loginPhoto = findViewById(R.id.login_photo);
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
              startActivity(registerActivity);
              finish();


            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty() ){
                    showMessage("Please Verify All Field");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);

                }
                else
                {
                    signIn(mail,password);
                }




            }
        });


    }






    private void signIn() {
        googlesigninbtn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        or.setVisibility(View.INVISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"Your Google Account is connected to our application.", Toast.LENGTH_SHORT);
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        finish();

                    }
                });

                //if (account !=null)firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                e.printStackTrace();
                //Google sign in fail
                googlesigninbtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                or.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Failed to login with Google.Please input your Credentials.", Toast.LENGTH_LONG).show();

                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            googlesigninbtn.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updareUI(user);
                        } else {
                            //Google sign in fail
                            Toast.makeText(LoginActivity.this, "SignIn Failed!", Toast.LENGTH_SHORT).show();


                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updareUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updareUI(FirebaseUser user) {

        if (user !=null){

            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());

           // text.append("Info : \n");
           // text.append("name + \n");
           // text.append(email);


        } else {

        }

    }


    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();


                }
                else
                    showMessage(task.getException().getMessage());
                btnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);

            }
        });



    }

    private void updateUI() {

        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //user is already connected so we need to redirect him to splash page
            updateUI();

        }

    }
}
