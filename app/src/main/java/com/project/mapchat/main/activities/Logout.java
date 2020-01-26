package com.project.mapchat.main.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.login.LoginManager;
import com.project.mapchat.SharedPrefs;

public class Logout {

    public void logout(SharedPrefs prefs,Context context){

        LoginManager.getInstance().logOut();
        prefs.removeServerToken();
        prefs.removeFbToken();
        Intent i = new Intent(context,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
