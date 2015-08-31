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

	private ImageButton titleLeftButton;// ��������߰�ť��Ĭ��ͼ��Ϊ���أ�����Ϊ͸��?
	private TextView titleTextView;// ���������б���
	private ImageView titleDownArrow;// �����Ҳ�С��ͷ��Ĭ�ϲ��ɼ�
	private RelativeLayout titleMiddleButton;// �в��ɵ�����򣬰��������Լ�С��ͷ��Ĭ�ϲ��ɵ��
	protected  ImageButton titleRightButton;// �������ұ߰�ť��Ĭ��ͼ��Ϊˢ�£�����Ϊ͸��
	public Dialog mProgressDialog;//������
	public boolean isCancel=false;//�ж��ǰ��˷��ؼ�
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
				R.style.process_dialog);//�����Զ��������
		mProgressDialog.setContentView(R.layout.progress_dialog);//�Զ��������������
		mProgressDialog.setCancelable(true);
		mProgressDialog
				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						isCancel=true;
						if(finishActivity)finish();
					}
				});
		mProgressDialog.show();//��ʾ������
	
	}
	
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_BACK) {

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("ȷ���˳�ϵͳ��")
						.setCancelable(false)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
										ActivityUtil.getInstance().exit();
										System.exit(0);
									}
								})
						.setNegativeButton("ȡ��",
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
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);// ���ñ����������ļ�Ϊtitle_model
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
		 * �����ұ߰�ť�Ƿ�ɼ�?
		 * 
		 * @param visibility
		 *            �ɼ�״̬
		 */
		public void setTitleRightButtonVisibility(int visibility) {
			titleRightButton.setVisibility(visibility);
		}

		/**
		 * ����С��ͷ�Ƿ�ɼ�?
		 * 
		 * @param visibility
		 *            �ɼ�״̬
		 */
		public void setTitleDownArrowVisibility(int visibility) {
			titleDownArrow.setVisibility(visibility);
		}

		/**
		 * ������ఴťͼ��?
		 * 
		 * @param leftButtonImg
		 *            ͼ����Դid
		 */
		public void setTitleLeftButtonImg(int leftButtonImg) {
			titleLeftButton.setImageResource(leftButtonImg);
		}

		/**
		 * �����Ҳఴťͼ��
		 * 
		 * @param rightButtonImg
		 *            ͼ����Դid
		 */
		public void setTitleRightButtonImg(int rightButtonImg) {
			titleRightButton.setImageResource(rightButtonImg);
		}

		/**
		 * ���ñ����ı�
		 * 
		 * @param titleText
		 *            �����ı�
		 */
		public void setTitleText(String titleText) {
			titleTextView.setText(titleText);
			titleTextView.setTextColor(0xFFFFFFFF);
		}

		/**
		 * �����в�����ɵ��״̬
		 * 
		 * @param clickable
		 *            �ɵ��״�?
		 */
		public void setTitleMiddleButtonClickable(boolean clickable) {
			titleMiddleButton.setClickable(clickable);
		}

		/**
		 * ��ఴť����¼����� Ĭ�Ϸ���������
		 */
		public void leftButtonOnClick() {
			titleLeftButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					onBackPressed();
				}
			});
		}

		/**
		 * �в������������¼����� Ĭ���л�����
		 */
		public void middleButtonOnClick() {
			titleMiddleButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent("com.cattsoft.mos.CHANGE_WORK_AREA");// CHANGE_WORK_AREAΪ�Զ���action����Manifest������
					startActivity(intent);
				}
			});
		}

		/**
		 * �Ҳఴť����¼�����?
		 */
		public void rightButtonOnClick() {
			titleRightButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

				}
			});
		}

		/**
		 * ��post��ʽ�ύ����
		 * @param url �������ĵ�ַ��ֱ��дAction�ĵ�ַ����tm/WoHandleAction.do?method=query
		 * @param parameter ƴ�Ӻõ�json�ַ���
		 * @return �����json�ַ��������������˷���AppException��SysExceptionʱ���õ����ַ�����һ��html�ĵ�
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
				// ��ȡ��Ӧ�Ľ��
				HttpResponse response = client.execute(post);
				// ��ȡHttpEntity
				HttpEntity respEntity = response.getEntity();
				// ��ȡ��Ӧ�Ľ����Ϣ
				String resp= EntityUtils.toString(respEntity); 
				res= JSON.parseObject(resp, Result.class);  
				System.out.println("respnoserespnoser===="+resp);
			} catch (Exception e) {
				res.resultCode=0;
				res.result="���������쳣��";
				
			} 
			return res;
		}
		
		
		
		
		public Handler baseHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case -1: {//�����쳣��Ϣ
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
