package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.jamcom.connecting.Jemin.Calendar.OneDayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SaturdayDecorator
import com.example.jamcom.connecting.Jemin.Calendar.SundayDecorator
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostRoom
import com.example.jamcom.connecting.Network.Post.Response.PostRoomResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_room_setting.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class RoomSettingActivity : AppCompatActivity() {

    internal lateinit var materialCalendarView: MaterialCalendarView
    internal lateinit var startZeroMonth: String
    internal lateinit var startZeroDay: String
    internal lateinit var finishZeroMonth: String
    internal lateinit var finishZeroDay: String
    lateinit var start_date: String
    lateinit var end_date: String
    lateinit var networkService : NetworkService

    var roomName : String = ""
    var roomTypeID : Int = 0
    var roomStartDate : String = ""
    var roomEndDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_setting)

        roomName = intent.getStringExtra("roomName")
        roomTypeID = intent.getIntExtra("roomTypeID", 0)

        room_setting_location_selected_btn.setVisibility(View.GONE)
        materialCalendarView = findViewById<View>(R.id.m_calendarView) as MaterialCalendarView
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        // LocationManager 객체를 얻어온다
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        materialCalendarView.addDecorators(
                SundayDecorator(),
                SaturdayDecorator(),
                OneDayDecorator())
        materialCalendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE

        room_setting_current_btn.setOnClickListener {

            try {
                    room_setting_location_selected_btn.setVisibility(View.VISIBLE)
                    room_setting_current_btn.setVisibility(View.GONE)
                    room_setting_map_btn.setVisibility(View.GONE)
                    room_setting_location_selected_btn.setText("수신중..")
                    // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                            100, // 통지사이의 최소 시간간격 (miliSecond)
                            1f, // 통지사이의 최소 변경거리 (m)
                            mLocationListener)
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                            100, // 통지사이의 최소 시간간격 (miliSecond)
                            1f, // 통지사이의 최소 변경거리 (m)
                            mLocationListener)

                    //room_setting_location_selected_btn.setText("위치정보 미수신중")
                   // lm.removeUpdates(mLocationListener)  //  미수신할때는 반드시 자원해체를 해주어야 한다.

            } catch (ex: SecurityException) {
            }


            //val intent = Intent(applicationContext, TestActivity::class.java)
            //startActivity(intent)
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
            val intent = Intent(applicationContext, MapViewActivity::class.java)
            startActivity(intent)
        }

        room_setting_confirm_btn.setOnClickListener{
            postRoom()
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
            room_setting_location_selected_btn.setText("위도 : " + longitude + " 경도 : " + latitude)
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

    fun postRoom()
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        var roomCreaterID : Int = 0
        roomCreaterID = userID
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        Log.v("TAG", "방 생성시 보내는 값, 방 제목 = " + roomName)
        Log.v("TAG", "방 생성시 보내는 값, 방 만든 사람 = " + roomCreaterID)
        Log.v("TAG", "방 생성시 보내는 값, 방 시작날짜 = " + roomStartDate)
        Log.v("TAG", "방 생성시 보내는 값, 방 끝날짜 = " + roomEndDate)
        Log.v("TAG", "방 생성시 보내는 값, 방 타입 = " + roomTypeID)
        var data = PostRoom(roomName, roomCreaterID, roomStartDate,roomEndDate,roomTypeID)
        var postRoomResponse = networkService.postRoom(data)
        Log.v("TAG", "방 생성 통신 전")
        postRoomResponse.enqueue(object : retrofit2.Callback<PostRoomResponse>{

            override fun onResponse(call: Call<PostRoomResponse>, response: Response<PostRoomResponse>) {
                Log.v("TAG", "방 생성 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "방 생성 값 전달 성공")
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<PostRoomResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }

}
