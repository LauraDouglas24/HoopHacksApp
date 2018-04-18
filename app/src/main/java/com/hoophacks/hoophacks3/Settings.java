package com.hoophacks.hoophacks3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the MainMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(Settings.this, Login.class);
                Settings.this.startActivity(myIntent);
                break;
            case R.id.action_skill_areas:
                Intent skillIntent = new Intent(Settings.this, SkillAreas.class);
                Settings.this.startActivity(skillIntent);
                break;
            case R.id.action_view_results:
                Intent resultsIntent = new Intent(Settings.this, ViewResults.class);
                Settings.this.startActivity(resultsIntent);
                break;
            case R.id.action_user_profile:
                Intent profileIntent = new Intent(Settings.this, UserProfile.class);
                Settings.this.startActivity(profileIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(Settings.this, Settings.class);
                Settings.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
