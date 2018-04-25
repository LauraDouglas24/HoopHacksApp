package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class WorkoutView extends AppCompatActivity {

    private RecyclerView rvExercises;

    private ArrayList<String> workoutExercises;
    private String workoutName;
    private String exerciseVideo;
    private String skillArea;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseRecyclerAdapter<Workout, WorkoutList.WorkoutViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        workoutName = getIntent().getStringExtra("workoutName");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(WorkoutView.this, Login.class);
                    WorkoutView.this.startActivity(myIntent);
                } else {
                }
            }
        };

        final DatabaseReference workoutRef = database.getReference().child("workouts").child(user.getUid()).child(workoutName);
        workoutRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                workoutExercises = dataSnapshot.getValue(Workout.class).getExercises();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        rvExercises = findViewById(R.id.rvExercises);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvExercises.setLayoutManager(layoutManager);

        populateRecycler();

        rvExercises.setAdapter(firebaseRecyclerAdapter);
    }

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

        public void setSkillLevel(String skillLevel){
            TextView tvSkillLevel = (TextView) mView.findViewById(R.id.tvSkillLevel);
            tvSkillLevel.setText(skillLevel);
        }

        public void setImage(Uri uri){
            ImageView ivExercise = mView.findViewById(R.id.ivExercise);
            Glide.with(mView.getContext()).load(uri).into(ivExercise);
        }
    }

    private void populateRecycler() {
        final DatabaseReference exercisesRef = database.getReference().child("exercises");
        final FirebaseRecyclerAdapter<Exercise, WorkoutView.ExerciseViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Exercise, WorkoutView.ExerciseViewHolder>(
                        Exercise.class,
                        R.layout.item_layout,
                        WorkoutView.ExerciseViewHolder.class,
                        exercisesRef) {
                    @Override
                    protected void populateViewHolder(final WorkoutView.ExerciseViewHolder viewHolder, Exercise model, final int position) {

                        final String exerciseName = getRef(position).getKey();

                        exercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (String exercise : workoutExercises) {
                                    if (exerciseName.equals(exercise)) {
                                        String exerciseTip = dataSnapshot.child(exerciseName).getValue(Exercise.class).getTip();
                                        String exerciseSkillLevel = dataSnapshot.child(exerciseName).getValue(Exercise.class).getSkillLevel();
                                        String exerciseUri = dataSnapshot.child(exerciseName).getValue(Exercise.class).getImage();

                                        viewHolder.setName(exerciseName);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {

                                DatabaseReference myRef = database.getReference().child("exercises").child(exerciseName);
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        skillArea = dataSnapshot.getValue(Exercise.class).getSkillArea();

                                        try {
                                            exerciseVideo = dataSnapshot.getValue(Exercise.class).getVideo();
                                        } catch (Exception e) {
                                            exerciseVideo = null;
                                            return;
                                        }

                                        if (exerciseVideo != null) {
                                            Intent intent = new Intent(WorkoutView.this, ExerciseViewVideo.class);
                                            intent.putExtra("exerciseName", exerciseName);
                                            intent.putExtra("skillArea", skillArea);
                                            intent.putExtra("exerciseVideo", exerciseVideo);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(WorkoutView.this, ExerciseView.class);
                                            intent.putExtra("exerciseName", exerciseName);
                                            intent.putExtra("skillArea", skillArea);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_workout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(WorkoutView.this, Login.class);
                WorkoutView.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(WorkoutView.this, SkillAreas.class);
                WorkoutView.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(WorkoutView.this, CreateWorkout.class);
                WorkoutView.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(WorkoutView.this, UserProfile.class);
                WorkoutView.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(WorkoutView.this, UserFeed.class);
                WorkoutView.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(WorkoutView.this, ViewResults.class);
                WorkoutView.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(WorkoutView.this, Settings.class);
                WorkoutView.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
