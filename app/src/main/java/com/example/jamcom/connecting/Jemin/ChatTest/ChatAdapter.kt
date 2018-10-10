package com.example.jamcom.connecting.Jemin.ChatTest

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Adapter.MyDibsViewHolder
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.R

class ChatAdapter (private var chatListItem : ArrayList<ChatListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<ChatViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(mainView)
    }

    override fun getItemCount(): Int = chatListItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        Log.v("Asdf", "채팅 어댑터 유저 번호 = " + chatListItem[position].ChatUserID)
        requestManager.load(chatListItem[position].ChatUserImgUrl).centerCrop().into(holder.chatUserImg)
        //holder.img_url.setImageResource(homelistItem[position].roomImage!!)
        holder.chatUserName.text = chatListItem[position].ChatUserName
        holder.chatContent.text = chatListItem[position].ChatContent
    }
}