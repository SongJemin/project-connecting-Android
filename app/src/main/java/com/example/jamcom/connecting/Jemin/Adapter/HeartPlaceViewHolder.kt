package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class HeartPlaceViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    //var kakaoProfile : ImageView = itemView!!.findViewById(R.id.item_profile_image) as ImageView
    var heartPlaceRank : TextView = itemView!!.findViewById(R.id.item_heart_recomplace_rank_tv)
    var heartPlaceImage: ImageView = itemView!!.findViewById(R.id.item_heart_recomplace_img)

    var heartPlaceTitle: TextView = itemView!!.findViewById(R.id.item_heart_recomplace_title_tv)
    var heartPlaceAddress: TextView = itemView!!.findViewById(R.id.item_heart_recomplace_address_tv)

}