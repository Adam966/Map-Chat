package com.project.mapchat.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.EventToSend;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends AppCompatActivity {
    private SharedPrefs appSharedPrefs;
    private SearchView searchView;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPrefs = new SharedPrefs(this);
        getEvents(appSharedPrefs.getServerToken());

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        searchView = findViewById(R.id.searchViewEvents);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                eventAdapter.getFilter().filter(s.toLowerCase());
                return false;
            }
        });

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
                        Intent intentHome = new Intent(EventsActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_groups:
                        //Toast.makeText(MainActivity.this, "Group Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentGroups = new Intent(EventsActivity.this, EventsActivity.class);
                        startActivity(intentGroups);
                        break;
                    case R.id.action_settings:
                        //Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentSettings = new Intent(EventsActivity.this, SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                }
                return true;
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
                    ArrayList<EventFromServer> list = response.body();
                    setAdapter(list);
                    Log.wtf("Events",response.body().toString());
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

    private void setAdapter(ArrayList<EventFromServer> list) {

        RecyclerView recyclerView = findViewById(R.id.eventItemRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(list,this);
        recyclerView.setAdapter(eventAdapter);
    }
}

