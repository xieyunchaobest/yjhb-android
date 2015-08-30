package cn.com.xyc.activity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.view.datepicker.DatePicker;
import cn.com.xyc.view.datepicker.DatePicker.DateTimeSetListener;

public class BuyActivity extends BaseActivity {
	
	private LabelText ltmd;//ȡ���ŵ�
	private LabelText ltdate;
	private LabelText ltmodel;
	private LabelText lttotalfee;
	
	private Button btnOk;
	private int STORE_GET_CODE=0;
	private int STORE_RETURN_CODE=1;
	
	private int CAR_GET_CODE=4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.buy);
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
			super.setTitleBar("����",View.GONE,View.GONE,View.INVISIBLE,false);
			initView();
			registerListener();
//			isLogin();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	public void isLogin(){
		CacheProcess c=new CacheProcess();
		String cache=c.getCacheValueInSharedPreferences(this, Constant.LOCAL_STORE_KEY_USER);
		if(StringUtil.isBlank(cache)) {
			startActivity(new Intent(BuyActivity.this, LoginActivity.class));
			return ;
		}
		JSONObject user = JSON.parseObject(cache);  
		System.out.println("uuuuuuuuuuuuuuu="+cache);
		if(user!=null && user.containsKey("mobileNo") && !(StringUtil.isBlank(user.getString("mobileNo")))) {
		}else {
			startActivity(new Intent(BuyActivity.this, LoginActivity.class));
		}
	}
	
	
	public void initView() {
		ltmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltdate=(LabelText)findViewById(R.id.elt_time);
		ltmodel=(LabelText)findViewById(R.id.elt_clxh);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		lttotalfee.getValueText().setText("0Ԫ");
		btnOk=(Button)findViewById(R.id.btn_buy);
	}
	
	
	protected void registerListener() {
		ltmd.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BuyActivity.this,
						StoreListActivity.class);
				intent.putExtra("fromFlag", STORE_GET_CODE);
				
				startActivityForResult(intent,STORE_GET_CODE);
			}
		});
		
 
		
		ltdate.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
					DatePicker	birth = new DatePicker(BuyActivity.this, new DateTimeSetListener() {
						public void onDateSet(int year, int month,
								int day,int hour) {
							ltdate.getValueText().setText(year+"-"+(month>10?month:"0"+month)+"-"+day+" "+hour+":00");
						}
						 
					});
					birth.showAtLocation(BuyActivity.this.findViewById(R.id.root),
							Gravity.BOTTOM, 0, 0);
				}
			});
		
 
		ltmodel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BuyActivity.this,
						CarListActivity.class);
				intent.putExtra("fromFlag", CAR_GET_CODE);
				
				startActivityForResult(intent,CAR_GET_CODE);
			}
		});
		
		btnOk.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
			 boolean flag=validate();
			 if(flag==false)return;
			 Intent intent = new Intent();
			 intent.setClass(BuyActivity.this, BuyConfirmActivity.class);
			 HashMap m=new HashMap();
			 m.put("storeName", ltmd.getValueText().getText());
			 m.put("date", ltdate.getValueText().getText());
			 m.put("model", ltmodel.getValueText().getText());
			 m.put("fee", lttotalfee.getValueText().getText());
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", m);
			intent.putExtras(bundle);
			
			startActivity(intent);	
			}
		});
	 
	}
	
	public boolean validate() {
		if(StringUtil.isBlank(ltmd.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "��ѡ���ŵ����ƣ�",
						Toast.LENGTH_SHORT).show();
			 return false;
		}
		if(StringUtil.isBlank(ltdate.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "��ѡ�񹺳�ʱ�䣡",
						Toast.LENGTH_SHORT).show();
			 return false;
		}
		if(StringUtil.isBlank(ltmodel.getValueText().getText().toString())) {
			 Toast.makeText(getApplicationContext(), "��ѡ�����ͺţ�",
						Toast.LENGTH_SHORT).show();
			 return false;
		}
		return true;
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(data!=null) {
			 if(requestCode==STORE_GET_CODE){
		        	Bundle b=data.getExtras();
		        	Map storemap=(Map)b.getSerializable("store");
		        	ltmd.getValueText().setText((String)storemap.get("item_name"));
		        } else if(requestCode==CAR_GET_CODE){
		        	Bundle b=data.getExtras();
		        	Map caremap=(Map)b.getSerializable("car");
		        	String model=(String)caremap.get("item_model");
		        	ltmodel.getValueText().setText(model);
		        	String fee=(String)caremap.get("item_fee");
		        	
		        	lttotalfee.getValueText().setText(fee.replaceAll("/Сʱ", ""));
		        	
		        } 
		        super.onActivityResult(requestCode, resultCode, data);
		    }
		 }
	 
	 
		@Override
		protected void onResume() {
			super.onResume();
			isLogin();
		}
}
