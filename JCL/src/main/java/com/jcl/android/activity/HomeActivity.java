package com.jcl.android.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.LoginBean;
import com.jcl.android.bean.MessageNumBean;
import com.jcl.android.fragment.HomeFragment;
import com.jcl.android.fragment.MessageFragment;
import com.jcl.android.fragment.PublicFragment;
import com.jcl.android.fragment.UserCenterFragment;
import com.jcl.android.fragment.WebViewFragment;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.service.UpdateService;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.BadgeView;
import com.jcl.android.view.CircleView;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.NoScrollViewPager;

/**
 * 主页
 * 
 * @author msz
 *
 */
public class HomeActivity extends BaseActivity implements OnClickListener{

	private RadioGroup rg_tab;
	private static NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private MyUINavigationView uINavigationView;
	private static Button btnRightText;
	ImageButton btnLeftText;
//	private ImageButton btnLeftText;
	private static RadioButton tab_msg,tab_home,tab_fx,tab_public,tab_usercenter;
	private CircleView textView1;
	private JCLApplication myApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
		initNavigation();
		addListener();
		checkVersion();//版本检测
		
		
//	    IntentFilter intentFilter = new IntentFilter();  
//	    intentFilter.addAction("touchuan");  
//	    registerReceiver(new MyBroadcastReceiver(), intentFilter);  
	}
	
