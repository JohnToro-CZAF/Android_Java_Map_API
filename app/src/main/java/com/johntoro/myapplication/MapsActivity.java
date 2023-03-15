package com.johntoro.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.johntoro.myapplication.models.GeocodingResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    // widgets
    private ViewAnimator viewAnimator;
    private ProgressBar progressBar;
    private ImageView mGps, mInfo, mPlacePicker;

    // vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap gMap;
    // Handle the location service of the search suggestion
    private Handler handler = new Handler();
    private PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter()).create();
    private RequestQueue queue;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;

    // After initializing the map, the callback function will be triggered to setup the map
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng singapore = new LatLng(1.290270, 103.851959);
        gMap = googleMap;
        gMap.setTrafficEnabled(true); // Display traffic
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, DEFAULT_ZOOM));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setSupportActionBar(findViewById(R.id.toolbar));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        viewAnimator = (ViewAnimator) findViewById(R.id.view_animator);
        placesClient = (PlacesClient) Places.createClient(this);
        queue = Volley.newRequestQueue(this);
        mGps = (ImageView) findViewById(R.id.ic_my_location);
        initRecyclerView();
        initGps();
        getLocationPermissionAndInitialize();
        if (mLocationPermissionsGranted) {
            initMap();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            sessionToken = AutocompleteSessionToken.newInstance();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView
                .addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        adapter.setPlaceClickListener(this::geocodePlaceAndDisplay);
    }
    private void initGps() {
        Log.d(TAG, "init: initializing");
        mGps.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked gps icon");
            getDeviceLocation();
        });
    }
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    private void initSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setIndeterminate(true);

                // Cancel any previous place prediction requests
                handler.removeCallbacksAndMessages(null);

                // Start a new place prediction request in 300 ms
                handler.postDelayed(() -> {
                    getPlacePredictions(newText);
                }, 300);
                return true;
            }
        });
    }
    private void getPlacePredictions(String query) {
        final LocationBias boxBias = RectangularBounds.newInstance(
                new LatLng(1.290270, 103.851959),
                new LatLng(1.290270, 103.851959));
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setLocationBias(boxBias)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .setCountries("IN")
                .build();

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener((response) -> {
            List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
            adapter.setPredictions(predictions);

            progressBar.setIndeterminate(false);
            viewAnimator.setDisplayedChild(predictions.isEmpty() ? 0 : 1);
        }).addOnFailureListener((exception) -> {
            progressBar.setIndeterminate(false);
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }
    private void geocodePlaceAndDisplay(AutocompletePrediction placePrediction) {
        // Construct the request URL
        final String apiKey = BuildConfig.MAPS_API_KEY;
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String requestURL = String.format(url, placePrediction.getPlaceId(), apiKey);

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                response -> {
                    try {
                        // Inspect the value of "results" and make sure it's not empty
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() == 0) {
                            Log.w(TAG, "No results from geocoding request.");
                            return;
                        }

                        // Use Gson to convert the response JSON object to a POJO
                        GeocodingResult result = gson.fromJson(
                                results.getString(0), GeocodingResult.class);
                        moveCamera(result.geometry.location);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "Request failed"));

        // Add the request to the Request queue.
        queue.add(request);
    }
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                @SuppressLint("MissingPermission") Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();
                        if (currentLocation != null) {
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            Toast.makeText(MapsActivity.this, "Current location is " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "task is successful but current location is null");
                            // If the current location is null, then move the camera to the default location
                            Toast.makeText(MapsActivity.this, "unable to get current location (due to the internal app error)", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        // If the current location is null, then move the camera to the default location
                        Toast.makeText(MapsActivity.this, "unble to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    private void moveCamera(LatLng latLng){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        MarkerOptions options = new MarkerOptions().position(latLng);
        gMap.addMarker(options);
        // Hide the keyboard after searching
        hideSoftKeyboard();
    }
    private void getLocationPermissionAndInitialize(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void hideSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}