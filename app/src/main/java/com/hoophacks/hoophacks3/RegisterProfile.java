package com.hoophacks.hoophacks3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterProfile extends AppCompatActivity implements View.OnClickListener{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    Intent myIntent = new Intent(RegisterProfile.this, Login.class);
                    RegisterProfile.this.startActivity(myIntent);
                } else {

                }
            }
        };

        // Getting firebase authentication uid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Getting cloud firestore data using uid
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        etFirstName.setText(task.getResult().getString("FirstName"));
                        etSurname.setText(task.getResult().getString("Surname"));
                        etAge.setText(Integer.toString(task.getResult().getDouble("Age").intValue()));
                        etHeight.setText(Integer.toString(task.getResult().getDouble("Height").intValue()));
                        etWeight.setText(Integer.toString(task.getResult().getDouble("Weight").intValue()));

                        String gender = task.getResult().getString("Gender");
                        ArrayAdapter genderAdapter = (ArrayAdapter) genderSpinner.getAdapter();
                        int genderPosition = genderAdapter.getPosition(gender);
                        genderSpinner.setSelection(genderPosition);

                        String skill = task.getResult().getString("SkillSet");
                        ArrayAdapter skillAdapter = (ArrayAdapter) skillSpinner.getAdapter();
                        int skillPosition = skillAdapter.getPosition(skill);
                        skillSpinner.setSelection(skillPosition);

                    } else {
                        Log.i(TAG, "No such document");
                    }
                } else {
                    Log.i(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bUpdateProfile:
                updateProfile();

                Intent myIntent = new Intent(RegisterProfile.this, UserProfile.class);
                RegisterProfile.this.startActivity(myIntent);
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

        Log.i(TAG, user.getUid());
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference users = db.collection("users");
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("FirstName", etFirstName.getText().toString());
//        data.put("Surname", etSurname.getText().toString());
//        data.put("Age", Integer.parseInt(etAge.getText().toString()));
//        data.put("Height", Integer.parseInt(etHeight.getText().toString()));
//        data.put("Weight", Integer.parseInt(etWeight.getText().toString()));
//        data.put("Gender", genderSpinner.getSelectedItem().toString());
//        data.put("SkillSet", skillSpinner.getSelectedItem().toString());
//        users.document(user.getUid()).set(data);

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

        UserInfo userInfo = new UserInfo(firstName, lastName, age, height, weight, gender, skill);

        myRef.child(user.getUid()).setValue(userInfo);
    }
}

