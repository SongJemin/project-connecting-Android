package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Fragment.HomeFragment
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.R

class HomeListAdapter (private var homelistItem : ArrayList<HomeListItem>, var requestManager : RequestManager) : RecyclerView.Adapter<HomeListViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_list_item, parent, false)
        mainView.setOnClickListener(onItemClick)
        return HomeListViewHolder(mainView)
    }

    override fun getItemCount(): Int = homelistItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        requestManager.load(homelistItem[position].img_url).into(holder.img_url)
        //holder.img_url.setImageResource(homelistItem[position].roomImage!!)
        holder.homeLIstTitle.text = homelistItem[position].roomName
        holder.homeLIstStartDate.text = homelistItem[position].roomStartDate
        holder.homeLIstEndDate.text = homelistItem[position].roomEndDate
        holder.homeLIstAddress.text = homelistItem[position].typeName
        holder.homeListParticip1Image.setImageResource(homelistItem[position].participMember1!!)
        holder.homeListParticip2Image.setImageResource(homelistItem[position].participMember2!!)
        holder.homeListCompletedBtn.visibility = View.GONE
        if(HomeFragment.homeFragment.homelistData[position].roomStatus == 1)
        {
            holder.homeListCompletedBtn.visibility = View.VISIBLE
            holder.homeLIstStartDate.text = HomeFragment.homeFragment.homelistData[position].roomSelectedDate
            holder.homeLIstEndDate.visibility = View.GONE
            holder.homeListDateCenter.visibility = View.GONE
            holder.homeLIstAddress.text = HomeFragment.homeFragment.homelistData[position].roomSelectedLocation
        }
    }
}