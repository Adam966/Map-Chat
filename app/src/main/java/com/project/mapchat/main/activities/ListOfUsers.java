package com.project.mapchat.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.adapters.ListOfUsersAdapter;
import com.project.mapchat.entities.UserInfoData;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListOfUsers extends AppCompatActivity {

    private ArrayList<UserInfoData> usersFromEventList;
    private SharedPrefs appSharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPrefs = new SharedPrefs(this);

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);

        // getting user list of event from EventDetail activity
        usersFromEventList = parseUserListFromIntent();

        // added list to adapter of recycler view that showing users info
        setAdapter(usersFromEventList);

    }

    private ArrayList<UserInfoData> parseUserListFromIntent(){
        Gson gson = new Gson();
        String usersList = getIntent().getStringExtra("userList");
        Type type = new TypeToken<ArrayList<UserInfoData>>(){}.getType();
        usersFromEventList = gson.fromJson(usersList,type);
        //Log.wtf("USERLIST",usersFromEventList.toString());
        return usersFromEventList;
    }

    private void setAdapter(ArrayList<UserInfoData> list) {

        RecyclerView recyclerView = findViewById(R.id.users_info_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListOfUsersAdapter listToAdapter = new ListOfUsersAdapter(list,this);
        recyclerView.setAdapter(listToAdapter);

    }
}
