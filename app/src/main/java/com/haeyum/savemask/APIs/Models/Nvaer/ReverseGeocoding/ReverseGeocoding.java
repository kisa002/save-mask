package com.haeyum.savemask.APIs.Models.Nvaer.ReverseGeocoding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReverseGeocoding {
    @SerializedName("results")
    private ArrayList<ReverseGeocodingResults> results;

    public ArrayList<ReverseGeocodingResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReverseGeocodingResults> results) {
        this.results = results;
    }
}
