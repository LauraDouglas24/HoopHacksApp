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
import android.widget.ImageButton;

public class SkillAreas extends AppCompatActivity implements View.OnClickListener {

    private ImageButton bShooting;
    private ImageButton bDribbling;
    private ImageButton bPassing;
    private ImageButton bDefense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_areas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bShooting = findViewById(R.id.bShooting);
        bShooting.setOnClickListener(this);

        bDribbling = findViewById(R.id.bDribbling);
        bDribbling.setOnClickListener(this);

        bPassing = findViewById(R.id.bPassing);
        bPassing.setOnClickListener(this);

        bDefense = findViewById(R.id.bDefense);
        bDefense.setOnClickListener(this);
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
                Intent myIntent = new Intent(SkillAreas.this, Login.class);
                SkillAreas.this.startActivity(myIntent);
                break;
            case R.id.action_user_profile:
                Intent skillIntent = new Intent(SkillAreas.this, UserProfile.class);
                SkillAreas.this.startActivity(skillIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(SkillAreas.this, ViewResults.class);
                SkillAreas.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(SkillAreas.this, Settings.class);
                SkillAreas.this.startActivity(settingIntent);
                break;
        }
        return false;
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
                dribblingIntent.putExtra("skillArea", "Ball Handling");
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
        }
    }
}
