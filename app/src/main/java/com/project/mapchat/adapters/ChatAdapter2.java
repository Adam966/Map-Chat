package com.project.mapchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.chat.MessageGroup;

import java.util.ArrayList;

public class ChatAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessageGroup> list;

    public ChatAdapter2(ArrayList<MessageGroup> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message, parent, false);

        return new VieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VieHolder) holder).message.setText(list.get(position).getMessageText());
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isBelongToUser())
            return 1;
        else
            return 2;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class VieHolder extends RecyclerView.ViewHolder {
        TextView message;

        public VieHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_body);
        }
    }
}
