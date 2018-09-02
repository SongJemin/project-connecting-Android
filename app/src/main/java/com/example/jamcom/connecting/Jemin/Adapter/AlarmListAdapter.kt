package com.example.jamcom.connecting.Jemin.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.RoomInformActivity
import com.example.jamcom.connecting.Jemin.Fragment.AlarmFragment
import com.example.jamcom.connecting.Jemin.Item.AlarmListItem
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.R

class AlarmListAdapter (context: Context, private var alarmListItem : ArrayList<AlarmListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<AlarmListViewHolder>(){

    val  mContext : Context = context
    var roomNameCheck : String = ""
    var roomNameCheckNumber : Int = 0
    var checkNumberList = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.alarm_list_item, parent, false)
        return AlarmListViewHolder(mainView)
    }

    override fun getItemCount(): Int = alarmListItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: AlarmListViewHolder, position: Int) {
        requestManager.load(alarmListItem[position].AlarmRoomImgUrl).into(holder.alarmRoomImgUrl)
        //holder.img_url.setImageResource(homelistItem[position].roomImage!!)


            if(roomNameCheck == alarmListItem[position].AlarmRoomName!!)
            {
                Log.v("TAG", "방 이름 중복임")
                holder.alarmRoomName.visibility = GONE
                holder.alarmRoomImgUrl.visibility = INVISIBLE
                holder.alarmVisibleLayout.visibility = GONE
            }
            else{
                Log.v("TAG", "방 이름 중복 아님 : " + position)
                roomNameCheck = alarmListItem[position].AlarmRoomName!!
                roomNameCheckNumber = position
                if(checkNumberList.size > 0)
                {
                    if(checkNumberList.get(checkNumberList.size - 1) > roomNameCheckNumber)
                    {

                    }
                    else{
                        checkNumberList.add(roomNameCheckNumber)
                    }
                }

                else{
                        checkNumberList.add(roomNameCheckNumber)
                }
            }

        if(checkNumberList.contains(position)){
            holder.alarmRoomName.visibility = VISIBLE
            holder.alarmRoomImgUrl.visibility = VISIBLE
            holder.alarmVisibleLayout.visibility = VISIBLE
        }

        holder.alarmRoomName.text = alarmListItem[position].AlarmRoomName
        holder.alarmRoomContent.text = alarmListItem[position].AlarmRoomContent

        holder.alarmRoomContent.setOnClickListener {
            var intent = Intent(mContext, RoomInformActivity::class.java)
            intent.putExtra("roomID", AlarmFragment.alarmFragment.alarmlistData[position].roomID)
            mContext.startActivity(intent)
        }
    }
}