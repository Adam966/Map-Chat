package com.project.mapchat.service.requests;

import com.project.mapchat.entities.EventFromServer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetEventById {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/getEventByID")
    Call<EventFromServer> getEventsRequest(
            @Header("Authorization") String serverToken,
            @Query("idE") int eventId
    );
}
