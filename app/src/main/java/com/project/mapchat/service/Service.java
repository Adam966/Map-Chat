package com.project.mapchat.service;

import com.project.mapchat.entities.Friend;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface Service {
    @GET("friends/{id}")
    Call<ArrayList<Friend>> getFriends(@Path("id") int userId);
}
