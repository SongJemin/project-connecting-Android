package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Item.HomeListItem
import com.example.jamcom.connecting.Jemin.Item.RecomListItem
import com.example.jamcom.connecting.R

class RecomListAdapter (private var recomListItem : ArrayList<RecomListItem>) : RecyclerView.Adapter<RecomListViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomListViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recom_list_item, parent, false)
        mainView.setOnClickListener(onItemClick)
        return RecomListViewHolder(mainView)
    }

    override fun getItemCount(): Int = recomListItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RecomListViewHolder, position: Int) {
        holder.recomListImage.setImageResource(recomListItem[position].recomListImage!!)
        holder.recomLIstTitle.text = recomListItem[position].recomListTitle

    }
}