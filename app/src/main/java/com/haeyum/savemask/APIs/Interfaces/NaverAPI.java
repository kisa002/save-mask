package com.haeyum.savemask.APIs.Interfaces;

import com.haeyum.savemask.APIs.Models.Nvaer.ReverseGeocoding.ReverseGeocoding;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface NaverAPI {
    @Headers({
        "X-NCP-APIGW-API-KEY-ID:b7af4mkvhz",
        "X-NCP-APIGW-API-KEY:DXQMbc9SWzc3KHU9sqQc24CzcsgdX1NIS4E3aHpo"
    })
    @GET
    Call<ReverseGeocoding> getReverseGeocoding(@Url String url);
}
