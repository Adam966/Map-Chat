package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AddFriend {
    @Headers("Content-Type: application/json")
    @GET("api/user/addFriend")
    Call<ResponseBody> addFriendRequest(
            @Header("Authorization") String serverToken,
            @Query("idF") int friendId
    );
}
