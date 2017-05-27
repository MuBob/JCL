package com.jcl.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.view.MyUINavigationView;

@SuppressLint("NewApi") public class MyWebViewActivity extends
         BaseActivity implements OnClickListener{

	private WebView wv_map;
	private String weburl,title;
	private Bundle bundle;
	private MyUINavigationView uINavigationView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_mapwebview);
		initView();
	}
	
	private void initView() {
		Intent intent = getIntent();
		bundle = intent.getExtras();
		weburl = bundle.getString("weburl");
		title = bundle.getString("title");
		Log.e("syl", "weburl~~~~~"+weburl);
		wv_map = (WebView) findViewById(R.id.wv_map);
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		TextView tv_title = uINavigationView.getTv_title();
		tv_title.setText(title);
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		WebSettings webSettings = wv_map.getSettings(); 
		wv_map.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		 //设置WebView属性，能够执行Javascript脚本    
		 webSettings.setJavaScriptEnabled(true);    
		 //设置可以访问文件  
	     webSettings.setAllowFileAccess(true);  
		//设置支持缩放  
		 webSettings.setBuiltInZoomControls(true);  
		 
		 //加载需要显示的网页    
		 wv_map.loadUrl(weburl);    
		 //设置Web视图    
		 wv_map.setWebViewClient(new webViewClient()); 
	}

	//Web视图    
    private class webViewClient extends WebViewClient {    
        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
            view.loadUrl(url);    
            return true;    
        }    
    }

	@Override
	public void onClick(View v) {
		
	} 

}
