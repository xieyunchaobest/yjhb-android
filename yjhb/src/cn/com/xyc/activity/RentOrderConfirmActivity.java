package cn.com.xyc.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.view.LabelText;

public class RentOrderConfirmActivity extends BaseActivity {
	private Button btn_submit_order;
	private LabelText ltgetmd;//取车门店
	private LabelText ltreturnmd;//门店
	private LabelText ltgetdate;
	private LabelText ltreturndate;
	private LabelText ltgetmodel;
	private LabelText ltretunmodel;
	private LabelText lttotalfee;
	
	private TextView tvtxt_fee; 
	Intent intent =null;
	
	Map storegetmap=null;
	String getTime="";
	String returnTime="";
	Map getcaremap=null;
	
	Map feeMap=null;
	Map getMap=null;
	Map returnMap=null;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rent_order_confirm);
			super.setTitleBar("订单确认",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			initView();
			registerListener();
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void initView() {
		btn_submit_order=(Button)findViewById(R.id.btn_submit_order);
		ltgetmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltreturnmd=(LabelText)findViewById(R.id.elt_mdmc_return);
		ltgetdate=(LabelText)findViewById(R.id.elt_time);
		ltreturndate=(LabelText)findViewById(R.id.elt_time_return);
		ltgetmodel=(LabelText)findViewById(R.id.elt_clxh);
		ltretunmodel=(LabelText)findViewById(R.id.elt_clxh_return);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		lttotalfee.getValueText().setText("0元");
		
		tvtxt_fee=(TextView)findViewById(R.id.txt_fee);
		
		intent=getIntent();
		HashMap infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
		getMap=(Map)infoMap.get("getMap");
		returnMap=(Map)infoMap.get("returnMap");
		feeMap=(Map)infoMap.get("feeMap");
		
		ltgetmd.getValueText().setText((String)getMap.get("storeName"));
		ltgetdate.getValueText().setText((String)getMap.get("date"));
		ltgetmodel.getValueText().setText((String)getMap.get("carModel"));
		
		ltreturnmd.getValueText().setText((String)returnMap.get("storeName"));
		ltreturndate.getValueText().setText((String)returnMap.get("date"));
		ltretunmodel.getValueText().setText((String)returnMap.get("carModel"));
		
		double bsxf=(Double)feeMap.get("bsxf");
		double sinfee=(Double)feeMap.get("sinfee");
		int  useTime=(Integer)feeMap.get("useTime");
		double  mdsxf=(Double)feeMap.get("mdsxf");
		double totalfee=(Double)feeMap.get("totalfee");
		tvtxt_fee.setText("费用["+bsxf+"元手续费+"+sinfee+"元/h*"+useTime+"h+"+mdsxf+"元异店换车费]");
		lttotalfee.getValueText().setText(totalfee+"");
	}
	
 
	protected void registerListener() {
		btn_submit_order.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				  
			}
		});
		 
	}
	
 
	 
 
	 
}
