package com.project.mapchat.service.requests;

import com.project.mapchat.entities.Event;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GetEventTags {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/getEventTags?idE'={id}")
    Call<ResponseBody> getEventTags(
            @Header("Authorization") String serverToken,
            @Path("id") int eventId
    );
}
