/**

 A class representing the northeast location of a geometry object returned by the Google Maps Geocoding API.
 */
package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Northeast implements Serializable {
    @SerializedName("lng")
    private String lng;
    @SerializedName("lat")
    private String lat;

    /**
     * Returns the longitude of the northeast location.
     *
     * @return The longitude of the northeast location.
     */
    public String getLng() {
        return lng;
    }

    /**
     * Sets the longitude of the northeast location.
     *
     * @param lng The longitude of the northeast location.
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * Returns the latitude of the northeast location.
     *
     * @return The latitude of the northeast location.
     */
    public String getLat() {
        return lat;
    }

    /**
     * Sets the latitude of the northeast location.
     *
     * @param lat The latitude of the northeast location.
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * Returns a string representation of the northeast object.
     *
     * @return A string representation of the northeast object.
     */
    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [lng = " + lng + ", lat = " + lat + "]";
    }

}
