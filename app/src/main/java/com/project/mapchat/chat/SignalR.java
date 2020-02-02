package com.project.mapchat.chat;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import io.reactivex.Single;

public class SignalR
{
    private static HubConnection hubConnection;

    //private static String tokken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJuYW1laWQiOiIyIiwidW5pcXVlX25hbWUiOiIyNjYyOTA4OTg3MDg1Nzc3IiwiZ2l2ZW5fbmFtZSI6IlRvbcOhxaEiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3dpbmRvd3N1c2VyY2xhaW0iOiJtYXBfY2hhdCIsIm5iZiI6MTU3OTY5NjIwOCwiZXhwIjoxNTg0OTY2NjA4LCJpYXQiOjE1Nzk2OTYyMDh9.1FyWh33Adkt3FUrtzng9Zg1hM7bNlu3O_qbWOLyJ80EAT-DPa1wh2c46KTS27i-I_mvSzAJTJhZlK3B2XIfCQA";
    private static String tokken = "";

    private static void initSocket()
    {

        hubConnection = HubConnectionBuilder.create("http://map-chat.azurewebsites.net/groupchat")
                .withAccessTokenProvider(Single.defer(() -> {
                    return Single.just(tokken);
                })).build();
    }

    public static void disconnectScoket()
    {
        hubConnection.stop();
    }

    public static HubConnection getInstance()
    {
        if (hubConnection != null)
        {
            if(hubConnection.getConnectionState().toString() == "CONNECTED")
            {return hubConnection;}
            else
            {
                hubConnection.start().blockingAwait();
                return hubConnection;
            }
        }
        else
        {
            initSocket();
            hubConnection.start().blockingAwait();
            return hubConnection;
        }

    }

    public static void getToken(String token) {
        tokken = token;
    }
}
