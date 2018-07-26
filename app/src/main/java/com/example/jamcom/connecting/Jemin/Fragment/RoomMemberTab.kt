package com.example.jamcom.connecting.Jemin.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Adapter.RoomMemberAdapter
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
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
import kotlinx.android.synthetic.main.fragment_room_member.*
import kotlinx.android.synthetic.main.fragment_room_member.view.*

class RoomMemberTab : Fragment()
{
    lateinit var roomMemberItems: ArrayList<RoomMemberItem>
    lateinit var roomMemberAdapter : RoomMemberAdapter

    internal var url = "https://cdn-images-1.medium.com/max/2000/1*irPXj5W9eigW-VY7LvYX8Q.jpeg"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_room_member, container, false)
        Log.v("TAG","체크1 = ")
        roomMemberItems = ArrayList()
        roomMemberItems.add(RoomMemberItem(R.drawable.song,"송제민"))
        roomMemberItems.add(RoomMemberItem(R.drawable.eunsu,"조은수"))
        roomMemberItems.add(RoomMemberItem(R.drawable.jihye,"김지혜"))
        roomMemberItems.add(RoomMemberItem(R.drawable.chun,"정해천"))


        Log.v("TAG","체크 = " + roomMemberItems[0].toString())
        Log.v("TAG","체크 = " + roomMemberItems[1].toString())
        Log.v("TAG","체크 = " + roomMemberItems[2].toString())


        roomMemberAdapter = RoomMemberAdapter(roomMemberItems)
        v.room_member_recyclerview.layoutManager = LinearLayoutManager(v.context)
        v.room_member_recyclerview.adapter = roomMemberAdapter

        v.room_memeber_invite_btn.setOnClickListener{

            sendLink()
        }

        return v
    }


    private fun sendLink() {
        val params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("졸업작품 회의",
                        url,
                        LinkObject.newBuilder().setWebUrl("")
                                .setMobileWebUrl("").build())
                        .setDescrption("일하자")
                        .build())
                .addButton(ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build()

        KakaoLinkService.getInstance().sendDefault(activity, params, object : ResponseCallback<KakaoLinkResponse>() {
            override fun onFailure(errorResult: ErrorResult) {
                Logger.e(errorResult.toString())
            }

            override fun onSuccess(result: KakaoLinkResponse) {

            }
        })

    }

}