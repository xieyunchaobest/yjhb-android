package cn.com.xyc.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import cn.com.xyc.YjhbApp;

public class CacheProcess{
	
 
	public void initCache(YjhbApp mosApp,JSONObject jsonObject) {
		
	}


	
	public void clearCache(YjhbApp mosApp) {
		//mosApp.clearAll();
	}
	
	
	/**
	 * add by xyc 20130521 缓存在mosApp中的数据可能由于Android操作系统的某种机制被回收，经常�?�成系统获取缓存对象为空，因此在获得服务器端传来的数据后，放到本地文件中
	 * @param context
	 * @param resJson 
	 * @throws JSONException 
	 */
	public void save(Context context,String key,com.alibaba.fastjson.JSONObject jsonObject) throws JSONException{
		SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		
		String cacheStr=jsonObject.toString();
		edit.putString(key, cacheStr);
		edit.commit();
	}
 
 
	
 
	/**
	 * 根据键获取缓存在SharedPreference的�??
	 * @param context
	 * @param key
	 */
	public String getCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String val=sharedPreferences.getString(key, "");
		return val;
	}
}
