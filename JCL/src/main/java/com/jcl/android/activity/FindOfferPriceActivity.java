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

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.FindCarsByListFragment;
import com.jcl.android.fragment.OfferPriceListFragment;
import com.jcl.android.fragment.SettingPublicGoodsFragment;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

/**
 * 查看报价
 * 
 * @author xuelei
 *
 */
public class FindOfferPriceActivity extends BaseActivity {

	private RadioGroup rg_tab;
	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private MyUINavigationView uINavigationView;
	private String goodsid;
	private String chufadi;
	private String mudidi;
	private String chexing;
	private String chufashijian;
	private String detailgood;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_find_offer_price);
		initNavigation();
		initView();
		Intent intent = this.getIntent();
		goodsid = intent.getStringExtra("goodsid");
		chufadi = intent.getStringExtra("chufadi");
		mudidi = intent.getStringExtra("mudidi");
		chexing = intent.getStringExtra("chexing");
		chufashijian = intent.getStringExtra("chufashijian");
		detailgood = intent.getStringExtra("detailgood");
	}
	//初始化标题栏
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
	}

	private void initView() {
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
		viewPager.setOffscreenPageLimit(2);
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
			//设置tab跳转
			switch (position) {
			case 0:
				return OfferPriceListFragment.newInstance(goodsid,chufadi,mudidi,chexing,chufashijian,detailgood);
			case 1:
				return new FindCarsByListFragment();
			default:
				return OfferPriceListFragment.newInstance(goodsid,chufadi,mudidi,chexing,chufashijian,detailgood);
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 2;
		}
	}

}
