package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.Workout;

import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {

    private RecyclerView rvWorkouts;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseRecyclerAdapter<Workout, WorkoutList.WorkoutViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(WorkoutList.this, Login.class);
                    WorkoutList.this.startActivity(myIntent);
                } else {
                }
            }
        };

        rvWorkouts = findViewById(R.id.rvWorkouts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvWorkouts.setLayoutManager(layoutManager);

        final DatabaseReference myRef = database.getReference().child("workouts").child(user.getUid());
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Workout, WorkoutViewHolder>(
                Workout.class,
                R.layout.item_workout_list,
                WorkoutList.WorkoutViewHolder.class,
                myRef) {
            @Override
            protected void populateViewHolder(final  WorkoutList.WorkoutViewHolder viewHolder, Workout model, final int position) {

                final String user = getRef(position).getKey();

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String workoutName = dataSnapshot.child(user).getKey();
                        ArrayList<String> workoutExercises = dataSnapshot.child(user).getValue(Workout.class).getExercises();

                        viewHolder.setWorkoutName(workoutName);
                        viewHolder.setWorkoutExercises(workoutExercises);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                //Meant to send user to workout view and display the exercises within the workout
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View view) {
//                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String workoutName = dataSnapshot.child(user).getKey();
//
//                                Intent intent = new Intent(WorkoutList.this, WorkoutView.class);
//                                intent.putExtra("workoutName", workoutName);
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                });
            }
        };
        rvWorkouts.setAdapter(firebaseRecyclerAdapter);
    }

    //ViewHolder for RecyclerView
    public static class WorkoutViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public WorkoutViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setWorkoutName(String name) {
            TextView tvWorkout = (TextView) mView.findViewById(R.id.tvWorkout);
            tvWorkout.setText(name);
        }

        public void setWorkoutExercises(ArrayList<String> exercises) {
            TextView tvWorkoutExercises = mView.findViewById(R.id.tvWorkoutExercises);

            String formattedWorkoutExercises = "";

            for(String exercise : exercises) {
                formattedWorkoutExercises = formattedWorkoutExercises + exercise + "\n";
            }

            tvWorkoutExercises.setText(formattedWorkoutExercises);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(WorkoutList.this, Login.class);
                WorkoutList.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(WorkoutList.this, SkillAreas.class);
                WorkoutList.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(WorkoutList.this, CreateWorkout.class);
                WorkoutList.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(WorkoutList.this, WorkoutView.class);
                WorkoutList.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(WorkoutList.this, UserProfile.class);
                WorkoutList.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(WorkoutList.this, UserFeed.class);
                WorkoutList.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(WorkoutList.this, ViewResults.class);
                WorkoutList.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(WorkoutList.this, Settings.class);
                WorkoutList.this.startActivity(settingIntent);
                break;
            case R.id.action_menu:
                Intent menuIntent = new Intent(WorkoutList.this, MenuActivity.class);
                WorkoutList.this.startActivity(menuIntent);
                break;
        }
        return false;
    }
}
