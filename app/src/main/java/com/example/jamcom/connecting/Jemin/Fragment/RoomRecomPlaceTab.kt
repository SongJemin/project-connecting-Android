package com.example.jamcom.connecting.Jemin.Fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace1Adapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace2Adapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace3Adapter
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.Network.Get.Response.GetCategoryResponse
import com.example.jamcom.connecting.Network.Get.Response.GetImageSearchResponse
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_room_recom_place.*
import kotlinx.android.synthetic.main.fragment_room_recom_place.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomRecomPlaceTab : Fragment(), View.OnClickListener {


    override fun onClick(v: View?) {

/*
        val first_place_idx : Int
        first_place_idx= room_recomplace1_recyclerview.getChildAdapterPosition(v)

        val second_place_idx : Int
        second_place_idx = room_recomplace2_recyclerview.getChildAdapterPosition(v)

        val third_place_idx : Int
        third_place_idx = room_recomplace3_recyclerview.getChildAdapterPosition(v)

        Log.v("TAG","클릭숫자 확인1 : " + first_place_idx)
        Log.v("TAG","클릭숫자 확인2 : " + second_place_idx)
        Log.v("TAG","클릭숫자 확인3 : " + third_place_idx)

        if(first_place_idx != null)
        {
            Log.v("TAG","1클릭")
            Log.v("TAG","클릭숫자 확인1 : " + first_place_idx)
            Log.v("TAG","클릭숫자 확인2 : " + second_place_idx)
            Log.v("TAG","클릭숫자 확인3 : " + third_place_idx)
            selectedPlaceName = roomRecomPlace1Items[first_place_idx].place_name
            selectedPlaceHomepageUrl = roomRecomPlace1Items[first_place_idx].place_url!!
            selectedPhoneNum = roomRecomPlace1Items[first_place_idx].phone!!
            selectedRoadAddress = roomRecomPlace1Items[first_place_idx].road_address_name!!

        }
        else if(second_place_idx != null)
        {
            Log.v("TAG","2클릭")
            Log.v("TAG","클릭숫자 확인1 : " + first_place_idx)
            Log.v("TAG","클릭숫자 확인2 : " + second_place_idx)
            Log.v("TAG","클릭숫자 확인3 : " + third_place_idx)
            selectedPlaceName = roomRecomPlace2Items[second_place_idx].place_name
            selectedPlaceHomepageUrl = roomRecomPlace2Items[second_place_idx].place_url!!
            selectedPhoneNum = roomRecomPlace2Items[second_place_idx].phone!!
            selectedRoadAddress = roomRecomPlace2Items[second_place_idx].road_address_name!!

        }
        else
        {
            Log.v("TAG","3클릭")
            Log.v("TAG","클릭숫자 확인1 : " + first_place_idx)
            Log.v("TAG","클릭숫자 확인2 : " + second_place_idx)
            Log.v("TAG","클릭숫자 확인3 : " + third_place_idx)
            selectedPlaceName = roomRecomPlace3Items[third_place_idx].place_name
            selectedPlaceHomepageUrl = roomRecomPlace3Items[third_place_idx].place_url!!
            selectedPhoneNum = roomRecomPlace3Items[third_place_idx].phone!!
            selectedRoadAddress = roomRecomPlace3Items[third_place_idx].road_address_name!!

        }

        val intent = Intent(getActivity(), PlaceDetailActivity::class.java)
        intent.putExtra("selectedPlaceName", selectedPlaceName)
        intent.putExtra("selectedPlaceHomepageUrl", selectedPlaceHomepageUrl)
        intent.putExtra("selectedPhoneNum", selectedPhoneNum)
        intent.putExtra("selectedRoadAddress", selectedRoadAddress)

        Log.v("TAG", "플레이스 상세페이지로 넘기는 이름 = " + selectedPlaceName)
        Log.v("TAG", "플레이스 상세페이지로 넘기는 홈페이지 주소 = " + selectedPlaceHomepageUrl)
        Log.v("TAG", "플레이스 상세페이지로 넘기는 번호 = " + selectedPhoneNum)
        Log.v("TAG", "플레이스 상세페이지로 넘기는 도로명 주소 = " + selectedRoadAddress)

        startActivity(intent)

        // 프래그먼트 전환 시 사용
        //replaceFragment(HomeTab())
*/
    }

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
    lateinit var restNetworkService : RestNetworkService
    lateinit var roomRecomPlace1Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomFirstPlaceNames : ArrayList<String>
    lateinit var roomRecomPlace1ImageUrl : ArrayList<String>
    lateinit var roomRecomPlace1Adapter: RoomRecomPlace1Adapter

    lateinit var roomRecomPlace2Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomSecondPlaceNames : ArrayList<String>
    lateinit var roomRecomPlace2Adapter: RoomRecomPlace2Adapter
    lateinit var roomRecomPlace2ImageUrl : ArrayList<String>

    lateinit var roomRecomPlace3Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomThirdPlaceNames : ArrayList<String>
    lateinit var roomRecomPlace3Adapter: RoomRecomPlace3Adapter
    lateinit var roomRecomPlace3ImageUrl : ArrayList<String>


    var firstPlaceImgArray = arrayOfNulls<String>(11)!!
    var secondPlaceImgArray = arrayOfNulls<String>(11)!!
    var thirdPlaceImgArray = arrayOfNulls<String>(11)!!



    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수


    var x : String = ""
    var select_x : String = ""
    var y : String = ""
    var select_y : String = ""
    var typeName : String = ""
    var category_group_code : String = ""
    var flag_rank : Int = 0

    var query : String = ""

    var selectedPlaceName : String = ""
    var selectedPlaceHomepageUrl : String = ""
    var selectedRoadAddress : String = ""
    var selectedPhoneNum : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_room_recom_place, container, false)
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)
        requestManager = Glide.with(this)


        val extra = arguments
        x = extra!!.getString("x")
        y = extra!!.getString("y")
        typeName = extra!!.getString("typeName")

        roomRecomPlaceTab = this

        roomRecomPlace1ImageUrl = ArrayList()
        roomRecomPlace2ImageUrl = ArrayList()
        roomRecomPlace3ImageUrl = ArrayList()

        roomRecomPlace1Items = ArrayList()
        roomRecomPlace2Items = ArrayList()
        roomRecomPlace3Items = ArrayList()

        roomRecomFirstPlaceNames = ArrayList()
        roomRecomSecondPlaceNames = ArrayList()
        roomRecomThirdPlaceNames = ArrayList()

        if(typeName.equals("밥 먹자"))
        {
            category_group_code = "FD6"
            Log.v("TAG", "밥먹자 카테고리 선택")
        }
        else if(typeName.equals("술 먹자"))
        {
            category_group_code = "CE7"
            Log.v("TAG", "술먹자 카테고리 선택")
        }
        else if(typeName.equals("카페 가자"))
        {
            category_group_code = "CE7"
            Log.v("TAG", "카페가자 카테고리 선택")
        }
        else if(typeName.equals("공부하자"))
        {
            category_group_code = "CE7"
            Log.v("TAG", "공부하자 카테고리 선택")
        }
        else if(typeName.equals("일하자"))
        {
            category_group_code = "CE7"
            Log.v("TAG", "일하자 카테고리 선택")
        }
        else if(typeName.equals("기타"))
        {
            category_group_code = "CE7"
            Log.v("TAG", "기타 카테고리 선택")
        }

        subwayCategorySearch()

        v.room_recomplace1_recyclerview.setOnClickListener {
            val intent = Intent(getActivity(), PlaceDetailActivity::class.java)
            startActivity(intent)
        }


        return v
    }

    fun categorySearch(select_x : String, select_y : String, flag_rank : Int)
    {
        var radius : Int = 0
        Log.v("TAG", "카테고리 선택값 = " +  category_group_code)

        radius = 10000

        var getSearchCategory = restNetworkService.getCategorySearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", category_group_code, select_x, select_y, radius)
        getSearchCategory.enqueue(object : Callback<GetCategoryResponse> {

            override fun onResponse(call: Call<GetCategoryResponse>?, response: Response<GetCategoryResponse>?) {
                if(response!!.isSuccessful)
                {
                    Log.v("TAG","카테고리 카페 검색 값 가져오기 성공 " + response.body() )

                    for(i in 0..10) {

                        if(flag_rank == 1)
                        {
                            roomRecomPlace1Items.add(RoomRecomPlaceItem("Asdf", response!!.body()!!.documents[i]!!.place_name!!, response!!.body()!!.documents[i]!!.road_address_name!!,response!!.body()!!.documents[i]!!.place_url, response!!.body()!!.documents[i]!!.phone, response!!.body()!!.documents[i]!!.x, response!!.body()!!.documents[i]!!.y))

                        }
                        else if(flag_rank == 2)
                        {
                            roomRecomPlace2Items.add(RoomRecomPlaceItem("Asdf",response!!.body()!!.documents[i]!!.place_name!!, response!!.body()!!.documents[i]!!.road_address_name!!,response!!.body()!!.documents[i]!!.place_url, response!!.body()!!.documents[i]!!.phone, response!!.body()!!.documents[i]!!.x, response!!.body()!!.documents[i]!!.y))
                        }
                        else{
                            roomRecomPlace3Items.add(RoomRecomPlaceItem("Asdf",response!!.body()!!.documents[i]!!.place_name!!, response!!.body()!!.documents[i]!!.road_address_name!!,response!!.body()!!.documents[i]!!.place_url, response!!.body()!!.documents[i]!!.phone, response!!.body()!!.documents[i]!!.x, response!!.body()!!.documents[i]!!.y))
                        }

                    }

                    if(flag_rank==1)
                    {
                        imageSearch(1)
                    }

                    else if(flag_rank==2)
                    {
                        imageSearch(2)
                    }
                    else{
                        imageSearch(3)
                    }

                }
                else
                {
                    Log.v("TAG","카테고리 카페 검색 값 가져오기 실패")
                }
            }
            override fun onFailure(call: Call<GetCategoryResponse>?, t: Throwable?) {
                Log.v("TAG","카테고리 카페 서버 통신 실패"+t.toString())
            }
        })
    }

    fun subwayCategorySearch()
    {

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

                        recom_second_x = response!!.body()!!.documents[1]!!.x!!
                        recom_second_y = response!!.body()!!.documents[1]!!.y!!
                        recom_second_name = splitResult2[0]

                        recom_third_x = response!!.body()!!.documents[2]!!.x!!
                        recom_third_y = response!!.body()!!.documents[2]!!.y!!
                        recom_third_name = splitResult3[0]

                    }

                    room_recomplace1_name_tv.setText(recom_first_name)
                    room_recomplace2_name_tv.setText(recom_second_name)
                    room_recomplace3_name_tv.setText(recom_third_name)

                    select_x = recom_first_x
                    select_y = recom_first_y
                    flag_rank = 1
                    categorySearch(select_x, select_y, flag_rank)

                    select_x = recom_second_x
                    select_y = recom_second_y
                    flag_rank = 2
                    categorySearch(select_x, select_y, flag_rank)

                    select_x = recom_third_x
                    select_y = recom_third_y
                    flag_rank = 3
                    categorySearch(select_x, select_y, flag_rank)
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

    fun imageSearch(img_flag : Int)
    {

        if(img_flag==1)
        {
            for(j in 0..10) {

                var getImageSearchResponse = restNetworkService.getImageSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", roomRecomPlace1Items[j].place_name)
                getImageSearchResponse.enqueue(object : Callback<GetImageSearchResponse> {

                    override fun onResponse(call: Call<GetImageSearchResponse>?, response: Response<GetImageSearchResponse>?) {
                        if(response!!.isSuccessful)
                        {
                            query = response!!.body()!!.documents[0].image_url!!
                            firstPlaceImgArray[j] = query

                            if(j==10) {
                                roomRecomPlace1Adapter = RoomRecomPlace1Adapter(context!!, roomRecomPlace1Items, firstPlaceImgArray, requestManager)

                                roomRecomPlace1Adapter.setOnItemClickListener1(this@RoomRecomPlaceTab)
                                room_recomplace1_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                room_recomplace1_recyclerview.adapter = roomRecomPlace1Adapter
                            }
                        }
                        else
                        {
                            Log.v("TAG","이미지 검색 값 가져오기 실패")
                        }
                    }
                    override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
                        Log.v("TAG","이미지 서버 통신 실패"+t.toString())
                    }
                })
            }
        }

        else if(img_flag==2) {

            for(k in 0..10) {

                var getImageSearchResponse = restNetworkService.getImageSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", roomRecomPlace2Items[k].place_name)
                getImageSearchResponse.enqueue(object : Callback<GetImageSearchResponse> {

                    override fun onResponse(call: Call<GetImageSearchResponse>?, response: Response<GetImageSearchResponse>?) {
                        if(response!!.isSuccessful)
                        {
                            secondPlaceImgArray[k] = response!!.body()!!.documents[0].image_url!!

                            if(k==10) {
                                roomRecomPlace2Adapter = RoomRecomPlace2Adapter(context!!,roomRecomPlace2Items, secondPlaceImgArray, requestManager)

                                roomRecomPlace2Adapter.setOnItemClickListener2(this@RoomRecomPlaceTab)
                                room_recomplace2_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                room_recomplace2_recyclerview.adapter = roomRecomPlace2Adapter
                            }
                        }
                        else
                        {
                            Log.v("TAG","이미지 검색 값 가져오기 실패")
                        }
                    }
                    override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
                        Log.v("TAG","이미지 서버 통신 실패"+t.toString())
                    }
                })
            }
        }

        else{

            for(s in 0..10) {

                var getImageSearchResponse = restNetworkService.getImageSearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", roomRecomPlace3Items[s].place_name)
                getImageSearchResponse.enqueue(object : Callback<GetImageSearchResponse> {

                    override fun onResponse(call: Call<GetImageSearchResponse>?, response: Response<GetImageSearchResponse>?) {
                        if(response!!.isSuccessful)
                        {
                            thirdPlaceImgArray[s] = response!!.body()!!.documents[0].image_url!!

                            if(s==10) {

                                roomRecomPlace3Adapter = RoomRecomPlace3Adapter(context!!, roomRecomPlace3Items, thirdPlaceImgArray, requestManager)

                                roomRecomPlace3Adapter.setOnItemClickListener3(this@RoomRecomPlaceTab)
                                room_recomplace3_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                room_recomplace3_recyclerview.adapter = roomRecomPlace3Adapter
                            }
                        }
                        else
                        {
                            Log.v("TAG","이미지 검색 값 가져오기 실패")
                        }
                    }

                    override fun onFailure(call: Call<GetImageSearchResponse>?, t: Throwable?) {
                        Log.v("TAG","이미지 서버 통신 실패"+t.toString())
                    }
                })

            }
        }
    }


    companion object {
        lateinit var roomRecomPlaceTab: RoomRecomPlaceTab
        //일종의 스태틱
    }



}