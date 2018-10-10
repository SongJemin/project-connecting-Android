package com.example.jamcom.connecting.Jemin.ChatTest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

import com.example.jamcom.connecting.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class StartActivity : AppCompatActivity() {

    private var user_chat: EditText? = null
    private var user_edit: EditText? = null
    private var user_next: Button? = null
    private var chat_list: ListView? = null

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        user_chat = findViewById<View>(R.id.user_chat) as EditText
        user_edit = findViewById<View>(R.id.user_edit) as EditText
        user_next = findViewById<View>(R.id.user_next) as Button
        chat_list = findViewById<View>(R.id.chat_list) as ListView

        user_next!!.setOnClickListener(View.OnClickListener {
            if (user_edit!!.text.toString() == "" || user_chat!!.text.toString() == "")
                return@OnClickListener

            val intent = Intent(this@StartActivity, ChatActivity::class.java)
            intent.putExtra("chatName", user_chat!!.text.toString())
            intent.putExtra("userName", user_edit!!.text.toString())
            startActivity(intent)
        })

        showChatList()

    }

    private fun showChatList() {
        // 리스트 어댑터 생성 및 세팅
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1)
        chat_list!!.adapter = adapter

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.key!!)
                adapter.add(dataSnapshot.key)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
}