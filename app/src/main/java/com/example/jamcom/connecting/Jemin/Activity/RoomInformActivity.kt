package com.example.jamcom.connecting.Jemin.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.inputmethod.ExtractedTextRequest
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomMemberAdapter
import com.example.jamcom.connecting.Jemin.Fragment.*
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.Network.Get.GetLocationMessage
import com.example.jamcom.connecting.Network.Get.GetParticipMemberMessage
import com.example.jamcom.connecting.Network.Get.GetRoomDetailMessage
import com.example.jamcom.connecting.Network.Get.Response.*
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room_inform.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_room_member.view.*
import kotlinx.android.synthetic.main.fragment_room_recom_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomInformActivity : AppCompatActivity() {

    private val FRAGMENT1 = 1
    private val FRAGMENT2 = 2
    private val FRAGMENT3 = 3
    private val FRAGMENT4 = 4
    internal lateinit var locationManager: LocationManager
    private var decideTv: TextView? = null
    private var memberTv: TextView? = null
    private var recomPlaceTv: TextView? = null
    private var myInformTv: TextView? = null
    internal lateinit var myToolbar: Toolbar
    lateinit var locationData : java.util.ArrayList<GetLocationMessage>

    var count : Int = 0
    var countVaule : String = ""

    lateinit var restNetworkService : RestNetworkService
    var roomDetailData : ArrayList<GetRoomDetailMessage> = ArrayList()
    lateinit var networkService : NetworkService
    lateinit var requestManager: RequestManager
    lateinit var roomMemberItems: ArrayList<RoomMemberItem>
    lateinit var memberlistData : ArrayList<GetParticipMemberMessage>

    var roomName : String = ""
    var roomStartDate : String = ""
    var roomEndDate : String = ""
    var typeName : String = ""
    var roomCreaterID : Int = 0
    var roomID : Int = 0

    var promiseLatSum : Double = 0.0
    var recomPromiseLat : Double = 0.0
    var promiseLonSum : Double = 0.0
    var recomPromiseLon : Double = 0.0
    var x : String = ""
    var y : String = ""

    // 추천 장소(지하철역) 랭킹 1위 좌표
    var recom_first_x : String = ""
    var recom_first_y : String = ""
    var recom_first_name : String = ""

    // 추천 장소(지하철역) 랭킹 2위 좌표
    var recom_second_x : String = ""
    var recom_second_y : String = ""
    var recom_second_name : String = ""

    // 추천 장소(지하철역) 랭킹 3위 좌표
    var recom_third_x : String = ""
    var recom_third_y : String = ""
    var recom_third_name : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_inform)

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

        // 위젯에 대한 참조
        decideTv = findViewById(R.id.room_inform_decide_tv) as TextView
        memberTv = findViewById(R.id.room_inform_member_tv) as TextView
        recomPlaceTv = findViewById(R.id.room_inform_recomplace_tv) as TextView
        myInformTv = findViewById(R.id.room_inform_myinform_tv) as TextView

        // 탭 버튼에 대한 리스너 연결

        roomID = intent.getIntExtra("roomID", 0)
        Log.v("TAG", "넘겨받은 룸 번호 = " + roomID)

        getRoomDetail()
        getLocation()
        getParticipMemberList()
        subwayCategorySearch()
        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(FRAGMENT1)

        room_inform_decide_tv.setSelected(true)

        room_inform_decide_tv.setOnClickListener {
            room_inform_decide_tv.isSelected = true
            room_inform_decide_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_member_tv.isSelected = false
            room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_recomplace_tv.isSelected = false
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_myinform_tv.isSelected = false
            room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))


            callFragment(FRAGMENT1)
        }

        room_inform_member_tv.setOnClickListener {

            room_inform_member_tv.isSelected = true
            room_inform_member_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_decide_tv.isSelected = false
            room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_recomplace_tv.isSelected = false
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_myinform_tv.isSelected = false
            room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))
            callFragment(FRAGMENT2)
        }

        room_inform_recomplace_tv.setOnClickListener {
            room_inform_recomplace_tv.isSelected = true
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_decide_tv.isSelected = false
            room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_member_tv.isSelected = false
            room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_myinform_tv.isSelected = false
            room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))
            callFragment(FRAGMENT3)
        }

        room_inform_myinform_tv.setOnClickListener {
            room_inform_myinform_tv.isSelected = true
            room_inform_myinform_tv.setTextColor(Color.parseColor("#764dd1"))

            room_inform_decide_tv.isSelected = false
            room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_recomplace_tv.isSelected = false
            room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
            room_inform_member_tv.isSelected = false
            room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
            // '버튼4' 클릭 시 '프래그먼트2' 호출
            callFragment(FRAGMENT4)
        }


        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(intent)
            finish()
        }

        //마시멜로 이상이면 권한 요청하기
        if (Build.VERSION.SDK_INT >= 23) {
            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(this@RoomInformActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@RoomInformActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@RoomInformActivity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                requestMyLocation()
            }//권한이 있는 경우
        } else {
            requestMyLocation()
        }//마시멜로 아래

    }

    //나의 위치 요청
    fun requestMyLocation() {
        if (ContextCompat.checkSelfPermission(this@RoomInformActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@RoomInformActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }




    private fun callFragment(frament_no: Int) {

        // 프래그먼트 사용을 위해
        val transaction = supportFragmentManager.beginTransaction()

        when (frament_no) {
            1 -> {
                // '프래그먼트1' 호출
                val roomDecideTab = RoomDecideTab()
                val bundle = Bundle()
                bundle.putInt("roomID", roomID)
                Log.v("TAG","상세정보에서 보내는 방 번호 = "+ roomID)
                roomDecideTab.setArguments(bundle)

                transaction.replace(R.id.room_inform_frame_layout, roomDecideTab)
                transaction.commit()
            }

            2 -> {
                // '프래그먼트2' 호출
                val roomMemberTab = RoomMemberTab()
                val bundle = Bundle()
                bundle.putInt("roomID", roomID)
                Log.v("TAG","상세정보에서 보내는 방 번호 = "+ roomID)
                roomMemberTab.setArguments(bundle)

                transaction.replace(R.id.room_inform_frame_layout, roomMemberTab)
                transaction.commit()
            }


            3 -> {
                // '프래그먼트4' 호출
                val roomRecomPlaceTab = RoomRecomPlaceTab()
                val bundle = Bundle()
                bundle.putString("x", x)
                bundle.putString("y", y)
                bundle.putString("typeName", typeName)
                Log.v("TAG","상세정보에서 보내는 x = "+ x)
                Log.v("TAG","상세정보에서 보내는 y = "+ y)
                Log.v("TAG","상세정보에서 보내는 타입 = "+ typeName)
                roomRecomPlaceTab.setArguments(bundle)

                transaction.replace(R.id.room_inform_frame_layout, roomRecomPlaceTab)
                transaction.commit()
            }

            4 -> {
                // '프래그먼트5' 호출
                val roomMyInformTab = RoomMyInformTab()
                transaction.replace(R.id.room_inform_frame_layout, roomMyInformTab)
                transaction.commit()
            }
        }

    }

    fun getRoomDetail(){

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getRoomDetailRespnose = networkService.getRoomDetail(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getRoomDetailRespnose.enqueue(object : Callback<GetRoomDetailRespnose> {
                override fun onResponse(call: Call<GetRoomDetailRespnose>?, response: Response<GetRoomDetailRespnose>?) {
                    Log.v("TAG","방 세부사항 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","방 세부사항 값 갖고오기 성공")
                        roomDetailData = response.body()!!.result

                        roomName = roomDetailData[0].roomName
                        roomStartDate = roomDetailData[0].roomStartDate
                        roomEndDate = roomDetailData[0].roomEndDate
                        typeName = roomDetailData[0].typeName
                        roomCreaterID = roomDetailData[0].roomCreaterID
                        requestManager.load(roomDetailData[0].img_url).into(room_inform_bg_img)

                        room_inform_title_tv.setText(roomName)
                        room_inform_type_tv.setText(typeName)

                    }
                }

                override fun onFailure(call: Call<GetRoomDetailRespnose>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }


    private fun getParticipMemberList() {
        roomMemberItems = ArrayList()
        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getParticipMemberResponse = networkService.getParticipMemberList(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getParticipMemberResponse.enqueue(object : Callback<GetParticipMemberResponse> {
                override fun onResponse(call: Call<GetParticipMemberResponse>?, response: Response<GetParticipMemberResponse>?) {
                    Log.v("TAG","상세정보 참여 멤버 리스트 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","상세정보 참여 멤버 리스트 값 갖고오기 성공")
                        memberlistData = response.body()!!.result
                        var test : String = ""
                        test = memberlistData.toString()
                        Log.v("TAG","상세정보 참여 멤버 리스트 데이터 값"+ test)
                        Log.v("TAG","상세정보 참여 멤버 리스트 데이터 크기 : "+ memberlistData.size)

                        for(i in 0..memberlistData.size-1) {
                            if(memberlistData[i].userImageUrl == ""){
                                memberlistData[i].userImageUrl = "http://18.188.54.59:8080/resources/upload/bg_sample.png"
                            }

                            if(i == 0){
                                Log.v("TAG","1번 도착")
                                requestManager.load(memberlistData[0].userImageUrl).into(room_inform_profile1_img)
                            }
                            else if(i == 1){
                                Log.v("TAG","2번 도착")
                                requestManager.load(memberlistData[1].userImageUrl).into(room_inform_profile2_img)
                            }
                            else if(i == 2){
                                Log.v("TAG","3번 도착")
                                requestManager.load(memberlistData[2].userImageUrl).into(room_inform_profile3_img)
                            }

                            else{
                                count += 1

                            }

                        }

                        countVaule = "+" + count.toString()
                        room_inform_plus_member_number_tv.setText(countVaule)

                    }
                }

                override fun onFailure(call: Call<GetParticipMemberResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    private fun getLocation() {
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getLocationResponse = networkService.getLocation(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getLocationResponse.enqueue(object : Callback<GetLocationResponse> {
                override fun onResponse(call: Call<GetLocationResponse>?, response: Response<GetLocationResponse>?) {
                    Log.v("TAG","위도경도 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","위도경도 값 갖고오기 성공")
                        locationData = response.body()!!.result
                        var test : String = ""
                        test = locationData.toString()
                        Log.v("TAG","위도경도 데이터 값"+ test)

                        for(i in 0..locationData.size-1) {
                            promiseLatSum += locationData[i].promiseLat!!
                            promiseLonSum += locationData[i].promiseLon!!
                            count += 1
                            //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                        }

                        recomPromiseLat = promiseLatSum/count
                        recomPromiseLon = promiseLonSum/count
                        Log.v("TAG", "위도 합 = " + promiseLatSum)
                        Log.v("TAG", "경도 합 = " + promiseLonSum)
                        Log.v("TAG", "추천 위도 = " + recomPromiseLat)
                        Log.v("TAG", "추천 경도 = " + recomPromiseLon)

                        x = recomPromiseLon.toString()
                        y = recomPromiseLat.toString()

                    }
                }

                override fun onFailure(call: Call<GetLocationResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    fun subwayCategorySearch()
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)

        var subway_group_code : String = ""

        var radius : Int = 0

        subway_group_code = "SW8"
        radius = 10000

        var getSearchCategory = restNetworkService.getCategorySearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", subway_group_code, x, y, radius)
        getSearchCategory.enqueue(object : Callback<GetCategoryResponse> {

            override fun onResponse(call: Call<GetCategoryResponse>?, response: Response<GetCategoryResponse>?) {
                if(response!!.isSuccessful)
                {
                    if(response!!.body()!!.documents.size == 0)
                    {

                    }

                    else
                    {
                        val splitResult1 = response!!.body()!!.documents[0]!!.place_name!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var count : Int = 0

                        for (sp in splitResult1) {
                            splitResult1[count] = sp
                            count += 1
                        }

                        val splitResult2 = response!!.body()!!.documents[1]!!.place_name!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var count2 : Int = 0

                        for (sp in splitResult2) {
                            splitResult2[count2] = sp
                            count2 += 1
                        }

                        val splitResult3 = response!!.body()!!.documents[2]!!.place_name!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        var count3 : Int = 0

                        for (sp in splitResult3) {
                            splitResult3[count3] = sp
                            count3 += 1
                        }


                        recom_second_name = splitResult2[0]


                        recom_first_x = response!!.body()!!.documents[0]!!.x!!
                        recom_first_y = response!!.body()!!.documents[0]!!.y!!
                        recom_first_name = splitResult1[0]
                        Log.v("TAG", "1등 x = " + recom_first_x)
                        Log.v("TAG", "1등 y = " + recom_first_y)

                        recom_second_x = response!!.body()!!.documents[1]!!.x!!
                        recom_second_y = response!!.body()!!.documents[1]!!.y!!
                        recom_second_name = splitResult2[0]
                        Log.v("TAG", "2등 x = " + recom_second_x)
                        Log.v("TAG", "2등 y = " + recom_second_y)

                        recom_third_x = response!!.body()!!.documents[2]!!.x!!
                        recom_third_y = response!!.body()!!.documents[2]!!.y!!
                        recom_third_name = splitResult3[0]
                        Log.v("TAG", "3등 x = " + recom_third_x)
                        Log.v("TAG", "3등 y = " + recom_third_y)


                    }

                }
                else
                {
                    Log.v("TAG","카테고리 검색 값 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<GetCategoryResponse>?, t: Throwable?) {
                Log.v("TAG","카테고리 서버 통신 실패"+t.toString())
            }

        })

    }


}