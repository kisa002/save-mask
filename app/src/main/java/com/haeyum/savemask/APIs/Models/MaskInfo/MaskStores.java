package com.haeyum.savemask.APIs.Models.MaskInfo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MaskStores {
    @SerializedName("count")
    private int count;

    @SerializedName("stores")
    private ArrayList<MaskStore> stores;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<MaskStore> getStores() {
        return stores;
    }

    public void setStores(ArrayList<MaskStore> stores) {
        this.stores = stores;
    }
}
