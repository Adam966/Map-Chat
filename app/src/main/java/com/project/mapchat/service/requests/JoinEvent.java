package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JoinEvent {
    @Headers("Content-Type: application/json")
    @GET("/api/mapchat/joinEvent")
    Call<ResponseBody> joinEventRequest(
            @Header("Authorization") String serverToken,
            @Query("idE") int eventId
    );
}
