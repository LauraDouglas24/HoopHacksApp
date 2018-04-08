package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ExerciseView extends AppCompatActivity implements View.OnClickListener{

    private Button bBack;
    public static String TAG = "EXERCISE VIEW - ";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("exercises");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String exerciseName = getIntent().getStringExtra("exerciseName");

        Log.i(TAG, exerciseName);
        bBack = findViewById(R.id.bBack);
        bBack.setOnClickListener(this);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(exerciseName);

        final Query query = myRef.orderByChild("exerciseName").equalTo(exerciseName);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBack:
                Intent intentForgotten = new Intent(ExerciseView.this, ExerciseList.class);
                ExerciseView.this.startActivity(intentForgotten);
        }
    }
}
