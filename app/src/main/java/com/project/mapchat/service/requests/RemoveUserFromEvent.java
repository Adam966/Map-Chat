package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoveUserFromEvent {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/removeUserFromEvent")
    Call<ResponseBody> removeUserFromEventRequest(
            @Header("Authorization") String serverToken,
            @Query("idUR") int userId,
            @Query("idE") int eventId
    );
}
