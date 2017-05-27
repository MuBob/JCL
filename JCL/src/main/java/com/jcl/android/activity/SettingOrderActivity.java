package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.view.MyUINavigationView;

/**
 * 我的订单页面
 * 
 * @author msz
 *
 */
public class SettingOrderActivity extends BaseActivity implements
		OnClickListener {
	private MyUINavigationView uINavigationView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_order);
		initView();
		initNavigation();
	}

	private void initView() {
		findViewById(R.id.tv_setting_goods_order).setOnClickListener(this);
		findViewById(R.id.tv_setting_car_order).setOnClickListener(this);
	}
	
	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnRightText.setVisibility(View.GONE);
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_setting_goods_order:
			startActivity(new Intent(this, SettingGoodsOrderActivity.class));
			break;
		case R.id.tv_setting_car_order:
			startActivity(new Intent(this, SettingCarsOrderActivity.class));
			break;

		default:
			break;
		}

	}
}
