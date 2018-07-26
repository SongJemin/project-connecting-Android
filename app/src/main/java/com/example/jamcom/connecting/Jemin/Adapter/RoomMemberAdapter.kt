package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.R

class RoomMemberAdapter(private var memberItems : ArrayList<RoomMemberItem>) : RecyclerView.Adapter<RoomMemberViewHolder>() {


    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomMemberViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_member_list_item, parent, false)
        return RoomMemberViewHolder(mainView)
    }

    override fun getItemCount(): Int = memberItems.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomMemberViewHolder, position: Int) {
        holder.roomMemberProfileImage.setImageResource(memberItems[position].img_url!!)
        holder.roomMemberName.text = memberItems[position].name
    }
}