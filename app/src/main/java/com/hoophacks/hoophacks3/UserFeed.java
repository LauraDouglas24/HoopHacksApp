package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.hoophacks.hoophacks3.model.Status;
import com.hoophacks.hoophacks3.model.User;

public class UserFeed extends AppCompatActivity implements View.OnClickListener {

    private EditText etStatus;
    private  TextView tvLikes;
    private Button bPost;
    private ImageButton ibLikes;
    private RecyclerView rvUserfeed;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String name;
    private FirebaseRecyclerAdapter<Status, UserFeed.UserfeedViewHolder> firebaseRecyclerAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String TAG = "USERFEED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Userfeed");

        etStatus = findViewById(R.id.etStatus);
        bPost = findViewById(R.id.bPost);
        bPost.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(UserFeed.this, Login.class);
                    UserFeed.this.startActivity(myIntent);
                } else {
                    String userId = user.getUid();
                }
            }
        };

        getUserName();

        final DatabaseReference myRef = database.getReference().child("userfeed");

        rvUserfeed = findViewById(R.id.rvUserfeed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvUserfeed.setLayoutManager(layoutManager);

        final Query query = myRef.orderByKey();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Status, UserfeedViewHolder>(
                Status.class,
                R.layout.userfeed_item_layout,
                UserfeedViewHolder.class,
                query) {

            @Override
            protected void populateViewHolder(final UserfeedViewHolder viewHolder, Status model, final int position) {

                final String timestamp = getRef(position).getKey();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String status = dataSnapshot.child(timestamp).getValue(Status.class).getStatus();
                        String name = dataSnapshot.child(timestamp).getValue(Status.class).getName();
                        int likes = dataSnapshot.child(timestamp).getValue(Status.class).getLikes();

                        viewHolder.setStatus(status);
                        viewHolder.setName(name);
                        viewHolder.setLikes(likes);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                viewHolder.ibLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Query query = myRef.orderByKey();
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String status = dataSnapshot.child(timestamp).getValue(Status.class).getStatus();
                                String name = dataSnapshot.child(timestamp).getValue(Status.class).getName();
                                int likes = dataSnapshot.child(timestamp).getValue(Status.class).getLikes();

                                likes ++;
                                viewHolder.ibLikes.setEnabled(false);

                                Status statusInfo = new Status(status, name, likes);

                                DatabaseReference userfeedRef = database.getReference("userfeed");
                                userfeedRef.child(timestamp).setValue(statusInfo);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }

                        });

                    }
                });
            }
        };
        rvUserfeed.setAdapter(firebaseRecyclerAdapter);

    }

    public void getUserName(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.getValue(User.class).getFirstName();
                String lastName = dataSnapshot.getValue(User.class).getLastName();

                name = firstName + " " + lastName;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static class UserfeedViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public ImageButton ibLikes;

        public UserfeedViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            ibLikes = mView.findViewById(R.id.ibLikes);
        }

        public void setStatus(String status) {
            TextView tvStatus = (TextView) mView.findViewById(R.id.tvStatus);
            tvStatus.setText(status);
        }

        public void setName(String name) {
            TextView tvName = (TextView) mView.findViewById(R.id.tvName);
            tvName.setText(name);
        }

        public void setLikes(int likes) {
            TextView tvLikes = (TextView) mView.findViewById(R.id.tvLikes);
            tvLikes.setText(String.valueOf(likes));
        }
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bPost:
                String timestamp = Long.toString(System.currentTimeMillis()/1000);
                String status = etStatus.getText().toString();
                int likes = 0;
                Status statusInfo = new Status(status, name, likes);

                DatabaseReference userfeedRef = database.getReference("userfeed");
                userfeedRef.child(timestamp).setValue(statusInfo);

                etStatus.setText("");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(UserFeed.this, Login.class);
                UserFeed.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(UserFeed.this, SkillAreas.class);
                UserFeed.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(UserFeed.this, CreateWorkout.class);
                UserFeed.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(UserFeed.this, WorkoutList.class);
                UserFeed.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(UserFeed.this, UserProfile.class);
                UserFeed.this.startActivity(profileIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(UserFeed.this, ViewResults.class);
                UserFeed.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(UserFeed.this, Settings.class);
                UserFeed.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
