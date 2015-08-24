package cn.com.xyc.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.JsonUtil;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.view.datepicker.DatePicker;
import cn.com.xyc.view.datepicker.DatePicker.DateTimeSetListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RentActivity extends BaseActivity {
	private Button btnRent;
	private LabelText ltgetmd;//取车门店
	private LabelText ltreturnmd;//门店
	private LabelText ltgetdate;
	private LabelText ltreturndate;
	private LabelText ltgetmodel;
	private LabelText ltretunmodel;
	private LabelText lttotalfee;
	
	private int STORE_GET_CODE=0;
	private int STORE_RETURN_CODE=1;
	
	private int CAR_GET_CODE=2;
	private int CAR_RETURN_CODE=3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rent);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
			initView();
			registerListener();
			super.setTitleBar("租车",View.GONE,View.GONE,View.INVISIBLE,false);
			isLogin();
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void initView() {
		btnRent=(Button)findViewById(R.id.btn_rent);
		ltgetmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltreturnmd=(LabelText)findViewById(R.id.elt_mdmc_return);
		ltgetdate=(LabelText)findViewById(R.id.elt_time);
		ltreturndate=(LabelText)findViewById(R.id.elt_time_return);
		ltgetmodel=(LabelText)findViewById(R.id.elt_clxh);
		ltretunmodel=(LabelText)findViewById(R.id.elt_clxh_return);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		lttotalfee.getValueText().setText("65元");
	}
	
	
	protected void registerListener() {
		ltgetmd.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RentActivity.this,
						StoreListActivity.class);
				intent.putExtra("fromFlag", STORE_GET_CODE);
				
				startActivityForResult(intent,STORE_GET_CODE);
			}
		});
		
		ltreturnmd.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RentActivity.this,
						StoreListActivity.class);
				intent.putExtra("fromFlag", STORE_RETURN_CODE);
				startActivityForResult(intent,STORE_RETURN_CODE);
			}
		});
		
		ltgetdate.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
					DatePicker	birth = new DatePicker(RentActivity.this, new DateTimeSetListener() {
						public void onDateSet(int year, int month,
								int day) {
							ltgetdate.getValueText().setText(year+"-"+(month>10?month:"0"+month)+"-"+day);
						}
						 
					});
					birth.showAtLocation(RentActivity.this.findViewById(R.id.root),
							Gravity.BOTTOM, 0, 0);
				}
			});
		
		ltreturndate.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				DatePicker	birth = new DatePicker(RentActivity.this, new DateTimeSetListener() {
					public void onDateSet(int year, int month,
							int day) {
						ltreturndate.getValueText().setText(year+"-"+(month>10?month:"0"+month)+"-"+day);
					}
					 
				});
				birth.showAtLocation(RentActivity.this.findViewById(R.id.root),
						Gravity.BOTTOM, 0, 0);
			}
		});
		
		ltgetmodel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RentActivity.this,
						CarListActivity.class);
				intent.putExtra("fromFlag", CAR_GET_CODE);
				
				startActivityForResult(intent,CAR_GET_CODE);
			}
		});
		
		ltretunmodel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RentActivity.this,
						CarListActivity.class);
				intent.putExtra("fromFlag", CAR_RETURN_CODE);
				startActivityForResult(intent,CAR_RETURN_CODE);
			}
		});
	}
	
	public void isLogin(){
		CacheProcess c=new CacheProcess();
		String cache=c.getCacheValueInSharedPreferences(this, Constant.LOCAL_STORE_KEY_USER);
		if(StringUtil.isBlank(cache)) {
			startActivity(new Intent(RentActivity.this, LoginActivity.class));
			return ;
		}
		JSONObject user = JSON.parseObject(cache);  
		System.out.println("uuuuuuuuuuuuuuu="+cache);
		if(user!=null && user.containsKey("mobileNo") && !(StringUtil.isBlank(user.getString("mobileNo")))) {
		}else {
			startActivity(new Intent(RentActivity.this, LoginActivity.class));
		}
	}
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(data!=null) {
			 if(requestCode==STORE_GET_CODE){
		        	Bundle b=data.getExtras();
		        	Map storemap=(Map)b.getSerializable("store");
		        	ltgetmd.getValueText().setText((String)storemap.get("item_name"));
		        }
		        else if(requestCode==STORE_RETURN_CODE){
		        	Bundle b=data.getExtras();
		        	Map storemap=(Map)b.getSerializable("store");
		        	ltreturnmd.getValueText().setText((String)storemap.get("item_name"));
		        }else if(requestCode==CAR_GET_CODE){
		        	Bundle b=data.getExtras();
		        	Map caremap=(Map)b.getSerializable("car");
		        	ltgetmodel.getValueText().setText((String)caremap.get("item_model"));
		        }else if(requestCode==CAR_RETURN_CODE){
		        	Bundle b=data.getExtras();
		        	Map caremap=(Map)b.getSerializable("car");
		        	ltretunmodel.getValueText().setText((String)caremap.get("item_model"));
		        }
		 }
	        
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	 
	 
}
