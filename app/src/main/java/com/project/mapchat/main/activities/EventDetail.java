package com.project.mapchat.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.UserInfoData;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

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
    private Button viewViewUsersBtn;
    private ArrayList<UserInfoData> usersFromEventList;

    // views for show data from event
    private TextView groupName,createDate,eventDesc,meetTime,place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        appSharedPrefs = new SharedPrefs(this);

        // settings views
        groupName = findViewById(R.id.groupName);
        createDate = findViewById(R.id.createDate);
        eventDesc = findViewById(R.id.eventDesc);
        meetTime = findViewById(R.id.meetTime);
        place = findViewById(R.id.place);

        intent = getIntent();
        // getting event id from intent to use request
        final int eventId = Integer.parseInt(intent.getStringExtra("eventId"));

        getEventById(appSharedPrefs.getServerToken(),eventId);

        eventFromServer = new EventFromServer();

        viewViewUsersBtn = findViewById(R.id.viewEventUsersBtn);
        viewViewUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ListOfUsers.class);
                i.putExtra("userList",usersListToString(usersFromEventList));
                startActivity(i);
            }
        });

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
                joinEvent(appSharedPrefs.getServerToken(),eventId);
            }
        });
    }

    // request for getting event by id
    private void getEventById(final String serverToken, final int eventId) {
        Call<EventFromServer> call = ServerService
                .getInstance()
                .getEventById()
                .getEventsRequest("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<EventFromServer>() {
            @Override
            public void onResponse(Call<EventFromServer> call, Response<EventFromServer> response) {
                if(response.isSuccessful()){
                    eventFromServer = response.body();
                    setViewValues(eventFromServer);
                    getEventUsers(serverToken,eventId);
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
        Call<ArrayList<UserInfoData>> call = ServerService
                .getInstance()
                .getEventUsers()
                .getEventUsers("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<ArrayList<UserInfoData>>() {
            @Override
            public void onResponse(Call<ArrayList<UserInfoData>> call, Response<ArrayList<UserInfoData>> response) {
                if(response.isSuccessful()){
                    usersFromEventList = response.body();
                    setButtonText(usersFromEventList);
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
            public void onFailure(Call<ArrayList<UserInfoData>> call, Throwable t) {
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
                    Toast.makeText(getApplicationContext(),
                            "You have joined the event "+eventFromServer.getGroupName(),Toast.LENGTH_LONG);
                }else {
                    switch(response.code()){
                        case 401:{

                            Toast.makeText(getApplicationContext(),
                                    "Rejected to join to event - "+eventFromServer.getGroupName(),Toast.LENGTH_LONG);

                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        case 500:{
                            Toast.makeText(getApplicationContext(),"Problem with connection or server ",Toast.LENGTH_LONG).show();
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

    private void setViewValues(EventFromServer event){
        groupName.setText(event.getGroupName());
        createDate.setText(event.getCreationTime());
        eventDesc.setText(event.getDescription());
        meetTime.setText(event.getMeetTime());

        String location = event.getLocation().getTown() +
                " "+event.getLocation().getAddress()+
                " "+event.getLocation().getPostalCode()+
                " "+event.getLocation().getCountry();

        place.setText(location);
    }

    private void setButtonText(ArrayList<UserInfoData> list){
        String userSize = String.valueOf(list.size());
        viewViewUsersBtn.setText(userSize);
    }

    private String usersListToString(ArrayList<UserInfoData> list){
        Gson gson = new Gson();
        String listToSend = gson.toJson(list);
        return listToSend;
    }

}
