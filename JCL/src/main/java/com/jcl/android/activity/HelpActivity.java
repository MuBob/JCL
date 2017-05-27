package com.jcl.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.WebViewFragment;
import com.jcl.android.view.MyUINavigationView;

public class HelpActivity extends BaseActivity {
	private MyUINavigationView uINavigationView;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_help);
		initNavigation();
		initFragment();
	}

	private void initFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.container,
						WebViewFragment
								.newInstance(C.BASE_URL+"/help?userid="+JCLApplication.getInstance().getUserId()))
				.commit();
	}
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
