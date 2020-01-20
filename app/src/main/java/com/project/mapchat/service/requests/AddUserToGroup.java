package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AddUserToGroup {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/addUserToGroup?idUTA={userId}&idE={id}  ")
    Call<ResponseBody> addUserToGroupRequest(
            @Header("Authorization") String serverToken,
            @Path("userId") int userId,
            @Path("id") int eventId
    );
}