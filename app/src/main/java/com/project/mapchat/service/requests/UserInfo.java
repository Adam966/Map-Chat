package com.project.mapchat.service.requests;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface UserInfo {

    @Headers("Content-Type:application/json")
    @GET("api/user/userinfo")
    Call<ResponseBody> userInfoRequest(@Header("Authorization") String serverToken, @Body RequestBody body);

}
