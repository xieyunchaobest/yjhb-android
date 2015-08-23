package cn.com.xyc.activity;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class LoginActivity extends BaseActivity {

	private JSONObject reqJson = null;
	private Result response = null;

	private Button btnGetAuthCode = null;
	private EditText etMobileNo=null;
	private Button btnLogin= null;
	private EditText etAutoCode=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login);
			// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			// R.layout.title);
			super.setTitleBar("登录", View.GONE, View.GONE, View.INVISIBLE, false);
			initData();
			initView();
			registerListener();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void initView() {
		btnGetAuthCode = (Button) findViewById(R.id.btn_getautocode);
		etMobileNo=(EditText)findViewById(R.id.login_mobile);
		btnLogin=(Button) findViewById(R.id.btn_ok);
		etAutoCode=(EditText)findViewById(R.id.login_authcode);
		
	}

	public void initData() {
		reqJson = new com.alibaba.fastjson.JSONObject();
	}
	
	private boolean validate(String flag) {
		if(flag.equals("1")) {
			if(etMobileNo.getText().length()!=11) {
				Toast.makeText(LoginActivity.this,"请输入正确的手机号！",
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		if(etAutoCode.getText().length()!=4) {
			Toast.makeText(LoginActivity.this,"请输入正确验证码！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	

	protected void registerListener() {
		btnGetAuthCode.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean vd=validate("");
				if(vd==false)return;
				
				reqJson.put("mobileNo", etMobileNo.getText());
				getAuthCode();
			}
		});
		
		
	
		
		btnLogin.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean vd=validate("1");
				if(vd==false)return;
				reqJson.put("mobileNo", etMobileNo.getText());
				reqJson.put("authCode", etAutoCode.getText());
				login();
			}
		});

	}

	
 
	
	public void login() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_LOGIN,
								reqJson.toJSONString());
						if (handleError(response) == true)
							return;
						Message m = new Message();
						m.what = 2;
						handler.sendMessage(m);
					}
				});
		mThread.start();
	}
	
	private void redrawUI() {

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				redrawUI();
			}
			case 2: {
				saveLocal();
				//ActivityUtil.clearLogin();
				finish();

			}
				if (mProgressDialog != null)
					mProgressDialog.dismiss();// 当接到消息时，关闭进度条
			}
		}
	};
	
	public void saveLocal() {
		CacheProcess cache=new CacheProcess();
		try {
			cache.save(this,Constant.LOCAL_STORE_KEY_USER, (JSONObject) JSON.toJSON(response.result) );
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

 
	public void getAuthCode() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_GET_AUTO_CODE,
								reqJson.toJSONString());
						if (handleError(response) == true)
							return;
						Message m = new Message();
						m.what = 1;
						handler.sendMessage(m);
					}
				});
		mThread.start();
	}

}
