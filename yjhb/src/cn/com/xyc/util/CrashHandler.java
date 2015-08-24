package cn.com.xyc.util;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.view.View.OnClickListener;

public class CrashHandler implements UncaughtExceptionHandler {  
    public static final String TAG = "CrashHandler";  
    private static CrashHandler INSTANCE = new CrashHandler();  
    private Context mContext;  
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
  
    private CrashHandler() {  
    }  
  
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    public void init(Context ctx) {  
        mContext = ctx;  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
         if (!handleException(ex) && mDefaultHandler != null) {  
         mDefaultHandler.uncaughtException(thread, ex);  
         } else {  
         android.os.Process.killProcess(android.os.Process.myPid());  
         System.exit(10);  
         }  
        System.out.println("uncaughtException");  
  
        ex.printStackTrace();  
    }  
  
    /** 
     * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼� 
     * 
     * @param ex 
     * @return true:��������˸��쳣��Ϣ;���򷵻�false 
     */  
    private boolean handleException(Throwable ex) {  
    	if(ex!=null)ex.printStackTrace();
        if (ex == null) {  
            return true;  
        }   
        return true;  
    }  
}  