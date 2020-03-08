package com.haeyum.savemask.APIs;

import com.haeyum.savemask.APIs.Interfaces.MaskAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetClient {
    public static final String MASK_BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/";

    private static Retrofit retrofitMask;
    private static MaskAPI maskAPI;

    private static Retrofit getService(String baseUrl){
        if (retrofitMask == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15000, TimeUnit.MILLISECONDS)
                    .readTimeout(15000, TimeUnit.MILLISECONDS).build();
            retrofitMask = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitMask;
    }

    public static MaskAPI NetClientNaver() {
        if (maskAPI == null) {
            maskAPI = getService(MASK_BASE_URL).create(MaskAPI.class);
        }

        return maskAPI;
    }
}
