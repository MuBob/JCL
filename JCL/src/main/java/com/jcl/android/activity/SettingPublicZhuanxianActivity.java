package com.jcl.android.activity;

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.SettingPublicZhuanxianFragment;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 管理发布专线页面
 * 
 * @author msz
 *
 */
public class SettingPublicZhuanxianActivity extends BaseActivity {
	private MyUINavigationView uINavigationView;
	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_public_zhuanxian);
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
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(SharePerfUtil.getLinkMan().isEmpty()){
					if (SharePerfUtil.getSubmittype().equals("0")) {
						startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
					} else {
						startActivity(new Intent(getApplicationContext(), CompanyInfoActivity.class));
					}
					Toast.makeText(SettingPublicZhuanxianActivity.this, "请先完善信息，完善后才能发布信息",
							Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent();
					intent.setClass(SettingPublicZhuanxianActivity.this, PublicZhuanxianActivity.class);
					startActivity(intent);
				}
				
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
			return SettingPublicZhuanxianFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 1;
		}
	}

}
