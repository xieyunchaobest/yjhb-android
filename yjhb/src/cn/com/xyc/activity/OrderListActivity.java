package cn.com.xyc.activity;

import java.io.InputStream;
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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.xyc.R;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.view.PullToRefreshListView;
import cn.com.xyc.wxapi.WXEntryActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OrderListActivity extends BaseListActivity {

	private SimpleAdapter simpleAdapter;
	private PullToRefreshListView refreshListView; 
	private TextView tv=null;

	private List orderList = new ArrayList();
	private String[] key = {"item_img","item_model", "item_date","item_mdmc",
			"item_fee","item_id"};
	String isCanDefectInput=null;
	JSONObject reqJson=new JSONObject();
	Result response=null;
	boolean isReq=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		super.setTitleBar("订单",View.GONE,View.GONE,View.INVISIBLE,false);
		tv=(TextView)findViewById(R.id.tv_norecord);
		tv.setVisibility(View.GONE);
		refreshListView = (PullToRefreshListView) getListView();
		System.out.println("开始开始开始开始开始开始开始开始开始开始开始开始");
		if(isLogin()) {
			super.showProcessDialog(false);
			initView();
			isReq=true;
			getOrderList();
		}
		registerListener();
		 
	}
	
	public boolean isLogin(){
		CacheProcess c=new CacheProcess();
		String cache=c.getCacheValueInSharedPreferences(this, Constant.LOCAL_STORE_KEY_USER);
		if(StringUtil.isBlank(cache)) {
			startActivity(new Intent(OrderListActivity.this, LoginActivity.class));
			return false;
		}
		JSONObject user = JSON.parseObject(cache);  
		System.out.println("uuuuuuuuuuuuuuu="+cache);
		if(user!=null && user.containsKey("mobileNo") && !(StringUtil.isBlank(user.getString("mobileNo")))) {
			return true;
		}else {
			startActivity(new Intent(OrderListActivity.this, LoginActivity.class));
			return false;
		}
	}
	
	
 
	protected void redrawUI() {
		simpleAdapter=new SimpleAdapter(OrderListActivity.this, orderList,
				R.layout.order_list_item, key, new int[] {
				R.id.item_img,
				R.id.item_model,
				R.id.item_date,
				R.id.item_mdmc,
				R.id.item_fee,
				R.id.item_id});
		refreshListView.setDivider(new ColorDrawable(Color.GRAY));  
		refreshListView.setDividerHeight(1);
		setListAdapter(simpleAdapter);
		refreshListView.post(new Runnable(){
		    public void run(){
		    	for(int i=0;i<refreshListView.getChildCount();i++) {
		    		if(i==0)continue;
		    		RelativeLayout ll=(RelativeLayout)refreshListView.getChildAt(i);
					//RelativeLayout lla=(RelativeLayout)ll.findViewById(R.id.lay_item);
					RelativeLayout rl=(RelativeLayout)ll.findViewById(R.id.lay_img);
					ImageView iv=(ImageView)rl.findViewById(R.id.item_img);
					iv.setImageBitmap((Bitmap)((Map)orderList.get(i-1)).get("item_img"));
					 
				} 
		 
		    }
		});
		
	}
	
	public void getOrderList() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_GET_ORDERS,
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
				
			}
			isReq=false;
			if (mProgressDialog != null)
				mProgressDialog.dismiss();// 当接到消息时，关闭进度条
		}
	};
	
	
	public void loadImage() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
			@Override
			public void run() {
				for(int i=0;i<orderList.size();i++) {
					Map m=(Map)orderList.get(i);
					Object obj=m.get(key[0]);
					if(obj instanceof String) {
						try {
							URL url = new URL(Constant.IMGURL_CONTEXT+obj);
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
					}else {
						m.put("item_img", (Bitmap)obj);
					}
					
				}
				Message m = new Message();
				m.what = 2;
				handler.sendMessage(m);
			}
		});
mThread.start();
	}
	
	private void initDataSource(){
		com.alibaba.fastjson.JSONArray ja = (JSONArray) JSON.toJSON(response.result);
		orderList = com.alibaba.fastjson.JSON.parseArray(
				ja.toJSONString(),
				java.util.HashMap.class);
		if(orderList!=null) {
			for(int i=0;i<orderList.size();i++) {
				Map m=(Map)orderList.get(i);
				String payDate=(String)m.get("item_date");
				if(payDate==null ||"".equals(payDate)) {
					payDate="未支付";
					m.put("item_date", payDate);
				}
			}
		}
		if(orderList==null || orderList.size()==0) {
			tv.setVisibility(View.VISIBLE);
			refreshListView.setVisibility(View.GONE);
		}else {
			refreshListView.setVisibility(View.VISIBLE);
			tv.setVisibility(View.GONE);
			loadImage();
		}
	 
		
	}
	

	
	 
	@Override
	protected void onResume() {
		super.onResume();
		System.out.print("继续继续继续继续继续继续继续");
		
		if (isReq == false) {
			initView();
			getOrderList();
		}
		
	}
	 
	
 
	protected void initView() {
		CacheProcess cache=new CacheProcess();
		String user=cache.getCacheValueInSharedPreferences(this, Constant.LOCAL_STORE_KEY_USER);
		JSONObject json=new JSONObject();
		if(!StringUtil.isBlank(user)) {
			json=json.parseObject(user); 
			String mobileNo=json.getString("mobileNo");
			System.out.println("mobileNomobileNomobileNomobileNo="+mobileNo);
			reqJson.put("mobileNo", mobileNo);
		}
		
	}
	
 
 
	 
 
	protected void registerListener() {
		refreshListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
				HashMap m=(HashMap)orderList.get(arg2-1);
				String tradeType=(String)m.get("trade_type");
				Class target=null;
				if("B".equals(tradeType)) {
					target=BuyConfirmActivity.class;
				}else {
					target=WXEntryActivity.class;
				}
				Intent intent = new Intent();
				Bundle b=new Bundle();
				b.putInt("orderId",(Integer)m.get("item_id"));
				b.putString("payDate", (String)m.get("item_date"));
				
				intent.putExtras(b);
				intent.setClass(OrderListActivity.this,
						target);
				startActivity(intent);
            }
             
        });
	}
}
