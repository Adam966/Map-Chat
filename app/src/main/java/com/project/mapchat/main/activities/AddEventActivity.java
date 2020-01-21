package com.project.mapchat.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.project.mapchat.ChoosePlace;
import com.project.mapchat.R;
import com.project.mapchat.dialogs.DatePickerFragment;
import com.project.mapchat.dialogs.TimePickerFragment;
import com.project.mapchat.entities.Event;
import com.project.mapchat.entities.Location;
import com.project.mapchat.entities.Place;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements DatePickerFragment.DialogListener, TimePickerFragment.DialogListener {
    /////////////////////////////// EVENT OBJ ////////////////////
    private Event event;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

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
            Log.wtf("EVENT", event.toString());
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
        event = new Event();
        event.setGroupName(eventName.getText().toString());
        event.setDescription(description.getText().toString());
        event.setLocation(location);
        //event.setTags();
        event.setType(visibility);
        event.setMeetTime(date.getText().toString() + "" + time.getText().toString());
        //request to server
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
        startActivity(new Intent(this, ChoosePlace.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Place place = (Place) getIntent().getBundleExtra("place").getSerializable("place");

        Log.wtf("PLACE", place.toString());

        location = new Location();
        location.setAddress(place.getRoad() + " " + place.getHouseNumber());
        location.setCountry(place.getCountry());
        location.setPostalCode(place.getPostcode());
        location.setTown(place.getTown());
        location.setLatitude(String.valueOf(place.getLat()));
        location.setLongtitude(String.valueOf(place.getLng()));

        Log.wtf("Location", location.toString());

        if (intent.getStringExtra("placeName") != null)
            placeName.setText(place.getFormatted());
        else
            placeName.setText("Choose place");
    }
}
