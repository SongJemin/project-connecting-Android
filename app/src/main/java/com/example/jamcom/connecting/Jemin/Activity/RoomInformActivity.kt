package com.example.jamcom.connecting.Jemin.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.ExtractedTextRequest
import android.widget.ImageButton
import android.widget.TextView
import com.example.jamcom.connecting.Jemin.Fragment.*
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room_inform.*

class RoomInformActivity : AppCompatActivity() {

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2
    private val FRAGMENT3 = 3
    private val FRAGMENT4 = 4
    internal lateinit var locationManager: LocationManager
    private var decideTv: TextView? = null
    private var memberTv: TextView? = null
    private var recomPlaceTv: TextView? = null
    private var myInformTv: TextView? = null
    internal lateinit var myToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_inform)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

        // 위젯에 대한 참조
        decideTv = findViewById(R.id.room_inform_decide_tv) as TextView
        memberTv = findViewById(R.id.room_inform_member_tv) as TextView
        recomPlaceTv = findViewById(R.id.room_inform_recomplace_tv) as TextView
        myInformTv = findViewById(R.id.room_inform_myinform_tv) as TextView

        // 탭 버튼에 대한 리스너 연결


        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1)

        room_inform_decide_tv.setSelected(true)

        room_inform_decide_tv.setOnClickListener {
            room_inform_decide_tv.isSelected = true
            room_inform_decide_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_member_tv.isSelected = false
            room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_recomplace_tv.isSelected = false
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_myinform_tv.isSelected = false
            room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))


            callFragment(FRAGMENT1)
        }

        room_inform_member_tv.setOnClickListener {

            room_inform_member_tv.isSelected = true
            room_inform_member_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_decide_tv.isSelected = false
            room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_recomplace_tv.isSelected = false
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_myinform_tv.isSelected = false
            room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))
            callFragment(FRAGMENT2)
        }

        room_inform_recomplace_tv.setOnClickListener {
            room_inform_recomplace_tv.isSelected = true
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_decide_tv.isSelected = false
            room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_member_tv.isSelected = false
            room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_myinform_tv.isSelected = false
            room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))
            callFragment(FRAGMENT3)
        }

        room_inform_myinform_tv.setOnClickListener {
            room_inform_myinform_tv.isSelected = true
            room_inform_myinform_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_decide_tv.isSelected = false
            room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_recomplace_tv.isSelected = false
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_member_tv.isSelected = false
            room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
            // '버튼4' 클릭 시 '프래그먼트2' 호출
            callFragment(FRAGMENT4)
        }


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(intent)
            finish()
        }

        //마시멜로 이상이면 권한 요청하기
        if (Build.VERSION.SDK_INT >= 23) {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(this@RoomInformActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@RoomInformActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@RoomInformActivity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                requestMyLocation()
            }//권한이 있는 경우
        } else {
            requestMyLocation()
        }//마시멜로 아래

    }

    //나의 위치 요청
    fun requestMyLocation() {
        if (ContextCompat.checkSelfPermission(this@RoomInformActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@RoomInformActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }




    private fun callFragment(frament_no: Int) {

        // 프래그먼트 사용을 위해
        val transaction = supportFragmentManager.beginTransaction()

        when (frament_no) {
            1 -> {
                // '프래그먼트1' 호출
                val roomDecideTab = RoomDecideTab()
                transaction.replace(R.id.room_inform_frame_layout, roomDecideTab)

                transaction.commit()
            }

            2 -> {
                // '프래그먼트2' 호출
                val roomMemberTab = RoomMemberTab()
                transaction.replace(R.id.room_inform_frame_layout, roomMemberTab)
                transaction.commit()
            }


            3 -> {
                // '프래그먼트4' 호출
                val roomRecomPlaceTab = RoomRecomPlaceTab()
                transaction.replace(R.id.room_inform_frame_layout, roomRecomPlaceTab)
                transaction.commit()
            }

            4 -> {
                // '프래그먼트5' 호출
                val roomMyInformTab = RoomMyInformTab()
                transaction.replace(R.id.room_inform_frame_layout, roomMyInformTab)
                transaction.commit()
            }
        }

    }


}