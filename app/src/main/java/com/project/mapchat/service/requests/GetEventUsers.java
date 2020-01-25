package com.project.mapchat.service.requests;

import com.project.mapchat.entities.UserInfoData;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetEventUsers {
        @Headers("Content-Type: application/json")
        @GET("api/mapchat/getEventUsers")
        Call<ArrayList<UserInfoData>> getEventUsers(
                @Header("Authorization") String serverToken,
                @Query("idE") int eventId
        );
}
