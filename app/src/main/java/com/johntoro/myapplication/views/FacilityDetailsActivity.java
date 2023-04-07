/**

 This class displays the details of a facility, including its name, rating, address, availability, and an image.
 It also allows the user to navigate to a map showing the distance between the facility and the user's current location.
 The class expects to receive a Results object representing the facility details as a serializable extra in the intent that starts the activity.
 If no rating is available for the facility, the rating UI elements will be hidden.
 If no opening hours information is available for the facility, a warning message will be displayed instead of the availability information.
 If an error occurs while attempting to load the facility image, an error message will be displayed and a default error image will be shown.
 */
package com.johntoro.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.johntoro.myapplication.BuildConfig;
import com.johntoro.myapplication.R;
import com.johntoro.myapplication.models.Photos;
import com.johntoro.myapplication.models.Results;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class FacilityDetailsActivity extends AppCompatActivity {
    //constants
    private static final String TAG = FacilityDetailsActivity.class.getName();
    public static final String FACILITY_DETAILS = "facility_details";
    //UI elements
    private ImageView facilityImage;
    private TextView facilityName, facilityRating, facilityAddress, facilityAvailability;
    private RatingBar facilityRatingBar;
    private LinearLayout linearLayoutRating, linearLayoutShowDistanceOnMap;

    //vars
    private Results results;

    /**
     This method is called when the activity is created.
     It sets up the UI elements and loads the facility details.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detail);

        //get the Results object representing the facility details
        Bundle bundle = getIntent().getExtras();
        results = (Results) bundle.getSerializable(FACILITY_DETAILS);
        Log.d(TAG, results.toString());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //set up the toolbar
        //added to prevent error for null object reference on getGeometry
        myToolbar.setNavigationOnClickListener(v -> {
            Intent replyIntent = new Intent(FacilityDetailsActivity.this, MapsActivity.class);
            replyIntent.putExtra("showDistance", (Serializable) results);
            setResult(RESULT_OK, replyIntent);
            finish();
        });
        //get references to the UI elements
        linearLayoutRating = (LinearLayout) findViewById(R.id.linearLayoutRating);
        facilityName = (TextView) findViewById(R.id.textViewName);
        facilityRating = (TextView) findViewById(R.id.textViewRating);
        facilityAddress = (TextView) findViewById(R.id.textViewAddress);
        facilityAvailability = (TextView) findViewById(R.id.textViewAvailability);
        facilityRatingBar = findViewById(R.id.ratingBar);
        facilityImage = findViewById(R.id.imageView);
        init();
    }

    /**

     This method loads the facility details into the UI elements.
     */
    private void init() {
        try {
            // get photo
            Photos photos = results.getPhotos()[0];
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&photoreference=%s&key=%s", 400, photos.getPhoto_reference(), BuildConfig.MAPS_API_KEY);
            Log.d("photoUrl", photoUrl);
            Picasso
                    .get()
                    .load(photoUrl)
                    .into(facilityImage);
        } catch (Exception e) {
            //display an error message and show a default error image if the facility image cannot be loaded
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso
                    .get()
                    .load(R.drawable.ic_error_image)
                    .into(facilityImage);
        }
        //set the facility name and address
        Log.d(TAG, results.getName());
        facilityName.setText(results.getName());
        facilityAddress.setText(results.getVicinity());
        // check if ratings is available for the place
        if (results.getRating() != null) {
            linearLayoutRating.setVisibility(View.VISIBLE);
            facilityRating.setText(results.getRating());
            facilityRatingBar.setRating(Float.parseFloat(results.getRating()));
        }
        // check if opening hours is available
        if (results.getOpeningHours() != null) {
            facilityAvailability.setText(!results.getOpeningHours().getOpenNow() ? "Close now" : "Open now");
        } else {
            facilityAvailability.setText(R.string.warning_no_opening_hours);
        }
        linearLayoutShowDistanceOnMap = (LinearLayout) findViewById(R.id.linearLayoutShowDistanceOnMap);
        linearLayoutShowDistanceOnMap.setOnClickListener(view -> {
            Intent replyIntent = new Intent(FacilityDetailsActivity.this, MapsActivity.class);
            replyIntent.putExtra("showDistance", (Serializable) results);
            setResult(RESULT_OK, replyIntent);
            finish();
        });
    }
}
