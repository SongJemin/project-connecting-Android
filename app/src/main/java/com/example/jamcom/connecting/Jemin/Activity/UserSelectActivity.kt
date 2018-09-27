package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_user_select.*

class UserSelectActivity : AppCompatActivity() {

    var userID : Int = 0
    var userTestFlag : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_select)

        user_select_user1_btn.setOnClickListener {
            userID = 1
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "라이언") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

        user_select_user2_btn.setOnClickListener {
            userID = 2
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "어피치") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

        user_select_user3_btn.setOnClickListener {
            userID = 3
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "튜브") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

        user_select_user4_btn.setOnClickListener {
            userID = 4
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "네오") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

        user_select_user5_btn.setOnClickListener {
            userID = 5
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "무지") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

        user_select_user6_btn.setOnClickListener {
            userID = 6
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "튜브") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

        user_select_user7_btn.setOnClickListener {
            userID = 30
            userTestFlag = 1
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("userTestFlag", userTestFlag)

            var pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
            var editor : SharedPreferences.Editor = pref.edit()
            editor.putInt("userID", userID) //userID란  key값으로 userID 데이터를 저장한다.
            editor.putString("userName", "테스트") //userID란  key값으로 userID 데이터를 저장한다.
            editor.commit()
            startActivity(intent)
        }

    }
}
