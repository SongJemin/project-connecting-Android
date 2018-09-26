package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.GetWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SKPlanetNetworkService {

    /*
    @GET("/weather/current/minutely")
    fun getWeatherData(
            @Header("appKey") appKey: String,
            @Query("version") version : String,
            @Query("lat") lat : String,
            @Query("lon") lon : String,
            @Query("city") city : String,
            @Query("county") county : String,
            @Query("village") village : String,
            @Query("stnid") stnid : String
    ) : Call<GetWeatherResponse>
*/
    // 단기 예보
    @GET("/weather/forecast/3days")
    fun getWeatherShortData(
            @Header("appKey") appKey: String,
            @Query("version") version : String,
            @Query("lat") lat : String,
            @Query("lon") lon : String,
            @Query("city") city : String,
            @Query("county") county : String,
            @Query("village") village : String,
            @Query("stnid") stnid : String
    ) : Call<GetWeatherResponse>

}