package cn.com.xyc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.com.xyc.R;

public class RentActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rent);
		super.setTitleBar("×â³µ",View.GONE,View.GONE,View.INVISIBLE,false);
	}
}
