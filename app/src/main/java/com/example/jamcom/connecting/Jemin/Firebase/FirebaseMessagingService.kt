package com.example.jamcom.connecting.Jemin.Firebase

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.jamcom.connecting.Jemin.Activity.MainActivity
import com.example.jamcom.connecting.R

import com.google.firebase.messaging.RemoteMessage

import java.net.URL



class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]

    // 푸시 메세지를 수신했을때 호출되는 메소드
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (isAppRunning(this)) {           // 앱 포그라운드 실행중
            Log.d(TAG, "앱 포그라운드 실행중")

            // 푸시 메세지 내용 pushDataMap
            val pushDataMap = remoteMessage!!.data
            // 이렇게 데이터에 있는걸 키값으로 뽑아 쓰면 된다.
            val title = remoteMessage.getData().get("title")!!
            val body = remoteMessage.notification!!.body!!
            //val body = "asdf"
            //val body = remoteMessage.getData()!!.get("body")!!
            sendNotification(title, body)


        }
        // 앱 백그라운드 실행중
        else {
            Log.d(TAG, "app background running...")
            // 푸시 메세지 내용 pushDataMap
            val pushDataMap = remoteMessage!!.data
            val title = remoteMessage.getData().get("title")!!
            val body = remoteMessage.getData()!!.get("body")!!
            Log.v("TAG", "백그라운드 타이틀 = " + title)
            Log.v("TAG", "백그라운드 바디 = " + body)
            sendNotification(title, body)
        }

    }

    // 푸시 메세지를 알림으로 표현해주는 메소드
    private fun sendNotification(title : String, body : String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val nBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(300, 500, 300, 500))     //진동
                .setLights(Color.BLUE,1,1)
                .setContentIntent(pendingIntent);


        val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.notify(0 /* ID of notification */, nBuilder.build())
    }

    companion object {

        private val TAG = "MyFirebaseMsgService"

        fun isAppRunning(context: Context): Boolean {                        // 어플이 실행 중인지 확인 하는 함수.
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos = activityManager.runningAppProcesses
            for (i in procInfos.indices) {
                if (procInfos[i].processName == context.packageName) {
                    return true
                }
            }
            return false
        }
    }

}