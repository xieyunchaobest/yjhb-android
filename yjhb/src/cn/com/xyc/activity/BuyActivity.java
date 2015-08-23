package cn.com.xyc.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.com.xyc.R;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.view.datepicker.DatePicker;
import cn.com.xyc.view.datepicker.DatePicker.DateTimeSetListener;

public class BuyActivity extends BaseActivity {
	
	private LabelText ltmd;//È¡³µÃÅµê
	private LabelText ltdate;
	private LabelText ltmodel;
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
			setContentView(R.layout.buy);
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
			super.setTitleBar("¹º³µ",View.GONE,View.GONE,View.INVISIBLE,false);
			initView();
			registerListener();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	public void initView() {
		ltmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltdate=(LabelText)findViewById(R.id.elt_time);
		ltmodel=(LabelText)findViewById(R.id.elt_clxh);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		lttotalfee.getValueText().setText("650Ôª");
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
								int day) {
							ltdate.getValueText().setText(year+"-"+(month>10?month:"0"+month)+"-"+day);
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
	 
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if(requestCode==STORE_GET_CODE){
	        	Bundle b=data.getExtras();
	        	Map storemap=(Map)b.getSerializable("store");
	        	ltmd.getValueText().setText((String)storemap.get("item_name"));
	        } else if(requestCode==CAR_GET_CODE){
	        	Bundle b=data.getExtras();
	        	Map caremap=(Map)b.getSerializable("car");
	        	ltmodel.getValueText().setText((String)caremap.get("item_model"));
	        } 
	        super.onActivityResult(requestCode, resultCode, data);
	    }
 
	 @Override
	public void onPause()
	{
		 System.out.println("ÔÝÍ£ÔÝÍ£ÔÝÍ£ÔÝÍ£ÔÝÍ£ÔÝÍ£ÔÝÍ£ÔÝÍ£");
		 super.onPause();
		}
}
