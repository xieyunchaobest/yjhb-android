package cn.com.xyc.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

public class DataPub {
	
	private List  storeList = new ArrayList ();
	public String[] storekey = {"item_name", "item_address",
			"item_id","item_jd","item_wd","item_busroute"};
	
	public List initStoreList(Context ctx ) {
		CacheProcess c= new CacheProcess();
		String cache=c.getCacheValueInSharedPreferences(ctx, Constant.LOCAL_STORE_CACHES);
		JSONObject cj=com.alibaba.fastjson.JSONObject.parseObject(cache);
		List list = com.alibaba.fastjson.JSON.parseArray(
				cj.getJSONArray("storeList").toJSONString(),
				java.util.HashMap.class);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				Map m=(Map)list.get(i);
				int id=(Integer)m.get("id");
				String name=(String)m.get("storeName");
				String address=(String)m.get("address");
				String busRoute=(String)m.get("busRoute");
				double longitude=((BigDecimal)m.get("longitude")).doubleValue();
				double latitude=((BigDecimal)m.get("latitude")).doubleValue();
				
				
				Map mm=new HashMap();
				mm.put(storekey[0], name);
				mm.put(storekey[1], address);
				mm.put(storekey[2], id);
				mm.put(storekey[3], longitude);
				mm.put(storekey[4], latitude);
				mm.put(storekey[5], busRoute);
				storeList.add(mm);
			}
		} 
		return storeList;
	}
}
