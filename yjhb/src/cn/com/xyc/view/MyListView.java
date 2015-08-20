package cn.com.xyc.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.xyc.R;
  
public class MyListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemLongClickListener{  
  
    private static final String TAG = "listview";  
  
    private final static int RELEASE_To_REFRESH = 0;  
    private final static int PULL_To_REFRESH = 1;  
    private final static int REFRESHING = 2;  
    private final static int DONE = 3;  
    private final static int LOADING = 4;  
  
    // 实锟绞碉拷padding锟侥撅拷锟斤拷锟斤拷锟斤拷锟斤拷锟狡拷凭锟斤拷锟侥憋拷锟斤拷  
    private final static int RATIO = 3;  
  
    private LayoutInflater inflater;  
  
    private LinearLayout headView;  
  
    private TextView tipsTextview;  
    private TextView lastUpdatedTextView;  
    private ImageView arrowImageView;  
    private ProgressBar progressBar;  
  
  
    private RotateAnimation animation;  
    private RotateAnimation reverseAnimation;  
  
    // 锟斤拷锟节憋拷证startY锟斤拷值锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷touch锟铰硷拷锟斤拷只锟斤拷锟斤拷录一锟斤拷  
    private boolean isRecored;  
  
    private int headContentWidth;  
    private int headContentHeight;  
  
    private int startY;  
    private int firstItemIndex;  
  
    private int state;  
  
    private boolean isBack;  
  
    private OnRefreshListener refreshListener;  
  
    private boolean isRefreshable;  
  
    public MyListView(Context context) {  
        super(context);  
        init(context);  
    }  
  
