package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.PostUser
import com.example.jamcom.connecting.Network.Post.Response.PostPromiseResponse
import com.example.jamcom.connecting.Network.Post.Response.PostUserResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdateRoomDateResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R
import com.kakao.auth.ISessionCallback

import com.kakao.auth.Session
import com.kakao.auth.helper.Base64
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by JAMCOM on 2018-03-26.
 */

class LoginActivity : Activity() {

    private var sessionCallback: SessionCallback? = null

    var user_kakaoID : Long = 0
    var userName : String = ""
    var userImageUrl : String = ""

    var userID : Int = 1
    var userTestFlag : Int = 0

    lateinit var networkService : NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
        try {
            val info = packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(signature.toByteArray())
                Log.d("aaaa", Base64.encodeBase64URLSafeString(messageDigest.digest()))
            }
        } catch (e: Exception) {
            Log.d("error", "PackageInfo error is " + e.toString())
        }

        sessionCallback = SessionCallback()
        Session.getCurrentSession().addCallback(sessionCallback)
        Session.getCurrentSession().checkAndImplicitOpen()
        val token = Session.getCurrentSession().tokenInfo.accessToken
        Log.v("TAG", "토큰값 = $token")
        //com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        //한번이라도 로그인한 기록이있다면 재로그인

        // 로컬에 저장되어 있는 토큰 만료 시간 값
        // Session.getCurrentSession().getTokenInfo().hasValidAccessToken()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun request() {
        UserManagement.getInstance().requestMe(object : MeResponseCallback() {

            override fun onFailure(errorResult: ErrorResult?) {
                Log.d("Error", "오류로 카카오로그인 실패 ")
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                Log.d("Error", "오류로 카카오로그인 실패 ")
            }

            override fun onNotSignedUp() {}
            override fun onSuccess(userProfile: UserProfile) {
                //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴

                Log.e("UserProfile", userProfile.toString())
                userName = userProfile.getNickname()
                userImageUrl = userProfile.profileImagePath
                user_kakaoID = userProfile.id
                // Toast.makeText(LoginActivity.this, "사용자 이름은 " + userProfile.getNickname(), Toast.LENGTH_SHORT).show();
                postUser()



            }
        })


        /* 나중에 사용할 카카오 계정 정보들
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("error", "Session Closed Error is " + errorResult.toString());
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile result) {
                Toast.makeText(LoginActivity.this, "사용자 이름은 " + result.getNickname(), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    fun postUser()
    {

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        Log.v("TAG", "유저 생성시 보내는 값, 유저 이름 = " + userName)
        Log.v("TAG", "유저 생성시 보내는 값, 유저 카카오 넘버 = " + user_kakaoID)
        Log.v("TAG", "유저 생성시 보내는 값, 프로필사진 경로 = " + userImageUrl)
        var postData = PostUser(user_kakaoID, userName, userImageUrl)
        var postUserResponse = networkService.postUser(postData)
        Log.v("TAG", "유저 생성 통신 전")
        postUserResponse.enqueue(object : retrofit2.Callback<PostUserResponse>{

            override fun onResponse(call: Call<PostUserResponse>, response: Response<PostUserResponse>) {
                Log.v("TAG", "유저 생성 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "유저 생성 값 전달 성공")

                    val mHandler = Handler()
                    mHandler.postDelayed({
                        val myIntent = Intent(applicationContext, MainActivity::class.java)
                        userID = 11
                        userTestFlag = 1
                        myIntent.putExtra("userID", userID)
                        myIntent.putExtra("userTestFlag", userTestFlag)
                        //myIntent.putExtra("nickName",userProfile.getNickname());
                        startActivity(myIntent)        //main.class 시작
                    }, 3000)//딜레이 3000

                }
            }

            override fun onFailure(call: Call<PostUserResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"유저 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }



    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            request()

        }

        override fun onSessionOpenFailed(exception: KakaoException) {
            Log.d("error", "Session Fail Error is " + exception.message.toString())
        }
    }


}