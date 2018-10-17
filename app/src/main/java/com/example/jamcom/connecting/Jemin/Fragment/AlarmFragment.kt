package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.AlarmListAdapter
import com.example.jamcom.connecting.Jemin.Item.AlarmListItem
import com.example.jamcom.connecting.Network.Get.GetAlarmListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetAlarmListResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.ApiClient

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class AlarmFragment : Fragment() {

    internal lateinit var myToolbar: Toolbar
    lateinit var alarmListItem: ArrayList<AlarmListItem>
    lateinit var alarmListAdapter : AlarmListAdapter
    lateinit var networkService : NetworkService
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수

    lateinit var alarmlistData : ArrayList<GetAlarmListMessage>
    var userID : Int = 0
    var userName : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_alarm, container, false)
        myToolbar = v.findViewById<View>(R.id.my_toolbar) as Toolbar
        requestManager = Glide.with(this)

        val pref = this.activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        userName = pref.getString("userName","")

        v.alarm_name_tv.text = userName + "님,"

        alarmListItem = ArrayList()
        getAlarmList(v)
        alarmFragment = this

        return v
    }

    // 자신의 알림 리스트 가져오기
    private fun getAlarmList(v : View) {
        try {
            val pref = this.activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getAlarmResponse = networkService.getAlarmList(userID) // 네트워크 서비스의 getContent 함수를 받아옴
            getAlarmResponse.enqueue(object : Callback<GetAlarmListResponse> {
                override fun onResponse(call: Call<GetAlarmListResponse>?, response: Response<GetAlarmListResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        // 알림 리스트 크기 = 0
                        if(response.body()!!.result.size == 0)
                        {

                        }
                        else
                        {
                            alarmlistData = response.body()!!.result
                            var test : String = ""
                            test = alarmlistData.toString()
                            Log.v("TAG","알람리스트 데이터 값"+ test)

                            for(i in 0..alarmlistData.size-1) {
                                if(alarmlistData[i].img_url == ""){
                                    alarmlistData[i].img_url = "http://18.188.54.59:8080/resources/upload/bg_sample.png"
                                }
                                alarmListItem.add(AlarmListItem(alarmlistData[i].img_url, alarmlistData[i].roomName!!, alarmlistData[i].alarmContent!!, alarmlistData[i].roomID))
                                //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                                alarmListAdapter = AlarmListAdapter(context!!, alarmListItem, requestManager)
                            }

                            v.alarm_recyclerview.layoutManager = LinearLayoutManager(context)
                            v.alarm_recyclerview.adapter = alarmListAdapter
                            v.alarm_recyclerview.setNestedScrollingEnabled(false)
                        }


                    }
                }

                override fun onFailure(call: Call<GetAlarmListResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }
    companion object {
        lateinit var alarmFragment : AlarmFragment
        //일종의 스태틱
    }

}