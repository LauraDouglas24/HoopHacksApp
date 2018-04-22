package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

}
