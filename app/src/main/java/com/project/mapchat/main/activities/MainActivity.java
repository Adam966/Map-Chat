package com.project.mapchat.main.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.project.mapchat.R;
import com.project.mapchat.SharedPrefs;
import com.project.mapchat.chatEvents.ChatEvents;
import com.project.mapchat.dialogs.ExitDialog;
import com.project.mapchat.entities.EventFromServer;
import com.project.mapchat.entities.UserEvent;
import com.project.mapchat.service.ServerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,PermissionsListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MapboxMap.OnMapClickListener, ExitDialog.DialogListener {

    // Mapbox
    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 1;
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);
    private ImageView userLocation;
    private MarkerViewManager markerViewManager;
    private MarkerView markerView;

    // Google
    public static final int REQUEST_LOCATION=001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    Context context;
    PendingResult<LocationSettingsResult> pendingResult;

    // Class for Shared Preferences
    private SharedPrefs appSharedPrefs;

    // Permissions
    private PermissionsManager permissionsManager;

    // State of the marker which is opened or not
    static boolean isOpen = false;
    private ArrayList<EventFromServer> events;
    private ArrayList<UserEvent> userEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        appSharedPrefs = new SharedPrefs(this);
        //setDarkMode(appSharedPrefs);

        if(appSharedPrefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }

        super.onCreate(savedInstanceState);
        context = this;

        // accessing tokens from shared preferences
        Log.i("Tokens",appSharedPrefs.getServerToken()+"   "+appSharedPrefs.getServerToken());

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        /*
        userLocation = findViewById(R.id.userLocation);
        userLocation.setImageDrawable(getDrawable(R.drawable.ic_my_location_black_24dp));
        userLocation.bringToFront();*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        //Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentHome = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_settings:
                        //Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                        Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                }
            return true;
            }
        });

        mapView = findViewById(R.id.mapViewPlace);
        mapView.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Gps is Enabled", Toast.LENGTH_SHORT).show();
        } else {
           EnableGPS();
        }
        mapView.getMapAsync(this);
    }

    //////////////////////////////////////////// EXIT APP DIALOG ///////////////////////////////////
    @Override
    public void onBackPressed() {
        DialogFragment fragment = new ExitDialog();
        fragment.show(getSupportFragmentManager(), "EXIT");
    }

    @Override
    public void exitListener() {
        finishAffinity();
    }

    //////////////////////////////////////////// MAPBOX ON READY ///////////////////////////////////
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        Log.wtf("MAPBOX", "MAP READY");
        this.mapboxMap = mapboxMap;
        mapboxMap.addOnMapClickListener(this);
        setDarkModeMap(mapboxMap);
    }

    public void userLocation(View view) {
        Log.wtf("LOCATION", "GET USER LOCATION");
        enableLocationComponent(mapboxMap.getStyle());
    }

    ////////////////////////////////// CHANGE CAMERA POSITION //////////////////////////////////////
    private void changeCameraPosition(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(14)
                .build();
        Log.wtf("CAMERA POSITION", "CHANGE");
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 7000);
    }

    ///////////////////////////////////////////// LOCATION PERMISSIONS /////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(this, R.string.user_location_permission, Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////// LOCATION COMPONENT INITIALIZATION /////////////////////
    @SuppressLint("MissingPermission")
    private void enableLocationComponent(Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

            initLocationEngine();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);

        if (getIntent().getDoubleExtra("Lat", 0) != 0) {
            changeCameraPosition(new LatLng(getIntent().getDoubleExtra("Lat", 0), getIntent().getDoubleExtra("Lon", 0)));
            getIntent().removeExtra("Lat");
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Log.wtf("DADA","ON MAP CLICK");
        if (isOpen) {
            markerViewManager.removeMarker(markerView);
            isOpen = false;
            return true;
        }
        return false;
    }

    ////////////////////////////////////////////// LOCATION CHANGE CALLBACK ////////////////////////
    private static class MainActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MainActivity> activityWeakReference;

        MainActivityLocationCallback(MainActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {
            MainActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //////////////////////////////// ADD EVENT /////////////////////////////////////////////////////
    public void addEvent(View view) {
        startActivity(new Intent(this, AddEventActivity.class));
    }

    /////////////////////////////// OPEN CHAT ////////////////////////////////////
    public void openChat(View view) {
        startActivity(new Intent(this, ChatEvents.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.wtf("ONRESUME","On resume is on");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }

        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        if (markerViewManager != null)
            markerViewManager.onDestroy();

        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    ///////////////////////////////////// setting darkmode /////////////////////////////////////////
    private void setDarkMode(SharedPrefs prefs){
        if(prefs.loadDarkModeState() == true){
            setTheme(R.style.AppDark);
        }else {
            setTheme(R.style.AppNormal);
        }
    }

    ////////////////////////////////////// SET STYLE FOR MAP ///////////////////////////////////////
    private void setDarkModeMap(MapboxMap mapbox){
        if(appSharedPrefs.loadDarkModeState() == true){
            mapbox.setStyle(new Style.Builder().fromUri("mapbox://styles/pralko/ck5hao1a708ya1iqfrrwlwd9d/draft"), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                    getUserEvents(appSharedPrefs.getServerToken());
                    //setMapLayer(null);
                }
            });
        }else {
            mapbox.setStyle(new Style.Builder().fromUri("mapbox://styles/pralko/ck5fyoun504er1iqmrzc7zlbk/draft"), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                    getUserEvents(appSharedPrefs.getServerToken());
                    //setMapLayer(null);
                }
            });
        }
    }

    /////////////////////////////////// GET ALL USER EVENTS /////////////////////////////////////////////
    private void getUserEvents(String serverToken) {
        Call<ArrayList<EventFromServer>> call = ServerService
                .getInstance()
                .getEvents()
                .getEventsRequest("Bearer " + serverToken);

        call.enqueue(new Callback<ArrayList<EventFromServer>>() {
            @Override
            public void onResponse(Call<ArrayList<EventFromServer>> call, Response<ArrayList<EventFromServer>> response) {
                if(response.isSuccessful()){
                    events = response.body();
                    getUserEvents();
                }else {
                    switch(response.code()){
                        case 401:{
                            Log.wtf("401","Unauthorized");
                            new Logout().logout(appSharedPrefs,getApplicationContext());
                        }
                        break;

                        case 500:{
                            Toast.makeText(getApplicationContext(),"Server Problem",Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EventFromServer>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserEvents() {
        Call<ArrayList<UserEvent>> call = ServerService
                .getInstance()
                .getUserJoinedEvents()
                .getUserJoinedEventsRequest("Bearer " + appSharedPrefs.getServerToken());

        call.enqueue(new Callback<ArrayList<UserEvent>>() {
            @Override
            public void onResponse(Call<ArrayList<UserEvent>> call, Response<ArrayList<UserEvent>> response) {
                userEvents = response.body();
                setMapLayer(events);
            }

            @Override
            public void onFailure(Call<ArrayList<UserEvent>> call, Throwable t) {

            }
        });



    }

    ////////////////////////////////// MAP LAYER MANAGER ///////////////////////////////////////////
    private void setMapLayer(ArrayList<EventFromServer> list) {
        mapboxMap.getStyle().addImage("location_icon", getDrawable(R.drawable.ic_location_on_black_24dp));
        mapboxMap.getStyle().addImage("location_icon_user", getDrawable(R.drawable.ic_location_on_blue_24dp));

        ArrayList<EventFromServer> userEvent = new ArrayList<>();

        for (UserEvent e : userEvents) {
            Log.wtf("User Event ID", String.valueOf(e.getEvent()));
            for (EventFromServer es : list) {
                if (e.getEvent() == Integer.valueOf(es.getId())) {
                    userEvent.add(es);
                }
            }
        }

        list.removeAll(userEvent);

        Log.wtf("USER LIST", String.valueOf(userEvent.size()));
        Log.wtf("LIST", String.valueOf(list.size()));

        SymbolManager manager = new SymbolManager(mapView, mapboxMap, mapboxMap.getStyle(), null, new GeoJsonOptions()
                .withCluster(true)
                .withClusterMaxZoom(12)
                .withClusterRadius(10));

        manager.setIconAllowOverlap(true);
        List<SymbolOptions> symbolOptionsList = new ArrayList<>();

        //////////////////// ALL EVENTS ///////////////
         Log.wtf("LIST", String.valueOf(list.size()));
        Gson gson = new Gson();
        for (EventFromServer e: list) {

            HashMap<String,Object> map = new HashMap<>();

            map.put("groupName",e.getGroupName());
            map.put("description",e.getDescription());
            map.put("eventId",e.getId());

            symbolOptionsList.add(new SymbolOptions()
                    .withLatLng(new LatLng(Double.valueOf(e.getLocation().getLatitude()), Double.valueOf(e.getLocation().getLongtitude())))
                    .withIconImage("location_icon")
                    .withData(new JsonPrimitive(gson.toJson(map)))
            );

            map.clear();
        }

        //////////////////// USER EVENTS /////////////////
        for (EventFromServer e: userEvent) {

            HashMap<String,Object> map = new HashMap<>();

            map.put("groupName",e.getGroupName());
            map.put("description",e.getDescription());
            map.put("eventId",e.getId());

            symbolOptionsList.add(new SymbolOptions()
                    .withLatLng(new LatLng(Double.valueOf(e.getLocation().getLatitude()), Double.valueOf(e.getLocation().getLongtitude())))
                    .withIconImage("location_icon_user")
                    .withData(new JsonPrimitive(gson.toJson(map)))
            );

            map.clear();
        }

        manager.create(symbolOptionsList);
        manager.addClickListener(new OnSymbolClickListener() {
            @Override
            public void onAnnotationClick(Symbol symbol) {
                //Toast.makeText(getApplicationContext(), "SYMBOL CLICKED " + symbol.getData().getAsString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "SYMBOL CLICKED " + symbol.getData(), Toast.LENGTH_SHORT).show();
                if (!isOpen) {
                    showMarkerView(symbol);
                    isOpen = true;
                }

            }
        });
    }

    //////////////////////////// MARKER VIEW POP UP/////////////////////////////////////////////////
    private void showMarkerView(Symbol symbol) {
        markerViewManager = new MarkerViewManager(mapView, mapboxMap);

        View eventPopUp = LayoutInflater.from(MainActivity.this).inflate(R.layout.map_event_view, null);
        eventPopUp.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView eventName = eventPopUp.findViewById(R.id.eventName);
        TextView eventDesc = eventPopUp.findViewById(R.id.eventDesc);
        Button openEvent = eventPopUp.findViewById(R.id.openEvent);

        JsonParser parser = new JsonParser();

        JsonElement jsonElement = parser.parse(symbol.getData().getAsJsonPrimitive().getAsString());
        final JsonObject rootObject = jsonElement.getAsJsonObject();

        eventName.setText(rootObject.get("groupName").getAsString());
        eventDesc.setText(rootObject.get("description").getAsString());

        markerView = new MarkerView(new LatLng(symbol.getLatLng().getLatitude(), symbol.getLatLng().getLongitude()), eventPopUp);
        markerViewManager.addMarker(markerView);

        openEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "OPEN EVENT DETAIL", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),EventDetail.class);
                i.putExtra("eventId",rootObject.get("eventId").getAsString());
                startActivity(i);
            }
        });
/*
        findViewById(R.id.mapView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("DADA","DADA");
                markerViewManager.removeMarker(markerView);
                isOpen = false;
            }
        });
*/
        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerViewManager.removeMarker(markerView);
                isOpen = false;
            }
        });*/
    }



    //////////////////////////// GOOGLE POPUP TO GPS ON ////////////////////////////////////////////
    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /////////////////////////// GOOGLE SERVICE /////////////////////////////////////////////////////
    public void EnableGPS() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        LocationSetting();
    }

    public void LocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        serviceResult();

    }

    public void serviceResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();

                Log.i("CODE",status.toString());

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        //this.recreate();
                        Toast.makeText(context, "Gps enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(context, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void restart() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            //Code for recreate
            this.recreate();
        } else {
            //Code for Intent
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
