package com.johntoro.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.johntoro.myapplication.models.Photos;
import com.johntoro.myapplication.models.Results;
import com.squareup.picasso.Picasso;

public class FacilityDetailsActivity extends AppCompatActivity {
    private static final String TAG = FacilityDetailsActivity.class.getName();
    public static final String FACILITY_DETAILS = "facility_details";
    private ImageView facilityImage;
    private Photos photos;
    private TextView facilityName, facilityRating, facilityAddress, facilityAvailability;
    private RatingBar facilityRatingBar;
    private LinearLayout linearLayoutRating;

    //vars
    private Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_detail);
        Bundle bundle = getIntent().getExtras();
        results = (Results) bundle.getSerializable(FACILITY_DETAILS);
        if (results == null) {
            Log.d(TAG, "onCreate: results is null");
        } else {
            Log.d(TAG, "onCreate: " + results.toString());
        }
        linearLayoutRating = findViewById(R.id.linearLayoutRating);
        facilityName = findViewById(R.id.textViewName);
        facilityRating = findViewById(R.id.textViewRating);
        facilityAddress = findViewById(R.id.textViewAddress);
        facilityAvailability = findViewById(R.id.textViewAvailability);
        facilityRatingBar = findViewById(R.id.ratingBar);
        facilityImage = findViewById(R.id.imageView);
        init();
    }
    private void init() {
        try {
            // get photo
            photos = results.getPhotos()[0];
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&photoreference=%s&key=%s", 400, photos.getPhoto_reference(), BuildConfig.MAPS_API_KEY);
            Log.d("photoUrl", photoUrl);
            Picasso
                    .get()
                    .load(photoUrl)
                    .into(facilityImage);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso
                    .get()
                    .load(R.drawable.ic_error_image)
                    .into(facilityImage);
        }

        facilityName.setText(results.getName());
        facilityAddress.setText(results.getVicinity());
        // check if ratings is available for the place
        if (results.getRating() != null) {
            linearLayoutRating.setVisibility(View.VISIBLE);
            facilityRating.setText(results.getRating());
            facilityRatingBar.setRating(Float.valueOf(results.getRating()));
        }
        // check if opening hours is available
        if (results.getOpeningHours() != null) {
            facilityAvailability.setText(results.getOpeningHours().getOpenNow() == false ? "Close now" : "Open now");
        } else {
            facilityAvailability.setText("Not found!");
        }
//        linearLayoutShowDistanceOnMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
//                intent.putExtra("result", results);
//                intent.putExtra("lat", lat);
//                intent.putExtra("lng", lng);
//                intent.putExtra("type", "distance");
//                startActivity(intent);
//            }
//        });
    }
}
