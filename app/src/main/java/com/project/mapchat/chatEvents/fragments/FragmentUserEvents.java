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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.share.Share;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.adapters.ChatUsersEventsRecyclerAdapter;
import com.project.mapchat.chatEvents.ChatEvents;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.main.activities.Logout;
import com.project.mapchat.main.activities.MainActivity;
import com.project.mapchat.service.ServerService;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserEvents extends Fragment {

    View rootView;
    private ArrayList<EventFromServer> userEventsList;
    private SharedPrefs appSharedPrefs;

    public FragmentUserEvents(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_events_fragment,container,false);

        RecyclerView recyclerView = rootView.findViewById(R.id.userEventsFragmentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ChatUsersEventsRecyclerAdapter adapter = new ChatUsersEventsRecyclerAdapter
        (
            getEventsList()
        );

        // 4. set adapter
        recyclerView.setAdapter(adapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }



    private ArrayList<EventFromServer> getEventsList(){
        ChatEvents activity = (ChatEvents) getActivity();
        return activity.getEventsList();
    }

    private ArrayList<EventFromServer> getUserEventsList(){
        ChatEvents activity = (ChatEvents) getActivity();
        return activity.getUserEventsList();
    }



}
