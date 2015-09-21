package cn.com.xyc.wxapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.activity.BaseActivity;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.SignUtils;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.util.Util;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.vo.PayResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	private Button btn_submit_order;
	private LabelText ltgetmd;//ȡ���ŵ�
	private LabelText ltreturnmd;//�ŵ�
	private LabelText ltgetdate;
	private LabelText ltreturndate;
	private LabelText ltgetmodel;
	private LabelText lttotalfee;
	private CheckBox cbShareWechat;
	
	private TextView tvtxt_fee; 
	Intent intent =null;
	
	Map storegetmap=null;
	String getTime="";
	String returnTime="";
	Map getcaremap=null;
	
	Map feeMap=null;
	HashMap getMap=null;
	Map returnMap=null;
	
	HashMap infoMap=new HashMap();
	
	// �̻�PID
	public static final String PARTNER = Constant.PARTNER;
	// �̻��տ��˺�
	public static final String SELLER = Constant.SELLER;
	// �̻�˽Կ��pkcs8��ʽ
	public static final String RSA_PRIVATE = Constant.RSA_PRIVATE;
	// ֧������Կ
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	private IWXAPI api;
	private static final int THUMB_SIZE = 150;
	
	String outTradeNo="";
	
	Result response=null;
	com.alibaba.fastjson.JSONObject reqJson=null;
	
	Integer orderId=null;
	String payDate=null;
	
	Result response1=null;
	com.alibaba.fastjson.JSONObject reqJson1=null;
	String tradeNo=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rent_order_confirm);
			intent=getIntent();
			Bundle b=intent.getExtras();
			orderId=b.getInt("orderId");
			payDate=b.getString("payDate");
			
			System.out.println("orderIdorderId="+orderId);
			btn_submit_order=(Button)findViewById(R.id.btn_submit_order);
			cbShareWechat=(CheckBox)findViewById(R.id.cb_chare_wechat);
			if(!"δ֧��".equals(payDate) && !StringUtil.isBlank(payDate)) {
				btn_submit_order.setVisibility(View.GONE);
				cbShareWechat.setVisibility(View.GONE);
			}
			
			
			if(orderId!=null && orderId.intValue()!=0) {
				super.showProcessDialog(false);
				requestOrderInfo();
			}else {
				infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
				initView();
				registerListener();
			}
			
			
			api = WXAPIFactory.createWXAPI(this, Constant.WE_CHART_ID, false);
	    	api.registerApp(Constant.WE_CHART_ID);   
	        api.handleIntent(getIntent(), this);
			super.setTitleBar("����ȷ��",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			
			
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void requestOrderInfo() {
		Thread mThread = new Thread(new Runnable() {// �����µ��̣߳�
					@Override
					public void run() {
						reqJson1=new JSONObject();
						reqJson1.put("orderId", orderId);
						response1 = getPostHttpContent("",
								Constant.METHOD_GET_ORDERINFO,
								reqJson1.toJSONString());
						if (handleError(response1) == true)
							return;
						Message m = new Message();
						m.what = 4;
						mHandler.sendMessage(m);
					}
				});
		mThread.start();
	}
	
	public void redvelopData() {
		reqJson1=(JSONObject) JSON.toJSON(response1.result) ;
		Integer carId=reqJson1.getInteger("carId");
		Integer storeId=reqJson1.getInteger("storeId");
		Double totalFee=reqJson1.getDouble("totalFee");
		String payTime=reqJson1.getString("payTime");
		String carModel=reqJson1.getString("carModel");
		String getStoreName=reqJson1.getString("getStoreName");
		String rentTime=reqJson1.getString("rentTime");
		String returnStoreName=reqJson1.getString("returnStoreName");
		String returnTime=reqJson1.getString("returnTime");
		Double sxf=reqJson1.getDouble("sxf");
		Double sinFee=reqJson1.getDouble("sinFee");
		Integer useTime=reqJson1.getInteger("useTime");
		Double ydhcf=reqJson1.getDouble("ydhcf");
		outTradeNo=reqJson1.getString("outTradeNo");
		
		getMap=new HashMap();
		getMap.put("storeName", getStoreName);
		getMap.put("date", rentTime);
		getMap.put("carModel", carModel);
		getMap.put("carId", carId);
		getMap.put("storeId", storeId); 
		getMap.put("getStoreName", getStoreName); 
		
		returnMap=new HashMap();
		returnMap.put("storeName", returnStoreName);
		returnMap.put("date", returnTime);
		
		feeMap=new HashMap();
		feeMap.put("bsxf", sxf);
		feeMap.put("sinfee", sinFee);
		feeMap.put("useTime", useTime);
		feeMap.put("mdsxf", ydhcf);
		feeMap.put("totalfee", totalFee);
		
		infoMap.put("getMap", getMap);
		infoMap.put("returnMap", returnMap);
		infoMap.put("feeMap", feeMap);
		
	}
	
	public void initView() {
		
		
		System.out.println("initViewin=============================");
		ltgetmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltreturnmd=(LabelText)findViewById(R.id.elt_mdmc_return);
		ltgetdate=(LabelText)findViewById(R.id.elt_time);
		ltreturndate=(LabelText)findViewById(R.id.elt_time_return);
		ltgetmodel=(LabelText)findViewById(R.id.elt_clxh);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		lttotalfee.getValueText().setText("0Ԫ");
		
		cbShareWechat.setChecked(false);
		tvtxt_fee=(TextView)findViewById(R.id.txt_fee);
		
		
		getMap=(HashMap)infoMap.get("getMap");
		returnMap=(Map)infoMap.get("returnMap");
		feeMap=(Map)infoMap.get("feeMap");
		
		
		ltgetmd.getValueText().setText((String)getMap.get("storeName"));
		ltgetdate.getValueText().setText((String)getMap.get("date"));
		ltgetmodel.getValueText().setText((String)getMap.get("carModel"));
		
		ltreturnmd.getValueText().setText((String)returnMap.get("storeName"));
		ltreturndate.getValueText().setText((String)returnMap.get("date"));
		
		double bsxf=(Double)feeMap.get("bsxf");
		double sinfee=(Double)feeMap.get("sinfee");
		int  useTime=(Integer)feeMap.get("useTime");
		double  mdsxf=(Double)feeMap.get("mdsxf");
		double totalfee=(Double)feeMap.get("totalfee");
		tvtxt_fee.setText("����["+bsxf+"Ԫ������+"+sinfee+"Ԫ/h*"+useTime+"h+"+mdsxf+"Ԫ��껻����]");
		lttotalfee.getValueText().setText(totalfee+"");
	}
	
 
	protected void registerListener() {
		btn_submit_order.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbShareWechat.isChecked()) {
					weChatShare();
				}else {
					createOrder();
				}
				
			}
		});
		 
	}
	
    
    private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
 
    
	@Override
	protected void onNewIntent(Intent intent) {
		System.out.println();
		super.onNewIntent(intent);
		System.out.println("infoMap================="+infoMap);
		setIntent(intent);
		 
        api.handleIntent(intent, this);
	}

	
	public void weChatShare() {
		
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://s-210283.gotocdn.com:9090/yjhb/server/desc";
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "�㻹��Ϊ���з��������Ա�ݵĻ��峵�ɣ�";
		msg.description = "���峵���ޡ����ۣ�������ĳ������⣡";
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		msg.thumbData = Util.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
		
	}
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 3: {
				
				int code=response.resultCode;
				if(code==1) {
					pay();
				}else {
					Toast.makeText(WXEntryActivity.this, "��������ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case 4:{
				redvelopData();
				initView();
				registerListener();
				break;
			}
			
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					WXEntryActivity.this.finish();
					Toast.makeText(WXEntryActivity.this, "֧���ɹ�",
							Toast.LENGTH_SHORT).show();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(WXEntryActivity.this, "֧�����ȷ����",
								Toast.LENGTH_SHORT).show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Toast.makeText(WXEntryActivity.this, "֧��ʧ��",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(WXEntryActivity.this, "�����Ϊ��" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			} 
			}
			
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
		};
	};
	
	
	@Override
	protected void onPause() {
		super.onPause();
		getMap=(HashMap)infoMap.get("getMap");
		System.out.println(getMap);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		intent=getIntent();
		infoMap=(HashMap)intent.getSerializableExtra("info");
		System.out.println("xxxxxxxxxxxxxxxxxxxx");
	}
	
	/**
	 * call alipay sdk pay. ����SDK֧��
	 * 
	 */
	public void pay() {
		System.out.println("��ʼ֧����");
		// ����
		String orderInfo = getOrderInfo("�νݻ���", "�νݻ���", String.valueOf((Double)(feeMap.get("totalfee"))));

		// �Զ�����RSA ǩ��
		String sign = sign(orderInfo);
		try {
			// �����sign ��URL����
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// �����ķ���֧���������淶�Ķ�����Ϣ
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(WXEntryActivity.this);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * ��ѯ�ն��豸�Ƿ����֧������֤�˻�
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask payTask = new PayTask(WXEntryActivity.this);
				// ���ò�ѯ�ӿڣ���ȡ��ѯ���
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. ��ȡSDK�汾��
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. ����������Ϣ
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// ǩԼ���������ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// ǩԼ����֧�����˺�
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// �̻���վΨһ������
		orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// ��Ʒ����
		orderInfo += "&body=" + "\"" + body + "\"";

		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + Constant.NotifyURL
				+ "\"";

		// ����ӿ����ƣ� �̶�ֵ
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// ֧�����ͣ� �̶�ֵ
		orderInfo += "&payment_type=\"1\"";

		// �������룬 �̶�ֵ
		orderInfo += "&_input_charset=\"utf-8\"";

		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		//orderInfo += "&return_url=\"m.alipay.com\"";

		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		System.out.println("outTradeNo========"+key);
		return key;
	}
	
	
	public void readyParameter() {
		if(StringUtil.isBlank(outTradeNo)) {
			outTradeNo=getOutTradeNo();
		}
		
		reqJson=new com.alibaba.fastjson.JSONObject();
		reqJson.put("tradeType", "R");
		reqJson.put("out_trade_no", outTradeNo);
		CacheProcess c=new CacheProcess();
		String mobileNo=c.getMobileNo(this);
		reqJson.put("mobileNo", mobileNo);
		reqJson.put("carId", (Integer)getMap.get("carId"));
		reqJson.put("storeId", (Integer)getMap.get("storeId"));
		reqJson.put("totalFee", (Double)feeMap.get("totalfee"));
		
		reqJson.put("carModel", (String)getMap.get("carModel"));
		reqJson.put("getStoreName", (String)getMap.get("storeName"));
		reqJson.put("rentTime", (String)getMap.get("date"));
		reqJson.put("returnStoreName", (String)returnMap.get("storeName"));
		reqJson.put("returnTime", (String)returnMap.get("date"));
		
		reqJson.put("sxf",feeMap.get("bsxf"));
		reqJson.put("sinfee",(Double)feeMap.get("sinfee"));
		reqJson.put("useTime",(Integer)feeMap.get("useTime"));
		reqJson.put("ydhcf",(Double)feeMap.get("mdsxf"));
		
		
	}
	
	
	
	public void createOrder() {
		WXEntryActivity.this.showProcessDialog(false);
		Thread mThread = new Thread(new Runnable() {// �����µ��̣߳�
			@Override
			public void run() {
				readyParameter();
				response = getPostHttpContent("",
						Constant.METHOD_CREATE_ORDER,
						reqJson.toJSONString());
				if (handleError(response) == true)
					return;
				Message m = new Message();
				m.what = 3;
				mHandler.sendMessage(m);
			}
		});
mThread.start();
	}
	
	

	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 WXEntryActivity.this.finish();
			 }
			 return true;
		 }
		 
		 

	/**
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}


	@Override
	public void onReq(BaseReq arg0) {
		
	}

	
	

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		System.out.println("eeeeeeeeeeeeeeee");
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			createOrder();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	
	}

	
 
	 
 
	 
}
