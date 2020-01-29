package com.project.mapchat.service.requests;

import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.UserEvent;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface GetUserJoinedEvents {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/getUserJoinedEvents")
    Call<ArrayList<UserEvent>> getUserJoinedEventsRequest(@Header("Authorization") String serverToken);
}
