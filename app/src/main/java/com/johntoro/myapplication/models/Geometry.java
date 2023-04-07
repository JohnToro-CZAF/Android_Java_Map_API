/**

 A class representing the geometry of a Geocoding result returned by the Google Maps Geocoding API.
 */
package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Geometry implements Serializable {
    @SerializedName("viewport")
    private Viewport viewport;
    @SerializedName("location")
    private Location location;

    /**
     * Returns the viewport of the geometry object.
     *
     * @return The viewport of the geometry object.
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Sets the viewport of the geometry object.
     *
     * @param viewport The viewport of the geometry object.
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Returns the location of the geometry object.
     *
     * @return The location of the geometry object.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the geometry object.
     *
     * @param location The location of the geometry object.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns a string representation of the geometry object.
     *
     * @return A string representation of the geometry object.
     */
    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [viewport = " + viewport + ", location = " + location + "]";
    }

}