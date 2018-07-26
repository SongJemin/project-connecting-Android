package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class RoomRecomPlace3ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    //var kakaoProfile : ImageView = itemView!!.findViewById(R.id.item_profile_image) as ImageView

    var roomRecomPlaceImage: ImageView = itemView!!.findViewById(R.id.room_recomplace_img)

    var roomRecomPlaceTitle: TextView = itemView!!.findViewById(R.id.room_recomplace_title_tv)
    var roomRecomPlaceAddress: TextView = itemView!!.findViewById(R.id.room_recomplace_address_tv)

}