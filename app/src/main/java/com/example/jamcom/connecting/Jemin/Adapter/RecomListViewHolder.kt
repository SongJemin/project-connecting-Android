package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.jamcom.connecting.R

class RecomListViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

    var recomListImage : ImageView = itemView!!.findViewById(R.id.recom_item_backgroun_img)
    var recomLIstTitle: TextView = itemView!!.findViewById(R.id.recom_item_title_tv)
}