    public MyListView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(context);  
    }  
  
    private void init(Context context) {  
        //setCacheColorHint(context.getResources().getColor(R.color.transparent));  
        inflater = LayoutInflater.from(context);  
  
        headView = (LinearLayout) inflater.inflate(R.layout.list_head, null);  
  
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
                
        arrowImageView.setMinimumWidth(70);  
        arrowImageView.setMinimumHeight(50);  
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
                
  
        measureView(headView);  
        headContentHeight = headView.getMeasuredHeight();  
        headContentWidth = headView.getMeasuredWidth();  
  
        headView.setPadding(0, -1 * headContentHeight, 0, 0);  
        headView.invalidate();  
  
//        Log.v("size", "width:" + headContentWidth + " height:"  
//                + headContentHeight);  
  
        addHeaderView(headView, null, false);  
        setOnScrollListener(this);  
  
        animation = new RotateAnimation(0, -180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        animation.setInterpolator(new LinearInterpolator());  
        animation.setDuration(250);  
        animation.setFillAfter(true);  
  
        reverseAnimation = new RotateAnimation(-180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);  
        reverseAnimation.setInterpolator(new LinearInterpolator());  
        reverseAnimation.setDuration(200);  
        reverseAnimation.setFillAfter(true);  
  
        state = DONE;  
        isRefreshable = false;  
    }  
  
    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,  
            int arg3) {  
        firstItemIndex = firstVisiableItem;  
    }  
  
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    	
    }  
  
    public boolean onTouchEvent(MotionEvent event) {  
  
        if (isRefreshable) {  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                if (firstItemIndex == 0 && !isRecored) {  
                    isRecored = true;  
                    startY = (int) event.getY();  
//                    Log.v(TAG, "锟斤拷down时锟斤拷锟铰硷拷锟角拔伙拷谩锟�");  
                }  
                break;  
  
            case MotionEvent.ACTION_UP:  
  
                if (state != REFRESHING && state != LOADING) {  
                    if (state == DONE) {  
                        // 什么锟斤拷锟斤拷锟斤拷  
                    }  
                    if (state == PULL_To_REFRESH) {  
                        state = DONE;  
                        changeHeaderViewByState();  
  
//                        Log.v(TAG, "锟斤拷锟斤拷锟斤拷刷锟斤拷状态锟斤拷锟斤拷done状态");  
                    }  
                    if (state == RELEASE_To_REFRESH) {  
                        state = REFRESHING;  
                        changeHeaderViewByState();  
                        onRefresh();  
  
//                        Log.v(TAG, "锟斤拷锟缴匡拷刷锟斤拷状态锟斤拷锟斤拷done状态");  
                    }  
                }  
  
                isRecored = false;  
                isBack = false;  
  
                break;  
  
            case MotionEvent.ACTION_MOVE:  
                int tempY = (int) event.getY();  
  
                if (!isRecored && firstItemIndex == 0) {  
//                    Log.v(TAG, "锟斤拷move时锟斤拷锟铰硷拷锟轿伙拷锟�");  
                    isRecored = true;  
                    startY = tempY;  
                }  
  
                if (state != REFRESHING && isRecored && state != LOADING) {  
  
                    // 锟斤拷证锟斤拷锟斤拷锟斤拷padding锟侥癸拷锟斤拷锟叫ｏ拷锟斤拷前锟斤拷位锟斤拷一直锟斤拷锟斤拷head锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷斜锟斤拷锟斤拷锟侥伙拷幕锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷频锟绞憋拷锟斤拷斜锟斤拷同时锟斤拷锟叫癸拷锟斤拷  
  
                    // 锟斤拷锟斤拷锟斤拷锟斤拷去刷锟斤拷锟斤拷  
                    if (state == RELEASE_To_REFRESH) {  
  
                        setSelection(0);  
  
                        // 锟斤拷锟斤拷锟斤拷锟剿ｏ拷锟狡碉拷锟斤拷锟斤拷幕锟姐够锟节革拷head锟侥程度ｏ拷锟斤拷锟角伙拷没锟斤拷锟狡碉拷全锟斤拷锟节盖的地诧拷  
                        if (((tempY - startY) / RATIO < headContentHeight)  
                                && (tempY - startY) > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "锟斤拷锟缴匡拷刷锟斤拷状态转锟戒到锟斤拷锟斤拷刷锟斤拷状态");  
                        }  
                        // 一锟斤拷锟斤拷锟狡碉拷锟斤拷锟斤拷  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "锟斤拷锟缴匡拷刷锟斤拷状态转锟戒到done状态");  
                        }  
                        // 锟斤拷锟斤拷锟斤拷锟剿ｏ拷锟斤拷锟竭伙拷没锟斤拷锟斤拷锟狡碉拷锟斤拷幕锟斤拷锟斤拷锟节革拷head锟侥地诧拷  
                        else {  
                            // 锟斤拷锟矫斤拷锟斤拷锟截憋拷牟锟斤拷锟斤拷锟街伙拷酶锟斤拷锟絧addingTop锟斤拷值锟斤拷锟斤拷锟斤拷  
                        }  
                    }  
                    // 锟斤拷没锟叫碉拷锟斤拷锟斤拷示锟缴匡拷刷锟铰碉拷时锟斤拷,DONE锟斤拷锟斤拷锟斤拷PULL_To_REFRESH状态  
                    if (state == PULL_To_REFRESH) {  
  
                        setSelection(0);  
  
                        // 锟斤拷锟斤拷锟斤拷锟斤拷锟皆斤拷锟斤拷RELEASE_TO_REFRESH锟斤拷状态  
                        if ((tempY - startY) / RATIO >= headContentHeight) {  
                            state = RELEASE_To_REFRESH;  
                            isBack = true;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "锟斤拷done锟斤拷锟斤拷锟斤拷锟斤拷刷锟斤拷状态转锟戒到锟缴匡拷刷锟斤拷");  
                        }  
                        // 锟斤拷锟狡碉拷锟斤拷锟斤拷  
                        else if (tempY - startY <= 0) {  
                            state = DONE;  
                            changeHeaderViewByState();  
  
//                            Log.v(TAG, "锟斤拷DOne锟斤拷锟斤拷锟斤拷锟斤拷刷锟斤拷状态转锟戒到done状态");  
                        }  
                    }  
  
                    // done状态锟斤拷  
                    if (state == DONE) {  
                        if (tempY - startY > 0) {  
                            state = PULL_To_REFRESH;  
                            changeHeaderViewByState();  
                        }  
                    }  
  
                    // 锟斤拷锟斤拷headView锟斤拷size  
                    if (state == PULL_To_REFRESH) {  
                        headView.setPadding(0, -1 * headContentHeight  
                                + (tempY - startY) / RATIO, 0, 0);  
  
                    }  
  
                    // 锟斤拷锟斤拷headView锟斤拷paddingTop  
                    if (state == RELEASE_To_REFRESH) {  
                        headView.setPadding(0, (tempY - startY) / RATIO  
                                - headContentHeight, 0, 0);  
                    }  
  
                }  
  
                break;  
            }  
        }  
  
        return super.onTouchEvent(event);  
    }  
  
    // 锟斤拷状态锟侥憋拷时锟津，碉拷锟矫该凤拷锟斤拷锟斤拷锟皆革拷锟铰斤拷锟斤拷  
    private void changeHeaderViewByState() {  
        switch (state) {  
        case RELEASE_To_REFRESH:  
            arrowImageView.setVisibility(View.VISIBLE);  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
            arrowImageView.clearAnimation();  
            arrowImageView.startAnimation(animation);  
  
            tipsTextview.setText("锟缴匡拷锟斤拷锟斤拷刷锟斤拷");  
  
//            Log.v(TAG, "锟斤拷前状态锟斤拷锟缴匡拷刷锟斤拷");  
            break;  
        case PULL_To_REFRESH:  
            progressBar.setVisibility(View.GONE);  
            tipsTextview.setVisibility(View.VISIBLE);  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.VISIBLE);  
            // 锟斤拷锟斤拷RELEASE_To_REFRESH状态转锟斤拷锟斤拷锟斤拷  
            if (isBack) {  
                isBack = false;  
                arrowImageView.clearAnimation();  
                arrowImageView.startAnimation(reverseAnimation);  
  
                tipsTextview.setText("锟斤拷锟斤拷锟较讹拷锟斤拷锟斤拷刷锟斤拷");  
            } else {  
                tipsTextview.setText("锟斤拷锟斤拷锟较讹拷锟斤拷锟斤拷刷锟斤拷");  
            }  
//            Log.v(TAG, "锟斤拷前状态锟斤拷锟斤拷锟斤拷刷锟斤拷");  
            break;  
  
        case REFRESHING:  
  
            headView.setPadding(0, 0, 0, 0);  
  
            progressBar.setVisibility(View.VISIBLE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.GONE);  
            tipsTextview.setText("锟斤拷锟斤拷刷锟斤拷...");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
//            Log.v(TAG, "锟斤拷前状态,锟斤拷锟斤拷刷锟斤拷...");  
            break;  
        case DONE:  
            headView.setPadding(0, -1 * headContentHeight, 0, 0);  
  
            progressBar.setVisibility(View.GONE);  
            arrowImageView.clearAnimation();  
            arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);  
            tipsTextview.setText("锟缴匡拷锟斤拷锟斤拷刷锟斤拷");  
            lastUpdatedTextView.setVisibility(View.VISIBLE);  
  
