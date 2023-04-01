package com.johntoro.myapplication.models;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterControl {
    private boolean isActiveHours;
    private boolean isRating;
    private static final String TAG = "FilterControl";
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
        Log.d(TAG, String.valueOf(res));
        if (isActiveHours){
            //res.sort(Comparator.comparing(Results::getOpeningHours));
            isActiveHours = false;
        }
        if (isRating){
            res.sort((r1, r2) -> {
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
            isRating = false;
        }
    }

}
