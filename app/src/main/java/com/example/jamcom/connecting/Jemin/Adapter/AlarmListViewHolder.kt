package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.jamcom.connecting.R

class AlarmListViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    var alarmRoomImgUrl : ImageView = itemView!!.findViewById(R.id.alarm_item_room_background_img)
    var alarmRoomName: TextView = itemView!!.findViewById(R.id.alarm_item_room_name_tv)
    var alarmRoomContent: TextView = itemView!!.findViewById(R.id.alarm_item_room_content_tv)
    var alarmVisibleLayout : LinearLayout = itemView!!.findViewById(R.id.alarm_item_visible_layout)
}