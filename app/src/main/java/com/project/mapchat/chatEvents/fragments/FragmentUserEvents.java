package com.project.mapchat.chatEvents.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.share.Share;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.adapters.ChatUsersEventsRecyclerAdapter;
import com.project.mapchat.chatEvents.ChatEvents;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.Location;
import com.project.mapchat.main.activities.Logout;
import com.project.mapchat.main.activities.MainActivity;
import com.project.mapchat.service.ServerService;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserEvents extends Fragment {

    private View rootView;
    private SharedPrefs appSharedPrefs;
    private RecyclerView myRecycle;
    private ChatUsersEventsRecyclerAdapter adapter;
    private ArrayList<EventFromServer> events =   new ArrayList<>();
    public FragmentUserEvents(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_events_fragment,container,false);
        appSharedPrefs = new SharedPrefs(getContext());
        myRecycle = rootView.findViewById(R.id.userEventsFragmentRecycler);
        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycle.setAdapter(adapter);
       // getUserEvents(appSharedPrefs.getServerToken());
        getUserJoinedEvents(appSharedPrefs.getServerToken());

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getUserEvents(String serverToken) {
        Call<ArrayList<EventFromServer>> call = ServerService
                .getInstance()
                .getUserEvents()
                .getUserEventsRequest("Bearer"+" "+serverToken);

        call.enqueue(new Callback<ArrayList<EventFromServer>>() {
            @Override
            public void onResponse(Call<ArrayList<EventFromServer>> call, Response<ArrayList<EventFromServer>> response) {
                if(response.isSuccessful()){
                    events.addAll(response.body());
                    Intent i = new Intent();

                }else{
                    switch(response.code()){
                        case 401:{
                            new Logout().logout(appSharedPrefs,getContext());
                        }
                        case 500:{
                            Toast.makeText(getContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EventFromServer>> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserJoinedEvents(String serverToken) {
        Call<ArrayList<EventFromServer>> call = ServerService
                .getInstance()
                .getUserJoinedEvents()
                .getUserJoinedEventsRequest("Bearer"+" "+serverToken);

        call.enqueue(new Callback<ArrayList<EventFromServer>>() {
            @Override
            public void onResponse(Call<ArrayList<EventFromServer>> call, Response<ArrayList<EventFromServer>> response) {
                Log.wtf("DATA: ", response.body().toString());
                /*
                events.removeAll(response.body());
                events.addAll(response.body());
                 */
                adapter = new ChatUsersEventsRecyclerAdapter(response.body());
                adapter.notifyDataSetChanged();
                myRecycle.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<EventFromServer>> call, Throwable t) {

            }
        });
    }
}
