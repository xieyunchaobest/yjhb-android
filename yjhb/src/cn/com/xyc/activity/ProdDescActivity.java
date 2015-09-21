package cn.com.xyc.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.view.LabelText;

public class ProdDescActivity extends BaseActivity {
 
	Intent intent =null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.prod_desc);
			intent=getIntent();
	 
			super.setTitleBar("≤˙∆∑ΩÈ…‹",View.GONE,View.GONE,View.INVISIBLE,false);
			initView();
			registerListener();
			ActivityUtil.getInstance().addActivity(this);
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 ProdDescActivity.this.finish();
		 }
		 return true;
	 }
	
	public void initView() {
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("http://116.255.186.54:9090/yjhb/server/desc");
	}
	
	
	 
	
	protected void registerListener() {
	}
	
	 
	 
 
}
