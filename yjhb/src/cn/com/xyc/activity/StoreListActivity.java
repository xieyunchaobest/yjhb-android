package cn.com.xyc.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.CrashHandler;
import cn.com.xyc.util.DataPub;
import cn.com.xyc.view.PullToRefreshListView;

import com.alibaba.fastjson.JSONObject;

public class StoreListActivity  extends BaseListActivity{

	private int STORE_GET_CODE=0;
	private int STORE_RETURN_CODE=1;
	
	private SimpleAdapter simpleAdapter;
	private PullToRefreshListView refreshListView; 

	private List  storeList = new ArrayList (); 
	String isCanDefectInput=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.store_list);

			// 设置标题栏
			setTitleBar("选择门店",View.VISIBLE,View.GONE,View.GONE,false);

			refreshListView = (PullToRefreshListView) getListView();
			initView();
			registerListener();
			ActivityUtil.getInstance().addActivity(this);
			CrashHandler crashHandler = CrashHandler.getInstance();    
	        crashHandler.init(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
 
	}
	
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 StoreListActivity.this.finish();
			 }
			 return true;
		 }
		 
		 
	 
 
	protected void initView() {
		DataPub p=new DataPub();
		storeList=p.initStoreList(this);
		simpleAdapter=new SimpleAdapter(StoreListActivity.this, storeList,
				R.layout.store_list_item, p.storekey, new int[] {
				R.id.item_name,
				R.id.item_address});
		refreshListView.setDivider(new ColorDrawable(Color.GRAY));  
		refreshListView.setDividerHeight(1);
		//refreshListView.setBackgroundColor(R.drawable.background);
		setListAdapter(simpleAdapter);
		
	}
	
 

 
	protected void registerListener() {
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap m=(HashMap)storeList.get(arg2-1);
				String isOpen=(String)m.get("item_is_open");
				
				updateStatInfo(String.valueOf((Integer)m.get("item_id")));
				if("N".equals(isOpen)) {
					 Toast.makeText(getApplicationContext(), "该门店暂未开业，请选择其他门店！",
								Toast.LENGTH_SHORT).show();
					 return ;
				}
				Intent intent=getIntent();
				int flag=intent.getIntExtra("fromFlag",0);
				intent.putExtra("store",  m);
				setResult(flag,intent);

				StoreListActivity.this.finish();  

			}

		});
	}
	
	
	public void updateStatInfo(final String storeId) {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						com.alibaba.fastjson.JSONObject j=new com.alibaba.fastjson.JSONObject();
						j.put("storeId", storeId);
						getPostHttpContent("",
								Constant.METHOD_GET_STORESTATINFO,
								j.toJSONString());
					}
				});
		mThread.start();
	}

}
