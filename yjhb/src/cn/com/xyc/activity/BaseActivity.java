package cn.com.xyc.activity;
 
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.YjhbApp;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.StringUtil;

import com.alibaba.fastjson.JSON;

public class BaseActivity extends Activity {

	private ImageButton titleLeftButton;// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ß°ï¿½Å¥ï¿½ï¿½Ä¬ï¿½ï¿½Í¼ï¿½ï¿½Îªï¿½ï¿½ï¿½Ø£ï¿½ï¿½ï¿½ï¿½ï¿½ÎªÍ¸ï¿½ï¿?
	private TextView titleTextView;// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ð±ï¿½ï¿½ï¿½
	private ImageView titleDownArrow;// ï¿½ï¿½ï¿½ï¿½ï¿½Ò²ï¿½Ð¡ï¿½ï¿½Í·ï¿½ï¿½Ä¬ï¿½Ï²ï¿½ï¿½É¼ï¿½
	private RelativeLayout titleMiddleButton;// ï¿½Ð²ï¿½ï¿½Éµï¿½ï¿½ï¿½ï¿½ï¿½ò£¬°ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ô¼ï¿½Ð¡ï¿½ï¿½Í·ï¿½ï¿½Ä¬ï¿½Ï²ï¿½ï¿½Éµï¿½ï¿½
	protected  ImageButton titleRightButton;// ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ò±ß°ï¿½Å¥ï¿½ï¿½Ä¬ï¿½ï¿½Í¼ï¿½ï¿½ÎªË¢ï¿½Â£ï¿½ï¿½ï¿½ï¿½ï¿½ÎªÍ¸ï¿½ï¿½
	public Dialog mProgressDialog;//ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
	public boolean isCancel=false;//ï¿½Ð¶ï¿½ï¿½Ç°ï¿½ï¿½Ë·ï¿½ï¿½Ø¼ï¿½
	public String exceptionDesc=null;
	private String encryptUsable;
	private SharedPreferences sharedPreferences;
	TextView  titleWrapText;
	
