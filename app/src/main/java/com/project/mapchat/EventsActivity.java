package com.project.mapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventsActivity extends AppCompatActivity {

    private SharedPrefs modSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        modSharedPrefs = new SharedPrefs(this);

        if(modSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        //Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentHome = new Intent(EventsActivity.this,MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_groups:
                        //Toast.makeText(MainActivity.this, "Group Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentGroups = new Intent(EventsActivity.this, EventsActivity.class);
                        startActivity(intentGroups);
                        break;
                    case R.id.action_settings:
                        //Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentSettings = new Intent(EventsActivity.this,SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                }
                return true;
            }
        });
    }
}


