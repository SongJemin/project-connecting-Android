package com.example.jamcom.connecting.Jemin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamcom.connecting.Jemin.Item.MyDibsListItem
import com.example.jamcom.connecting.R

class MyDibsAdapter (private var myDibslistItem : ArrayList<MyDibsListItem>) : RecyclerView.Adapter<MyDibsViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(l : View.OnClickListener){
        onItemClick = l

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDibsViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.mydibs_list_item, parent, false)
        mainView.setOnClickListener(onItemClick)
        return MyDibsViewHolder(mainView)
    }

    override fun getItemCount(): Int = myDibslistItem.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: MyDibsViewHolder, position: Int) {
        holder.mydibsImage.setImageResource(myDibslistItem[position].DibsImage!!)
    }
}