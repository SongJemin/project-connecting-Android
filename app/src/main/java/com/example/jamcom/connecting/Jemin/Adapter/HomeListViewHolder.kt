package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class HomeListViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    var img_url : ImageView = itemView!!.findViewById(R.id.home_list_background_img)
    var homeLIstTitle: TextView = itemView!!.findViewById(R.id.home_list_title_tv)
    var homeLIstStartDate: TextView = itemView!!.findViewById(R.id.home_list_start_date_tv)
    var homeLIstEndDate: TextView = itemView!!.findViewById(R.id.home_list_end_date_tv)
    var homeLIstAddress: TextView = itemView!!.findViewById(R.id.home_list_address_tv)
    var homeListParticip1Image : ImageView = itemView!!.findViewById(R.id.home_list_particip1_iv)
    var homeListParticip2Image : ImageView = itemView!!.findViewById(R.id.home_list_particip2_iv)
    var homeListCompletedBtn : Button = itemView!!.findViewById(R.id.home_list_completed_btn)
    var homeListDateCenter: TextView = itemView!!.findViewById(R.id.home_list_date_center_tv)
}