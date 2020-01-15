package com.project.mapchat.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.gson.Gson;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.main.activities.Login;
import com.project.mapchat.service.ServerService;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationOwner extends Application implements LifecycleObserver {
    private SharedPrefs appSharedPrefs;
    private String serverToken;
    private String fbToken;

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appSharedPrefs = new SharedPrefs(this);
        serverToken = appSharedPrefs.getServerToken();
        fbToken = appSharedPrefs.getFbToken();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume()
    {
        Log.d("TEST","ON_RESUME");
        /*if(serverToken != null && fbToken != null){
            updateUserData(serverToken,fbToken);
        }

         */
    }

    // request to update user data
    private void updateUserData(String serverToken,String facebookToken){

        HashMap<String, String> params = new HashMap<>();
        // putting token to parameters of request
        params.put("Authorization","Bearer"+" "+serverToken);
        params.put("fToken","Bearer"+" "+serverToken);

        // making Request Body through Gson Library
        String strRequestBody = new Gson().toJson(params);
        Log.wtf("updateUserData",strRequestBody);

        final RequestBody requestBody = RequestBody.create(MediaType.
                parse("application/json"),strRequestBody);

        Log.wtf("requestBody",requestBody.toString());

        Call<ResponseBody> call = ServerService
                .getInstance()
                .getupdateUserDataReq()
                .updateUserDataRequest(serverToken,facebookToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.wtf("ResponseCode",String.valueOf(response.code()));
                if(response.code() == 400){
                    Log.wtf("GLOBALSTATE",response.toString());
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }else{
                    Log.wtf("GLOBALSTATE",response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}


