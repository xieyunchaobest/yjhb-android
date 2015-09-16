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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.DataPub;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

	List storeList=new ArrayList();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);
	
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
		DataPub d=new DataPub();
		storeList=d.initStoreList(this);
		
		Map m=(Map)(storeList).get(0); 
		LatLng p = new LatLng((Double)m.get("item_jd"),(Double)m.get("item_wd"));
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().target(p).zoom(15).build()));
		OverlayOptions ooA = new MarkerOptions().position(p).icon(bdA)
				.zIndex(9).draggable(true);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
		super.setTitleBar("游捷滑板",View.GONE,View.GONE,View.INVISIBLE,false);
		
		
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
			if (marker == mMarkerA ) {
				Button button = new Button(getApplicationContext());
				//button.setBackgroundResource(R.drawable.popup);
				
				button.setText("我要租车");
				button.setBackgroundResource(R.drawable.btn_map_sign);
				button.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
//						marker.setIcon(bd);
//						mBaiduMap.hideInfoWindow();
						Bundle bundle = new Bundle();
						bundle.putSerializable("storeData", (HashMap)storeList.get(0));
						intent.putExtras(bundle);
						intent.setClass(MapActivity.this, StoreInfoActivity.class);
						startActivity(intent);

					}
				});
				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(button, ll, -87);
				mBaiduMap.showInfoWindow(mInfoWindow);
			
			}  
			return true;
		}
	});
	
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
