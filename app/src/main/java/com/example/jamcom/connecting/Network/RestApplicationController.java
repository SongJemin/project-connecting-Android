package com.example.jamcom.connecting.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApplicationController {
    public static String BASE_URL ="https://dapi.kakao.com";
    private static Retrofit restRetrofit = null;
    public static Retrofit getRetrofit(){
        if(restRetrofit == null){
            restRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return restRetrofit;
    }
}