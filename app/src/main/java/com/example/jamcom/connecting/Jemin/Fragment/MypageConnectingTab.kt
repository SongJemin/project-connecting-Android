package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_connecting_count.*
import kotlinx.android.synthetic.main.dialog_connecting_friend_layout.view.*
import kotlinx.android.synthetic.main.fragment_connecting_point_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageConnectingTab: Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        val idx : Int = connecting_count_recyclerview.getChildAdapterPosition(v)
        selectedUserImageUrl = connectingListItem[idx].userImageUrl
        selectedUserName = connectingListItem[idx].userName
        selectedUserCount = connectingListItem[idx].connectingCount
        showFriendDialog()

        Log.v("asd", "클릭")
    }
    var selectedUserImageUrl : String = ""
    var selectedUserName : String = ""
    var selectedUserCount : Int = 0

    lateinit var connectingListItem: ArrayList<ConnectingListItem>
    lateinit var networkService : NetworkService
    lateinit var connectingData : ArrayList<GetConnectingCountMessage>
    lateinit var connectingAdapter : ConnectingAdapter
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수
    lateinit var dialog : Dialog
    lateinit var friendDialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_connecting_point_list, container, false)
        requestManager = Glide.with(this)
        v.connecting_list_friend_layout.visibility = View.GONE
        v.connecting_list_nofriend_layout.visibility = View.GONE
        v.connecting_count_recyclerview.setFocusable(false);

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
                            connectingAdapter.setOnItemClickListener(this@MypageConnectingTab)
                            v.connecting_count_recyclerview.layoutManager = LinearLayoutManager(v.context)
                            v.connecting_count_recyclerview.adapter = connectingAdapter
                            v.connecting_count_recyclerview.setNestedScrollingEnabled(false)
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

    // 특정 친구와의 관계 다이얼로그 실행
    protected fun showFriendDialog() {
        friendDialog = Dialog(activity)
        friendDialog.setCancelable(true)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_connecting_friend_layout, null)
        friendDialog.setContentView(view)
        view.friend_dialog_title_tv.text = selectedUserName + "와의 연결고리"
        requestManager.load(selectedUserImageUrl).into(view.dialog_friend_profile_img)
        view.dialog_friend_name_tv.text = selectedUserName
        view.dialog_friend_count_tv.text = selectedUserCount.toString() + "회"

        friendDialog.show()

    }
}