package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_room_setting.*

/**
 * Created by JAMCOM on 2018-03-25.
 */

class SplashActivity : Activity() {

    var roomIDValue: String = ""
    var flag : Int = 0
    var roomID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
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

        var test : Uri? = null
        test = intent.data

        // 카카오톡으로 넘어온 경우
        if (test != null) {
            flag = 1
            roomIDValue = test.getQueryParameter("roomIDValue")
            roomID = Integer.parseInt(roomIDValue)

            Log.v("TAG","까똑 = " + test)
            Log.v("TAG","테스트 값 = " + test.toString())
            Log.v("TAG", "카카오톡 방 넘버 = " + test.getQueryParameter("roomIDValue"))
            Log.v("TAG", "카카오톡 진짜 받은 프로젝트 넘버 = " + roomIDValue )
            //room_setting_range_btn.visibility = View.GONE
        }

        // 인텐트로 넘어온 경우
        else{
            flag = 0
            Log.v("TAG", "카톡 통해서 들어온거 아님")
        }


        val hd = Handler()
        hd.postDelayed(splashhandler(), 3000) // 3000ms=3초후에 핸들러 실행 //딜레이 3000
    }

    private inner class splashhandler : Runnable {
        override fun run() {
            var intent = Intent(applicationContext, LoginActivity::class.java)
            // 그냥 들어옴
            if(flag == 0){
                intent.putExtra("flag", flag)
                Log.v("ㅁㄴㅇㄹ", "그냥 들어와서 넘김 flag = " + flag)
            }
            // 카톡 통해서 들어옴
            else if(flag == 1){
                intent.putExtra("roomID", roomID)
                intent.putExtra("flag", flag)
                Log.v("ㅁㄴㅇㄹ", "카톡 통해서 들어와서 보낸 방번호 = " + roomID)
                Log.v("ㅁㄴㅇㄹ", "카톡 넘김 flag = " + flag)
            }
            startActivity(intent)
            //startActivity(new Intent(getApplication(), UserSelectActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // fade in, fade out 애니메이션 효과

            this@SplashActivity.finish() // 스플래쉬 페이지 액티비티 스택에서 제거
        }
    }

    override fun onBackPressed() {
        //스플래쉬 화면에서 뒤로가기 버튼 금지
    }


}

