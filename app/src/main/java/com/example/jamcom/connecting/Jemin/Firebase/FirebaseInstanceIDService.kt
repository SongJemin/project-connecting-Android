package com.example.jamcom.connecting.Jemin.Firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostPromise
import com.example.jamcom.connecting.Network.Post.PostToken
import com.example.jamcom.connecting.Network.Post.Response.PostPromiseResponse
import com.example.jamcom.connecting.Network.Post.Response.PostTokenResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import retrofit2.Call
import retrofit2.Response

class FirebaseInstanceIDService : FirebaseInstanceIdService() {
    lateinit var networkService : NetworkService
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken)
        Log.v("TAG", "토큰 Refreshed token : " + refreshedToken)
        postToken(refreshedToken!!)
    }
    // [END refresh_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // 토큰이 리프레쉬 될때 서버와 통신 소스를 넣어주시면 됩니다
        //FCM 토큰 갱신
    }


    fun postToken(token : String)
    {
        Log.v("TAG", "토큰 = " + token)
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        while(userID == 0){
            Log.v("ㅁㅇㄴㄹ", "유저아이디 0")
            userID = pref.getInt("userID",0)
        }
        Log.v("ㅁㄴㅇㄹ", "토큰 생성 유저 아이디 = " + userID)
        Log.v("ㅁㄴㅇㄹ", "토큰 생성 유저 토큰 = " + token)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var postData = PostToken(userID, token)
        var postTokenResponse = networkService.postToken(postData)
        Log.v("TAG", "토큰 생성 통신 전")
        postTokenResponse.enqueue(object : retrofit2.Callback<PostTokenResponse>{

            override fun onResponse(call: Call<PostTokenResponse>, response: Response<PostTokenResponse>) {
                Log.v("TAG", "토큰 생성 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "토큰 생성 값 전달 성공")
                }
                else{
                    Log.v("Asdf","토큰 생성 실패 = " + response!!.message())

                }
            }

            override fun onFailure(call: Call<PostTokenResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"토큰 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })

    }
}
