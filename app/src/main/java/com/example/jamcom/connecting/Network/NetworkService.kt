package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.GetHomeListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetRoomDetailRespnose
import com.example.jamcom.connecting.Network.Get.Response.GetRoomIDResponse
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.PostRoom
import com.example.jamcom.connecting.Network.Post.Response.PostPromiseResponse
import com.example.jamcom.connecting.Network.Post.Response.PostRoomResponse
import com.example.jamcom.connecting.Network.Post.Response.PostRoomTestResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdateRoomDateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("/boot/rest/posts/{userID}/homepromiselist")
    fun getHomeList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    @GET("/boot/rest/posts/room/{roomID}")
    fun getRoomDetail(
            @Path("roomID") roomID : Int
    ) : Call<GetRoomDetailRespnose>


    @POST("/boot/rest/posts/postroom")
    fun postRoom(
            @Body postRoom : PostRoom
    ): Call<PostRoomResponse>

    @GET("/boot/rest/posts/lastnumber")
    fun getRoomID(
    ) : Call<GetRoomIDResponse>

    @POST("/boot/rest/posts/postpromise")
    fun postPromise(
            @Body postRoom : PostPromise
    ): Call<PostPromiseResponse>

    @Multipart
    @POST("boot/rest/posts/room")
    fun postRoomTest(
            @Part("roomCreaterID") roomCreaterID : RequestBody,
            @Part("roomName") roomName : RequestBody,
            @Part("roomStartDate") roomStartDate : RequestBody,
            @Part("roomEndDate") roomEndDate: RequestBody,
            @Part("roomTypeID") roomTypeID : RequestBody,
            @Part image : MultipartBody.Part?
    ) : Call<PostRoomTestResponse>

    @Multipart
    @POST("boot/rest/posts/updateroom")
    fun updateRoomDate(
            @Part("roomID") roomID : RequestBody,
            @Part("roomStartDate") roomStartDate : RequestBody,
            @Part("roomEndDate") roomEndDate: RequestBody
    ) : Call<UpdateRoomDateResponse>

    @FormUrlEncoded
    @POST("boot/rest/posts/room")
    fun postRoomTest2(
            @Header("contentType") contentType : String,
            @Field("roomCreaterID") roomCreaterID : String,
            @Field("roomName") roomName : String,
            @Field("roomStartDate") roomStartDate : String,
            @Field("roomEndDate") roomEndDate: String,
            @Field("roomTypeID") roomTypeID : String,
            @Field("image") image : MultipartBody.Part?
    ) : Call<PostRoomTestResponse>
}