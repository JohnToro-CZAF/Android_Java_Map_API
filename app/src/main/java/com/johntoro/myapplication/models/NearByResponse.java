package com.johntoro.myapplication.models;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class NearByResponse implements Serializable {

    @SerializedName("results")
    private List<Results> results;
    @SerializedName("html_attributions")
    private String[] html_attributions;
    @SerializedName("status")
    private String status;

    public List<Results> getResults() {
        return results;
    }
    public void setResults(List<Results> results) {
        this.results = results;
    }
    public String[] getHtml_attributions() {
        return html_attributions;
    }
    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClassPojo [next_page_token, results = " + results + ", html_attributions = " + html_attributions + ", status = " + status + "]";
    }
}
