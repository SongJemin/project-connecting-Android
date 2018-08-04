package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.jamcom.connecting.Network.Get.GetRoomIDMessage
import com.example.jamcom.connecting.Network.Get.Response.GetRoomIDResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.Response.PostRoomTestResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_create.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class CreateActivity : AppCompatActivity() {

    lateinit var networkService : NetworkService
    private val REQ_CODE_SELECT_IMAGE = 100
    lateinit var data : Uri
    internal lateinit var myToolbar: Toolbar
    private var image : MultipartBody.Part? = null
    internal lateinit var context: Context

    lateinit var roomIDData : ArrayList<GetRoomIDMessage>
    var roomName : String = ""
    var roomTypeID : Int = 0
    var roomID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

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
        context = this

        room_create_meal_btn.setOnClickListener {
            room_create_meal_btn.setSelected(true)

            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)

            roomTypeID = 1

        }

        room_create_alcohol_btn.setOnClickListener {
            room_create_alcohol_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)

            roomTypeID = 2
        }

        room_create_cafe_btn.setOnClickListener {
            room_create_cafe_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)

            roomTypeID = 3
        }

        room_create_study_btn.setOnClickListener {
            room_create_study_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)

            roomTypeID = 4
        }

        room_create_work_btn.setOnClickListener {
            room_create_work_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)

            roomTypeID = 5
        }

        room_create_etc_btn.setOnClickListener {
            room_create_etc_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_study_btn.setSelected(false)

            roomTypeID = 6
        }

        room_create_camera_btn.setOnClickListener {
            changeImage()
        }

        room_create_confirm_btn.setOnClickListener {

            postRoom()
            //roomName = room_create_name_edit.text.toString()

            //val intent = Intent(applicationContext, RoomSettingActivity::class.java)
            //intent.putExtra("roomName", roomName)
            //intent.putExtra("roomTypeID", roomTypeID)
            //startActivity(intent)
        }


    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //if(ApplicationController.getInstance().is)
                    this.data = data!!.data
                    Log.v("이미지", this.data.toString())

                    val options = BitmapFactory.Options()

                    var input: InputStream? = null // here, you need to get your context.
                    try {
                        input = contentResolver.openInputStream(this.data)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(input, null, options) // InputStream 으로부터 Bitmap 을 만들어 준다.
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                    val img = File(getRealPathFromURI(context,this.data).toString()) // 가져온 파일의 이름을 알아내려고 사용합니다

                    Log.v("TAG","이미지 이름 = " + img)
                    Log.v("TAG","이미지 바디 = " + photoBody.toString())

                    ///RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                    // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!


                    image = MultipartBody.Part.createFormData("image", img.name, photoBody)
                    Log.v("TAG", "이미지 값 = "+image)

                    //body = MultipartBody.Part.createFormData("image", photo.getName(), profile_pic);

                    Glide.with(this)
                            .load(data.data)
                            .centerCrop()
                            .into(room_create_background_img)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    fun changeImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)

        fun postBoard() {


            //networkService.postPart(PostTemp.)

        }
    }


    fun postRoom() {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)
        var roomCreaterIDValue : String = ""
        roomCreaterIDValue = userID.toString()
        var typeIDValue : String = ""
        typeIDValue = roomTypeID.toString()
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        val roomCreaterID = RequestBody.create(MediaType.parse("text.plain"), roomCreaterIDValue)
        val roomName = RequestBody.create(MediaType.parse("text.plain"), room_create_name_edit.text.toString())
        val roomStartDate = RequestBody.create(MediaType.parse("text.plain"),"" )
        val roomEndDate = RequestBody.create(MediaType.parse("text.plain"), "")
        val roomTypeID = RequestBody.create(MediaType.parse("text.plain"), typeIDValue)

      //  var roomCreaterID : Int
       //         roomCreaterID= 1
        //var roomName : String
         //       roomName = "testtest2"
        //var roomStartDate : String
         //      roomStartDate= "2018-11-12"
        //var roomEndDate : String
         //      roomEndDate= "2018-12-12"
        //var roomTypeID : Int
         //       roomTypeID= 1

        val postRoomTestResponse = networkService.postRoomTest(roomCreaterID, roomName,roomStartDate,roomEndDate,roomTypeID,image)

        Log.v("TAG", "테스트 방 개설 생성 전송 : 생성자 아이디 = " + roomCreaterIDValue + ", 제목 = " + room_create_name_edit.text.toString() + ", 시작날짜 = " + roomStartDate
                + ", 끝날짜 = " + roomEndDate + ", 타입아이디 = " + typeIDValue + ", 이미지 = " + image)

        postRoomTestResponse.enqueue(object : retrofit2.Callback<PostRoomTestResponse>{

            override fun onResponse(call: Call<PostRoomTestResponse>, response: Response<PostRoomTestResponse>) {
                Log.v("TAG", "테스트 방개설 통신 성공")
                if(response.isSuccessful){
                    var message = response!!.body()

                    Log.v("TAG", "테스트 방 개설 값 전달 성공"+ message.toString())
                    getRoomID()

                }
                else{
                    Toast.makeText(applicationContext,"프로젝트 이미지를 선택해주세요", Toast.LENGTH_SHORT).show()

                    Log.v("TAG", "테스트 방 개설 값 전달 실패"+ response!!.body().toString())
                }
            }

            override fun onFailure(call: Call<PostRoomTestResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"테스트 방 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getRoomID() {

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getRoomIDResponse = networkService.getRoomID() // 네트워크 서비스의 getContent 함수를 받아옴
            Log.v("TAG","생성한 방ID GET 통신 준비")
            getRoomIDResponse.enqueue(object : Callback<GetRoomIDResponse> {
                override fun onResponse(call: Call<GetRoomIDResponse>?, response: Response<GetRoomIDResponse>?) {
                    Log.v("TAG","생성한 방ID GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","생성한 방ID 값 갖고오기 성공")
                        roomIDData = response.body()!!.result

                        roomID = roomIDData[0].roomID
                        Log.v("TAG", "리턴 방 ID = " + roomID)

                        var intent = Intent(applicationContext, RoomSettingActivity::class.java)
                        intent.putExtra("roomID", roomID)
                        startActivity(intent)

                        //postPromise()
                    }
                }

                override fun onFailure(call: Call<GetRoomIDResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }



}
