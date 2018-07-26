package com.example.jamcom.connecting.Jemin.Fragment

import android.annotation.TargetApi
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.graphics.Color
import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.fragment_room_inform.*
import kotlinx.android.synthetic.main.fragment_room_inform.view.*

class RoomInformFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v)
        {
            room_inform_decide_tv -> {
                room_inform_decide_tv.isSelected = true
                room_inform_decide_tv.setTextColor(Color.parseColor("#764dd1"))

                room_inform_member_tv.isSelected = false
                room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_recomplace_tv.isSelected = false
                room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_myinform_tv.isSelected = false
                room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))

                replaceFragment(RoomDecideTab())
            }

            room_inform_member_tv -> {
                room_inform_member_tv.isSelected = true
                room_inform_member_tv.setTextColor(Color.parseColor("#764dd1"))

                room_inform_decide_tv.isSelected = false
                room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_recomplace_tv.isSelected = false
                room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_myinform_tv.isSelected = false
                room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))

                replaceFragment(RoomMemberTab())
            }

            room_inform_recomplace_tv -> {
                room_inform_recomplace_tv.isSelected = true
                room_inform_recomplace_tv.setTextColor(Color.parseColor("#764dd1"))

                room_inform_decide_tv.isSelected = false
                room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_member_tv.isSelected = false
                room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_myinform_tv.isSelected = false
                room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))

                replaceFragment(RoomRecomPlaceTab())
            }

            room_inform_myinform_tv -> {
                room_inform_myinform_tv.isSelected = true
                room_inform_myinform_tv.setTextColor(Color.parseColor("#764dd1"))

                room_inform_decide_tv.isSelected = false
                room_inform_decide_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_recomplace_tv.isSelected = false
                room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
                room_inform_member_tv.isSelected = false
                room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))

                replaceFragment(RoomMyInformTab())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_room_inform,container,false)

        v.room_inform_decide_tv.setOnClickListener(this)
        v.room_inform_member_tv.setOnClickListener(this)
        v.room_inform_recomplace_tv.setOnClickListener(this)
        v.room_inform_myinform_tv.setOnClickListener(this)

        v.room_inform_decide_tv.isSelected = true
        v.room_inform_decide_tv.setTextColor(Color.BLACK)

        v.room_inform_member_tv.setTextColor(Color.parseColor("#2b2b2b"))
        v.room_inform_recomplace_tv.setTextColor(Color.parseColor("#2b2b2b"))
        v.room_inform_myinform_tv.setTextColor(Color.parseColor("#2b2b2b"))

        addFragment(RoomDecideTab())

        return v
    }

    fun addFragment(fragment : Fragment){
        val fm = childFragmentManager
        val transaction = fm.beginTransaction()
        transaction.add(R.id.room_inform_frame_layout, fragment)
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment)
    {
        val fm = childFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.room_inform_frame_layout, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }
}