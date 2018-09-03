package com.example.jamcom.connecting.Jemin.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton


import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import com.example.jamcom.connecting.Jemin.Fragment.*
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.R
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var networkService : NetworkService

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2
    private val FRAGMENT4 = 4
    private val FRAGMENT5 = 5
    internal lateinit var locationManager: LocationManager
    private var bt_tab1: ImageButton? = null
    private var bt_tab2: ImageButton? = null
    private var bt_tab3: ImageButton? = null
    private var bt_tab4: ImageButton? = null
    private var bt_tab5: ImageButton? = null
    private var userID : Int = 0
    private var userTestFlag : Int = 0
    private var userName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.v("TAG", "Refreshed token : " + refreshedToken)

        userTestFlag = intent.getIntExtra("userTestFlag", 0)

        if(userTestFlag==1) {
            userID = intent.getIntExtra("userID", userID)
            userName = intent.getStringExtra("userName")

            Log.v("TAG", "선택된 유저 아이디 = " + userID)
            Log.v("TAG", "선택된 유저 이름 = " + userName)
            var pref = applicationContext.getSharedPreferences("auto",Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", userName) //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()

        }

        /*
        userID = 3
*/
        // 위젯에 대한 참조
        bt_tab1 = findViewById(R.id.main_hometab_btn) as ImageButton
        bt_tab2 = findViewById(R.id.main_recom_btn) as ImageButton
        bt_tab3 = findViewById(R.id.main_plus_btn) as ImageButton
        bt_tab4 = findViewById(R.id.main_alarm_btn) as ImageButton
        bt_tab5 = findViewById(R.id.main_mypage_btn) as ImageButton

        // 탭 버튼에 대한 리스너 연결
        bt_tab1!!.setOnClickListener(this)
        bt_tab2!!.setOnClickListener(this)
        bt_tab3!!.setOnClickListener(this)
        bt_tab4!!.setOnClickListener(this)
        bt_tab5!!.setOnClickListener(this)

        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1)

        main_hometab_btn.setSelected(true)


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
            if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                requestMyLocation()
            }//권한이 있는 경우
        } else {
            requestMyLocation()
        }//마시멜로 아래

    }

    //나의 위치 요청
    fun requestMyLocation() {
        if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.main_hometab_btn -> {

                main_hometab_btn.setSelected(true)

                main_recom_btn.setSelected(false)
                main_alarm_btn.setSelected(false)
                main_mypage_btn.setSelected(false)
                // '홈 탭' 클릭 시 '홈 리스트 프래그먼트' 호출
                callFragment(FRAGMENT1)
            }


            R.id.main_recom_btn ->{
                main_recom_btn.setSelected(true)

                main_hometab_btn.setSelected(false)
                main_alarm_btn.setSelected(false)
                main_mypage_btn.setSelected(false)
                // '추천 탭' 클릭 시 '추천 프래그먼트' 호출
                callFragment(FRAGMENT2)
            }

            R.id.main_plus_btn -> {
                // '플러스 버튼' 클릭 시 '약속방 생성 화면' 호출
                val intent = Intent(applicationContext, CreateActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }

            R.id.main_alarm_btn ->{
                //postAlarm()

                main_alarm_btn.setSelected(true)
                main_recom_btn.setSelected(false)
                main_hometab_btn.setSelected(false)
                main_mypage_btn.setSelected(false)
                // '알림 탭' 클릭 시 '알림 프래그먼트' 호출
                callFragment(FRAGMENT4)

            }
            R.id.main_mypage_btn ->{
                main_mypage_btn.setSelected(true)

                main_recom_btn.setSelected(false)
                main_alarm_btn.setSelected(false)
                main_hometab_btn.setSelected(false)
                // '마이페이지 탭' 클릭 시 '마이페이지 프래그먼트' 호출
                callFragment(FRAGMENT5)
            }
        }
    }


    private fun callFragment(frament_no: Int) {

        // 프래그먼트 사용을 위해
        val transaction = supportFragmentManager.beginTransaction()

        when (frament_no) {
            1 -> {
                // '홈 탭' 호출
                val mainFragment = HomelistFragment()
                transaction.replace(R.id.fragment_container, mainFragment)
                transaction.commit()
            }

            2 -> {
                // '추천 탭' 호출
                val recomFragment = RecomFragment()
                transaction.replace(R.id.fragment_container, recomFragment)
                transaction.commit()
            }


            4 -> {
                // '알림 탭' 호출
                val alarmFragment = AlarmFragment()
                transaction.replace(R.id.fragment_container, alarmFragment)
                transaction.commit()
            }

            5 -> {
                // '마이페이지 탭' 호출
                val mypageFragment = MyPageFragment()
                transaction.replace(R.id.fragment_container, mypageFragment)
                transaction.commit()
            }
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("연결고리를 종료할까요?")
                .setPositiveButton("예") { dialog, which ->
                    moveTaskToBack(true)
                    finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .setNegativeButton("아니요", null)
                .show()
    }


}