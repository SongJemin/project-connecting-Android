package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.AlarmListItem
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.R

class AlarmListAdapter (private var alarmListItem : ArrayList<AlarmListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<AlarmListViewHolder>(){



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
        holder.alarmRoomName.text = alarmListItem[position].AlarmRoomName
        holder.alarmRoomContent.text = alarmListItem[position].AlarmRoomContent
    }
}