package cn.com.xyc.view;

import android.content.Context;
import android.text.Html;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.xyc.R;

/**
 * 自定义组件，用于在显示详细信�?
 * @author xieyunchao
 * CreateTime Dec 11, 2012 5:51:39 PM
 */
public class EditLabelText extends LinearLayout {
	
	private String label=""; //标签,如姓名：，年�?
	private String value="";//属�?��??
	private String hasStar="";
	
	private int imageResource=0;

	
	TextView labelText=null;
	EditText valueText=null;
	
	public EditLabelText(Context context){
		this(context,null);
	}
	

	public EditLabelText(Context context, AttributeSet attrs) {
		super(context, attrs);
		int labelResouceId = -1;
		int valueResouceId=-1;
		int hasStarResourceId=-1;
		labelText=new TextView(context); 
		valueText=new EditText(context);
		valueText.setBackgroundResource(0);
		LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(  
				  
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);  
		valueText.setLayoutParams(imagebtn_params);
		
		labelResouceId = attrs.getAttributeResourceValue(null, "label", 0);
		if (labelResouceId > 0) {
            label = context.getResources().getText(labelResouceId).toString();
        } else {
            label = "";
        }
		hasStarResourceId= attrs.getAttributeResourceValue(null, "hasStar", 0);
		if(hasStarResourceId>0) {
			label="<font color='red'>*</font>"+label;
		}
		
		labelText.setText(Html.fromHtml(label));
		labelText.setTextSize(16.0f);
		labelText.setTextColor(0xFF000000);
		  
		
		valueResouceId = attrs.getAttributeResourceValue(null, "value", 0);
		if (valueResouceId > 0) {
            value = context.getResources().getText(valueResouceId).toString();
        } else {
            value = "";
        }
		valueText.setText(value);
		valueText.setTextColor(0xFF666666);
		valueText.setAutoLinkMask(Linkify.WEB_URLS);
		valueText.setFocusable(true);
		valueText.setTextSize(16f);
		addView(labelText);
		addView(valueText);
		
		this.setGravity(LinearLayout.HORIZONTAL);
		
		imageResource = attrs.getAttributeResourceValue(null, "bgImage", 0);
		if(imageResource==0) {
			imageResource=R.drawable.detail_info;
		}
		
		this.setBackgroundResource(imageResource);
		
		this.setGravity(Gravity.CENTER_VERTICAL);
		
	}
	
	public int getImageResource() {
		return imageResource;
	}


	public void setImageResource(int imageResource) {
		this.imageResource = imageResource;
	}


	public void setLabel(String alabel) {
		labelText.setText(alabel);
	}
	
	public void setValue(String aValue) {
		valueText.setText(aValue);
	}

	
	
	public TextView getLabelText() {
		return labelText;
	}


	public void setLabelText(TextView labelText) {
		this.labelText = labelText;
	}


	public EditText getValueText() {
		return valueText;
	}


	public void setValueText(EditText valueText) {
		this.valueText = valueText;
	}


	public String getHasStar() {
		return hasStar;
	}


	public void setHasStar(String hasStar) {
		this.hasStar = hasStar;
	}

 
	
}
