package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RemoveUserFromEvent {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/removeUserFromEvent?idUR={idU}&idE={idE}")
    Call<ResponseBody> leaveEventRequest(
            @Header("Authorization") String serverToken,
            @Path("idU") int userId,
            @Path("idE") int eventId
    );
}
