package com.haeyum.savemask.APIs.Interfaces;

import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStores;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MaskAPI {
    @GET
    Call<MaskStores> getMaskStores(@Url String url);
}
