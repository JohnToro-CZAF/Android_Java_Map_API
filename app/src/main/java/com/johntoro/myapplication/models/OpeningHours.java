/**

 Represents the opening hours of a place.
 */
package com.johntoro.myapplication.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OpeningHours implements Serializable {

    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = null;

    /**
     * Returns whether the place is currently open or not.
     * @return True if the place is currently open, false otherwise.
     */
    public Boolean getOpenNow() {
        return openNow;
    }

    /**
     * Sets whether the place is currently open or not.
     * @param openNow True if the place is currently open, false otherwise.
     */
    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    /**
     * Returns a list of strings representing the opening hours for each day of the week.
     * @return A list of strings representing the opening hours for each day of the week.
     */
    public List<Object> getWeekdayText() {
        return weekdayText;
    }

    /**
     * Sets a list of strings representing the opening hours for each day of the week.
     * @param weekdayText A list of strings representing the opening hours for each day of the week.
     */
    public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }

}
