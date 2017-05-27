package com.jcl.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.FindBGoodsByListFragment;
import com.jcl.android.fragment.FindBGoodsByNearbyFragment;
import com.jcl.android.fragment.FindOthersByListFragment;
import com.jcl.android.fragment.ShowMapFragment;
import com.jcl.android.view.NoScrollViewPager;
/**
 * 查找其他页面
 * @author msz
 *
 */
public class FindOthersActivity extends BaseActivity implements OnPageChangeListener{
	

	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ImageView iv_back;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_find_others);
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
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
				return new FindOthersByListFragment();
			default:
				return new FindOthersByListFragment();
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 1;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {

		default:
			break;
		}
		
	}

}
