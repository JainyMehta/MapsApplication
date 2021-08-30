package com.example.n01390712_jainy_placessdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements GoogleMap.OnMapClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isLocationPermissionGranted;
    FusedLocationProviderClient flp;
    AutocompleteSupportFragment autocompleteFragment;
    ImageButton btn_search, btn_info;
    Button btn_searchterm;
    Place place1;
    String currentLat, currentLng;
    int LAUNCH_SECOND_ACTIVITY = 1;
    JSONArray jsonArr1=null;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String apiKey = getString(R.string.google_places_key);
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        btn_search = (ImageButton) findViewById(R.id.btn_Search);
        btn_info= (ImageButton) findViewById(R.id.btn_Info);
        btn_searchterm = (Button) findViewById(R.id.btn_searchterm);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();

                btn_searchterm.setVisibility(View.VISIBLE);
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {


                        place1=place;
                        Log.i("jm", "An error occurred: " + place1.getName());
                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i("jm", "An error occurred: " + status);
                    }
                });
            }
        });


        btn_searchterm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+currentLat+","+currentLng+"&radius=5000&key="
                        +getResources().getString(R.string.google_places_key)+"&keyword="+place1.getName().replace(' ','+');

                Log.e("response: ",url);
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                String name = results.getJSONObject(i).getString("name");
                                String vicinity = results.getJSONObject(i).getString("vicinity");
                                LatLng l1 = new LatLng(results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                                MarkerOptions options = new MarkerOptions().position(l1).title(name).snippet(vicinity);
                                Marker marker = mMap.addMarker(options);
                                jsonArr1=results;
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("response: ",e.toString());
                        }
                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                requestQueue.add(jsonObjectRequest);


                getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(currentLat),Double.parseDouble(currentLng))));
                btn_searchterm.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(this);

        showUserLocation();
    }


    void showUserLocation() {
        getUserLocationPermission();
        if (isLocationPermissionGranted)
            centerMapOnUserLocation();
        else
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
    }

    void getUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            isLocationPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
    }

    void centerMapOnUserLocation() {
        FusedLocationProviderClient flp = new FusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            Task<Location> locationTask = flp.getLastLocation();
            locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location lastKnownUserLocation = task.getResult();
                        if (lastKnownUserLocation != null) {
                            Log.d("jm", "onComplete: Location: " + lastKnownUserLocation.toString());
                            LatLng userLatLng = new LatLng(lastKnownUserLocation.getLatitude(), lastKnownUserLocation.getLongitude());
                            Log.d("jm", "onComplete: Location: " + userLatLng.toString());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                        }
                        getLocationUpdates();
                    } else
                        Toast.makeText(MainActivity2.this, "Unknown last user location", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void getLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }

        flp = new FusedLocationProviderClient(this);
        //Remove onPause
        flp.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    currentLat= String.valueOf(location.getLatitude());
                    currentLng= String.valueOf(location.getLongitude());
                    Toast.makeText(MainActivity2.this, " " + location.getLatitude() + location.getLongitude(), Toast.LENGTH_SHORT).show();
                }

            }
        }, null);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latLng.latitude+","+latLng.longitude+"&sensor=true&key="+getResources().getString(R.string.google_places_key);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    String name = results.getJSONObject(0).getString("formatted_address");
                    String type = results.getJSONObject(0).getString("types");
                    MarkerOptions options = new MarkerOptions().position(latLng).title(name).snippet(type
                    );
                    Marker marker = mMap.addMarker(options);
                    marker.showInfoWindow();
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("response: ",e.toString());
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void infoClicked(View view) {
        if(jsonArr1!=null) {
            Intent intent = new Intent(MainActivity2.this, InfoActivity.class);
            intent.putExtra("JsonArray",jsonArr1.toString());
            startActivityForResult(intent, 1000);
        }else{
            Toast.makeText(mapFragment.getContext(),"There is no data to be shown",Toast.LENGTH_LONG);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode==RESULT_OK){
            String text = data.getStringExtra("lat")+","+data.getStringExtra("lng")+"       "+data.getStringExtra("latlng");
            LatLng latLng=new LatLng(Double.parseDouble(data.getStringExtra("lat")),Double.parseDouble(data.getStringExtra("lng")));
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+latLng.latitude+","+latLng.longitude+"&sensor=true&key="+getResources().getString(R.string.google_places_key);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        String name = results.getJSONObject(0).getString("formatted_address");
                        String type = results.getJSONObject(0).getString("types");
                        MarkerOptions options = new MarkerOptions().position(latLng).title(name).snippet(type
                        );
                        Marker marker = mMap.addMarker(options);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        marker.showInfoWindow();

                    }

                    catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("response: ",e.toString());
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue.add(jsonObjectRequest);

        }

    }

}