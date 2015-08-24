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

public class CarListActivity  extends BaseListActivity{

	private int CAR_GET_CODE=2;
	private int CAR_RETURN_CODE=3;
	
	private SimpleAdapter simpleAdapter;
	private PullToRefreshListView refreshListView; 

	private ArrayList<Map<String, Object>> carList = new ArrayList<Map<String, Object>>();
	private String[] key = {"item_img","item_model", "item_xh",
			"item_fee","item_id"};
	String isCanDefectInput=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.car_list);

			// 设置标题栏
			setTitleBar("选择车辆",View.GONE,View.GONE,View.GONE,false);

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
		m1.put(key[0], R.drawable.car_1);
		m1.put(key[1], "极品飞车1");
		m1.put(key[2], "30公里续航");
		m1.put(key[3], "30公里/小时");
		m1.put(key[4], "1");
		
		Map m2=new HashMap();
		m2.put(key[0], R.drawable.car_1);
		m2.put(key[1], "极品飞车2");
		m2.put(key[2], "40公里续航");
		m2.put(key[3], "40公里/小时");
		m2.put(key[4], "2");
		
		carList.add(m1);
		carList.add(m2);
	}
 
	protected void initView() {
		initDataSource();
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
		
	}
	
 

 
	protected void registerListener() {
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=getIntent();
				int flag=intent.getIntExtra("fromFlag",0);
				intent.putExtra("car",  (HashMap)carList.get(arg2-1));
				setResult(flag,intent);

				CarListActivity.this.finish();  


			}

		});
	}
	 

}
