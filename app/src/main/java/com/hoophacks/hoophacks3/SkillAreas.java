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

public class SkillAreas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_areas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_skill_areas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent myIntent = new Intent(SkillAreas.this, Login.class);
                SkillAreas.this.startActivity(myIntent);
                break;
            case R.id.action_user_profile:
                Intent skillIntent = new Intent(SkillAreas.this, UserProfile.class);
                SkillAreas.this.startActivity(skillIntent);
                break;
            case R.id.action_settings:
                Intent settingIntent = new Intent(SkillAreas.this, Settings.class);
                SkillAreas.this.startActivity(settingIntent);
                break;
        }
        return false;
    }
}
