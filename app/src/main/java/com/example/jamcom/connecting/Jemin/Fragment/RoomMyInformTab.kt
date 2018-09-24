package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Activity.ChangeLocationDialog
import com.example.jamcom.connecting.Network.Get.Response.GetChangeLocationResponse
import com.example.jamcom.connecting.Network.Get.Response.GetMyChoiceLocationResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_room_my_inform.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.fragment_room_my_inform.view.*
import java.nio.file.Files.delete
import android.widget.TextView
import android.R.id.edit
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.support.v7.appcompat.R.attr.colorPrimary
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.jamcom.connecting.Jemin.Activity.MainActivity
import com.example.jamcom.connecting.Jemin.Activity.MapViewActivity
import com.example.jamcom.connecting.Jemin.Activity.RoomViewActivity
import com.example.jamcom.connecting.Jemin.Adapter.ConnectingAdapter
import com.example.jamcom.connecting.Jemin.Calendar.OneDayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SaturdayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SelectedDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SundayDecorator
import com.example.jamcom.connecting.Jemin.Item.ConnectingListItem
import com.example.jamcom.connecting.Network.Get.Response.GetConnectingCountResponse
import com.example.jamcom.connecting.Network.Get.Response.GetMyChoiceDateResponse
import com.example.jamcom.connecting.Network.Post.DeleteDate
import com.example.jamcom.connecting.Network.Post.PostDate
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.Response.DeleteDateResponse
import com.example.jamcom.connecting.Network.Post.Response.PostDateResponse
import com.example.jamcom.connecting.Network.Post.Response.PostPromiseResponse
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_change_location.*
import kotlinx.android.synthetic.main.activity_change_location.view.*
import kotlinx.android.synthetic.main.activity_room_setting.*
import kotlinx.android.synthetic.main.fragment_connecting_point_list.view.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import java.util.*


class RoomMyInformTab : Fragment() {

    lateinit var networkService : NetworkService
    lateinit var restNetworkService : RestNetworkService
    var roomID : Int = 0
    var selectedX : String = ""
    var selectedY : String = ""
    internal lateinit var materialCalendarView: MaterialCalendarView
    lateinit var dialog : Dialog
    var selectDate : String = ""
    var preferDateList  = ArrayList<String>()

    var selectedCalendarDates: ArrayList<CalendarDay> = ArrayList()
    var selectedDateList = ArrayList<String>()


    var modifiedLat : String = ""
    var modifiedLon : String = ""

    var modify_flag : Int = 1
    var selectRealDate : String = ""
    var selectedYear : String = ""
    var selectedMonth : String = ""
    var selectedDay : String = ""
    var selectedMonthValue : Int = 0
    var roomStatus : Int = 0

