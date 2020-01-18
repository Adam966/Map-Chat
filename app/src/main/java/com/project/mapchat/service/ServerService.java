package com.project.mapchat.service;

import com.project.mapchat.service.requests.AddAdminPrivilege;
import com.project.mapchat.service.requests.CreateEvent;
import com.project.mapchat.service.requests.DeleteEvent;
import com.project.mapchat.service.requests.GetEventTags;
import com.project.mapchat.service.requests.GetEventUsers;
import com.project.mapchat.service.requests.GetEvents;
import com.project.mapchat.service.requests.GetUserEvents;
import com.project.mapchat.service.requests.LeaveEvent;
import com.project.mapchat.service.requests.LoginAuth;
import com.project.mapchat.service.requests.RemoveUserFromEvent;
import com.project.mapchat.service.requests.RevokeAdminPrivilege;
import com.project.mapchat.service.requests.UpdateUserData;
import com.project.mapchat.service.requests.UserFriends;
import com.project.mapchat.service.requests.UserInfo;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerService {
    // change ip address when connected to another wifi or network
    private final String BASE_URL = "http://192.168.1.15:5000/";
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


        // USER EVENTS
        public LoginAuth getloginAuthReq(){
            return retrofit.create(LoginAuth.class);
        }

        public UpdateUserData getupdateUserDataReq(){
            return retrofit.create(UpdateUserData.class);
        }

        public UserFriends getUserFriendsReq(){
            return retrofit.create(UserFriends.class);
        }

        public UserInfo getUserInfoReq(){
            return retrofit.create(UserInfo.class);
        }


        // EVENT REQUESTS
        public CreateEvent createEvent(){
            return retrofit.create(CreateEvent.class);
        }

        public DeleteEvent deleteEvent(){
            return retrofit.create(DeleteEvent.class);
        }

        public LeaveEvent leaveEvent(){
            return retrofit.create(LeaveEvent.class);
        }


        // EVENTS REQUESTS
        public GetEvents getEvents(){
            return retrofit.create(GetEvents.class);
        }

        public GetUserEvents getUserEvents() {
            return retrofit.create(GetUserEvents.class);
        }

        public GetEventTags getEventTags(){
            return retrofit.create(GetEventTags.class);
        }

        public GetEventUsers getEventUsers(){
            return retrofit.create(GetEventUsers.class);
        }


        // ADMIN REQUEST
        public AddAdminPrivilege addAdminPrivilege(){
            return retrofit.create(AddAdminPrivilege.class);
        }

        public RevokeAdminPrivilege revokeAdminPrivilege(){
            return retrofit.create(RevokeAdminPrivilege.class);
        }

        public RemoveUserFromEvent removeUserFromEvent(){
            return retrofit.create(RemoveUserFromEvent.class);
        }

}
