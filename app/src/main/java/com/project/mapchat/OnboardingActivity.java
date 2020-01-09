package com.project.mapchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class OnboardingActivity extends AppCompatActivity {

    private SharedPrefs appSharedPrefs;
    private ViewPager screenPager;
    TutorialView tutorialView;
    TabLayout tab_tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        tab_tutorial = findViewById(R.id.tab_tutorial);

        // Shared Preferences class for check first time run on application
        appSharedPrefs = new SharedPrefs(this);

        List<ScreenItem> list = new ArrayList<>();
        list.add(new ScreenItem("Text1","text1",R.drawable.ic_menu_24px));
        list.add(new ScreenItem("Text2","text2",R.drawable.ic_home_24px));
        list.add(new ScreenItem("Text2","text2",R.drawable.ic_settings_applications_24px));

        screenPager = findViewById(R.id.screen_viewpager);
        tutorialView = new TutorialView(this,list);
        screenPager.setAdapter(tutorialView);

        // setup with the screen page in activity_onboarding.xml
        tab_tutorial.setupWithViewPager(screenPager);

        Button btnRedirect = findViewById(R.id.btnOnboarding);
/*
        // Method for check first time run on application
        if(checkFirstTime()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            btnRedirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(OnboardingActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
 */
    }

    private boolean checkFirstTime(){
        if(appSharedPrefs.getOnboardingState()){
            return true;
        } else {
            appSharedPrefs.setOnboardingState(true);
            return false;
        }
    }
}
