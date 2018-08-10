package com.example.jamcom.connecting.Jemin.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace1Adapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace2Adapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace3Adapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.Network.Get.Response.GetCategoryResponse
import com.example.jamcom.connecting.Network.RestApplicationController
import com.example.jamcom.connecting.Network.RestNetworkService
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_room_decide.*
import kotlinx.android.synthetic.main.fragment_room_recom_place.*
import kotlinx.android.synthetic.main.fragment_room_recom_place.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomRecomPlaceTab : Fragment() {

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
    lateinit var roomRecomPlace1Adapter: RoomRecomPlace1Adapter

    lateinit var roomRecomPlace2Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomPlace2Adapter: RoomRecomPlace2Adapter

    lateinit var roomRecomPlace3Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomPlace3Adapter: RoomRecomPlace3Adapter

    var x : String = ""
    var select_x : String = ""
    var y : String = ""
    var select_y : String = ""
    var typeName : String = ""
    var category_group_code : String = ""
    var flag_rank : Int = 0




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_room_recom_place, container, false)

        val extra = arguments
        x = extra!!.getString("x")
        y = extra!!.getString("y")
        typeName = extra!!.getString("typeName")

        Log.v("TAG", "받아온 x = " + x)
        Log.v("TAG", "받아온 y = " + y)
        Log.v("TAG", "받아온 타입명 = " + typeName)



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

        return v
    }

    fun categorySearch(select_x : String, select_y : String, flag_rank : Int)
    {
        restNetworkService = RestApplicationController.getRetrofit().create(RestNetworkService::class.java)

        roomRecomPlace1Items = ArrayList()
        roomRecomPlace2Items = ArrayList()
        roomRecomPlace3Items = ArrayList()

        var radius : Int = 0
        Log.v("TAG", "카테고리 선택값 = " +  category_group_code)

        //카페일 경우

        radius = 10000

        var getSearchCategory = restNetworkService.getCategorySearch("KakaoAK 3897b8b78021e2b29c516d6276ce0b08", category_group_code, select_x, select_y, radius)
        getSearchCategory.enqueue(object : Callback<GetCategoryResponse> {

            override fun onResponse(call: Call<GetCategoryResponse>?, response: Response<GetCategoryResponse>?) {
                if(response!!.isSuccessful)
                {
                    Log.v("TAG","카테고리 카페 검색 값 가져오기 성공 " + response.body() )



                    for(i in 0..response!!.body()!!.documents.size-1) {

                        if(flag_rank == 1)
                        {
                            roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.bg_sample,response!!.body()!!.documents[i]!!.place_name!!, response!!.body()!!.documents[i]!!.road_address_name!!))
                            roomRecomPlace1Adapter = RoomRecomPlace1Adapter(roomRecomPlace1Items)
                        }
                        else if(flag_rank == 2)
                        {
                            roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.bg_sample,response!!.body()!!.documents[i]!!.place_name!!, response!!.body()!!.documents[i]!!.road_address_name!!))
                            roomRecomPlace2Adapter = RoomRecomPlace2Adapter(roomRecomPlace2Items)
                        }
                        else{
                            roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.bg_sample,response!!.body()!!.documents[i]!!.place_name!!, response!!.body()!!.documents[i]!!.road_address_name!!))
                            roomRecomPlace3Adapter = RoomRecomPlace3Adapter(roomRecomPlace3Items)
                        }

                    }

                    if(flag_rank == 1)
                    {
                        room_recomplace1_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        room_recomplace1_recyclerview.adapter = roomRecomPlace1Adapter
                    }
                    else if(flag_rank == 2)
                    {
                        room_recomplace2_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        room_recomplace2_recyclerview.adapter = roomRecomPlace2Adapter
                    }
                    else
                    {
                        room_recomplace3_recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        room_recomplace3_recyclerview.adapter = roomRecomPlace3Adapter
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


}