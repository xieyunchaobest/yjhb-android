package cn.com.xyc.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.ActivityUtil;
import cn.com.xyc.util.SignUtils;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.vo.PayResult;

import com.alipay.sdk.app.PayTask;

public class RentOrderConfirmSubActivity extends BaseActivity {
	private LabelText ltgetmd;//ȡ���ŵ�
	private LabelText ltreturnmd;//�ŵ�
	private LabelText ltgetdate;
	private LabelText ltreturndate;
	private LabelText ltgetmodel;
	private LabelText ltretunmodel;
	private LabelText lttotalfee;
	
	private TextView tvtxt_fee; 
	Intent intent =null;
	
	Map storegetmap=null;
	String getTime="";
	String returnTime="";
	Map getcaremap=null;
	
	Map feeMap=null;
	Map getMap=null;
	Map returnMap=null;
	
	// �̻�PID
	public static final String PARTNER = "2088021385295054";
	// �̻��տ��˺�
	public static final String SELLER = "huanwangkejigongsi@163.com";
	// �̻�˽Կ��pkcs8��ʽ
	public static final String RSA_PRIVATE = "MIICXQIBAAKBgQCqsYsUMU2CWhzO5nBAgCAAyFw3jRUAWhKfREu3OzrwREgk7uHcCY5s0hpOsvUGIyUcIdxsVolohHyRZPtVNJTEW3j7Ak/p5YPmpMDeZ/OIQFcEVGhISfPJh6PDhz6r3YxaKQ3y2BnZjZm+1nU/m9YbUn8LdtJTHwLiukpet0axHQIDAQABAoGAAOSy/KURacg89FxCZCQHhtmFmgjT/k96X3kFCG137n/8/Kx/ZB5sr2ceGiFXpPOUIySOOcbuKyzeVgh4REblLGu4e8LvmaUdX3Akq+8XkSd6fgBCPwoPofJKYytp8iC9e0DdKpqThWioOtGeZDynkJO0/IPETsincOjVmP8h+4ECQQDg3+bsWl+kD3PS2W5N8e54XnilIyc/gJJ4MGgi3Qr9KrZmOXF47JD8LXWfKOQCY5m0uR6OIr+0aJtL3setsYK9AkEAwlHQmctPgaDRONG8Z//sl7i/Xnqk6poFHKLHwxUYhxw7woRL04PfMufTZ6eFXGTmK+gXoqkxrB03jWOBmCb94QJBALZ19jEgwymjQB99PPsRqqUQQmP7ugTUlgPfgx+GqzvwRD99rIypppp3aFDUJO2rUzRIYHqDx3jix98vzGUq+yECQQCqUI02DvWLl0lptKKewLg8rufERlh/aylp1N6jhLzvxvY14kCXjfC2LGylYDXKKoF3IEB/CC6KPT1whjOTCTHhAkB1D44y2ZWExWbvF1fKIYktNE6o0D+0LF/uMKdC8lMNUcRI3ivkKrn7NUCfXBrXwExg2m4ZvOMvA6PmM7jIoOs4";
	// ֧������Կ
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqsYsUMU2CWhzO5nBAgCAAyFw3jRUAWhKfREu3OzrwREgk7uHcCY5s0hpOsvUGIyUcIdxsVolohHyRZPtVNJTEW3j7Ak/p5YPmpMDeZ/OIQFcEVGhISfPJh6PDhz6r3YxaKQ3y2BnZjZm+1nU/m9YbUn8LdtJTHwLiukpet0axHQIDAQAB";

	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.rent_order_confirm_sub);
			super.setTitleBar("����ȷ��",View.VISIBLE,View.GONE,View.INVISIBLE,false);
			initView();
			ActivityUtil.getInstance().addActivity(this);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void initView() {
		ltgetmd=(LabelText)findViewById(R.id.elt_mdmc);
		ltreturnmd=(LabelText)findViewById(R.id.elt_mdmc_return);
		ltgetdate=(LabelText)findViewById(R.id.elt_time);
		ltreturndate=(LabelText)findViewById(R.id.elt_time_return);
		ltgetmodel=(LabelText)findViewById(R.id.elt_clxh);
		ltretunmodel=(LabelText)findViewById(R.id.elt_clxh_return);
		lttotalfee=(LabelText)findViewById(R.id.elt_clxh_fee);
		lttotalfee.getValueText().setText("0Ԫ");
		
		tvtxt_fee=(TextView)findViewById(R.id.txt_fee);
		
		intent=getIntent();
		HashMap infoMap=(HashMap)intent.getSerializableExtra("info");//from page StoreInfo
		getMap=(Map)infoMap.get("getMap");
		returnMap=(Map)infoMap.get("returnMap");
		feeMap=(Map)infoMap.get("feeMap");
		
		ltgetmd.getValueText().setText((String)getMap.get("storeName"));
		ltgetdate.getValueText().setText((String)getMap.get("date"));
		ltgetmodel.getValueText().setText((String)getMap.get("carModel"));
		
		ltreturnmd.getValueText().setText((String)returnMap.get("storeName"));
		ltreturndate.getValueText().setText((String)returnMap.get("date"));
		ltretunmodel.getValueText().setText((String)returnMap.get("carModel"));
		
		double bsxf=(Double)feeMap.get("bsxf");
		double sinfee=(Double)feeMap.get("sinfee");
		int  useTime=(Integer)feeMap.get("useTime");
		double  mdsxf=(Double)feeMap.get("mdsxf");
		double totalfee=(Double)feeMap.get("totalfee");
		tvtxt_fee.setText("����["+bsxf+"Ԫ������+"+sinfee+"Ԫ/h*"+useTime+"h+"+mdsxf+"Ԫ��껻����]");
		lttotalfee.getValueText().setText(totalfee+"");
	}
	
 
 
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				
				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(RentOrderConfirmSubActivity.this, "֧���ɹ�",
							Toast.LENGTH_SHORT).show();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(RentOrderConfirmSubActivity.this, "֧�����ȷ����",
								Toast.LENGTH_SHORT).show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Toast.makeText(RentOrderConfirmSubActivity.this, "֧��ʧ��",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(RentOrderConfirmSubActivity.this, "�����Ϊ��" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	
	/**
	 * call alipay sdk pay. ����SDK֧��
	 * 
	 */
	public void pay(View v) {
		// ����
		String orderInfo = getOrderInfo("���Ե���Ʒ", "�ò�����Ʒ����ϸ����", "0.01");

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
				PayTask alipay = new PayTask(RentOrderConfirmSubActivity.this);
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
				PayTask payTask = new PayTask(RentOrderConfirmSubActivity.this);
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
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// ��Ʒ����
		orderInfo += "&body=" + "\"" + body + "\"";

		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
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
		orderInfo += "&return_url=\"m.alipay.com\"";

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
		return key;
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

	
 
	 
 
	 
}
