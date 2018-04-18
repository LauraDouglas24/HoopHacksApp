package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.User;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{

    public String TAG="UserProfile";

    private TextView tvFirstNameData;
    private TextView tvLastNameData;
    private TextView tvAgeData;
    private TextView tvHeightData;
    private TextView tvWeightData;
    private TextView tvGenderData;
    private TextView tvSkillData;

    private Button bUpdateProfile;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvFirstNameData = findViewById(R.id.tvFirstNameData);
        tvLastNameData = findViewById(R.id.tvLastNameData);
        tvAgeData = findViewById(R.id.tvAgeData);
        tvHeightData = findViewById(R.id.tvHeightData);
        tvWeightData = findViewById(R.id.tvWeightData);
        tvGenderData = findViewById(R.id.tvGenderData);
        tvSkillData = findViewById(R.id.tvSkillData);

        bUpdateProfile = findViewById(R.id.bUpdateProfile);
        bUpdateProfile.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" User Profile ");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(UserProfile.this, Login.class);
                    UserProfile.this.startActivity(myIntent);
                } else {

                }
            }
        };

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // Read from the database
        myRef.child("users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            User uInfo = new User();

            if(ds.getKey().equals(user.getUid())) {
                uInfo.setFirstName(ds.getValue(User.class).getFirstName());
                uInfo.setLastName(ds.getValue(User.class).getLastName());
                uInfo.setAge(ds.getValue(User.class).getAge());
                uInfo.setHeight(ds.getValue(User.class).getHeight());
                uInfo.setWeight(ds.getValue(User.class).getWeight());
                uInfo.setGender(ds.getValue(User.class).getGender());
                uInfo.setSkill(ds.getValue(User.class).getSkill());

                tvFirstNameData.setText(uInfo.getFirstName());
                tvLastNameData.setText(uInfo.getLastName());
                tvAgeData.setText(Integer.toString(uInfo.getAge()));
                tvHeightData.setText(Integer.toString(uInfo.getHeight()));
                tvWeightData.setText(Integer.toString(uInfo.getWeight()));
                tvGenderData.setText(uInfo.getGender());
                tvSkillData.setText(uInfo.getSkill());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bUpdateProfile:
                Intent myIntent = new Intent(UserProfile.this, UpdateProfile.class);
                UserProfile.this.startActivity(myIntent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(UserProfile.this, Login.class);
                UserProfile.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(UserProfile.this, SkillAreas.class);
                UserProfile.this.startActivity(skillIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(UserProfile.this, ViewResults.class);
                UserProfile.this.startActivity(resultsIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(UserProfile.this, UserFeed.class);
                UserProfile.this.startActivity(userfeedIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(UserProfile.this, Settings.class);
                UserProfile.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
