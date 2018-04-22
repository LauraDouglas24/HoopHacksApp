package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.hoophacks.hoophacks3.model.Exercise;
import com.hoophacks.hoophacks3.model.Workout;

import java.sql.Struct;
import java.util.ArrayList;

public class WorkoutList extends AppCompatActivity {

    private RecyclerView rvWorkouts;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseRecyclerAdapter<Workout, WorkoutList.WorkoutViewHolder> firebaseRecyclerAdapter;

    public static String TAG = "WORKOUT LIST - ";

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

        final DatabaseReference myRef = database.getReference().child("workouts").child(user.getUid());

        rvWorkouts = findViewById(R.id.rvWorkouts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvWorkouts.setLayoutManager(layoutManager);

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

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String workoutName = dataSnapshot.child(user).getKey();

                                Intent intent = new Intent(WorkoutList.this, WorkoutView.class);
                                intent.putExtra("workoutName", workoutName);
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };
        rvWorkouts.setAdapter(firebaseRecyclerAdapter);
    }

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
                Log.i(TAG, exercise);

                formattedWorkoutExercises = formattedWorkoutExercises + exercise + "\n";
            }

            tvWorkoutExercises.setText(formattedWorkoutExercises);
        }
    }

}