//            Log.v(TAG, "锟斤拷前状态锟斤拷done");  
            break;  
        }  
    }  
  
    public void setOnRefreshListener(OnRefreshListener refreshListener) {  
        this.refreshListener = refreshListener;  
        isRefreshable = true;  
    }  
  
    public interface OnRefreshListener {  
        public void onRefresh();  
    }  
  
    public void onRefreshComplete() {  
        state = DONE;  
        SimpleDateFormat format=new SimpleDateFormat("yyyy锟斤拷MM锟斤拷dd锟斤拷  HH:mm");  
        String date=format.format(new Date());  
        lastUpdatedTextView.setText("锟较达拷刷锟斤拷 锟斤拷" + date);  
        changeHeaderViewByState();  
    }  
  
    private void onRefresh() {  
    	 if ( null != refreshListener) {    
            refreshListener.onRefresh();  
        }  
    }  
  
    // 锟剿凤拷锟斤拷直锟斤拷锟秸帮拷锟斤拷锟斤拷锟斤拷锟较碉拷一锟斤拷锟斤拷锟斤拷刷锟铰碉拷demo锟斤拷锟剿达拷锟角★拷锟斤拷锟狡★拷headView锟斤拷width锟皆硷拷height  
    private void measureView(View child) {  
         ViewGroup.LayoutParams p = child.getLayoutParams();  
            if (p == null) {  
                p = new ViewGroup.LayoutParams(  
                        ViewGroup.LayoutParams.FILL_PARENT,  
                        ViewGroup.LayoutParams.WRAP_CONTENT);  
            }  
  
            int childWidthSpec = ViewGroup.getChildMeasureSpec(0,  
                    0 + 0, p.width);  
            int lpHeight = p.height;  
            int childHeightSpec;  
            if (lpHeight > 0) {  
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);  
            } else {  
                childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);  
            }  
            child.measure(childWidthSpec, childHeightSpec);  
    }  
  
    public void setAdapter(BaseAdapter adapter) {  
        SimpleDateFormat format=new SimpleDateFormat("yyyy锟斤拷MM锟斤拷dd锟斤拷  HH:mm");  
        String date=format.format(new Date());  
        lastUpdatedTextView.setText("锟较达拷刷锟斤拷 锟斤拷" + date);  
        super.setAdapter(adapter);  
    }

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		return false;
	}

      
  
}