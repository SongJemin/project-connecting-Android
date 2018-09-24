package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.Post.*
import com.example.jamcom.connecting.Network.Post.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("/boot/rest/posts/{userID}/homeproceedinglist")
    fun getHomeProceedingList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    @GET("/boot/rest/posts/{userID}/homecompletedlist")
    fun getHomeCompletedList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    @GET("/boot/rest/posts/{userID}/alarmlist")
    fun getAlarmList(
            @Path("userID") userID : Int
    ) : Call<GetAlarmListResponse>

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

    @GET("/boot/rest/posts/{roomID}/choice/{userID}/date")
    fun getMyChoiceDate(
            @Path("roomID") roomID : Int,
            @Path("userID") userID : Int
    ) : Call<GetMyChoiceDateResponse>


    @POST("/boot/rest/posts/postroom")
    fun postRoom(
            @Body postRoom : PostRoom
    ): Call<PostRoomResponse>

    @GET("/boot/rest/posts/lastnumber")
    fun getRoomID(
    ) : Call<GetRoomIDResponse>

    @GET("/boot/rest/posts/userlastnumber")
    fun getUserID(
    ) : Call<GetUserIDResponse>


    @POST("/boot/rest/posts/postpromise")
    fun postPromise(
            @Body postRoom : PostPromise
    ): Call<PostPromiseResponse>

    @POST("/boot/rest/posts/postrelationship")
    fun postRelationship(
            @Body postRelationship : PostRelationship
    ): Call<PostRelationshipResponse>

    @POST("/boot/rest/posts/postdate")
    fun postDate(
            @Body postDate : PostDate
    ) : Call<PostDateResponse>

    @POST("/boot/rest/posts/postalarm")
    fun postAlarm(
            @Body postAlarm : PostAlarm
    ) : Call<PostAlarmResponse>

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
    @POST("boot/rest/posts/updateprofile")
    fun updateProfileImg(
            @Part("userID") userID : RequestBody,
            @Part image : MultipartBody.Part?
    ) : Call<UpdateProfileImgResponse>

    @Multipart
    @POST("boot/rest/posts/updateroom")
    fun updateRoomDate(
            @Part("roomID") roomID : RequestBody,
            @Part("roomStartDate") roomStartDate : RequestBody,
            @Part("roomEndDate") roomEndDate: RequestBody
    ) : Call<UpdateRoomDateResponse>

    @Multipart
    @POST("boot/rest/posts/confirmedPromise")
    fun confirmedPromise(
            @Part("roomID") roomID : RequestBody,
            @Part("confirmedName") confirmedName : RequestBody,
            @Part("confirmedLat") confirmedLat : RequestBody,
            @Part("confirmedLon") confirmedLon : RequestBody,
            @Part("confirmedDate") confirmedDate: RequestBody
    ) : Call<ConfirmedPromiseResponse>

    @Multipart
    @POST("boot/rest/posts/updatelocation")
    fun updateLocation(
            @Part("roomID") roomID : RequestBody,
            @Part("userID") userID : RequestBody,
            @Part("promiseLat") promiseLat: RequestBody,
            @Part("promiseLon") promiseLon: RequestBody
    ) : Call<UpdateLocationResponse>

    @Multipart
    @POST("boot/rest/posts/updatetokenflag")
    fun updatePushTokenFlag(
            @Part("userID") roomID : RequestBody,
            @Part("tokenFlag") userID : RequestBody
    ) : Call<UpdatePushTokenFlagResponse>


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

    @GET("/boot/rest/posts/{userID}/favorite")
    fun getFavoriteList(
            @Path("userID") userID : Int
    ) : Call<GetFavoriteListResponse>

    @GET("/boot/rest/posts/{userID}/relationship")
    fun getConnectingCountList(
            @Path("userID") userID : Int
    ) : Call<GetConnectingCountResponse>

    @GET("/boot/rest/posts/{userID}/tokenFlag")
    fun getTokenFlag(
            @Path("userID") userID : Int
    ) : Call<GetTokenFlagResponse>

    @GET("/boot/rest/posts/{userID}/check/{favoriteName}/favorite")
    fun getFavoriteCheck(
            @Path("userID") userID : Int,
            @Path("favoriteName") favoriteName : String
    ) : Call<GetFavoriteChcekResponse>

    @GET("/boot/rest/posts/usercheck/{user_kakaoID}")
    fun getUserCheck(
            @Path("user_kakaoID") userID : Long
    ) : Call<GetUserCheckResponse>


    @POST("/boot/rest/posts/posttoken")
    fun postToken(
            @Body postToken : PostToken
    ): Call<PostTokenResponse>

    @POST("/boot/rest/posts/fcm/{roomID}/{userID}/{flag}/invite")
    fun postFcmInvite(
            @Path("roomID") roomID : Int,
            @Path("userID") userID : Int,
            @Path("flag") flag : Int
    ) : Call<PostFcmInviteResponse>

    @POST("/boot/rest/posts/postuser")
    fun postUser(
            @Body postUser : PostUser
    ) : Call<PostUserResponse>

}