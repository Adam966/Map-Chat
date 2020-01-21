package com.project.mapchat.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetail extends AppCompatActivity {

    SharedPrefs appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        appSharedPrefs = new SharedPrefs(this);
        int eventId=1;

        getUserEvents(appSharedPrefs.getServerToken(),eventId);

    }

    private void getUserEvents(String serverToken, int eventId) {
        Call<EventFromServer> call = ServerService
                .getInstance()
                .getEventById()
                .getEventsRequest("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<EventFromServer>() {
            @Override
            public void onResponse(Call<EventFromServer> call, Response<EventFromServer> response) {
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
            public void onFailure(Call<EventFromServer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
