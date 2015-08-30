package cn.com.xyc.activity;



import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.YjhbApp;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.CrashHandler;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	private TextView main_tab_new_message;
	
	Result response=null;
	private JSONObject reqJson = null;
	Result res=null;
	
	RadioGroup radioGroup=null;
	RadioButton main_tab_settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try {
    		  super.onCreate(savedInstanceState);
    	        
    	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	        setContentView(R.layout.main);
    	        initData();
    	        initView();
    	        regesterListener();
    	        requestCache();
    	        CrashHandler crashHandler = CrashHandler.getInstance();    
    	        crashHandler.init(this);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
      
    }
    
    
   public void  regesterListener(){
	      radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.main_tab_addExam:
						tabHost.setCurrentTabByTag("租车");
						break;
					case R.id.main_tab_myExam:
						tabHost.setCurrentTabByTag("购车");
						break;
					case R.id.main_tab_message:
						tabHost.setCurrentTabByTag("订单");
						break;
					case R.id.main_tab_settings:
						tabHost.setCurrentTabByTag("我的");
						break;
					default:
						tabHost.setCurrentTabByTag("租车");
						break;
					}
				}
			});
    }
    
    public void initData() {
    	reqJson=new JSONObject();
    }
    
    public void requestCache() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
					@Override
					public void run() {
						response = getPostHttpContent("",
								Constant.METHOD_GET_LOCAL_CACHE,
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
    
    public void initView() { 
        
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        main_tab_settings=(RadioButton)findViewById(R.id.main_tab_settings);
        
        intent=new Intent().setClass(this, MapActivity.class);
        spec=tabHost.newTabSpec("游捷滑板").setIndicator("游捷滑板").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, RentActivity.class);
        spec=tabHost.newTabSpec("租车").setIndicator("租车").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,BuyActivity.class);
        spec=tabHost.newTabSpec("购车").setIndicator("购车").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, OrderListActivity.class);
        spec=tabHost.newTabSpec("订单").setIndicator("订单").setContent(intent);
        tabHost.addTab(spec);
        
     
        intent=new Intent().setClass(this, SettingActivity.class);
        spec=tabHost.newTabSpec("我的").setIndicator("我的").setContent(intent);
        tabHost.addTab(spec);
        
         radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        
    }
    
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				initCache();
			}
			case 2: {
				//finish();
			} 
			}
		}
	};
	
    public void logout() {
    	tabHost.setCurrentTabByTag("游捷滑板");
    	main_tab_settings.setChecked(false);
    	tabHost.setCurrentTabByTag("游捷滑板");
    	CacheProcess c=new CacheProcess();
    	try {
			c.save(this,Constant.LOCAL_STORE_KEY_USER, new com.alibaba.fastjson.JSONObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    
	protected void redrawComponent(Message msg) {

	}
	
	 
	protected void dealWithException() {
		
	}
	
	public Handler baseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1: {//处理异常信息
				Toast.makeText(getApplicationContext(), "数据初始化异常！",
						Toast.LENGTH_SHORT).show();
				dealWithException();
				break;
			}
			case 100: {
				redrawComponent(msg);
				break;
			}
			}
		}
	};
    protected boolean handleError(Result res) {
		if (res.resultCode==0) { 
			Message errMsg=new Message();
			errMsg.what=-1;
			baseHandler.sendMessage(errMsg);
			return true;
		}
		return false;
	}
    
    /**
     * get cars
     */
    public void initCache() {
    	CacheProcess c =new CacheProcess();
    	try {
			c.save(this, Constant.LOCAL_STORE_CACHES, (JSONObject) JSON.toJSON(response.result) );
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    

	/**
	 * 以post方式提交数据
	 * @param url 服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter 拼接好的json字符串
	 * @return 服务端json字符串，当服务器端返回AppException或SysException时，得到的字符串是一个html文档
	 * @throws SysException 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Result getPostHttpContent(String serverName,String method,String parameter) {
		YjhbApp  app= (YjhbApp)this.getApplication();   
		HttpClient client = app.getHttpClient(); 
		String serverUrl="";
		 res=new Result();
		if(!StringUtil.isBlank(serverName)) {
			serverUrl=serverName;
		}else {
			serverUrl=Constant.ServerURL+method;
		}
		
		System.out.println("parameterparameter="+parameter);
		HttpPost post = new HttpPost(serverUrl);
		post.addHeader("Content-Type", "application/json");
		com.alibaba.fastjson.JSONObject reqJson=com.alibaba.fastjson.JSONObject.parseObject(parameter);
		String cacheInfo=new CacheProcess().getCacheValueInSharedPreferences(this, "cacheInfo");
		System.out.println("cacheInfocacheInfo="+cacheInfo);
		if(!StringUtil.isBlank(cacheInfo)) {
			com.alibaba.fastjson.JSONObject j=com.alibaba.fastjson.JSONObject.parseObject(cacheInfo);
			com.alibaba.fastjson.JSONObject userCache=j.getJSONObject("suveJsonObject");
			com.alibaba.fastjson.JSONObject sysUser=userCache.getJSONObject("sysUserSVO");
			reqJson.put("sysUserId", sysUser.getString("sysUserId"));
			reqJson.put("loginType", "A");
		}else {
			reqJson.put("staffId", "");
			reqJson.put("loginType", "A");
		}
		
		try {
			StringEntity resEntity = new StringEntity(reqJson.toJSONString(), "UTF-8");
			post.setEntity(resEntity);
			// 获取响应的结果
			HttpResponse response = client.execute(post);
			// 获取HttpEntity
			HttpEntity respEntity = response.getEntity();
			// 获取响应的结果信息
			String resp= EntityUtils.toString(respEntity); 
			res= JSON.parseObject(resp, Result.class);  
			System.out.println("respnoserespnoser===="+resp);
		} catch (Exception e) {
			res.resultCode=0;
			res.result="网络连接异常！";
			
		} 
		return res;
	}
	
	
   
}