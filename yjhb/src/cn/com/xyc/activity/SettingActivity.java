package cn.com.xyc.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import cn.com.xyc.R;
import cn.com.xyc.view.LabelText;

public class SettingActivity extends BaseActivity {
	
	private LabelText ltlxkf;
	private LabelText ltwxfx;
	private LabelText ltdqbb;
	Dialog dialog=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		super.setTitleBar("我的",View.GONE,View.GONE,View.INVISIBLE,false);
		initView();
		registerListener();
	}
	
	
	private void initView() {
		ltlxkf=(LabelText)findViewById(R.id.elt_lxkf);
		ltwxfx=(LabelText)findViewById(R.id.elt_wxfx);
		ltdqbb=(LabelText)findViewById(R.id.elt_dqbb);
		ltlxkf.getValueText().setText("10086");
		ltdqbb.getValueText().setText("1.0");
		
		dialog = new AlertDialog.Builder(this).setIcon(
			     android.R.drawable.btn_star).setTitle("呼叫").setMessage(
			     "是否电话联系客服10086").setPositiveButton("是",
			     new OnClickListener() {

			      public void onClick(DialogInterface dialog, int which) {
			    	     Intent intent = new Intent(Intent.ACTION_CALL , Uri.parse("tel:" +  10086));  
			                //相应事件   
			    	     SettingActivity.this.startActivity(intent);  
			      }
			     }).setNegativeButton("否", new OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {
			    	   dialog.dismiss();
			    }
			   }).create();

	}
	
	
	protected void registerListener() {
		ltlxkf.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
	}		 

		 
	
}
