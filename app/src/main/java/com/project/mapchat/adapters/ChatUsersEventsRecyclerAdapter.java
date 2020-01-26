package com.project.mapchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.entities.EventFromServer;

import java.util.ArrayList;

public class ChatUsersEventsRecyclerAdapter extends RecyclerView.Adapter<ChatUsersEventsRecyclerAdapter.ViewHolder> {

    private ArrayList<EventFromServer> userEventsData;

    public ChatUsersEventsRecyclerAdapter(ArrayList<EventFromServer> eventsData) {
        this.userEventsData = eventsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_event_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String userEventName = userEventsData.get(position).getGroupName();

        holder.chatUserEventName.setText(userEventName);
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
