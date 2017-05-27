package com.jcl.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.view.MyUINavigationView;

public class BlogActivity extends BaseActivity implements OnClickListener{

	private MyUINavigationView uINavigationView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_blog);
		initNavigation();
		initView();
	}
	
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setVisibility(View.GONE);
	}
	private void initView(){
		WebView webView = (WebView) findViewById(R.id.blog_WebView); 
		String url = C.BLOG_URL; 
//		String url = "http://www.csdn.net"; 
		webView.loadUrl(url);
	}
	
	@Override
	public void onClick(View arg0) {
		
	}
}

