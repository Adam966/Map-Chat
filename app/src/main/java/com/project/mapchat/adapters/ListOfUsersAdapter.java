package com.project.mapchat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.project.mapchat.R;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.UserInfoData;
import com.project.mapchat.main.activities.ListOfUsers;
import com.project.mapchat.main.activities.SettingsActivity;

import java.util.ArrayList;

public class ListOfUsersAdapter extends RecyclerView.Adapter<ListOfUsersAdapter.ViewHolder> {
    private ArrayList<UserInfoData> userInfoData;
    private ConstraintLayout inflater;
    private Context context;

    public ListOfUsersAdapter(ArrayList<UserInfoData> userInfoData,Context context) {
        this.userInfoData = userInfoData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_info,parent,false);
        ViewHolder holder = new ViewHolder(view);
        //context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String username = userInfoData.get(position).getFirstName()+" "+userInfoData.get(position).getLastName();
        String id = userInfoData.get(position).getFacebookId();

        holder.userName.setText(username);
        setImage(id,holder.imageUser);
    }

    @Override
    public int getItemCount() {
        return userInfoData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout parentLayout;
        TextView userName;
        ImageView imageUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.userItemLayout);
            userName = itemView.findViewById(R.id.user_name);
            imageUser = itemView.findViewById(R.id.image_user);
        }
    }

    private void setImage(String id,ImageView image){
        String imageUrl = "https://graph.facebook.com/"+id+"/picture?type=normal";

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT);

        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
    }
}
