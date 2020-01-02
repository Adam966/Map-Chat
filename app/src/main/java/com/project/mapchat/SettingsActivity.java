package com.project.mapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button logoutBtn;
    private CircleImageView circleImageView;
    private SharedPrefs appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPrefs = new SharedPrefs(this);

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Switch switchDarkMode = findViewById(R.id.switchDark);
        if(appSharedPrefs.loadDarkModeState() == true){
            switchDarkMode.setChecked(true);
        }
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    appSharedPrefs.setDarkModeState(true);
                    restart();
                }else{
                    appSharedPrefs.setDarkModeState(false);
                    restart();
                }
            }
        });

        logoutBtn = findViewById(R.id.btn_logout);
        circleImageView = findViewById(R.id.profile_image);

        setImage(appSharedPrefs.getId());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent i = new Intent(SettingsActivity.this,Login.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        //Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentHome = new Intent(SettingsActivity.this,MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_groups:
                        //Toast.makeText(MainActivity.this, "Group Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentGroups = new Intent(SettingsActivity.this, EventsActivity.class);
                        startActivity(intentGroups);
                        break;
                    case R.id.action_settings:
                        //Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentSettings = new Intent(SettingsActivity.this,SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                }
                return true;
            }
        });
    }

    private void setImage(String id){
        String imageUrl = "https://graph.facebook.com/"+id+"/picture?type=normal";
        Glide.with(SettingsActivity.this).load(imageUrl).into(circleImageView);
    }

    private void restart(){
        Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
    }

}
