package com.project.mapchat.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.service.ServerService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetail extends AppCompatActivity {

    private SharedPrefs appSharedPrefs;
    private Intent intent;
    private ImageView reject;
    private ImageView join;
    private EventFromServer eventFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        appSharedPrefs = new SharedPrefs(this);

        intent = getIntent();
        // getting event id from intent to use request
        int eventId = Integer.parseInt(intent.getStringExtra("eventId"));

        getEventById(appSharedPrefs.getServerToken(),eventId);

        eventFromServer = new EventFromServer();

        reject = findViewById(R.id.rejectBtn);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        join = findViewById(R.id.joinBtn);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // request for getting event by id
    private void getEventById(String serverToken, int eventId) {
        Call<EventFromServer> call = ServerService
                .getInstance()
                .getEventById()
                .getEventsRequest("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<EventFromServer>() {
            @Override
            public void onResponse(Call<EventFromServer> call, Response<EventFromServer> response) {
                if(response.isSuccessful()){
                    eventFromServer = response.body();
                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventFromServer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // request for getting users from event by event id
    private void getEventUsers(String serverToken, int eventId) {
        Call<ResponseBody> call = ServerService
                .getInstance()
                .getEventUsers()
                .getEventUsers("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // join to event
    private void joinEvent(String serverToken, int eventId) {
        Call<ResponseBody> call = ServerService
                .getInstance()
                .joinEvent()
                .joinEventRequest("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
