package cn.com.xyc.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
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
	
	private TextView tvtxt_fee;
	
	private int STORE_GET_CODE=0;
	private int STORE_RETURN_CODE=1;
	
	private int CAR_GET_CODE=2;
	private int CAR_RETURN_CODE=3;
	
	Intent intent =null;
	
	Map storegetmap=null;
	String getTime="";
	String returnTime="";
	Map getcaremap=null;
	
	Map feeMap=new HashMap();
	Map getMap=new HashMap();
	Map returnMap=new HashMap();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rent);
			intent=getIntent();
			Object store=intent.getSerializableExtra("storeData");//from page StoreInfo
			if(store!=null) {
				super.setTitleBar("租车",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			}else {
				super.setTitleBar("租车",View.GONE,View.GONE,View.INVISIBLE,false);
			}
			
			initView();
			registerListener();
			ActivityUtil.getInstance().addActivity(this);
		//	isLogin();
			 
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
		lttotalfee.getValueText().setText("0元");
		
		tvtxt_fee=(TextView)findViewById(R.id.txt_fee);
		
	
		Object store=intent.getSerializableExtra("storeData");//from page StoreInfo
		if(store!=null) {
			storegetmap=(Map)store;
			String storeName=(String)storegetmap.get("item_name");
			ltgetmd.getValueText().setText(storeName);
			getMap.put("storeName", storeName);
		}
		
	}
	
	
	public void calcPrice() {
		double bsxf=5.00d;//基本手续费
		double syf=0.0d;//使用费
		double mdsxf=0d;//门店手续费，取车和换车门店不同时
		Double totalfee=0d;
		double sinfee=0d;
		int useTime=0;
		if(!StringUtil.isBlank(ltgetmd.getValueText().getText().toString()) && 
				!StringUtil.isBlank(ltgetdate.getValueText().getText().toString()) && 
				!StringUtil.isBlank(ltgetmodel.getValueText().getText().toString()) &&
				!StringUtil.isBlank(ltreturnmd.getValueText().getText().toString()) &&
			 !StringUtil.isBlank(ltreturndate.getValueText().getText().toString())
				) {
			if(!ltgetmd.getValueText().getText().toString().equals(ltreturnmd.getValueText().getText().toString())) {
				mdsxf=5.00d;
			}
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date getDate=null;
			Date returnDate=null;
			try {
				getDate = df.parse(getTime);
				returnDate = df.parse(returnTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			useTime=(int) ((returnDate.getTime()-getDate.getTime())/(60 * 60 * 1000));
			sinfee=Double.parseDouble(((String)getcaremap.get("item_fee")).replaceAll("元/小时", ""));
			syf=sinfee*useTime;
			System.out.println("useTime======"+useTime);
			totalfee=mdsxf+bsxf+syf;
			if(getTime.equals(returnTime))totalfee=0d;
			if(totalfee<0)totalfee=0d;
		}
		feeMap.put("bsxf", bsxf);
		feeMap.put("sinfee", sinfee);
		feeMap.put("useTime", useTime);
		feeMap.put("mdsxf", mdsxf);
		feeMap.put("totalfee", totalfee);
		tvtxt_fee.setText("费用["+bsxf+"元手续费+"+sinfee+"元/h*"+useTime+"h+"+mdsxf+"元异店换车费]");
		lttotalfee.getValueText().setText(String.valueOf(totalfee));
	}
	
	protected void registerListener() {
		btnRent.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(validate()==false) return ;
				 intent = new Intent();
				 intent.setClass(RentActivity.this, RentOrderConfirmActivity.class);
				 HashMap m=new HashMap();
				 m.put("getMap", getMap);
				 m.put("returnMap", returnMap);
				 m.put("feeMap", feeMap); 
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", m);
				intent.putExtras(bundle);
				
				startActivity(intent);	
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
							getMap.put("date", getTime);
							calcPrice();
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
						returnMap.put("date", returnTime);
						calcPrice();
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
		        	String storeName=(String)storegetmap.get("item_name");
		        	ltgetmd.getValueText().setText(storeName);
		        	getMap.put("storeName", storeName);
		        	calcPrice();
		        }
		        else if(requestCode==STORE_RETURN_CODE){
		        	Bundle b=data.getExtras();
		        	Map storemap=(Map)b.getSerializable("store");
		        	String storeName=(String)storemap.get("item_name");
		        	ltreturnmd.getValueText().setText(storeName);
		        	returnMap.put("storeName", storeName);
		        	calcPrice();
		        }else if(requestCode==CAR_GET_CODE){
		        	Bundle b=data.getExtras();
		        	getcaremap=(Map)b.getSerializable("car");
		        	String carModel=(String)getcaremap.get("item_model");
		        	ltgetmodel.getValueText().setText(carModel);
		        	getMap.put("carModel", carModel);
		        	calcPrice();
		        }else if(requestCode==CAR_RETURN_CODE){
		        	Bundle b=data.getExtras();
		        	Map caremap=(Map)b.getSerializable("car");
		        	String carModel=(String)caremap.get("item_model");
		        	ltretunmodel.getValueText().setText(carModel);
		        	returnMap.put("carModel", carModel);
		        	calcPrice();
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
	 
	 
	@Override
	protected void onResume() {
		super.onResume();
		System.out.print("继续继续继续继续继续继续继续");
		isLogin();
	}
	 
}
