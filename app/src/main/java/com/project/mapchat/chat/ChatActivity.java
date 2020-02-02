package com.project.mapchat.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.signalr.HubConnection;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.adapters.ChatAdapter;
import com.project.mapchat.adapters.ChatAdapter2;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.UserInfoData;
import com.project.mapchat.main.activities.Logout;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private EditText writeMessage;
    private ImageButton sendMessageBtn;
    private RecyclerView messageView;

    private SharedPrefs appSharedPrefs;

    private ChatAdapter2 adapter;
    private String idU = "";

    HubConnection mSocket;
    private ArrayList<MessageGroup> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // initializing views in activity_chat.xml
        writeMessage = findViewById(R.id.writeMessage);
        sendMessageBtn = findViewById(R.id.sendBtn);
        messageView = findViewById(R.id.messages_view);

        appSharedPrefs = new SharedPrefs(this);
        userInfoRequest(appSharedPrefs.getServerToken());

        SignalR.getToken(appSharedPrefs.getServerToken());
        mSocket = SignalR.getInstance();

        Intent i = getIntent();
        String idE = i.getStringExtra("eventId");



        Log.wtf("ID USER", idU);
        // call request for history
        getGroupChat(appSharedPrefs.getServerToken(),Integer.valueOf(idE));

        MessageGroup messageG = new MessageGroup();

        // getting values from function getEvenData
        EventFromServer event = getEventData();

        // getting onclicked event id
        messageG.setIdEG(Integer.valueOf(event.getId()));

        adapter = new ChatAdapter2(list);
        messageView.setLayoutManager(new LinearLayoutManager(this));
        messageView.setAdapter(adapter);

        mSocket.send("AddToUGroup", messageG.getIdEG());

        mSocket.on("groupConnection", (message) -> { //you get essage if you successfully connected to the group or not
            //if the client disconnects always you need to reconnect to every group
            Log.wtf("MESSAGE WE GOT", message.idG + " " + message.isConnected());

        }, GroupConnection.class);

        mSocket.on("ReceiveMessageGroup", (message) -> {
            Log.wtf("MESSAGE WE GOT", message.getMessageText() + " " + message.getcTime());
            Log.wtf("MESSAGE", message.toString());

            ChatActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //textView.setText(message.getMessageText());

                    // changes
                    if (message.getIdU() != Integer.valueOf(idU)) {
                        message.setBelongToUser(true);
                        list.add(message);
                        adapter.notifyItemInserted(list.size() - 1);
                    }
                }
            });
        }, MessageGroup.class);

        sendMessageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // changes
                messageG.setBelongToUser(false);
                attemptSend2(messageG);
                list.add(messageG);
                adapter.notifyItemInserted(list.size() - 1);
                Log.d("SENDNING0", messageG.toString());
            }
        });
    }

    private void attemptSend2(MessageGroup messageG) {
        try {
            String message = writeMessage.getText().toString().trim();

            messageG.setMessageText(message);
            Log.d("STATE", mSocket.getConnectionState().toString());

            mSocket.send("SendMessageGroup",messageG);

        }
        catch (Exception e) {
            Log.d("EXC", e.toString());
        }
    }

    public void onBackPressed() {
        SignalR.disconnectScoket();
        super.onBackPressed();
    }

    private void getGroupChat(String serverToken,int idE) {
        Call<ArrayList<MessageGroup>> call = ServerService
                .getInstance()
                .getGroupChat()
                .getGroupChat("Bearer " + serverToken, idE);

        call.enqueue(new Callback<ArrayList<MessageGroup>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageGroup>> call, Response<ArrayList<MessageGroup>> response) {
                if(response.isSuccessful()){
                    Log.wtf("HISTORY", response.body().toString());
                    //list = response.body();
                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MessageGroup>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private Boolean compareId(int idU,int idU2){
        return idU == idU2;
    }

    private EventFromServer getEventData(){

        Intent i = getIntent();
        EventFromServer event = new EventFromServer();
        event.setGroupName(i.getStringExtra("groupName"));
        event.setId(i.getStringExtra("eventId"));
        event.setActive(i.getStringExtra("active"));

        return event;
    }

    private void userInfoRequest(String serverToken){
        Call<UserInfoData> call = ServerService
                .getInstance()
                .getUserInfoReq()
                .userInfoRequest("Bearer"+" "+serverToken);

        call.enqueue(new Callback<UserInfoData>() {
            @Override
            public void onResponse(Call<UserInfoData> call, Response<UserInfoData> response) {
                Log.wtf("ResponseCode",String.valueOf(response.code()));
                if(response.isSuccessful()){
                    Log.wtf("UserInfo","Success");
                    UserInfoData data = response.body();
                        idU = String.valueOf(data.getId());
                }else{
                    switch(response.code()){
                        case 401:{
                            //new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoData> call, Throwable t) {
                new Logout().logout(appSharedPrefs,getApplicationContext());
            }
        });
    }
}
