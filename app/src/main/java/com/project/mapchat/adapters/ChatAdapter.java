package com.project.mapchat.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.mapchat.R;
import com.project.mapchat.chat.MessageGroup;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    List<MessageGroup> messages = new ArrayList<>();
    Context context;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void add(MessageGroup message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        MessageGroup messageGroup = messages.get(position);
        Log.wtf("GET VIEW", "NOW");
        if(messageGroup.isBelongToUser()){
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody =  convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(messageGroup.getMessageText());
        }else {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.name = convertView.findViewById(R.id.name);
            holder.messageBody = convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            //holder.name.setText(messageGroup.getRecieverName());
            holder.messageBody.setText(messageGroup.getMessageText());
        }
        return convertView;
    }
}

class MessageViewHolder {
    public TextView name;
    public TextView messageBody;
}
