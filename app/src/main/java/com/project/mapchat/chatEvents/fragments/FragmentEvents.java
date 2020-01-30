package com.project.mapchat.chatEvents.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.adapters.ChatEventsRecyclerAdapter;
import com.project.mapchat.adapters.ChatUsersEventsRecyclerAdapter;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.main.activities.Logout;
import com.project.mapchat.service.ServerService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEvents extends Fragment {

    private View rootView;
    private SharedPrefs appSharedPrefs;
    private RecyclerView myRecycle;
    private ChatEventsRecyclerAdapter adapter;

    public FragmentEvents(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.events_fragment,container,false);
        appSharedPrefs = new SharedPrefs(getContext());
        // setup recycle view
        myRecycle = rootView.findViewById(R.id.eventsFragmentRecycler);
        myRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycle.setAdapter(adapter);
        getEvents(appSharedPrefs.getServerToken());

        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getEvents(String serverToken) {
        Call<ArrayList<EventFromServer>> call = ServerService
                .getInstance()
                .getEvents()
                .getEventsRequest("Bearer"+" "+serverToken);

        call.enqueue(new Callback<ArrayList<EventFromServer>>() {
            @Override
            public void onResponse(Call<ArrayList<EventFromServer>> call, Response<ArrayList<EventFromServer>> response) {
                if(response.isSuccessful()){
                    adapter = new ChatEventsRecyclerAdapter(response.body());
                    adapter.notifyDataSetChanged();
                    Log.wtf("FragmentEvents",response.body().toString());
                    myRecycle.setAdapter(adapter);
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

}
