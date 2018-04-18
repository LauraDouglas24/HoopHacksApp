package com.hoophacks.hoophacks3;

import android.media.MediaPlayer;
import android.os.Bundle;
//import android.os.CountDownTimer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
}
