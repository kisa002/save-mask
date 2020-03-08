package com.haeyum.savemask.APIs.Models.MaskInfo;

import com.google.gson.annotations.SerializedName;

public class MaskStore {
    @SerializedName("addr")
    private String addr;

    @SerializedName("code")
    private String code;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lng")
    private Double lng;

    @SerializedName("name")
    private String name;

    @SerializedName("remain_cnt")
    private int remain_cnt;

    @SerializedName("sold_cnt")
    private String sold_cnt;

    @SerializedName("sold_out")
    private Boolean sold_out;

    @SerializedName("stock_cnt")
    private int stock_cnt;

    @SerializedName("stock_t")
    private String stock_t;

    @SerializedName("type")
    private String type;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRemain_cnt() {
        return remain_cnt;
    }

    public void setRemain_cnt(int remain_cnt) {
        this.remain_cnt = remain_cnt;
    }

    public String getSold_cnt() {
        return sold_cnt;
    }

    public void setSold_cnt(String sold_cnt) {
        this.sold_cnt = sold_cnt;
    }

    public Boolean getSold_out() {
        return sold_out;
    }

    public void setSold_out(Boolean sold_out) {
        this.sold_out = sold_out;
    }

    public int getStock_cnt() {
        return stock_cnt;
    }

    public void setStock_cnt(int stock_cnt) {
        this.stock_cnt = stock_cnt;
    }

    public String getStock_t() {
        return stock_t;
    }

    public void setStock_t(String stock_t) {
        this.stock_t = stock_t;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
