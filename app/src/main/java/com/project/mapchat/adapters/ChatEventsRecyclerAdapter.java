package com.project.mapchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.main.activities.MainActivity;

import java.util.ArrayList;


public class ChatEventsRecyclerAdapter extends RecyclerView.Adapter<ChatEventsRecyclerAdapter.ViewHolder>{

    private ArrayList<EventFromServer> eventsData;

    public ChatEventsRecyclerAdapter(ArrayList<EventFromServer> eventsData) {
        this.eventsData = eventsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String eventName = eventsData.get(position).getGroupName();
        final String Lat = eventsData.get(position).getLocation().getLatitude();
        final String Lon = eventsData.get(position).getLocation().getLongtitude();

        holder.eventName.setText(eventName);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("ONCLICK","Works");
                Intent i = new Intent(view.getContext(), MainActivity.class);
                i.putExtra("eventName",eventName);
                i.putExtra("Lat", Double.valueOf(Lat));
                i.putExtra("Lon", Double.valueOf(Lon));
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (eventsData == null) ? 0 : eventsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventItemName);
            parentLayout = itemView.findViewById(R.id.eventItemLayout);
        }
    }
}
