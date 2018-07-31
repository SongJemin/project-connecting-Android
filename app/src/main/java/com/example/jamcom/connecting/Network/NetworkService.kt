package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.GetChangeAddressResponse
import com.example.jamcom.connecting.Network.Get.Response.GetHomeListResponse
import com.example.jamcom.connecting.Network.Post.PostRoom
import com.example.jamcom.connecting.Network.Post.Response.PostRoomResponse
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("/boot/rest/posts/{userID}/homepromiselist")
    fun getHomeList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    @POST("/boot/rest/posts/postroom")
    fun postRoom(
            @Body postRoom : PostRoom
    ): Call<PostRoomResponse>

}