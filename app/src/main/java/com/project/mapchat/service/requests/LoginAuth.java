package com.project.mapchat.service.requests;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginAuth {

    @Headers("Content-Type: application/json")
    @POST("api/auth/login")
    Call<ResponseBody> loginAuthRequest(@Body RequestBody fbReqToken);

}
