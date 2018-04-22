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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hoophacks.hoophacks3.model.User;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener{

    public String TAG="RegisterProfile";

    private EditText etFirstName;
    private EditText etSurname;
    private EditText etAge;
    private EditText etHeight;
    private EditText etWeight;

    private Spinner genderSpinner, skillSpinner;
    private Button bUpdateProfile;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Getting firebase authentication uid
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Update Profile");

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etAge = (EditText) findViewById(R.id.etAge);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);

        bUpdateProfile = findViewById(R.id.bUpdateProfile);
        bUpdateProfile.setOnClickListener(this);

        addToGenderSpinner();
        addToSkillSetSpinner();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent myIntent = new Intent(UpdateProfile.this, Login.class);
                    UpdateProfile.this.startActivity(myIntent);
                } else {

                }
            }
        };
        getUserInfo();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bUpdateProfile:
                updateProfile();

                Intent myIntent = new Intent(UpdateProfile.this, UserProfile.class);
                UpdateProfile.this.startActivity(myIntent);
                break;
        }
    }

    public void addToGenderSpinner(){
        genderSpinner = findViewById(R.id.genderSpinner);
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(dataAdapter);
    }

    public void addToSkillSetSpinner(){
        skillSpinner = findViewById(R.id.skillSpinner);
        List<String> skillList = new ArrayList<>();
        skillList.add("Beginner");
        skillList.add("Intermediate");
        skillList.add("Pro");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, skillList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        skillSpinner.setAdapter(dataAdapter);
    }

    private void updateProfile(){
        // Getting firebase authentication uid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        // Realtime database Version
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etSurname.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString());
        int height = Integer.parseInt(etHeight.getText().toString());
        int weight = Integer.parseInt(etWeight.getText().toString());
        String gender = genderSpinner.getSelectedItem().toString();
        String skill = skillSpinner.getSelectedItem().toString();

        User userInfo = new User(firstName, lastName, age, height, weight, gender, skill);

        myRef.child(user.getUid()).setValue(userInfo);
    }

    private void getUserInfo() {
        DatabaseReference myRef = database.getReference().child("users").child(user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    String firstName = dataSnapshot.getValue(User.class).getFirstName();
                    String lastName = dataSnapshot.getValue(User.class).getLastName();
                    int age = dataSnapshot.getValue(User.class).getAge();
                    int height = dataSnapshot.getValue(User.class).getHeight();
                    int weight = dataSnapshot.getValue(User.class).getWeight();
                    String gender = dataSnapshot.getValue(User.class).getGender();
                    String skill = dataSnapshot.getValue(User.class).getSkill();

                    etFirstName.setText(firstName);
                    etSurname.setText(lastName);
                    etAge.setText(String.valueOf(age));
                    etHeight.setText(String.valueOf(height));
                    etWeight.setText(String.valueOf(weight));

                    ArrayAdapter genderAdapter = (ArrayAdapter) genderSpinner.getAdapter();
                    int genderPosition = genderAdapter.getPosition(gender);
                    genderSpinner.setSelection(genderPosition);

                    ArrayAdapter skillAdapter = (ArrayAdapter) skillSpinner.getAdapter();
                    int skillPosition = skillAdapter.getPosition(skill);
                    skillSpinner.setSelection(skillPosition);
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
        getMenuInflater().inflate(R.menu.menu_update_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(UpdateProfile.this, Login.class);
                UpdateProfile.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(UpdateProfile.this, SkillAreas.class);
                UpdateProfile.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(UpdateProfile.this, CreateWorkout.class);
                UpdateProfile.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(UpdateProfile.this, WorkoutList.class);
                UpdateProfile.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(UpdateProfile.this, UserProfile.class);
                UpdateProfile.this.startActivity(profileIntent);
                break;
            case R.id.action_userfeed:
                Intent userfeedIntent = new Intent(UpdateProfile.this, UserFeed.class);
                UpdateProfile.this.startActivity(userfeedIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(UpdateProfile.this, ViewResults.class);
                UpdateProfile.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(UpdateProfile.this, Settings.class);
                UpdateProfile.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}

