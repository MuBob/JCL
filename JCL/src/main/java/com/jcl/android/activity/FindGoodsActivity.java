package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.FindLogisticsActivity.GetStr;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.bean.FindLogisticsNearBean;
import com.jcl.android.bean.FindLogisticsNearBean.Logistics;
import com.jcl.android.fragment.FindBGoodsByListFragment;
import com.jcl.android.fragment.FindBGoodsByNearbyFragment;
import com.jcl.android.fragment.ShowMapFragment;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.obj.PointInfo;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.NoScrollViewPager;
/**
 * 查找货源页面
 * @author msz
 *
 */
public class FindGoodsActivity extends BaseActivity implements OnPageChangeListener{
	
	private RadioGroup rg_tab;

	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private CheckBox cb_isMap;
	private ImageView iv_back;
	private TextView tv_kuaixun;
	private List<FindGoodsListBean.Goods> dataList  ;
	List<PointInfo> maplist = new ArrayList<PointInfo>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_find_goods);
		initView();
		dataList = new ArrayList<FindGoodsListBean.Goods>();
		loadData();

	}

	@SuppressWarnings("deprecation")
	private void initView() {
		cb_isMap = (CheckBox) findViewById(R.id.cb_isMap);
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
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
					startActivity(new Intent(FindGoodsActivity.this, PublishCarActivity.class));
				}else if(SharePerfUtil.getLinkMan().isEmpty()){
					if (SharePerfUtil.getSubmittype().equals("0")) {
						startActivity(new Intent(FindGoodsActivity.this, PersonalInfoActivity.class));
					} else {
						startActivity(new Intent(FindGoodsActivity.this, CompanyInfoActivity.class));
					}
					Toast.makeText(FindGoodsActivity.this, "请先完善信息，完善后才能发布信息",
							Toast.LENGTH_LONG).show();
				}else{
					startActivity(new Intent(FindGoodsActivity.this,LoginActivity.class));
				}
				
			}
		});
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
		cb_isMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb_isMap.setText("查看地图");
					viewPager.setCurrentItem(1);
				}else{
					cb_isMap.setText("查看列表");
					viewPager.setCurrentItem(2);
				}			}

		});
		
	}
	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		private String sort;// {"key1":"asc/desc"}排序
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.filters = filters;
			this.type = "2000";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}
	private int pagenum = 1;

	/**
	 * 列表查询数据
	 * 
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData() {
		// 获取经纬度
		BDLocation location = JCLApplication.getInstance().getMyLocation();
		String longitude = "", latitude = "";
		if (location != null) {
			longitude = location.getLongitude() + "";
			latitude = location.getLatitude() + "";
		}
		
		// 拼接filter中数据
		String filters = "EQ_userid:"
				+ ",longitude:" + longitude
				+ ",latitude:" + latitude;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		
		executeRequest(new GsonRequest<FindGoodsListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, FindGoodsListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<FindGoodsListBean>() {

					@Override
					public void onResponse(FindGoodsListBean arg0) {
						
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
									dataList.clear();
									dataList = arg0.getData();
									
									for(FindGoodsListBean.Goods logic : dataList){
										PointInfo pi = new PointInfo();
										pi.setId(logic.get_id());
										pi.setLat(logic.getLatitude());
										pi.setLng(logic.getLongitude());
										pi.setShowInfo(logic.getDetailname());
										pi.setType(C.INTENT_TYPE_FIND_GOODS);
										maplist.add(pi);
									}
							} 
						} 
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}));
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
				return new FindBGoodsByListFragment();
			case 1:
				return new FindBGoodsByNearbyFragment();
			case 2:
				return new ShowMapFragment(maplist);
			default:
				return new FindBGoodsByListFragment();
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 3;
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
		case 0:
			cb_isMap.setVisibility(View.GONE);
			break;
		case 1:
			cb_isMap.setVisibility(View.VISIBLE);
			break;
		case 2:
			cb_isMap.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		
	}

}
