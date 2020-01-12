package com.project.mapchat;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    SharedPreferences appSharedPrefs;

    public SharedPrefs(Context context){
        appSharedPrefs = context.getSharedPreferences("appUser",Context.MODE_PRIVATE);
    }

    public void setDarkModeState(Boolean state){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putBoolean("darkmode",state);
        editor.commit();
    }

    public Boolean loadDarkModeState(){
        Boolean state = appSharedPrefs.getBoolean("darkmode",false);
        return state;
    }

    public void setId(String id){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putString("id", id);
        editor.apply();
    }

    public String getId(){
        String id = appSharedPrefs.getString("id",null);
        return id;
    }

    public void setToken(){

    }

    public void getToken(){

    }

    public void setOnboardingState(boolean state){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putBoolean("tutorialViewed",state);
        editor.apply();
    }

    public Boolean getOnboardingState(){
        Boolean state = appSharedPrefs.getBoolean("tutorialViewed",false);
        return state;
    }
}
