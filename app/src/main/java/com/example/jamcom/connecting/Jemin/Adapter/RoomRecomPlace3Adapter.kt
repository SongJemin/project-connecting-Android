package com.example.jamcom.connecting.Jemin.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.jamcom.connecting.Jemin.Activity.PlaceDetailActivity
import com.example.jamcom.connecting.Jemin.Fragment.RoomRecomPlaceTab
import com.example.jamcom.connecting.Jemin.Item.RoomRecomPlaceItem
import com.example.jamcom.connecting.R

class RoomRecomPlace3Adapter(context: Context, private var recomPlace3Items: ArrayList<RoomRecomPlaceItem>, private var recomPlace3ImageUrl: Array<String?>, var requestManager: RequestManager) : RecyclerView.Adapter<RoomRecomPlace3ViewHolder>() {

    val  mContext : Context = context
    private lateinit var onItemClick : View.OnClickListener
    var selectedPosition : Int = 0
    var selectedPlaceName : String = ""
    var selectedPlaceHomepageUrl : String = ""
    var selectedRoadAddress : String = ""
    var selectedPhoneNum : String = ""
    var selectedPlaceImgUrl : String = ""
    var selectedX : String = ""
    var selectedY : String = ""
    var typeName : String = ""

    fun setOnItemClickListener3(l : View.OnClickListener){
        onItemClick = l
    }

    //내가 쓸 뷰홀더가 뭔지를 적어준다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRecomPlace3ViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.room_recom_place_list_item, parent, false)
        mainView.setOnClickListener(onItemClick)
        return RoomRecomPlace3ViewHolder(mainView)
    }

    override fun getItemCount(): Int = recomPlace3Items.size

    //데이터클래스와 뷰홀더를 이어준다.
    override fun onBindViewHolder(holder: RoomRecomPlace3ViewHolder, position: Int) {
        Log.v("TAG", "이미지url 확인 = " + recomPlace3ImageUrl[position])
        Log.v("TAG", "장소 이름 확인 = " + recomPlace3Items[position])
        Log.v("TAG", "장소 주소 확인 = " + recomPlace3Items[position])
        requestManager.load(recomPlace3ImageUrl[position]).centerCrop().into(holder.roomRecomPlaceImage)
        holder.roomRecomPlaceTitle.text = recomPlace3Items[position].place_name
        holder.roomRecomPlaceAddress.text = recomPlace3Items[position].road_address_name

        holder.itemView.setOnClickListener { selectedPosition = holder.adapterPosition
            Log.v("TAG","어댑터 클릭2 포지션 = " + position)
            selectedPlaceName = RoomRecomPlaceTab.roomRecomPlaceTab.roomRecomPlace3Items[position].place_name
            selectedPlaceHomepageUrl = RoomRecomPlaceTab.roomRecomPlaceTab.roomRecomPlace3Items[position].place_url!!
            selectedRoadAddress = RoomRecomPlaceTab.roomRecomPlaceTab.roomRecomPlace3Items[position].road_address_name!!
            selectedPhoneNum = RoomRecomPlaceTab.roomRecomPlaceTab.roomRecomPlace3Items[position].phone!!
            selectedX = RoomRecomPlaceTab.roomRecomPlaceTab.roomRecomPlace3Items[position].x!!
            selectedY = RoomRecomPlaceTab.roomRecomPlaceTab.roomRecomPlace3Items[position].y!!
            selectedPlaceImgUrl = RoomRecomPlaceTab.roomRecomPlaceTab.thirdPlaceImgArray[position]!!
            typeName = RoomRecomPlaceTab.roomRecomPlaceTab.typeName
            Log.v("TAG","어댑터 클릭3 가게 이름 = " + selectedPlaceName)
            Log.v("TAG","어댑터 클릭3 가게 홈페이지주소 = " + selectedPlaceHomepageUrl)
            Log.v("TAG","어댑터 클릭3 가게 도로명주소 = " + selectedRoadAddress)
            Log.v("TAG","어댑터 클릭3 가게 핸드폰번호 = " + selectedPhoneNum)
            Log.v("TAG","어댑터 클릭3 가게 이미지 url = " + selectedPlaceImgUrl)
            Log.v("TAG","어댑터 클릭3 가게 타입명 = " + typeName)

            var intent = Intent(mContext, PlaceDetailActivity::class.java)
            intent.putExtra("selectedPlaceName", selectedPlaceName)
            intent.putExtra("selectedPlaceHomepageUrl", selectedPlaceHomepageUrl)
            intent.putExtra("selectedRoadAddress", selectedRoadAddress)
            intent.putExtra("selectedPhoneNum", selectedPhoneNum)
            intent.putExtra("selectedPlaceImgUrl", selectedPlaceImgUrl)
            intent.putExtra("selectedX", selectedX)
            intent.putExtra("selectedY", selectedY)
            intent.putExtra("typeName", typeName)
            mContext.startActivity(intent)
        }

    }
}