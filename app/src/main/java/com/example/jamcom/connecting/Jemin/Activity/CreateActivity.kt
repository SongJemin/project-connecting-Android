package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.jamcom.connecting.R
import com.kakao.network.NetworkService
import kotlinx.android.synthetic.main.activity_create.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


        room_create_meal_btn.setOnClickListener {
            room_create_meal_btn.setSelected(true)

            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)

        }

        room_create_alcohol_btn.setOnClickListener {
            room_create_alcohol_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)
        }

        room_create_cafe_btn.setOnClickListener {
            room_create_cafe_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)
        }

        room_create_study_btn.setOnClickListener {
            room_create_study_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)
        }

        room_create_work_btn.setOnClickListener {
            room_create_work_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
            room_create_etc_btn.setSelected(false)
        }

        room_create_etc_btn.setOnClickListener {
            room_create_etc_btn.setSelected(true)

            room_create_meal_btn.setSelected(false)
            room_create_alcohol_btn.setSelected(false)
            room_create_cafe_btn.setSelected(false)
            room_create_work_btn.setSelected(false)
            room_create_study_btn.setSelected(false)
        }

        room_create_camera_btn.setOnClickListener {
            changeImage()
        }

        room_create_confirm_btn.setOnClickListener {
            val intent = Intent(applicationContext, RoomSettingActivity::class.java)
            startActivity(intent)
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
                    val photo = File(this.data.toString()) // 가져온 파일의 이름을 알아내려고 사용합니다

                    ///RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                    // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!

                    image = MultipartBody.Part.createFormData("photo", photo.name, photoBody)

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

    fun changeImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
        intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)

        fun postBoard() {


            //networkService.postPart(PostTemp.)

        }
    }

}
