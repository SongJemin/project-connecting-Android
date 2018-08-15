package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class MyDibsViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    var mydibsImage : ImageView = itemView!!.findViewById(R.id.mydibs_background_img)
    var mydibsName : TextView = itemView!!.findViewById(R.id.mydibs_name_tv)
    var mydibsAddress : TextView = itemView!!.findViewById(R.id.mydibs_address_tv)

}