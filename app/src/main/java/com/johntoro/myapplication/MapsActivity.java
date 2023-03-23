package com.johntoro.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.johntoro.myapplication.adapters.LatLngAdapter;
import com.johntoro.myapplication.models.NearByResponse;
import com.johntoro.myapplication.models.Results;
import com.johntoro.myapplication.remotes.DirectionsJSONParser;
import com.johntoro.myapplication.remotes.HandleURL;
import com.johntoro.myapplication.remotes.GoogleApiService;
import com.johntoro.myapplication.remotes.RetrofitBuilder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback
{
    // region Final Variables
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    // endregion
    // region widgets
    private ViewAnimator viewAnimator;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout bottomSheet;
    private ProgressBar progressBar;
    private ImageView mGps;
    private AppCompatButton mHospital, mRestaurent, mPetro, mCarPark;
    private LinearLayout mFacilitiesLayout, mExitDirections;
    private GoogleMap gMap;
    private Marker currentLocationMarker;
    private Marker searchedLocationMarker;
    private Polyline currentPolyline;
    // endregion
    // vars
    private Boolean mLocationPermissionsGranted = false;
    private Boolean isNearByFacilities = false;
    // region AutoSuggestionSearchLocation
    private final Handler handler = new Handler();
    private final PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter()).create();
    private RequestQueue queue;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    // endregion
    private android.location.Location currentLocation;
    // TODO: SearchedLocation
    private android.location.Location searchedLocation;
    private List<Results> nearByFacilities;
    private List<Marker> nearByFacilitiesMarkers;
    private Set<Results> favourites = new HashSet<Results>();

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
            gMap.setOnMapClickListener(latLng -> {
                if (isNearByFacilities) {
                    onExitNearByFacilities();
                }
                onDropSuggestion(false);
                hideSoftKeyboard();
            });
        }
    }
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setSupportActionBar(findViewById(R.id.toolbar));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        viewAnimator = (ViewAnimator) findViewById(R.id.view_animator);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        placesClient = Places.createClient(this);
        queue = Volley.newRequestQueue(this);
        mExitDirections = (LinearLayout) findViewById(R.id.exit_direction);
        mGps = (ImageView) findViewById(R.id.ic_my_location);
        mHospital = (AppCompatButton) findViewById(R.id.btn_options_hospital);
        mRestaurent = (AppCompatButton) findViewById(R.id.btn_options_restaurant);
        mCarPark = (AppCompatButton) findViewById(R.id.btn_options_carpark);
        mPetro = (AppCompatButton) findViewById(R.id.btn_options_petro_station);
        mFacilitiesLayout = (LinearLayout) findViewById(R.id.facilities_buttons_layout);
        bottomSheet = (RelativeLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);
        initRecyclerView();
        getLocationPermissionAndInitialize();
        if (mLocationPermissionsGranted) {
            initMap();
            initGps();
            initRetrieveFacilities();
        }
    }
    @Override
    public boolean onCreateOptionsMenu (@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search) {
            sessionToken = AutocompleteSessionToken.newInstance();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initRecyclerView () {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView
                .addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        adapter.setPlaceClickListener(this::onClickSuggestionAndMoveCamera);
    }
    private void initGps () {
        Log.d(TAG, "init: initializing");
        mGps.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked gps icon");
            getDeviceLocation();
        });
    }
    private void initRetrieveFacilities () {
        Log.d(TAG, "init: initializing BUTTON to retrieve facilities");
        mPetro.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked petro station button");
            if (isNearByFacilities) {
                onExitNearByFacilities();
            }
            retrieveFacilitiesFragment("petro station");
        });
        mHospital.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked hospital button");
            if (isNearByFacilities) {
                onExitNearByFacilities();
            }
            retrieveFacilitiesFragment("hospital");
        });
        mRestaurent.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked restaurant button");
            if (isNearByFacilities) {
                onExitNearByFacilities();
            }
            retrieveFacilitiesFragment("restaurant");
        });
        mCarPark.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked car park button");
            if (isNearByFacilities) {
                onExitNearByFacilities();
            }
            retrieveFacilitiesFragment("car park");
        });
    }
    private void initMap () {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    private void initSearchView (SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocationByString(query);
                onDropSuggestion(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setIndeterminate(true);
                onDropSuggestion(true);
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
    private void getPlacePredictions (String query) {
        final LocationBias boxBias = RectangularBounds.newInstance(
                new LatLng(1.290270, 103.851959),
                new LatLng(1.290270, 103.851959));
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setCountry("SG")
                .setLocationBias(boxBias)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
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
    public void onClickSuggestionAndMoveCamera (AutocompletePrediction placePrediction) {
        // Construct the request URL
        final String apiKey = BuildConfig.MAPS_API_KEY;
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
        final String placeId = placePrediction.getPlaceId();
        final String requestURL = String.format(url, placeId, apiKey);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        Task<FetchPlaceResponse> fetchPlaceTask = placesClient.fetchPlace(request);
        fetchPlaceTask.addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            LatLng searchedLatLng = place.getLatLng();
            Log.d(TAG, "seached string: " + searchedLatLng.toString());
            searchedLocation = new android.location.Location("");
            searchedLocation.setLatitude(searchedLatLng.latitude);
            searchedLocation.setLongitude(searchedLatLng.longitude);
            moveCamera(searchedLatLng);
            Log.i(TAG, "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }
    private void searchLocationByString (String query) {
        Log.d(TAG, "searchGeoLocation: searching for location");
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(query, 1);
        } catch (Exception e) {
            Log.e(TAG, "searchGeoLocation: IOException: " + e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "searchGeoLocation: found a location: " + address.toString());
            searchedLocation = new android.location.Location(String.valueOf(address));
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()));
        }
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation () {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        currentLocation = task.getResult();
                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    private void moveCamera (LatLng latLng) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        MarkerOptions options = new MarkerOptions().position(latLng);
        gMap.addMarker(options);
        // Hide the keyboard after searching
        hideSoftKeyboard();
    }
    private void moveCamera (LatLng latLng, double zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        MarkerOptions options = new MarkerOptions().position(latLng);
        gMap.addMarker(options);
        gMap.animateCamera(CameraUpdateFactory.zoomTo((float) zoom));
        // Hide the keyboard after searching
        hideSoftKeyboard();
    }
    private void moveCamera (List<Results> nearByFacilities) {
        double lat = 0.0;
        double lng = 0.0;
        for (Results facility : nearByFacilities) {
            LatLng latLng = facility.getGeometry().getLocation().getLatLng();
            lat += latLng.latitude;
            lng += latLng.longitude;
        }
        lat += currentLocation.getLatitude();
        lng += currentLocation.getLongitude();
        lat /= (nearByFacilities.size() + 1);
        lng /= (nearByFacilities.size() + 1);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), DEFAULT_ZOOM));
    }
    private String buildUrl (double latitude, double longitude, String API_KEY, String facilityType) {
        return "api/place/nearbysearch/json?" + "location=" +
                Double.toString(latitude) +
                "," +
                Double.toString(longitude) +
                "&radius=5000" + // places between 5 kilometer
                "&types=" + facilityType.toLowerCase() +
                "&key=" + API_KEY;
    }

    private void retrieveFacilitiesFragment (String facilityType) {
        isNearByFacilities = true;
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.show();
        String apiKey = BuildConfig.MAPS_API_KEY;
        if (searchedLocation == null) {
            searchedLocation = currentLocation;
        }
        Log.d("searchedLocation", searchedLocation.toString());
        String url = buildUrl(searchedLocation.getLatitude(), searchedLocation.getLongitude(), apiKey, facilityType);
        GoogleApiService googleApiService = RetrofitBuilder.builder().create(GoogleApiService.class);
        Call<NearByResponse> call = googleApiService.getMyNearByPlaces(url);
        call.enqueue(new Callback<NearByResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearByResponse> call, @NonNull Response<NearByResponse> response) {
                NearByResponse nearByResponse = response.body();
                if (nearByResponse != null) {
                    nearByFacilities = nearByResponse.getResults();
                    // region set up the bottom sheet once we got List<Results>
                    createMarkers(nearByFacilities);
                    moveCamera(nearByFacilities);
                    NearbyFacilitiesListFragment nearbyFacilitiesListFragment = NearbyFacilitiesListFragment.newInstance(nearByFacilities);
                    nearbyFacilitiesListFragment.setOnItemClickListener(MapsActivity.this::moveCamera);
                    nearbyFacilitiesListFragment.setOnItemFavoriteClickListener(MapsActivity.this::changeFavorite);
                    nearbyFacilitiesListFragment.setOnItemDetailsClickListener(MapsActivity.this::startFacilityDetails);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container_view, nearbyFacilitiesListFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    // endregion
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(@NonNull Call<NearByResponse> call, @NonNull Throwable t) {
                Log.d("@nearByResponse: Got request failed", t.toString());
                dialog.dismiss();
                Toast.makeText(MapsActivity.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected class FacilityDetailsContract extends ActivityResultContract<Bundle, Results> {
        @NonNull
        public Intent createIntent(@NonNull Context context, Bundle input) {
            Intent intent = new Intent(context, FacilityDetailsActivity.class);
            intent.putExtras(input);
            return intent;
        }
        @Override
        public Results parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode != RESULT_OK) {
                return null;
            }
            return (Results) intent.getSerializableExtra("showDistance");
        }
    }
    ActivityResultLauncher<Bundle> facilityDetailsLauncher = registerForActivityResult(
            new FacilityDetailsContract(),
            (Results results) -> {onExitNearByFacilities(); showDistance(results); showDirection(results);}
    );

    private void showDistance(Results chosenFacility) {
        LatLng chosenFacilityLatLng = chosenFacility.getGeometry().getLocation().getLatLng();
        LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        double distance = SphericalUtil.computeDistanceBetween(chosenFacilityLatLng, currentLocationLatLng);
        distance /= 1000;
        String distanceString = String.format("%.2f", distance);
        Toast.makeText(this, "Distance: " + distanceString + " km", Toast.LENGTH_SHORT).show();
    }
    private void showDirection(Results results) {
        LatLng destination = results.getGeometry().getLocation().getLatLng();
        LatLng currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        // region For destination
        gMap.addMarker(new MarkerOptions()
                .position(destination)
                .title(results.getName())
                .snippet(results.getVicinity())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .alpha(1f)).showInfoWindow();
        gMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 13.0f));
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        // endregion
        // region For currentLocation
        gMap.addMarker(new MarkerOptions().position(currentPosition)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .alpha(1f)).showInfoWindow();

        gMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13.0f));
        gMap.getUiSettings().setCompassEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        // endregion
        HandleURL handleURL = new HandleURL(destination, currentPosition);
        String url = handleURL.getDirectionsUrl();
        PlotDirection plotDirection = new PlotDirection();
        plotDirection.execute(url);
        Log.d(TAG, "showDirection got first oops: " + url);
        gMap.setTrafficEnabled(false);
        moveCamera(currentPosition, 14.0f);
        // TODO: Add new button to exit the view, deleting the polyline and move camera to current location
        Button exitDirectionBtn = new Button(this);
        exitDirectionBtn.setText("Exit Directing");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mExitDirections.addView(exitDirectionBtn, lp);
        exitDirectionBtn.setOnClickListener(v -> {
            moveCamera(currentPosition);
            gMap.clear();
            currentLocationMarker = gMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .alpha(1f));
            currentLocationMarker.showInfoWindow();
            mExitDirections.removeAllViews();
        });
    }
    private void startFacilityDetails(Results results) {
        // Attach this method to fragment's onItemDetailsClickListener
        Bundle bundle = new Bundle();
        bundle.putSerializable(FacilityDetailsActivity.FACILITY_DETAILS, results);
        facilityDetailsLauncher.launch(bundle);
    }
    private void createMarkers (List<Results> nearByFacilities) {
        if (searchedLocation == null) {
            searchedLocation = currentLocation;
        }
        MarkerOptions markerOptions = new MarkerOptions().
                position(new LatLng(searchedLocation.getLatitude(), searchedLocation.getLongitude()))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        searchedLocationMarker = gMap.addMarker(markerOptions);
        nearByFacilitiesMarkers = new ArrayList<Marker>();
        for (Results facility : nearByFacilities) {
            MarkerOptions option = new MarkerOptions();
            option.position(facility.getGeometry().getLocation().getLatLng())
                    .title(facility.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Marker marker = gMap.addMarker(option);
            nearByFacilitiesMarkers.add(marker);
        }
    }
    private void removeMarkers () {
        // TODO: remove all needed markers instead of by marker.remove() but this method
        // TODO: is not working
        if (searchedLocation == null) {
            searchedLocation = currentLocation;
        }
        gMap.clear();
        LatLng latLng = new LatLng(searchedLocation.getLatitude(), searchedLocation.getLongitude());
        gMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
    }
    private void getLocationPermissionAndInitialize (){
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
    private void hideSoftKeyboard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void onExitNearByFacilities () {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        nearByFacilities.clear();
        removeMarkers();
        nearByFacilitiesMarkers.clear();
        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        isNearByFacilities = false;
    }
    private void onDropSuggestion (boolean isDrop) {
        if (isDrop) {
            this.viewAnimator.setVisibility(View.VISIBLE);
            this.mFacilitiesLayout.setVisibility(View.GONE);
            this.mGps.setVisibility(View.GONE);
        } else {
           this.viewAnimator.setVisibility(View.GONE);
           this.mFacilitiesLayout.setVisibility(View.VISIBLE);
           this.mGps.setVisibility(View.VISIBLE);
        }
    }
    private void changeFavorite (Results facilityDetails, boolean isFavorite) {
        for (Results facility : nearByFacilities) {
            Log.d(TAG, "changeFavorite: " + facility.getName());
        }
        if (isFavorite) {
            favourites.add(facilityDetails);
        } else {
            favourites.remove(facilityDetails);
        }
    }
    private class PlotDirection extends AsyncTask<String, Void, String> {
        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            Log.d("ParserTask", result.toString());
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DirectionsJSONParser parser = new DirectionsJSONParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                currentPolyline = gMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
}