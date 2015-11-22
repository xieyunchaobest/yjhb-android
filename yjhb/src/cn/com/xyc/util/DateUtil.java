package cn.com.xyc.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DateUtil {

	public static SimpleDateFormat datetimef = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat datetimehm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

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
	public static Timestamp getTimestamp(JSONObject jsonObject, String name)
			throws JSONException {
		if (!jsonObject.isNull(name)) {
			if (!jsonObject.getJSONObject(name).isNull("timestamp")) {
				return new Timestamp(jsonObject.getJSONObject(name)
						.getJSONObject("timestamp").getLong("time"));
			} else if (!jsonObject.getJSONObject(name).isNull("time")) {
				return new Timestamp(jsonObject.getJSONObject(name).getLong(
						"time"));

			}
		}
		return null;
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
	public static String getTimestampString(JSONObject jsonObject, String name) {
		if (!jsonObject.isNull(name)) {
			try {
				if (!jsonObject.getJSONObject(name).isNull("timestamp")) {
					String timestampString = new String();
					timestampString = (jsonObject.getJSONObject(name)
							.getJSONObject("timestamp").getInt("year") + 1900)
							+ "-";
					timestampString += (jsonObject.getJSONObject(name)
							.getJSONObject("timestamp").getInt("month") + 1)
							+ "-";
					timestampString += jsonObject.getJSONObject(name)
							.getJSONObject("timestamp").getInt("date")
							+ " ";
					timestampString += jsonObject.getJSONObject(name)
							.getJSONObject("timestamp").getInt("hours")
							+ ":";
					timestampString += jsonObject.getJSONObject(name)
							.getJSONObject("timestamp").getInt("minutes")
							+ ":";
					timestampString += jsonObject.getJSONObject(name)
							.getJSONObject("timestamp").getInt("seconds");
					return timestampString;
				} else if (!jsonObject.getJSONObject(name).isNull("time")) {
					String timestampString = new String();
					timestampString = (jsonObject.getJSONObject(name).getInt(
							"year") + 1900)
							+ "-";
					timestampString += (jsonObject.getJSONObject(name).getInt(
							"month") + 1)
							+ "-";
					timestampString += jsonObject.getJSONObject(name).getInt(
							"date")
							+ " ";
					timestampString += jsonObject.getJSONObject(name).getInt(
							"hours")
							+ ":";
					timestampString += jsonObject.getJSONObject(name).getInt(
							"minutes")
							+ ":";
					timestampString += jsonObject.getJSONObject(name).getInt(
							"seconds");
					return timestampString;
				}
			} catch (JSONException e) {
				Log.d("【JsonUtil�?", "getTimestamp解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 日期（精确到日）转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String date2Str(Date date) {
		String str = "";
		if (null != date) {
			str = DateUtil.to_char(date, "yyyy-MM-dd");
		}
		return str;
	}

	public static String time2Str(Date date) {
		String str = "";
		if (null != date) {
			str = DateUtil.to_char(date, "yyyy-MM-dd HH:mm:ss");
		}
		return str;
	}

	/**
	 * 按指定的格式将日期对象转换为字符�?
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String to_char(Date date, String format) {
		if (date == null)
			return null;
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 日期（精确到分）转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateHm2Str(Date date) {
		String str = "";
		if (null != date) {
			str = DateUtil.to_char(date, "yyyy-MM-dd HH:mm");
		}
		return str;
	}

	/**
	 * 日期（精确到秒）转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateTime2Str(Date date) {
		String str = "";
		if (null != date) {
			str = DateUtil.to_char(date, "yyyy-MM-dd HH:mm:ss");
		}
		return str;
	}

	/**
	 * 得到大于传入天数的日�?
	 * 
	 * @param date
	 * @param num
	 * @return java.util.Date
	 * @throws AppException
	 */
	public static Date addDays(Date date, int num) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(GregorianCalendar.DAY_OF_MONTH, num);
		return cal.getTime();
	}

	/**
	 * 字符串转日期（精确到分）
	 * 
	 * @param str
	 * @return
	 * @throws AppException
	 * @throws SysException
	 */
	public static Date str2DateTimeHM(String str) {
		Date date = null;
		if (!StringUtil.isBlank(str)) {
			date = DateUtil.to_date(str, "yyyy-MM-dd HH:mm");
		}
		return date;

	}

	/**
	 * 按指定格式将字符串转换为日期对象
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws SysException
	 * 
	 */
	public static Date to_date(String dateStr, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串转日期（只有小时和分钟�?
	 * 
	 * @param str
	 * @return
	 * @throws AppException
	 * @throws SysException
	 */
	public static Date str2HourMiniTime(String str) {
		Date date = null;
		if (!StringUtil.isBlank(str)) {
			date = DateUtil.to_date(str, "HH:mm");
		}
		return date;

	}

	/**
	 * 字符串转日期（精确到日）
	 * 
	 * @param str
	 * @return
	 * @throws AppException
	 * @throws SysException
	 */
	public static Date str2Date(String str) {
		Date date = null;
		if (!StringUtil.isBlank(str)) {
			date = DateUtil.to_date(str, "yyyy-MM-dd");
		}
		return date;

	}

	public static double dateDiff(String date1, String date2) {
		Date d1 = DateUtil.str2Date(date1);
		Date d2 = DateUtil.str2Date(date2);
		long diff = d2.getTime() - d1.getTime();
		return (diff / (1000 * 60 * 60 * 24));
	}

	private static String formatString(int x) {
		String s = Integer.toString(x);
		if (s.length() == 1) {
			s = "0" + s;
		}
		return s;
	}

	public static String getCurrentDateStr() {
		Date d = new Date();
		return date2Str(d);
	}

	public static String getYesterdayStr() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal
				.getTime());
		return yesterday;
	}
	
	
	public static String getLastMonth(java.util.Date date) {
		Calendar cal = Calendar.getInstance();     
		cal.setTime(date);        
		cal.add(Calendar.MONTH, -1);
		Date adate=cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(adate);
				
	}
	
	public static int getYesterdayYear() {
		return Integer.parseInt(getYesterdayStr().substring(0,4));
	}
	
	public static int getYesterdayMonth() {
		return Integer.parseInt(getYesterdayStr().substring(5,7));
	}
	
	public static int getYesterdayDay() {
		return Integer.parseInt(getYesterdayStr().substring(8, 10));
	}

	public static String getCurrentTime() {
		Date d = new Date();
		return time2Str(d);
	}
	
	public static void main(String args[]) {
	System.out.println(	DateUtil.getYesterdayYear());
	System.out.println(	DateUtil.getYesterdayMonth());
	System.out.println(DateUtil.getYesterdayDay());
	
	}
}
