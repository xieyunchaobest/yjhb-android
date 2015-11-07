package cn.com.xyc.activity;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;
import cn.com.xyc.view.PullToRefreshListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CarListActivity  extends BaseListActivity{

	private int CAR_GET_CODE=2;
	private int CAR_RETURN_CODE=3;
	
	private SimpleAdapter simpleAdapter;
	private PullToRefreshListView refreshListView; 

	private ArrayList<Map<String, Object>> carList = new ArrayList<Map<String, Object>>();
	private String[] key = {"item_img","item_model", "item_xh",
			"item_fee","item_id","item_topPrice","item_priceM","item_priceE"};
	String isCanDefectInput=null;
	
	Result response=null;
	private JSONObject reqJson = new com.alibaba.fastjson.JSONObject();
	Intent intent=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.car_list);
			
			intent=getIntent();
			

			// 设置标题栏
			setTitleBar("选择车辆",View.VISIBLE,View.GONE,View.GONE,false);

			refreshListView = (PullToRefreshListView) getListView();
			registerListener();
			super.showProcessDialog(false);
			getCars();
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
 
	}
	
	
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 CarListActivity.this.finish();
			 }
			 return true;
		 }
		 
		 

	 
	public void loadImage() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
			@Override
			public void run() {
				for(int i=0;i<carList.size();i++) {
					Map m=(Map)carList.get(i);
					String imgAddr=(String)m.get(key[0]);
					try {
						URL url = new URL(Constant.IMGURL_CONTEXT+imgAddr);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input = connection.getInputStream();
						Bitmap myBitmap = BitmapFactory.decodeStream(input);
						input.close();
						m.put("item_img", myBitmap);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				Message m = new Message();
				m.what = 2;
				handler.sendMessage(m);
			}
		});
mThread.start();
	}
	
	public void getCars() {
		String storeId=intent.getStringExtra("storeId");
		int flag=intent.getIntExtra("fromFlag",0);
		reqJson.put("storeId", storeId);
		reqJson.put("tradeType", flag);
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_GET_GET_CARS,
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
	
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				initDataSource();
				break;
			}
			case 2: {
				redrawUI();
			}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();// 当接到消息时，关闭进度条
			}
		}
	};
	
	private void initDataSource(){
 
		com.alibaba.fastjson.JSONArray ja = (JSONArray) JSON.toJSON(response.result);
		List list = com.alibaba.fastjson.JSON.parseArray(
				ja.toJSONString(),
				java.util.HashMap.class);
		for(int i=0;i<list.size();i++) {
			Map m=(Map)list.get(i);
			Integer sid=(Integer)m.get("id");
			String model=(String)m.get("model");
			Integer kmAmount=(Integer)m.get("kmCount");
			Double price=((BigDecimal)m.get("price")).doubleValue();
			String imgAddr=(String)m.get("imgAddr");
			Double priceTop=((BigDecimal)m.get("topPrice")).doubleValue();
			Double priceM=((BigDecimal)m.get("priceM")).doubleValue();
			Double priceE=((BigDecimal)m.get("priceE")).doubleValue();
            
			Map mm=new HashMap(); 
			mm.put(key[0], imgAddr);
			mm.put(key[1], model);
			mm.put(key[2], kmAmount+"公里");
			mm.put(key[3], price+"元/小时");
			mm.put(key[4], sid);
			mm.put(key[5], priceTop);
			mm.put(key[6], priceM);
			mm.put(key[7], priceE);
			
			carList.add(mm);
		}
		loadImage();
	}
 
	protected void redrawUI() {
		simpleAdapter=new SimpleAdapter(CarListActivity.this, carList,
				R.layout.car_list_item, key, new int[] {
				R.id.item_img,
				R.id.item_model,
				R.id.item_xh,
				R.id.item_fee,
				R.id.item_id});
		refreshListView.setDivider(new ColorDrawable(Color.GRAY));  
		refreshListView.setDividerHeight(1);
		
		//refreshListView.setBackgroundColor(R.drawable.background);
		setListAdapter(simpleAdapter);
		refreshListView.post(new Runnable(){
		    public void run(){
		    	for(int i=0;i<refreshListView.getChildCount();i++) {
		    		if(i==0)continue;
		    		RelativeLayout ll=(RelativeLayout)refreshListView.getChildAt(i);
					//RelativeLayout lla=(RelativeLayout)ll.findViewById(R.id.lay_item);
					RelativeLayout rl=(RelativeLayout)ll.findViewById(R.id.lay_img);
					ImageView iv=(ImageView)rl.findViewById(R.id.item_img);
					iv.setImageBitmap((Bitmap)((Map)carList.get(i-1)).get("item_img"));
				} 
		 
		    }
		});
		
	}
	
 

 
	protected void registerListener() {
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=getIntent();
				int flag=intent.getIntExtra("fromFlag",0);
				for(int i=0;i<carList.size();i++) {
					Map m=(Map)carList.get(i);
					m.put("item_img", null);
				}
				intent.putExtra("car",  (HashMap)carList.get(arg2-1));
				setResult(flag,intent);

				CarListActivity.this.finish();  


			}

		});
	}
	 

}
