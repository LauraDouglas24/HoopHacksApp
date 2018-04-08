package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.Exercise;

public class ExerciseView extends AppCompatActivity implements View.OnClickListener{

    private ImageView ivExercise;
    private TextView tvExerciseTip;
    private Button bBack;
    private Button bStart;
    public static String TAG = "EXERCISE VIEW - ";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("exercises");

    private static String skillArea;
    private static String exerciseName;
    private static String exerciseTip;
    private static String exerciseSkillLevel;
    private static String exerciseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        skillArea = getIntent().getStringExtra("skillArea");
        exerciseName = getIntent().getStringExtra("exerciseName");

        bBack = findViewById(R.id.bBack);
        bBack.setOnClickListener(this);

        bStart = findViewById(R.id.bStart);
        bStart.setOnClickListener(this);

        final Query query = myRef.orderByKey().equalTo(exerciseName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                exerciseTip = dataSnapshot.child(exerciseName).getValue(Exercise.class).getTip();
                exerciseSkillLevel = dataSnapshot.child(exerciseName).getValue(Exercise.class).getSkillLevel();
                exerciseUri = dataSnapshot.child(exerciseName).getValue(Exercise.class).getImage();

                Log.i(TAG, exerciseName);
                Log.i(TAG, exerciseTip);
                Log.i(TAG, exerciseSkillLevel);
                Log.i(TAG, exerciseUri);

                ImageView ivExercise = findViewById(R.id.ivExercise);
                Glide.with(getBaseContext()).load(Uri.parse(exerciseUri)).into(ivExercise);

                TextView tvTitle = findViewById(R.id.tvTitle);
                tvTitle.setText(exerciseName);

                TextView tvExerciseTip = findViewById(R.id.tvExerciseTip);
                tvExerciseTip.setText(exerciseTip);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExerciseView.this, ExerciseList.class);
        intent.putExtra("skillArea", skillArea);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBack:
                Intent intent = new Intent(ExerciseView.this, ExerciseList.class);
                intent.putExtra("skillArea", skillArea);
                startActivity(intent);
            case R.id.bStart:
                Intent startIntent = new Intent(ExerciseView.this, EnterResults.class);
                startIntent.putExtra("exerciseName", exerciseName);
                startActivity(startIntent);
        }
    }
}
