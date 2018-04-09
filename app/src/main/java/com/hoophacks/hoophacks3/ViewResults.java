package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.hoophacks.hoophacks3.model.Result;

import java.util.ArrayList;
import java.util.List;

public class ViewResults extends AppCompatActivity {

    private RecyclerView rvResults;
    private FirebaseRecyclerAdapter<Exercise, ExerciseList.ExerciseViewHolder> exerciseAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String TAG = "RESULT VIEW - ";
    public Spinner resultSpinnner;
    public Button bResult;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseRecyclerAdapter<Result, ResultViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        DatabaseReference myRef = database.getReference().child("results").child(user.getUid());

        rvResults = findViewById(R.id.rvResults);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvResults.setLayoutManager(layoutManager);

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
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(ViewResults.this, UserProfile.class);
                ViewResults.this.startActivity(profileIntent);
            case R.id.action_settings:
                Intent settingIntent = new Intent(ViewResults.this, Settings.class);
                ViewResults.this.startActivity(settingIntent);
                break;
        }
        return false;
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
}