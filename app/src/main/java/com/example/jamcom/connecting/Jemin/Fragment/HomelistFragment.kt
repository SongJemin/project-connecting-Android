package com.example.jamcom.connecting.Jemin.Fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_homelist.*
import kotlinx.android.synthetic.main.fragment_homelist.view.*

class HomelistFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v)
        {
            // 진행 중인 약속 리스트 표시
            homelist_proceeding_btn -> {
                homelist_proceeding_btn.isSelected = true
                homelist_completed_btn.isSelected = false
                homelist_proceeding_btn.setTextColor(Color.parseColor("#ffffff"))
                homelist_completed_btn.setTextColor(Color.parseColor("#484848"))
                replaceFragment(HomeProceedingFragment())
            }

            // 완료된 약속 리스트 표시
            homelist_completed_btn -> {
                homelist_completed_btn.isSelected = true
                homelist_proceeding_btn.isSelected = false
                homelist_completed_btn.setTextColor(Color.parseColor("#ffffff"))
                homelist_proceeding_btn.setTextColor(Color.parseColor("#484848"))
                replaceFragment(HomeCompletedFragment())
            }
        }
    }
    internal lateinit var myToolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_homelist,container,false)

        v.homelist_proceeding_btn.setOnClickListener(this)
        v.homelist_completed_btn.setOnClickListener(this)

        myToolbar = v.findViewById<View>(R.id.my_toolbar) as Toolbar
        v.homelist_proceeding_btn.isSelected = true

        addFragment(HomeProceedingFragment())

        return v
    }

    // 프래그먼트 처음 추가
    fun addFragment(fragment : Fragment){
        val fm = childFragmentManager
        val transaction = fm.beginTransaction()
        transaction.add(R.id.homelist_content_layout, fragment)
        transaction.commit()
    }

    // 프래그먼트 교체
    fun replaceFragment(fragment: Fragment)
    {
        val fm = childFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.homelist_content_layout, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }
}