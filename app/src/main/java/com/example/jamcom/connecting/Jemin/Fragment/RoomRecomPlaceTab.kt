package com.example.jamcom.connecting.Jemin.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace1Adapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace2Adapter
import com.example.jamcom.connecting.Jemin.Adapter.RoomRecomPlace3Adapter
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_room_recom_place.view.*

class RoomRecomPlaceTab : Fragment() {

    lateinit var roomRecomPlace1Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomPlace1Adapter: RoomRecomPlace1Adapter

    lateinit var roomRecomPlace2Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomPlace2Adapter: RoomRecomPlace2Adapter

    lateinit var roomRecomPlace3Items : ArrayList<RoomRecomPlaceItem>
    lateinit var roomRecomPlace3Adapter: RoomRecomPlace3Adapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_room_recom_place, container, false)

        roomRecomPlace1Items = ArrayList()
        roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.test1,"흘림목", "서울시 노원구 월계3동"))
        roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.test2,"지금 먹고 싶은 곱창", "서울시 노원구 공릉2동"))
        roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.test3,"말이 필요 없는 모듬전", "서울시 노원구 상계1동"))
        roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.test4,"맛잇겠지", "서울시 노원구 하계1동"))
        roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.test1,"테스트 타이틀1", "서울시 노원구 중계2동"))
        roomRecomPlace1Items.add(RoomRecomPlaceItem(R.drawable.test2,"테스트 타이틀2", "서울시 노원구 월계1동"))
        // Inflate the layout for this fragment

        roomRecomPlace1Adapter = RoomRecomPlace1Adapter(roomRecomPlace1Items)
        v.room_recomplace1_recyclerview.layoutManager = GridLayoutManager(v.context,2)
        v.room_recomplace1_recyclerview.adapter = roomRecomPlace1Adapter

        roomRecomPlace2Items = ArrayList()
        roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.test4,"꾸에엑", "313호"))
        roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.test2,"히히히히", "우리집" ))
        roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.test3,"테스트테스트", "너네집"))
        roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.test1,"귀찮...", "모두의집"))
        roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.test3,"화이팅!", "학교"))
        roomRecomPlace2Items.add(RoomRecomPlaceItem(R.drawable.test4,"연결고리", "동방"))
        // Inflate the layout for this fragment

        roomRecomPlace2Adapter = RoomRecomPlace2Adapter(roomRecomPlace2Items)
        v.room_recomplace2_recyclerview.layoutManager = GridLayoutManager(v.context,2)
        v.room_recomplace2_recyclerview.adapter = roomRecomPlace2Adapter

        roomRecomPlace3Items = ArrayList()
        roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.test2,"더쓸게없다", "313호"))
        roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.test1,"힘들어", "우리집" ))
        roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.test4,"ㄱㄱㄱㄱ", "너네집"))
        roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.test3,"ㄴㄴㄴㄴㄴ", "모두의집"))
        roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.test2,"ㄷㄷㄷ!", "학교"))
        roomRecomPlace3Items.add(RoomRecomPlaceItem(R.drawable.test1,"ㄹㄹㄹ", "동방"))
        // Inflate the layout for this fragment

        roomRecomPlace3Adapter = RoomRecomPlace3Adapter(roomRecomPlace3Items)
        v.room_recomplace3_recyclerview.layoutManager = GridLayoutManager(v.context,2)
        v.room_recomplace3_recyclerview.adapter = roomRecomPlace3Adapter

        return v
    }
}