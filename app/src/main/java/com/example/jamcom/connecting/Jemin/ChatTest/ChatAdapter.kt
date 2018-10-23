package com.example.jamcom.connecting.Jemin.ChatTest

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Fragment.HomeProceedingFragment
import com.example.jamcom.connecting.R

class ChatAdapter (private var chatListItem: ArrayList<ChatListItem>, var requestManager: RequestManager) : RecyclerView.Adapter<ChatViewHolder>(){


    var userID : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        Log.v("Asdf", "채팅어댑터 크기 = " + chatListItem.size)
        return ChatViewHolder(mainView)
    }

    override fun getItemCount(): Int = chatListItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        Log.v("Asdf", "채팅 포지션 크기 = " + position)
        userID = HomeProceedingFragment.homeProceedingFragment.userID
        //Log.v("Asdf", "채팅 어댑터 유저 번호 = " + chatListItem[position]!!.ChatUserID)
        Log.v("Asdf", "채팅 어댑터 기기 유저 번호 = " + userID)
        Log.v("adsf", "찾자 = " + chatListItem[position].toString())

        if(userID == chatListItem[position]!!.ChatUserID)
        {
            holder.chatLeftTime.visibility = View.VISIBLE
            holder.chatRightTime.visibility = View.GONE
            Log.v("Asdf", "오른쪽 메시지 내용 = " + chatListItem[position]!!.ChatContent)
            holder.chatContent.setBackgroundResource(R.drawable.chat_purple)
            holder.chatProfileLayout.visibility = View.GONE
            holder.chatUserName.visibility = View.GONE
            //holder.chatMainLayout.gravity = Gravity.END
            holder.chatChatLayout.gravity = Gravity.END
            holder.chatMineViewLayout.visibility = View.VISIBLE
            requestManager.load(chatListItem[position]!!.ChatUserImgUrl).centerCrop().into(holder.chatUserImg)
            //holder.img_url.setImageResource(homelistItem[position].roomImage!!)
            holder.chatUserName.text = chatListItem[position]!!.ChatUserName
            holder.chatContent.text = chatListItem[position]!!.ChatContent
            holder.chatLeftTime.text = chatListItem[position]!!.ChatDateTime
            holder.chatContent.setTextColor(Color.WHITE)
        }
        else{
            holder.chatLeftTime.visibility = View.GONE
            holder.chatRightTime.visibility = View.VISIBLE
            Log.v("Asdf", "왼쪽 메시지 내용 = " + chatListItem[position]!!.ChatContent)
            holder.chatContent.setBackgroundResource(R.drawable.chat_gray)
            holder.chatMainLayout.gravity = Gravity.LEFT
            holder.chatMineViewLayout.visibility = View.GONE
            requestManager.load(chatListItem[position]!!.ChatUserImgUrl).centerCrop().into(holder.chatUserImg)
            //holder.img_url.setImageResource(homelistItem[position].roomImage!!)
            holder.chatUserName.text = chatListItem[position]!!.ChatUserName
            holder.chatContent.text = chatListItem[position]!!.ChatContent
            holder.chatRightTime.text = chatListItem[position]!!.ChatDateTime
        }

        holder.setIsRecyclable(false)
    }
}