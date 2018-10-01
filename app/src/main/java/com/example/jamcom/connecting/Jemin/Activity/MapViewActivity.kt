package com.example.jamcom.connecting.Jemin.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.jamcom.connecting.Network.Get.GetLocationMessage
import com.example.jamcom.connecting.Network.Get.Response.GetLocationResponse
import com.example.jamcom.connecting.Network.NetworkService
import com.example.jamcom.connecting.Network.Post.Response.UpdateLocationResponse
import com.example.jamcom.connecting.Network.Post.Response.UpdateRoomDateResponse
import com.example.jamcom.connecting.Old.retrofit.ApiClient

import com.example.jamcom.connecting.R
import kotlinx.android.synthetic.main.map_view.*

import net.daum.android.map.MapViewTouchEventListener
import net.daum.mf.map.api.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.IOException
import java.util.ArrayList

class MapViewActivity : AppCompatActivity(), MapView.MapViewEventListener, MapViewTouchEventListener, MapView.CurrentLocationEventListener, MapView.POIItemEventListener {

    var currentLat: Double = 0.0
    var currentLon: Double = 0.0
    var preferLat = 0.0
    var preferLon = 0.0
    var return_flag = 0
    var roomID: Int = 0
    var modify_flag : Int = 0
    var polyline_flag : Int = 0
    lateinit var locationData : ArrayList<GetLocationMessage>

    var recomPromiseLat : Double = 0.0
    var recomPromiseLon : Double = 0.0
    var modifiedLat : String = ""
    var modifiedLon : String = ""
    lateinit var networkService : NetworkService
    lateinit var mapView : MapView
    var recomPlace : String = ""


