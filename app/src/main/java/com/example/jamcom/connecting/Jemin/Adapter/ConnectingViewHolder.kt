package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class ConnectingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    //var kakaoProfile : ImageView = itemView!!.findViewById(R.id.item_profile_image) as ImageView

    var connectingProfileImage: ImageView = itemView!!.findViewById(R.id.item_connecting_profile_img)
    var connectingLine1Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line1_img)
    var connectingLine2Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line2_img)
    var connectingLine3Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line3_img)
    var connectingLine4Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line4_img)

    var connectingSilverLine1Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line_silver1_img)
    var connectingSilverLine2Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line_silver2_img)
    var connectingSilverLine3Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line_silver3_img)
    var connectingSilverLine4Image: ImageView = itemView!!.findViewById(R.id.item_connecting_line_silver4_img)
    var connectingName: TextView = itemView!!.findViewById(R.id.item_connecting_name_tv)

    var connectingCount: TextView = itemView!!.findViewById(R.id.item_connecting_count_tv)

    var connectingCountNumber: TextView = itemView!!.findViewById(R.id.item_connecting_number_tv)

}