package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton bProfile;
    private ImageButton bExercises;
    private ImageButton bCreateWorkout;
    private ImageButton bViewWorkout;
    private ImageButton bUserfeed;
    private ImageButton bViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Menu");

        bProfile = findViewById(R.id.bProfile);
        bProfile.setOnClickListener(this);

        bExercises = findViewById(R.id.bExercises);
        bExercises.setOnClickListener(this);

        bCreateWorkout = findViewById(R.id.bCreateWorkout);
        bCreateWorkout.setOnClickListener(this);

        bViewWorkout = findViewById(R.id.bViewWorkout);
        bViewWorkout.setOnClickListener(this);

        bUserfeed = findViewById(R.id.bUserfeed);
        bUserfeed.setOnClickListener(this);

        bViewResults = findViewById(R.id.bViewResults);
        bViewResults.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bProfile:
                Intent profileIntent = new Intent(MenuActivity.this, UserProfile.class);
                MenuActivity.this.startActivity(profileIntent);
                break;
            case R.id.bExercises:
                Intent exerciseIntent = new Intent(MenuActivity.this, SkillAreas.class);
                MenuActivity.this.startActivity(exerciseIntent);
                break;
            case R.id.bCreateWorkout:
                Intent createWorkoutIntent = new Intent(MenuActivity.this, CreateWorkout.class);
                MenuActivity.this.startActivity(createWorkoutIntent);
                break;
            case R.id.bViewWorkout:
                Intent viewWorkoutIntent = new Intent(MenuActivity.this, WorkoutList.class);
                MenuActivity.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.bUserfeed:
                Intent userfeedIntent = new Intent(MenuActivity.this, UserFeed.class);
                MenuActivity.this.startActivity(userfeedIntent);
                break;
            case R.id.bViewResults:
                Intent resultsIntent = new Intent(MenuActivity.this, ViewResults.class);
                MenuActivity.this.startActivity(resultsIntent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(MenuActivity.this, Login.class);
                MenuActivity.this.startActivity(myIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(MenuActivity.this, Settings.class);
                MenuActivity.this.startActivity(settingIntent);
                break;
        }
        return false;
    }

}
