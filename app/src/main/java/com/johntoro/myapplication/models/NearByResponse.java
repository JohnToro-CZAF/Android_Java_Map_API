package com.johntoro.myapplication.models;
import java.io.Serializable;
import java.util.List;
public class NearByResponse implements Serializable {
    private List<Results> results;

    private String[] html_attributions;

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

    @Override
    public String toString() {
        return "ClassPojo [next_page_token, results = " + results + ", html_attributions = " + html_attributions + ", status = " + status + "]";
    }
}
