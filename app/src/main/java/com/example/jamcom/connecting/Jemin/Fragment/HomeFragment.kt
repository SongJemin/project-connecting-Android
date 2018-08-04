package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.RoomInformActivity
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Network.ApplicationController
import com.example.jamcom.connecting.Network.Get.GetHomeListMessage
import com.example.jamcom.connecting.Network.Get.Response.GetHomeListResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.jamcom.connecting.Old.retrofit.APIService
import com.example.jamcom.connecting.Old.retrofit.ApiClient
import com.example.jamcom.connecting.Old.retrofit.list.ListData
import com.google.gson.Gson








/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    var homeListItems : java.util.ArrayList<HomeListItem> = java.util.ArrayList()
    lateinit var homelistData : ArrayList<GetHomeListMessage>
    internal lateinit var myToolbar: Toolbar
    var userID : Int = 0
    var roomID : Int = 0
    lateinit var requestManager : RequestManager // 이미지를 불러올 때 처리하는 변수



    override fun onClick(v: View?) {
        val idx : Int = home_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","클릭이벤트 감지 포지션 = " + idx)
        roomID = homelistData[idx].roomID
        Log.v("TAG", "선택 룸 번호 = " + roomID)

        val intent = Intent(getActivity(), RoomInformActivity::class.java)
        intent.putExtra("roomID", roomID)
        startActivity(intent)

       // callFragment(RoomInformFragment())
    }

    lateinit var networkService : NetworkService
    lateinit var homeListItem: ArrayList<HomeListItem>
    lateinit var homeListAdapter : HomeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        myToolbar = v.findViewById<View>(R.id.my_toolbar) as Toolbar
        requestManager = Glide.with(this)


        val pref = this.activity!!.getSharedPreferences("pref", Activity.MODE_PRIVATE)
        userID = pref.getInt("userID",0)
        Log.v("TAG","홈프래그먼트 유저아이디 = " + userID)
       // networkService = ApplicationController.instance.networkSerVice // 어플리케이션을 실행하자마자 어플리케이션 콘트롤러가 실행되는데 그 때 사용?

        getHomeList(v)
        //homeListItem = ArrayList()
        //homeListItem.add(HomeListItem("진비 생일 파티!", "2018. 07. 21", "2018. 07. 22", "서울시 노원구 공릉2동", R.drawable.bg_sample, R.drawable.song, R.drawable.eunsu))
        //homeListItem.add(HomeListItem("제민이와 아이들", "2018. 09. 06", "2018. 09. 21", "서울시 노원구 하계 1동", R.drawable.bg_sample, R.drawable.jihye, R.drawable.chun))
        //homeListItem.add(HomeListItem("오랜만에 컴공주", "2018. 10. 11", "2018. 09. 21", "서울시 노원구 월계 1동", R.drawable.bg_sample, R.drawable.eunsu, R.drawable.jihye))
        //homeListItem.add(HomeListItem("글로벌소프트웨어 공모전", "2018. 03. 21", "2018. 09. 21","미래관 313호", R.drawable.bg_sample, R.drawable.song, R.drawable.jihye))
        //homeListItem.add(HomeListItem("졸업작품 회의", "2018. 07. 26","2018. 09. 21", "이씨 동방", R.drawable.bg_sample, R.drawable.chun, R.drawable.eunsu))
        //homeListItem.add(HomeListItem("제민이 생일", "2018. 05. 06","2018. 09. 21", "서울시 노원구 월계3동", R.drawable.bg_sample, R.drawable.song, R.drawable.eunsu))
        //homeListItem.add(HomeListItem("우리팀 회식", "2018. 08. 06","2018. 09. 21", "공릉 술집", R.drawable.bg_sample, R.drawable.jihye, R.drawable.eunsu))

        //homeListAdapter = HomeListAdapter(homeListItem)
        //homeListAdapter.setOnItemClickListener(this)
        //v.home_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
       // v.home_list_recyclerview.adapter = homeListAdapter

        return v
    }

    fun replaceFragment(fragment: Fragment)
    {// 프래그먼트 사용을 위해
        val fm = childFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun callFragment(frament : Fragment) {

        // 프래그먼트 사용을 위해
        val transaction = activity!!.supportFragmentManager.beginTransaction()

                // '프래그먼트1' 호출
                val roomInformFragment = RoomInformFragment()
                transaction.replace(R.id.fragment_container, roomInformFragment)
                transaction.commit()

    }
/*
    fun get(v : View)
    {

        var getHomeLIstResponse = networkService.getHomeList("1") // 네트워크 서비스의 getContent 함수를 받아옴
        getHomeLIstResponse.enqueue(object : Callback<GetHomeListResponse> {
            override fun onResponse(call: Call<GetHomeListResponse>?, response: Response<GetHomeListResponse>?) {
                Log.v("TAG","홈리스트 GET 통신 성공")
                if(response!!.isSuccessful)
                {
                    Log.v("TAG","홈리스트 값 갖고오기 성공")
                    homelistData = response.body()!!.result
                    homelistData[0]
                    var test : String = ""
                    test = homelistData.toString()
                    Log.v("TAG","홈리스트 데이터 값"+ test)
                    /*
                    for(i in 0..data.size-1) {

                        if (data[i].department == null){
                            data[i].department= " "
                            Log.v("TAG","널값 발견")
                        }

                        if (data[i].img_url!!.size == 0){
                            data[i].img_url!!.add("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531124962614.png")
                        }

                        if(data[i].aim == null){
                            data[i].aim=="null"
                        }
                        else if(data[i].title == null){
                            data[i].title=="null"
                        }
                        else if(data[i].area == null){
                            data[i].area=="null"
                        }
                        projectItems.add(ProjectItem(data[i].img_url!![0], data[i].title, data[i].area, data[i].department, data[i].aim))
                        //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                        projectAdapter = ProjectAdapter(projectItems,requestManager)
                    }

                    projectAdapter.setOnItemClickListener(this@HomeFragment)
                    v.recyclerview.layoutManager = GridLayoutManager(v.context, 2)
                    v.recyclerview.adapter = projectAdapter
*/
                }
            }

            override fun onFailure(call: Call<GetHomeListResponse>?, t: Throwable?) {
                Log.v("TAG","통신 실패")
            }
        })
    }
