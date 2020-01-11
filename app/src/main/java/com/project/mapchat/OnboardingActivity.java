package com.project.mapchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class OnboardingActivity extends AppCompatActivity {

    private SharedPrefs appSharedPrefs;
    private ViewPager screenPager;
    TutorialView tutorialView;
    TabLayout tab_tutorial;
    Button btnRedirect;
    Button btnFinish;
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // OnboardingActivity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_onboarding);

        // initialize own SharedPreferences Class
        appSharedPrefs = new SharedPrefs(this);

        // Shared Preferences class for check first time run on application
        checkFirstTime(appSharedPrefs);

        tab_tutorial = findViewById(R.id.tab_tutorial);

        // List that contains items for screenPager in activity_onboarding.xml
        final List<ScreenItem> list = new ArrayList<>();
        // add item and setup item text and image throughScreenItem class constructor
        list.add(new ScreenItem("Text1","text1",R.drawable.ic_menu_24px));
        list.add(new ScreenItem("Text2","text2",R.drawable.ic_home_24px));
        list.add(new ScreenItem("Text3","text3",R.drawable.ic_settings_applications_24px));

        screenPager = findViewById(R.id.screen_viewpager);
        tutorialView = new TutorialView(this,list);
        screenPager.setAdapter(tutorialView);

        // for handling events in screenPager
        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // check if user is on last screen(image with 2 text fields) of screenPager
                if(position == screenPager.getAdapter().getCount()-1){
                    lastTutorialScreen();
                }else{
                    // to handle if user wanted to go back from last screen
                    otherTutorialScreens();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // setup with the screen page in activity_onboarding.xml
        tab_tutorial.setupWithViewPager(screenPager);

        btnRedirect = findViewById(R.id.btnOnboarding);
        btnFinish = findViewById(R.id.btnFinish);

        btnRedirect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // handle to load next screen with a button (not swipe)
                position = screenPager.getCurrentItem();
                if(position < list.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }
                // check if user is on last screen of screenPager or is he swipe to back
                if(position == list.size()-1){
                    lastTutorialScreen();
                }else{
                    otherTutorialScreens();
                }
            }
        });

        // redirect to main activity
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnboardingActivity.this,MainActivity.class);
                startActivity(i);
                appSharedPrefs.setOnboardingState(true);
                finish();
            }
        });
   }

    private void lastTutorialScreen(){

            btnRedirect.setVisibility(View.INVISIBLE);
            tab_tutorial.setVisibility(View.INVISIBLE);
    }

    private void otherTutorialScreens(){

        btnRedirect.setVisibility(View.VISIBLE);
        tab_tutorial.setVisibility(View.VISIBLE);
    }

    private void checkFirstTime(SharedPrefs prefs){
        if(prefs.getOnboardingState()){
            Intent i = new Intent(OnboardingActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
