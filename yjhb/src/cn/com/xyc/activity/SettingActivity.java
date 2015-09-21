package cn.com.xyc.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
import cn.com.xyc.util.Constant;
import cn.com.xyc.util.StringUtil;
import cn.com.xyc.util.UpdateManager;
import cn.com.xyc.view.LabelText;

public class SettingActivity extends BaseActivity {
	
	private LabelText ltlxkf;
	private LabelText elt_cpjs;
	private LabelText ltquestion;
	private LabelText ltdqbb;
	private LabelText ltdqyh;
	
	Dialog dialog=null;
	private Button logoutButton;
	Dialog logoutdialog=null;
	UpdateManager manager = new UpdateManager(SettingActivity.this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		super.setTitleBar("�ҵ�",View.GONE,View.GONE,View.INVISIBLE,false);
		initView();
		registerListener();
	}
	
	
	
	/**
	 * ��ʾ������¶Ի���
	 */
	private void showUpdateDialog() {
		// ����Ի���
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(manager.mosVersionSVO.getVersionDesc());
		// ����
		builder.setPositiveButton(R.string.soft_update_updatebtn,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// ��ʾ���ضԻ���
						manager.showDownloadDialog();
					}
				});
		// �Ժ����
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
				Toast.makeText(getApplicationContext(), "�������°汾��",
						Toast.LENGTH_SHORT).show();
			}
				break;
		
			}
		}
	};
	
	public void startCheckThread() {
		Thread checkThread = new Thread(new Runnable() {// �����µ��̣߳�
			@Override
			public void run() {
				try {
					// ����������
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
		elt_cpjs=(LabelText)findViewById(R.id.elt_cpjs);
		ltdqbb=(LabelText)findViewById(R.id.elt_dqbb);
		ltlxkf.getValueText().setText("01082886982");
		ltdqbb.getValueText().setText("1.0");
		ltquestion=(LabelText)findViewById(R.id.elt_question);
		logoutButton=(Button)findViewById(R.id.btn_logout);
		dialog = new AlertDialog.Builder(this).setIcon(
			     android.R.drawable.btn_star).setTitle("����").setMessage(
			     "�Ƿ�绰��ϵ�ͷ�").setPositiveButton("��",
			     new OnClickListener() {

			      public void onClick(DialogInterface dialog, int which) {
			    	     Intent intent = new Intent(Intent.ACTION_CALL , Uri.parse("tel:" +  "01082886982"));  
			                //��Ӧ�¼�   
			    	     SettingActivity.this.startActivity(intent);  
			      }
			     }).setNegativeButton("��", new OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {
			    	   dialog.dismiss();
			    }
			   }).create();
		logoutdialog = new AlertDialog.Builder(this).setIcon(
			     android.R.drawable.btn_star).setTitle("��ʾ").setMessage(
			     "ȷ���˳���ǰ�û���").setPositiveButton("��",
			     new OnClickListener() {

			      public void onClick(DialogInterface dialog, int which) {
			    	  logout();
			      }
			     }).setNegativeButton("��", new OnClickListener() {

			    public void onClick(DialogInterface dialog, int which) {
			    	   dialog.dismiss();
			    }
			   }).create();
		
		
		ltdqyh=(LabelText)findViewById(R.id.elt_dqyh);
		CacheProcess c=new CacheProcess();
		String cache=c.getCacheValueInSharedPreferences(this, Constant.LOCAL_STORE_KEY_USER);
	 
		JSONObject user = JSON.parseObject(cache);  
		if(user!=null && user.containsKey("mobileNo") && !(StringUtil.isBlank(user.getString("mobileNo")))) {
			ltdqyh.getValueText().setText(user.getString("mobileNo"));
		}
		
		
		
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
		
	 
		
		ltdqbb.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startCheckThread();
			}
		});
		
		elt_cpjs.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent();
				 intent.setClass(SettingActivity.this, ProdDescActivity.class); 
				startActivity(intent);	
			}
		});
		
		ltquestion.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent();
				 intent.setClass(SettingActivity.this, QuestionActivity.class); 
				startActivity(intent);	
			}
		});
		
	}		 

		 
	
}