*/
    private fun getHomeList(v : View) {

        try {

            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getHomeLIstResponse = networkService.getHomeList(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getHomeLIstResponse.enqueue(object : Callback<GetHomeListResponse> {
                override fun onResponse(call: Call<GetHomeListResponse>?, response: Response<GetHomeListResponse>?) {
                    Log.v("TAG","홈리스트 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","홈리스트 값 갖고오기 성공")
                        homelistData = response.body()!!.result
                        var test : String = ""
                        test = homelistData.toString()
                        Log.v("TAG","홈리스트 데이터 값"+ test)

                        for(i in 0..homelistData.size-1) {
                            if(homelistData[i].img_url == ""){
                                homelistData[i].img_url = "http://18.188.54.59:8080/resources/upload/bg_sample.png"
                            }
                            homeListItems.add(HomeListItem(homelistData[i].roomID, homelistData[i].roomName!!, homelistData[i].roomStartDate!!, homelistData[i].roomEndDate!!, homelistData[i].typeName!!, R.drawable.song, R.drawable.eunsu, homelistData[i].img_url))
                            //projectItems.add(ProjectItem("https://project-cowalker.s3.ap-northeast-2.amazonaws.com/1531113346984.jpg", "ㅁㄴㅇㅎ", "ㅁㄴㅇㄹㄴㅁㅇㅎ", "ㅁㄴㅇㄹ", "ㅇㅎㅁㄴㅇㄹ"))
                            homeListAdapter = HomeListAdapter(homeListItems, requestManager)
                        }

                        homeListAdapter.setOnItemClickListener(this@HomeFragment)
                        v.home_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
                        v.home_list_recyclerview.adapter = homeListAdapter

                    }
                }

                override fun onFailure(call: Call<GetHomeListResponse>?, t: Throwable?) {
                    Log.v("TAG","통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }


}