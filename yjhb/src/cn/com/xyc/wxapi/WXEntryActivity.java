package cn.com.xyc.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.Util;
import cn.com.xyc.view.LabelText;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button gotoBtn, checkBtn;
	private LabelText lbfx=null;
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        lbfx=(LabelText)findViewById(R.id.elt_wxfx);
        // ͨ��WXAPIFactory��������ȡIWXAPI��ʵ��
    	api = WXAPIFactory.createWXAPI(this, Constant.WE_CHART_ID, false);
    	api.registerApp(Constant.WE_CHART_ID);   
        
        api.handleIntent(getIntent(), this);
        
         regesterListen();
    }

    
    public void validate() {
    	int wxSdkVersion = api.getWXAppSupportAPI();
		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
			Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG).show();
		}
    }
    
    public void regesterListen() {
    	   lbfx.setOnClickListener(new Button.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.car_1);
    				WXImageObject imgObj = new WXImageObject(bmp);
    				
    				WXMediaMessage msg = new WXMediaMessage();
    				msg.mediaObject = imgObj;
    				
    				Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
    				bmp.recycle();
    				msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // ��������ͼ

    				SendMessageToWX.Req req = new SendMessageToWX.Req();
    				req.transaction = buildTransaction("img");
    				req.message = msg;
    				req.scene = SendMessageToWX.Req.WXSceneSession;
    				
    				api.sendReq(req);
    				
    			}
    		});
    	    
    }
    
    private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
 
    
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

 

	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
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


	@Override
	public void onReq(BaseReq arg0) {
		
	}
	
  
}