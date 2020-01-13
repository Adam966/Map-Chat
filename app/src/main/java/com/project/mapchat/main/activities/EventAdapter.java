package com.project.mapchat.main.activities;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private List<String> eventsData;
    private LayoutInflater inflater;

    public EventAdapter(List<String> eventsData, Context context) {
        this.eventsData = eventsData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final String eventName = eventsData.get(position);
       holder.eventName.setText(eventName);

       holder.parentLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent i = new Intent(v.getContext(),MainActivity.class);
                i.putExtra("eventName",eventName);
           }
       });

    }

    @Override
    public int getItemCount() {
        return eventsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventName;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventItemName);
            parentLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

}
