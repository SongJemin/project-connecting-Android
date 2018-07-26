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
import com.example.jamcom.connecting.Jemin.Adapter.HomeListAdapter
import com.example.jamcom.connecting.Jemin.Adapter.MyDibsAdapter
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*


/**
 * A simple [Fragment] subclass.
 */
class MyPageFragment : Fragment(), View.OnClickListener {

    lateinit var mydibsListItem: ArrayList<MyDibsListItem>
    lateinit var myDibsAdapter: MyDibsAdapter


    override fun onClick(v: View?) {
        val idx : Int = mypage_list_recyclerview.getChildAdapterPosition(v)
        Log.v("TAG","마이페이지 감지 포지션 = " + idx)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_mypage, container, false)

        mydibsListItem = ArrayList()
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))
        mydibsListItem.add(MyDibsListItem(R.drawable.bg_sample))

        myDibsAdapter = MyDibsAdapter(mydibsListItem)
        myDibsAdapter.setOnItemClickListener(this)
        v.mypage_list_recyclerview.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.HORIZONTAL, false)
        v.mypage_list_recyclerview.adapter = myDibsAdapter

        return v
    }

}