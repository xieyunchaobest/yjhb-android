package cn.com.xyc.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.alipay.sdk.app.PayTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
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
import cn.com.xyc.view.datepicker.DatePicker;
import cn.com.xyc.view.datepicker.DatePicker.DateTimeSetListener;
import cn.com.xyc.vo.PayResult;

public class BuyConfirmActivity extends BaseActivity {
	
	private LabelText ltmd;//取车门店
	private LabelText ltdate;
	private LabelText ltmodel;
	private LabelText lttotalfee;
	private Button btnOk; 
	String outTradeNo="";
	com.alibaba.fastjson.JSONObject reqJson=null;
	Result response=null;
	HashMap infoMap=null;
	
	// 商户PID
		public static final String PARTNER = Constant.PARTNER;
		// 商户收款账号
		public static final String SELLER = Constant.SELLER;
		// 商户私钥，pkcs8格式
		public static final String RSA_PRIVATE = Constant.RSA_PRIVATE;
		// 支付宝公钥
		public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

		
		private static final int SDK_PAY_FLAG = 1;
		private static final int SDK_CHECK_FLAG = 2;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.buyconfirm);
			//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
			super.setTitleBar("订单确认",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			initView();
			registerListener();
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
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Constant.NotifyURL
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		//orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}
	
	public void pay() {
		System.out.println("开始支付！");
		// 订单
		String orderInfo = getOrderInfo("游捷滑板", "游捷滑板", ((String)infoMap.get("fee")).replaceAll("元", ""));

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(BuyConfirmActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
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
					Toast.makeText(BuyConfirmActivity.this, "创建订单失败！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					BuyConfirmActivity.this.finish();
					Toast.makeText(BuyConfirmActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(BuyConfirmActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(BuyConfirmActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(BuyConfirmActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	public void readyParameter() {
		outTradeNo=getOutTradeNo();
		reqJson=new com.alibaba.fastjson.JSONObject();
		reqJson.put("tradeType", "B");
		reqJson.put("out_trade_no", outTradeNo);
		CacheProcess c=new CacheProcess();
		String mobileNo=c.getMobileNo(this);
		reqJson.put("mobileNo", mobileNo);
		reqJson.put("carId", (Integer)infoMap.get("carId"));
		reqJson.put("storeId", (Integer)infoMap.get("storeId"));
		reqJson.put("totalFee", ((String)infoMap.get("fee")).replaceAll("元", ""));
	}
	
	
	
	public void createOrder() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
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
		btnOk=(Button)findViewById(R.id.btn_buy);
		
		Intent intent=getIntent();
		infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
		ltmd.getValueText().setText((String)infoMap.get("storeName"));
		ltdate.getValueText().setText((String)infoMap.get("date"));
		ltmodel.getValueText().setText((String)infoMap.get("model"));
		lttotalfee.getValueText().setText((String)infoMap.get("fee"));
		
		
	}
	
 
 
}