//	 class MyBroadcastReceiver extends BroadcastReceiver{  
//	        @Override  
//	        public void onReceive(Context context, Intent intent) {  
//	        	selectPaper(3);
//	        }  
//	    } 
//	private boolean istongzhi=false;
//	private void afterCreate() {
//		// TODO Auto-generated method stub	
//		if(getIntent().hasExtra("tongzhi"))
//		{
//			istongzhi=true;
//		}
//	}

	@Override
	protected void onResume() {
		super.onResume();
		if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
			btnRightText.setText("邀请");
			btnRightText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(HomeActivity.this,ZxingActivity.class));
				}
			});
		}else{
			btnRightText.setText("登录");
			btnRightText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(HomeActivity.this,
							LoginActivity.class));
				}
			});
		}
		
		loadMessageNum();
		loadPublicType();
	}

	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		btnRightText = uINavigationView.getBtn_right();
		btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setImageResource(R.drawable.icon_btn_server);
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ C.BASE_SERVICE_TEL));
				startActivity(intent);
			}
		});
	}
	
	public static void selectPaper(int check)
	{
		tab_home.setChecked(false);
        tab_fx.setChecked(false);
        tab_public.setChecked(false);
        tab_msg.setChecked(true);
        tab_usercenter.setChecked(false);
        viewPager.setCurrentItem(check);
//        btnRightText.setVisibility(View.GONE);
		JCLApplication.getInstance().homeItme=check;
	}

	private void initView() {
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		tab_msg = (RadioButton) findViewById(R.id.tab_msg);
		tab_home = (RadioButton) findViewById(R.id.tab_home);
		tab_fx = (RadioButton) findViewById(R.id.tab_fx);
		tab_public = (RadioButton) findViewById(R.id.tab_public);
		tab_usercenter = (RadioButton) findViewById(R.id.tab_usercenter);
		textView1 = (CircleView) findViewById(R.id.textView1);
				
//		afterCreate();
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
//		if(istongzhi)
//		{
//			selectPaper(3);
//		}
		tab_msg.setOnClickListener(this);
		tab_home.setOnClickListener(this);
		tab_fx.setOnClickListener(this);
		tab_public.setOnClickListener(this);
		tab_usercenter.setOnClickListener(this);
	}

	private void loadMessageNum(){
		String filters=new Gson().toJson(new GetFilters(JCLApplication.getInstance().getUserId()));
		String getStr = new Gson().toJson(new GetStr( filters));
		executeRequest(new GsonRequest<MessageNumBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, MessageNumBean.class, null,
				ParamsBuilder.getStrParams(getStr),
				new Listener<MessageNumBean>() {
					@Override
					public void onResponse(MessageNumBean arg0) {
						// 清除刷新小图标
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								Log.e("syl", arg0.getData().getCount());
								if (arg0.getData().getCount().equals("0")) {
									textView1.setVisibility(View.GONE);
								} else {
//									addPoint(tab_msg,arg0.getData().getCount());
									textView1.setVisibility(View.VISIBLE);
									textView1.setText(arg0.getData().getCount());
									textView1.setBackgroundColor(Color.RED); 
								 }
								} else {
								MyToast.showToast(HomeActivity.this, "服务端异常");
							}
						} else {
							MyToast.showToast(HomeActivity.this, "服务端异常");
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
//						MyToast.showToast(HomeActivity.this, arg0.getMessage());
					}
				}));
	}
	
    private void loadPublicType() {
    	if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
    	String filters = "_id:" + JCLApplication.getInstance().getUserId();
		String getStr = new Gson().toJson(new GetPublicStr( filters));
		executeRequest(new GsonRequest<LoginBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, LoginBean.class, null,
				ParamsBuilder.getStrParams(getStr),
				new Listener<LoginBean>() {
					@Override
					public void onResponse(LoginBean arg0) {
						// 清除刷新小图标
						if (arg0 != null) {
							SharePerfUtil.saveIcount(arg0.getData()
									.getIcount());
						} else {
							MyToast.showToast(HomeActivity.this, "服务端异常");
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
//						MyToast.showToast(HomeActivity.this, arg0.getMessage());
					}
				}));
		} else {

		}
	}

	public void addPoint(View view,String number)
	{
		BadgeView  badge1 = new BadgeView(HomeActivity.this, view);//创建一个BadgeView对象，view为你需要显示提醒的控件
        badge1.setText(number); //显示类容
        badge1.setBadgePosition(BadgeView.POSITION_CENTER);//显示的位置.中间
        badge1.setTextColor(Color.WHITE);  //文本颜色
        badge1.setBadgeBackgroundColor(Color.RED); //背景颜色
        badge1.setTextSize(13); //文本大小
        badge1.setBadgeMargin(20, 0); //水平和竖直方向的间距
        /**
         * 可以设置一个动画
         */
        TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
        anim.setInterpolator(new BounceInterpolator());
        anim.setDuration(1000);
        badge1.toggle(anim,null);
	}

	
	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		public GetStr(String filters) {
			this.filters = filters;
			this.type = "00112";
		}

	}
	
	class GetFilters {
		private String userid;
		private String isread;
		public GetFilters(String userid) {
			this.userid = userid;
			this.isread = "0";
		}
	}
	
	class GetPublicStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		public GetPublicStr(String filters) {
			this.filters = filters;
			this.type = "9000";
		}

	}
	
	 @SuppressWarnings("deprecation")
		private void addListener() {
	        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
	            @Override
	            public void onPageSelected(int id) {
	                switch (id) {
	                
	                    case 0:
	                        tab_home.setChecked(true);
	                        tab_fx.setChecked(false);
	                        tab_public.setChecked(false);
	                        tab_msg.setChecked(false);
	                        tab_usercenter.setChecked(false);
	                        break;
	                        
	                    case 1:
	                    	tab_home.setChecked(false);
	                        tab_fx.setChecked(true);
	                        tab_public.setChecked(false);
	                        tab_msg.setChecked(false);
	                        tab_usercenter.setChecked(false);
	                        break;
	                        
	                    case 2:
	                    	tab_home.setChecked(false);
	                        tab_fx.setChecked(false);
	                        tab_public.setChecked(true);
	                        tab_msg.setChecked(false);
	                        tab_usercenter.setChecked(false);
	                        break;
	                        
	                    case 3:
	                    	tab_home.setChecked(false);
	                        tab_fx.setChecked(false);
	                        tab_public.setChecked(false);
	                        tab_msg.setChecked(true);
	                        tab_usercenter.setChecked(false);
	                        break;
	                        
	                    case 4:
	                    	tab_home.setChecked(false);
	                        tab_fx.setChecked(false);
	                        tab_public.setChecked(false);
	                        tab_msg.setChecked(false);
	                        tab_usercenter.setChecked(true);
	                        break;

	                    default:
	                        break;
	                }
	            }

	            @Override
	            public void onPageScrolled(int arg0, float arg1, int arg2) {

	            }

	            @Override
	            public void onPageScrollStateChanged(int arg0) {

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
				//首页
				return new HomeFragment();
			case 1:
				return 
						WebViewFragment.newInstance(C.BASE_URL+"/found/userid="+JCLApplication.getInstance().getUserId());
			case 2:
				//发布
				return new PublicFragment();
			case 3:
				//消息
				return new MessageFragment();
				
//				return 
//						WebViewFragment.newInstance("http://115.28.164.187:8080/hsdata/msg");
				//个人中心
			case 4:
				return new UserCenterFragment();
			default:
				return new HomeFragment();
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 5;
		}
	}
	
	private long exitTime = 0; 
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
	if((System.currentTimeMillis()-exitTime) > 2000){ 
	Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show(); 
	exitTime = System.currentTimeMillis(); 
	} else { 
	finish(); 
//	System.exit(0); 
	Intent intent = new Intent(Intent.ACTION_MAIN);
	intent.addCategory(Intent.CATEGORY_HOME);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(intent);
	android.os.Process.killProcess(android.os.Process.myPid());
	} 
	return true; 
	} 
	return super.onKeyDown(keyCode, event); 
	} 
	/***
	 * 检查是否更新版本
	 */
	public void checkVersion() {
		myApplication = (JCLApplication) getApplication();
		String userinfoJson = SharePerfUtil.getLoginUserInfo();
		LoginBean loginBean = new Gson()
				.fromJson(userinfoJson, LoginBean.class);
		if (myApplication.localVersion < myApplication.serverVersion) {
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新使用.")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// 开启更新服务UpdateService
									// 这里为了把update更好模块化，可以传一些updateService依赖的值
									// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
									SharePerfUtil.put(SP.SP_ISLOGIN, false);
									Intent updateIntent = new Intent(
											HomeActivity.this,
											UpdateService.class);
									updateIntent.putExtra(
											"app_name",
											getResources().getString(
													R.string.app_name));
									startService(updateIntent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			alert.create().show();
		}
//		else if(loginBean!= null && "0".equals(loginBean.getData().getIsauth())){
//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
//			alert.setTitle("信息提示")
//					.setMessage("打造更加诚信物流环境,请先完善个人信息/企业信息，认证会员优先获得货源/车源信息.")
//					.setPositiveButton("去完善",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									if (TextUtils.equals("0", SharePerfUtil.getSubmittype())) {
//										startActivity(new Intent(HomeActivity.this, PersonalInfoActivity.class));
//									} else if (TextUtils.equals("1", SharePerfUtil.getSubmittype())) {
//										startActivity(new Intent(HomeActivity.this, CompanyInfoActivity.class));
//									}
//									
//								}
//							})
//					.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.dismiss();
//								}
//							});
//			alert.create().show();
//			
//		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_home:
			viewPager.setCurrentItem(0);
			btnRightText.setVisibility(View.VISIBLE);
//			btnLeftText.setVisibility(View.VISIBLE);
			break;
		case R.id.tab_fx:
			viewPager.setCurrentItem(1);
			btnRightText.setVisibility(View.GONE);
//			btnLeftText.setVisibility(View.GONE);
			break;
		case R.id.tab_public:
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				viewPager.setCurrentItem(2);
				btnRightText.setVisibility(View.GONE);
//				btnLeftText.setVisibility(View.GONE);
			}else {
				//未登录
				startActivity(new Intent(HomeActivity.this,LoginActivity.class));
			}
			
			break;
		case R.id.tab_msg:
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				viewPager.setCurrentItem(3);
				btnRightText.setVisibility(View.GONE);
				JCLApplication.getInstance().homeItme=3;
//				btnLeftText.setVisibility(View.GONE);
			}else {
				//未登录
				startActivity(new Intent(HomeActivity.this,LoginActivity.class));
			}
			break;
			
		case R.id.tab_usercenter:
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				//已登录
				viewPager.setCurrentItem(4);
				btnRightText.setVisibility(View.GONE);
//				btnLeftText.setVisibility(View.GONE);
			} else {
				//未登录
				startActivity(new Intent(HomeActivity.this,LoginActivity.class));
			}
			break;
			
		default:
			btnRightText.setVisibility(View.VISIBLE);
//			btnLeftText.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(0);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		unregisterReceiver(new MyBroadcastReceiver());
	}
}