    private var flag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_view)

        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.parseColor("#FFFFFF")
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            window.statusBarColor = Color.BLACK
        }

        // java code
        mapView = MapView(this)
        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        mapView.setMapViewEventListener(this) // this에 MapView.MapViewEventListener 구현.
        mapView.setPOIItemEventListener(this)
        mapView.setCurrentLocationEventListener(this)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true)
        mapView.setShowCurrentLocationMarker(true)

        val confirmBtn = findViewById<View>(R.id.map_view_confirm_btn) as Button

        polyline_flag = intent.getIntExtra("polyline_flag",0)
        if(polyline_flag == 1)
        {
            map_view_confirm_btn.visibility = View.GONE
            mapView.removeAllPOIItems()
            mapView.removeAllPolylines()
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            mapView.setShowCurrentLocationMarker(false)
            roomID = intent.getIntExtra("roomID",0)
            recomPlace = intent.getStringExtra("recomPlace")
            recomPromiseLat = intent.getDoubleExtra("recomPromiseLat",0.0)
            recomPromiseLon = intent.getDoubleExtra("recomPromiseLon",0.0)
            Log.v("TAG", "폴리라인을 위해 받은 방 번호 = " + roomID)
            Log.v("TAG", "폴리라인을 위해 받은 방 추천Lat = " + recomPromiseLat)
            Log.v("TAG", "폴리라인을 위해 받은 방 추천Lon = " + recomPromiseLon)

            getLocation()

            confirmBtn.setOnClickListener {
                var intent = Intent(this, RoomInformActivity::class.java)
                intent.putExtra("roomID", roomID)
                startActivity(intent)
            }


        }
        else{
            map_view_confirm_btn.visibility = View.VISIBLE
            mapView.removeAllPOIItems()
            mapView.removeAllPolylines()
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving
            roomID = intent.getIntExtra("roomID", 0)
            modify_flag = intent.getIntExtra("modify_flag", 0)
            Log.v("TAG", "맵뷰에서 받은 방번호 = $roomID")
            Log.v("TAG", "맵뷰에서 받은 수정플래그번호 = $modify_flag")

            confirmBtn.setOnClickListener {
                if (modify_flag == 0) {
                    return_flag = 1
                    Log.v("tag", "서비스 반환 Lat = "+ preferLat)
                    Log.v("tag", "서비스 반환 Lon = "+ preferLon)

                    if(preferLat == 0.0 || preferLon == 0.0){
                        Toast.makeText(applicationContext, "출발 장소를 선택해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        var intent2 = Intent()
                        intent2.putExtra("preferLat", preferLat)
                        intent2.putExtra("preferLon", preferLon)
                        intent2.putExtra("return_flag", return_flag)
                        intent2.putExtra("roomID", roomID)
                        setResult(28, intent2)
                        finish()
                    }

                } else {
                    if(preferLat == 0.0 || preferLon == 0.0){
                        Toast.makeText(applicationContext, "출발 장소를 선택해주세요.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        modifiedLat = preferLat.toString()
                        modifiedLon = preferLon.toString()
                        Log.v("tag", "반환 Lat = "+ modifiedLat)
                        Log.v("tag", "반환 Lon = "+ modifiedLon)
                        updateLocation()
                    }
                }
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

        if(polyline_flag == 0)
        {
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

        if(polyline_flag == 0)
        {
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

    private fun getLocation() {
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
            var getLocationResponse = networkService.getLocation(roomID) // 네트워크 서비스의 getContent 함수를 받아옴

            getLocationResponse.enqueue(object : Callback<GetLocationResponse> {
                override fun onResponse(call: Call<GetLocationResponse>?, response: Response<GetLocationResponse>?) {
                    Log.v("TAG","폴리라인 위도경도 GET 통신 성공")
                    if(response!!.isSuccessful)
                    {
                        Log.v("TAG","폴리라인 위도경도 값 갖고오기 성공")
                        locationData = response.body()!!.result

                        var polyline = MapPolyline();
                        polyline.setTag(1000);
                        polyline.setLineColor(Color.argb(128, 118, 101, 191)); // Polyline 컬러 지정.
                        //polyline2.setLineColor(Color.); // Polyline 컬러 지정.

                        var customMarker = MapPOIItem();

                        // Polyline 좌표 지정.
                        for(i in 0 .. locationData.size-1)
                        {
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(locationData[i].promiseLat!!, locationData[i].promiseLon!!));

                            customMarker.setItemName(locationData[i].userName);
                            customMarker.setTag(1);
                            customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(locationData[i].promiseLat!!, locationData[i].promiseLon!!));
                            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                            customMarker.setCustomImageResourceId(R.drawable.marker_member); // 마커 이미지.
                            customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                            customMarker.setCustomImageAnchor(0.5f, 0.75f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                            mapView.addPOIItem(customMarker);

                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(recomPromiseLat, recomPromiseLon));
                        }


                        customMarker.setItemName(recomPlace);
                        customMarker.setTag(1);
                        customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(recomPromiseLat, recomPromiseLon));
                        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                        customMarker.setCustomImageResourceId(R.drawable.marker_place); // 마커 이미지.
                        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                        customMarker.setCustomImageAnchor(0.5f, 0.765f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                        mapView.addPOIItem(customMarker);

                        // Polyline 지도에 올리기.
                        mapView.addPolyline(polyline);
                        //mapView.addPolyline(polyline2);

                        // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                        var mapPointBounds = MapPointBounds(polyline.getMapPoints());
                        var padding : Int = 100; // px
                        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                        var test : String = ""
                        test = locationData.toString()
                        Log.v("TAG","위도경도 데이터 값"+ test)

                    }
                }

                override fun onFailure(call: Call<GetLocationResponse>?, t: Throwable?) {
                    Log.v("TAG","폴리라인 위도경도 통신 실패")
                }
            })
        } catch (e: Exception) {
        }

    }

    override fun onBackPressed() {

        if(modify_flag == 0){
            var intent = Intent()
            intent.putExtra("backBtnFlag", 1)
            setResult(28, intent)
            finish()
        }
        else{
            var intent = Intent()
            intent.putExtra("backBtnFlag", 1)
            setResult(29, intent)
            finish()
        }

    }


}
