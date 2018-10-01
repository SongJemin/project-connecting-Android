package com.example.jamcom.connecting.Network

import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.Post.*
import com.example.jamcom.connecting.Network.Post.Response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    // 진행 중인 약속 방 리스트 가져오기
    @GET("/boot/rest/posts/{userID}/homeproceedinglist")
    fun getHomeProceedingList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    // 완료된 약속 방 리스트 가져오기
    @GET("/boot/rest/posts/{userID}/homecompletedlist")
    fun getHomeCompletedList(
            @Path("userID") userID : Int
    ) : Call<GetHomeListResponse>

    // 자신의 알림 리스트 가져오기
    @GET("/boot/rest/posts/{userID}/alarmlist")
    fun getAlarmList(
            @Path("userID") userID : Int
    ) : Call<GetAlarmListResponse>

    // 해당 방의 참여 인원 리스트 가져오기
    @GET("/boot/rest/posts/{roomID}/member")
    fun getParticipMemberList(
            @Path("roomID") roomID : Int
    ) : Call<GetParticipMemberResponse>

    // 해당 방의 상세 정보 가져오기
    @GET("/boot/rest/posts/room/{roomID}")
    fun getRoomDetail(
            @Path("roomID") roomID : Int
    ) : Call<GetRoomDetailRespnose>

    // 해당 방의 모든 선호 날짜 데이터 가져오기
    @GET("/boot/rest/posts/{roomID}/date")
    fun getDate(
            @Path("roomID") roomID : Int
    ) : Call<GetDateResponse>

    // 해당 방의 모든 출발 위치 데이터 가져오기
    @GET("/boot/rest/posts/location/{roomID}")
    fun getLocation(
            @Path("roomID") roomID : Int
    ) : Call<GetLocationResponse>

    // 인기 많은 약속 장소 리스트 가져오기
    @GET("/boot/rest/posts/hotlocation")
    fun getHotLocation(
    ) : Call<GetHotLocationResponse>

    // 내가 선택한 해당 방의 출발 위치 데이터 가져오기
    @GET("/boot/rest/posts/{roomID}/choice/{userID}/location")
    fun getMyChoiceLocation(
            @Path("roomID") roomID : Int,
            @Path("userID") userID : Int
    ) : Call<GetMyChoiceLocationResponse>

    // 내가 선택한 해당 방의 선호 날짜 데이터 가져오기
    @GET("/boot/rest/posts/{roomID}/choice/{userID}/date")
    fun getMyChoiceDate(
            @Path("roomID") roomID : Int,
            @Path("userID") userID : Int
    ) : Call<GetMyChoiceDateResponse>

    // 마지막 방 번호 가져오기
    @GET("/boot/rest/posts/lastnumber")
    fun getRoomID(
    ) : Call<GetRoomIDResponse>

    // 마지막 유저 번호 가져오기
    @GET("/boot/rest/posts/userlastnumber")
    fun getUserID(
    ) : Call<GetUserIDResponse>

    // 해당 유저의 프로필 이미지 경로 가져오기
    @GET("/boot/rest/posts/{userID}/image")
    fun getUserImageUrl(
            @Path("userID") userID : Int
    ) : Call<GetUserImageUrlResponse>

    // 자신의 찜리스트 가져오기
    @GET("/boot/rest/posts/{userID}/favorite")
    fun getFavoriteList(
            @Path("userID") userID : Int
    ) : Call<GetFavoriteListResponse>

    // 찜 인기 순위 리스트 가져오기
    @GET("/boot/rest/posts/heartplace")
    fun getHeartPlaceList(
    ) : Call<GetHeartPlaceListResponse>

    // 해당 유저의 연결고리 리스트 가져오기
    @GET("/boot/rest/posts/{userID}/relationship")
    fun getConnectingCountList(
            @Path("userID") userID : Int
    ) : Call<GetConnectingCountResponse>

    // 해당 유저의 푸시 알람 ON/OFF Flag 정보 가져오기
    @GET("/boot/rest/posts/{userID}/tokenFlag")
    fun getTokenFlag(
            @Path("userID") userID : Int
    ) : Call<GetTokenFlagResponse>

    // 유저가 해당 가게를 찜 했는지 알려주는 데이터 가져오기
    @GET("/boot/rest/posts/{userID}/check/{favoriteName}/favorite")
    fun getFavoriteCheck(
            @Path("userID") userID : Int,
            @Path("favoriteName") favoriteName : String
    ) : Call<GetFavoriteChcekResponse>

    // 카카오 유저 중복 검사
    @GET("/boot/rest/posts/usercheck/{user_kakaoID}")
    fun getUserCheck(
            @Path("user_kakaoID") userID : Long
    ) : Call<GetUserCheckResponse>



    // 새로운 약속 생성
    @POST("/boot/rest/posts/postpromise")
    fun postPromise(
            @Body postRoom : PostPromise
    ): Call<PostPromiseResponse>

    // 새로운 연결고리 생성
    @POST("/boot/rest/posts/postrelationship")
    fun postRelationship(
            @Body postRelationship : PostRelationship
    ): Call<PostRelationshipResponse>

    // 새로운 선호 날짜 생성
    @POST("/boot/rest/posts/postdate")
    fun postDate(
            @Body postDate : PostDate
    ) : Call<PostDateResponse>

    // 새로운 알림 생성
    @POST("/boot/rest/posts/postalarm")
    fun postAlarm(
            @Body postAlarm : PostAlarm
    ) : Call<PostAlarmResponse>

    // 새로운 방 생성
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

    // 프로필 이미지 변경
    @Multipart
    @POST("boot/rest/posts/updateprofile")
    fun updateProfileImg(
            @Part("userID") userID : RequestBody,
            @Part image : MultipartBody.Part?
    ) : Call<UpdateProfileImgResponse>

    // 방 정보 변경
    @Multipart
    @POST("boot/rest/posts/updateroom")
    fun updateRoomDate(
            @Part("roomID") roomID : RequestBody,
            @Part("roomStartDate") roomStartDate : RequestBody,
            @Part("roomEndDate") roomEndDate: RequestBody
    ) : Call<UpdateRoomDateResponse>

    // 확정된 약속 정보 전송
    @Multipart
    @POST("boot/rest/posts/confirmedPromise")
    fun confirmedPromise(
            @Part("roomID") roomID : RequestBody,
            @Part("confirmedName") confirmedName : RequestBody,
            @Part("confirmedLat") confirmedLat : RequestBody,
            @Part("confirmedLon") confirmedLon : RequestBody,
            @Part("confirmedDate") confirmedDate: RequestBody,
            @Part("confirmedTime") confirmedTime: RequestBody
    ) : Call<ConfirmedPromiseResponse>

    // 약속 정보 수정
    @Multipart
    @POST("boot/rest/posts/updatelocation")
    fun updateLocation(
            @Part("roomID") roomID : RequestBody,
            @Part("userID") userID : RequestBody,
            @Part("promiseLat") promiseLat: RequestBody,
            @Part("promiseLon") promiseLon: RequestBody
    ) : Call<UpdateLocationResponse>

    // 푸시 알람 ON/OFF Flag 수정
    @Multipart
    @POST("boot/rest/posts/updatetokenflag")
    fun updatePushTokenFlag(
            @Part("userID") roomID : RequestBody,
            @Part("tokenFlag") userID : RequestBody
    ) : Call<UpdatePushTokenFlagResponse>

    // 해당 방의 자신의 선호 날짜 데이터 삭제
    @POST("boot/rest/posts/deletedate")
    fun deleteDate(
            @Body deletePromise : DeleteDate
    ) : Call<DeleteDateResponse>

    // 해당 유저 삭제
    @POST("boot/rest/posts/deleteuser")
    fun deleteUser(
            @Body deleteUser : DeleteUser
    ) : Call<DeleteUserResponse>

    // 해당 약속 삭제
    @POST("boot/rest/posts/deletepromise")
    fun deletePromise(
            @Body deletePromise : DeletePromise
    ) : Call<DeletePromiseResponse>

    // 해당 약속 방 삭제
    @POST("boot/rest/posts/deleteroom")
    fun deleteRoom(
            @Body deleteRoom : DeleteRoom
    ) : Call<DeleteRoomResponse>

    // 찜 정보 추가
    @POST("boot/rest/posts/postfavorite")
    fun postFavorite(
            @Body postFavorite : PostFavorite
    ) : Call<PostFavoriteResponse>

    // 찜 정보 삭제
    @POST("boot/rest/posts/deletefavorite")
    fun deleteFavorite(
            @Body deleteFavorite : DeleteFavorite
    ) : Call<DeleteFavoriteResponse>

    // 새로운 기기의 토큰 값 추가
    @POST("/boot/rest/posts/posttoken")
    fun postToken(
            @Body postToken : PostToken
    ): Call<PostTokenResponse>

    // FCM 푸시 알람 서버 전송
    @POST("/boot/rest/posts/fcm/{roomID}/{userID}/{flag}/invite")
    fun postFcmInvite(
            @Path("roomID") roomID : Int,
            @Path("userID") userID : Int,
            @Path("flag") flag : Int
    ) : Call<PostFcmInviteResponse>

    // 새로운 유저 생성
    @POST("/boot/rest/posts/postuser")
    fun postUser(
            @Body postUser : PostUser
    ) : Call<PostUserResponse>

}