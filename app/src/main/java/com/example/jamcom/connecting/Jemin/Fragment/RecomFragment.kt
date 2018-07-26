package com.example.jamcom.connecting.Jemin.Fragment

/**
 * Created by JAMCOM on 2018-03-27.
 */

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Adapter.RecomListAdapter
import com.example.jamcom.connecting.Jemin.Item.RecomListItem

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_recom.*
import kotlinx.android.synthetic.main.fragment_recom.view.*


/**
 * A simple [Fragment] subclass.
 */
class RecomFragment : Fragment(), View.OnClickListener {

    lateinit var recomListItem: ArrayList<RecomListItem>
    lateinit var recomListAdapter : RecomListAdapter

    override fun onClick(v: View?) {
        val idx : Int = recom_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","추천 프래그먼트 클릭이벤트 감지 포지션 = " + idx)

        // callFragment(RoomInformFragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_recom, container, false)
        // Inflate the layout for this fragmen
        recomListItem = ArrayList()

        recomListItem.add(RecomListItem(R.drawable.bg_sample, "이태원 핫플레이스 TOP 10"))
        recomListItem.add(RecomListItem(R.drawable.bg_sample, "요즘 가기 좋은 건대 루프탑"))
        recomListItem.add(RecomListItem(R.drawable.bg_sample, "광명 피플의 예쁜 카페 투어"))
        recomListItem.add(RecomListItem(R.drawable.bg_sample, "석계역 맛집 TOP 10"))
        recomListItem.add(RecomListItem(R.drawable.bg_sample, "서울 과기대 맛집 TOP 20"))
        recomListItem.add(RecomListItem(R.drawable.bg_sample, "공릉 떡볶이 맛집 TOP 5"))

        recomListAdapter = RecomListAdapter(recomListItem)
        recomListAdapter.setOnItemClickListener(this)
        v.recom_list_recyclerview.layoutManager = LinearLayoutManager(v.context)
        v.recom_list_recyclerview.adapter = recomListAdapter
        return v
    }

}// Required empty public constructor