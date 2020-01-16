package com.project.mapchat.service.requests;

import com.project.mapchat.entities.Event;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface GetAllEvents {
    @Headers("Content-Type: application/json")
    @GET("api/auth/login")
    Call<ArrayList<Event>> allEvents();
}
