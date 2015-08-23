package cn.com.xyc;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class YjhbApp extends Application {

	
	private HttpClient httpClient;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		httpClient = createHttpClient();
		// ��ʹ�� SDK �����֮ǰ��ʼ�� context ��Ϣ������ ApplicationContext
		SDKInitializer.initialize(this);
	}
	
	
	/**
	 * �ⲿ�ӿڻ��HttpClient�ķ���
	 * 
	 * @return
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	// ����HttpClient����
		private HttpClient createHttpClient() {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 500000);
			HttpConnectionParams.setSoTimeout(params, 1800000);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
//			schReg.register(new Scheme("https",
//					SSLSocketFactory.getSocketFactory(), 443));  
			//ClientConnectionManager connManager = new ThreadSafeClientConnManager(params, schReg); 

			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			

			return new DefaultHttpClient(conMgr, params);
		}

	


}