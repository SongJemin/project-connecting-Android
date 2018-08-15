package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Network.Get.Response.GetChangeLocationResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.DeleteDate
import com.example.jamcom.connecting.Network.Post.DeleteFavorite
import com.example.jamcom.connecting.Network.Post.PostFavorite
import com.example.jamcom.connecting.Network.Post.Response.DeleteDateResponse
import com.example.jamcom.connecting.Network.Post.Response.DeleteFavoriteResponse
import com.example.jamcom.connecting.Network.Post.Response.PostFavoriteResponse
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.activity_room_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailActivity : AppCompatActivity() {

    var selectedPlaceName : String = ""
    var selectedPlaceHomepageUrl : String = ""
    var selectedRoadAddress : String = ""
    var selectedPhoneNum : String = ""
    var selectedX : String = ""
    var selectedY : String = ""
    var selectedPlaceImgUrl : String = ""
    var typeName : String = ""
    lateinit var restNetworkService : RestNetworkService
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수
    var selected_flag : Int = 0

    lateinit var networkService : NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

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

        selectedPlaceName = intent.getStringExtra("selectedPlaceName")
        selectedPlaceHomepageUrl = intent.getStringExtra("selectedPlaceHomepageUrl")
        selectedRoadAddress = intent.getStringExtra("selectedRoadAddress")
        selectedPhoneNum = intent.getStringExtra("selectedPhoneNum")
        selectedX = intent.getStringExtra("selectedX")
        selectedY = intent.getStringExtra("selectedY")
        selectedPlaceImgUrl = intent.getStringExtra("selectedPlaceImgUrl")
        typeName = intent.getStringExtra("typeName")

        place_detail_place_name_tv.setText(selectedPlaceName)
        place_detail_homepage_content_tv.setText(selectedPlaceHomepageUrl)
        place_detail_phone_content_tv.setText(selectedPhoneNum)

        if(typeName.equals("밥 먹자"))
        {
            place_detail_type_img.setImageResource(R.drawable.btn_rice_on)
            Log.v("TAG", "밥먹자 카테고리 선택")
        }
        else if(typeName.equals("술 먹자"))
        {
            place_detail_type_img.setImageResource(R.drawable.btn_alchol_on)
            Log.v("TAG", "술먹자 카테고리 선택")
        }
        else if(typeName.equals("카페 가자"))
        {
            place_detail_type_img.setImageResource(R.drawable.btn_cafe_on)
            Log.v("TAG", "카페가자 카테고리 선택")
        }
        else if(typeName.equals("공부하자"))
        {
            place_detail_type_img.setImageResource(R.drawable.btn_study_on)
            Log.v("TAG", "공부하자 카테고리 선택")
        }
        else if(typeName.equals("일하자"))
        {
            place_detail_type_img.setImageResource(R.drawable.btn_work_on)
            Log.v("TAG", "일하자 카테고리 선택")
        }
        else if(typeName.equals("기타"))
        {
            place_detail_type_img.setImageResource(R.drawable.btn_etc_on)
            Log.v("TAG", "기타 카테고리 선택")
        }

        requestManager.load(selectedPlaceImgUrl).centerCrop().into(place_detail_background_img)

        changeLocation()

        place_detail_heart_btn.setOnClickListener {

            if(selected_flag == 0)
            {
                place_detail_heart_btn.isSelected = true
                Toast.makeText(applicationContext, "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                selected_flag = 1
                postFavorite()
            }

            else if(selected_flag == 1)
            {
                place_detail_heart_btn.isSelected = false
                Toast.makeText(applicationContext, "찜 목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                selected_flag = 0
                deleteFavorite()
            }

        }

    }

    fun changeLocation()
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)


        var getChangeLocationResponse = restNetworkService.getSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", selectedX, selectedY)
        getChangeLocationResponse.enqueue(object : Callback<GetChangeLocationResponse> {

            override fun onResponse(call: Call<GetChangeLocationResponse>?, response: Response<GetChangeLocationResponse>?) {
                if(response!!.isSuccessful)
                {
                    if(response!!.body()!!.documents!![0]!!.road_address!! == null)
                    {
                        response!!.body()!!.documents!![0].road_address!!.address_name = ""
                    }
                    place_detail_new_address_content_tv.setText(response!!.body()!!.documents!![0].road_address!!.address_name)
                    place_detail_old_address_content_tv.setText(response!!.body()!!.documents!![0].address!!.address_name)

                }
                else
                {
                    Log.v("TAG","레스트 검색 값 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<GetChangeLocationResponse>?, t: Throwable?) {
                Log.v("TAG","검색 통신 실패")
                Log.v("TAG","검색 통신 실패"+t.toString())
            }

        })

    }

    fun postFavorite()
    {
        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var postFavorite = PostFavorite(userID, selectedPlaceName, selectedPlaceHomepageUrl, selectedRoadAddress)
        var postFavoriteResponse = networkService.postFavorite(postFavorite)
        Log.v("TAG", "찜리스트 추가 통신 전")
        postFavoriteResponse.enqueue(object : retrofit2.Callback<PostFavoriteResponse>{

            override fun onResponse(call: Call<PostFavoriteResponse>, response: Response<PostFavoriteResponse>) {
                Log.v("TAG", "찜리스트 추가 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "찜리스트 추가 전달 성공")
                }
            }

            override fun onFailure(call: Call<PostFavoriteResponse>, t: Throwable?) {
            }

        })

    }

    fun deleteFavorite()
    {

        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var userID : Int = 0
        userID = pref.getInt("userID",0)

        var favoriteName : String = ""
        favoriteName = selectedPlaceName

        Log.v("TAG", "삭제 찜 리스트 유저 번호 = " + userID)
        Log.v("TAG", "삭제 찜 리스트 가게 이름 = " + selectedPlaceName)

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var deleteFavorite = DeleteFavorite(userID, favoriteName)
        var deleteFavoriteResponse = networkService.deleteFavorite(deleteFavorite)
        Log.v("TAG", "찜 리스트 삭제 생성 통신 전")
        deleteFavoriteResponse.enqueue(object : retrofit2.Callback<DeleteFavoriteResponse>{

            override fun onResponse(call: Call<DeleteFavoriteResponse>, response: Response<DeleteFavoriteResponse>) {
                Log.v("TAG", "찜 리스트 삭제 통신 성공")
                if(response.isSuccessful){
                    Log.v("TAG", "찜 리스트 삭제 전달 성공")
                }
            }

            override fun onFailure(call: Call<DeleteFavoriteResponse>, t: Throwable?) {
            }

        })

    }
}
