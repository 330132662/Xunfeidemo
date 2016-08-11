package com.czh.xfdemo.utils;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by LiJianfei on 2016/8/4.
 */
public class RetrofitUtil {
    public static <T> T addUrlApi(String baseUrl, final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(service);
    }

}
