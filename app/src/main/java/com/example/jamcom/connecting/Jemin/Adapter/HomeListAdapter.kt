package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Fragment.HomeCompletedFragment
import com.example.jamcom.connecting.Jemin.Fragment.HomeProceedingFragment
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
    override fun onBindViewHolder(holder: HomeListViewHolder, position:Int) {
        requestManager.load(homelistItem[position].img_url).into(holder.img_url)
        requestManager.load(homelistItem[position].participMember1).into(holder.homeListParticip1Image)

        holder.homeLIstTitle.text = homelistItem[position].roomName
        holder.homeLIstStartDate.text = homelistItem[position].roomStartDate
        holder.homeLIstEndDate.text = homelistItem[position].roomEndDate
        holder.homeLIstAddress.text = homelistItem[position].typeName
        holder.homeListCompletedBtn.visibility = View.GONE

        // 진행 중인 리스트 표시
        if(HomeProceedingFragment.homeProceedingFragment.homeListFlag == 0)
        {
            // 참여 인원 한명
            if(HomeProceedingFragment.homeProceedingFragment.homelistData[position].attendantArr!![1] == "null"){
                holder.homeListMemberPlusTv.setText("+0")
            }
            // 참여 인원 두명 이상
            else{
                requestManager.load(homelistItem[position].participMember2).into(holder.homeListParticip2Image)
                holder.homeListMemberPlusTv.setText("+"+(HomeProceedingFragment.homeProceedingFragment.homelistData[position].attendantArr!!.size - 2).toString())
            }

            if(HomeProceedingFragment.homeProceedingFragment.homelistData[position].roomStatus == 1)
            {
                holder.homeListCompletedBtn.visibility = View.VISIBLE
                holder.homeLIstStartDate.text = HomeProceedingFragment.homeProceedingFragment.homelistData[position].confirmedDate
                holder.homeLIstEndDate.visibility = View.GONE
                holder.homeListDateCenter.visibility = View.GONE
                holder.homeLIstAddress.text = HomeProceedingFragment.homeProceedingFragment.homelistData[position].confirmedName
            }
        }

        // 완료된 리스트 표시
        else if(HomeProceedingFragment.homeProceedingFragment.homeListFlag == 1)
        {
            // 참여 인원 한명
            if(HomeCompletedFragment.homeCompletedFragment.homelistData[position].attendantArr!![1] == "null"){
                holder.homeListMemberPlusTv.setText("+0")
            }
            // 참여 인원 두명 이상
            else{
                requestManager.load(homelistItem[position].participMember2).into(holder.homeListParticip2Image)
                holder.homeListMemberPlusTv.setText("+"+(HomeCompletedFragment.homeCompletedFragment.homelistData[position].attendantArr!!.size - 2).toString())
            }

            if(HomeCompletedFragment.homeCompletedFragment.homelistData[position].roomStatus == 1)
            {
                holder.homeListCompletedBtn.visibility = View.VISIBLE
                holder.homeLIstStartDate.text = HomeCompletedFragment.homeCompletedFragment.homelistData[position].confirmedDate
                holder.homeLIstEndDate.visibility = View.GONE
                holder.homeListDateCenter.visibility = View.GONE
                holder.homeLIstAddress.text = HomeCompletedFragment.homeCompletedFragment.homelistData[position].confirmedName
            }
        }

    }
}