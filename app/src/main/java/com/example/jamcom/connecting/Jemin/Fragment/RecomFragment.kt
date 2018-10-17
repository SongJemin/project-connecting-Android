package com.example.jamcom.connecting.Jemin.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Adapter.HeartPlaceAdapter
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.Network.Get.GetHeartPlaceListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetHeartPlaceListResponse
import com.example.jamcom.connecting.Network.Get.Response.GetHotLocationResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.ApiClient

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_recom.*
import kotlinx.android.synthetic.main.fragment_recom.view.*
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

        // 해당 가게 상세 정보 intent 전달
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

    // 약속 인기 장소 리스트 가져오기
    private fun getHotLocation(v : View) {
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getHotLocationResponse = networkService.getHotLocation() // 네트워크 서비스의 getContent 함수를 받아옴

            getHotLocationResponse.enqueue(object : Callback<GetHotLocationResponse> {
                override fun onResponse(call: Call<GetHotLocationResponse>?, response: Response<GetHotLocationResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        v.recom_hot_first_location_tv.text = response!!.body()!!.result[0].confirmedName
                        v.recom_hot_second_location_tv.text = response!!.body()!!.result[1].confirmedName
                        v.recom_hot_third_location_tv.text = response!!.body()!!.result[2].confirmedName
                    }
                }

                override fun onFailure(call: Call<GetHotLocationResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    // 찜 인기 장소 리스트 가져오기
    private fun getHeartPlaceList(v : View) {
        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            heartPlaceListItem = ArrayList()

            var getHeartPlaceListResponse = networkService.getHeartPlaceList() // 네트워크 서비스의 getContent 함수를 받아옴

            getHeartPlaceListResponse.enqueue(object : Callback<GetHeartPlaceListResponse> {
                override fun onResponse(call: Call<GetHeartPlaceListResponse>?, response: Response<GetHeartPlaceListResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        // 데이터 = 0
                        if(response.body()!!.result.size == 0)
                        {
                        }
                        // 데이터 > 0
                        else
                        {
                            heartPlaceListData = response.body()!!.result
                            var test : String = ""
                            test = heartPlaceListData.toString()

                            for(i in 0..heartPlaceListData.size-1) {

                                heartPlaceListItem.add(MyDibsListItem(heartPlaceListData[i].favoriteName, heartPlaceListData[i].favoriteImgUrl!!, heartPlaceListData[i].favoriteAddress))
                                //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                                heartPlaceListAdapter = HeartPlaceAdapter(heartPlaceListItem, requestManager)
                            }

                            heartPlaceListAdapter.setOnItemClickListener(this@RecomFragment)
                            v.recom_heart_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
                            v.recom_heart_list_recyclerview.adapter = heartPlaceListAdapter
                            v.recom_heart_list_recyclerview.setNestedScrollingEnabled(false)

                        }

                    }
                }
                override fun onFailure(call: Call<GetHeartPlaceListResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

}// Required empty public constructor