package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.hoophacks.hoophacks3.model.Result;
import com.hoophacks.hoophacks3.model.User;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EnterResults extends AppCompatActivity implements View.OnClickListener {

    private EditText etResult;
    private Button bSubmit;
    private static String exerciseName;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String TAG = "ENTER RESULTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(EnterResults.this, Login.class);
                    EnterResults.this.startActivity(myIntent);
                } else {
                    String userId = user.getUid();
                }
            }
        };

        exerciseName = getIntent().getStringExtra("exerciseName");

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(exerciseName);

        etResult = findViewById(R.id.etResult);
        bSubmit = findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBack:
                Intent intent = new Intent(EnterResults.this, ExerciseView.class);;
                startActivity(intent);
            case R.id.bSubmit:
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                String timestamp = Long.toString(System.currentTimeMillis()/1000);

                int result = Integer.parseInt(etResult.getText().toString());
                Result resultInfo = new Result(result, exerciseName);

                DatabaseReference resultRef = database.getReference("results");
                resultRef.child(user.getUid()).child(timestamp).setValue(resultInfo);

                Intent submitIntent = new Intent( EnterResults.this, SkillAreas.class);
                startActivity(submitIntent);
        }
    }
}
