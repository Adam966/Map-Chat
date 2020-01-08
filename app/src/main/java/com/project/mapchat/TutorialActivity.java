package com.project.mapchat;

import  androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager screenPager;
    TutorialView tutorialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        List<ScreenItem> list = new ArrayList<>();

        screenPager = findViewById(R.id.screen_viewpager);
        tutorialView = new TutorialView(this,list);
        screenPager.setAdapter(tutorialView);

    }
}
