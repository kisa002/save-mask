package com.haeyum.savemask.APIs;

import com.haeyum.savemask.APIs.Interfaces.MaskAPI;
import com.haeyum.savemask.APIs.Interfaces.NaverAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
*
* @author 유광무
* @email vnycall74@naver.com
* @git https://github.com/kisa002
*
* @version 1.0.0
**/

public class NetClient {
    public static final String MASK_BASE_URL = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/";

    private static Retrofit retrofitMask;
    private static Retrofit retrofitNaver;
    private static MaskAPI maskAPI;
    private static NaverAPI naverAPI;

    private static Retrofit getMaskService(String baseUrl){
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

    public static MaskAPI NetClientMask() {
        if (maskAPI == null) {
            maskAPI = getMaskService(MASK_BASE_URL).create(MaskAPI.class);
        }

        return maskAPI;
    }

    private static Retrofit getNaverService(String baseUrl){
        if (retrofitNaver == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15000, TimeUnit.MILLISECONDS)
                    .readTimeout(15000, TimeUnit.MILLISECONDS).build();
            retrofitNaver = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitNaver;
    }

    public static NaverAPI NetClientNaver() {
        if (naverAPI == null) {
            naverAPI = getMaskService(MASK_BASE_URL).create(NaverAPI.class);
        }

        return naverAPI;
    }
}
