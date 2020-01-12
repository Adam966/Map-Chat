package com.project.mapchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.project.mapchat.adapters.PlacesAdapter;
import com.project.mapchat.main.activities.AddEventActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePlace extends AppCompatActivity implements PlacesAdapter.ItemClick {
    private SearchView search;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_picker_layout);

        recyclerView = findViewById(R.id.friendsList);
        search = findViewById(R.id.searchViewFriends);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!(s.length() < 3))
                    queryAddress(s);

                return false;
            }
        });
    }

    /////////////////////////////////////// QUERY EVENT ADDRESS ////////////////////////////////////
    public void queryAddress(String query) {
        MapboxGeocoding geocode = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(query)
                .build();

        geocode.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                setAdapter((ArrayList) response.body().features());
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setAdapter(ArrayList list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        PlacesAdapter placesAdapter = new PlacesAdapter(list, this);
        recyclerView.setAdapter(placesAdapter);
    }

    //////////////////////////////////////// CHOOSED ADDRESS //////////////////////////////////////
    @Override
    public void onItemClick(CarmenFeature feature) {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("placeName", feature.placeName());
        startActivity(intent);
    }
}
