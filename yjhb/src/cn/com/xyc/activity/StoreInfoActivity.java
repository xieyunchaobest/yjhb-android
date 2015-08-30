package cn.com.xyc.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.view.LabelText;

public class StoreInfoActivity extends BaseActivity {
	private Button btnRentNow=null;
	private LabelText ltelt_mdmc;
	private LabelText elt_mdwz;
	private LabelText elt_cclx;
	Intent intent=null;
	Map store=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		intent=getIntent();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_info);
		super.setTitleBar("√≈µÍ–≈œ¢",View.VISIBLE,View.GONE,View.INVISIBLE,false);
		
		initView();
		registerListener();
		ActivityUtil.getInstance().addActivity(this);
	}
	
	
	private void  initView() {
		store=(Map)intent.getSerializableExtra("storeData");
		
		btnRentNow=(Button)findViewById(R.id.btn_rentnow);
		ltelt_mdmc=(LabelText)findViewById(R.id.elt_mdmc);
		elt_mdwz=(LabelText)findViewById(R.id.elt_mdwz);
		elt_cclx=(LabelText)findViewById(R.id.elt_cclx);
		
		
		
		ltelt_mdmc.getValueText().setText((String)store.get("item_name"));
		elt_mdwz.getValueText().setText((String)store.get("item_address"));
		elt_cclx.getValueText().setText((String)store.get("item_busroute"));
	}
			
	protected void registerListener() {
		btnRentNow.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				
				bundle.putSerializable("storeData", (HashMap)store);
				Intent i=new Intent();
				i.putExtras(bundle);
				i.setClass(StoreInfoActivity.this, RentActivity.class);
				startActivity(i); 
			}
		});
		

	}
 
}
