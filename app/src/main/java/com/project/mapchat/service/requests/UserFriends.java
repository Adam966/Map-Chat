package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserFriends {

    @Headers("Content-Type:application/json")
    @POST("/api/user/friends")
    Call<ResponseBody> userFriendRequest(@Header("Authorization") String serverToken);

}
