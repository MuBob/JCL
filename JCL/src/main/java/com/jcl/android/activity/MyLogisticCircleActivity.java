package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.jcl.android.R;
import com.jcl.android.activity.SettingPublicOtherActivity.SectionsPagerAdapter;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.LogisticsCircleFragment;
import com.jcl.android.fragment.MyContactFragment;
import com.jcl.android.fragment.SettingPublicOtherFragment;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

public class MyLogisticCircleActivity extends BaseActivity {
	private MyUINavigationView uINavigationView;
	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private String type;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_public_car);
		initNavigation();
		initView();
	}
	
	private void initNavigation() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		type = bundle.getString("type");
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		    //我的经纪人
		if (type.equals("1")) {
			uINavigationView.getTv_title().setText("我的经纪人");
			//我的司机
		} else if(type.equals("2")){
			uINavigationView.getTv_title().setText("我的司机");
			//我的客户
		}else if(type.equals("3")){
			uINavigationView.getTv_title().setText("我的客户");
			btnRightText.setVisibility(View.GONE);
		}else{
			//联系人
			uINavigationView.getTv_title().setText("联系人");
		}
		
//		btnRightText.setText("添加");
		btnRightText.setVisibility(View.GONE);
		
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyLogisticCircleActivity.this,
						PublicOtherActivity.class));
			}
		});
	}

	private void initView() {
		
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
		
	}

	public android.support.v4.app.Fragment findFragmentByPosition(int position) {
		return getSupportFragmentManager().findFragmentByTag(
				"android:switcher:" + viewPager.getId() + ":"
						+ mSectionsPagerAdapter.getItemId(position));
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			if (type.equals("4")) {
				return MyContactFragment.newInstance();
			} else {
				return LogisticsCircleFragment.newInstance();
			}
			
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 1;
		}
	}

}

