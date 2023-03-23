package com.johntoro.myapplication.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {
   @SerializedName("lng")
    private String locationLong;
    @SerializedName("lat")
    private String locationLat;

    public String getLocationLong() {
        return locationLong;
    }
    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getLocationLat() {
        return locationLat;
    }
    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(locationLat), Double.parseDouble(locationLong));
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [lng = " + locationLong + ", lat = " + locationLat + "]";
    }
}
