package com.haeyum.savemask.APIs.Models.Nvaer.ReverseGeocoding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReverseGeocodingRegion {
    @SerializedName("area1")
    private ReverseGeocodingArea1 area1;

    @SerializedName("area2")
    private ReverseGeocodingArea2 area2;

    @SerializedName("area3")
    private ReverseGeocodingArea3 area3;

    @SerializedName("area4")
    private ReverseGeocodingArea4 area4;

    public ReverseGeocodingArea1 getArea1() {
        return area1;
    }

    public void setArea1(ReverseGeocodingArea1 area1) {
        this.area1 = area1;
    }

    public ReverseGeocodingArea2 getArea2() {
        return area2;
    }

    public void setArea2(ReverseGeocodingArea2 area2) {
        this.area2 = area2;
    }

    public ReverseGeocodingArea3 getArea3() {
        return area3;
    }

    public void setArea3(ReverseGeocodingArea3 area3) {
        this.area3 = area3;
    }

    public ReverseGeocodingArea4 getArea4() {
        return area4;
    }

    public void setArea4(ReverseGeocodingArea4 area4) {
        this.area4 = area4;
    }
}
