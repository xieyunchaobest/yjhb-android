package cn.com.xyc.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.util.CacheProcess;
import cn.com.xyc.util.UpdateManager;
import cn.com.xyc.view.LabelText;
import cn.com.xyc.wxapi.WXEntryActivity;

public class SettingActivity extends BaseActivity {
	
	private LabelText ltlxkf;
	private LabelText ltwxfx;
	private LabelText ltquestion;
	private LabelText ltdqbb;
	Dialog dialog=null;
	private Button logoutButton;
	Dialog logoutdialog=null;
	UpdateManager manager = new UpdateManager(SettingActivity.this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		super.setTitleBar("我的",View.GONE,View.GONE,View.INVISIBLE,false);
		initView();
		registerListener();
	}
	
	
	
	/**
	 * 显示软件更新对话框
	 */
	private void showUpdateDialog() {
		// 构造对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(manager.mosVersionSVO.getVersionDesc());
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 显示下载对话框
						manager.showDownloadDialog();
					}
				});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
							dialog.dismiss();
					}
				});
		builder.setOnKeyListener(keylistener);
		Dialog noticeDialog = builder.create();
		noticeDialog.setCanceledOnTouchOutside(false);
		noticeDialog.show();
	}
	
	
	OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            {
             return true;
            }
            else
            {
             return false;
            }
        }
    } ;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
		  
			case 3: {
				showUpdateDialog();
				break;
			}
			case 4:{
				Toast.makeText(getApplicationContext(), "已是最新版本！",
						Toast.LENGTH_SHORT).show();
			}
				break;
		
			}
		}
	};
	
	public void startCheckThread() {
		Thread checkThread = new Thread(new Runnable() {// 启动新的线程，
			@Override
			public void run() {
				try {
					// 检查软件更新
					if (manager.isUpdate()) {
						Message msg= new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					}else {
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				 
			}
		});
		checkThread.start();
	}
	
	private void initView() {
		ltlxkf=(LabelText)findViewById(R.id.elt_lxkf);
		ltwxfx=(LabelText)findViewById(R.id.elt_wxfx);
		ltdqbb=(LabelText)findViewById(R.id.elt_dqbb);
		ltlxkf.getValueText().setText("10086");
		ltdqbb.getValueText().setText("1.0");
		ltquestion=(LabelText)findViewById(R.id.elt_question);
		logoutButton=(Button)findViewById(R.id.btn_logout);
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
		logoutdialog = new AlertDialog.Builder(this).setIcon(
			     android.R.drawable.btn_star).setTitle("提示").setMessage(
			     "确定退出当前用户？").setPositiveButton("是",
			     new OnClickListener() {

			      public void onClick(DialogInterface dialog, int which) {
			    	  logout();
			      }
			     }).setNegativeButton("否", new OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {
			    	   dialog.dismiss();
			    }
			   }).create();
	}
	
	
	private void logout() {
		 ((MainActivity)this.getParent()).logout();
	}
	
	protected void registerListener() {
		ltlxkf.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		logoutButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutdialog.show();
			}
		});
		
		ltquestion.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this,WXEntryActivity.class));
			}
		});
		
		ltdqbb.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startCheckThread();
			}
		});
	}		 

		 
	
}
