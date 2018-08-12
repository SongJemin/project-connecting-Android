package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R

class RoomRecomPlace2Adapter(private var recomPlace2Items: ArrayList<RoomRecomPlaceItem>, private var recomPlace2ImageUrl: Array<String?>, var requestManager: RequestManager) : RecyclerView.Adapter<RoomRecomPlace2ViewHolder>() {


    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRecomPlace2ViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_recom_place_list_item, parent, false)
        return RoomRecomPlace2ViewHolder(mainView)
    }

    override fun getItemCount(): Int = recomPlace2Items.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomRecomPlace2ViewHolder, position: Int) {
        requestManager.load(recomPlace2ImageUrl[position]).centerCrop().into(holder.roomRecomPlaceImage)
        holder.roomRecomPlaceTitle.text = recomPlace2Items[position].place_name
        holder.roomRecomPlaceAddress.text = recomPlace2Items[position].road_address_name
    }
}