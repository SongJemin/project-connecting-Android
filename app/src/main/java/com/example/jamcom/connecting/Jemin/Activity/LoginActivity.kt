package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.example.jamcom.connecting.Network.Get.Response.GetUserCheckResponse
import com.example.jamcom.connecting.Network.Get.Response.GetUserIDResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostUser
import com.example.jamcom.connecting.Network.Post.Response.PostUserResponse
import com.example.jamcom.connecting.Network.ApiClient

import com.example.jamcom.connecting.R
import com.kakao.auth.ISessionCallback

import com.kakao.auth.Session
import com.kakao.auth.helper.Base64
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.Utility.getPackageInfo
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
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

    var roomID : Int = 0
    var flag : Int = 0
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
        flag = intent.getIntExtra("flag", 0)
        // 카톡 통해서 들어옴
        if(flag == 1){
            roomID = intent.getIntExtra("roomID", 0)
        }
        // 그냥 들어옴
        else if(flag == 0){

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


        com_kakao_login.visibility = View.INVISIBLE

        login_kakao_btn.setOnClickListener {
            com_kakao_login.performClick()
        }
        sessionCallback = SessionCallback()
        //한번이라도 로그인한 기록이있다면 재로그인
        Session.getCurrentSession().addCallback(sessionCallback)
        Session.getCurrentSession().checkAndImplicitOpen()
        val token = Session.getCurrentSession().tokenInfo.accessToken
        Log.v("TAG", "토큰값 = $token")

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
                Log.v("LoginActivity", "유저 네임 = " + userName)

                // 유저 프로필 사진 없을 시
                if(userProfile.profileImagePath == null){
                    userImageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Default_profile_picture_%28male%29_on_Facebook.jpg/600px-Default_profile_picture_%28male%29_on_Facebook.jpg"
                }
                else{
                    userImageUrl = userProfile.profileImagePath
                }

                user_kakaoID = userProfile.id
                getUserCheck()
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

    // 새로운 유저 생성
    fun postUser()
    {
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        var postData = PostUser(user_kakaoID, userName, userImageUrl)
        var postUserResponse = networkService.postUser(postData)
        postUserResponse.enqueue(object : retrofit2.Callback<PostUserResponse>{

            override fun onResponse(call: Call<PostUserResponse>, response: Response<PostUserResponse>) {
                if(response.isSuccessful){
                    getUserID()
                }
                else{
                    Log.v("LoginActivity", "유저 생성 값 전달 실패 = " + response!!.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<PostUserResponse>, t: Throwable?) {
                //Toast.makeText(applicationContext,"유저 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }

    // 생성한 유저ID 값 가져오기
    private fun getUserID() {
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getUserIDResponse = networkService.getUserID() // 네트워크 서비스의 getContent 함수를 받아옴
            getUserIDResponse.enqueue(object : Callback<GetUserIDResponse> {
                override fun onResponse(call: Call<GetUserIDResponse>?, response: Response<GetUserIDResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        userID = response.body()!!.result.userID
                        var pref = applicationContext.getSharedPreferences("auto",Activity.MODE_PRIVATE)
                        var editor : SharedPreferences.Editor = pref.edit()
                        editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
                        editor.putString("userName", userName) //userID란  key값으로 userID 데이터를 저장한다.
                        editor.commit()

                        val mHandler = Handler()
                        mHandler.postDelayed({
                            val myIntent = Intent(applicationContext, MainActivity::class.java)
                            userTestFlag = 1
                            myIntent.putExtra("userID", userID)
                            myIntent.putExtra("userName", userName)
                            myIntent.putExtra("userTestFlag", userTestFlag)
                            //myIntent.putExtra("nickName",userProfile.getNickname());
                            startActivity(myIntent)        //main.class 시작
                        }, 3000)//딜레이 3000

                    }
                }

                override fun onFailure(call: Call<GetUserIDResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    // 해당 유저 중복 체크
    private fun getUserCheck() {
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            var getUserCheckResponse = networkService.getUserCheck(user_kakaoID) // 네트워크 서비스의 getContent 함수를 받아옴

            getUserCheckResponse.enqueue(object : Callback<GetUserCheckResponse> {
                override fun onResponse(call: Call<GetUserCheckResponse>?, response: Response<GetUserCheckResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        if(response.body()!!.result!!.size == 0)
                        {
                            postUser()
                        }
                        else{
                            userID = response.body()!!.result[0].userID!!

                            val mHandler = Handler()
                            mHandler.postDelayed({

                                userTestFlag = 1
                                var pref = applicationContext.getSharedPreferences("auto",Activity.MODE_PRIVATE)
                                var editor : SharedPreferences.Editor = pref.edit()
                                editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
                                editor.putString("userName", userName) //userID란  key값으로 userID 데이터를 저장한다.
                                editor.commit()

                                // 카카오톡 초대로 들어왔을 경우
                                if(flag == 1){
                                    val myIntent = Intent(applicationContext, RoomSettingActivity::class.java)
                                    myIntent.putExtra("roomID", roomID)
                                    myIntent.putExtra("flag", flag)
                                    myIntent.putExtra("userTestFlag", userTestFlag)
                                    startActivity(myIntent)        //main.class 시작
                                }
                                // 아닌 경우
                                else if(flag ==0){
                                    val myIntent = Intent(applicationContext, MainActivity::class.java)
                                    myIntent.putExtra("flag", flag)
                                    myIntent.putExtra("userTestFlag", userTestFlag)
                                    startActivity(myIntent)        //main.class 시작
                                }

                            }, 10)//딜레이 3000

                        }
                    }
                }

                override fun onFailure(call: Call<GetUserCheckResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            request()

        }

        override fun onSessionOpenFailed(exception: KakaoException) {
            Log.d("error", "Session Fail Error is " + exception.message.toString())
        }
    }

    fun getKeyHash(context: Context): String? {
        val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w(TAG, "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }

}