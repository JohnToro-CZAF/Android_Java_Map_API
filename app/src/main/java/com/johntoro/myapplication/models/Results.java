/**

 A class that represents the search results returned by the Google Places API.
 Each result contains information about a place, such as its name, location, rating, photos, etc.
 */
package com.johntoro.myapplication.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Results implements Serializable {
    /**
     * An array of photo objects that represent the photos of the place.
     */
    @SerializedName("photos")
    private Photos[] photos;

    /**
     * The unique identifier of the place.
     */
    @SerializedName("id")
    private String id;

    /**
     * The unique identifier of the place, which can be used to get details about the place using the Places API.
     */
    @SerializedName("place_id")
    private String place_id;

    /**
     * The URL of the icon that represents the place.
     */
    @SerializedName("icon")
    private String icon;

    /**
     * The address or vicinity of the place.
     */
    @SerializedName("vicinity")
    private String vicinity;

    /**
     * The scope of the place.
     */
    @SerializedName("scope")
    private String scope;

    /**
     * The name of the place.
     */
    @SerializedName("name")
    private String name;

    /**
     * The rating of the place.
     */
    @SerializedName("rating")
    private String rating;

    /**
     * An array of strings representing the types of the place.
     */
    @SerializedName("types")
    private String[] types;

    /**
     * A unique identifier that can be used to retrieve details about the place using the Place Details API.
     */
    @SerializedName("reference")
    private String reference;

    /**
     * The geometry of the place, including its latitude, longitude, and viewport.
     */
    @SerializedName("geometry")
    private Geometry geometry;

    /**
     * The opening hours of the place.
     */
    @SerializedName("opening_hours")
    private OpeningHours openingHours;

    /**
     * Returns the opening hours of the place.
     * @return The opening hours of the place.
     */
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    /**
     * Sets the opening hours of the place.
     * @param openingHours The opening hours of the place.
     */
    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    /**
     * Returns an array of photo objects that represent the photos of the place.
     * @return An array of photo objects that represent the photos of the place.
     */
    public Photos[] getPhotos() {
        return photos;
    }

    /**
     * Sets the photos of the place.
     * @param photos An array of photo objects that represent the photos of the place.
     */
    public void setPhotos(Photos[] photos) {
        this.photos = photos;
    }

    /**
     * Returns the rating of the place.
     * @return The rating of the place.
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the rating of the place.
     * @param rating The rating of the place.
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * Returns the unique identifier of the place.
     * @return The unique identifier of the place.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the place.
     * @param id The unique identifier of the place.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The unique identifier of the place, which can be used to get details about the place using the Places API.
     */
    public String getPlace_id() {
        return place_id;
    }

    /**
     * @param place_id the unique identifier of the place, which can be used to get details about the place using the Places API.
     */
    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    /**
     * @return get the URL of the icon that represents the place.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon set the URL of the icon that represents the place.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**

     Returns the vicinity of the place.
     @return the vicinity of the place.
     */
    public String getVicinity() {
        return vicinity;
    }
    /**

     Sets the vicinity of the place.
     @param vicinity the vicinity of the place.
     */
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    /**

     Returns the scope of the place.
     @return the scope of the place.
     */
    public String getScope() {
        return scope;
    }
    /**

     Sets the scope of the place.
     @param scope the scope of the place.
     */
    public void setScope(String scope) {
        this.scope = scope;
    }
    /**

     Returns the name of the place.
     @return the name of the place.
     */
    public String getName() {
        return name;
    }
    /**

     Sets the name of the place.
     @param name the name of the place.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**

     Returns the types of the place.
     @return the types of the place.
     */
    public String[] getTypes() {
        return types;
    }
    /**

     Sets the types of the place.
     @param types the types of the place.
     */
    public void setTypes(String[] types) {
        this.types = types;
    }
    /**

     Returns the reference of the place.
     @return the reference of the place.
     */
    public String getReference() {
        return reference;
    }
    /**

     Sets the reference of the place.
     @param reference the reference of the place.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
    /**

     Returns the geometry of the place.
     @return the geometry of the place.
     */
    public Geometry getGeometry() {
        return geometry;
    }
    /**

     Sets the geometry of the place.
     @param geometry the geometry of the place.
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    /**

     Returns a string representation of this LocationDetails object.
     @return a string representation of this LocationDetails object.
     */
    @NonNull
    @Override
    public String toString() {
        return "LocationDetails [photos = " + photos + ", id = " + id + ", place_id = " + place_id + ", icon = " + icon + ", vicinity = " + vicinity + ", scope = " + scope + ", name = " + name + ", types = " + types + ", reference = " + reference + ", geometry = " + geometry + "]";
    }
}
