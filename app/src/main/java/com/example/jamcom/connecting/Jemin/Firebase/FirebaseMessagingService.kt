package com.example.jamcom.connecting.Jemin.Firebase

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
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
            Log.d(TAG, "앱 실행중")


        } else {              // 앱 백그라운드 실행중
            Log.d(TAG, "app background running...")
            // 푸시 메세지 내용 pushDataMap
            val pushDataMap = remoteMessage!!.data
            Log.v("TAG","푸시" + pushDataMap)
            // 이렇게 데이터에 있는걸 키값으로 뽑아 쓰면 된다.
            val title = remoteMessage.getData().get("title")!!
            val body = remoteMessage.getData()!!.get("body")!!
            sendNotification(title, body)
        }

    }

    /*
    // 푸시 메세지를 알림으로 표현해주는 메소드
    private fun sendNotification(dataMap: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val nBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(dataMap["title"])
                .setContentText(dataMap["msg"])
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(1000, 1000))
                .setLights(Color.WHITE, 1500, 1500)
                .setContentIntent(contentIntent)

        val nManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nManager.notify(0 /* ID of notification */, nBuilder.build())
    }
    */

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