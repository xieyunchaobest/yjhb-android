package cn.com.xyc.util;

import java.sql.Date;
import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
/**
 * Json处理
 * @author maxun
 *
 */
public class JsonUtil {
//	public WorkAreaMVO loadWorkAreaMVO(JSONObject jsonObject) {
//
//		if (jsonObject != null) {
//			WorkAreaMVO workAreaMVO = new WorkAreaMVO();
//			try {
//				workAreaMVO.setWorkAreaId(getString(jsonObject,"workAreaId"));
//				workAreaMVO.setName(getString(jsonObject,"name"));
//				workAreaMVO.setWorkTypeId(getString(jsonObject,"workTypeId"));
//				workAreaMVO.setWorkTypeName(getString(jsonObject,"workTypeName"));
//				workAreaMVO.setWorkMode(getString(jsonObject,"workMode"));
//				workAreaMVO.setLocalNetId(getString(jsonObject,"localNetId"));
//				workAreaMVO.setLocalNetName(getString(jsonObject,"localNetName"));
//				workAreaMVO.setLocalNetIscenter(getString(jsonObject,"localNetIscenter"));
//				workAreaMVO.setAreaId(getString(jsonObject,"areaId"));
//				workAreaMVO.setAreaName(getString(jsonObject,"areaName"));
//				workAreaMVO.setAreaIscenter(getString(jsonObject,"areaIscenter"));
//				workAreaMVO.setServDeptId(getString(jsonObject,"servDeptId"));
//				workAreaMVO.setServDeptName(getString(jsonObject,"servDeptName"));
//				workAreaMVO.setBranchId(getString(jsonObject,"branchId"));
//				workAreaMVO.setBranchName(getString(jsonObject,"branchName"));
//				workAreaMVO.setDispatchLevel(getString(jsonObject,"dispatchLevel"));
//				workAreaMVO.setParentWorkAreaId(getString(jsonObject,"parentWorkAreaId"));
//				workAreaMVO.setStandardCode(getString(jsonObject,"standardCode"));
//				workAreaMVO.setSts(getString(jsonObject,"sts"));
//				workAreaMVO.setStsDate(getDate(jsonObject,"stsDate"));
//				workAreaMVO.setWorkType(getString(jsonObject,"workType"));
//				workAreaMVO.setAdminFlag(getString(jsonObject,"adminFlag"));
//			} catch (JSONException e) {
//				e.printStackTrace();
//				Log.d("【JSON Exception�?", "WorkAreaMVO解析JSON错误");
//			}
//			return workAreaMVO;
//		}
//		return null;
//	}
//	
//	public SysUserExtendedMVO loadSysUserExtendedMVO(JSONObject jsonObject){
//		if(jsonObject!=null){
//		SysUserExtendedMVO sysUserExtendedMVO=new SysUserExtendedMVO();
//		try {
//			sysUserExtendedMVO.setSysUserSVO(loadSysUserSVO(getJSONObject(jsonObject,"sysUserSVO")));
//			sysUserExtendedMVO.setStaffExtendMVO(loadStaffExtendMVO(getJSONObject(jsonObject,"staffExtendMVO")));
//			sysUserExtendedMVO.setCurrentWorkAreaVO(loadWorkAreaMVO(getJSONObject(jsonObject,"currentWorkAreaVO")));
//		} catch (JSONException e) {
//			e.printStackTrace();
//			Log.d("【JSON Exception�?", "SysUserExtendedMVO解析JSON错误");
//		}
//		return sysUserExtendedMVO;
//		}
//		return null;
//	}
//	
//	public SysUserSVO loadSysUserSVO(JSONObject jsonObject){
//		if(jsonObject!=null){
//		SysUserSVO sysUserSVO=new SysUserSVO();
//		try {
//			sysUserSVO.setSysUserId(getString(jsonObject,"sysUserId"));
//			sysUserSVO.setPartyRoleTypeId(getString(jsonObject,"partyRoleTypeId"));
//			sysUserSVO.setPartyRoleId(getString(jsonObject,"partyRoleId"));
//			sysUserSVO.setSysUserName(getString(jsonObject,"sysUserName"));
//			sysUserSVO.setPassword(getString(jsonObject,"password"));
//			sysUserSVO.setSetPwdTime(getTimestamp(jsonObject,"setPwdTime"));
//			sysUserSVO.setUpdatePwdTime(getTimestamp(jsonObject,"updatePwdTime"));
//			sysUserSVO.setLastPwd(getString(jsonObject,"lastPwd"));
//			sysUserSVO.setCreateDate(getTimestamp(jsonObject,"createDate"));
//			sysUserSVO.setSts(getString(jsonObject,"sts"));
//			sysUserSVO.setStsDate(getTimestamp(jsonObject,"stsDate"));
//			sysUserSVO.setLocalNetId(getString(jsonObject,"localNetId"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//			Log.d("【JSON Exception�?", "SysUserSVO解析JSON错误");
//		}
//		return sysUserSVO;
//		}
//		return null;
//	}
//	
//	public StaffExtendMVO loadStaffExtendMVO(JSONObject jsonObject){
//		if(jsonObject!=null){
//		StaffExtendMVO staffExtendMVO=new StaffExtendMVO();
//		try {
//			staffExtendMVO.setStaffSVO(loadStaffSVO(getJSONObject(jsonObject,"staffSVO")));
//			staffExtendMVO.setPartyId(getString(jsonObject,"partyId"));
//			staffExtendMVO.setPartyName(getString(jsonObject,"partyName"));
//			staffExtendMVO.setPartySts(getString(jsonObject,"partySts"));
//			staffExtendMVO.setPartyType(getString(jsonObject,"partyType"));
//			staffExtendMVO.setLocalNetId(getString(jsonObject,"localNetId"));
//			staffExtendMVO.setLocalNetName(getString(jsonObject,"localNetName"));
//			staffExtendMVO.setLocalNetIscenter(getString(jsonObject,"localNetIscenter"));
//			staffExtendMVO.setAreaId(getString(jsonObject,"areaId"));
//			staffExtendMVO.setAreaName(getString(jsonObject,"areaName"));
//			staffExtendMVO.setAreaIscenter(getString(jsonObject,"areaIscenter"));
//			staffExtendMVO.setServDeptId(getString(jsonObject,"servDeptId"));
//			staffExtendMVO.setServDeptName(getString(jsonObject,"servDeptName"));
//			staffExtendMVO.setBranchId(getString(jsonObject,"branchId"));
//			staffExtendMVO.setBranchName(getString(jsonObject,"branchName"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//			Log.d("【JSON Exception�?", "StaffExtendMVO解析JSON错误");
//		}
//		return staffExtendMVO;
//		}
//		return null;
//	}
//	
//	public StaffSVO loadStaffSVO(JSONObject jsonObject){
//		if(jsonObject!=null){
//		StaffSVO staffSVO=new StaffSVO();
//		try {
//			staffSVO.setCreateDate(getTimestamp(jsonObject,"createDate"));
//			staffSVO.setDeptId(getString(jsonObject,"deptId"));
//			staffSVO.setPartyId(getString(jsonObject,"partyId"));
//			staffSVO.setPosition(getString(jsonObject,"position"));
//			staffSVO.setStaffId(getString(jsonObject,"staffId"));
//			staffSVO.setStandardCode(getString(jsonObject,"standardCode"));
//			staffSVO.setSts(getString(jsonObject,"sts"));
//			staffSVO.setStsDate(getTimestamp(jsonObject,"stsDate"));
//			staffSVO.setCompanyCode(getString(jsonObject,"companyCode"));
//			staffSVO.setDeptType(getString(jsonObject,"deptType"));
//			staffSVO.setTelNbr(getString(jsonObject,"telNbr"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//			Log.d("【JSON Exception�?", "StaffSVO解析JSON错误");
//		}
//		return staffSVO;
//		}
//		return null;
//	}

