package com.johntoro.myapplication.models;

import android.util.Log;

import java.util.Comparator;
import java.util.List;

public class FilterControl {
    private boolean isActiveHours;
    private boolean isRating;
    public FilterControl(){
        this.isActiveHours = false;
        this.isRating = false;
    }
    public FilterControl(boolean rating, boolean active){
        this.isActiveHours = active;
        this.isRating = rating;
    }
    // when click on filter by rating, set this to true
    public void setActiveHours(boolean activeHours) {
        this.isActiveHours = activeHours;
    }
    public void setRating(boolean rating) {
        this.isRating = rating;
    }
    public void toggleActiveHours(){
        this.isActiveHours = !this.isActiveHours;
    }
    public void toggleRating(){
        this.isRating = !this.isRating;
    }

    public List<Results> sort(List<Results> nearByPlaces){
        Log.d("FilterControl", "sort: " + nearByPlaces.toString());
        if (isActiveHours){
            //res.sort(Comparator.comparing(Results::getOpeningHours));
            for (Results r : nearByPlaces){
                if (r.getOpeningHours() == null){
                    nearByPlaces.remove(r);
                }
            }
            isActiveHours = false;
        }
        if (isRating){
            nearByPlaces.sort(Comparator.comparing(Results::getRating));
            isRating = false;
        }
        Log.d("FilterControl", "sort: " + nearByPlaces.toString());
        return nearByPlaces;
    }
    public boolean getActiveHours(){
        return this.isActiveHours;
    }
    public boolean getRating(){
        return this.isRating;
    }
}
