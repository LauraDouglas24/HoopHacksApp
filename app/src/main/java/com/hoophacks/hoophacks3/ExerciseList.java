package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.Exercise;

public class ExerciseList extends AppCompatActivity {

    private RecyclerView rvExercises;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseRecyclerAdapter<Exercise, ExerciseViewHolder> exerciseAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("exercises");

    private String exerciseVideo;

    public static String TAG = "EXERCISE LIST - ";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Exercise List");

        final String skillArea = getIntent().getStringExtra("skillArea");

        rvExercises = findViewById(R.id.rvExercises);
        rvExercises.setHasFixedSize(true);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));

        //Query to query firebase exercise object by skill area
        final Query query = myRef.orderByChild("skillArea").equalTo(skillArea);
        final FirebaseRecyclerAdapter<Exercise, ExerciseViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Exercise, ExerciseViewHolder>(
                        Exercise.class,
                        R.layout.item_layout,
                        ExerciseViewHolder.class,
                        query)
                {
                    @Override
                    protected void populateViewHolder(final ExerciseViewHolder viewHolder, Exercise model, final int position) {

                        final String exerciseName = getRef(position).getKey();

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String exerciseTip = dataSnapshot.child(exerciseName).getValue(Exercise.class).getTip();
                                String exerciseSkillLevel = dataSnapshot.child(exerciseName).getValue(Exercise.class).getSkillLevel();
                                String exerciseUri = dataSnapshot.child(exerciseName).getValue(Exercise.class).getImage();

                                //populating viewHolder
                                viewHolder.setName(exerciseName);
                                viewHolder.setImage(Uri.parse(exerciseUri));
                                viewHolder.setSkillLevel(exerciseSkillLevel);
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
                                        //Checking if there is a video in the exercises
                                        try {
                                            exerciseVideo = dataSnapshot.getValue(Exercise.class).getVideo();
                                        } catch (Exception e) {
                                            exerciseVideo = null;
                                            return;
                                        }

                                        //Passing exercise values to ExerciseView, passing video depending on exercise
                                        if (exerciseVideo != null) {
                                            Intent intent = new Intent(ExerciseList.this, ExerciseViewVideo.class);
                                            intent.putExtra("exerciseName", exerciseName);
                                            intent.putExtra("skillArea", skillArea);
                                            intent.putExtra("exerciseVideo", exerciseVideo);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(ExerciseList.this, ExerciseView.class);
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
            //Using Glide to load the image from its Uri
            Glide.with(mView.getContext()).load(uri).into(ivExercise);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExerciseList.this, SkillAreas.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(ExerciseList.this, Login.class);
                ExerciseList.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(ExerciseList.this, SkillAreas.class);
                ExerciseList.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(ExerciseList.this, CreateWorkout.class);
                ExerciseList.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(ExerciseList.this, WorkoutList.class);
                ExerciseList.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(ExerciseList.this, UserProfile.class);
                ExerciseList.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(ExerciseList.this, UserFeed.class);
                ExerciseList.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(ExerciseList.this, ViewResults.class);
                ExerciseList.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(ExerciseList.this, Settings.class);
                ExerciseList.this.startActivity(settingIntent);
                break;
            case R.id.action_menu:
                Intent menuIntent = new Intent(ExerciseList.this, MenuActivity.class);
                ExerciseList.this.startActivity(menuIntent);
                break;
        }
        return false;
    }
}

