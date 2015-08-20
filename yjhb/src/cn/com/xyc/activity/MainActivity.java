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
        spec=tabHost.newTabSpec("游捷滑板").setIndicator("游捷滑板").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, RentActivity.class);
        spec=tabHost.newTabSpec("租车").setIndicator("租车").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,BuyActivity.class);
        spec=tabHost.newTabSpec("购车").setIndicator("购车").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this, OrdersActivity.class);
        spec=tabHost.newTabSpec("订单").setIndicator("订单").setContent(intent);
        tabHost.addTab(spec);
        
     
        intent=new Intent().setClass(this, SettingActivity.class);
        spec=tabHost.newTabSpec("我的").setIndicator("我的").setContent(intent);
        tabHost.addTab(spec);
        
        RadioGroup radioGroup=(RadioGroup) this.findViewById(R.id.main_tab_group);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tab_addExam:
					tabHost.setCurrentTabByTag("租车");
					break;
				case R.id.main_tab_myExam:
					tabHost.setCurrentTabByTag("购车");
					break;
				case R.id.main_tab_message:
					tabHost.setCurrentTabByTag("订单");
					break;
				case R.id.main_tab_settings:
					tabHost.setCurrentTabByTag("我的");
					break;
				default:
					tabHost.setCurrentTabByTag("租车");
					break;
				}
			}
		});
    }
    
   
}