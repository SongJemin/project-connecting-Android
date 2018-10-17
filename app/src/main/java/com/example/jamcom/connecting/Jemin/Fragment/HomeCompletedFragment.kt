package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.RoomInformActivity
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Network.Get.GetHomeListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetHomeListResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.jamcom.connecting.Network.ApiClient
import kotlinx.android.synthetic.main.fragment_completed_home.*
import kotlinx.android.synthetic.main.fragment_completed_home.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeCompletedFragment : Fragment(), View.OnClickListener {

    var homeListItems : java.util.ArrayList<HomeListItem> = java.util.ArrayList()
    lateinit var homelistData : ArrayList<GetHomeListMessage>
    internal lateinit var myToolbar: Toolbar
    var userID : Int = 0
    var roomID : Int = 0
    var dataCount : Int = 0
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수


    override fun onClick(v: View?) {
        val idx : Int = home_completed_list_recyclerview.getChildAdapterPosition(v)
        roomID = homelistData[idx].roomID
        val intent = Intent(getActivity(), RoomInformActivity::class.java)
        intent.putExtra("roomID", roomID)
        startActivity(intent)

    }

    lateinit var networkService : NetworkService
    lateinit var homeListItem: ArrayList<HomeListItem>
    lateinit var homeListAdapter : HomeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_completed_home, container, false)
        //myToolbar = v.findViewById<View>(R.id.my_toolbar) as Toolbar
        requestManager = Glide.with(this)
        HomeProceedingFragment.homeProceedingFragment.homeListFlag = 1 // 완료된 리스트 플래그로 변경

        v.home_completed_data_layout.visibility = View.INVISIBLE
        v.home_completed_nodata_layout.visibility = View.INVISIBLE

        val pref = this.activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        userID = pref.getInt("userID",0)

        homeCompletedFragment = this
        getHomeList(v)

        return v
    }

    // 자신의 약속 방 리스트 가져오기(완료된 약속 방)
    private fun getHomeList(v : View) {

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getHomeLIstResponse = networkService.getHomeCompletedList(userID) // 네트워크 서비스의 getContent 함수를 받아옴
            getHomeLIstResponse.enqueue(object : Callback<GetHomeListResponse> {
                override fun onResponse(call: Call<GetHomeListResponse>?, response: Response<GetHomeListResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        homeListItems.clear()
                        // 결과 = 0
                        if(response.body()!!.result.size == 0)
                        {
                            v.home_completed_nodata_layout.visibility = View.VISIBLE
                        }
                        // 결과 > 0
                        else
                        {
                            v.home_completed_data_layout.visibility = View.VISIBLE
                            homelistData = response.body()!!.result

                            dataCount = homelistData.size
                            for(i in 0..homelistData.size-1) {
                                if(homelistData[i].attendantArr!!.size == 1)
                                {
                                    homelistData[i].attendantArr!!.add("null");
                                    homeListItems.add(HomeListItem(homelistData[i].roomID, homelistData[i].roomName!!, homelistData[i].roomStartDate!!, homelistData[i].roomEndDate!!, homelistData[i].typeName!!, homelistData[i].attendantArr!![0], homelistData[i].attendantArr!![1], homelistData[i].img_url, homelistData[i].confirmedDate, homelistData[i].confirmedName, homelistData[i].roomStatus))
                                }
                                else if(homelistData[i].attendantArr!!.size == 1)
                                {
                                    homeListItems.add(HomeListItem(homelistData[i].roomID, homelistData[i].roomName!!, homelistData[i].roomStartDate!!, homelistData[i].roomEndDate!!, homelistData[i].typeName!!, homelistData[i].attendantArr!![0], homelistData[i].attendantArr!![1], homelistData[i].img_url, homelistData[i].confirmedDate, homelistData[i].confirmedName, homelistData[i].roomStatus))
                                }

                                else{
                                    homeListItems.add(HomeListItem(homelistData[i].roomID, homelistData[i].roomName!!, homelistData[i].roomStartDate!!, homelistData[i].roomEndDate!!, homelistData[i].typeName!!, homelistData[i].attendantArr!![0], homelistData[i].attendantArr!![1], homelistData[i].img_url, homelistData[i].confirmedDate, homelistData[i].confirmedName, homelistData[i].roomStatus))
                                }
                                homeListAdapter = HomeListAdapter(homeListItems, requestManager)
                            }

                            homeListAdapter.setOnItemClickListener(this@HomeCompletedFragment)
                            v.home_completed_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
                            v.home_completed_list_recyclerview.adapter = homeListAdapter
                            v.home_completed_list_recyclerview.setNestedScrollingEnabled(false)
                        }
                    }
                }

                override fun onFailure(call: Call<GetHomeListResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }
    }

    companion object {
        lateinit var homeCompletedFragment : HomeCompletedFragment
        //일종의 스태틱
    }


}