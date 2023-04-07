package com.johntoro.myapplication.models;

import android.util.Log;

import java.util.Comparator;
import java.util.List;

public class FilterControl {
    private boolean isActiveHours;
    private boolean isRating;
    private static final String TAG = "FilterControl";

    /**
     * default constructor
     */
    public FilterControl(){
        this.isActiveHours = false;
        this.isRating = false;
    }

    /**
     * FilterControl constructor
     * @param rating if set to true, then List is sorted by rating
     * @param active if set to true, then List is sorted by whether locations are open
     */
    public FilterControl(boolean rating, boolean active){
        this.isActiveHours = active;
        this.isRating = rating;
    }
    // when click on filter by rating, set this to true

    /**
     * activeHours setter
     * @param activeHours sets activeHours to true or false.
     */
    public void setActiveHours(boolean activeHours) {
        this.isActiveHours = activeHours;
    }
    /**
     * activeHours setter
     * @param rating sets rating to true or false.
     */
    public void setRating(boolean rating) {
        this.isRating = rating;
    }

    /**
     * negation of isActiveHours
     */
    public void toggleActiveHours(){
        this.isActiveHours = !this.isActiveHours;
    }
    /**
     * negation of isRating
     */
    public void toggleRating(){
        this.isRating = !this.isRating;
    }

    /**
     * method to sort a List of Results. method will sort the List according to
     * whether isRating or isActiveHours is true.
     *
     * @param nearByPlaces passes a List of Results to be sorted
     * @return returns the sorted list either based on rating or whether the locations are open
     */
    public List<Results> sort(List<Results> nearByPlaces){
        Log.d("FilterControl", "sort: " + nearByPlaces.toString());
        if (isActiveHours){
            nearByPlaces.removeIf(result -> result.getOpeningHours() == null || !result.getOpeningHours().getOpenNow());
            nearByPlaces.sort((r1, r2) -> {
                boolean openNow1 = r1.getOpeningHours().getOpenNow();
                boolean openNow2 = r2.getOpeningHours().getOpenNow();
                if (openNow1 && openNow2) {
                    return 0;
                } else if (openNow1) {
                    return -1;
                } else {
                    return 1;
                }
            });
            isActiveHours = false;
        }
        if (isRating){
            nearByPlaces.sort((r1, r2) -> {
                String rating1 = r1.getRating();
                String rating2 = r2.getRating();
                if (rating1 == null && rating2 == null) {
                    return 0;
                } else if (rating1 == null) {
                    return 1;
                } else if (rating2 == null) {
                    return -1;
                } else {
                    float floatRating1 = Float.parseFloat(rating1);
                    float floatRating2 = Float.parseFloat(rating2);
                    return Float.compare(floatRating2, floatRating1);
                }
            });
            isActiveHours = false;
        }
        Log.d("FilterControl", "sort: " + nearByPlaces.toString());
        return nearByPlaces;
    }

    /**
     * getter for isActiveHours
     * @return boolean value of isActiveHours
     */
    public boolean getActiveHours(){
        return this.isActiveHours;
    }
    /**
     * getter for isRating
     * @return boolean value of isRating
     */
    public boolean getRating(){
        return this.isRating;
    }
}
