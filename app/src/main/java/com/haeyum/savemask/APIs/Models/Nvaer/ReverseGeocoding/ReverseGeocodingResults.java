package com.haeyum.savemask.APIs.Models.Nvaer.ReverseGeocoding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReverseGeocodingResults {
    @SerializedName("region")
    private ReverseGeocodingRegion region;

    public ReverseGeocodingRegion getRegion() {
        return region;
    }

    public void setRegion(ReverseGeocodingRegion region) {
        this.region = region;
    }
}
