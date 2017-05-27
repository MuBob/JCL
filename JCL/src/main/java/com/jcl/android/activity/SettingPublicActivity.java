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
 * 管理发布页面
 * @author msz
 *
 */
public class SettingPublicActivity extends BaseActivity implements OnClickListener{
	private MyUINavigationView uINavigationView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_public);
		initNavigation();
		initView();
	}

	private void initView() {
		findViewById(R.id.tv_setting_public_goods).setOnClickListener(this);
		findViewById(R.id.tv_setting_public_car).setOnClickListener(this);
		findViewById(R.id.tv_setting_public_zhuanxian).setOnClickListener(this);
		findViewById(R.id.tv_setting_public_other).setOnClickListener(this);
		findViewById(R.id.tv_setting_public_kuaixun).setOnClickListener(this);
		
	}
	
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_setting_public_goods:
			startActivity(new Intent(this,SettingPublicGoodsActivity.class));
			break;
		case R.id.tv_setting_public_car:
			startActivity(new Intent(this,SettingPublicCarsActivity.class));
			break;
		case R.id.tv_setting_public_zhuanxian:
			startActivity(new Intent(this,SettingPublicZhuanxianActivity.class));
			break;

		case R.id.tv_setting_public_other:
			startActivity(new Intent(this,SettingPublicOtherActivity.class));
			break;

		case R.id.tv_setting_public_kuaixun:
			startActivity(new Intent(this,SettingPublicKuaiXunActivity.class));
			break;
		default:
			break;
		}
		
	}

}
