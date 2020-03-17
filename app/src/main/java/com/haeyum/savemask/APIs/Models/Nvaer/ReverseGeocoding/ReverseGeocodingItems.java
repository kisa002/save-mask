package com.haeyum.savemask.APIs.Models.Nvaer.ReverseGeocoding;

import com.google.gson.annotations.SerializedName;

public class ReverseGeocodingItems {
    @SerializedName("address")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
