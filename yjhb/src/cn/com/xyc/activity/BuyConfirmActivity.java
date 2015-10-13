package cn.com.xyc.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Result;
import cn.com.xyc.util.SignUtils;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.vo.PayResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;

public class BuyConfirmActivity extends BaseActivity {
	
	private LabelText ltmd;//ȡ���ŵ�
	private LabelText ltdate;
	private LabelText ltmodel;
	private LabelText lttotalfee;
	private Button btnOk; 
	private LabelText  elt_address;
	String outTradeNo="";
	com.alibaba.fastjson.JSONObject reqJson=null;
	Result response=null;
	HashMap infoMap=new HashMap();
	
	JSONObject reqJson1=null;
	Result response1=null;
	
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
		
		Integer orderId=null;
		String payDate=null;
		
		Intent intent=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.buyconfirm);
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
			super.setTitleBar("����ȷ��",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			intent=getIntent();
			Bundle b=intent.getExtras();
			orderId=b.getInt("orderId");
			payDate=b.getString("payDate");
			btnOk=(Button)findViewById(R.id.btn_buy);
			if(!"δ֧��".equals(payDate) && !StringUtil.isBlank(payDate)) {
				btnOk.setVisibility(View.GONE);
			}
			if(orderId!=null && orderId.intValue()!=0) {
				requestOrderInfo();
			}else {
				infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
				initView();
				registerListener();
			}
			
			
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
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
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
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
	
	public void pay() {
		System.out.println("��ʼ֧����");
		// ����
		String orderInfo = getOrderInfo("�νݻ���", "�νݻ���", ((String)infoMap.get("fee")).replaceAll("Ԫ", ""));

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
				PayTask alipay = new PayTask(BuyConfirmActivity.this);
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
	
	

	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 BuyConfirmActivity.this.finish();
			 }
			 return true;
		 }
	 
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mProgressDialog != null)
				mProgressDialog.dismiss();
			switch (msg.what) {
			case 3: {
				
				int code=response.resultCode;
				if(code==1) {
					pay();
				}else {
					Toast.makeText(BuyConfirmActivity.this, "��������ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					BuyConfirmActivity.this.finish();
					Toast.makeText(BuyConfirmActivity.this, "֧���ɹ�",
							Toast.LENGTH_SHORT).show();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(BuyConfirmActivity.this, "֧�����ȷ����",
								Toast.LENGTH_SHORT).show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Toast.makeText(BuyConfirmActivity.this, "֧��ʧ��",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(BuyConfirmActivity.this, "�����Ϊ��" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case 4:{
				redvelopData();
				initView();
				registerListener();
				break;
			}
			default:
				break;
			}
		};
	};
	
	public void redvelopData() {
		reqJson1=(JSONObject) JSON.toJSON(response1.result) ;
		Integer carId=reqJson1.getInteger("carId");
		Integer storeId=reqJson1.getInteger("storeId");
		Double totalFee=reqJson1.getDouble("totalFee");
		String payTime=reqJson1.getString("payTime");
		String carModel=reqJson1.getString("carModel");
		String getStoreName=reqJson1.getString("getStoreName");
		String rentTime=reqJson1.getString("rentTime");
		String address=reqJson1.getString("address");
		outTradeNo=reqJson1.getString("outTradeNo");
		
		infoMap.put("storeName", getStoreName);
		infoMap.put("date", rentTime);
		infoMap.put("model", carModel);
		infoMap.put("fee", String.valueOf(totalFee));
		infoMap.put("address", address);
		 
		
	}

	public void readyParameter() {
		if(StringUtil.isBlank(outTradeNo)) {
			outTradeNo=getOutTradeNo();
		}
		reqJson=new com.alibaba.fastjson.JSONObject();
		reqJson.put("tradeType", "B");
		reqJson.put("out_trade_no", outTradeNo);
		CacheProcess c=new CacheProcess();
		String mobileNo=c.getMobileNo(this);
		reqJson.put("mobileNo", mobileNo);
		reqJson.put("carId", (Integer)infoMap.get("carId"));
		reqJson.put("storeId", (Integer)infoMap.get("storeId"));
		reqJson.put("totalFee", ((String)infoMap.get("fee")).replaceAll("Ԫ", ""));
		reqJson.put("model", (String)infoMap.get("model"));
		reqJson.put("carModel", (String)infoMap.get("model"));
		reqJson.put("rentTime", (String)infoMap.get("date"));
		reqJson.put("getStoreName", (String)infoMap.get("storeName"));
		reqJson.put("address", (String)infoMap.get("address"));
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
	
	
	public void createOrder() {
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
	
	
	protected void registerListener() {
		btnOk.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				BuyConfirmActivity.this.showProcessDialog(false);
				createOrder();
			}
		});
		 
	}
	
	
	public void initView() {
		ltmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltdate=(LabelText)findViewById(R.id.elt_time);
		ltmodel=(LabelText)findViewById(R.id.elt_clxh);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		elt_address=(LabelText)findViewById(R.id.elt_address);
		
		ltmd.getValueText().setText((String)infoMap.get("storeName"));
		ltdate.getValueText().setText((String)infoMap.get("date"));
		ltmodel.getValueText().setText((String)infoMap.get("model"));
		lttotalfee.getValueText().setText((String)infoMap.get("fee"));
		elt_address.getValueText().setText((String)infoMap.get("address"));
		
		
	}
	
 
 
}
