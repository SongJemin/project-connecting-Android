package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jamcom.connecting.Jemin.Calendar.OneDayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SaturdayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SundayDecorator
import com.example.jamcom.connecting.Network.Get.GetRoomDetailMessage
import com.example.jamcom.connecting.Network.Get.Response.GetChangeLocationResponse
import com.example.jamcom.connecting.Network.Get.Response.GetRoomDetailRespnose
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostAlarm
import com.example.jamcom.connecting.Network.Post.PostDate
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.Response.*
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_room_setting.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class RoomSettingActivity : AppCompatActivity() {

    internal lateinit var materialCalendarView: MaterialCalendarView
    internal lateinit var startZeroMonth: String
    internal lateinit var startZeroDay: String
    internal lateinit var finishZeroMonth: String
    internal lateinit var finishZeroDay: String
    lateinit var start_date: String
    lateinit var end_date: String
    lateinit var networkService : NetworkService
    lateinit var restNetworkService : RestNetworkService
    var roomDetailData : ArrayList<GetRoomDetailMessage> = ArrayList()
    var preferDateList  = ArrayList<String>()

    var rangeStartYear : Int = 0
    var rangeStartMonth : Int = 0
    var rangeStartDay : Int = 0

    var rangeEndYear : Int = 0
    var rangeEndMonth : Int = 0
    var rangeEndDay : Int = 0


    var backBtnFlag : Int = 0
    var roomID : Int = 0
    var roomName : String = ""
    var modify_flag : Int = 0
    var roomStartDate : String = ""
    var roomEndDate : String = ""
    var selectDate : String = ""
    var selectRealDate : String = ""

    var x : String = ""
    var y : String = ""

    var promiseLat : Double = 0.0
    var promiseLon : Double = 0.0

    var selectedYear : String = ""
    var selectedMonth : String = ""
    var selectedDay : String = ""
    var selectedMonthValue : Int = 0
    var flag : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_setting)

        // 추가된 소스, Toolbar를 생성한다.
        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.parseColor("#FFFFFF")
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            window.statusBarColor = Color.BLACK
        }

        roomSettingActivity = this
        flag = intent.getIntExtra("flag", 0)
        materialCalendarView = findViewById<View>(R.id.m_calendarView) as MaterialCalendarView

        // 카카오톡 초대로 입장 시
        if(flag == 1){
            Log.v("asdf", "카톡으로 룸세팅 들어옴")
            roomID = intent.getIntExtra("roomID", 0)
            room_setting_location_selected_btn.setVisibility(View.GONE)
            room_setting_modify_btn.setVisibility(View.GONE)
            materialCalendarView.visibility = View.VISIBLE
            getRoomDetail()
        }
        // 일반적으로 입장 시
        else if(flag == 0){
            roomID = intent.getIntExtra("roomID", 0)
            room_setting_location_selected_btn.setVisibility(View.GONE)
            room_setting_modify_btn.setVisibility(View.GONE)
            room_setting_range_tv.visibility = View.GONE
            room_setting_range_select_layout.visibility = View.INVISIBLE
            materialCalendarView.visibility = View.INVISIBLE
        }

        // LocationManager 객체를 얻어온다
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        materialCalendarView.selectionColor = Color.parseColor("#7665bf")
        materialCalendarView.addDecorators(
                SundayDecorator(),
                SaturdayDecorator(),
                OneDayDecorator())
        materialCalendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE

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

        })

        // '현재 위치' 버튼 클릭 시
        room_setting_current_btn.setOnClickListener {
            try {
                var currentLocation: Location? = null
                    room_setting_location_selected_btn.setVisibility(View.VISIBLE)
                    room_setting_current_btn.setVisibility(View.GONE)
                    room_setting_map_btn.setVisibility(View.GONE)
                room_setting_modify_btn.setVisibility(View.VISIBLE)
                    // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!

                val locationProvider = LocationManager.GPS_PROVIDER

                // 수동으로 위치 구하기
                currentLocation = lm.getLastKnownLocation(locationProvider)
                if (currentLocation != null) {
                    promiseLat = currentLocation.getLatitude()
                    promiseLon = currentLocation.getLongitude()

                    lm.removeUpdates(mLocationListener)  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    x = promiseLon.toString()
                    y = promiseLat.toString()
                    changeLocation()
                }


            } catch (ex: SecurityException) {
            }
        }

        // 위치 '수정' 버튼 클릭 시
        room_setting_modify_btn.setOnClickListener {
            room_setting_location_selected_btn.setVisibility(View.GONE)
            room_setting_current_btn.setVisibility(View.VISIBLE)
            room_setting_map_btn.setVisibility(View.VISIBLE)
            room_setting_modify_btn.setVisibility(View.GONE)
        }

        // '약속 가능 날짜 범위' 버튼 클릭 시
        room_setting_range_btn.setOnClickListener {
            val pd = MyYearMonthPickerDialog()
            pd.setOnConfirmDateListener { startYear, startMonth, startDay, finishYear, finishMonth, finishDay ->
                var startDay = startDay
                var finishDay = finishDay
                if (startMonth < 10) {
                    startZeroMonth = "0$startMonth"
                } else {
                    startZeroMonth = Integer.toString(startMonth)
                }
                if (startDay < 9) {
                    startDay += 1
                    startZeroDay = "0$startDay"
                } else {
                    startZeroDay = Integer.toString(startDay + 1)
                }
                if (finishMonth < 10) {
                    finishZeroMonth = "0$finishMonth"
                } else {
                    finishZeroMonth = Integer.toString(finishMonth)
                }
                if (finishDay < 9) {
                    finishDay += 1
                    finishZeroDay = "0$finishDay"
                } else {
                    finishZeroDay = Integer.toString(finishDay + 1)
                }

                this@RoomSettingActivity.start_date = startYear.toString() + "-" + startZeroMonth + "-" + startZeroDay
                this@RoomSettingActivity.end_date = finishYear.toString() + "-" + finishZeroMonth + "-" + finishZeroDay

                roomStartDate =  this@RoomSettingActivity.start_date
                roomEndDate = this@RoomSettingActivity.end_date
                room_setting_range_select_layout.visibility = View.VISIBLE
                materialCalendarView.visibility = View.VISIBLE
                materialCalendarView.state().edit()
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setMinimumDate(CalendarDay.from(startYear, startMonth-1, startDay))
                        .setMaximumDate(CalendarDay.from(finishYear, finishMonth-1, finishDay))
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit()
                room_setting_range_btn.text = roomStartDate + " ~ " + roomEndDate
            }
            pd.show(fragmentManager, "YearMonthPickerTest")
        }

        // '지도에서 선택하기' 버튼 클릭 시
        room_setting_map_btn.setOnClickListener {
            modify_flag = 0
            val intent = Intent(applicationContext, MapViewActivity::class.java)
            intent.putExtra("roomID", roomID)
            intent.putExtra("modify_flag", modify_flag)
            intent.putExtra("polyline_flag", 0)
            startActivityForResult(intent, 28)
        }

        // '확인' 버튼 클릭 시
        room_setting_confirm_btn.setOnClickListener{
            // 방 생성했을 때
            if(flag==0)
            {
                if(roomStartDate == "" || roomEndDate == "" || promiseLat == 0.0 || promiseLon == 0.0 || preferDateList.size == 0)
                {
                    if(roomStartDate == "" || roomEndDate == "")
                    {
                        Toast.makeText(applicationContext, "약속 날짜 범위를 정해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else if(promiseLat == 0.0 || promiseLon == 0.0){
                        Toast.makeText(applicationContext, "출발 장소를 선택해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else if(preferDateList.size == 0){
                        Toast.makeText(applicationContext, "가능 날짜를 선택해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else{

                    }
                }
                else{
                    updateRoomDate()
                }
            }

            // 카카오톡으로 초대 받아 들어온 경우
            else{
                postPromise()
            }

        }
    }


    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            val longitude = location.longitude //경도
            val latitude = location.latitude   //위도
            val altitude = location.altitude   //고도
            val accuracy = location.accuracy    //정확도
            val provider = location.provider   //위치제공자
        }

        override fun onProviderDisabled(provider: String) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:$provider")
        }


        override fun onProviderEnabled(provider: String) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:$provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:$provider, status:$status ,Bundle:$extras")
        }
    }

    // 새로운 약속 정보 생성
    fun postPromise()
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var postData = PostPromise(roomID, userID, promiseLat,promiseLon)
        var postPromiseResponse = networkService.postPromise(postData)
        postPromiseResponse.enqueue(object : retrofit2.Callback<PostPromiseResponse>{

            override fun onResponse(call: Call<PostPromiseResponse>, response: Response<PostPromiseResponse>) {
                if(response.isSuccessful){
                    postDate()
                }
            }

            override fun onFailure(call: Call<PostPromiseResponse>, t: Throwable?) {
            }

        })

    }

    // 해당 방의 약속 가능 날짜 범위 재설정
    fun updateRoomDate() {
        var roomIDValue : String = ""
        roomIDValue = roomID.toString()
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        val roomID = RequestBody.create(MediaType.parse("text.plain"), roomIDValue)
        val roomStartDate = RequestBody.create(MediaType.parse("text.plain"),roomStartDate )
        val roomEndDate = RequestBody.create(MediaType.parse("text.plain"), roomEndDate)

        val updateRoomDateResponse = networkService.updateRoomDate(roomID, roomStartDate,roomEndDate)

        updateRoomDateResponse.enqueue(object : retrofit2.Callback<UpdateRoomDateResponse>{

            override fun onResponse(call: Call<UpdateRoomDateResponse>, response: Response<UpdateRoomDateResponse>) {
                if(response.isSuccessful){
                    var message = response!!.body()
                    postPromise()
                }
                else{
                }
            }

            override fun onFailure(call: Call<UpdateRoomDateResponse>, t: Throwable?) {
            }

        })
    }

    // 약속 가능 날짜 데이터 전송
    fun postDate()
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        var postDate = PostDate(roomID, userID, preferDateList)
        var postDateResponse = networkService.postDate(postDate)
        postDateResponse.enqueue(object : retrofit2.Callback<PostDateResponse>{

            override fun onResponse(call: Call<PostDateResponse>, response: Response<PostDateResponse>) {
                if(response.isSuccessful){
                    postAlarm()
                }
            }

            override fun onFailure(call: Call<PostDateResponse>, t: Throwable?) {
            }

        })

    }

    // 해당 방의 상세 정보 가져오기
    fun getRoomDetail(){
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getRoomDetailRespnose = networkService.getRoomDetail(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getRoomDetailRespnose.enqueue(object : Callback<GetRoomDetailRespnose> {
                override fun onResponse(call: Call<GetRoomDetailRespnose>?, response: Response<GetRoomDetailRespnose>?) {
                    if(response!!.isSuccessful)
                    {
                        roomDetailData = response.body()!!.result
                        roomStartDate = roomDetailData[0].roomStartDate
                        roomEndDate = roomDetailData[0].roomEndDate
                        rangeStartYear = Integer.parseInt(roomStartDate.substring(0,4))
                        rangeStartMonth = Integer.parseInt(roomStartDate.substring(5,7))
                        rangeStartDay = Integer.parseInt(roomStartDate.substring(8,10))

                        rangeEndYear = Integer.parseInt(roomEndDate.substring(0,4))
                        rangeEndMonth = Integer.parseInt(roomEndDate.substring(5,7))
                        rangeEndDay = Integer.parseInt(roomEndDate.substring(8,10))
                        room_setting_range_tv.setText(roomStartDate + " ~ " + roomEndDate)
                        materialCalendarView.state().edit()
                                .setFirstDayOfWeek(Calendar.SUNDAY)
                                .setMinimumDate(CalendarDay.from(rangeStartYear, rangeStartMonth-1, rangeStartDay))
                                .setMaximumDate(CalendarDay.from(rangeEndYear, rangeEndMonth-1, rangeEndDay))
                                .setCalendarDisplayMode(CalendarMode.MONTHS)
                                .commit()
                    }
                }

                override fun onFailure(call: Call<GetRoomDetailRespnose>?, t: Throwable?) {
                    Log.v("RoomSetting","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    // 좌표를 주소로 변경해주는 api
    fun changeLocation()
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)


        var getChangeLocationResponse = restNetworkService.getSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", x, y)
        getChangeLocationResponse.enqueue(object : Callback<GetChangeLocationResponse> {

            override fun onResponse(call: Call<GetChangeLocationResponse>?, response: Response<GetChangeLocationResponse>?) {
                if(response!!.isSuccessful)
                {
                    room_setting_location_selected_btn.setText(response!!.body()!!.documents!![0].address!!.address_name)
                }
                else
                {
                    Log.v("RoomSetting","좌표 변경 검색 값 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<GetChangeLocationResponse>?, t: Throwable?) {
            }
        })
    }

    // 새로운 알림 생성
    fun postAlarm()
    {
        var userName : String
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val pref = applicationContext!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        userName = pref.getString("userName","")
        var postData = PostAlarm(roomID, userName + "님이 추가되었습니다. 약속방에서 확인해주세요.")
        var postAlarmResponse = networkService.postAlarm(postData)
        postAlarmResponse.enqueue(object : retrofit2.Callback<PostAlarmResponse>{

            override fun onResponse(call: Call<PostAlarmResponse>, response: Response<PostAlarmResponse>) {
                if(response.isSuccessful){
                    postFcmInvite()

                    var intent = Intent(applicationContext, MainActivity::class.java)
                    // 유저 테스트용 임시
                    var userTestFlag : Int
                    userTestFlag = 0
                    intent.putExtra("userTestFlag", userTestFlag)
                    startActivity(intent)

                }
            }

            override fun onFailure(call: Call<PostAlarmResponse>, t: Throwable?) {
                // 알람 전송 서버 연결 실패
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 28){

            backBtnFlag = data!!.getIntExtra("backBtnFlag",0)
            // 백버튼을 누른 경우일 때
            if(backBtnFlag == 1){

            }
            // 백버튼을 누른 경우가 아닐 때
            else{
                Log.v("TAG", "서비스 이용 지도 액티비티에서 옴")
                promiseLat = data!!.getDoubleExtra("preferLat", promiseLat)
                promiseLon = data!!.getDoubleExtra("preferLon", promiseLon)

                room_setting_current_btn.setVisibility(View.GONE)
                room_setting_map_btn.setVisibility(View.GONE)
                room_setting_location_selected_btn.setVisibility(View.VISIBLE)
                room_setting_modify_btn.setVisibility(View.VISIBLE)

                x = promiseLon.toString()
                y = promiseLat.toString()
                changeLocation()
            }

        }
    }

    // FCM 서버로 전송
    fun postFcmInvite() {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        var flag : Int = 0
        userID = pref.getInt("userID",0)
        flag = 0
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)
        val postFcmInviteResponse = networkService.postFcmInvite(roomID, userID,flag)

        postFcmInviteResponse.enqueue(object : retrofit2.Callback<PostFcmInviteResponse>{
            override fun onResponse(call: Call<PostFcmInviteResponse>, response: Response<PostFcmInviteResponse>) {
                if(response.isSuccessful){

                }
                else{

                }
            }

            override fun onFailure(call: Call<PostFcmInviteResponse>, t: Throwable?) {
            }

        })
    }

    companion object {
        lateinit var roomSettingActivity : RoomSettingActivity
        //일종의 스태틱
    }
}
