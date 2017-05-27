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

/**
 * 管理我的物流圈页面
 * @author syl
 *
 */

public class SettingLogisticsCircleActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_logisitics_circle);
		initView();
		initNavigation();
	}
	
	private MyUINavigationView uINavigationView;
	private void initNavigation() {
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

	private void initView() {
		findViewById(R.id.tv_jingjiren).setOnClickListener(this);
		findViewById(R.id.tv_siji).setOnClickListener(this);
		findViewById(R.id.tv_kehu).setOnClickListener(this);
		findViewById(R.id.tv_lianxiren).setOnClickListener(this);
		
		findViewById(R.id.tv_jingjiren).setVisibility(View.GONE);
		findViewById(R.id.tv_siji).setVisibility(View.GONE);
		findViewById(R.id.tv_kehu).setVisibility(View.GONE);	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_jingjiren:
			startActivity(new Intent(this,MyLogisticCircleActivity.class).putExtra("type", "1"));
			break;
		case R.id.tv_siji:
			startActivity(new Intent(this,MyLogisticCircleActivity.class).putExtra("type", "2"));
			break;
		case R.id.tv_kehu:
			startActivity(new Intent(this,MyLogisticCircleActivity.class).putExtra("type", "3"));
			break;

		case R.id.tv_lianxiren:
			startActivity(new Intent(this,MyLogisticCircleActivity.class).putExtra("type", "4"));
			break;

		default:
			break;
		}
		
	}

}
