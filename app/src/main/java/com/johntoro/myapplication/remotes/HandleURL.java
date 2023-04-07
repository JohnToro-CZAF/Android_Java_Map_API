/**

 A utility class for generating the Google Maps Directions API URL based on the given origin and destination LatLng coordinates.
 */
package com.johntoro.myapplication.remotes;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.johntoro.myapplication.BuildConfig;


public class HandleURL extends Object {
    private static final String TAG = HandleURL.class.getSimpleName();
    private LatLng origin, dest;

    /**
     * Constructs a new HandleURL object with the given origin and destination LatLng coordinates.
     * @param origin The origin LatLng coordinate
     * @param dest The destination LatLng coordinate
     */
    public HandleURL(LatLng origin, LatLng dest) {
        this.origin = origin;
        this.dest = dest;
    }

    /**
     * Generates the Google Maps Directions API URL based on the given origin and destination LatLng coordinates.
     * @return The generated URL
     */
    public String getDirectionsUrl() {
        String str_origin = "origin=" + this.origin.latitude + "," + this.origin.longitude;
        String str_dest = "destination=" + this.dest.latitude + "," + this.dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&key=" + BuildConfig.MAPS_API_KEY;
        String output = "json";
        Log.d("finalUrl", "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters);
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

}
