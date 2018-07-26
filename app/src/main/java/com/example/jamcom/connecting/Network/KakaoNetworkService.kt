package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.GetChangeAddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoNetworkService {

    @GET("/api/signup/check")
    fun getSignupCheck(
            @Query("email") email : String
    ) : Call<GetChangeAddressResponse>
}