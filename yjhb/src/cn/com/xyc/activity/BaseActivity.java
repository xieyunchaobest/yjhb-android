package cn.com.xyc.activity;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.xyc.R;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_BACK) {

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("��ȷ���˳���")
						.setCancelable(false)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
										System.exit(0);
									}
								})
						.setNegativeButton("����",
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

		
}
