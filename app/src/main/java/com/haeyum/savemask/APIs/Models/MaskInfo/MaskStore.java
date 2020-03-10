package com.haeyum.savemask.APIs.Models.MaskInfo;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author 유광무
 * @email vnycall74@naver.com
 * @git https://github.com/kisa002
 *
 * @version 1.0.0
 **/

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

    @SerializedName("remain_stat")
    private String remain_stat;

    @SerializedName("stock_at")
    private String stock_at;

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

    public String getRemain_stat() {
        return remain_stat;
    }

    public void setRemain_stat(String remain_stat) {
        this.remain_stat = remain_stat;
    }

    public String getStock_at() {
        return stock_at;
    }

    public void setStock_at(String stock_at) {
        this.stock_at = stock_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
