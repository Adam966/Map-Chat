package com.project.mapchat.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.chat.MessageGroup;
import com.project.mapchat.entities.UserInfoData;

import java.util.ArrayList;

public class ChatAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessageGroup> list;
    private int id;
    private ArrayList<UserInfoData> users;

    public ChatAdapter2(ArrayList<MessageGroup> list, int id, ArrayList<UserInfoData> users) {
        this.id = id;
        this.list = list;
        this.users = users;
    }

    @NonNull
    @Override
    public VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);

        return new VieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VieHolder) holder).message.setText(list.get(position).getMessageText());

        for (UserInfoData u: users) {
            Log.wtf("USER ID", list.get(position).getIdU() + " " + u.getId());
            if (list.get(position).getIdU() == u.getId() && u.getId() != id) {
                Log.wtf("USER NAME", (u.getFirstName() + " " + u.getLastName()));
                ((VieHolder) holder).name.setText((u.getFirstName() + " " + u.getLastName()));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.wtf("IF", String.valueOf(list.get(position).getIdU())+ " " + id);
        Log.wtf("state", String.valueOf(list.get(position).getIdU() == id));
        if(list.get(position).getIdU() != id)
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
        TextView name;

        public VieHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_body);
            name = itemView.findViewById(R.id.message_name_holder);
        }
    }
}
