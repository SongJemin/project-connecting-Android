package com.example.jamcom.connecting.Jemin.ChatTest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.ConnectingAdapter
import com.example.jamcom.connecting.Jemin.Item.ConnectingListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.Network.ApiClient
import com.example.jamcom.connecting.Network.Get.GetParticipMemberMessage
import com.example.jamcom.connecting.Network.Get.Response.GetParticipMemberResponse
import com.example.jamcom.connecting.Network.Get.Response.GetUserImageUrlResponse
import com.example.jamcom.connecting.Network.Get.Response.GetUserInformResponse
import com.example.jamcom.connecting.Network.NetworkService

import com.example.jamcom.connecting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_connecting_count.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    internal lateinit var locationManager: LocationManager
    lateinit var chatAdapter : ChatAdapter
    lateinit var chatListItem: ArrayList<ChatListItem>

    private var CHAT_NAME: String? = null
    private var USER_NAME: String? = null
    lateinit var networkService : NetworkService
    private var chat_view: ListView? = null
    private var chat_edit: EditText? = null
    private var chat_send: Button? = null
    var userID : Int = 0
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    lateinit var roomParticipMemberItems: ArrayList<RoomMemberItem>
    lateinit var roomMemberlistData : ArrayList<GetParticipMemberMessage>

    var roomID : Int = 0
    var count : Int = 0
    var countVaule : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 추가된 소스, Toolbar를 생성한다.
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

        requestManager = Glide.with(this)
        // 위젯 ID 참조
        //chat_view = findViewById<View>(R.id.chat_view) as ListView
        chat_edit = findViewById<View>(R.id.chat_edit) as EditText
        chat_send = findViewById<View>(R.id.chat_sent) as Button

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        val intent = intent
        roomID = intent.getIntExtra("roomID", 0)
        CHAT_NAME = intent.getStringExtra("chatName")
        USER_NAME = intent.getStringExtra("userID")
        userID = Integer.parseInt(USER_NAME)
        //userID = intent.getIntExtra("userID",0)
        getParticipMemberList()
        Log.v("asdf", "현재 유저 번호 = " + userID)
        Log.v("asdf", "현재 유저 이름 = " + USER_NAME)
        chat_roomname_tv.text = CHAT_NAME

        chatListItem = ArrayList()
        // 채팅 방 입장
        openChat(CHAT_NAME)

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send!!.setOnClickListener(View.OnClickListener {
            if (chat_edit!!.text.toString() == "")
                return@OnClickListener

            val chat = ChatDTO(userID, USER_NAME, chat_edit!!.text.toString()) //ChatDTO를 이용하여 데이터를 묶는다.
            databaseReference.child("chat").child(CHAT_NAME!!).push().setValue(chat) // 데이터 푸쉬
            chat_edit!!.setText("") //입력창 초기화
        })
    }

    private fun addMessage(dataSnapshot: DataSnapshot, chatAdapter: ChatAdapter) {

        //getUserInform(userID,dataSnapshot, chatAdapter)


        //adapter.add(chatDTO!!.userName + " : " + chatDTO.message)
    }

    private fun removeMessage(dataSnapshot: DataSnapshot, adapter: ArrayAdapter<String>) {
        val chatDTO = dataSnapshot.getValue(ChatDTO::class.java)
        //adapter.remove(chatDTO!!.userName + " : " + chatDTO.message)
    }

    private fun openChat(chatName: String?) {
        // 리스트 어댑터 생성 및 세팅
        var chatAdapter = ChatAdapter(chatListItem, requestManager)

        //chat_view!!.adapter = adapter

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName!!).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                getUserInform(dataSnapshot, chatAdapter)
                //addMessage(dataSnapshot, chatAdapter)
               // Log.e("LOG", "s:" + s!!)

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
//                removeMessage(dataSnapshot, adapter)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    // 유저 이미지 주소 가져오기
    fun getUserInform(dataSnapshot: DataSnapshot, chatAdapter: ChatAdapter){
        val chatDTO = dataSnapshot.getValue(ChatDTO::class.java)
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            var getUserInformResponse = networkService.getUserInform(Integer.parseInt(chatDTO!!.userName)) // 네트워크 서비스의 getContent 함수를 받아옴

            getUserInformResponse.enqueue(object : Callback<GetUserInformResponse> {
                override fun onResponse(call: Call<GetUserInformResponse>?, response: Response<GetUserInformResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        chatListItem.add(ChatListItem(Integer.parseInt(chatDTO!!.userName), response.body()!!.result.userImageUrl, response.body()!!.result.userName, chatDTO.message))
                        Log.v("asdf", "채팅 유저 이미지 확인 = " + response.body()!!.result.userImageUrl)
                        Log.v("asdf", "채팅 유저 이름 확인 = " + response.body()!!.result.userName)
                        chat_chat_recyclerview.adapter = chatAdapter
                        chat_chat_recyclerview.layoutManager = LinearLayoutManager(this@ChatActivity)
                        chat_chat_recyclerview.scrollToPosition(chatListItem.size-1)
                    }
                }

                override fun onFailure(call: Call<GetUserInformResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }

    // 해당 방의 참여 멤버 리스트 가져오기
    private fun getParticipMemberList() {
        roomParticipMemberItems = ArrayList()
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getParticipMemberResponse = networkService.getParticipMemberList(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getParticipMemberResponse.enqueue(object : Callback<GetParticipMemberResponse> {
                override fun onResponse(call: Call<GetParticipMemberResponse>?, response: Response<GetParticipMemberResponse>?) {
                    if(response!!.isSuccessful)
                    {
                        roomMemberlistData = response.body()!!.result

                        for(i in 0..roomMemberlistData.size-1) {
                            if(roomMemberlistData[i].userImageUrl == ""){
                                roomMemberlistData[i].userImageUrl = "http://18.188.54.59:8080/resources/upload/bg_sample.png"
                            }

                            if(i == 0){
                                requestManager.load(roomMemberlistData[0].userImageUrl).into(chat_profile1_img)
                                count = 0
                            }
                            else if(i == 1){
                                requestManager.load(roomMemberlistData[1].userImageUrl).into(chat_profile2_img)
                                count = 0
                            }
                            else if(i == 2){
                                requestManager.load(roomMemberlistData[2].userImageUrl).into(chat_profile3_img)
                                count = 0
                            }

                            else{
                                count += 1

                            }

                        }
                        countVaule = "+" + (count).toString()
                        chat_plus_member_number_tv.setText(countVaule)
                    }
                }

                override fun onFailure(call: Call<GetParticipMemberResponse>?, t: Throwable?) {
                }
            })
        } catch (e: Exception) {
        }

    }
}