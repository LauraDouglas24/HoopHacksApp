package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button bCreateUser;
    private Button bReturnLogIn;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bCreateUser = findViewById(R.id.bCreateUser);
        bCreateUser.setOnClickListener(this);

        bReturnLogIn = findViewById(R.id.bReturnLogIn);
        bReturnLogIn.setOnClickListener(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(Register.this, UpdateProfile.class);
                    Register.this.startActivity(myIntent);
                } else {

                }
            }
        };

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bCreateUser:
                createUser(etEmail.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.bReturnLogIn:
                Intent intent = new Intent(Register.this, Login.class);
                Register.this.startActivity(intent);
                break;
        }
    }

    // Email and Password - createUser
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && isValidEmaillId(etEmail.getText().toString().trim()) && isValidPassword(etPassword.getText().toString().trim())) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent myIntent = new Intent(Register.this, UpdateProfile.class);
                            Register.this.startActivity(myIntent);
                        } else {
                            Toast.makeText(Register.this, "Unable to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Password Validation
    private boolean isValidPassword(String password) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,24}$"
        );
        return !TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches();
    }

    //Email Validation
    private boolean isValidEmaillId(String email){
        Pattern EMAIL_PATTERN
                = Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        );
        return !TextUtils.isEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
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
}

