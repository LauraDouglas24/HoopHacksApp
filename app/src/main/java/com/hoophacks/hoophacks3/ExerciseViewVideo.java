package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.hoophacks.hoophacks3.model.Exercise;

public class ExerciseViewVideo extends YouTubeBaseActivity implements View.OnClickListener,
 YouTubePlayer.OnInitializedListener{

    private ImageView ivExercise;
    private TextView tvExerciseTip;
    private Button bBack;
    private Button bStart;
    public static String TAG = "EXERCISE VIEW - ";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("exercises");

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    public static final String API_KEY = "AIzaSyDsUH46VFTzb7gAnCImv4dSTBHY-qD-LwQ";

    private static String skillArea;
    private static String exerciseName;
    private static String exerciseTip;
    private static String exerciseSkillLevel;
    private static String exerciseUri;
    private static String exerciseVideo;
    private static int exerciseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_view_video);
        setTitle("Exercise View");

        skillArea = getIntent().getStringExtra("skillArea");
        exerciseName = getIntent().getStringExtra("exerciseName");
        exerciseVideo = getIntent().getStringExtra("exerciseVideo");

        youTubeView = findViewById(R.id.vExercise);
        youTubeView.initialize(API_KEY, this);

        bBack = findViewById(R.id.bBack);
        bBack.setOnClickListener(this);

        bStart = findViewById(R.id.bStart);
        bStart.setOnClickListener(this);

        final Query query = myRef.orderByKey().equalTo(exerciseName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                exerciseTip = dataSnapshot.child(exerciseName).getValue(Exercise.class).getTip();
                exerciseSkillLevel = dataSnapshot.child(exerciseName).getValue(Exercise.class).getSkillLevel();

                try {
                    exerciseTime = dataSnapshot.child(exerciseName).getValue(Exercise.class).getTime();
                } catch (Exception e) {
                    exerciseTime = 0;
                    return;
                }

                TextView tvTitle = findViewById(R.id.tvTitle);
                tvTitle.setText(exerciseName);

                TextView tvExerciseTip = findViewById(R.id.tvExerciseTip);
                tvExerciseTip.setText(exerciseTip);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExerciseViewVideo.this, ExerciseList.class);
        intent.putExtra("skillArea", skillArea);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBack:
                Intent intent = new Intent(ExerciseViewVideo.this, ExerciseList.class);
                intent.putExtra("skillArea", skillArea);
                startActivity(intent);
            case R.id.bStart:
                if(exerciseTime != 0) {
                    Intent startIntent = new Intent(ExerciseViewVideo.this, CountdownTimer.class);
                    startIntent.putExtra("exerciseName", exerciseName);
                    startIntent.putExtra("exerciseTime", exerciseTime);
                    startActivity(startIntent);
                } else {
                    Intent startIntent = new Intent(ExerciseViewVideo.this, EnterResults.class);
                    startIntent.putExtra("exerciseName", exerciseName);
                    startActivity(startIntent);
                }
        }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(exerciseVideo); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(ExerciseViewVideo.this, Login.class);
                ExerciseViewVideo.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(ExerciseViewVideo.this, SkillAreas.class);
                ExerciseViewVideo.this.startActivity(skillIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(ExerciseViewVideo.this, ViewResults.class);
                ExerciseViewVideo.this.startActivity(resultsIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(ExerciseViewVideo.this, UserProfile.class);
                ExerciseViewVideo.this.startActivity(profileIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(ExerciseViewVideo.this, Settings.class);
                ExerciseViewVideo.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
