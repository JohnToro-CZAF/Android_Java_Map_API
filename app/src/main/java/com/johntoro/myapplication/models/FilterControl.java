package com.johntoro.myapplication.models;

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
        isActiveHours = activeHours;
    }

    public void setRating(boolean rating) {
        isRating = rating;
    }

    public void sort(List<Results> res){
        if (isActiveHours){
            //res.sort(Comparator.comparing(Results::getOpeningHours));
            isActiveHours = false;
        }
        if (isRating){
            res.sort(Comparator.comparing(Results::getRating));
            isRating = false;
        }
    }

}
