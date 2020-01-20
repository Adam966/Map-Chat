package com.project.mapchat.main.activities;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.entities.EventFromServer;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private ArrayList<EventFromServer> eventsData;
    private ConstraintLayout inflater;

    public EventAdapter(ArrayList<EventFromServer> eventsData, Context context) {
        this.eventsData = eventsData;
        //this.inflater = ConstraintLayout.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final String eventName = eventsData.get(position).getGroupName();
       holder.eventName.setText(eventName);

       holder.parentLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                Log.wtf("ONCLICK","Works");
                Intent i = new Intent(view.getContext(),MainActivity.class);
                i.putExtra("eventName",eventName);
                i.putExtra("Lat", 50.56);
                i.putExtra("Lon", 80.54);
                view.getContext().startActivity(i);
           }
       });
    }

    @Override
    public int getItemCount() {
        return eventsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventItemName);
            parentLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

}
