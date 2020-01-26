package com.project.mapchat.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.entities.EventFromServer;

import java.util.ArrayList;


public class ChatEventsRecyclerAdapter extends RecyclerView.Adapter<ChatEventsRecyclerAdapter.ViewHolder>{

    private ArrayList<EventFromServer> eventsData;

    public ChatEventsRecyclerAdapter(ArrayList<EventFromServer> eventsData) {
        this.eventsData = eventsData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
