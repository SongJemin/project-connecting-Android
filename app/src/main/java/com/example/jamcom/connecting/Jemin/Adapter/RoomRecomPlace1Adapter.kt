package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R

class RoomRecomPlace1Adapter(private var recomPlace1Items: ArrayList<RoomRecomPlaceItem>, var imgArray: Array<String?>, var requestManager: RequestManager) : RecyclerView.Adapter<RoomRecomPlace1ViewHolder>() {


    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRecomPlace1ViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_recom_place_list_item, parent, false)
        return RoomRecomPlace1ViewHolder(mainView)
    }

    override fun getItemCount(): Int = recomPlace1Items.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomRecomPlace1ViewHolder, position: Int) {
        requestManager.load(imgArray[position]).centerCrop().into(holder.roomRecomPlaceImage)
        holder.roomRecomPlaceTitle.text = recomPlace1Items[position].place_name
        holder.roomRecomPlaceAddress.text = recomPlace1Items[position].road_address_name
    }
}