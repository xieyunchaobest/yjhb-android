package cn.com.xyc.wxapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import cn.com.xyc.activity.SettingActivity;
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
	private LabelText ltgetmd;//取车门店
	private LabelText ltreturnmd;//门店
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
	
	Dialog dialog=null;
	
	HashMap infoMap=new HashMap();
	
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
	
	double bsxf=0d;
	double syf=0d;
	double  mdsxf=0d;
	
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
			if(!"未支付".equals(payDate) && !StringUtil.isBlank(payDate)) {
				btn_submit_order.setVisibility(View.GONE);
				cbShareWechat.setVisibility(View.GONE);
			}
			
			
			if(orderId!=null && orderId.intValue()!=0) {
				super.showProcessDialog(false);
				requestOrderInfo();
				cbShareWechat.setVisibility(View.GONE);
			}else {
				infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
				initView();
				registerListener();
			}
			
			
			api = WXAPIFactory.createWXAPI(this, Constant.WE_CHART_ID, false);
	    	api.registerApp(Constant.WE_CHART_ID);   
	        api.handleIntent(getIntent(), this);
			super.setTitleBar("订单确认",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			
			
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void requestOrderInfo() {
		Thread mThread = new Thread(new Runnable() {// 启动新的线程，
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
		Double syf1=reqJson1.getDouble("syf");
		String payTime=reqJson1.getString("payTime");
		String carModel=reqJson1.getString("carModel");
		String getStoreName=reqJson1.getString("getStoreName");
		String rentTime=reqJson1.getString("rentTime");
		String returnStoreName=reqJson1.getString("returnStoreName");
		String returnTime=reqJson1.getString("returnTime");
		Double sxf1=reqJson1.getDouble("sxf");
		Double sinFee=reqJson1.getDouble("sinFee");
		Integer useTime=reqJson1.getInteger("useTime");
		Double ydhcf1=reqJson1.getDouble("ydhcf");
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
		feeMap.put("bsxf", sxf1);
		feeMap.put("sinfee", sinFee);
		feeMap.put("useTime", useTime);
		feeMap.put("mdsxf", ydhcf1);
		feeMap.put("totalfee", totalFee);
		feeMap.put("syf", syf1);
		
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
		lttotalfee.getValueText().setText("0元");
		
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
		
		bsxf=(Double)feeMap.get("bsxf");
		double sinfee=(Double)feeMap.get("sinfee");
		int  useTime=(Integer)feeMap.get("useTime");
		 mdsxf=(Double)feeMap.get("mdsxf");
		syf=(Double)feeMap.get("syf");
		double totalfee=(Double)feeMap.get("totalfee");
		tvtxt_fee.setText("费用["+bsxf+"元手续费+"+syf+"元使用费+"+mdsxf+"元异店换车费]");
		lttotalfee.getValueText().setText(totalfee+"");
		
		dialog=new AlertDialog.Builder(WXEntryActivity.this).setIcon(
			     android.R.drawable.btn_star).setTitle("提示").setMessage(
			     "尊敬的客户，到店时请携带本人身份证及信用卡（1000人民币额度）作为押金。").setPositiveButton("确定",
			     new OnClickListener() {

			      public void onClick(DialogInterface dialog, int which) {
			    	  if(cbShareWechat.isChecked()) {
							weChatShare();
						}else {
							createOrder();
						}
			      }
			     }).setNegativeButton("取消", new OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {
			    	   dialog.dismiss();
			    }
			   }).create();
		
	}
	
 
	protected void registerListener() {
		btn_submit_order.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
//				 if(cbShareWechat.isChecked()) {
//						weChatShare();
//					}else {
//						createOrder();
//					}
			}
		});
		
		cbShareWechat.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cbShareWechat.isChecked()) {
					System.out.println("已选中！");
					bsxf=(Double)feeMap.get("bsxf");
					tvtxt_fee.setText("费用["+0+"元手续费+"+syf+"元使用费+"+mdsxf+"元异店换车费]");
					double totalfee=(Double)feeMap.get("totalfee");
					lttotalfee.getValueText().setText((totalfee-bsxf)+"");
				} else {
					double totalfee=(Double)feeMap.get("totalfee");
					bsxf=(Double)feeMap.get("bsxf");
					tvtxt_fee.setText("费用["+bsxf+"元手续费+"+syf+"元使用费+"+mdsxf+"元异店换车费]");
					lttotalfee.getValueText().setText(totalfee+"");
					System.out.println("没选中！");
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
		webpage.webpageUrl = "http://116.255.186.54:9090/yjhb/server/desc";
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "你还在为出行发愁吗？试试便捷的滑板车吧！";
		msg.description = "滑板车租赁、销售，解决您的出行问题！";
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
					Toast.makeText(WXEntryActivity.this, "创建订单失败！",
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
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					WXEntryActivity.this.finish();
					Toast.makeText(WXEntryActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(WXEntryActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(WXEntryActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(WXEntryActivity.this, "检查结果为：" + msg.obj,
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
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {
		System.out.println("开始支付！");
		// 订单
		double totalfee=(Double)(feeMap.get("totalfee"));
		if (cbShareWechat.isChecked()) {
			bsxf=(Double)feeMap.get("bsxf");
			totalfee=totalfee-bsxf;
		}
			
		String orderInfo = getOrderInfo("游捷用车", "游捷用车", String.valueOf(totalfee));

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
				PayTask alipay = new PayTask(WXEntryActivity.this);
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

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(WXEntryActivity.this);
				// 调用查询接口，获取查询结果
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
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
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
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
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
		
	 
		
		
		reqJson.put("carModel", (String)getMap.get("carModel"));
		reqJson.put("getStoreName", (String)getMap.get("storeName"));
		reqJson.put("rentTime", (String)getMap.get("date"));
		reqJson.put("returnStoreName", (String)returnMap.get("storeName"));
		reqJson.put("returnTime", (String)returnMap.get("date"));
		Double syf=(Double)feeMap.get("bsxf");
		Double totalFee=(Double)feeMap.get("totalfee");
		reqJson.put("totalFee", (Double)feeMap.get("totalfee"));
		reqJson.put("sxf",syf);
		
		if (cbShareWechat.isChecked()) {
			totalFee=totalFee-bsxf;
			reqJson.put("totalFee",totalFee);
		}
		
		reqJson.put("sinfee",(Double)feeMap.get("sinfee"));
		reqJson.put("useTime",(Integer)feeMap.get("useTime"));
		reqJson.put("ydhcf",(Double)feeMap.get("mdsxf"));
		reqJson.put("syf",(Double)feeMap.get("syf"));
		
		
	}
	
	
	
	public void createOrder() {
		WXEntryActivity.this.showProcessDialog(false);
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
	
	

	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 WXEntryActivity.this.finish();
			 }
			 return true;
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

	/**
	 * get the sign type we use. 获取签名方式
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
