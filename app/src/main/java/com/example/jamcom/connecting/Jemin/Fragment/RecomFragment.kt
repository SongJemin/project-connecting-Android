package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat.canScrollVertically
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Adapter.HeartPlaceAdapter
import com.example.jamcom.connecting.Jemin.Adapter.MyDibsAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RecomListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomMemberAdapter
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.Jemin.Item.RecomListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.Network.Get.GetFavoriteListMessage
import com.example.jamcom.connecting.Network.Get.GetHeartPlaceListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetFavoriteListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetHeartPlaceListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetHotLocationResponse
import com.example.jamcom.connecting.Network.Get.Response.GetParticipMemberResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_favorite_list.*
import kotlinx.android.synthetic.main.fragment_favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_recom.*
import kotlinx.android.synthetic.main.fragment_recom.view.*
import kotlinx.android.synthetic.main.fragment_room_member.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class RecomFragment : Fragment(), View.OnClickListener {

    lateinit var networkService : NetworkService
    lateinit var heartPlaceListItem: ArrayList<MyDibsListItem>
    lateinit var heartPlaceListAdapter : HeartPlaceAdapter

    lateinit var requestManager: RequestManager
    lateinit var heartPlaceListData : ArrayList<GetHeartPlaceListMessage>

    override fun onClick(v: View?) {
        val idx : Int = recom_heart_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","마이페이지 감지 포지션 = " + idx)

        var intent = Intent(activity!!, PlaceDetailActivity::class.java)
        intent.putExtra("selectedPlaceName", heartPlaceListData[idx].favoriteName)
        intent.putExtra("selectedPlaceHomepageUrl", heartPlaceListData[idx].favoriteHomepage)
        intent.putExtra("selectedRoadAddress", heartPlaceListData[idx].favoriteAddress)
        intent.putExtra("selectedPhoneNum", heartPlaceListData[idx].favoritePhone)
        intent.putExtra("selectedX", heartPlaceListData[idx].favoriteLon)
        intent.putExtra("selectedY", heartPlaceListData[idx].favoriteLat)
        intent.putExtra("selectedPlaceImgUrl", heartPlaceListData[idx].favoriteImgUrl)
        intent.putExtra("typeName", heartPlaceListData[idx].favoriteType)

        startActivity(intent)

        // callFragment(RoomInformFragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_recom, container, false)
        // Inflate the layout for this fragment
        requestManager = Glide.with(this)
        getHotLocation(v)

        getHeartPlaceList(v)

        return v
    }

    private fun getHotLocation(v : View) {
        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getHotLocationResponse = networkService.getHotLocation() // 네트워크 서비스의 getContent 함수를 받아옴

            getHotLocationResponse.enqueue(object : Callback<GetHotLocationResponse> {
                override fun onResponse(call: Call<GetHotLocationResponse>?, response: Response<GetHotLocationResponse>?) {
                    Log.v("TAG","참여 멤버 리스트 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","인기 장소 값 갖고오기 성공")

                        v.recom_hot_first_location_tv.text = response!!.body()!!.result[0].confirmedName
                        v.recom_hot_second_location_tv.text = response!!.body()!!.result[1].confirmedName
                        v.recom_hot_third_location_tv.text = response!!.body()!!.result[2].confirmedName
                    }
                }

                override fun onFailure(call: Call<GetHotLocationResponse>?, t: Throwable?) {
                    Log.v("TAG","인기 장소 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }
    private fun getHeartPlaceList(v : View) {

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            heartPlaceListItem = ArrayList()

            var getHeartPlaceListResponse = networkService.getHeartPlaceList() // 네트워크 서비스의 getContent 함수를 받아옴

            getHeartPlaceListResponse.enqueue(object : Callback<GetHeartPlaceListResponse> {
                override fun onResponse(call: Call<GetHeartPlaceListResponse>?, response: Response<GetHeartPlaceListResponse>?) {
                    Log.v("TAG","찜 인기리스트 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","찜 인기리스트 값 갖고오기 성공")

                        if(response.body()!!.result.size == 0)
                        {

                        }
                        else
                        {
                            heartPlaceListData = response.body()!!.result
                            var test : String = ""
                            test = heartPlaceListData.toString()
                            Log.v("TAG","인기 찜리스트 데이터 값"+ test)

                            for(i in 0..heartPlaceListData.size-1) {

                                heartPlaceListItem.add(MyDibsListItem(heartPlaceListData[i].favoriteName, heartPlaceListData[i].favoriteImgUrl!!, heartPlaceListData[i].favoriteAddress))
                                //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                                heartPlaceListAdapter = HeartPlaceAdapter(heartPlaceListItem, requestManager)

                            }

                            heartPlaceListAdapter.setOnItemClickListener(this@RecomFragment)
                            v.recom_heart_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
                            v.recom_heart_list_recyclerview.adapter = heartPlaceListAdapter

                        }

                    }
                }

                override fun onFailure(call: Call<GetHeartPlaceListResponse>?, t: Throwable?) {
                    Log.v("TAG","찜 인기리스트 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

}// Required empty public constructor