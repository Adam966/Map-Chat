package com.project.mapchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.project.mapchat.adapters.FriendAdapter;
import com.project.mapchat.entities.Friend;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChooseFriends extends AppCompatActivity implements FriendAdapter.OnClickListener {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_picker_layout);

        searchView = findViewById(R.id.searchViewFriends);
        recyclerView = findViewById(R.id.friendsList);

        /*
        serverService = new ServerService();
        serverService.createRetrofit();
        serverService.getService().getFriends(1).enqueue(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                setAdapter(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                Log.i("ERROR", t.getMessage());
            }
        });

        */
    }

    @Override
    public void itemClicked(Friend friend) {
        Log.i("CLICKED ITEM", friend.getNickName());
    }

    private void setAdapter(ArrayList<Friend> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        FriendAdapter friendAdapter = new FriendAdapter(list, this);
        recyclerView.setAdapter(friendAdapter);
    }
}
