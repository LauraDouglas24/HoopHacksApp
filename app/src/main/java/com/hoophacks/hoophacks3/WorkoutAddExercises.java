package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.Exercise;
import com.hoophacks.hoophacks3.model.Workout;

import java.util.ArrayList;

public class WorkoutAddExercises extends AppCompatActivity implements View.OnClickListener {

    private String workoutName;
    private ArrayList<String> exercisesArrayList = new ArrayList<String>();

    private Button bCreateWorkout;
    private RecyclerView rvExercises;

    private FirebaseRecyclerAdapter<Exercise, WorkoutAddExercises.ExerciseViewHolder> exerciseAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("exercises");

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static String TAG = "WORKOUT ADD EXERCISES - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_add_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        workoutName = getIntent().getStringExtra("workoutName");

        bCreateWorkout = findViewById(R.id.bCreateWorkout);
        bCreateWorkout.setOnClickListener(this);

        rvExercises = findViewById(R.id.rvExercises);
        rvExercises.setHasFixedSize(true);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseRecyclerAdapter<Exercise, WorkoutAddExercises.ExerciseViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Exercise, WorkoutAddExercises.ExerciseViewHolder>(
                        Exercise.class,
                        R.layout.item_workout_add_exercises,
                        WorkoutAddExercises.ExerciseViewHolder.class,
                        myRef)
                {
                    @Override
                    protected void populateViewHolder(final WorkoutAddExercises.ExerciseViewHolder viewHolder, Exercise model, final int position) {

                        final String exerciseName = getRef(position).getKey();

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                viewHolder.setName(exerciseName);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.mView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {

                                DatabaseReference myRef = database.getReference().child("exercises").child(exerciseName);
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        final String checkBoxExerciseName = dataSnapshot.getKey();

                                        viewHolder.setCheckBox();
                                        Boolean checked = viewHolder.getCheckBox();

                                        if(checked){
                                            exercisesArrayList.add(checkBoxExerciseName);
                                        } else {
                                            int index = exercisesArrayList.indexOf(checkBoxExerciseName);
                                            exercisesArrayList.remove(index);
                                        }

                                        Log.i(TAG, exercisesArrayList.toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                };
        rvExercises.setAdapter(firebaseRecyclerAdapter);
    }

    //ViewHolder for RecyclerView
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ExerciseViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView tvExercise = (TextView) mView.findViewById(R.id.tvExercise);
            tvExercise.setText(name);
        }

        public void setCheckBox(){
            CheckBox cbExercise = mView.findViewById(R.id.cbExercise);
            if(cbExercise.isChecked()) {
                cbExercise.setChecked(false);
            } else {
                cbExercise.setChecked(true);
            }
        }

        public Boolean getCheckBox(){
            CheckBox cbExercise = mView.findViewById(R.id.cbExercise);
            if(cbExercise.isChecked()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bCreateWorkout:

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                ArrayList<String> exercises = exercisesArrayList;

                Workout workoutInfo = new Workout(exercises);

                DatabaseReference resultRef = database.getReference("workouts");
                resultRef.child(user.getUid()).child(workoutName).setValue(workoutInfo);

                Intent intent = new Intent(WorkoutAddExercises.this, WorkoutList.class);
                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_add_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(WorkoutAddExercises.this, Login.class);
                WorkoutAddExercises.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(WorkoutAddExercises.this, SkillAreas.class);
                WorkoutAddExercises.this.startActivity(skillIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(WorkoutAddExercises.this, WorkoutList.class);
                WorkoutAddExercises.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(WorkoutAddExercises.this, UserProfile.class);
                WorkoutAddExercises.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(WorkoutAddExercises.this, UserFeed.class);
                WorkoutAddExercises.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(WorkoutAddExercises.this, ViewResults.class);
                WorkoutAddExercises.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(WorkoutAddExercises.this, Settings.class);
                WorkoutAddExercises.this.startActivity(settingIntent);
                break;
            case R.id.action_menu:
                Intent menuIntent = new Intent(WorkoutAddExercises.this, MenuActivity.class);
                WorkoutAddExercises.this.startActivity(menuIntent);
                break;
        }
        return false;
    }
}
