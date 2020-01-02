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

    private SharedPrefs appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        appSharedPrefs = new SharedPrefs(this);

        Button btnRedirect = findViewById(R.id.btnOnboarding);

        if(checkFirstTime()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            btnRedirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(OnboardingActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private boolean checkFirstTime(){
        if(appSharedPrefs.getOnboardingState()){
            return true;
        } else {
            appSharedPrefs.setOnboardingState(true);
            return false;
        }
    }
}
