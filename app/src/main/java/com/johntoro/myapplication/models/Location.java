/**
 The Location class represents the location of a Geocoding result. It contains the longitude and latitude
 coordinates of the location, and provides methods for getting and setting these values.
 This class implements the Serializable interface, allowing instances of the class to be serialized and
 deserialized.
 */
package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Location implements Serializable {
    /**
     * The longitude of the location.
     */
    @SerializedName("lng")
    private String locationLong;

    /**
     * The latitude of the location.
     */
    @SerializedName("lat")
    private String locationLat;

    /**
     * Returns the longitude of the location.
     *
     * @return the longitude of the location.
     */
    public String getLocationLong() {
        return locationLong;
    }

    /**
     * Sets the longitude of the location.
     *
     * @param locationLong the longitude of the location to set.
     */
    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    /**
     * Returns the latitude of the location.
     *
     * @return the latitude of the location.
     */
    public String getLocationLat() {
        return locationLat;
    }

    /**
     * Sets the latitude of the location.
     *
     * @param locationLat the latitude of the location to set.
     */
    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    /**
     * Returns a LatLng object representing the location.
     *
     * @return a LatLng object representing the location.
     */
    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLong));
    }

    /**
     * Returns a string representation of the Location object, in the format "ClassPojo [lng = longitude, lat = latitude]".
     *
     * @return a string representation of the Location object.
     */
    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [lng = " + locationLong + ", lat = " + locationLat + "]";
    }

}
