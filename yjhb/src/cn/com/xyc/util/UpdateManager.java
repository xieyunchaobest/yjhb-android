package cn.com.xyc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.com.xyc.R;
import cn.com.xyc.activity.BaseActivity;
import cn.com.xyc.vo.MosVersionSVO;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* SD卡不存在，下载失败*/
	private static final int DOWNLOAD_FAULT = 3;
	/* 网络连接失败，下载失败*/
	private static final int DOWNLOAD_CONNECT_FAIL = 4;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private BaseActivity mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	String mosVersionUrl =Constant.METHOD_CHECK_VERSION;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			case DOWNLOAD_FAULT:
				Toast.makeText(mContext, "您的SD卡已拔出，不能下载更新！",
						Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_CONNECT_FAIL:
				Toast.makeText(mContext, "网络出错，下载失败！",
						Toast.LENGTH_SHORT).show();
		        break;
			default:
				break;
			}
		};
	};

	public UpdateManager(BaseActivity context)
	{
		this.mContext = context;
	}

	public MosVersionSVO getLateVersion() {
		MosVersionSVO mosVersion = null;
        
		Result MosVersionJsonRes;
		com.alibaba.fastjson.JSONObject v=new com.alibaba.fastjson.JSONObject();
		v.put("pfType", "A");
		MosVersionJsonRes = mContext.getPostHttpContent("", mosVersionUrl,
				com.alibaba.fastjson.JSONObject.toJSONString(v));
		//com.alibaba.fastjson.JSONObject contentJson = com.alibaba.fastjson.JSONObject.parseObject(MosVersionJsonRes);
 
		mosVersion = com.alibaba.fastjson.JSON.parseObject(
				com.alibaba.fastjson.JSONObject.toJSONString(MosVersionJsonRes.result), MosVersionSVO.class);
		
		return mosVersion;
		
	}
	public MosVersionSVO mosVersionSVO;
	/**
	 * 检测软件更新
	 */
	public void checkUpdate()
	{
		if (isUpdate())
		{
			// 显示提示对话框
			showNoticeDialog();
		} else
		{
			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	public boolean isUpdate()
	{
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		mosVersionSVO = getLateVersion();
		if (null != mosVersionSVO)
		{
			int serviceCode = Integer.valueOf(mosVersionSVO.getVersionNum());
			// 版本判断
			if (serviceCode > versionCode)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("cn.com.xyc", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(mosVersionSVO.getVersionDesc());
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	public void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		builder.setOnKeyListener(keylistener);
		mDownloadDialog = builder.create();
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		// 现在文件
		//downloadApk();
		new downloadApkThread().start();
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
	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "Download";
				//	URL url = new URL("http://10.21.1.34:7001/web/mos.apk");
					URL url = new URL(mosVersionSVO.getPublishPath());
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath,"mos");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}else {
					mHandler.sendEmptyMessage(DOWNLOAD_FAULT);
				}
			} catch (MalformedURLException e)
			{
				mHandler.sendEmptyMessage(DOWNLOAD_CONNECT_FAIL);
				e.printStackTrace();
			} catch (IOException e)
			{
				mHandler.sendEmptyMessage(DOWNLOAD_CONNECT_FAIL);
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath,"mos");
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
