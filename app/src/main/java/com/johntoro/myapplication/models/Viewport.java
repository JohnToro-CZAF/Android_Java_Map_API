/**

 Viewport represents the viewport of a location on a map.
 It contains the coordinates of the southwest and northeast corners of the area displayed on the map.
 */
package com.johntoro.myapplication.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Viewport implements Serializable {

    private Southwest southwest;

    private Northeast northeast;

    /**
     * Returns the Southwest corner of the map viewport.
     *
     * @return the Southwest corner of the map viewport.
     */
    public Southwest getSouthwest() {
        return southwest;
    }

    /**
     * Sets the Southwest corner of the map viewport.
     *
     * @param southwest the Southwest corner of the map viewport.
     */
    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    /**
     * Returns the Northeast corner of the map viewport.
     *
     * @return the Northeast corner of the map viewport.
     */
    public Northeast getNortheast() {
        return northeast;
    }

    /**
     * Sets the Northeast corner of the map viewport.
     *
     * @param northeast the Northeast corner of the map viewport.
     */
    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    /**
     * Returns a string representation of the Viewport object.
     *
     * @return a string representation of the Viewport object.
     */
    @NonNull
    @Override
    public String toString() {
        return "Viewport [southwest = " + southwest + ", northeast = " + northeast + "]";
    }
}

