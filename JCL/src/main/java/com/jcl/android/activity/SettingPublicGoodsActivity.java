package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.SettingPublicGoodsFragment;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

/**
 * 管理发布货源页面
 * 
 * @author msz
 *
 */
public class SettingPublicGoodsActivity extends BaseActivity {

	private RadioGroup rg_tab;
	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private MyUINavigationView uINavigationView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting_public_goods);
		initNavigation();
		initView();
	}
	
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		//返回键
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//添加货源
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(SharePerfUtil.getLinkMan().isEmpty()){
					if (SharePerfUtil.getSubmittype().equals("0")) {
						startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
					} else {
						startActivity(new Intent(getApplicationContext(), CompanyInfoActivity.class));
					}
					Toast.makeText(SettingPublicGoodsActivity.this, "请先完善信息，完善后才能发布信息",
							Toast.LENGTH_LONG).show();
				}else{
					startActivity(new Intent(SettingPublicGoodsActivity.this,QuickPublicGoodsActivity.class));
				}
				
			}
		});
	}

	
	
	private void initView() {
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
		viewPager.setOffscreenPageLimit(3);
		rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_left:
					viewPager.setCurrentItem(0);
					break;
				case R.id.tab_center:
					viewPager.setCurrentItem(1);
					break;
				case R.id.tab_right:
					viewPager.setCurrentItem(9);
					break;
				default:
					viewPager.setCurrentItem(0);
					break;
				}

			}
		});
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
			return SettingPublicGoodsFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 3;
		}
	}

}
