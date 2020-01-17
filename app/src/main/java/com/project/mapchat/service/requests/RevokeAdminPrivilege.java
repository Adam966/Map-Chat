package com.project.mapchat.service.requests;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface RevokeAdminPrivilege {
    @Headers("Content-Type: application/json")
    @GET("api/mapchat/revokeAdmin?idUR={idU}&idE={idE}")
    Call<ResponseBody> addAdminPrivilegeRequest(
            @Header("Authorization") String serverToken,
            @Path("idU") int userId,
            @Path("idE") int eventId
    );
}
