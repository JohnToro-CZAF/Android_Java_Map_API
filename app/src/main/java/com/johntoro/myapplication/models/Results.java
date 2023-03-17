package com.johntoro.myapplication.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Results implements Serializable {
    @SerializedName("photos")
    private Photos[] photos;
    @SerializedName("id")
    private String id;
    @SerializedName("place_id")
    private String place_id;
    @SerializedName("icon")
    private String icon;
    @SerializedName("vicinity")
    private String vicinity;
    @SerializedName("scope")
    private String scope;
    @SerializedName("name")
    private String name;
    @SerializedName("rating")
    private String rating;
    @SerializedName("types")
    private String[] types;
    @SerializedName("reference")
    private String reference;
    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    public OpeningHours getOpeningHours() {
        return openingHours;
    }
    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public Photos[] getPhotos() {
        return photos;
    }
    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPlace_id() {
        return place_id;
    }
    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String[] getTypes() {
        return types;
    }
    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }

    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public String toString() {
        return "ClassPojo [photos = " + photos + ", id = " + id + ", place_id = " + place_id + ", icon = " + icon + ", vicinity = " + vicinity + ", scope = " + scope + ", name = " + name + ", types = " + types + ", reference = " + reference + ", geometry = " + geometry + "]";
    }
}
