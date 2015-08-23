package cn.com.xyc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends BaseActivity {

	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.map);
			Intent intent = getIntent();
			mMapView = (MapView) findViewById(R.id.bmapView);
//			if (intent.hasExtra("x") && intent.hasExtra("y")) {
//				// 当用intent参数时，设置中心点为指定点
//				Bundle b = intent.getExtras();
//				LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
//				mMapView = new MapView(this,
//						new BaiduMapOptions().mapStatus(new MapStatus.Builder()
//								.target(p).build()));
//			} else {
//				mMapView = new MapView(this, new BaiduMapOptions());
//			}
//			
			LatLng p = new LatLng(39.983868,116.420128);
			mBaiduMap = mMapView.getMap();
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(p).zoom(15).build()));
			OverlayOptions ooA = new MarkerOptions().position(p).icon(bdA)
					.zIndex(9).draggable(true);
			mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			super.setTitleBar("游捷滑板",View.GONE,View.VISIBLE,View.INVISIBLE,false);
			
			
			mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
				public void onMapClick(LatLng point) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
				}

				public boolean onMapPoiClick(MapPoi poi) {
					return false;
				}
			});
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				
				OnInfoWindowClickListener listener = null;
				if (marker == mMarkerA ) {
					Button button = new Button(getApplicationContext());
					button.setBackgroundResource(R.drawable.popup);
					
					button.setText("我要租车");
					button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
//							marker.setIcon(bd);
//							mBaiduMap.hideInfoWindow();
							startActivity(new Intent(MapActivity.this, StoreInfoActivity.class));

						}
					});
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(button, ll, -47);
					mBaiduMap.showInfoWindow(mInfoWindow);
				
				}  
				return true;
			}
		});
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	/**
	 */
	public void rightButtonOnClick() {
		titleRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(MapActivity.this, LoginActivity.class));
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
		bdA.recycle();
	}

 
}
