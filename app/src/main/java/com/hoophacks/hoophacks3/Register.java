package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

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
        setTitle("Register");

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

    // Email and Password - createUser
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete (@NonNull Task < Void > task){
                                                if (task.isSuccessful()) {
                                                    Log.i(TAG, "Email sent.");
                                                }
                                            }
                                        });

                            Intent myIntent = new Intent(Register.this, UpdateProfile.class);
                            Register.this.startActivity(myIntent);
                        } else {

                        }
                    }
                });
    }
}

