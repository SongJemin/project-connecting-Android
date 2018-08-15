package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.Post.*
import com.example.jamcom.connecting.Network.Post.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("/boot/rest/posts/{userID}/homepromiselist")
    fun getHomeList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    @GET("/boot/rest/posts/{roomID}/member")
    fun getParticipMemberList(
            @Path("roomID") roomID : Int
    ) : Call<GetParticipMemberResponse>

    @GET("/boot/rest/posts/room/{roomID}")
    fun getRoomDetail(
            @Path("roomID") roomID : Int
    ) : Call<GetRoomDetailRespnose>

    @GET("/boot/rest/posts/{roomID}/date")
    fun getDate(
            @Path("roomID") roomID : Int
    ) : Call<GetDateResponse>

    @GET("/boot/rest/posts/location/{roomID}")
    fun getLocation(
            @Path("roomID") roomID : Int
    ) : Call<GetLocationResponse>

    @GET("/boot/rest/posts/{roomID}/choice/{userID}/location")
    fun getMyChoiceLocation(
            @Path("roomID") roomID : Int,
            @Path("userID") userID : Int
    ) : Call<GetMyChoiceLocationResponse>


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

    @POST("/boot/rest/posts/postdate")
    fun postDate(
            @Body postDate : PostDate
    ) : Call<PostDateResponse>


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

    @Multipart
    @POST("boot/rest/posts/updatelocation")
    fun updateLocation(
            @Part("roomID") roomID : RequestBody,
            @Part("userID") userID : RequestBody,
            @Part("promiseLat") promiseLat: RequestBody,
            @Part("promiseLon") promiseLon: RequestBody
    ) : Call<UpdateLocationResponse>

    @POST("boot/rest/posts/deletedate")
    fun deleteDate(
            @Body deleteDate : DeleteDate
    ) : Call<DeleteDateResponse>

    @POST("boot/rest/posts/postfavorite")
    fun postFavorite(
            @Body postFavorite : PostFavorite
    ) : Call<PostFavoriteResponse>

    @POST("boot/rest/posts/deletefavorite")
    fun deleteFavorite(
            @Body deleteFavorite : DeleteFavorite
    ) : Call<DeleteFavoriteResponse>

    @GET("/boot/rest/posts/{userID}/image")
    fun getUserImageUrl(
            @Path("userID") userID : Int
    ) : Call<GetUserImageUrlResponse>

}