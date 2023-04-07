/**

 A model class representing a Southwest coordinate, containing a longitude and latitude value.
 */
package com.johntoro.myapplication.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Southwest implements Serializable {
    /**
     * The longitude value of the Southwest coordinate.
     */
    @SerializedName("lng")
    private String lng;

    /**
     * The latitude value of the Southwest coordinate.
     */
    @SerializedName("lat")
    private String lat;

    /**
     * Returns the longitude value of the Southwest coordinate.
     * @return The longitude value of the Southwest coordinate.
     */
    public String getLng() {
        return lng;
    }

    /**
     * Sets the longitude value of the Southwest coordinate.
     * @param lng The longitude value to set for the Southwest coordinate.
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * Returns the latitude value of the Southwest coordinate.
     * @return The latitude value of the Southwest coordinate.
     */
    public String getLat() {
        return lat;
    }

    /**
     * Sets the latitude value of the Southwest coordinate.
     * @param lat The latitude value to set for the Southwest coordinate.
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * Returns a string representation of the Southwest object.
     * @return A string representation of the Southwest object.
     */
    @NonNull
    @Override
    public String toString() {
        return "Southwest [lng = " + lng + ", lat = " + lat + "]";
    }
}
