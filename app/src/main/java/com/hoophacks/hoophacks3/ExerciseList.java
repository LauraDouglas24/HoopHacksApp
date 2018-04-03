package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.Exercise;

public class ExerciseList extends AppCompatActivity {

    public String TAG="HoopHacks - ExerciseList";

    private RecyclerView rvExercises;
    private FirebaseRecyclerAdapter<Exercise, ExerciseViewHolder> exerciseAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("exercises");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvExercises = findViewById(R.id.rvExercises);
        rvExercises.setHasFixedSize(true);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));

        Log.i(TAG, "onCreate");

        final FirebaseRecyclerAdapter<Exercise, ExerciseViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Exercise, ExerciseViewHolder>(
                        Exercise.class,
                        R.layout.item_layout,
                        ExerciseViewHolder.class,
                        myRef)
                {
                    @Override
                    protected void populateViewHolder(final ExerciseViewHolder viewHolder, Exercise model, int position) {

                        final String exerciseName = getRef(position).getKey();

                        // Read from the database
                        myRef.child(exerciseName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i(TAG, exerciseName.toString());
                                viewHolder.setName(dataSnapshot.getKey());
                                viewHolder.setImage(Uri.parse(dataSnapshot.getValue(Exercise.class).getImage()));
                                viewHolder.setSkillLevel(dataSnapshot.getValue(Exercise.class).getSkillLevel());

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    }
                };

        rvExercises.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_list, menu);
        return true;
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
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(ExerciseList.this, UserProfile.class);
                ExerciseList.this.startActivity(profileIntent);
            case R.id.action_settings:
                Intent settingIntent = new Intent(ExerciseList.this, Settings.class);
                ExerciseList.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
