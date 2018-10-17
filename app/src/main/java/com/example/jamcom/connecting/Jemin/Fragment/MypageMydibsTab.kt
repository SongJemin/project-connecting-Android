package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Adapter.MyDibsAdapter
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.Network.Get.GetFavoriteListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetFavoriteListResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_favorite_list.*
import kotlinx.android.synthetic.main.fragment_favorite_list.view.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageMydibsTab : Fragment(), View.OnClickListener  {

    lateinit var networkService : NetworkService
    lateinit var mydibsListItem: ArrayList<MyDibsListItem>
    lateinit var myDibsAdapter: MyDibsAdapter

    lateinit var data : Uri
    lateinit var requestManager: RequestManager
    lateinit var favoriteListData : ArrayList<GetFavoriteListMessage>

    override fun onClick(v: View?) {
        val idx : Int = mypage_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","마이페이지 감지 포지션 = " + idx)

        var intent = Intent(activity!!, PlaceDetailActivity::class.java)
        intent.putExtra("selectedPlaceName", favoriteListData[idx].favoriteName)
        intent.putExtra("selectedPlaceHomepageUrl", favoriteListData[idx].favoriteHomepage)
        intent.putExtra("selectedRoadAddress", favoriteListData[idx].favoriteAddress)
        intent.putExtra("selectedPhoneNum", favoriteListData[idx].favoritePhone)
        intent.putExtra("selectedX", favoriteListData[idx].favoriteLon)
        intent.putExtra("selectedY", favoriteListData[idx].favoriteLat)
        intent.putExtra("selectedPlaceImgUrl", favoriteListData[idx].favoriteImgUrl)
        intent.putExtra("typeName", favoriteListData[idx].favoriteType)

        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_favorite_list, container, false)

        requestManager = Glide.with(this)
        v.myfavorite_like_layout.visibility = View.INVISIBLE
        v.myfavorite_nolike_layout.visibility = View.INVISIBLE

        getFavoriteList(v)

        return v
    }

    // 자신의 찜목록 리스트 가져오기
    private fun getFavoriteList(v : View) {

        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)
            mydibsListItem = ArrayList()

            var getFavoriteListResponse = networkService.getFavoriteList(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getFavoriteListResponse.enqueue(object : Callback<GetFavoriteListResponse> {
                override fun onResponse(call: Call<GetFavoriteListResponse>?, response: Response<GetFavoriteListResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        if(response.body()!!.result.size == 0)
                        {
                            v.myfavorite_nolike_layout.visibility = View.VISIBLE
                        }
                        else
                        {
                            v.myfavorite_like_layout.visibility = View.VISIBLE
                            favoriteListData = response.body()!!.result

                            for(i in 0..favoriteListData.size-1) {
                                mydibsListItem.add(MyDibsListItem(favoriteListData[i].favoriteName, favoriteListData[i].favoriteImgUrl!!, favoriteListData[i].favoriteAddress))
                                myDibsAdapter = MyDibsAdapter(mydibsListItem, requestManager)
                            }
                            myDibsAdapter.setOnItemClickListener(this@MypageMydibsTab)
                            v.mypage_list_recyclerview.layoutManager = GridLayoutManager(v.context, 2)
                            v.mypage_list_recyclerview.adapter = myDibsAdapter
                            v.mypage_list_recyclerview.setNestedScrollingEnabled(false)
                        }
                    }
                }
                override fun onFailure(call: Call<GetFavoriteListResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }
}