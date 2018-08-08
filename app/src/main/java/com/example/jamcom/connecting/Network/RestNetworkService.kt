package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.Post.PostDate
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.PostRoom
import com.example.jamcom.connecting.Network.Post.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RestNetworkService {

    @GET("/v2/local/geo/coord2address.json")
    fun getSearch(
            @Header("Authorization") Authorization: String,
            @Query("x") x : String,
            @Query("y") y : String
    ) : Call<GetChangeLocationResponse>


}