package com.example.jamcom.connecting.Jemin.Fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Network.Get.Response.GetChangeLocationResponse
import com.example.jamcom.connecting.Network.Get.Response.GetMyChoiceLocationResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_room_my_inform.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.fragment_room_my_inform.view.*
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import com.example.jamcom.connecting.Jemin.Activity.MapViewActivity
import com.example.jamcom.connecting.Jemin.Calendar.OneDayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SaturdayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SelectedDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SundayDecorator
import com.example.jamcom.connecting.Network.Get.Response.GetMyChoiceDateResponse
import com.example.jamcom.connecting.Network.Post.DeleteDate
import com.example.jamcom.connecting.Network.Post.PostDate
import com.example.jamcom.connecting.Network.Post.Response.DeleteDateResponse
import com.example.jamcom.connecting.Network.Post.Response.PostDateResponse
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_change_location.view.*
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

    var roomStartDate : String = ""
    var roomEndDate : String = ""

    var rangeStartYear : Int = 0
    var rangeStartMonth : Int = 0
    var rangeStartDay : Int = 0

    var rangeEndYear : Int = 0
    var rangeEndMonth : Int = 0
    var rangeEndDay : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_room_my_inform, container, false)

        val extra = arguments
        roomID = extra!!.getInt("roomID")
        roomStatus = extra!!.getInt("roomStatus")
        roomStartDate = extra!!.getString("roomStartDate")
        roomEndDate = extra!!.getString("roomEndDate")

        rangeStartYear = Integer.parseInt(roomStartDate.substring(0,4))
        rangeStartMonth = Integer.parseInt(roomStartDate.substring(5,7))
        rangeStartDay = Integer.parseInt(roomStartDate.substring(8,10))

        rangeEndYear = Integer.parseInt(roomEndDate.substring(0,4))
        rangeEndMonth = Integer.parseInt(roomEndDate.substring(5,7))
        rangeEndDay = Integer.parseInt(roomEndDate.substring(8,10))

        getMychoiceDate(v)
        getMychoiceLocation()

        v.room_inform_myinform_mcalendarView.selectionColor = Color.parseColor("#7665bf")
        v.room_inform_myinform_mcalendarView.selectedDate = CalendarDay.from(2018, 9, 28)
        v.room_inform_myinform_mcalendarView.selectedDate = CalendarDay.from(2018, 9, 29)

        // 확정된 약속 방이 아닐 경우
        if(roomStatus == 0){
            v.room_inform_myinform_edit1_btn.visibility = View.VISIBLE
            v.room_inform_myinform_edit2_btn.visibility = View.VISIBLE

            // 선호 날짜 변경 버튼 클릭 시
            v.room_inform_myinform_chagne_date_layout.setOnClickListener {
                showDialog()
            }

            // 출발 위치 변경 버튼 클릭 시
            v.room_inform_myinform_change_location_layout.setOnClickListener {
                val intent = Intent(activity, MapViewActivity::class.java)
                modify_flag = 1
                intent.putExtra("modify_flag", modify_flag)
                intent.putExtra("roomID", roomID)
                intent.putExtra("polyline_flag", 0)
                startActivityForResult(intent, 29)
            }
        }
        // 확정된 약속 방일 경우
        if(roomStatus == 1){
            v.room_inform_myinform_edit1_btn.visibility = View.INVISIBLE
            v.room_inform_myinform_edit2_btn.visibility = View.INVISIBLE
        }
        return v
    }

    // 나의 출발 위치 데이터 가져오기
    fun getMychoiceLocation(){

        try {

            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getMyChoiceLcoationResponse = networkService.getMyChoiceLocation(roomID, userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getMyChoiceLcoationResponse.enqueue(object : Callback<GetMyChoiceLocationResponse> {
                override fun onResponse(call: Call<GetMyChoiceLocationResponse>?, response: Response<GetMyChoiceLocationResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        selectedY = response!!.body()!!.result[0].promiseLat!!.toString()
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

    // 나의 선호 날짜리스트 가져오기
    fun getMychoiceDate(v : View){
        try {
            val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getMyChoiceDateResponse = networkService.getMyChoiceDate(roomID, userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getMyChoiceDateResponse.enqueue(object : Callback<GetMyChoiceDateResponse> {
                override fun onResponse(call: Call<GetMyChoiceDateResponse>?, response: Response<GetMyChoiceDateResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        for(i in 0 .. response.body()!!.result.size-1)
                        {
                            selectedDateList.add(response!!.body()!!.result[i].promiseDate!!)
                            var day : CalendarDay = CalendarDay.from(Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(0,4)), Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(5,7))-1, Integer.parseInt(response!!.body()!!.result[i].promiseDate!!.substring(8,10)))
                            selectedCalendarDates.add(day)

                        }
                        v.room_inform_myinform_mcalendarView.addDecorator(SelectedDecorator(selectedCalendarDates))
                        v.room_inform_myinform_mcalendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_NONE

                    }

                }

                override fun onFailure(call: Call<GetMyChoiceDateResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    // 출발 위치 변경
    fun changeLocation()
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)

        var getChangeLocationResponse = restNetworkService.getSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", selectedX, selectedY)
        getChangeLocationResponse.enqueue(object : Callback<GetChangeLocationResponse> {

            override fun onResponse(call: Call<GetChangeLocationResponse>?, response: Response<GetChangeLocationResponse>?) {
                if(response!!.isSuccessful)
                {
                    if(room_inform_myinform_selected_location_tv == null){
                    }
                    else{
                        room_inform_myinform_selected_location_tv.text = response!!.body()!!.documents!![0].address!!.address_name
                    }
                }
                else
                {
                }
            }

            override fun onFailure(call: Call<GetChangeLocationResponse>?, t: Throwable?) {
            }

        })

    }

    // 달력 다이얼로그 실행
    protected fun showDialog() {
        dialog = Dialog(activity)
        dialog.setCancelable(true)

        val view = activity!!.layoutInflater.inflate(R.layout.activity_change_location, null)
        dialog.setContentView(view)

        materialCalendarView = view.findViewById<View>(R.id.change_location_mcalendar) as MaterialCalendarView
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(rangeStartYear, rangeStartMonth-1, rangeStartDay))
                .setMaximumDate(CalendarDay.from(rangeEndYear, rangeEndMonth-1, rangeEndDay))
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

            selectRealDate = selectedYear + "-" + selectedMonth + "-" + selectedDay


            if(preferDateList.contains(selectRealDate)){
                preferDateList.remove(selectRealDate)
            }
            else{
                preferDateList.add(selectRealDate)
            }

            for(i in 0 .. preferDateList.size-1)
            {
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
            // 백버튼 눌렀을 시
            if(backBtnFlag == 1){

            }
            else{
                modifiedLat = data!!.getStringExtra("modifiedLat")
                modifiedLon = data!!.getStringExtra("modifiedLon")
                getMychoiceLocation()
            }


        }
    }

    // 선호 날짜 데이터 삭제
    fun deleteDate()
    {
        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var deleteDate = DeleteDate(roomID, userID)
        var deleteDateResponse = networkService.deleteDate(deleteDate)
        deleteDateResponse.enqueue(object : retrofit2.Callback<DeleteDateResponse>{

            override fun onResponse(call: Call<DeleteDateResponse>, response: Response<DeleteDateResponse>) {
                if(response.isSuccessful){
                    if(preferDateList.size == 0){
                        Toast.makeText(context, "가능 날짜를 선택해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        postDate()
                    }
                }
            }

            override fun onFailure(call: Call<DeleteDateResponse>, t: Throwable?) {
            }

        })

    }

    // 새로운 선호 날짜리스트 전송
    fun postDate()
    {
        val pref = activity!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        var postDate = PostDate(roomID, userID, preferDateList)
        var postDateResponse = networkService.postDate(postDate)
        postDateResponse.enqueue(object : retrofit2.Callback<PostDateResponse>{

            override fun onResponse(call: Call<PostDateResponse>, response: Response<PostDateResponse>) {
                if(response.isSuccessful){
                    dialog.dismiss()
                }
            }
            override fun onFailure(call: Call<PostDateResponse>, t: Throwable?) {
            }

        })

    }

}