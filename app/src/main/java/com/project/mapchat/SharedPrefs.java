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

    public void setServerToken(String token){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putString("serverToken",token);
        editor.apply();
    }

    public String getServerToken(){
        String token = appSharedPrefs.getString("serverToken",null);
        return token;
    }

    public void setFbToken(String token){
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putString("fbToken",token);
        editor.apply();
    }

    public String getFbToken(){
        String token = appSharedPrefs.getString("fbToken",null);
        return token;
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
