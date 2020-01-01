package com.project.mapchat;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    SharedPreferences appSharedPrefs;

    protected SharedPrefs(Context context){
        appSharedPrefs = context.getSharedPreferences("appUser",Context.MODE_PRIVATE);
    }

    protected void setDarkModeState(Boolean state){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putBoolean("darkmode",state);
        editor.commit();
    }

    protected Boolean loadDarkModeState(){
        Boolean state = appSharedPrefs.getBoolean("darkmode",false);
        return state;
    }

    protected void setId(String id){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putString("id", id);
        editor.apply();
    }

    protected String getId(){
        String id = appSharedPrefs.getString("id",null);
        return id;
    }

    protected void setToken(){

    }

    protected void getToken(){

    }

    protected void setOnboardingState(boolean state){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putBoolean("state",state);
        editor.apply();
    }
}
