package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.utils.SharePerfUtil;


/**
 * 
 * @{# GuideActivity.java Create on 2013-5-2 下午10:59:08
 * 
 *     class desc: 引导界面
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author pb
 * 
 * 
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	// 底部小点图片
	private ImageView[] dots;
	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.guide);

		// 初始化页�?
		initViews();

		// 初始化底部小�?
		initDots();
		
		initconfig();
	}

	//给推送  播报 设置默认开启 只设置一次
	private void initconfig()
	{
		SharePerfUtil.put(SP.SP_IS_OPEN_PUSH, true);
		SharePerfUtil.put(SP.SP_IS_OPEN_SPEECH, true);
	}
	
	
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列�?
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);
		
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰�?
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即�?�中状�??
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调�?
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	// 当新的页面被选中时调�?
	@Override
	public void onPageSelected(int arg0) {
		// 设置底部小点选中状�??
		setCurrentDot(arg0);
	}
	
	public class ViewPagerAdapter extends PagerAdapter {

			// 界面列表
			private List<View> views;
			private Activity activity;
	
			private static final String SHAREDPREFERENCES_NAME = "first_pref";
	
			public ViewPagerAdapter(List<View> views, Activity activity) {
				this.views = views;
				this.activity = activity;
			}
	
			// �?毁arg1位置的界�?
			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPager) arg0).removeView(views.get(arg1));
			}
	
			@Override
			public void finishUpdate(View arg0) {
			}
	
			// 获得当前界面�?
			@Override
			public int getCount() {
				if (views != null) {
					return views.size();
				}
				return 0;
			}
	
			// 初始化arg1位置的界�?
			@Override
			public Object instantiateItem(View arg0, int arg1) {
				((ViewPager) arg0).addView(views.get(arg1), 0);
				if (arg1 == views.size() - 1) {
					ImageView mStartWeiboImageButton = (ImageView) arg0
							.findViewById(R.id.iv_start_weibo);
					mStartWeiboImageButton.setOnClickListener(new OnClickListener() {
	
						@Override
						public void onClick(View v) {
							// 设置已经引导
							setGuided();
							goHome();
	
						}
	
					});
				}
				return views.get(arg1);
			}
	
			private void goHome() {
				// 跳转
				Intent intent = new Intent(activity, HomeActivity.class);
				activity.startActivity(intent);
				activity.finish();
			}
	
			/**
			 * 
			 * method desc：设置已经引导过了，下次启动不用再次引导
			 */
			private void setGuided() {
				SharedPreferences preferences = activity.getSharedPreferences(
						SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				// 存入数据
				editor.putBoolean("isFirstIn", false);
				// 提交修改
				editor.commit();
			}
	
			// 判断是否由对象生成界�?
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return (arg0 == arg1);
			}
	
			@Override
			public void restoreState(Parcelable arg0, ClassLoader arg1) {
			}
	
			@Override
			public Parcelable saveState() {
				return null;
			}
	
			@Override
			public void startUpdate(View arg0) {
				
			}
	}

}
