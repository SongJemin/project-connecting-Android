package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.ConnectingAdapter
import com.example.jamcom.connecting.Jemin.Item.ConnectingListItem
import com.example.jamcom.connecting.Network.Get.GetConnectingCountMessage
import com.example.jamcom.connecting.Network.Get.Response.GetConnectingCountResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_connecting_count.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConnectingCountActivity : AppCompatActivity() {
    lateinit var connectingListItem: ArrayList<ConnectingListItem>
    lateinit var networkService : NetworkService
    lateinit var connectingData : ArrayList<GetConnectingCountMessage>
    lateinit var connectingAdapter : ConnectingAdapter
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connecting_count)
        requestManager = Glide.with(this)
        getConnectingCoutnList()
    }

    // 유저별 연결고리 보여주기
    private fun getConnectingCoutnList() {
        connectingListItem = ArrayList()
        try {
            val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getConnectingCountResponse = networkService.getConnectingCountList(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getConnectingCountResponse.enqueue(object : Callback<GetConnectingCountResponse> {
                override fun onResponse(call: Call<GetConnectingCountResponse>?, response: Response<GetConnectingCountResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        connectingData = response.body()!!.result
                        var test : String = ""
                        test = connectingData.toString()

                        for(i in 0..connectingData.size-1) {

                            connectingListItem.add(ConnectingListItem(connectingData[i].userName, connectingData[i].userImageUrl,connectingData[i].connectingCount))
                            //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                            connectingAdapter = ConnectingAdapter(connectingListItem, requestManager)
                        }

                        connecting_count_recyclerview.layoutManager = LinearLayoutManager(this@ConnectingCountActivity)
                        connecting_count_recyclerview.adapter = connectingAdapter

                    }
                }

                override fun onFailure(call: Call<GetConnectingCountResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }
}
