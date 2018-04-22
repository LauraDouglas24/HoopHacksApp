package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.os.CountDownTimer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CountdownTimer extends AppCompatActivity {

    private TextView tvTime;
    private Button bStart;
    private Button bStop;
    private Button bComplete;
    private CountDownTimer countdownTimer;
    private static String exerciseName;
    private static int exerciseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Timer");

        exerciseName = getIntent().getStringExtra("exerciseName");
        exerciseTime = getIntent().getIntExtra("exerciseTime", 0);

        bStart = (Button) findViewById(R.id.bStart);
        bStart.setOnClickListener(bClickListener);
        bStop = (Button) findViewById(R.id.bStop);
        bStop.setOnClickListener(bClickListener);
        tvTime = (TextView) findViewById(R.id.tvTime);


    }

private View.OnClickListener bClickListener = new View.OnClickListener(){
        @Override
    public void onClick(View v) {
            switch(v.getId()){
                case R.id.bStart :
                    startTimer();
                    break;
                case R.id.bStop :
                    stop();
                    break;
            }
        }
};

    private void startTimer(){

        countdownTimer = new CountDownTimer(exerciseTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished){
                tvTime.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                MediaPlayer mp = MediaPlayer.create(getBaseContext(),  R.raw.bleep);
                mp.start();
                tvTime.setText("Complete ! ");
            }
        };

        countdownTimer.start();
    }

    private void stop() {
        if(countdownTimer != null) {
            countdownTimer.cancel();
            countdownTimer = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(CountdownTimer.this, Login.class);
                CountdownTimer.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(CountdownTimer.this, SkillAreas.class);
                CountdownTimer.this.startActivity(skillIntent);
                break;
            case R.id.action_create_workout:
                Intent createWorkoutIntent = new Intent(CountdownTimer.this, CreateWorkout.class);
                CountdownTimer.this.startActivity(createWorkoutIntent);
                break;
            case R.id.action_view_workout:
                Intent viewWorkoutIntent = new Intent(CountdownTimer.this, WorkoutList.class);
                CountdownTimer.this.startActivity(viewWorkoutIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(CountdownTimer.this, UserProfile.class);
                CountdownTimer.this.startActivity(profileIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(CountdownTimer.this, ViewResults.class);
                CountdownTimer.this.startActivity(resultsIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(CountdownTimer.this, Settings.class);
                CountdownTimer.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
