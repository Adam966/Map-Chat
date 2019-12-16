package com.project.mapchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.project.mapchat.dialogs.DatePickerFragment;
import com.project.mapchat.dialogs.TimePickerFragment;

import java.util.Date;

public class EventAddActivity extends AppCompatActivity implements DatePickerFragment.DialogListener, TimePickerFragment.DialogListener {
    private EditText eventName;
    private SearchView locationSearch;
    private Switch eventVisibility;
    private Spinner friendList;
    private TextView date;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

        eventName =  findViewById(R.id.eventName);
        locationSearch = findViewById(R.id.searchViewLocation);
        eventVisibility = findViewById(R.id.eventVisibility);
        friendList = findViewById(R.id.friendListSpinner);
        date = findViewById(R.id.eventDate);
        time = findViewById(R.id.eventTime);


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
    }

    ////////////////////////////////////// ADD EVENT FUNCTIONS /////////////////////////////////////
    public void addEvent(View view) {

    }

    public void cancel(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void getDate(Date date) {
        this.date.setText(date.toString().substring(0, 10));
    }

    @Override
    public void getTime(Date date) {
        time.setText(date.toString().substring(10, 19));
    }
}
