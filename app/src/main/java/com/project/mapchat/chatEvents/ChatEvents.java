package com.project.mapchat.chatEvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

        getUserEvents(appSharedPrefs.getServerToken());
        getEvents(appSharedPrefs.getServerToken());

    }

    private void getUserEvents(String serverToken) {
        Call<ArrayList<EventFromServer>> call = ServerService
                .getInstance()
                .getUserEvents()
                .getUserEventsRequest("Bearer"+" "+serverToken);

        call.enqueue(new Callback<ArrayList<EventFromServer>>() {
            @Override
            public void onResponse(Call<ArrayList<EventFromServer>> call, Response<ArrayList<EventFromServer>> response) {
                if(response.isSuccessful()){
                    userEventsList = response.body();
                    Log.wtf("User Events to Fragment",response.body().toString());
                }else{
                    switch(response.code()){
                        case 401:{
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EventFromServer>> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getEvents(String serverToken) {
        Call<ArrayList<EventFromServer>> call = ServerService
                .getInstance()
                .getEvents()
                .getEventsRequest("Bearer"+" "+serverToken);

        call.enqueue(new Callback<ArrayList<EventFromServer>>() {
            @Override
            public void onResponse(Call<ArrayList<EventFromServer>> call, Response<ArrayList<EventFromServer>> response) {
                if(response.isSuccessful()){
                    eventsList = response.body();
                    Log.wtf("Events to Fragment",response.body().toString());
                }else{
                    switch(response.code()){
                        case 401:{
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EventFromServer>> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<EventFromServer> getUserEventsList() {
        return userEventsList;
    }

    public ArrayList<EventFromServer> getEventsList() {
        return eventsList;
    }
}
