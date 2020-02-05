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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetail extends AppCompatActivity {

    private SharedPrefs appSharedPrefs;
    private Intent intent;
    private ImageView rejectBtn, joinBtn, exitEventBtn;
    private EventFromServer eventFromServer;
    private Button viewViewUsersBtn;
    private ArrayList<UserInfoData> usersFromEventList;

    // views for show data from event
    private TextView groupName,createDate,eventDesc, meetDate,place,timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        appSharedPrefs = new SharedPrefs(this);

        // settings views
        groupName = findViewById(R.id.groupName);
        createDate = findViewById(R.id.createDate);
        eventDesc = findViewById(R.id.eventDesc);
        meetDate = findViewById(R.id.meetDate);
        place = findViewById(R.id.place);
        timeText = findViewById(R.id.timeText);

        // getting intent
        intent = getIntent();

        // getting event id from intent to use request
        final int eventId = Integer.parseInt(intent.getStringExtra("eventId"));

        getEventById(appSharedPrefs.getServerToken(),eventId);
        getIfUserIsInEvent(appSharedPrefs.getServerToken(),eventId);

        // show users who are in event already - getEventUsers request scroll down
        viewViewUsersBtn = findViewById(R.id.viewEventUsersBtn);
        viewViewUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ListOfUsers.class);
                i.putExtra("userList",usersListToString(usersFromEventList));
                startActivity(i);
            }
        });

        // onclick event that finish activity
        rejectBtn = findViewById(R.id.rejectBtn);
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // onclick event for joinBtn to event - joinEvent request scroll down
        joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinEvent(appSharedPrefs.getServerToken(),eventId);
            }
        });

        exitEventBtn = findViewById(R.id.exitEventBtn);
        exitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO leave request
                leaveEvent(appSharedPrefs.getServerToken(),eventId);
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

                    // setting values to views in EventDetail activity layout
                    setViewValues(eventFromServer);

                    // getting users who are in event
                    getEventUsers(serverToken,eventId);
                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                        break;
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

                    // set button number of people who are in actual event
                    setButtonText(usersFromEventList);
                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<UserInfoData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // joinBtn to event
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
                    restart();
                }else {
                    switch(response.code()){
                        case 401:{

                            Toast.makeText(getApplicationContext(),
                                    "Rejected to joinBtn to event - "+eventFromServer.getGroupName(),Toast.LENGTH_LONG);

                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Problem with connection or server ",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // exitBtn to leave event
    private void leaveEvent(String serverToken, int eventId) {
        Call<ResponseBody> call = ServerService
                .getInstance()
                .leaveEvent()
                .leaveEventRequest("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "You have joined the event "+eventFromServer.getGroupName(),Toast.LENGTH_LONG);
                    restart();

                }else {
                    switch(response.code()){
                        case 401:{

                            Toast.makeText(getApplicationContext(),
                                    "Rejected to joinBtn to event - "+eventFromServer.getGroupName(),Toast.LENGTH_LONG);

                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Problem with connection or server ",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // check if user is in event or not, for purpose to hide joinBtn button
    private void getIfUserIsInEvent(String serverToken, int eventId) {
        Call<ResponseBody> call = ServerService
                .getInstance()
                .getIfUserIsInEvent()
                .checkIfUserIsInEventRequest("Bearer " + serverToken,eventId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    boolean state = true;
                    checkJoinedUser(state);

                }else {
                    switch(response.code()){
                        case 400 :{
                            boolean state = false;
                            checkJoinedUser(state);
                        }
                        break;

                        case 401:{

                            Toast.makeText(getApplicationContext(),
                                    "Unauthorized - "+eventFromServer.getGroupName(),Toast.LENGTH_LONG);

                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Problem with connection or server ",Toast.LENGTH_LONG).show();
                        }
                        break;
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
        Log.wtf("LOCATION", event.getLocation().toString());

        groupName.setText(event.getGroupName());
        eventDesc.setText(event.getDescription());
        place.setText(event.getLocation().getAddress());

        // parsing and setting date,time to views
        createDate.setText(parseDate(event.getCreationTime()));
        meetDate.setText(parseDate(event.getMeetTime()));
        timeText.setText(parseTime(event.getMeetTime()));

        /*
        createDate.setText(event.getCreationTime().replace('T', ' '));
        meetDate.setText(event.getMeetTime().replace('T', ' '));
         */

        /*
        String location = event.getLocation().getTown() +
                " "+event.getLocation().getAddress()+
                " "+event.getLocation().getPostalCode()+
                " "+event.getLocation().getCountry();*/

    }

    private void setButtonText(ArrayList<UserInfoData> list){
        String userSize = String.valueOf(list.size());
        viewViewUsersBtn.setText(userSize);
    }

    // method for passing list (user from actual event) to another activity (ListOfUsers)
    private String usersListToString(ArrayList<UserInfoData> list){
        Gson gson = new Gson();
        String listToSend = gson.toJson(list);
        return listToSend;
    }


    private void checkJoinedUser(boolean state){
        if(state){
            joinBtn.setVisibility(View.GONE);
            rejectBtn.setVisibility(View.GONE);
            exitEventBtn.setVisibility(View.VISIBLE);
        }else{
            joinBtn.setVisibility(View.VISIBLE);
            rejectBtn.setVisibility(View.VISIBLE);
            exitEventBtn.setVisibility(View.GONE);
        }
    }

    private void restart(){
        recreate();
        overridePendingTransition(0, 0);
    }

    private String parseDate(String date) {

        return date.replaceAll("T"," ").replaceAll("-",".");
    }

    private String parseTime(String date) {

        return date.substring(11,16);
    }

}