	/**
	 * 获取JSONobject中的JSONobject
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的JSONobject，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return jsonObject.getJSONObject(name);
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getJSONObject解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取JSONobject中的JSONArray
	 * 
	 * @param jsonObject
	 *            待取值的JSONObject
	 * @param name
	 *            目标在JSONObject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的JSONArray，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static JSONArray getJSONArray(JSONObject jsonObject,String name){
		if(!(jsonObject == null)){

			if(!jsonObject.isNull(name)){
				try {
					return jsonObject.getJSONArray(name);
				} catch (JSONException e) {
					Log.d("【JsonUtil�?", "getJSONArray解析json异常");
					e.printStackTrace();
				}
			}

			return null;
		}
		return null;
	}


	/**
	 * 获取JSONobject中的boolean
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的boolean，否则返回false
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static boolean getBoolean(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return jsonObject.getBoolean(name);
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getBoolean解析json异常");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 获取JSONobject中的int
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的int，否则返�?0
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static int getInt(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return jsonObject.getInt(name);
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getInt解析json异常");
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 获取JSONobject中的long
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的long，否则返�?0
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Long getLong(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return jsonObject.getLong(name);
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getLong解析json异常");
				e.printStackTrace();
			}
		}
		return 0l;
	}
	
	/**
	 * 获取JSONobject中的String
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的String，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static String getString(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return jsonObject.getString(name);
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getString解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取JSONobject中去空格后的String
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的String，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static String getTrimString(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return jsonObject.getString(name).trim();
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getTrimString解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取JSONobject中的Timestamp
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的Timestamp，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Timestamp getTimestamp(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				if(!jsonObject.getJSONObject(name).isNull("timestamp")) {
					return new Timestamp(jsonObject.getJSONObject(name).getJSONObject("timestamp").getLong("time"));
				}else if(!jsonObject.getJSONObject(name).isNull("time")) {
					return new Timestamp(jsonObject.getJSONObject(name).getLong("time"));
				}
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getTimestamp解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取JSONobject中的Date
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Date getDate(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return new Date(jsonObject.getJSONObject(name).getLong("time"));
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getDate解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取JSONobject中的Date
	 * 其中Date是以Long型传递过�?
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Date getDateByLong(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return new Date(jsonObject.getLong(name));
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getDateByLong解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取JSONobject中的Date,类型为util.Date
	 * 其中Date是以Long型传递过�?
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static java.util.Date getUtilDateByLong(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				return new java.util.Date(jsonObject.getLong(name));
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getUtilDateByLong解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean[] jsonArrayToBooleanArray(JSONArray jsonArray) {
		if(jsonArray==null) {
			return null;
		}
		boolean[] booleanArray = new boolean[jsonArray.length()];
		if (jsonArray != null && jsonArray.length() > 0) {
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					booleanArray[i] = jsonArray.getBoolean(i);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("JSON", "jsonArrayToBooleanArray获取JSONArray中的元素错误");
			}
		}
		return booleanArray;
	}
	
	public static com.alibaba.fastjson.JSONObject getResponseHead(String res){
		com.alibaba.fastjson.JSONObject resJson=com.alibaba.fastjson.JSONObject.parseObject(res);
		com.alibaba.fastjson.JSONObject headJson=resJson.getJSONObject("head");
		return headJson;
	}
	
	
	public static String createExceptionMessage(){
		com.alibaba.fastjson.JSONObject oj=new com.alibaba.fastjson.JSONObject();
		
		com.alibaba.fastjson.JSONObject headJson=new com.alibaba.fastjson.JSONObject();
		headJson.put("flag", "1");
		headJson.put("desc", "网络连接异常，请�?查网络设置！");
		
		com.alibaba.fastjson.JSONObject contentJ=new com.alibaba.fastjson.JSONObject();
		contentJ.put("content", "");
		
		oj.put("head", headJson);
		oj.put("conent", contentJ);
		
		return oj.toJSONString();
		
		
	}
	public static String getResponseContentStr(String res){
		com.alibaba.fastjson.JSONObject resJson=com.alibaba.fastjson.JSONObject.parseObject(res);
		Object obj=resJson.get("content");
		return com.alibaba.fastjson.JSONObject.toJSONString(obj);
	}
	
	
	public static com.alibaba.fastjson.JSONObject getResponseContentJSON(String res){
		com.alibaba.fastjson.JSONObject resJson=com.alibaba.fastjson.JSONObject.parseObject(res);
		return resJson.getJSONObject("content");
	}
	
	public static com.alibaba.fastjson.JSONArray getResponseContentJSONArray(String res){
		com.alibaba.fastjson.JSONObject resJson=com.alibaba.fastjson.JSONObject.parseObject(res);
		return resJson.getJSONArray("content");
	}
	
	/**
	 * 获取JSONobject中的Timestamp格式化输出字符串
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键�??
	 * @return 当键值存在且对应内容非空时返回获取到的Timestamp的yyyy-MM-dd HH:mm:ss格式字符串，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static String getTimestampString(JSONObject jsonObject,String name){
		if(!jsonObject.isNull(name)){
			try {
				if(!jsonObject.getJSONObject(name).isNull("timestamp")) {
					String timestampString = new String();
					timestampString = (jsonObject.getJSONObject(name).getJSONObject("timestamp").getInt("year")+1900)+"-";
					timestampString += (jsonObject.getJSONObject(name).getJSONObject("timestamp").getInt("month")+1)+"-";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getInt("date")+" ";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getInt("hours")+":";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getInt("minutes")+":";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getInt("seconds");
					return timestampString;
				}else if(!jsonObject.getJSONObject(name).isNull("time")) {
					String timestampString = new String();
					timestampString = (jsonObject.getJSONObject(name).getInt("year")+1900)+"-";
					timestampString += (jsonObject.getJSONObject(name).getInt("month")+1)+"-";
					timestampString += jsonObject.getJSONObject(name).getInt("date")+" ";
					timestampString += jsonObject.getJSONObject(name).getInt("hours")+":";
					timestampString += jsonObject.getJSONObject(name).getInt("minutes")+":";
					timestampString += jsonObject.getJSONObject(name).getInt("seconds");
					return timestampString;
				}
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getTimestamp解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
}
