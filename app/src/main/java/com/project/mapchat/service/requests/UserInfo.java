package com.project.mapchat.service.requests;

import com.project.mapchat.entities.UserInfoData;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;

public interface UserInfo {

    @Headers({ "Content-Type: application/json;charset=UTF-8",
            "Content-Encoding: gzip"})
    @GET("api/user/userinfo")
    Call<UserInfoData> userInfoRequest(@Header("Authorization") String serverToken);

}
