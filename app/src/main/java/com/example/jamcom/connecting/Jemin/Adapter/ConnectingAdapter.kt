package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Item.ConnectingListItem
import com.example.jamcom.connecting.Jemin.Item.RoomMemberItem
import com.example.jamcom.connecting.R

class ConnectingAdapter(private var connectingListItem : ArrayList<ConnectingListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<ConnectingViewHolder>() {

    var count : Int = 0
    var goldCount : Int = 0
    var result : Int = 0
    var test : Int = 0
    var testGold : Int = 0
    var testSilver : Int = 0
    var testPurple : Int = 0

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

        testGold = 0
        testSilver = 0
        testPurple = 0

        test = connectingListItem[position].connectingCount
        result = connectingListItem[position].connectingCount % 25
        count = result / 5
        goldCount = connectingListItem[position].connectingCount / 25

        while(test>4)
        {
            if(test>=25)
            {
                testGold = test/25
                test = test % 25
            }
            else{
                testSilver = test / 5
                test = test % 5
            }

        }
        testPurple = test

        holder.connectingLine5Image.setVisibility(View.INVISIBLE)
        holder.connectingCount.setVisibility(View.INVISIBLE)

        if(testGold == 0){
            holder.connectingGoldLine1Image.setVisibility(View.GONE)
            holder.connectingGoldLine2Image.setVisibility(View.GONE)
            holder.connectingGoldLine3Image.setVisibility(View.GONE)
            holder.connectingGoldLine4Image.setVisibility(View.GONE)
        }
        else if(testGold == 1){
            holder.connectingGoldLine1Image.setVisibility(View.GONE)
            holder.connectingGoldLine2Image.setVisibility(View.GONE)
            holder.connectingGoldLine3Image.setVisibility(View.GONE)
        }
        else if(testGold == 2){
            holder.connectingGoldLine1Image.setVisibility(View.GONE)
            holder.connectingGoldLine2Image.setVisibility(View.GONE)
        }
        else if (testGold == 3){
            holder.connectingGoldLine1Image.setVisibility(View.GONE)
        }
        else if(testGold == 4){

        }
        else{
            Log.v("Tag", "100번 넘게 만남")
        }

        if(testSilver == 0){
            holder.connectingSilverLine1Image.setVisibility(View.GONE)
            holder.connectingSilverLine2Image.setVisibility(View.GONE)
            holder.connectingSilverLine3Image.setVisibility(View.GONE)
            holder.connectingSilverLine4Image.setVisibility(View.GONE)
        }
        else if(testSilver == 1){
            holder.connectingSilverLine1Image.setVisibility(View.GONE)
            holder.connectingSilverLine2Image.setVisibility(View.GONE)
            holder.connectingSilverLine3Image.setVisibility(View.GONE)
        }
        else if(testSilver == 2){
            holder.connectingSilverLine1Image.setVisibility(View.GONE)
            holder.connectingSilverLine2Image.setVisibility(View.GONE)
        }
        else if(testSilver == 3){
            holder.connectingSilverLine1Image.setVisibility(View.GONE)
        }
        else if(testSilver == 4){

        }

        if(testPurple == 0){
            holder.connectingLine1Image.setVisibility(View.GONE)
            holder.connectingLine2Image.setVisibility(View.GONE)
            holder.connectingLine3Image.setVisibility(View.GONE)
            holder.connectingLine4Image.setVisibility(View.GONE)
        }
        else if(testPurple == 1){
            holder.connectingLine1Image.setVisibility(View.GONE)
            holder.connectingLine2Image.setVisibility(View.GONE)
            holder.connectingLine3Image.setVisibility(View.GONE)
        }
        else if(testPurple == 2){
            holder.connectingLine1Image.setVisibility(View.GONE)
            holder.connectingLine2Image.setVisibility(View.GONE)
        }
        else if(testPurple == 3){
            holder.connectingLine1Image.setVisibility(View.GONE)
        }
        else if(testPurple == 4){

        }

        holder.connectingName.text = connectingListItem[position].userName
        requestManager.load(connectingListItem[position].userImageUrl).into(holder.connectingProfileImage)
        holder.connectingCount.text = connectingListItem[position].connectingCount.toString()

    }
}