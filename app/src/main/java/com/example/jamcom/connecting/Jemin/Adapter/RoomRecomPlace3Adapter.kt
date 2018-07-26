package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R

class RoomRecomPlace3Adapter(private var recomPlace1Items : ArrayList<RoomRecomPlaceItem>) : RecyclerView.Adapter<RoomRecomPlace3ViewHolder>() {


    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRecomPlace3ViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_recom_place_list_item, parent, false)
        return RoomRecomPlace3ViewHolder(mainView)
    }

    override fun getItemCount(): Int = recomPlace1Items.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomRecomPlace3ViewHolder, position: Int) {
        holder.roomRecomPlaceImage.setImageResource(recomPlace1Items[position].img_url!!)
        holder.roomRecomPlaceTitle.text = recomPlace1Items[position].title
        holder.roomRecomPlaceAddress.text = recomPlace1Items[position].address
    }
}