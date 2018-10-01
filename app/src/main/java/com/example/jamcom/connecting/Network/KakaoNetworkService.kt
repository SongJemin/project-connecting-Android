package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.GetChangeAddressResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoNetworkService {

    // 카카오 로그인
    @GET("/api/signup/check")
    fun getSignupCheck(
            @Query("email") email : String
    ) : Call<GetChangeAddressResponse>
}