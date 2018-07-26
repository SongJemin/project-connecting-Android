package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Activity.RoomInformActivity
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), View.OnClickListener {

    internal lateinit var myToolbar: Toolbar
    override fun onClick(v: View?) {
        val idx : Int = home_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","클릭이벤트 감지 포지션 = " + idx)

        val intent = Intent(getActivity(), RoomInformActivity::class.java)
        startActivity(intent)

       // callFragment(RoomInformFragment())
    }

    lateinit var homeListItem: ArrayList<HomeListItem>
    lateinit var homeListAdapter : HomeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        myToolbar = v.findViewById<View>(R.id.my_toolbar) as Toolbar
        homeListItem = ArrayList()
        homeListItem.add(HomeListItem("진비 생일 파티!", "2018. 07. 21", "서울시 노원구 공릉2동", R.drawable.bg_sample, R.drawable.song, R.drawable.eunsu))
        homeListItem.add(HomeListItem("제민이와 아이들", "2018. 09. 06", "서울시 노원구 하계 1동", R.drawable.bg_sample, R.drawable.jihye, R.drawable.chun))
        homeListItem.add(HomeListItem("오랜만에 컴공주", "2018. 10. 11", "서울시 노원구 월계 1동", R.drawable.bg_sample, R.drawable.eunsu, R.drawable.jihye))
        homeListItem.add(HomeListItem("글로벌소프트웨어 공모전", "2018. 03. 21", "미래관 313호", R.drawable.bg_sample, R.drawable.song, R.drawable.jihye))
        homeListItem.add(HomeListItem("졸업작품 회의", "2018. 07. 26", "이씨 동방", R.drawable.bg_sample, R.drawable.chun, R.drawable.eunsu))
        homeListItem.add(HomeListItem("제민이 생일", "2018. 05. 06", "서울시 노원구 월계3동", R.drawable.bg_sample, R.drawable.song, R.drawable.eunsu))
        homeListItem.add(HomeListItem("우리팀 회식", "2018. 08. 06", "공릉 술집", R.drawable.bg_sample, R.drawable.jihye, R.drawable.eunsu))

        homeListAdapter = HomeListAdapter(homeListItem)
        homeListAdapter.setOnItemClickListener(this)
        v.home_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
        v.home_list_recyclerview.adapter = homeListAdapter

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

}