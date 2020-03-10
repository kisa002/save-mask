package com.haeyum.savemask.APIs.Interfaces;

import com.haeyum.savemask.APIs.Models.MaskInfo.MaskStores;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 *
 * @author 유광무
 * @email vnycall74@naver.com
 * @git https://github.com/kisa002
 *
 * @version 1.0.0
 **/

public interface MaskAPI {
    @GET
    Call<MaskStores> getMaskStores(@Url String url);
}
