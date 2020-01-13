package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateUserData {

    @Headers("Content-Type:application/json")
    @GET("/api/user/updateUserData")
    Call<ResponseBody> updateUserDataRequest(@Header("Authorization") String serverToken, @Header("fToken") String facebookToken);
}
