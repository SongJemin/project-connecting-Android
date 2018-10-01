package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class RoomMemberViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var roomMemberProfileImage: ImageView = itemView!!.findViewById(R.id.room_member_profile_item_img)
    var roomMemberName: TextView = itemView!!.findViewById(R.id.room_member_profile_name_tv)
}