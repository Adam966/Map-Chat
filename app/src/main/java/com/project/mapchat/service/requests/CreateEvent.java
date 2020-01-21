package com.project.mapchat.service.requests;

import com.project.mapchat.entities.EventToSend;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface CreateEvent {
    @Headers("Content-Type: application/json")
    @HTTP(method = "GET", path = "api/mapchat/createEvent", hasBody = true)
    //@GET("api/mapchat/createEvent")
    Call<ResponseBody> createEventRequest(@Header("Authorization") String serverToken, @Body EventToSend eventToSend);
}
