package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import com.example.jamcom.connecting.Network.Get.Response.GetTokenFlagResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.DeleteUser
import com.example.jamcom.connecting.Network.Post.Response.DeleteUserResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdatePushTokenFlagResponse
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.R
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.kakao.util.helper.log.Logger
import kotlinx.android.synthetic.main.activity_setting.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    lateinit var networkService : NetworkService
    internal lateinit var myToolbar: Toolbar
    var pushTokenFlag : Int = 0

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        // 푸시 알람 체크 ON
        if(setting_push_check.isChecked){
            pushTokenFlag = 1
            updatePushToken()
        }
        // 푸시 알람 체크 OFF
        else{
            pushTokenFlag = 0
            updatePushToken()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 추가된 소스, Toolbar를 생성한다.
        myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
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
        getPushTokenFlag()
        setting_push_check.setOnCheckedChangeListener(this)

        // '개인정보 처리방침' 버튼 클릭 시
        setting_privacy_layout.setOnClickListener {
            Toast.makeText(applicationContext, "준비 중입니다.", Toast.LENGTH_SHORT).show()
        }

        // '회원 탈퇴' 버튼 클릭 시
        setting_unlink_layout.setOnClickListener {
            onClickUnlink()
        }
        // '로그아웃' 버튼 클릭 시
        setting_logout_btn.setOnClickListener {
            //onClickLogout()
            var intent = Intent(applicationContext, UserSelectActivity::class.java)
            startActivity(intent)
        }

    }
    fun onClickUnlink() {
        val appendMessage = getString(R.string.com_kakao_confirm_unlink)
        AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button)) { dialog, which ->
                    UserManagement.getInstance().requestUnlink(object: UnLinkResponseCallback() {
                        override fun onFailure(errorResult: ErrorResult?) {
                            Logger.e(errorResult!!.toString())
                        }

                        override fun onSessionClosed(errorResult: ErrorResult) {
                        }

                        override fun onNotSignedUp() {
                        }

                        override fun onSuccess(userId:Long?) {
                            deleteUser()
                        }
                    })
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        object: DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which:Int) {
                                dialog.dismiss()
                            }
                        })
                .show()


    }

    // 푸시알람 ON/OFF Flag 수정
    fun updatePushToken() {
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        val pref = applicationContext!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userIDValue : Int = 0
        userIDValue = pref.getInt("userID",0)

        val userID = RequestBody.create(MediaType.parse("text.plain"), userIDValue.toString())
        val tokenFlag = RequestBody.create(MediaType.parse("text.plain"),pushTokenFlag.toString())

        val updatePushTokenFlagResponse = networkService.updatePushTokenFlag(userID, tokenFlag)

        updatePushTokenFlagResponse.enqueue(object : retrofit2.Callback<UpdatePushTokenFlagResponse>{

            override fun onResponse(call: Call<UpdatePushTokenFlagResponse>, response: Response<UpdatePushTokenFlagResponse>) {
                if(response.isSuccessful){
                }
                else{
                }
            }

            override fun onFailure(call: Call<UpdatePushTokenFlagResponse>, t: Throwable?) {
            }

        })
    }

    // 푸시 알람 ON/OFF Flag값 가져오기
    fun getPushTokenFlag(){
        try {
            val pref = applicationContext!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var userID : Int = 0
            userID = pref.getInt("userID",0)

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getRoomDetailRespnose = networkService.getTokenFlag(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getRoomDetailRespnose.enqueue(object : Callback<GetTokenFlagResponse> {
                override fun onResponse(call: Call<GetTokenFlagResponse>?, response: Response<GetTokenFlagResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        pushTokenFlag = response!!.body()!!.tokenFlag

                        if(pushTokenFlag == 1){
                            setting_push_check.isChecked = true
                        }
                        else if(pushTokenFlag == 0){
                            setting_push_check.isChecked = false
                        }
                    }
                }
                override fun onFailure(call: Call<GetTokenFlagResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    // 해당 유저 삭제
    fun deleteUser()
    {
        val pref = applicationContext!!.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var deleteUser = DeleteUser(userID)
        var deleteUserResponse = networkService.deleteUser(deleteUser)
        deleteUserResponse.enqueue(object : retrofit2.Callback<DeleteUserResponse>{

            override fun onResponse(call: Call<DeleteUserResponse>, response: Response<DeleteUserResponse>) {
                if(response.isSuccessful){
                    var intent = Intent(applicationContext, SplashActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable?) {
            }

        })

    }
    // 로그아웃
    fun onClickLogout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                var intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
