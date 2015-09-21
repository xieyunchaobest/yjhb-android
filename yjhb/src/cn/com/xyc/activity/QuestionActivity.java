package cn.com.xyc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class QuestionActivity extends BaseActivity {
 
	Intent intent =null;
	
	TextView tv=null;
	 
	Result response=null;
	JSONObject reqJson=new JSONObject();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.question);
			intent=getIntent();
	 
			super.setTitleBar("常见问题",View.GONE,View.GONE,View.INVISIBLE,false);
			initView();
			super.showProcessDialog(true);
			requestion();
			registerListener();
			ActivityUtil.getInstance().addActivity(this);
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
		 
			case 2: {
				System.out.println("xxxxxx");
				JSONObject j= (JSONObject) JSON.toJSON(response.result);
				String v=j.getString("contents");
				if(v!=null) {
					tv.setText(Html.fromHtml(v));
				}
				
				break;
			}
				
			}
			
			if (mProgressDialog != null)
				mProgressDialog.dismiss();// 当接到消息时，关闭进度条
		}
	};
	
	public void requestion() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_SHOW_QUESTION,
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
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 QuestionActivity.this.finish();
		 }
		 return true;
	 }
	
	
	
	
	public void initView() {
		tv=(TextView)findViewById(R.id.tv_question);
	}
	
	
	 
	
	protected void registerListener() {
		
	}
	
	 
	 
 
}
