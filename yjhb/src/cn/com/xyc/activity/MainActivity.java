package cn.com.xyc.activity;



import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import cn.com.xyc.R;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	private TextView main_tab_new_message;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try {
    		  super.onCreate(savedInstanceState);
    	        
    	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	        setContentView(R.layout.main);
    	        
    	        main_tab_new_message=(TextView) findViewById(R.id.main_tab_new_message);
    	        main_tab_new_message.setVisibility(View.VISIBLE);
    	        main_tab_new_message.setText("10");
    	        
    	        tabHost=this.getTabHost();
    	        TabHost.TabSpec spec;
    	        Intent intent;
    	        
    	        intent=new Intent().setClass(this, MapActivity.class);
    	        spec=tabHost.newTabSpec("�νݻ���").setIndicator("�νݻ���").setContent(intent);
    	        tabHost.addTab(spec);

    	        intent=new Intent().setClass(this, RentActivity.class);
    	        spec=tabHost.newTabSpec("�⳵").setIndicator("�⳵").setContent(intent);
    	        tabHost.addTab(spec);
    	        
    	        intent=new Intent().setClass(this,BuyActivity.class);
    	        spec=tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
    	        tabHost.addTab(spec);
    	        
    	        intent=new Intent().setClass(this, OrderListActivity.class);
    	        spec=tabHost.newTabSpec("����").setIndicator("����").setContent(intent);
    	        tabHost.addTab(spec);
    	        
    	     
    	        intent=new Intent().setClass(this, SettingActivity.class);
    	        spec=tabHost.newTabSpec("�ҵ�").setIndicator("�ҵ�").setContent(intent);
    	        tabHost.addTab(spec);
    	        
    	        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
    	        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    				
    				@Override
    				public void onCheckedChanged(RadioGroup group, int checkedId) {
    					switch (checkedId) {
    					case R.id.main_tab_addExam:
    						tabHost.setCurrentTabByTag("�⳵");
    						break;
    					case R.id.main_tab_myExam:
    						tabHost.setCurrentTabByTag("����");
    						break;
    					case R.id.main_tab_message:
    						tabHost.setCurrentTabByTag("����");
    						break;
    					case R.id.main_tab_settings:
    						tabHost.setCurrentTabByTag("�ҵ�");
    						break;
    					default:
    						tabHost.setCurrentTabByTag("�⳵");
    						break;
    					}
    				}
    			});
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
      
    }
    
   
}