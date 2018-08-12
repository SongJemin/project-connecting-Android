package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R

class RoomRecomPlace3Adapter(private var recomPlace3Items: ArrayList<RoomRecomPlaceItem>, private var recomPlace3ImageUrl: Array<String?>, var requestManager: RequestManager) : RecyclerView.Adapter<RoomRecomPlace3ViewHolder>() {


    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRecomPlace3ViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_recom_place_list_item, parent, false)
        return RoomRecomPlace3ViewHolder(mainView)
    }

    override fun getItemCount(): Int = recomPlace3Items.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomRecomPlace3ViewHolder, position: Int) {
        Log.v("TAG", "이미지url 확인 = " + recomPlace3ImageUrl[position])
        Log.v("TAG", "장소 이름 확인 = " + recomPlace3Items[position])
        Log.v("TAG", "장소 주소 확인 = " + recomPlace3Items[position])
        requestManager.load(recomPlace3ImageUrl[position]).centerCrop().into(holder.roomRecomPlaceImage)
        holder.roomRecomPlaceTitle.text = recomPlace3Items[position].place_name
        holder.roomRecomPlaceAddress.text = recomPlace3Items[position].road_address_name
    }
}