	private Result res=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	 
	
	
	public void showProcessDialog(final boolean finishActivity) {
		mProgressDialog = new Dialog(this,
				R.style.process_dialog);//´´½¨×Ô¶¨Òå½ø¶ÈÌõ
		mProgressDialog.setContentView(R.layout.progress_dialog);//×Ô¶¨Òå½ø¶ÈÌõµÄÄÚÈÝ
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						isCancel=true;
						if(finishActivity)finish();
					}
				});
		mProgressDialog.show();//ÏÔÊ¾½ø¶ÈÌõ
	
	}
	
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_BACK) {

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("È·¶¨ÍË³öÏµÍ³Âð£¿")
						.setCancelable(false)
						.setPositiveButton("È·¶¨",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										ActivityUtil.getInstance().exit();
										finish();
										System.exit(0);
									}
								})
						.setNegativeButton("È¡Ïû",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}

			return super.onKeyDown(keyCode, event);
		}
	 
	 
		public void setTitleBar(String titleText, int leftButtonVisibility,
				int rightButtonVisibility, int downArrowVisibility,
				boolean middleButtonClickable) {
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);// ï¿½ï¿½ï¿½Ã±ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ä¼ï¿½Îªtitle_model
			titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
			titleTextView = (TextView) findViewById(R.id.titlebar_text);
			titleDownArrow = (ImageView) findViewById(R.id.titlebar_down_arrow);
			titleMiddleButton = (RelativeLayout) findViewById(R.id.titlebar_title);
			titleRightButton = (ImageButton) findViewById(R.id.titlebar_img_btn_right);
			setTitleText(titleText);
			setTitleLeftButtonVisibility(leftButtonVisibility);
			setTitleRightButtonVisibility(rightButtonVisibility);
			setTitleDownArrowVisibility(downArrowVisibility);
			leftButtonOnClick();
			middleButtonOnClick();
			rightButtonOnClick();
			setTitleMiddleButtonClickable(middleButtonClickable);
		} 
		
		
		public void setTitleLeftButtonVisibility(int visibility) {
			titleLeftButton.setVisibility(visibility);
		}

		/**
		 * ï¿½ï¿½ï¿½ï¿½ï¿½Ò±ß°ï¿½Å¥ï¿½Ç·ï¿½É¼ï¿?
		 * 
		 * @param visibility
		 *            ï¿½É¼ï¿½×´Ì¬
		 */
		public void setTitleRightButtonVisibility(int visibility) {
			titleRightButton.setVisibility(visibility);
		}

		/**
		 * ï¿½ï¿½ï¿½ï¿½Ð¡ï¿½ï¿½Í·ï¿½Ç·ï¿½É¼ï¿?
		 * 
		 * @param visibility
		 *            ï¿½É¼ï¿½×´Ì¬
		 */
		public void setTitleDownArrowVisibility(int visibility) {
			titleDownArrow.setVisibility(visibility);
		}

		/**
		 * ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½à°´Å¥Í¼ï¿½ï¿?
		 * 
		 * @param leftButtonImg
		 *            Í¼ï¿½ï¿½ï¿½ï¿½Ô´id
		 */
		public void setTitleLeftButtonImg(int leftButtonImg) {
			titleLeftButton.setImageResource(leftButtonImg);
		}

		/**
		 * ï¿½ï¿½ï¿½ï¿½ï¿½Ò²à°´Å¥Í¼ï¿½ï¿½
		 * 
		 * @param rightButtonImg
		 *            Í¼ï¿½ï¿½ï¿½ï¿½Ô´id
		 */
		public void setTitleRightButtonImg(int rightButtonImg) {
			titleRightButton.setImageResource(rightButtonImg);
		}

		/**
		 * ï¿½ï¿½ï¿½Ã±ï¿½ï¿½ï¿½ï¿½Ä±ï¿½
		 * 
		 * @param titleText
		 *            ï¿½ï¿½ï¿½ï¿½ï¿½Ä±ï¿½
		 */
		public void setTitleText(String titleText) {
			titleTextView.setText(titleText);
			titleTextView.setTextColor(0xFFFFFFFF);
		}

		/**
		 * ï¿½ï¿½ï¿½ï¿½ï¿½Ð²ï¿½ï¿½ï¿½ï¿½ï¿½Éµï¿½ï¿½×´Ì¬
		 * 
		 * @param clickable
		 *            ï¿½Éµï¿½ï¿½×´Ì?
		 */
		public void setTitleMiddleButtonClickable(boolean clickable) {
			titleMiddleButton.setClickable(clickable);
		}

		/**
		 * ï¿½ï¿½à°´Å¥ï¿½ï¿½ï¿½ï¿½Â¼ï¿½ï¿½ï¿½ï¿½ï¿½ Ä¬ï¿½Ï·ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
		 */
		public void leftButtonOnClick() {
			titleLeftButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					onBackPressed();
				}
			});
		}

		/**
		 * ï¿½Ð²ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Â¼ï¿½ï¿½ï¿½ï¿½ï¿½ Ä¬ï¿½ï¿½ï¿½Ð»ï¿½ï¿½ï¿½ï¿½ï¿½
		 */
		public void middleButtonOnClick() {
			titleMiddleButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent("com.cattsoft.mos.CHANGE_WORK_AREA");// CHANGE_WORK_AREAÎªï¿½Ô¶ï¿½ï¿½ï¿½actionï¿½ï¿½ï¿½ï¿½Manifestï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
					startActivity(intent);
				}
			});
		}

		/**
		 * ï¿½Ò²à°´Å¥ï¿½ï¿½ï¿½ï¿½Â¼ï¿½ï¿½ï¿½ï¿½ï¿?
		 */
		public void rightButtonOnClick() {
			titleRightButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

				}
			});
		}

		/**
		 * ÒÔpost·½Ê½Ìá½»Êý¾Ý
		 * @param url ·þÎñÆ÷µÄµØÖ·£¬Ö±½ÓÐ´ActionµÄµØÖ·£¬Èçtm/WoHandleAction.do?method=query
		 * @param parameter Æ´½ÓºÃµÄjson×Ö·û´®
		 * @return ·þÎñ¶Ëjson×Ö·û´®£¬µ±·þÎñÆ÷¶Ë·µ»ØAppException»òSysExceptionÊ±£¬µÃµ½µÄ×Ö·û´®ÊÇÒ»¸öhtmlÎÄµµ
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
				// »ñÈ¡ÏìÓ¦µÄ½á¹û
				HttpResponse response = client.execute(post);
				// »ñÈ¡HttpEntity
				HttpEntity respEntity = response.getEntity();
				// »ñÈ¡ÏìÓ¦µÄ½á¹ûÐÅÏ¢
				String resp= EntityUtils.toString(respEntity); 
				res= JSON.parseObject(resp, Result.class);  
				System.out.println("respnoserespnoser===="+resp);
			} catch (Exception e) {
				res.resultCode=0;
				res.result="ÍøÂçÁ¬½ÓÒì³££¡";
				
			} 
			return res;
		}
		
		
		
		
		public Handler baseHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1: {//´¦ÀíÒì³£ÐÅÏ¢
					Toast.makeText(getApplicationContext(), (String)res.result,
							Toast.LENGTH_SHORT).show();
					dealWithException();
					closeProcessDialog();
					break;
				}
				case 100: {
					redrawComponent(msg);
					closeProcessDialog();
					break;
				}
				}
			}
		};
		
		protected void redrawComponent(Message msg) {

		}
		
		
		protected void closeProcessDialog() {
			if(mProgressDialog!=null) {
				mProgressDialog.dismiss();
			}
		}
		
		protected void dealWithException() {
			
		}
		
		
		protected boolean handleError(Result res) {
			if (res.resultCode==0) { 
				Message errMsg=new Message();
				errMsg.what=-1;
				baseHandler.sendMessage(errMsg);
				return true;
			}
			return false;
		}
}
