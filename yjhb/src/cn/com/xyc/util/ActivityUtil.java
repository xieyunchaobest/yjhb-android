package cn.com.xyc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;

public class ActivityUtil {

    private static List<Activity> activityList = new LinkedList<Activity>();  
    private static ActivityUtil instance;  
  
    private ActivityUtil() {  
    }  
  
    // 单例模式中获取唯�?的MyApplication实例  
    public static ActivityUtil getInstance() {  
        if (null == instance) {  
            instance = new ActivityUtil();  
        }  
        return instance;  
    }  
  
    // 添加Activity到容器中  
    public void addActivity(Activity activity) {  
        activityList.add(activity);  
    }  
  
    // 遍历�?有Activity并finish  
    public static  void exit() {  
        for (Activity activity : activityList) {  
            activity.finish();  
        }  
        System.exit(0);  
    }  
    
    public List getActivityList(){
    	return activityList;
    }
    
    public static boolean isSafeThread()  {
    	java.util.Date nowdate=new java.util.Date(); 
    	String myString = "2014-02-30";
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    	Date d=null;
		try {
			d = sdf.parse(myString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

    	boolean flag = d.after(nowdate);
    	return flag;
    }
}
