package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LeaveEvent {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/leaveEvent")
    Call<ResponseBody> leaveEventRequest(
            @Header("Authorization") String serverToken,
            @Query("idE") int eventId
    );
}
