package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.jamcom.connecting.Jemin.Activity.MainActivity
import com.example.jamcom.connecting.Jemin.Activity.MapViewActivity
import com.example.jamcom.connecting.Jemin.Activity.RoomInformActivity
import com.example.jamcom.connecting.Network.Get.*
import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostAlarm
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.PostRelationship
import com.example.jamcom.connecting.Network.Post.Response.*
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.dialog_select_location.view.*
import kotlinx.android.synthetic.main.fragment_room_decide.*
import kotlinx.android.synthetic.main.fragment_room_decide.view.*
import net.daum.mf.map.api.MapView
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RoomDecideTab : Fragment() {

    lateinit var restNetworkService : RestNetworkService
    lateinit var dialog : Dialog
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
    var categoryData : ArrayList<GetCategoryMessage> = ArrayList()
    var roomStatus : Int = 0

    var selectedPosition : Int = 0

    internal lateinit var selectZeroMonth: String
    internal lateinit var selectZeroDay: String

    var confirmedNameValue : String = ""
    var confirmedLatValue : String = ""
    var confirmedLonValue : String = ""
    var confirmedDateValue : String = ""

    var recomFirstPlace : String = ""
    var recomSecondPlace : String = ""
    var recomThirdPlace : String = ""

    var x : String = ""
    var y : String = ""

    // 추천 장소(지하철역) 랭킹 1위 좌표
    var recom_first_x : String = ""
    var recom_first_y : String = ""

    // 추천 장소(지하철역) 랭킹 2위 좌표
    var recom_second_x : String = ""
    var recom_second_y : String = ""

    // 추천 장소(지하철역) 랭킹 3위 좌표
    var recom_third_x : String = ""
    var recom_third_y : String = ""


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

    lateinit var selected_date : String

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
        v.room_decide_proceeding_layout.visibility = View.GONE
        v.room_confirmed_layout.visibility = View.GONE

        getRoomDetail()

        getLocation(v)
        getDate(v)

        v.room_decide_confirm_btn.setOnClickListener {
            showDialog()
        }

        v.room_decide_member_start_location_tv.setOnClickListener {
            var intent = Intent(activity, MapViewActivity::class.java)
            intent.putExtra("polyline_flag", 1)
            intent.putExtra("roomID", roomID)
            intent.putExtra("recomPromiseLat", recomPromiseLat)
            intent.putExtra("recomPromiseLon", recomPromiseLon)
            startActivity(intent)
        }

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

                        x = recomPromiseLon.toString()
                        y = recomPromiseLat.toString()

                        categorySearch()


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
                            v.room_decide_vote1_value_tv.setVisibility(View.INVISIBLE)
                            v.room_decide_vote2_value_tv.setVisibility(View.INVISIBLE)
                            v.room_decide_vote3_value_tv.setVisibility(View.INVISIBLE)
                        }

                        else if(dateData.size == 1)
                        {
                            first_rank_year = dateData[0].promiseDate!!.substring(0,4)
                            first_rank_month = dateData[0].promiseDate!!.substring(5,7)
                            first_rank_day = dateData[0].promiseDate!!.substring(8,10)

                            v.room_decide_week1_tv.setVisibility(View.VISIBLE)
                            v.room_decide_vote2_value_tv.setVisibility(View.INVISIBLE)
                            v.room_decide_vote3_value_tv.setVisibility(View.INVISIBLE)

                            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val date = dateFormat.parse(dateData[0].promiseDate)

                            val cal = Calendar.getInstance()
                            cal.setTime(date)              // 하루더한 날자 값을 Calendar  넣는다.

                            val dayNum = cal.get(Calendar.DAY_OF_WEEK)   // 요일을 구해온다.
                            var convertedDayofWeek = ""

                            when (dayNum) {
                                1 -> convertedDayofWeek = "일요일"
                                2 -> convertedDayofWeek = "월요일"
                                3 -> convertedDayofWeek = "화요일"
                                4 -> convertedDayofWeek = "수요일"
                                5 -> convertedDayofWeek = "목요일"
                                6 -> convertedDayofWeek = "금요일"
                                7 -> convertedDayofWeek = "토요일"
                            }
                            Log.v("TAG", "해당 날짜의 요일은 = " + convertedDayofWeek)

                            room_decide_year1_tv.setText(first_rank_year)
                            room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)
                            room_decide_week1_tv.setText(convertedDayofWeek)

                            room_decide_vote1_value_tv.setText(dateData[0].countValue.toString() + "표")

                        }

                        else if(dateData.size == 2)
                        {
                            first_rank_year = dateData[0].promiseDate!!.substring(0,4)
                            first_rank_month = dateData[0].promiseDate!!.substring(5,7)
                            first_rank_day = dateData[0].promiseDate!!.substring(8,10)

                            second_rank_year = dateData[1].promiseDate!!.substring(0,4)
                            second_rank_month = dateData[1].promiseDate!!.substring(5,7)
                            second_rank_day = dateData[1].promiseDate!!.substring(8,10)

                            v.room_decide_week1_tv.setVisibility(View.VISIBLE)
                            v.room_decide_week2_tv.setVisibility(View.VISIBLE)
                            v.room_decide_vote3_value_tv.setVisibility(View.INVISIBLE)

                            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val date = dateFormat.parse(dateData[0].promiseDate)
                            val date2 = dateFormat.parse(dateData[1].promiseDate)

                            val cal = Calendar.getInstance()
                            val cal2 = Calendar.getInstance()
                            cal.setTime(date)              // 하루더한 날자 값을 Calendar  넣는다.
                            cal2.setTime(date2)              // 하루더한 날자 값을 Calendar  넣는다.

                            val dayNum = cal.get(Calendar.DAY_OF_WEEK)   // 요일을 구해온다.
                            val dayNum2 = cal2.get(Calendar.DAY_OF_WEEK)   // 요일을 구해온다.

                            var convertedDayofWeek = ""
                            var convertedDayofWeek2 = ""

                            when (dayNum) {
                                1 -> convertedDayofWeek = "일요일"
                                2 -> convertedDayofWeek = "월요일"
                                3 -> convertedDayofWeek = "화요일"
                                4 -> convertedDayofWeek = "수요일"
                                5 -> convertedDayofWeek = "목요일"
                                6 -> convertedDayofWeek = "금요일"
                                7 -> convertedDayofWeek = "토요일"
                            }
                            when (dayNum2) {
                                1 -> convertedDayofWeek2 = "일요일"
                                2 -> convertedDayofWeek2 = "월요일"
                                3 -> convertedDayofWeek2 = "화요일"
                                4 -> convertedDayofWeek2 = "수요일"
                                5 -> convertedDayofWeek2 = "목요일"
                                6 -> convertedDayofWeek2 = "금요일"
                                7 -> convertedDayofWeek2 = "토요일"
                            }
                            Log.v("TAG", "해당 날짜의 요일은 = " + convertedDayofWeek)


                            room_decide_year1_tv.setText(first_rank_year)
                            room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)

                            room_decide_year2_tv.setText(second_rank_year)
                            room_decide_month2_tv.setText(second_rank_month + "/" + second_rank_day)

                            room_decide_week1_tv.setText(convertedDayofWeek)
                            room_decide_week2_tv.setText(convertedDayofWeek2)

                            room_decide_vote1_value_tv.setText(dateData[0].countValue.toString() + "표")
                            room_decide_vote2_value_tv.setText(dateData[1].countValue.toString() + "표")
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

                            v.room_decide_week1_tv.setVisibility(View.VISIBLE)
                            v.room_decide_week2_tv.setVisibility(View.VISIBLE)
                            v.room_decide_week3_tv.setVisibility(View.VISIBLE)

                            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val date = dateFormat.parse(dateData[0].promiseDate)
                            val date2 = dateFormat.parse(dateData[1].promiseDate)
                            val date3 = dateFormat.parse(dateData[1].promiseDate)

                            val cal = Calendar.getInstance()
                            val cal2 = Calendar.getInstance()
                            val cal3 = Calendar.getInstance()
                            cal.setTime(date)              // 하루더한 날자 값을 Calendar  넣는다.
                            cal2.setTime(date2)              // 하루더한 날자 값을 Calendar  넣는다.
                            cal3.setTime(date3)              // 하루더한 날자 값을 Calendar  넣는다.

                            val dayNum = cal.get(Calendar.DAY_OF_WEEK)   // 요일을 구해온다.
                            val dayNum2 = cal2.get(Calendar.DAY_OF_WEEK)   // 요일을 구해온다.
                            val dayNum3 = cal3.get(Calendar.DAY_OF_WEEK)   // 요일을 구해온다.

                            var convertedDayofWeek = ""
                            var convertedDayofWeek2 = ""
                            var convertedDayofWeek3 = ""

                            when (dayNum) {
                                1 -> convertedDayofWeek = "일요일"
                                2 -> convertedDayofWeek = "월요일"
                                3 -> convertedDayofWeek = "화요일"
                                4 -> convertedDayofWeek = "수요일"
                                5 -> convertedDayofWeek = "목요일"
                                6 -> convertedDayofWeek = "금요일"
                                7 -> convertedDayofWeek = "토요일"
                            }
                            when (dayNum2) {
                                1 -> convertedDayofWeek2 = "일요일"
                                2 -> convertedDayofWeek2 = "월요일"
                                3 -> convertedDayofWeek2 = "화요일"
                                4 -> convertedDayofWeek2 = "수요일"
                                5 -> convertedDayofWeek2 = "목요일"
                                6 -> convertedDayofWeek2 = "금요일"
                                7 -> convertedDayofWeek2 = "토요일"
                            }
                            when (dayNum3) {
                                1 -> convertedDayofWeek3 = "일요일"
                                2 -> convertedDayofWeek3 = "월요일"
                                3 -> convertedDayofWeek3 = "화요일"
                                4 -> convertedDayofWeek3 = "수요일"
                                5 -> convertedDayofWeek3 = "목요일"
                                6 -> convertedDayofWeek3 = "금요일"
                                7 -> convertedDayofWeek3 = "토요일"
                            }
                            Log.v("TAG", "해당 날짜의 요일은 = " + convertedDayofWeek)

                            if(room_decide_year1_tv == null || room_decide_month1_tv == null || room_decide_year2_tv == null || room_decide_month2_tv == null ||
                                    room_decide_week1_tv == null || room_decide_week2_tv == null || room_decide_week3_tv == null || room_decide_year1_tv == null ||
                                    room_decide_month1_tv == null || room_decide_year2_tv == null || room_decide_month2_tv == null || room_decide_year3_tv == null ||
                                    room_decide_month3_tv == null || room_decide_vote1_value_tv == null || room_decide_vote2_value_tv == null || room_decide_vote3_value_tv == null){

                            }
                            else{
                                room_decide_year1_tv.setText(first_rank_year)
                                room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)

                                room_decide_year2_tv.setText(second_rank_year)
                                room_decide_month2_tv.setText(second_rank_month + "/" + second_rank_day)

                                room_decide_week1_tv.setText(convertedDayofWeek)
                                room_decide_week2_tv.setText(convertedDayofWeek2)
                                room_decide_week3_tv.setText(convertedDayofWeek3)


                                room_decide_year1_tv.setText(first_rank_year)
                                room_decide_month1_tv.setText(first_rank_month + "/" + first_rank_day)

                                room_decide_year2_tv.setText(second_rank_year)
                                room_decide_month2_tv.setText(second_rank_month + "/" + second_rank_day)

                                room_decide_year3_tv.setText(third_rank_year)
                                room_decide_month3_tv.setText(third_rank_month + "/" + third_rank_day)

                                room_decide_vote1_value_tv.setText(dateData[0].countValue.toString() + "표")
                                room_decide_vote2_value_tv.setText(dateData[1].countValue.toString() + "표")
                                room_decide_vote3_value_tv.setText(dateData[2].countValue.toString() + "표")
                            }

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
                        roomStatus = roomDetailData[0].roomStatus

                        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
                        var userID : Int = 0
                        userID = pref.getInt("userID",0)

                        // 아직 진행 중인 방일 경우
                        if(roomStatus == 0){

                            room_decide_proceeding_layout.visibility = View.VISIBLE
                            room_confirmed_layout.visibility = View.GONE
                            if(userID != roomCreaterID)
                            {
                                Log.v("TAG,", "방장이 아님")
                                room_decide_confirm_btn.visibility = View.GONE
                            }

                            Log.v("TAG", "방장 번호 = " + roomCreaterID)
                        }
                        // 확정된 방일 경우
                        else if(roomStatus == 1){
                            room_confirmed_layout.visibility = View.VISIBLE
                            room_decide_proceeding_layout.visibility = View.GONE

                            room_confirmed_date_tv.text = roomDetailData[0].confirmedDate
                            room_confirmed_name_tv.text = roomDetailData[0].confirmedName

                        }



                    }
                }

                override fun onFailure(call: Call<GetRoomDetailRespnose>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    fun categorySearch()
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)

        var category_group_code : String = ""

        var radius : Int = 0

        category_group_code = "SW8"
        radius = 10000

        var getSearchCategory = restNetworkService.getCategorySearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", category_group_code, x, y, radius)
        getSearchCategory.enqueue(object : Callback<GetCategoryResponse> {

            override fun onResponse(call: Call<GetCategoryResponse>?, response: Response<GetCategoryResponse>?) {
                if(response!!.isSuccessful)
                {
                    categoryData = response!!.body()!!.documents

                    if(response!!.body()!!.documents.size == 0)
                    {
                        room_decide_place1_tv.setText("미정")
                        room_decide_place2_tv.setText("미정")
                        room_decide_place3_tv.setText("미정")
                    }

                    else
                    {
                        val splitResult1 = response!!.body()!!.documents[0]!!.place_name!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var count : Int = 0

                        for (sp in splitResult1) {
                            splitResult1[count] = sp
                            count += 1
                        }

                        val splitResult2 = response!!.body()!!.documents[1]!!.place_name!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var count2 : Int = 0

                        for (sp in splitResult2) {
                            splitResult2[count2] = sp
                            count2 += 1
                        }

                        val splitResult3 = response!!.body()!!.documents[2]!!.place_name!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var count3 : Int = 0

                        for (sp in splitResult3) {
                            splitResult3[count3] = sp
                            count3 += 1
                        }

                        recomFirstPlace = splitResult1[0]
                        recomSecondPlace = splitResult2[0]
                        recomThirdPlace = splitResult3[0]

                        Log.v("TAG","카테고리 검색 값 가져오기 성공 : " + response!!.body()!!)
                        if(room_decide_place1_tv == null || room_decide_place2_tv == null || room_decide_place3_tv == null) {

                        }
                        else{
                            room_decide_place1_tv.setText(recomFirstPlace)
                            room_decide_place2_tv.setText(recomSecondPlace)
                            room_decide_place3_tv.setText(recomThirdPlace)
                        }

                    }

                }
                else
                {
                    Log.v("TAG","카테고리 검색 값 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<GetCategoryResponse>?, t: Throwable?) {
                Log.v("TAG","카테고리 서버 통신 실패"+t.toString())
            }

        })

    }

    protected fun showDialog() {
        dialog = Dialog(activity)
        dialog.setCancelable(true)

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_select_location, null)
        dialog.setContentView(view)

        dialog.show()

        view.select_place1_tv.text = recomFirstPlace
        view.select_place2_tv.text = recomSecondPlace
        view.select_place3_tv.text = recomThirdPlace


        view.select_confirm_btn.setOnClickListener {
            confirmedPromise()
        }

        view.select_choice_date_btn.setOnClickListener {

            val pd = DatePickerDialog()

            pd.setOnConfirmDateListener { selectYear, selectMonth, selectDay ->
                var selectDay = selectDay
                if (selectMonth < 10) {
                    selectZeroMonth = "0$selectMonth"
                } else {
                    selectZeroMonth = Integer.toString(selectMonth)
                }
                if (selectDay < 9) {
                    selectDay += 1
                    selectZeroDay = "0$selectDay"
                } else {
                    selectZeroDay = Integer.toString(selectDay + 1)
                }

                this@RoomDecideTab.selected_date = selectYear.toString() + "-" + selectZeroMonth + "-" + selectZeroDay

                confirmedDateValue =  this@RoomDecideTab.selected_date
                view.select_choice_date_btn.text = confirmedDateValue
            }
            pd.show(activity!!.fragmentManager, "DatePickerTest")


        }

        view.select_back_btn.setOnClickListener{
            dialog.dismiss()
        }

        view.select_placesquare1_layout.setOnClickListener{
            view.select_selected_location_tv.text = view.select_place1_tv.text
            confirmedNameValue = view.select_place1_tv.text.toString()
            selectedPosition = 0
            confirmedLatValue = categoryData[selectedPosition].y.toString()
            confirmedLonValue = categoryData[selectedPosition].x.toString()
        }

        view.select_placesquare2_layout.setOnClickListener{
            view.select_selected_location_tv.text = view.select_place2_tv.text
            confirmedNameValue = view.select_place2_tv.text.toString()
            selectedPosition = 1
            confirmedLatValue = categoryData[selectedPosition].y.toString()
            confirmedLonValue = categoryData[selectedPosition].x.toString()
        }

        view.select_placesquare3_layout.setOnClickListener{
            view.select_selected_location_tv.text = view.select_place3_tv.text
            confirmedNameValue = view.select_place3_tv.text.toString()
            selectedPosition = 2
            confirmedLatValue = categoryData[selectedPosition].y.toString()
            confirmedLonValue = categoryData[selectedPosition].x.toString()
        }
    }

    fun confirmedPromise() {

        var roomIDValue : String = ""
        roomIDValue = roomID.toString()
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        val roomID = RequestBody.create(MediaType.parse("text.plain"), roomIDValue)
        val confirmedName = RequestBody.create(MediaType.parse("text.plain"),confirmedNameValue)
        val confirmedLat = RequestBody.create(MediaType.parse("text.plain"),confirmedLatValue)
        val confirmedLon = RequestBody.create(MediaType.parse("text.plain"),confirmedLonValue)
        val confirmedDate = RequestBody.create(MediaType.parse("text.plain"), confirmedDateValue)

        Log.v("confirmed","확정 장소 이름 = " + confirmedNameValue)
        Log.v("confirmed","확정 장소 lat = " + confirmedLatValue)
        Log.v("confirmed","확정 장소 lon = " + confirmedLonValue)
        Log.v("confirmed","확정 날짜 = " + confirmedDateValue)

        val confirmedPromiseResponse = networkService.confirmedPromise(roomID, confirmedName,confirmedLat, confirmedLon, confirmedDate)

        confirmedPromiseResponse.enqueue(object : retrofit2.Callback<ConfirmedPromiseResponse>{

            override fun onResponse(call: Call<ConfirmedPromiseResponse>, response: Response<ConfirmedPromiseResponse>) {
                Log.v("TAG", "약속 확정 통신 성공")
                if(response.isSuccessful){
                    var message = response!!.body()

                    Log.v("TAG", "약속 확정 값 전달 성공"+ message.toString())
                    postAlarm()

                }
                else{

                    Log.v("TAG", "약속 확정 값 전달 실패"+ response!!.body().toString())
                }
            }

            override fun onFailure(call: Call<ConfirmedPromiseResponse>, t: Throwable?) {
            }

        })
    }

    fun postAlarm()
    {
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var postData = PostAlarm(roomID, "최종 약속 날짜, 장소가 정해졌습니다. 약속방에서 확인해주세요!")
        var postAlarmResponse = networkService.postAlarm(postData)
        Log.v("TAG", "알람 생성 통신 전")
        postAlarmResponse.enqueue(object : retrofit2.Callback<PostAlarmResponse>{

            override fun onResponse(call: Call<PostAlarmResponse>, response: Response<PostAlarmResponse>) {
                Log.v("TAG", "알람 생성 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "알람 생성 값 전달 성공")

                    postFcmInvite()

                }
            }

            override fun onFailure(call: Call<PostAlarmResponse>, t: Throwable?) {
            }

        })

    }

    fun postFcmInvite() {
        val pref = activity!!.applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        var flag : Int = 1
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)
        Log.v("TAG", "초대인원 푸시 알림 통신 준비")
        flag = 1
        val postFcmInviteResponse = networkService.postFcmInvite(roomID, userID, flag)

        postFcmInviteResponse.enqueue(object : retrofit2.Callback<PostFcmInviteResponse>{
            override fun onResponse(call: Call<PostFcmInviteResponse>, response: Response<PostFcmInviteResponse>) {
                Log.v("TAG", "초대인원 푸시 알림 통신 성공")
                if(response.isSuccessful){

                    postRelationship()

                }
                else{

                    Log.v("TAG", "초대인원 푸시 알림 전달 실패"+ response!!.body().toString())

                }
            }

            override fun onFailure(call: Call<PostFcmInviteResponse>, t: Throwable?) {
                Log.v("TAG", "초대인원 푸시 알림 전달 실패 : "+ t.toString())

                postRelationship()
                /*
                val intent = Intent(getActivity(), MainActivity::class.java)
                intent.putExtra("userTestFlag",0)
                startActivity(intent)
                */
            }

        })
    }

    fun postRelationship()
    {
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var postData = PostRelationship(roomID)
        var postRelationshipResponse = networkService.postRelationship(postData)
        Log.v("TAG", "연결고리 생성 통신 전")
        postRelationshipResponse.enqueue(object : retrofit2.Callback<PostRelationshipResponse>{

            override fun onResponse(call: Call<PostRelationshipResponse>, response: Response<PostRelationshipResponse>) {
                Log.v("TAG", "연결고리 생성 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "연결고리 생성 값 전달 성공")
                    val intent = Intent(getActivity(), MainActivity::class.java)
                    intent.putExtra("userTestFlag",0)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<PostRelationshipResponse>, t: Throwable?) {
                Toast.makeText(activity,"연결고리 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }

}