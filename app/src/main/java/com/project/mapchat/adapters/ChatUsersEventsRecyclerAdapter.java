package com.project.mapchat.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.chat.ChatActivity;
import com.project.mapchat.entities.UserInfoData;
import com.project.mapchat.main.activities.Logout;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUsersEventsRecyclerAdapter extends RecyclerView.Adapter<ChatUsersEventsRecyclerAdapter.ViewHolder> {

    private ArrayList<EventFromServer> userEventsData;

    public ChatUsersEventsRecyclerAdapter(ArrayList<EventFromServer> eventsData) {
        this.userEventsData = eventsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_event_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String userEventName = userEventsData.get(position).getGroupName();
        final String eventId = userEventsData.get(position).getId();
        final String groupName = userEventsData.get(position).getGroupName();
        final String active = userEventsData.get(position).getActive();

        holder.chatUserEventName.setText(userEventName);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ChatActivity.class);
                i.putExtra("eventId",eventId);
                i.putExtra("groupName",groupName);
                i.putExtra("active",active);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (userEventsData == null) ? 0 : userEventsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout parentLayout;
        TextView chatUserEventName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserEventName = itemView.findViewById(R.id.chatUserEventName);
            parentLayout = itemView.findViewById(R.id.chatUserEventItemLayout);
        }
    }
}
