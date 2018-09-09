package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.ConnectingListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.R

class ConnectingAdapter(private var connectingListItem : ArrayList<ConnectingListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<ConnectingViewHolder>() {

    var count : Int = 0

    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectingViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.connecting_count_item, parent, false)
        return ConnectingViewHolder(mainView)
    }

    override fun getItemCount(): Int = connectingListItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: ConnectingViewHolder, position: Int) {
        holder.connectingCountNumber.text = (position + 1).toString()

        count = connectingListItem[position].connectingCount / 5

        if((connectingListItem[position].connectingCount % 5) == 1)
        {
            if(count==0)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
                holder.connectingSilverLine4Image.setVisibility(View.GONE)
            }
            else if(count==1)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
            }
            else if(count==2)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
            }
            else if(count==3)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
            }
            else if(count==4)
            {

            }
            holder.connectingLine1Image.setVisibility(View.GONE)
            holder.connectingLine2Image.setVisibility(View.GONE)
            holder.connectingLine3Image.setVisibility(View.GONE)
        }
        else if((connectingListItem[position].connectingCount % 5)==2)
        {

            if(count==0)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
                holder.connectingSilverLine4Image.setVisibility(View.GONE)
            }
            else if(count==1)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
            }
            else if(count==2)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
            }
            else if(count==3)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
            }
            else if(count==4)
            {

            }
            holder.connectingLine1Image.setVisibility(View.GONE)
            holder.connectingLine2Image.setVisibility(View.GONE)
        }
        else if((connectingListItem[position].connectingCount % 5)==3)
        {

            if(count==0)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
                holder.connectingSilverLine4Image.setVisibility(View.GONE)
            }
            else if(count==1)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
            }
            else if(count==2)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
            }
            else if(count==3)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
            }
            else if(count==4)
            {

            }
            holder.connectingLine1Image.setVisibility(View.GONE)
        }
        else if((connectingListItem[position].connectingCount % 5)==4)
        {

            if(count==0)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
                holder.connectingSilverLine4Image.setVisibility(View.GONE)
            }
            else if(count==1)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
                holder.connectingSilverLine3Image.setVisibility(View.GONE)
            }
            else if(count==2)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
                holder.connectingSilverLine2Image.setVisibility(View.GONE)
            }
            else if(count==3)
            {
                holder.connectingSilverLine1Image.setVisibility(View.GONE)
            }
            else if(count==4)
            {

            }

        }
        else{
            holder.connectingLine1Image.setVisibility(View.GONE)
            holder.connectingLine2Image.setVisibility(View.GONE)
            holder.connectingLine3Image.setVisibility(View.GONE)
            holder.connectingLine4Image.setVisibility(View.GONE)
        }
        holder.connectingLine1Image.visibility
        holder.connectingName.text = connectingListItem[position].userName
        requestManager.load(connectingListItem[position].userImageUrl).into(holder.connectingProfileImage)
        holder.connectingCount.text = connectingListItem[position].connectingCount.toString()

    }
}