package com.johntoro.myapplication.models;
import java.io.Serializable;
import java.util.List;
public class MyPlace implements Serializable {
    private String next_page_token;

    private List<FacilityResults> FacilityResults;

    private String[] html_attributions;

    private String status;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public List<FacilityResults> getFacilityResults() {
        return FacilityResults;
    }

    public void setFacilityResults(List<FacilityResults> FacilityResults) {
        this.FacilityResults = FacilityResults;
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
        return "ClassPojo [next_page_token = " + next_page_token + ", FacilityResults = " + FacilityResults + ", html_attributions = " + html_attributions + ", status = " + status + "]";
    }
}
