package cn.com.xyc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.xyc.R;

public class MapActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		super.setTitleBar("”ŒΩ›ª¨∞Â",View.GONE,View.GONE,View.INVISIBLE,false);
	 
		  
	}
	
 
}
