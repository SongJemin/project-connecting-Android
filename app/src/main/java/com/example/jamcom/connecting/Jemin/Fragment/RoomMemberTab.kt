package com.example.jamcom.connecting.Jemin.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomMemberAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.Network.Get.GetHomeListMessage
import com.example.jamcom.connecting.Network.Get.GetParticipMemberMessage
import com.example.jamcom.connecting.Network.Get.Response.GetHomeListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetParticipMemberResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.util.helper.log.Logger
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_room_member.*
import kotlinx.android.synthetic.main.fragment_room_member.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomMemberTab : Fragment()
{
    lateinit var roomMemberItems: ArrayList<RoomMemberItem>
    lateinit var roomMemberAdapter : RoomMemberAdapter
    lateinit var networkService : NetworkService
    lateinit var memberlistData : ArrayList<GetParticipMemberMessage>
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수
    var roomID : Int = 0
    var roomIDValue : String = ""

    internal var url = "https://cdn-images-1.medium.com/max/2000/1*irPXj5W9eigW-VY7LvYX8Q.jpeg"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_room_member, container, false)
        Log.v("TAG","체크1 = ")

        val extra = arguments
        roomID = extra!!.getInt("roomID")

        Log.v("TAG", "받아온 roomID = " + roomID)
        roomIDValue = roomID.toString()

        requestManager = Glide.with(this)
        getParticipMemberList(v)

        v.room_memeber_invite_btn.setOnClickListener{

            sendLink(roomIDValue)
        }

        return v
    }


    private fun sendLink(roomIDValue : String) {
        val params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("멤버 초대 테스트",
                        url,
                        LinkObject.newBuilder().setWebUrl("")
                                .setMobileWebUrl("").build())
                        .setDescrption("당신은 해당 약속에 초대받으셨습니다." +
                                "약속에 참여해주세요!")
                        .build())

                .addButton(ButtonObject("연결고리 앱으로 열기", LinkObject.newBuilder()
                        //.setWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("roomIDValue="+roomIDValue)
                        .build()))
                .build()

        KakaoLinkService.getInstance().sendDefault(activity, params, object : ResponseCallback<KakaoLinkResponse>() {

            override fun onFailure(errorResult: ErrorResult) {

                Logger.e(errorResult.toString())
            }

            override fun onSuccess(result: KakaoLinkResponse) {}
        })
    }

    private fun getParticipMemberList(v : View) {
        roomMemberItems = ArrayList()
        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getParticipMemberResponse = networkService.getParticipMemberList(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getParticipMemberResponse.enqueue(object : Callback<GetParticipMemberResponse> {
                override fun onResponse(call: Call<GetParticipMemberResponse>?, response: Response<GetParticipMemberResponse>?) {
                    Log.v("TAG","참여 멤버 리스트 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","참여 멤버 리스트 값 갖고오기 성공")
                        memberlistData = response.body()!!.result
                        var test : String = ""
                        test = memberlistData.toString()
                        Log.v("TAG","참여 멤버 리스트 데이터 값"+ test)

                        for(i in 0..memberlistData.size-1) {
                            if(memberlistData[i].userImageUrl == ""){
                                memberlistData[i].userImageUrl = "http://18.188.54.59:8080/resources/upload/bg_sample.png"
                            }
                            roomMemberItems.add(RoomMemberItem(memberlistData[i].userName, memberlistData[i].userImageUrl))
                            //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                            roomMemberAdapter = RoomMemberAdapter(roomMemberItems, requestManager)
                        }

                        v.room_member_recyclerview.layoutManager = LinearLayoutManager(v.context)
                        v.room_member_recyclerview.adapter = roomMemberAdapter

                    }
                }

                override fun onFailure(call: Call<GetParticipMemberResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

}