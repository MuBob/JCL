package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jcl.android.R;
import com.jcl.android.activity.SettingPublicOtherActivity.SectionsPagerAdapter;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.SettingContactsFragment;
import com.jcl.android.fragment.SettingPublicOtherFragment;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

public class SettingContactsActivity extends BaseActivity {
	private MyUINavigationView uINavigationView;
	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_public_car);
		initNavigation();
		initView();
	}
	
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		uINavigationView.getTv_title().setText("联系人管理");
		btnRightText.setText("添加联系人");
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingContactsActivity.this,AddContactsActivity.class));
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
			return SettingContactsFragment.newInstance();
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 1;
		}
	}

}
