package com.project.mapchat.chat;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class ApplicationOwner extends Application implements LifecycleObserver
{

    //private String tokken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiIyIiwidW5pcXVlX25hbWUiOiIyNjYyOTA4OTg3MDg1Nzc3IiwiZ2l2ZW5fbmFtZSI6IlRvbcOhxaEiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3dpbmRvd3N1c2VyY2xhaW0iOiJtYXBfY2hhdCIsIm5iZiI6MTU3OTY5NjIwOCwiZXhwIjoxNTg0OTY2NjA4LCJpYXQiOjE1Nzk2OTYyMDh9.1FyWh33Adkt3FUrtzng9Zg1hM7bNlu3O_qbWOLyJ80EAT-DPa1wh2c46KTS27i-I_mvSzAJTJhZlK3B2XIfCQA";
    @Override
    public void onCreate()
    {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume()
    {
        SignalR.getInstance();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground()
    {
        SignalR.disconnectScoket();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause()
    {
        SignalR.disconnectScoket();
    }


}