package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
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
    private static int exerciseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Exercise View");

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

                //Checking if the exercise has a time value
                try {
                    exerciseTime = dataSnapshot.child(exerciseName).getValue(Exercise.class).getTime();
                } catch (Exception e) {
                    exerciseTime = 0;
                    return;
                }

                ImageView ivExercise = findViewById(R.id.ivExercise);
                //Loading image from Uri value
                Glide.with(getBaseContext()).load(Uri.parse(exerciseUri)).into(ivExercise);

                TextView tvTitle = findViewById(R.id.tvTitle);
                tvTitle.setText(exerciseName);

                TextView tvExerciseTip = findViewById(R.id.tvExerciseTip);
                tvExerciseTip.setText(exerciseTip);
                tvExerciseTip.setMovementMethod(new ScrollingMovementMethod());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBack:
                Intent intent = new Intent(ExerciseView.this, ExerciseList.class);
                intent.putExtra("skillArea", skillArea);
                startActivity(intent);
                break;
            case R.id.bStart:
                if(exerciseTime != 0) {
                    Intent startIntent = new Intent(ExerciseView.this, CountdownTimer.class);
                    startIntent.putExtra("exerciseName", exerciseName);
                    startIntent.putExtra("exerciseTime", exerciseTime);
                    startActivity(startIntent);
                } else {
                    Intent startIntent = new Intent(ExerciseView.this, EnterResults.class);
                    startIntent.putExtra("exerciseName", exerciseName);
                    startActivity(startIntent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExerciseView.this, ExerciseList.class);
        intent.putExtra("skillArea", skillArea);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(ExerciseView.this, Login.class);
                ExerciseView.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(ExerciseView.this, SkillAreas.class);
                ExerciseView.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(ExerciseView.this, CreateWorkout.class);
                ExerciseView.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(ExerciseView.this, WorkoutList.class);
                ExerciseView.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(ExerciseView.this, UserProfile.class);
                ExerciseView.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(ExerciseView.this, UserFeed.class);
                ExerciseView.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(ExerciseView.this, ViewResults.class);
                ExerciseView.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(ExerciseView.this, Settings.class);
                ExerciseView.this.startActivity(settingIntent);
                break;
            case R.id.action_menu:
                Intent menuIntent = new Intent(ExerciseView.this, MenuActivity.class);
                ExerciseView.this.startActivity(menuIntent);
                break;
        }
        return false;
    }
}
