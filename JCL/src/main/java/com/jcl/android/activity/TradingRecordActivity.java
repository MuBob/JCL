package com.jcl.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.ChongzhiRecordFragment;
import com.jcl.android.fragment.HomeFragment;
import com.jcl.android.fragment.PublicFragment;
import com.jcl.android.fragment.SettingGoodsOrderFragment;
import com.jcl.android.fragment.TixianRecordFragment;
import com.jcl.android.fragment.UserCenterFragment;
import com.jcl.android.fragment.WebViewFragment;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

/**
 * 交易记录
 * 
 * @author xueleiLin
 *
 */
public class TradingRecordActivity extends BaseActivity {

	private RadioGroup rg_tab;
	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private MyUINavigationView uINavigationView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_trading_record);
		initNavigation();
		initView();
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
				case R.id.tab_left2:
					viewPager.setCurrentItem(1);
					break;
				case R.id.tab_right2:
					viewPager.setCurrentItem(3);
					break;
				case R.id.tab_right:
					viewPager.setCurrentItem(2);
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
			switch (position) {
			case 0:
				//充值
				return ChongzhiRecordFragment.newInstance(position);
			case 1://提现
				return TixianRecordFragment.newInstance(position);
			case 2:
				//收入
				return ChongzhiRecordFragment.newInstance(position);
			case 3://支出
				return ChongzhiRecordFragment.newInstance(position);
			default:
				return ChongzhiRecordFragment.newInstance(position);
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 4;
		}
	}

}
