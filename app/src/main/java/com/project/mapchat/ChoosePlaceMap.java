package com.project.mapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.project.mapchat.entities.Place;
import com.project.mapchat.main.activities.AddEventActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ChoosePlaceMap extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private SearchView searchView;
    private SharedPrefs appSharedPrefs;
    private SymbolManager symbolManager;
    private Symbol symbol;
    private FloatingActionButton button;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appSharedPrefs = new SharedPrefs(this);

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_choose_place_map);

        searchView = findViewById(R.id.searchPlace);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.wtf("QUERY", s);
                queryAddress(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapView = findViewById(R.id.mapViewPlace);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        button = findViewById(R.id.addPlace);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                if (place != null) {
                    intent.putExtra("place", place);

                    Log.wtf("PLACE", place.toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        setDarkModeMap(mapboxMap);
        mapboxMap.addOnMapClickListener(this);
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        symbolManager.deleteAll();

        symbol = symbolManager.create(new SymbolOptions()
                .withLatLng(point)
                .withIconImage("location_icon"));
        return true;
    }

    private void animateCameraPosition(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(16)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 7000);
    }

    private void setDarkModeMap(MapboxMap mapbox){
        if(appSharedPrefs.loadDarkModeState() == true){
            mapbox.setStyle(new Style.Builder().fromUri("mapbox://styles/pralko/ck5hao1a708ya1iqfrrwlwd9d/draft"), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    symbolManager = new SymbolManager(mapView, mapboxMap, mapboxMap.getStyle());
                    mapboxMap.getStyle().addImage("location_icon", getDrawable(R.drawable.ic_location_on_black_24dp));

                }
            });
        }else {
            mapbox.setStyle(new Style.Builder().fromUri("mapbox://styles/pralko/ck5fyoun504er1iqmrzc7zlbk/draft"), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    symbolManager = new SymbolManager(mapView, mapboxMap, mapboxMap.getStyle());
                    mapboxMap.getStyle().addImage("location_icon", getDrawable(R.drawable.ic_location_on_black_24dp));

                }
            });
        }
    }

    private void queryAddress(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.opencagedata.com/geocode/v1/json?q=" + URLEncoder.encode(query) +"&key=a812717c7b764edea8377a9ed4caf3bc&pretty=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,  null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    place = getJsonPlaces(response.getJSONArray("results"));
                    animateCameraPosition(new LatLng(place.getLat(), place.getLng()));
                    Log.wtf("PLACE", place.toString());
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

    private Place getJsonPlaces(JSONArray array) {
        Place place = new Place();
            try {
                place.setLat(array.getJSONObject(0).getJSONObject("geometry").getDouble("lat"));
                place.setLng(array.getJSONObject(0).getJSONObject("geometry").getDouble("lng"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                place.setFormatted(array.getJSONObject(0).getString("formatted"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

