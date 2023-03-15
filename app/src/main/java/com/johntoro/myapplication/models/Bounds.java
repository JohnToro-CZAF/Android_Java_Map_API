package com.johntoro.myapplication.models;

import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;

/** The northeast and southwest points that delineate the outer bounds of a map. */
public class Bounds implements Serializable {

    private static final long serialVersionUID = 1L;
    /** The northeast corner of the bounding box. */
    public LatLng northeast;
    /** The southwest corner of the bounding box. */
    public LatLng southwest;

    @Override
    public String toString() {
        return String.format("[%s, %s]", northeast, southwest);
    }
}
