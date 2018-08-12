package com.example.jamcom.connecting.Jemin.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R
import android.util.Log
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Fragment.RoomRecomPlaceTab


class RoomRecomPlace1Adapter(context: Context, private var recomPlace1Items: ArrayList<RoomRecomPlaceItem>, var imgArray: Array<String?>, var requestManager: RequestManager) : RecyclerView.Adapter<RoomRecomPlace1ViewHolder>() {

    val  mContext : Context = context
    private lateinit var onItemClick : View.OnClickListener
    var selectedPosition : Int = 0

    fun setOnItemClickListener1(l : View.OnClickListener){
        onItemClick = l
    }

    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRecomPlace1ViewHolder {
        val mainView1 : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_recom_place_list_item, parent, false)
        mainView1.setOnClickListener(onItemClick)
        return RoomRecomPlace1ViewHolder(mainView1)
    }

    override fun getItemCount(): Int = recomPlace1Items.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomRecomPlace1ViewHolder, position: Int) {
        requestManager.load(imgArray[position]).centerCrop().into(holder.roomRecomPlaceImage)
        holder.roomRecomPlaceTitle.text = recomPlace1Items[position].place_name
        holder.roomRecomPlaceAddress.text = recomPlace1Items[position].road_address_name

        holder.itemView.setOnClickListener { selectedPosition = holder.adapterPosition
        Log.v("TAG","어댑터 클릭 포지션 = " + position)
            var intent = Intent(mContext, PlaceDetailActivity::class.java)
            mContext.startActivity(intent)
        }
    }
}