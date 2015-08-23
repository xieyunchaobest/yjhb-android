package cn.com.xyc.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.view.PullToRefreshListView;

public class StoreListActivity  extends BaseListActivity{

	private int STORE_GET_CODE=0;
	private int STORE_RETURN_CODE=1;
	
	private SimpleAdapter simpleAdapter;
	private PullToRefreshListView refreshListView; 

	private ArrayList<Map<String, Object>> storeList = new ArrayList<Map<String, Object>>();
	private String[] key = {"item_name", "item_address",
			"item_id","item_jd","item_wd"};
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
		}catch(Exception e) {
			e.printStackTrace();
		}
		
 
	}
	
	private void initDataSource(){
		Map m1=new HashMap();
		m1.put(key[0], "西土城店1");
		m1.put(key[1], "西土城南路32号会展中心南100米游捷滑板");
		m1.put(key[2], "1");
		m1.put(key[3], "11.12121");
		m1.put(key[4], "11.12121");
		
		Map m2=new HashMap();
		m2.put(key[0], "西土城店2");
		m2.put(key[1], "西土城南路32号会展中心南100米游捷滑板");
		m2.put(key[2], "1");
		m2.put(key[3], "11.12121");
		m2.put(key[4], "11.12121");
		
		storeList.add(m1);
		storeList.add(m2);
//		
//		String funcList=this.getCacheProcess().getCacheValueInSharedPreferences(this, "funcList"); 
//		isCanDefectInput=this.getCacheProcess().getCacheValueInSharedPreferences(this, "defectIsCanInput"); 
//		com.alibaba.fastjson.JSONArray funcArr=com.alibaba.fastjson.JSONObject.parseArray(funcList);
//		if(funcArr!=null && funcArr.size()>0) {
//			for(int i=0;i<funcArr.size();i++) {
//				com.alibaba.fastjson.JSONObject j=(com.alibaba.fastjson.JSONObject)funcArr.get(i);
//				String funcName=j.getString("name");
//				if("缺陷管理".equals(funcName)) {
//					Map defectMap=new HashMap();
//					defectMap.put(key[0], R.drawable.func_broken);
//					defectMap.put(key[1], "缺陷录入");
//					defectMap.put(key[2], "QXLR");
//					funcNodeList.add(defectMap);
//				}
//				if("设备台账".equals(funcName)) {
//					Map deviceMap=new HashMap();
//					deviceMap.put(key[0], R.drawable.func_device);
//					deviceMap.put(key[1], "设备录入");
//					deviceMap.put(key[2], "SBLR");
//					funcNodeList.add(deviceMap);
//				}
//			}
//		}
//		
//		Map settingMap=new HashMap();
//		settingMap.put(key[0], R.drawable.func_tools);
//		settingMap.put(key[1], "设置");
//		settingMap.put(key[2], "SZ");
//		
//		
//		
//		funcNodeList.add(settingMap);
	}
 
	protected void initView() {
		initDataSource();
		simpleAdapter=new SimpleAdapter(StoreListActivity.this, storeList,
				R.layout.store_list_item, key, new int[] {
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
