package cn.com.xyc.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.com.xyc.R;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.DataPub;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends BaseActivity {

	List storeList=new ArrayList();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private Marker mMarkerB;
	private Marker mMarkerC;
	private Marker mMarkerD;
	private Marker mMarkerE;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	BitmapDescriptor bdB = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markb);
	BitmapDescriptor bdC = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markc);
	BitmapDescriptor bdD = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_markd);
	BitmapDescriptor bdE = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marke);
	
	
	
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true;// 是否首次定位
	
	// 定位相关
		LocationClient mLocClient;
	
	Result response=null;
	private JSONObject reqJson = new JSONObject();
	Intent intent=new Intent();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.map);
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
			
			CacheProcess c= new CacheProcess();
			String cache=c.getCacheValueInSharedPreferences(this, Constant.LOCAL_STORE_CACHES);
			if(!StringUtil.isBlank(cache)) {
				System.out.println("不为空，直接加载数据地图");
				initMap();
			}else {
				System.out.println("为空，需要请求数据加载地图");
				requestCache();
			}
//			 
	}
	
	
	

	
	public void initMap() {
		Intent it=getIntent();
		Bundle b=it.getExtras();
		mBaiduMap = mMapView.getMap();
		if(b==null) {
			super.setTitleBar("游捷用车",View.GONE,View.GONE,View.INVISIBLE,false);
			DataPub d=new DataPub();
			storeList=d.initStoreList(this);
			
			Map m=(Map)(storeList).get(0); 
			LatLng p = new LatLng((Double)m.get("item_jd"),(Double)m.get("item_wd"));
			
			mBaiduMap.setMyLocationEnabled(true);
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(p).zoom(12).build()));
			for(int i=0;i<storeList.size();i++) {
				Map mm=(Map)(storeList).get(i); 
				LatLng pp = new LatLng((Double)mm.get("item_jd"),(Double)mm.get("item_wd"));
				if(i==0) {
					OverlayOptions ooA = new MarkerOptions().position(pp).icon(bdA)
							.zIndex(9).draggable(true);
					mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
				}
				
				if(i==1) {
					OverlayOptions ooB = new MarkerOptions().position(pp).icon(bdB)
							.zIndex(9).draggable(true);
					mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
				}
				if(i==2) {
					OverlayOptions ooC = new MarkerOptions().position(pp).icon(bdC)
							.zIndex(9).draggable(true);
					mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
				}
				if(i==3) {
					OverlayOptions ooD = new MarkerOptions().position(pp).icon(bdD)
							.zIndex(9).draggable(true);
					mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
				}
				if(i==4) {
					OverlayOptions ooE = new MarkerOptions().position(pp).icon(bdE)
							.zIndex(9).draggable(true);
					mMarkerE = (Marker) (mBaiduMap.addOverlay(ooE));
				}
				
			}
			
//			OverlayOptions ooA = new MarkerOptions().position(p).icon(bdA)
//					.zIndex(9).draggable(true);
//			mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
	//	
			
			
			mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
				public void onMapClick(LatLng point) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
				}

				public boolean onMapPoiClick(MapPoi poi) {
					return false;
				}
			});
	 
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				
				OnInfoWindowClickListener listener = null;
					Button button = new Button(getApplicationContext());
					//button.setBackgroundResource(R.drawable.popup);
					
					button.setText("我要租车");
					button.setBackgroundResource(R.drawable.btn_map_sign);
					button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
//							marker.setIcon(bd);
//							mBaiduMap.hideInfoWindow();
							Bundle bundle = new Bundle();
							if (marker == mMarkerA ) {
								bundle.putSerializable("storeData", (HashMap)storeList.get(0));
							}else if(marker == mMarkerB) {
								bundle.putSerializable("storeData", (HashMap)storeList.get(1));
							}else if(marker == mMarkerC) {
								bundle.putSerializable("storeData", (HashMap)storeList.get(2));
							}else if(marker == mMarkerD) {
								bundle.putSerializable("storeData", (HashMap)storeList.get(3));
							}else if(marker == mMarkerE) {
								bundle.putSerializable("storeData", (HashMap)storeList.get(4));
							}
							
							intent.putExtras(bundle);
							intent.setClass(MapActivity.this, StoreInfoActivity.class);
							startActivity(intent);

						}
					});
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(button, ll, -87);
					mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		}else {
			super.setTitleBar("门店位置",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			HashMap hm=(HashMap)b.getSerializable("storeinfo");
			LatLng pp = new LatLng((Double)hm.get("item_jd"),(Double)hm.get("item_wd"));
			OverlayOptions ooA = new MarkerOptions().position(pp).icon(bdA)
					.zIndex(13).draggable(true);
			mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		}
			
	
		
	
	
	}
	
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 MapActivity.this.finish();
			 }
			 return true;
		 }
		 
	
	  
    public void requestCache() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_GET_LOCAL_CACHE,
								reqJson.toJSONString());
						if (handleError(response) == true)
							return;
						Message m = new Message();
						m.what = 1;
						handler.sendMessage(m);
					}
				});
		mThread.start();
	
    }
    
    /**
     * get cars
     */
    public void initCache() { 
    	CacheProcess c =new CacheProcess();
    	try {
			c.save(this, Constant.LOCAL_STORE_CACHES, (JSONObject) JSON.toJSON(response.result) );
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				initCache();
				initMap();
			}
			case 2: {
			} 
			}
		}
	};
	
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
