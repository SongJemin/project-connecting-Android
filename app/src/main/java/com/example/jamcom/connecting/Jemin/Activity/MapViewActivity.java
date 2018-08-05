package com.example.jamcom.connecting.Jemin.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jamcom.connecting.R;

import net.daum.android.map.MapViewTouchEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class MapViewActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapViewTouchEventListener, MapView.CurrentLocationEventListener, MapView.POIItemEventListener {

    double currentLat, currentLon;
    double preferLat = 0.0;
    double preferLon = 0.0;
    int return_flag = 0;
    int roomID;

    private boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        Intent intent = getIntent();
        roomID = intent.getIntExtra("roomID", 0);
        Log.v("TAG", "맵뷰에서 받은 방번호 = " + roomID);

        // java code
        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mapView.setPOIItemEventListener(this);
        mapView.setCurrentLocationEventListener(this);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        mapView.setShowCurrentLocationMarker(true);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

        Button confirmBtn = (Button) findViewById(R.id.map_view_confirm_btn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return_flag = 1;
               Intent intent = new Intent(MapViewActivity.this, RoomSettingActivity.class);
               intent.putExtra("preferLat", preferLat);
               intent.putExtra("preferLon", preferLon);
               intent.putExtra("return_flag", return_flag);
               intent.putExtra("roomID", roomID);
               startActivity(intent);
            }
        });

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        mapView.removeAllPOIItems();

            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

            Log.i("TAG","위도 : " + mapPointGeo.latitude + "경도 : " + mapPointGeo.longitude);
            preferLat = mapPointGeo.latitude;
            preferLon = mapPointGeo.longitude;

            MapPOIItem marker = new MapPOIItem();
            marker.setItemName("선택한 위치");
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onSingleTap() {

    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public void onHoldMap() {

    }

    @Override
    public void onMoveMap() {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        currentLat = mapPointGeo.latitude;
        currentLon = mapPointGeo.longitude;
        Log.v("my_location", "현재위치 latitude = "+currentLat);
        Log.v("my_location", "현재위치 longitude = "+currentLon);
        if (flag) {
            MapPoint centerPoint = MapPoint.mapPointWithGeoCoord(currentLat, currentLon);
            mapView.setMapCenterPoint(centerPoint, true);

            flag = !flag;
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
