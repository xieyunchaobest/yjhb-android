package cn.com.xyc.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import cn.com.xyc.R;

public class WelcomeActivity extends BaseActivity {

	public static final String LOGIN = "com.cattsoft.mos.LOGIN";

	private String isFirstRun = "YES";
	private SharedPreferences sharedPreferences;
	
	private String location=""; 
	
	
	
	Map hm=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
//		loadGuideInfo();
//		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//		initData();

		Start();
	} 
	public void initData() {
		hm = new HashMap();
	 
		
		
	}

 

	public void Start() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
					Message m=new Message();
					m.what=1;
					handlermain.sendMessage(m);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	private Handler handlermain = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				startActivity(new Intent(WelcomeActivity.this,
						MainActivity.class));
				finish();
			}
			}
			
		}
	};
	 
	 
}
