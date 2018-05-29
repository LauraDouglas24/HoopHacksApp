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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.Result;

import java.util.ArrayList;
import java.util.List;

public class ViewResults extends AppCompatActivity {

    private RecyclerView rvResults;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String TAG = "RESULT VIEW - ";
    public Spinner resultSpinnner;
    public Button bResult;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference myRef = database.getReference().child("results").child(user.getUid());
    private FirebaseRecyclerAdapter<Result, ResultViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Results View");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(ViewResults.this, Login.class);
                    ViewResults.this.startActivity(myIntent);
                } else {
                }
            }
        };

        rvResults = findViewById(R.id.rvResults);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvResults.setLayoutManager(layoutManager);

        queryFirebase();

        rvResults.setAdapter(firebaseRecyclerAdapter);

        addToResultSpinner();

        bResult = findViewById(R.id.bResult);
        bResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resultSpinnner = findViewById(R.id.resultSpinnner);
                final String exercise = resultSpinnner.getSelectedItem().toString();

                if(exercise.equals("All")){
                    DatabaseReference myRef = database.getReference().child("results").child(user.getUid());

                    final Query query = myRef.orderByKey();
                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Result, ResultViewHolder>(
                            Result.class,
                            R.layout.result_layout,
                            ViewResults.ResultViewHolder.class,
                            query) {
                        @Override
                        protected void populateViewHolder(final ResultViewHolder viewHolder, Result model, final int position) {

                            final String timestamp = getRef(position).getKey();

                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String result = Integer.toString(dataSnapshot.child(timestamp).getValue(Result.class).getResult());
                                    String exerciseName = dataSnapshot.child(timestamp).getValue(Result.class).getExerciseName();

                                    viewHolder.setResult(result);
                                    viewHolder.setExerciseName(exerciseName);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    };
                    firebaseRecyclerAdapter.notifyDataSetChanged();
                    rvResults.getAdapter().notifyDataSetChanged();
                    rvResults.setAdapter(firebaseRecyclerAdapter);
                } else {
                    DatabaseReference myRef = database.getReference().child("results").child(user.getUid());

                    final Query query = myRef.orderByChild("exerciseName").equalTo(exercise);
                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Result, ResultViewHolder>(
                            Result.class,
                            R.layout.result_layout,
                            ViewResults.ResultViewHolder.class,
                            query) {
                        @Override
                        protected void populateViewHolder(final ResultViewHolder viewHolder, Result model, final int position) {

                            final String timestamp = getRef(position).getKey();

                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String result = Integer.toString(dataSnapshot.child(timestamp).getValue(Result.class).getResult());
                                    String exerciseName = dataSnapshot.child(timestamp).getValue(Result.class).getExerciseName();

                                    viewHolder.setResult(result);
                                    viewHolder.setExerciseName(exerciseName);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    };
                    firebaseRecyclerAdapter.notifyDataSetChanged();
                    rvResults.getAdapter().notifyDataSetChanged();
                    rvResults.setAdapter(firebaseRecyclerAdapter);
                }
            }
        });
    }

    private void queryFirebase(){
        final Query query = myRef.orderByKey();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Result, ResultViewHolder>(
                Result.class,
                R.layout.result_layout,
                ViewResults.ResultViewHolder.class,
                query) {
            @Override
            protected void populateViewHolder(final ResultViewHolder viewHolder, Result model, final int position) {

                final String timestamp = getRef(position).getKey();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String result = Integer.toString(dataSnapshot.child(timestamp).getValue(Result.class).getResult());
                        String exerciseName = dataSnapshot.child(timestamp).getValue(Result.class).getExerciseName();

                        viewHolder.setResult(result);
                        viewHolder.setExerciseName(exerciseName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
    }

    //ViewHolder for RecyclerView
    public static class ResultViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ResultViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setExerciseName(String name) {
            TextView tvExercise = (TextView) mView.findViewById(R.id.tvExercise);
            tvExercise.setText(name);
        }

        public void setResult(String result) {
            TextView tvResult = (TextView) mView.findViewById(R.id.tvResult);
            tvResult.setText(result);
        }
    }

    public void addToResultSpinner(){
        resultSpinnner = findViewById(R.id.resultSpinnner);
        DatabaseReference exerciseRef = database.getReference().child("exercises");
        final List<String> exerciseList = new ArrayList<>();

        exerciseList.add("All");

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exerciseList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        resultSpinnner.setAdapter(dataAdapter);

        exerciseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :  dataSnapshot.getChildren()){
                    exerciseList.add(ds.getKey());
                    dataAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(ViewResults.this, Login.class);
                ViewResults.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(ViewResults.this, SkillAreas.class);
                ViewResults.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(ViewResults.this, CreateWorkout.class);
                ViewResults.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(ViewResults.this, WorkoutList.class);
                ViewResults.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(ViewResults.this, UserProfile.class);
                ViewResults.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(ViewResults.this, UserFeed.class);
                ViewResults.this.startActivity(userfeedIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(ViewResults.this, Settings.class);
                ViewResults.this.startActivity(settingIntent);
                break;
            case R.id.action_menu:
                Intent menuIntent = new Intent(ViewResults.this, MenuActivity.class);
                ViewResults.this.startActivity(menuIntent);
                break;
        }
        return false;
    }
}
