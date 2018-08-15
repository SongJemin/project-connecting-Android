package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.Response.UpdateLocationResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdateRoomDateResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R

import net.daum.android.map.MapViewTouchEventListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

import java.io.IOException

class MapViewActivity : AppCompatActivity(), MapView.MapViewEventListener, MapViewTouchEventListener, MapView.CurrentLocationEventListener, MapView.POIItemEventListener {

    var currentLat: Double = 0.0
    var currentLon: Double = 0.0
    var preferLat = 0.0
    var preferLon = 0.0
    var return_flag = 0
    var roomID: Int = 0
    var modify_flag: Int = 0

    var modifiedLat : String = ""
    var modifiedLon : String = ""
    lateinit var networkService : NetworkService

    private var flag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_view)

        roomID = intent.getIntExtra("roomID", 0)
        modify_flag = intent.getIntExtra("modify_flag", 0)

        Log.v("TAG", "맵뷰에서 받은 방번호 = $roomID")
        Log.v("TAG", "맵뷰에서 받은 수정플래그번호 = $modify_flag")

        // java code
        val mapView = MapView(this)
        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        mapView.setMapViewEventListener(this) // this에 MapView.MapViewEventListener 구현.
        mapView.setPOIItemEventListener(this)
        mapView.setCurrentLocationEventListener(this)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true)
        mapView.setShowCurrentLocationMarker(true)
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving

        val confirmBtn = findViewById<View>(R.id.map_view_confirm_btn) as Button

        confirmBtn.setOnClickListener {
            if (modify_flag == 0) {
                return_flag = 1
                Log.v("tag", "서비스 반환 Lat = "+ preferLat)
                Log.v("tag", "서비스 반환 Lon = "+ preferLon)

                var intent2 = Intent()
                intent2.putExtra("preferLat", preferLat)
                intent2.putExtra("preferLon", preferLon)
                intent2.putExtra("return_flag", return_flag)
                intent2.putExtra("roomID", roomID)
                setResult(28, intent2)
                finish()


            } else {

                modifiedLat = preferLat.toString()
                modifiedLon = preferLon.toString()
                Log.v("tag", "반환 Lat = "+ modifiedLat)
                Log.v("tag", "반환 Lon = "+ modifiedLon)
                updateLocation()

            }
        }

    }

    override fun onMapViewInitialized(mapView: MapView) {

    }

    override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {

    }

    override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {

    }

    override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
        mapView.removeAllPOIItems()

        val mapPointGeo = mapPoint.mapPointGeoCoord

        Log.i("TAG", "위도 : " + mapPointGeo.latitude + "경도 : " + mapPointGeo.longitude)
        preferLat = mapPointGeo.latitude
        preferLon = mapPointGeo.longitude

        val marker = MapPOIItem()
        marker.itemName = "선택한 위치"
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude)
        marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker)

    }

    override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {

    }

    override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {

    }

    override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {

    }

    override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {

    }

    override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {

    }

    override fun onSingleTap() {

    }

    override fun onDoubleTap() {

    }

    override fun onHoldMap() {

    }

    override fun onMoveMap() {

    }

    override fun onCurrentLocationUpdate(mapView: MapView, mapPoint: MapPoint, v: Float) {

        val mapPointGeo = mapPoint.mapPointGeoCoord
        currentLat = mapPointGeo.latitude
        currentLon = mapPointGeo.longitude
        Log.v("my_location", "현재위치 latitude = $currentLat")
        Log.v("my_location", "현재위치 longitude = $currentLon")
        if (flag) {
            val centerPoint = MapPoint.mapPointWithGeoCoord(currentLat, currentLon)
            mapView.setMapCenterPoint(centerPoint, true)

            flag = !flag
        }
    }

    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView, v: Float) {

    }

    override fun onCurrentLocationUpdateFailed(mapView: MapView) {

    }

    override fun onCurrentLocationUpdateCancelled(mapView: MapView) {

    }

    override fun onPOIItemSelected(mapView: MapView, mapPOIItem: MapPOIItem) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView, mapPOIItem: MapPOIItem, calloutBalloonButtonType: MapPOIItem.CalloutBalloonButtonType) {

    }

    override fun onDraggablePOIItemMoved(mapView: MapView, mapPOIItem: MapPOIItem, mapPoint: MapPoint) {

    }

    fun updateLocation() {
        networkService = ApiClient.getRetrofit().create(com.example.jamcom.connecting.Network.NetworkService::class.java)

        var roomIDValue : String = ""
        roomIDValue = roomID.toString()

        val pref = applicationContext.getSharedPreferences("auto", Activity.MODE_PRIVATE)
        var prefUserID : Int = 0
        prefUserID = pref.getInt("userID",0)

        val roomID = RequestBody.create(MediaType.parse("text.plain"), roomIDValue)
        val userID = RequestBody.create(MediaType.parse("text.plain"), prefUserID.toString())
        val promiseLat = RequestBody.create(MediaType.parse("text.plain"), modifiedLat)
        val promiseLon = RequestBody.create(MediaType.parse("text.plain"), modifiedLon)

        val updateLocationResponse = networkService.updateLocation(roomID, userID, promiseLat, promiseLon)

        Log.v("TAG", "출발 위치 수정 전송 : 수정 방번호 = " + roomIDValue)
        Log.v("TAG", "출발 위치 수정 전송 : 수정 유저번호 = " + prefUserID.toString())
        Log.v("TAG", "출발 위치 수정 전송 : 수정 Lat = " + modifiedLat)
        Log.v("TAG", "출발 위치 수정 전송 : 수정 Lon = " + modifiedLon)

        updateLocationResponse.enqueue(object : retrofit2.Callback<UpdateLocationResponse>{

            override fun onResponse(call: Call<UpdateLocationResponse>, response: Response<UpdateLocationResponse>) {
                Log.v("TAG", "출발 위치 수정 통신 성공")
                if(response.isSuccessful){
                    var message = response!!.body()

                    Log.v("TAG", "출발 위치 수정 값 전달 성공"+ message.toString())
                    var intent = Intent()
                    intent.putExtra("modifiedLat", modifiedLat)
                    intent.putExtra("modifiedLon", modifiedLon)

                    setResult(29, intent)
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,"출발 위치 수정 값 전달 실패", Toast.LENGTH_SHORT).show()

                    Log.v("TAG", "출발 위치 수정 값 전달 실패"+ response!!.body().toString())
                }
            }

            override fun onFailure(call: Call<UpdateLocationResponse>, t: Throwable?) {
                Toast.makeText(applicationContext,"출발 위치 수정 서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
