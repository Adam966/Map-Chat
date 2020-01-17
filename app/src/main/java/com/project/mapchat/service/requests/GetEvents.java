package com.project.mapchat.service.requests;

import com.project.mapchat.entities.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface GetEvents {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/getEvents")
    Call<ArrayList<Event>> getEventsRequest(@Header("Authorization") String serverToken);
}
