package cn.com.xyc.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.view.datepicker.DatePicker;
import cn.com.xyc.view.datepicker.DatePicker.DateTimeSetListener;

public class BuyConfirmActivity extends BaseActivity {
	
	private LabelText ltmd;//取车门店
	private LabelText ltdate;
	private LabelText ltmodel;
	private LabelText lttotalfee;
	
	private Button btnOk; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.buyconfirm);
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
			super.setTitleBar("订单确认",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			initView();
			registerListener();
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	public void initView() {
		ltmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltdate=(LabelText)findViewById(R.id.elt_time);
		ltmodel=(LabelText)findViewById(R.id.elt_clxh);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		btnOk=(Button)findViewById(R.id.btn_buy);
		
		Intent intent=getIntent();
		HashMap infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
		ltmd.getValueText().setText((String)infoMap.get("storeName"));
		ltdate.getValueText().setText((String)infoMap.get("date"));
		ltmodel.getValueText().setText((String)infoMap.get("model"));
		lttotalfee.getValueText().setText((String)infoMap.get("fee"));
	}
	
	
	protected void registerListener() {
		
		btnOk.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	 
	}
 
 
}
