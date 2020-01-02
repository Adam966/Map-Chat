package com.project.mapchat.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerService {
    private Service service;

    public void createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.15:8080/")
                .build();


        service = retrofit.create(Service.class);

    }

    public Service getService() {
        return service;
    }
}
