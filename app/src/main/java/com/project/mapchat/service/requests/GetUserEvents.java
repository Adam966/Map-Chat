package com.project.mapchat.service.requests;

import com.project.mapchat.entities.EventToSend;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;


public interface GetUserEvents {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/getUserEvents")
    Call<ArrayList<EventToSend>> getUserEventsRequest(@Header("Authorization") String serverToken);
}
