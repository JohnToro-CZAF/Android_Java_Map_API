package com.johntoro.myapplication.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/** The geometry of a Geocoding result. */
public class Geometry implements Serializable {
    @SerializedName("viewport")
    private Viewport viewport;
    @SerializedName("location")
    private Location location;

    public Viewport getViewport() {
        return viewport;
    }
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ClassPojo [viewport = " + viewport + ", location = " + location + "]";
    }
}