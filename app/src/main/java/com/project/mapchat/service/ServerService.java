package com.project.mapchat.service;

import com.project.mapchat.service.requests.GetAllEvents;
import com.project.mapchat.service.requests.LoginAuth;
import com.project.mapchat.service.requests.UpdateUserData;
import com.project.mapchat.service.requests.UserFriends;
import com.project.mapchat.service.requests.UserInfo;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerService {
    // change ip address when connected to another wifi or network
    private final String BASE_URL = "http://192.168.0.103:5000/";
    private static ServerService clientInstance;
    private Retrofit retrofit;

    public ServerService() {

        // for debugging request
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        // build retrofit client with url of the server
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }

        // getting instance of the retrofit client
        public static synchronized ServerService getInstance() {
            if (clientInstance == null) {
                clientInstance = new ServerService();
            }
            return clientInstance;
        }

        // getting requests
        public LoginAuth getloginAuthReq(){
            return retrofit.create(LoginAuth.class);
        }

        public UpdateUserData getupdateUserDataReq(){
            return retrofit.create(UpdateUserData.class);
        }

        public UserFriends getuserFriendsReq(){
            return retrofit.create(UserFriends.class);
        }

        public UserInfo getuserInfoReq(){
            return retrofit.create(UserInfo.class);
        }

        public GetAllEvents getAllEvents() {return retrofit.create(GetAllEvents.class);}

}