    var backBtnFlag : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_room_my_inform, container, false)

        val extra = arguments
        roomID = extra!!.getInt("roomID")
        roomStatus = extra!!.getInt("roomStatus")
        getMychoiceDate(v)
        getMychoiceLocation()

        v.room_inform_myinform_mcalendarView.selectionColor = Color.parseColor("#7665bf")
        v.room_inform_myinform_mcalendarView.selectedDate = CalendarDay.from(2018, 9, 28)
        v.room_inform_myinform_mcalendarView.selectedDate = CalendarDay.from(2018, 9, 29)
        //
        //v.room_inform_myinform_mcalendarView.setDateSelected(CalendarDay.from(2018, 9, 28), true);
        //v.room_inform_myinform_mcalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        if(roomStatus == 0){
            v.room_inform_myinform_chagne_date_layout.setOnClickListener {
                showDialog()
            }

            v.room_inform_myinform_change_location_layout.setOnClickListener {
                val intent = Intent(activity, MapViewActivity::class.java)
                modify_flag = 1
                intent.putExtra("modify_flag", modify_flag)
                intent.putExtra("roomID", roomID)
                intent.putExtra("polyline_flag", 0)
                startActivityForResult(intent, 29)

            }
        }

        return v
    }

    fun getMychoiceLocation(){

        try {

            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getMyChoiceLcoationResponse = networkService.getMyChoiceLocation(roomID, userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getMyChoiceLcoationResponse.enqueue(object : Callback<GetMyChoiceLocationResponse> {
                override fun onResponse(call: Call<GetMyChoiceLocationResponse>?, response: Response<GetMyChoiceLocationResponse>?) {
                    Log.v("TAG","출발 위치 정보 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {

                        Log.v("TAG","출발 위치 정보 값 갖고오기 성공 lat = " + response!!.body()!!.result[0].promiseLat)
                        selectedY = response!!.body()!!.result[0].promiseLat!!.toString()
                        Log.v("TAG","출발 위치 정보 값 갖고오기 성공 lon = " + response!!.body()!!.result[0].promiseLon)
                        selectedX = response!!.body()!!.result[0].promiseLon!!.toString()

                        changeLocation()
                    }
                }

                override fun onFailure(call: Call<GetMyChoiceLocationResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    fun getMychoiceDate(v : View){

        try {

            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getMyChoiceDateResponse = networkService.getMyChoiceDate(roomID, userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getMyChoiceDateResponse.enqueue(object : Callback<GetMyChoiceDateResponse> {
                override fun onResponse(call: Call<GetMyChoiceDateResponse>?, response: Response<GetMyChoiceDateResponse>?) {
                    Log.v("TAG","선호 날짜 정보 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        for(i in 0 .. response.body()!!.result.size-1)
                        {
                            Log.v("Asdf", "선호 날짜 [" + i + "]번째 = " + response!!.body()!!.result[i].promiseDate!!)
                            selectedDateList.add(response!!.body()!!.result[i].promiseDate!!)
                            Log.v("Asdf", "년도 = " + Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(0,4)))
                            Log.v("Asdf", "월 = " + Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(5,7)))
                            Log.v("Asdf", "일 = " + Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(8,10)))
                            var day : CalendarDay = CalendarDay.from(Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(0,4)), Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(5,7))-1, Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(8,10)))
                            //v.room_inform_myinform_mcalendarView.selectedDate = day
                            Log.v("asdf","선택 날짜 = " + day.toString())
                            selectedCalendarDates.add(day)

                        }
                        v.room_inform_myinform_mcalendarView.addDecorator(SelectedDecorator(selectedCalendarDates))
                        v.room_inform_myinform_mcalendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_NONE

                    }

                }

                override fun onFailure(call: Call<GetMyChoiceDateResponse>?, t: Throwable?) {
                    Log.v("TAG","선호 날짜 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    fun changeLocation()
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)

        var getChangeLocationResponse = restNetworkService.getSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", selectedX, selectedY)
        getChangeLocationResponse.enqueue(object : Callback<GetChangeLocationResponse> {

            override fun onResponse(call: Call<GetChangeLocationResponse>?, response: Response<GetChangeLocationResponse>?) {
                if(response!!.isSuccessful)
                {
                    if(room_inform_myinform_selected_location_tv == null){
                        Log.v("at","tv is null")
                    }
                    else{
                        room_inform_myinform_selected_location_tv.text = response!!.body()!!.documents!![0].address!!.address_name
                    }
                }
                else
                {
                    Log.v("TAG","레스트 검색 값 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<GetChangeLocationResponse>?, t: Throwable?) {
                Log.v("TAG","검색 통신 실패")
                Log.v("TAG","검색 통신 실패"+t.toString())
            }

        })

    }

    protected fun showDialog() {
        dialog = Dialog(activity)
        dialog.setCancelable(true)

        val view = activity!!.layoutInflater.inflate(R.layout.activity_change_location, null)
        dialog.setContentView(view)

        materialCalendarView = view.findViewById<View>(R.id.change_location_mcalendar) as MaterialCalendarView
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1))
                .setMaximumDate(CalendarDay.from(2030, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()


        materialCalendarView.addDecorators(
                SundayDecorator(),
                SaturdayDecorator(),
                OneDayDecorator(
                ))
        materialCalendarView.selectionColor = Color.parseColor("#7665bf")
        materialCalendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE
        Log.v("Asdf","여기 확인이염")
        materialCalendarView.setDateSelected(CalendarDay.from(2018, 9, 28), true);
        val calendar = Calendar.getInstance()
        calendar.set(2018, 9, 10);


        dialog.show()

        materialCalendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->

            selectDate = date.toString().replace("CalendarDay{","").replace("}", "")

            val splitDate = selectDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var count : Int = 0

            for (sp in splitDate) {
                splitDate[count] = sp
                count += 1
            }

            selectedYear = splitDate[0]
            selectedMonthValue = Integer.parseInt(splitDate[1]) + 1
            selectedMonth = selectedMonthValue.toString()
            selectedDay = splitDate[2]

            Log.v("TAG" , "선택 년 = " + selectedYear)
            Log.v("TAG" , "선택 월 = " + selectedMonth)
            Log.v("TAG" , "선택 일 = " + selectedDay)


            selectRealDate = selectedYear + "-" + selectedMonth + "-" + selectedDay


            if(preferDateList.contains(selectRealDate)){
                preferDateList.remove(selectRealDate)
            }
            else{
                preferDateList.add(selectRealDate)
            }

            Log.v("TAG", "데이터리스트 크기 = " + preferDateList.size )
            for(i in 0 .. preferDateList.size-1)
            {
                Log.v("TAG", "데이터리스트[" + i + "] = " + preferDateList[i] )
            }

        })

        view.chane_location_confirm_btn.setOnClickListener {
            deleteDate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 29){
            backBtnFlag = data!!.getIntExtra("backBtnFlag",0)
            Log.v("TAG","백버튼 플래그 =" + backBtnFlag)

            if(backBtnFlag == 1){

            }
            else{
                modifiedLat = data!!.getStringExtra("modifiedLat")
                modifiedLon = data!!.getStringExtra("modifiedLon")
                Log.v("TAG","수정X 반환 값 ="+ modifiedLat)
                Log.v("TAG","수정Y 반환 값 ="+ modifiedLon)

                getMychoiceLocation()
            }


        }
    }

    fun deleteDate()
    {

        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var deleteDate = DeleteDate(roomID, userID)
        var deleteDateResponse = networkService.deleteDate(deleteDate)
        Log.v("TAG", "날짜 삭제 생성 통신 전")
        deleteDateResponse.enqueue(object : retrofit2.Callback<DeleteDateResponse>{

            override fun onResponse(call: Call<DeleteDateResponse>, response: Response<DeleteDateResponse>) {
                Log.v("TAG", "날짜 삭제 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "날짜 삭제 전달 성공")
                    postDate()
                }
            }

            override fun onFailure(call: Call<DeleteDateResponse>, t: Throwable?) {
            }

        })

    }

    fun postDate()
    {
        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        Log.v("TAG", "선호 날짜 생성시 보내는 값, 방 넘버 = " + roomID)
        Log.v("TAG", "선호 날짜 생성시 보내는 값, 유저 넘버 = " + userID)
        Log.v("TAG", "약속 생성시 보내는 값, 선호 날짜 = " + preferDateList[0])
        var postDate = PostDate(roomID, userID, preferDateList)
        var postDateResponse = networkService.postDate(postDate)
        Log.v("TAG", "날짜 통신 전")
        postDateResponse.enqueue(object : retrofit2.Callback<PostDateResponse>{

            override fun onResponse(call: Call<PostDateResponse>, response: Response<PostDateResponse>) {
                Log.v("TAG", "날짜 수정 후 삽입 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "선호 날짜 값 수정후 삽입 성공")

                    dialog.dismiss()


                }
            }

            override fun onFailure(call: Call<PostDateResponse>, t: Throwable?) {
            }

        })

    }

}