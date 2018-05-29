package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class SkillAreas extends AppCompatActivity implements View.OnClickListener {

    private ImageButton bShooting;
    private ImageButton bDribbling;
    private ImageButton bPassing;
    private ImageButton bDefense;
    private ImageButton bBallHandling;
    private ImageButton bFreeThrows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_areas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Skill Areas");

        bShooting = findViewById(R.id.bShooting);
        bShooting.setOnClickListener(this);

        bDribbling = findViewById(R.id.bDribbling);
        bDribbling.setOnClickListener(this);

        bPassing = findViewById(R.id.bPassing);
        bPassing.setOnClickListener(this);

        bDefense = findViewById(R.id.bDefense);
        bDefense.setOnClickListener(this);

        bBallHandling = findViewById(R.id.bBallHandling);
        bBallHandling.setOnClickListener(this);

        bFreeThrows = findViewById(R.id.bFreeThrows);
        bFreeThrows.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bShooting:
                Intent shootingIntent = new Intent(SkillAreas.this, ExerciseList.class);
                shootingIntent.putExtra("skillArea", "Shooting");
                SkillAreas.this.startActivity(shootingIntent);
                break;
            case R.id.bDribbling:
                Intent dribblingIntent = new Intent(SkillAreas.this, ExerciseList.class);
                dribblingIntent.putExtra("skillArea", "Dribbling");
                SkillAreas.this.startActivity(dribblingIntent);
                break;
            case R.id.bPassing:
                Intent passingIntent = new Intent(SkillAreas.this, ExerciseList.class);
                passingIntent.putExtra("skillArea", "Passing");
                SkillAreas.this.startActivity(passingIntent);
                break;
            case R.id.bDefense:
                Intent defenseIntent = new Intent(SkillAreas.this, ExerciseList.class);
                defenseIntent.putExtra("skillArea", "Defense");
                SkillAreas.this.startActivity(defenseIntent);
                break;
            case R.id.bBallHandling:
                Intent handlingIntent = new Intent(SkillAreas.this, ExerciseList.class);
                handlingIntent.putExtra("skillArea", "Ball Handling");
                SkillAreas.this.startActivity(handlingIntent);
                break;
            case R.id.bFreeThrows:
                Intent freeThrowsIntent = new Intent(SkillAreas.this, ExerciseList.class);
                freeThrowsIntent.putExtra("skillArea", "Free Throw");
                SkillAreas.this.startActivity(freeThrowsIntent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skill_areas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(SkillAreas.this, Login.class);
                SkillAreas.this.startActivity(myIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(SkillAreas.this, CreateWorkout.class);
                SkillAreas.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(SkillAreas.this, WorkoutList.class);
                SkillAreas.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(SkillAreas.this, UserProfile.class);
                SkillAreas.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(SkillAreas.this, UserFeed.class);
                SkillAreas.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(SkillAreas.this, ViewResults.class);
                SkillAreas.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(SkillAreas.this, Settings.class);
                SkillAreas.this.startActivity(settingIntent);
                break;
            case R.id.action_menu:
                Intent menuIntent = new Intent(SkillAreas.this, MenuActivity.class);
                SkillAreas.this.startActivity(menuIntent);
                break;
        }
        return false;
    }
}
