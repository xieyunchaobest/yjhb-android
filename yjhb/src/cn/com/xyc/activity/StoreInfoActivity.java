package cn.com.xyc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;

public class StoreInfoActivity extends BaseActivity {
	private Button btnRentNow=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_info);
		super.setTitleBar("√≈µÍ–≈œ¢",View.GONE,View.GONE,View.INVISIBLE,false);
		
		initView();
		registerListener();
		
	}
	
	
	private void  initView() {
		btnRentNow=(Button)findViewById(R.id.btn_rentnow);
	}
			
	protected void registerListener() {
		btnRentNow.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(StoreInfoActivity.this, RentActivity.class));
			}
		});
		

	}
 
}
