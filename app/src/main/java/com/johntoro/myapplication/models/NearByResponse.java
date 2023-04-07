/**

 The NearByResponse class represents the response received from a Places API Nearby Search request. It contains a
 list of nearby places and the status of the request.
 This class implements the Serializable interface, allowing instances of the class to be serialized and
 deserialized.
 */
package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class NearByResponse implements Serializable {

    /**
     * The list of nearby places returned by the Places API.
     */
    @SerializedName("results")
    private List<Results> results;

    /**
     * An array of HTML attributions to be displayed for the search results.
     */
    @SerializedName("html_attributions")
    private String[] html_attributions;

    /**
     * The status of the Nearby Search request.
     */
    @SerializedName("status")
    private String status;

    /**
     * Returns the list of nearby places returned by the Places API.
     *
     * @return the list of nearby places returned by the Places API.
     */
    public List<Results> getResults() {
        return results;
    }

    /**
     * Sets the list of nearby places returned by the Places API.
     *
     * @param results the list of nearby places returned by the Places API to set.
     */
    public void setResults(List<Results> results) {
        this.results = results;
    }

    /**
     * Returns an array of HTML attributions to be displayed for the search results.
     *
     * @return an array of HTML attributions to be displayed for the search results.
     */
    public String[] getHtml_attributions() {
        return html_attributions;
    }

    /**
     * Sets the array of HTML attributions to be displayed for the search results.
     *
     * @param html_attributions the array of HTML attributions to be displayed for the search results to set.
     */
    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    /**
     * Returns the status of the Nearby Search request.
     *
     * @return the status of the Nearby Search request.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the Nearby Search request.
     *
     * @param status the status of the Nearby Search request to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the NearByResponse object, in the format "ClassPojo [next_page_token, results = [list of results], html_attributions = [array of attributions], status = status]".
     *
     * @return a string representation of the NearByResponse object.
     */
    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [next_page_token, results = " + results + ", html_attributions = " + html_attributions + ", status = " + status + "]";
    }

}
