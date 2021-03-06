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

    // 좌표 -> 주소 변경 API
    @GET("/v2/local/geo/coord2address.json")
    fun getSearch(
            @Header("Authorization") Authorization: String,
            @Query("x") x : String,
            @Query("y") y : String
    ) : Call<GetChangeLocationResponse>

    // 카테고리 검색 API
    @GET("/v2/local/search/category.json")
    fun getCategorySearch(
            @Header("Authorization") Authorization: String,
            @Query("category_group_code") category_group_code : String,
            @Query("x") x : String,
            @Query("y") y : String,
            @Query("radius") radius : Int
    ) : Call<GetCategoryResponse>

    // 이미지 검색 API
    @GET("/v2/search/image")
    fun getImageSearch(
            @Header("Authorization") Authorization: String,
            @Query("query") query : String
    ) : Call<GetImageSearchResponse>

}