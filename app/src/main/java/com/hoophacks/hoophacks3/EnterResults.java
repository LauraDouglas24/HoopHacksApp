package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        setTitle(" Enter Results");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(EnterResults.this, Login.class);
                EnterResults.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(EnterResults.this, SkillAreas.class);
                EnterResults.this.startActivity(skillIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(EnterResults.this, UserProfile.class);
                EnterResults.this.startActivity(profileIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(EnterResults.this, ViewResults.class);
                EnterResults.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(EnterResults.this, Settings.class);
                EnterResults.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
