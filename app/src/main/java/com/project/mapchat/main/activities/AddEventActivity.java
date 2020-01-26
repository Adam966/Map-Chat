package com.project.mapchat.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.project.mapchat.ChoosePlace;
import com.project.mapchat.ChoosePlaceMap;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.dialogs.DatePickerFragment;
import com.project.mapchat.dialogs.TimePickerFragment;
import com.project.mapchat.entities.EventToSend;
import com.project.mapchat.entities.Location;
import com.project.mapchat.entities.Place;
import com.project.mapchat.service.ServerService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends AppCompatActivity implements DatePickerFragment.DialogListener, TimePickerFragment.DialogListener {
    /////////////////////////////// EVENT OBJ ////////////////////
    private EventToSend eventToSend;
    private Location location;
    private int visibility;

    /////////////////////////////// EVENT UI///////////////////////
    private EditText eventName;
    private Switch eventVisibility;

    private TextView date;
    private TextView time;
    private TextView timeText;
    private TextView dateText;
    private EditText description;

    private TextView tags;

    private TextView placeName;
    private EditText tagName;

    private TextView error;

    private SharedPrefs appSharedPrefs;
    private Button addEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

        appSharedPrefs = new SharedPrefs(this);

        eventName =  findViewById(R.id.eventName);
        eventVisibility = findViewById(R.id.eventVisibility);
        date = findViewById(R.id.eventDate);
        time = findViewById(R.id.eventTime);
        timeText = findViewById(R.id.timeText);
        dateText = findViewById(R.id.dateText);
        tagName = findViewById(R.id.tagName);
        tags = findViewById(R.id.tags);
        description = findViewById(R.id.description);
        error = findViewById(R.id.error);
        placeName = findViewById(R.id.placeName);

        placeName.setSelected(true);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), "datepicker");
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(), "timepicker");
            }
        });

        addEventBtn = findViewById(R.id.addEvent);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEvent()) {
                    addNewEvent();
                    Log.wtf("EVENT", eventToSend.toString());
                    //startActivity(new Intent(this, MainActivity.class));
                } else {
                    error.setVisibility(View.VISIBLE);
                }
            }
        });

        eventVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                visibility = b ? 1 : 0;
            }
        });
    }

    ////////////////////////////////////// ADD EVENT FUNCTIONS /////////////////////////////////////
    public void addEvent(View view) {
        if(checkEvent()) {
            addNewEvent();
            Log.wtf("EVENT", eventToSend.toString());
            //startActivity(new Intent(this, MainActivity.class));
        } else {
            error.setVisibility(View.VISIBLE);
        }
    }

    public void cancel(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean checkEvent() {
        boolean isCorrect = false;

        if (eventName.getText().toString().length() > 3 )
            isCorrect = true;

        if (!(timeText.getText().toString().equals("") && dateText.getText().toString().equals("")))
            isCorrect = true;

        if (description.getText().toString().length() != 0)
            isCorrect = true;

        if (!placeName.getText().toString().equals(""))
            isCorrect = true;

        return isCorrect;
    }

    private void addNewEvent() {
        eventToSend = new EventToSend();
        eventToSend.setGroupName(eventName.getText().toString());
        eventToSend.setDescription(description.getText().toString());
        eventToSend.setLocation(location);

        ArrayList<String> list = new ArrayList<>();
        list.add(tagName.getText().toString());

        eventToSend.setTags(list);
        eventToSend.setTags(list);
        eventToSend.setType(visibility);
        eventToSend.setMeetTime(date.getText().toString() + "" + time.getText().toString());
        createEvent(eventToSend,"Bearer"+" "+appSharedPrefs.getServerToken());
    }

    ////////////////////////////////////// TIME AND DATE PICKER ////////////////////////////////////
    @Override
    public void getDate(Date date) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formattedDate = simpleDateFormat.format(date);
        this.date.setText(formattedDate);
    }

    @Override
    public void getTime(Date date) {
        time.setText(date.toString().substring(10, 19));
    }

    ///////////////////////////////////// CHOOSE PLACE /////////////////////////////////////////////
    public void choosePlace(View view) {
        //startActivity(new Intent(this, ChoosePlace.class));
        startActivity(new Intent(this, ChoosePlaceMap.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle data = intent.getExtras();
        Place place = data.getParcelable("place");

        Log.wtf("PLACE_PARCELABLE", place.toString());

        if (data.getParcelable("place") != null)
            placeName.setText(place.getFormatted());
        else
            placeName.setText("Choose place");

        location = new Location();
        location.setAddress(place.getRoad() + " " + place.getHouseNumber());
        location.setCountry(place.getCountry());
        location.setPostalCode(place.getPostcode());
        location.setTown(place.getTown());
        location.setLatitude(String.valueOf(place.getLat()));
        location.setLongtitude(String.valueOf(place.getLng()));

        Log.wtf("Location", location.toString());
    }

    private void createEvent(EventToSend eventToSend,String serverToken){
        Call<ResponseBody> call = ServerService
                .getInstance()
                .createEvent()
                .createEventRequest(serverToken,eventToSend);

        Log.wtf("EventToSend",eventToSend.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.wtf("CreateEvent","Event was Created "+response.code());
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
