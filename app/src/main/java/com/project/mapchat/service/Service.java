package com.project.mapchat.service;

import com.project.mapchat.entities.Event;
import com.project.mapchat.entities.Friend;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Service {
    @GET("getFriends/{id}")
    Call<ArrayList<Friend>> getFriends(@Path("id") int userId);

    @GET("getAllEvents")
    Call<ArrayList<Event>> getAllEvents();

}
