package com.project.mapchat.chatEvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;


import com.google.android.material.tabs.TabLayout;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.main.activities.Logout;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatEvents extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager pager;
    private ViewPagerAdapter viewPagerAdapter;
    private SharedPrefs appSharedPrefs;

    private ArrayList<EventFromServer> userEventsList;
    private ArrayList<EventFromServer> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPrefs = new SharedPrefs(this);
        //setDarkMode(appSharedPrefs);

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_events);

        pager = findViewById(R.id.chatEventsPager);

        tabLayout = findViewById(R.id.chatTabLayout);
        tabLayout.setupWithViewPager(pager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(viewPagerAdapter);

    }
}
