package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Adapter.RecomListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomMemberAdapter
import com.example.jamcom.connecting.Jemin.Item.RecomListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.Network.Get.Response.GetHotLocationResponse
import com.example.jamcom.connecting.Network.Get.Response.GetParticipMemberResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R
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
    lateinit var recomListItem: ArrayList<RecomListItem>
    lateinit var recomListAdapter : RecomListAdapter

    override fun onClick(v: View?) {


        // callFragment(RoomInformFragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_recom, container, false)
        // Inflate the layout for this fragment
        getHotLocation(v)

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

}// Required empty public constructor