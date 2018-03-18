package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextUtils;

// Firebase Login
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// Google Login
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView tvStatus;
    private EditText etEmail;
    private EditText etPassword;
    private Button bLogIn;
    private TextView tvCreateUser;
    private TextView tvForgotten;

    // Firebase Login
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Google Login
    private SignInButton bGoogleLogIn;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvStatus = findViewById(R.id.tvStatus);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        bLogIn = findViewById(R.id.bLogIn);
        bLogIn.setOnClickListener(this);

        // Google Login
        bGoogleLogIn = (SignInButton) findViewById(R.id.bGoogleLogIn);
        bGoogleLogIn.setOnClickListener(this);

        tvCreateUser = findViewById(R.id.tvCreateUser);
        tvCreateUser.setOnClickListener(this);

        tvForgotten = findViewById(R.id.tvForgotten);
        tvForgotten.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent myIntent = new Intent(Login.this, UserProfile.class);
                    Login.this.startActivity(myIntent);
                } else {
                    tvStatus.setText("Please login.");
                }
            }
        };
    }

    // Shared components - onStart
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Shared components - onStop
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogIn:
                logIn(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.bGoogleLogIn:
                googleLogIn();
                break;
            case R.id.tvCreateUser:
                Intent intent = new Intent(Login.this, Register.class);
                Login.this.startActivity(intent);
                break;
            case R.id.tvForgotten:
                Intent intentForgotten = new Intent(Login.this, Password.class);
                Login.this.startActivity(intentForgotten);
        }
    }

    // Email and Password - logIn
    private void logIn(String email, String password){
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            tvStatus.setText("Email or Password must not be empty.");
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent myIntent = new Intent(Login.this, UserProfile.class);
                                Login.this.startActivity(myIntent);
                            } else {
                                tvStatus.setText("Authentication failed.");
                            }
                        }
                    });
        }
    }

    // Google Login - googleLogIn
    private void googleLogIn(){
        Intent logInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(logInIntent, 9001);
    }

    // Google Login - onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9001){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    // Google Login - handleSignInResult
    private void handleSignInResult(GoogleSignInResult result)
    {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            tvStatus.setText("Hello " + account.getDisplayName());

            Intent myIntent = new Intent(Login.this, UserProfile.class);
            Login.this.startActivity(myIntent);
        }
        else{

        }
    }
}
