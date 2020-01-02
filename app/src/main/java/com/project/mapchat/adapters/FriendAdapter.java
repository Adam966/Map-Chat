package com.project.mapchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.entities.Friend;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    private ArrayList<Friend> list;
    private OnClickListener listener;

    public FriendAdapter(ArrayList<Friend> list, OnClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_adapter_layout, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.friendName.setText(list.get(position).getNickName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView friendName;
        private OnClickListener listener;

        public ViewHolder(@NonNull View itemView, OnClickListener listener) {
            super(itemView);

            friendName = itemView.findViewById(R.id.friendNameAdapter);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.itemClicked(list.get(getAdapterPosition()));
        }
    }

    public interface OnClickListener {
        void itemClicked(Friend friend);
    }
}
