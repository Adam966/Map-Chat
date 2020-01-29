package com.project.mapchat.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.microsoft.signalr.HubConnection;
import com.project.mapchat.R;
import com.project.mapchat.adapters.ChatAdapter;

public class ChatActivity extends AppCompatActivity {

    private EditText writeMessage;
    private ImageButton sendMessageBtn;
    private ListView messageView;

    private ChatAdapter adapter;

    HubConnection mSocket = SignalR.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // initializing views in activity_chat.xml
        writeMessage = findViewById(R.id.writeMessage);
        sendMessageBtn = findViewById(R.id.sendBtn);
        messageView = findViewById(R.id.messages_view);

        MessageGroup messageG = new MessageGroup();
        messageG.setIdEG(16);

        adapter = new ChatAdapter(this);
        messageView.setAdapter(adapter);

        mSocket.on("groupConnection", (message) -> { //you get essage if you successfully connected to the group or not
            //if the client disconnects always you need to reconnect to every group
            Log.d("MESSAGE WE GOT", message.idG + " " + message.isConnected());

        }, GroupConnection.class);

        mSocket.on("ReceiveMessageGroup", (message) -> {
            Log.d("MESSAGE WE GOT", message.getMessageText() + " " + message.getcTime());

            ChatActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //textView.setText(message.getMessageText());
                    adapter.add(message);
                }
            });
        }, MessageGroup.class);

        sendMessageBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                attemptSend2(messageG);
                Log.d("SENDNING0", "???????????????,");
            }
        });
    }

    private void attemptSend2(MessageGroup messageG) {
        try {
            String message = writeMessage.getText().toString().trim();

            messageG.setMessageText(message);
            Log.d("STATE", mSocket.getConnectionState().toString());

            Gson gson = new Gson();
            String jsonMessage = gson.toJson(messageG);

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
}
