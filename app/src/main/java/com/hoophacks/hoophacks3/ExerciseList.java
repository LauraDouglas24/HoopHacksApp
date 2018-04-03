package com.hoophacks.hoophacks3;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
        setContentView(R.layout.activity_exercise_list);

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
                        Log.i(TAG, exerciseName.toString());

                        // Read from the database
                        myRef.child(exerciseName).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                viewHolder.setName(dataSnapshot.getKey());
                                viewHolder.setImage(Uri.parse(dataSnapshot.getValue(Exercise.class).getImage()));
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

        public void setImage(Uri uri){
            ImageView ivExercise = mView.findViewById(R.id.ivExercise);
            Glide.with(mView.getContext()).load(uri).into(ivExercise);
        }
    }
}

