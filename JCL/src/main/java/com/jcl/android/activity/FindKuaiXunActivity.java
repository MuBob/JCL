package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.FindKuaiXunByListCarFragment;
import com.jcl.android.fragment.FindKuaiXunByListFragment;
import com.jcl.android.fragment.FindOthersByListFragment;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

public class FindKuaiXunActivity extends BaseActivity implements OnPageChangeListener{

	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private MyUINavigationView uINavigationView;
	private RadioGroup rg_tab;
	private ImageView iv_back;
	private TextView tv_kuaixun;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_find_kuaixun);
		initView();
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_kuaixun = (TextView) findViewById(R.id.tv_kuaixun);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		tv_kuaixun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
					startActivity(new Intent(FindKuaiXunActivity.this, PublicKuaiXunActivity.class));
				}else if(SharePerfUtil.getLinkMan().isEmpty()){
					if (SharePerfUtil.getSubmittype().equals("0")) {
						startActivity(new Intent(FindKuaiXunActivity.this, PersonalInfoActivity.class));
					} else {
						startActivity(new Intent(FindKuaiXunActivity.this, CompanyInfoActivity.class));
					}
					Toast.makeText(FindKuaiXunActivity.this, "请先完善信息，完善后才能发布信息",
							Toast.LENGTH_LONG).show();
				}else{
					startActivity(new Intent(FindKuaiXunActivity.this,LoginActivity.class));
				}
				
			}
		});
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.rb_findbylist){
					viewPager.setCurrentItem(0);
				}else if(checkedId == R.id.rb_findbynearby){
					viewPager.setCurrentItem(1);
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
				return new FindKuaiXunByListFragment();
				
			case 1:
				return new FindKuaiXunByListCarFragment();
			default:
				return new FindKuaiXunByListFragment();
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 2;
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
