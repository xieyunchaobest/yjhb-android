package cn.com.xyc;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class YjhbApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		SDKInitializer.initialize(this);
	}

}