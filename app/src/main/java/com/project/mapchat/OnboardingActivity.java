package com.project.mapchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

       Button btnRedirect = findViewById(R.id.btnOnboarding);
        btnRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intentMain = new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(intentMain);*/
                Intent i=new Intent(OnboardingActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
