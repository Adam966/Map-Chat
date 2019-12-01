package com.project.mapchat;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    SharedPreferences modSharedPrefs;

    protected SharedPrefs(Context context){
        modSharedPrefs = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    protected void setDarkModeState(Boolean state){
        SharedPreferences.Editor editor = modSharedPrefs.edit();
        editor.putBoolean("darkmode",state);
        editor.commit();
    }

    protected Boolean loadDarkModeState(){
        Boolean state = modSharedPrefs.getBoolean("darkmode",false);
        return state;
    }
}
