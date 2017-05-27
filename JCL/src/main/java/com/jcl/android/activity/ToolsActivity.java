package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.view.MyUINavigationView;

public class ToolsActivity extends BaseActivity implements OnClickListener{

	private MyUINavigationView uINavigationView;
	private View rl_carsize;//测距布局
	private View rl_mileage;//尺寸布局
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_tools);
		initView();
		initNavigation();
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
	
	
	private void initView()
	{
		rl_carsize=findViewById(R.id.rl_carsize);
		rl_mileage=findViewById(R.id.rl_mileage);
		
		rl_carsize.setOnClickListener(this);
		rl_mileage.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_mileage:
			startActivity(new Intent(ToolsActivity.this, ToolBoxActivity.class));
			break;
		case R.id.rl_carsize:
			startActivity(new Intent(ToolsActivity.this, ToolCarSizeActivity.class));
			break;
		default:
			break;
		}
		
	}
	
	
	

	
}
