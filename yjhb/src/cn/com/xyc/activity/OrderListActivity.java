package cn.com.xyc.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import cn.com.xyc.R;
import cn.com.xyc.view.PullToRefreshListView;

public class OrderListActivity extends BaseListActivity {

	private SimpleAdapter simpleAdapter;
	private PullToRefreshListView refreshListView; 

	private ArrayList<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
	private String[] key = {"item_img","item_model", "item_date","item_mdmc",
			"item_fee","item_id"};
	String isCanDefectInput=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list);
		super.setTitleBar("订单",View.GONE,View.GONE,View.INVISIBLE,false);
		initView();
		registerListener();
	}
	
	

	private void initDataSource(){
		Map m1=new HashMap();
		m1.put(key[0], R.drawable.car_1);
		m1.put(key[1], "极品飞车1");
		m1.put(key[2], "2015-09-12");
		m1.put(key[3], "西土城大桥店");
		m1.put(key[3], "800元");
		m1.put(key[5], "1");
		
		Map m2=new HashMap();
		m2.put(key[0], R.drawable.car_1);
		m2.put(key[1], "极品飞车2");
		m2.put(key[2], "2015-09-14");
		m2.put(key[3], "西土城大桥店");
		m2.put(key[4], "650元");
		m2.put(key[5], "2");
		
		orderList.add(m1);
		orderList.add(m2);

	}
 
	protected void initView() {
		initDataSource();
		refreshListView = (PullToRefreshListView) getListView();
		simpleAdapter=new SimpleAdapter(OrderListActivity.this, orderList,
				R.layout.order_list_item, key, new int[] {
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
 

			}

		});
	}
}
