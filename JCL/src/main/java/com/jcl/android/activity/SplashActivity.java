package com.jcl.android.activity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.activity.MyWalletActivity.UserAccountFilters;
import com.jcl.android.activity.MyWalletActivity.UserAccountRequest;
import com.jcl.android.activity.PersonalInfoActivity.UserinfoRequest;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.UserAccountBean;
import com.jcl.android.bean.VersionBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;

/**
 * 
 * @{# SplashActivity.java Create on 2013-5-2 下午9:10:01
 * 
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方式
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)2s后执行(2)操作
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author pb
 * 
 */
public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟4�?
	private static final long SPLASH_DELAY_MILLIS = 2000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	
	/**
	 * Handler:跳转到不同界�?
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.logStringCache = Utils.getLogText(getApplicationContext());
		setContentView(R.layout.splash);
		init();
		JCLApplication.localVersion = JCLApplication.getVersionCode(this);
		JCLApplication.serverVersion = JCLApplication.getVersionCode(this);
		//获取服务器版本信息
		loadServerVersion();
	}
	
	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的�?�，如果没有该�?�，说明还未写入，用true作为默认�?
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法�?3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}
	
	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
	
	//version
	class VersionRequest {
		private String filters;
		private String type;

		public VersionRequest(String filters) {
			this.filters = filters;
			this.type = "5000";
		}
	}

	class versionFilters {
		private String type;

		public versionFilters(String type) {
			this.type = "android";
		}

	}
	String filters;
	String sorts;
	String jsonRequest;
	public void loadServerVersion(){
		filters = new Gson().toJson(new versionFilters("android"));
		jsonRequest = new Gson().toJson(new VersionRequest(filters));

		executeRequest(new GsonRequest<VersionBean>(Request.Method.GET,
				UrlCat.getInfoUrl(jsonRequest), VersionBean.class,
				null, null, new Listener<VersionBean>() {
					@Override
					public void onResponse(VersionBean arg0) {
						if (arg0 != null) {
							JCLApplication.serverVersion = Integer.parseInt(arg0.getData().getVersionname());
						} else {
							Toast.makeText(SplashActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(SplashActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}
}
