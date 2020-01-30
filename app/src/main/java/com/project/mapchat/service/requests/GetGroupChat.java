package com.project.mapchat.service.requests;

import com.project.mapchat.chat.MessageGroup;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetGroupChat {
    @Headers("Content-Type: application/json")
    @GET("api/user/getGroupChat")
    Call<ArrayList<MessageGroup>> getGroupChat(
            @Header("Authorization") String serverToken,
            @Query("idE") int eventId
    );
}
