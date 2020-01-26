package com.project.mapchat.chatEvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.project.mapchat.R;

public class ChatEvents extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_events);

        pager = findViewById(R.id.chatEventsPager);

        tabLayout = findViewById(R.id.chatTabLayout);
        tabLayout.setupWithViewPager(pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(viewPagerAdapter);
    }
}
