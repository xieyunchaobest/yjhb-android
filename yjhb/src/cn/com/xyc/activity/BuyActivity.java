package cn.com.xyc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.xyc.R;
import cn.com.xyc.view.MyListView;

public class BuyActivity extends BaseActivity {

	private MyListView myListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		super.setTitleBar("¹º³µ",View.GONE,View.GONE,View.INVISIBLE,false);
   
	}
 
}
