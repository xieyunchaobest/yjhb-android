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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
			setTitleBar("选择门店",View.GONE,View.GONE,View.GONE,false);

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
				Intent intent=getIntent();
				int flag=intent.getIntExtra("fromFlag",0);
				intent.putExtra("store",  (HashMap)storeList.get(arg2-1));
				setResult(flag,intent);

				StoreListActivity.this.finish();  
//				Intent intent = new Intent();
//				Map m=funcNodeList.get(arg2-1);
//				String code=(String)m.get("func_node_list_item_code");
//				if("QXLR".equals(code)){
//					if("1".equals(isCanDefectInput)) {
//						intent.setClass(StoreListActivity.this,
//								DefecAdd1tActivity.class);
//						startActivity(intent);
//					}else {
//						Toast.makeText(getApplicationContext(), "当前时间已超过开业时间+180天，不能录入!", Toast.LENGTH_SHORT).show();
//					}
//				}else if("SBLR".equals(code)){
//					intent.setClass(FuncNodeListActivity.this,
//							DeviceAdd1tActivity.class);
//					startActivity(intent);
//				}else{
//					intent.setClass(FuncNodeListActivity.this,
//							SettingActivity.class);
//					startActivity(intent);
//				}

			}

		});
	}
	

}
