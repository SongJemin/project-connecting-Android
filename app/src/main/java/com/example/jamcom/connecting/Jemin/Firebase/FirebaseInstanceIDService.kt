package com.example.jamcom.connecting.Jemin.Firebase

import android.app.Activity
import android.util.Log
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.PostToken
import com.example.jamcom.connecting.Network.Post.Response.PostTokenResponse
import com.example.jamcom.connecting.Network.ApiClient

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
        //FCM 토큰 갱신
    }

    // 생성된 토큰 DB로 전송
    fun postToken(token : String)
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        while(userID == 0){
            userID = pref.getInt("userID",0)
        }
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var postData = PostToken(userID, token)
        var postTokenResponse = networkService.postToken(postData)
        postTokenResponse.enqueue(object : retrofit2.Callback<PostTokenResponse>{

            override fun onResponse(call: Call<PostTokenResponse>, response: Response<PostTokenResponse>) {
                if(response.isSuccessful){
                    // 생성된 토큰 전달 성공
                }
                else{
                }
            }

            override fun onFailure(call: Call<PostTokenResponse>, t: Throwable?) {
                Log.v("PostToken", "토큰 전송 서버 연결 실패")
            }

        })

    }
}
