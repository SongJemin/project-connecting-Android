package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.ConnectingAdapter
import com.example.jamcom.connecting.Jemin.Item.ConnectingListItem
import com.example.jamcom.connecting.Network.Get.GetConnectingCountMessage
import com.example.jamcom.connecting.Network.Get.Response.GetConnectingCountResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_connecting_point_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageConnectingTab: Fragment() {
    lateinit var connectingListItem: ArrayList<ConnectingListItem>
    lateinit var networkService : NetworkService
    lateinit var connectingData : ArrayList<GetConnectingCountMessage>
    lateinit var connectingAdapter : ConnectingAdapter
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수
    lateinit var dialog : Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_connecting_point_list, container, false)

        requestManager = Glide.with(this)
        v.connecting_list_friend_layout.visibility = View.GONE
        v.connecting_list_nofriend_layout.visibility = View.GONE

        getConnectingCoutnList(v)

        v.connecting_list_question_layout.setOnClickListener {
            showDialog()
        }

        return v
    }

    // 자신의 연결고리 리스트 가져오기
    private fun getConnectingCoutnList(v : View) {
        connectingListItem = ArrayList()
        try {
            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getConnectingCountResponse = networkService.getConnectingCountList(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getConnectingCountResponse.enqueue(object : Callback<GetConnectingCountResponse> {
                override fun onResponse(call: Call<GetConnectingCountResponse>?, response: Response<GetConnectingCountResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        if(response.body()!!.result.size == 0){
                            v.connecting_list_nofriend_layout.visibility = View.VISIBLE

                        }
                        else{
                            v.connecting_list_friend_layout.visibility = View.VISIBLE
                            connectingData = response.body()!!.result

                            for(i in 0..connectingData.size-1) {

                                connectingListItem.add(ConnectingListItem(connectingData[i].userName, connectingData[i].userImageUrl,connectingData[i].connectingCount))
                                //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                                connectingAdapter = ConnectingAdapter(connectingListItem, requestManager)
                            }

                            v.connecting_count_recyclerview.layoutManager = LinearLayoutManager(v.context)
                            v.connecting_count_recyclerview.adapter = connectingAdapter
                        }

                    }
                }

                override fun onFailure(call: Call<GetConnectingCountResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    // 연결고리 설명 다이얼로그 실행
    protected fun showDialog() {
        dialog = Dialog(activity)
        dialog.setCancelable(true)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_question_layout, null)
        dialog.setContentView(view)

        dialog.show()

    }
}