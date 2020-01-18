package com.project.mapchat.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.service.ServerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private Button logoutBtn;
    private CircleImageView circleImageView;
    private SharedPrefs appSharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPrefs = new SharedPrefs(this);

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Switch switchDarkMode = findViewById(R.id.switchDark);
        if(appSharedPrefs.loadDarkModeState() == true){
            switchDarkMode.setChecked(true);
        }
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    appSharedPrefs.setDarkModeState(true);
                    restart();
                }else{
                    appSharedPrefs.setDarkModeState(false);
                    restart();
                }
            }
        });

        userInfoRequest(appSharedPrefs.getServerToken());

        logoutBtn = findViewById(R.id.btn_logout);
        circleImageView = findViewById(R.id.profile_image);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Logout().logout(appSharedPrefs,getApplicationContext());
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        //Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentHome = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_groups:
                        //Toast.makeText(MainActivity.this, "Group Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentGroups = new Intent(SettingsActivity.this, EventsActivity.class);
                        startActivity(intentGroups);
                        break;
                    case R.id.action_settings:
                        //Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentSettings = new Intent(SettingsActivity.this,SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                }
                return true;
            }
        });
    }

    private void setImage(String id){
        String imageUrl = "https://graph.facebook.com/"+id+"/picture?type=normal";
        Glide.with(SettingsActivity.this).load(imageUrl).into(circleImageView);
    }

    private void restart(){
        Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(i);
    }

    private void userInfoRequest(String serverToken){

        HashMap<String, String> params = new HashMap<>();
        // putting token to parameters of request
        params.put("Authorization","Bearer"+" "+serverToken);

        // making Request Body through Gson Library
        String strRequestBody = new Gson().toJson(params);
        Log.wtf("userInfo",strRequestBody);

        final RequestBody requestBody = RequestBody.create(MediaType.
                parse("application/json"),strRequestBody);

        Log.wtf("requestBody",requestBody.toString());

        Call<ResponseBody> call = ServerService
                .getInstance()
                .getUserInfoReq()
                .userInfoRequest(serverToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.wtf("ResponseCode",String.valueOf(response.code()));
                if(response.isSuccessful()){
                    Log.wtf("UserInfo","SUCCESS");
                }else{
                    switch(response.code()){
                        case 401:{
                            //new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });
    }


}
