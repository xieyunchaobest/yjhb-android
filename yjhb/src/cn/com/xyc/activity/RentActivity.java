package cn.com.xyc.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
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
	
	Intent intent =null;
	
	Map storegetmap=null;
	String getTime="";
	String returnTime="";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rent);
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
		btnRent.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(validate()==false) return ;
			}
		});
		
		ltgetmd.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 intent = new Intent(RentActivity.this,
						StoreListActivity.class);
				intent.putExtra("fromFlag", STORE_GET_CODE);
				
				startActivityForResult(intent,STORE_GET_CODE);
			}
		});
		
		ltreturnmd.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(RentActivity.this,
						StoreListActivity.class);
				intent.putExtra("fromFlag", STORE_RETURN_CODE);
				startActivityForResult(intent,STORE_RETURN_CODE);
			}
		});
		
		ltgetdate.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
					DatePicker	birth = new DatePicker(RentActivity.this, new DateTimeSetListener() {
						public void onDateSet(int year, int month,
								int day,int h) {
							getTime=year+"-"+(month>=10?month:"0"+month)+"-"+day+" "+(h>=10?h:"0"+h)+":00";
							ltgetdate.getValueText().setText(getTime);
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
							int day,int hour) {
						returnTime=year+"-"+(month>=10?month:"0"+month)+"-"+day+" "+(hour>=10?hour:"0"+hour)+":00";
						ltreturndate.getValueText().setText(returnTime);
					}
					 
				});
				birth.showAtLocation(RentActivity.this.findViewById(R.id.root),
						Gravity.BOTTOM, 0, 0);
			}
		});
		
		
		ltgetmodel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(RentActivity.this,
						CarListActivity.class);
				intent.putExtra("fromFlag", CAR_GET_CODE);
				startActivityForResult(intent,CAR_GET_CODE);
			}
		});
		
		ltretunmodel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(RentActivity.this,
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
		        	storegetmap=(Map)b.getSerializable("store");
		        	
		        	ltgetmd.getValueText().setText((String)storegetmap.get("item_name"));
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
	 
	 
	 public boolean validate() {
		 if(StringUtil.isBlank(ltgetmd.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "请选择门店名称！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }
		 if(StringUtil.isBlank(ltgetdate.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "请选择用车时间！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }
		 
		 if(StringUtil.isBlank(ltgetmodel.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "请选择车辆型号！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }
		 if(StringUtil.isBlank(ltreturnmd.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "请选择门店名称！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }if(StringUtil.isBlank(ltreturndate.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "请选择还车时间！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }
		 if(StringUtil.isBlank(ltretunmodel.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "请选择车辆型号！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }
		 if((long)(Long.parseLong(returnTime.replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "")) ) <=
				 (long)(Long.parseLong(getTime.replaceAll(" ", "").replaceAll(":", "").replaceAll("-", "")) )
				 ) {
			 Toast.makeText(getApplicationContext(), "还车时间应晚于用车时间！",
						Toast.LENGTH_SHORT).show();
			 return false;
		 }
		 return true;
	 }
	 
	 
}
