package com.project.mapchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;
import com.project.mapchat.adapters.PlacesAdapter;
import com.project.mapchat.entities.Place;
import com.project.mapchat.main.activities.AddEventActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

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
                if (!(s.length() < 5))
                    queryAddress(s);

                return false;
            }
        });
    }

    /////////////////////////////////////// QUERY EVENT ADDRESS ////////////////////////////////////
    /*public void queryAddress(String query) {
        MapboxGeocoding geocode = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(URLEncoder.encode(query))
                .geocodingTypes("address")
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
    }*/

    private void queryAddress(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.opencagedata.com/geocode/v1/json?q=" + URLEncoder.encode(query) +"&key=a812717c7b764edea8377a9ed4caf3bc&pretty=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,  null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.wtf("RESPONSE", response.toString());
                    setAdapter(getJsonPlaces(response.getJSONArray("results")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    private ArrayList<Place> getJsonPlaces(JSONArray array) {
        ArrayList<Place> list = new ArrayList<>();
        Place place = new Place();
        for (int i = 0; i < array.length(); i++) {
            try {
                place.setRoad(array.getJSONObject(i).getJSONObject("components").getString("road"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                place.setPostcode(array.getJSONObject(i).getJSONObject("components").getString("postcode"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                place.setHouseNumber(array.getJSONObject(i).getJSONObject("components").getString("house_number"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                place.setCountry(array.getJSONObject(i).getJSONObject("components").getString("country"));
                place.setTown(array.getJSONObject(i).getJSONObject("components").getString("town"));
            } catch(JSONException e) {
                e.printStackTrace();
            }

            try {
                place.setLat(array.getJSONObject(i).getJSONObject("geometry").getDouble("lat"));
                place.setLng(array.getJSONObject(i).getJSONObject("geometry").getDouble("lng"));

                list.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                place.setFormatted(array.getJSONObject(i).getString("formatted"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.wtf("PLACE", place.toString());
        return list;
    }

    private void setAdapter(ArrayList list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        PlacesAdapter placesAdapter = new PlacesAdapter(list, this);
        recyclerView.setAdapter(placesAdapter);
    }

    //////////////////////////////////////// CHOOSED ADDRESS //////////////////////////////////////
    @Override
    public void onItemClick(Place place) {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("place", place);

        Log.wtf("PLACE", place.toString());
        startActivity(intent);
    }
}
