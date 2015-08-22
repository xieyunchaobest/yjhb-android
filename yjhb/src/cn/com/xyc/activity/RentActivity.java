package cn.com.xyc.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;
import cn.com.xyc.view.LabelText;

public class RentActivity extends BaseActivity {
	private Button btnRent;
	private LabelText ltgetmd;//取车门店
	private LabelText ltreturnmd;//门店
	private int STORE_GET_CODE=0;
	private int STORE_RETURN_CODE=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent);
		initView();
		registerListener();
		super.setTitleBar("租车",View.GONE,View.GONE,View.INVISIBLE,false);
	}
	
	
	public void initView() {
		btnRent=(Button)findViewById(R.id.btn_rent);
		ltgetmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltreturnmd=(LabelText)findViewById(R.id.elt_mdmc_return);
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
	}
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if(requestCode==STORE_GET_CODE){
	        	Bundle b=data.getExtras();
	        	Map storemap=(Map)b.getSerializable("store");
	        	ltgetmd.getValueText().setText((String)storemap.get("item_name"));
	        }
	        else if(requestCode==STORE_RETURN_CODE){
	        	Bundle b=data.getExtras();
	        	Map storemap=(Map)b.getSerializable("store");
	        	ltreturnmd.getValueText().setText((String)storemap.get("item_name"));
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }
 
}
