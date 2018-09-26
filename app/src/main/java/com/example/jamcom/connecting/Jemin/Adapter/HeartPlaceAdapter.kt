package com.example.jamcom.connecting.Jemin.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Fragment.RoomRecomPlaceTab
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R



class HeartPlaceAdapter (private var heartPlaceItem : ArrayList<MyDibsListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<HeartPlaceViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartPlaceViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_heart_replace, parent, false)
        mainView.setOnClickListener(onItemClick)
        return HeartPlaceViewHolder(mainView)
    }

    override fun getItemCount(): Int = heartPlaceItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: HeartPlaceViewHolder, position: Int) {

        requestManager.load(heartPlaceItem[position].favoriteImageUrl).centerCrop().into(holder.heartPlaceImage)
        //holder.img_url.setImageResource(homelistItem[position].roomImage!!)
        holder.heartPlaceTitle.text = heartPlaceItem[position].favoriteName
        holder.heartPlaceAddress.text = heartPlaceItem[position].favoriteAddres
        holder.heartPlaceRank.text = (position + 1).toString() + "위"
    }
}