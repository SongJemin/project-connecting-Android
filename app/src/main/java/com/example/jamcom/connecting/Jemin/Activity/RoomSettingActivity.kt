package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jamcom.connecting.Jemin.Calendar.OneDayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SaturdayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SundayDecorator
import com.example.jamcom.connecting.Network.Get.GetChangeLocationMessage
import com.example.jamcom.connecting.Network.Get.GetHomeListMessage
import com.example.jamcom.connecting.Network.Get.GetRoomDetailMessage
import com.example.jamcom.connecting.Network.Get.GetRoomIDMessage
import com.example.jamcom.connecting.Network.Get.Response.GetChangeLocationResponse
import com.example.jamcom.connecting.Network.Get.Response.GetRoomDetailRespnose
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostDate
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.Response.PostDateResponse
import com.example.jamcom.connecting.Network.Post.Response.PostPromiseResponse
import com.example.jamcom.connecting.Network.Post.Response.PostRoomTestResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdateRoomDateResponse
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_room_inform.*
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

    internal var question_ㅣist = java.util.ArrayList<String>()

    lateinit var roomIDData : ArrayList<GetRoomIDMessage>
    var roomID : Int = 0
    var roomName : String = ""
    var roomTypeID : Int = 0
    var modify_flag : Int = 0
    var roomStartDate : String = ""
    var roomEndDate : String = ""
    var selectDate : String = ""
    var selectRealDate : String = ""

    lateinit var locationChangeData : GetChangeLocationMessage

    var roomIDValue: String = ""
    var flag : Int = 0
    var return_flag : Int = 0

    var x : String = ""
    var y : String = ""

    var promiseLat : Double = 0.0
    var promiseLon : Double = 0.0

    var selectedYear : String = ""
    var selectedMonth : String = ""
    var selectedDay : String = ""
    var selectedMonthValue : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_setting)

        var test : Uri? = null
        test = intent.data

        // 카카오톡으로 넘어온 경우
        if (test != null) {
                flag = 1
                roomIDValue = test.getQueryParameter("roomIDValue")

                Log.v("TAG","까똑 = " + test)
                Log.v("TAG","테스트 값 = " + test.toString())
                Log.v("TAG", "카카오톡 방 넘버 = " + test.getQueryParameter("roomIDValue"))
                roomID = Integer.parseInt(roomIDValue)
                Log.v("TAG", "카카오톡 진짜 받은 프로젝트 넘버 = " + roomIDValue )
            getRoomDetail()
            //room_setting_range_btn.visibility = View.GONE
            room_setting_location_selected_btn.setVisibility(View.GONE)
            room_setting_modify_btn.setVisibility(View.GONE)
        }

        // 인텐트로 넘어온 경우
        else{
            flag = 0
            roomID = intent.getIntExtra("roomID", 0)
            Log.v("TAG", "맵뷰에서 다시 받은 방번호 = " + roomID)

                Log.v("TAG", "방 생성 액티비티에서 옴")
                room_setting_location_selected_btn.setVisibility(View.GONE)
                room_setting_modify_btn.setVisibility(View.GONE)
                room_setting_range_tv.visibility = View.GONE

        }

        materialCalendarView = findViewById<View>(R.id.m_calendarView) as MaterialCalendarView
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1))
                .setMaximumDate(CalendarDay.from(2030, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        // LocationManager 객체를 얻어온다
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

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

            Toast.makeText(applicationContext,"선택 날짜 : "+ selectRealDate, Toast.LENGTH_SHORT).show()

        })

                room_setting_current_btn.setOnClickListener {

            try {
                var currentLocation: Location? = null
                    room_setting_location_selected_btn.setVisibility(View.VISIBLE)
                    room_setting_current_btn.setVisibility(View.GONE)
                    room_setting_map_btn.setVisibility(View.GONE)
                room_setting_modify_btn.setVisibility(View.VISIBLE)
                    // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!

/*
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                            100, // 통지사이의 최소 시간간격 (miliSecond)
                            1f, // 통지사이의 최소 변경거리 (m)
                            mLocationListener)
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                            100, // 통지사이의 최소 시간간격 (miliSecond)
                            1f, // 통지사이의 최소 변경거리 (m)
                            mLocationListener)
                    //room_setting_location_selected_btn.setText("위치정보 미수신중")
                    */
                val locationProvider = LocationManager.GPS_PROVIDER


                // 수동으로 위치 구하기
                currentLocation = lm.getLastKnownLocation(locationProvider)
                if (currentLocation != null) {
                    promiseLat = currentLocation.getLatitude()
                    promiseLon = currentLocation.getLongitude()

                    Log.d("Main", "현재 lat=" + promiseLat + " lon=" + promiseLon)
                    //room_setting_location_selected_btn.setText("위도 : " + promiseLat + " 경도 : " + promiseLon)
                    lm.removeUpdates(mLocationListener)  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    x = promiseLon.toString()
                    y = promiseLat.toString()
                    changeLocation()
                }





            } catch (ex: SecurityException) {
            }


            //val intent = Intent(applicationContext, TestActivity::class.java)
            //startActivity(intent)
        }

        room_setting_modify_btn.setOnClickListener {
            room_setting_location_selected_btn.setVisibility(View.GONE)
            room_setting_current_btn.setVisibility(View.VISIBLE)
            room_setting_map_btn.setVisibility(View.VISIBLE)
            room_setting_modify_btn.setVisibility(View.GONE)
        }

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
                room_setting_range_btn.text = roomStartDate + " ~ " + roomEndDate
            }
            pd.show(fragmentManager, "YearMonthPickerTest")
        }

        room_setting_map_btn.setOnClickListener {
            modify_flag = 0
            val intent = Intent(applicationContext, MapViewActivity::class.java)
            intent.putExtra("roomID", roomID)
            intent.putExtra("modify_flag", modify_flag)
            Log.v("TAG", "맵뷰로 보내는 방번호 = " + roomID)
            startActivityForResult(intent, 28)
        }

        room_setting_confirm_btn.setOnClickListener{

            // 인텐트로 넘어온 경우
            if(flag==0)
            {
                Log.v("TAG","인텐트로 들어옴")
                updateRoomDate()
            }

            // 카카오톡으로 초대 받아 들어온 경우
            else{
                roomID = Integer.parseInt(roomIDValue)
                Log.v("TAG","카톡으로 들어옴 방번호 값" + roomID)
                postPromise()
            }


            //val intent = Intent(applicationContext, MainActivity::class.java)
            //startActivity(intent)
        }
    }


    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.


            Log.d("test", "onLocationChanged, location:$location")
            val longitude = location.longitude //경도
            val latitude = location.latitude   //위도
            val altitude = location.altitude   //고도
            val accuracy = location.accuracy    //정확도
            val provider = location.provider   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.




            //room_setting_location_selected_btn.setText("위치정보 : " + provider + " 위도 : " + longitude + " 경도 : " + latitude
              //      + "\n고도 : " + altitude + "\n정확도 : " + accuracy)
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


    fun postPromise()
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var dateID : Int = 0
        dateID = 1
        Log.v("TAG", "약속 생성시 보내는 값, 방 넘버 = " + roomID)
        Log.v("TAG", "약속 생성시 보내는 값, 유저 넘버 = " + userID)
        Log.v("TAG", "약속 생성시 보내는 값, 위도 = " + promiseLat)
        Log.v("TAG", "약속 생성시 보내는 값, 경도 = " + promiseLon)
        Log.v("TAG", "약속 생성시 보내는 값, 선호 날짜 = " + dateID)
        var postData = PostPromise(roomID, userID, promiseLat,promiseLon, dateID)
        var postPromiseResponse = networkService.postPromise(postData)
        Log.v("TAG", "방 생성 통신 전")
        postPromiseResponse.enqueue(object : retrofit2.Callback<PostPromiseResponse>{

            override fun onResponse(call: Call<PostPromiseResponse>, response: Response<PostPromiseResponse>) {
                Log.v("TAG", "약속 생성 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "약속 생성 값 전달 성공")

                    postDate()

                }
            }

            override fun onFailure(call: Call<PostPromiseResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun updateRoomDate() {

        var roomIDValue : String = ""
        roomIDValue = roomID.toString()
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        val roomID = RequestBody.create(MediaType.parse("text.plain"), roomIDValue)
        val roomStartDate = RequestBody.create(MediaType.parse("text.plain"),roomStartDate )
        val roomEndDate = RequestBody.create(MediaType.parse("text.plain"), roomEndDate)

        val updateRoomDateResponse = networkService.updateRoomDate(roomID, roomStartDate,roomEndDate)

        Log.v("TAG", "방날짜 수정 전송 : 수정 방넘버 = " + roomIDValue + ", 수정 시작날짜 = " + roomStartDate
                + ", 수정 끝날짜 = " + roomEndDate)

        updateRoomDateResponse.enqueue(object : retrofit2.Callback<UpdateRoomDateResponse>{

            override fun onResponse(call: Call<UpdateRoomDateResponse>, response: Response<UpdateRoomDateResponse>) {
                Log.v("TAG", "방 날짜 수정 통신 성공")
                if(response.isSuccessful){
                    var message = response!!.body()

                    Log.v("TAG", "방 날짜 수정 값 전달 성공"+ message.toString())
                    postPromise()
                }
                else{
                    Toast.makeText(applicationContext,"방 날짜 수정 값 전달 실패", Toast.LENGTH_SHORT).show()

                    Log.v("TAG", "방 날짜 수정 값 전달 실패"+ response!!.body().toString())
                }
            }

            override fun onFailure(call: Call<UpdateRoomDateResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"방 날짜 수정 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun postDate()
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
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
                Log.v("TAG", "날짜 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "선호 날짜 값 전달 성공")

                    var intent = Intent(applicationContext, MainActivity::class.java)

                    // 유저 테스트용 임시
                    var userTestFlag : Int
                    userTestFlag = 0
                    intent.putExtra("userTestFlag", userTestFlag)

                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<PostDateResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"날짜 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

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

                        roomStartDate = roomDetailData[0].roomStartDate
                        roomEndDate = roomDetailData[0].roomEndDate

                        room_setting_range_tv.setText(roomStartDate + " ~ " + roomEndDate)

                    }
                }

                override fun onFailure(call: Call<GetRoomDetailRespnose>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }


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
                    Log.v("TAG","레스트 검색 값 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<GetChangeLocationResponse>?, t: Throwable?) {
                Log.v("TAG","검색 통신 실패")
                Log.v("TAG","검색 통신 실패"+t.toString())
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 28){
            Log.v("TAG", "서비스 이용 지도 액티비티에서 옴")
            promiseLat = data!!.getDoubleExtra("preferLat", promiseLat)
            promiseLon = data!!.getDoubleExtra("preferLon", promiseLon)
            //roomID = intent.getIntExtra("roomID", 0)
            Log.v("TAG", "서비스 선호 출발 위도 = " + promiseLat)
            Log.v("TAG", "서비스 선호 출발 경도 = " + promiseLon)
            Log.v("TAG", "서비스 선호 출발 방넘버 = " + roomID)

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
