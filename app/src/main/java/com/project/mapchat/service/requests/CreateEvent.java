package com.project.mapchat.service.requests;

import com.project.mapchat.entities.Event;
import com.project.mapchat.entities.Location;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface CreateEvent {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/getUserEvents")
    Call<ResponseBody> createEventRequest
            (@Header("Authorization") String serverToken,
             @Body HashMap eventToSend,
             Location location
            );
}
