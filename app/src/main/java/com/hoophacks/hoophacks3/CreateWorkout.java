package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class CreateWorkout extends AppCompatActivity implements View.OnClickListener {

    private String workoutName;
    private EditText etWorkoutName;
    private Button bAddExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etWorkoutName = findViewById(R.id.etWorkoutName);
        bAddExercises = findViewById(R.id.bAddExercises);
        bAddExercises.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bAddExercises:
                workoutName = etWorkoutName.getText().toString();

                Intent intent = new Intent(CreateWorkout.this, WorkoutAddExercises.class);
                intent.putExtra("workoutName", workoutName);
                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(CreateWorkout.this, Login.class);
                CreateWorkout.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(CreateWorkout.this, SkillAreas.class);
                CreateWorkout.this.startActivity(skillIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(CreateWorkout.this, WorkoutList.class);
                CreateWorkout.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(CreateWorkout.this, UserProfile.class);
                CreateWorkout.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(CreateWorkout.this, UserFeed.class);
                CreateWorkout.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(CreateWorkout.this, ViewResults.class);
                CreateWorkout.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(CreateWorkout.this, Settings.class);
                CreateWorkout.this.startActivity(settingIntent);
                break;
        }
        return false;
    }

}
