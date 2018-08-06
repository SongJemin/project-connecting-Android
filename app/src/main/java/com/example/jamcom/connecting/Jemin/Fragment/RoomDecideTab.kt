package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomMemberAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.Network.Get.*
import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_room_inform.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_room_decide.*
import kotlinx.android.synthetic.main.fragment_room_decide.view.*
import kotlinx.android.synthetic.main.fragment_room_member.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDecideTab : Fragment() {

    lateinit var dateData : ArrayList<GetDateMessage>
    lateinit var networkService : NetworkService
    var roomID : Int = 0
    var roomCreaterID : Int = 0
    var roomIDValue : String = ""
    lateinit var locationData : ArrayList<GetLocationMessage>
    var promiseLatSum : Double = 0.0
    var recomPromiseLat : Double = 0.0
    var promiseLonSum : Double = 0.0
    var recomPromiseLon : Double = 0.0
    var count : Int = 0
    var roomDetailData : ArrayList<GetRoomDetailMessage> = ArrayList()


    // 선호 날짜 랭킹 1위
    var first_rank_year : String = ""
    var first_rank_month : String = ""
    var first_rank_day : String = ""

    // 선호 날짜 랭킹 2위
    var second_rank_year : String = ""
    var second_rank_month : String = ""
    var second_rank_day : String = ""

    // 선호 날짜 랭킹 3위
    var third_rank_year : String = ""
    var third_rank_month : String = ""
    var third_rank_day : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_room_decide, container, false)

        val extra = arguments
        roomID = extra!!.getInt("roomID")

        Log.v("TAG", "받아온 roomID = " + roomID)
        roomIDValue = roomID.toString()
        v.room_decide_week1_tv.setVisibility(View.INVISIBLE)
        v.room_decide_week2_tv.setVisibility(View.INVISIBLE)
        v.room_decide_week3_tv.setVisibility(View.INVISIBLE)
        getRoomDetail()

        getLocation(v)
        getDate(v)

        return v
    }


    private fun getLocation(v : View) {
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getLocationResponse = networkService.getLocation(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getLocationResponse.enqueue(object : Callback<GetLocationResponse> {
                override fun onResponse(call: Call<GetLocationResponse>?, response: Response<GetLocationResponse>?) {
                    Log.v("TAG","위도경도 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","위도경도 값 갖고오기 성공")
                        locationData = response.body()!!.result
                        var test : String = ""
                        test = locationData.toString()
                        Log.v("TAG","위도경도 데이터 값"+ test)

                        for(i in 0..locationData.size-1) {
                            promiseLatSum += locationData[i].promiseLat!!
                            promiseLonSum += locationData[i].promiseLon!!
                            count += 1
                            //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                        }

                        recomPromiseLat = promiseLatSum/count
                        recomPromiseLon = promiseLonSum/count
                        Log.v("TAG", "위도 합 = " + promiseLatSum)
                        Log.v("TAG", "경도 합 = " + promiseLonSum)
                        Log.v("TAG", "추천 위도 = " + recomPromiseLat)
                        Log.v("TAG", "추천 경도 = " + recomPromiseLon)

                        room_decide_explain2_tv.setText("추천 위도 : " + recomPromiseLat + ", 경도 : " + recomPromiseLon)
                    }
                }

                override fun onFailure(call: Call<GetLocationResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    private fun getDate(v : View) {
        Log.v("TAG","로그 위치")
        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getDateResponse = networkService.getDate(roomID) // 네트워크 서비스의 getContent 함수를 받아옴
            Log.v("TAG","로그 위치 해당 방 선호날짜 순위 GET 통신 준비")
            getDateResponse.enqueue(object : Callback<GetDateResponse> {
                override fun onResponse(call: Call<GetDateResponse>?, response: Response<GetDateResponse>?) {
                    Log.v("TAG","해당 방 선호날짜 순위 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","해당 방 선호날짜 순위 값 갖고오기 성공")
                        dateData = response.body()!!.result
                        var test : String = ""
                        test = dateData.toString()
                        Log.v("TAG","해당 방 선호날짜 순위 데이터 값"+ test)

                        Log.v("TAG", "방 날짜 데이터 크기 값 = " + dateData.size)

                        if(dateData.size == 0)
                        {

                        }

                        else if(dateData.size == 1)
                        {
                            first_rank_year = dateData[0].promiseDate!!.substring(0,4)
                            first_rank_month = dateData[0].promiseDate!!.substring(5,7)
                            first_rank_day = dateData[0].promiseDate!!.substring(8,10)

                            room_decide_year1_tv.setText(first_rank_year)
                            room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)

                        }

                        else if(dateData.size == 2)
                        {
                            first_rank_year = dateData[0].promiseDate!!.substring(0,4)
                            first_rank_month = dateData[0].promiseDate!!.substring(5,7)
                            first_rank_day = dateData[0].promiseDate!!.substring(8,10)

                            second_rank_year = dateData[1].promiseDate!!.substring(0,4)
                            second_rank_month = dateData[1].promiseDate!!.substring(5,7)
                            second_rank_day = dateData[1].promiseDate!!.substring(8,10)

                            room_decide_year1_tv.setText(first_rank_year)
                            room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)

                            room_decide_year2_tv.setText(second_rank_year)
                            room_decide_month2_tv.setText(second_rank_month + "/" + second_rank_day)

                        }

                        else{

                            first_rank_year = dateData[0].promiseDate!!.substring(0,4)
                            first_rank_month = dateData[0].promiseDate!!.substring(5,7)
                            first_rank_day = dateData[0].promiseDate!!.substring(8,10)

                            second_rank_year = dateData[1].promiseDate!!.substring(0,4)
                            second_rank_month = dateData[1].promiseDate!!.substring(5,7)
                            second_rank_day = dateData[1].promiseDate!!.substring(8,10)

                            third_rank_year = dateData[2].promiseDate!!.substring(0,4)
                            third_rank_month = dateData[2].promiseDate!!.substring(5,7)
                            third_rank_day = dateData[2].promiseDate!!.substring(8,10)

                            room_decide_year1_tv.setText(first_rank_year)
                            room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)

                            room_decide_year2_tv.setText(second_rank_year)
                            room_decide_month2_tv.setText(second_rank_month + "/" + second_rank_day)

                            room_decide_year3_tv.setText(third_rank_year)
                            room_decide_month3_tv.setText(third_rank_month + "/" + third_rank_day)

                        }



                    }
                }

                override fun onFailure(call: Call<GetDateResponse>?, t: Throwable?) {
                    Log.v("TAG","해당 방 선호날짜 순위 값 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }


    fun getRoomDetail(){

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getRoomDetailRespnose = networkService.getRoomDetail(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getRoomDetailRespnose.enqueue(object : Callback<GetRoomDetailRespnose> {
                override fun onResponse(call: Call<GetRoomDetailRespnose>?, response: Response<GetRoomDetailRespnose>?) {
                    Log.v("TAG","방 세부사항 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","방 세부사항 값 갖고오기 성공")
                        roomDetailData = response.body()!!.result

                        roomCreaterID = roomDetailData[0].roomCreaterID


                        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
                        var userID : Int = 0
                        userID = pref.getInt("userID",0)

                        if(userID != roomCreaterID)
                        {
                            Log.v("TAG,", "방장이 아님")
                            room_decide_confirm_btn.visibility = View.GONE
                        }



                        Log.v("TAG", "방장 번호 = " + roomCreaterID)

                    }
                }

                override fun onFailure(call: Call<GetRoomDetailRespnose>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }



}