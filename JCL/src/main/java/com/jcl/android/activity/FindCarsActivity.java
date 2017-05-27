package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.CarInfo;
import com.jcl.android.bean.CarListBean;
import com.jcl.android.fragment.FindCarsByDedicatedLine;
import com.jcl.android.fragment.FindCarsByListFragment;
import com.jcl.android.fragment.FindCarsByNearbyFragment;
import com.jcl.android.fragment.ShowMapFragment;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.obj.PointInfo;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.NoScrollViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * 查找车源页面
 * @author msz
 *
 */
public class FindCarsActivity extends BaseActivity implements OnPageChangeListener{
	
	private RadioGroup rg_tab;

	private NoScrollViewPager viewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private CheckBox cb_isMap;
	private ImageView im_back;
	private Bundle bundle;
	private int cartype;
	private RadioButton rb_car,rb_nearcar,rb_zhuanxian;
	private List<CarInfo> dataList;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_find_cars);
		dataList=new ArrayList<CarInfo>();
		initView();
		loadData();
	}
	
	
	private int pagenum = 1;
	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		private String sort;// {"key1":"asc/desc"}排序
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.filters = filters;
			this.type = "100502";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}
	String filters;
	/**
	 * 列表查询数据
	 * 
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData() {
		
		if("".equals(JCLApplication.getInstance().filters))
		{
			// 获取经纬度
			BDLocation location = JCLApplication.getInstance().getMyLocation();
			String longitude = "", 
					latitude = "";
			if (location != null) {
				longitude = location.getLongitude() + "";
				latitude = location.getLatitude() + "";
			}
			filters =  "longitude:" + longitude+ ",latitude:" + latitude;
		}else{
			filters=JCLApplication.getInstance().filters;
		}
		
		
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		executeRequest(new GsonRequest<CarListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, CarListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<CarListBean>() {

					@Override
					public void onResponse(CarListBean arg0) {
						if (arg0 != null) {
							
							if (TextUtils.equals(arg0.getCode(), "1")) {
								if (isFromTop) {
									dataList=new ArrayList<CarInfo>();
									dataList = arg0.getData();	
									
								} else {
									dataList.addAll(arg0.getData());
								}
							} else {
								MyToast.showToast(FindCarsActivity.this, "服务端异常");
							}
						} else {
							MyToast.showToast(FindCarsActivity.this, "服务端异常");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						MyToast.showToast(FindCarsActivity.this, "服务端异常");
					}
				}));

	}

	@SuppressWarnings("deprecation")
	private void initView() {
		Intent intent = getIntent();
		bundle = intent.getExtras();
		cartype = bundle.getInt("cartype");
		cb_isMap = (CheckBox) findViewById(R.id.cb_isMap);
		im_back=(ImageView)findViewById(R.id.im_back);
		im_back.setOnClickListener(new ImageView.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		viewPager = (NoScrollViewPager) findViewById(R.id.pager);
		viewPager.setNoScroll(true);
		
		viewPager.setOffscreenPageLimit(1) ;
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mSectionsPagerAdapter);
		viewPager.setOnPageChangeListener(this);
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rb_car = (RadioButton) findViewById(R.id.rb_findbylist);
		rb_nearcar = (RadioButton) findViewById(R.id.rb_findbynearby);
		rb_zhuanxian = (RadioButton) findViewById(R.id.rb_finddedicatedline);
		
		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_finddedicatedline:
					viewPager.setCurrentItem(0);
					break;
				case R.id.rb_findbylist:
					viewPager.setCurrentItem(1);
					break;
				case R.id.rb_findbynearby:
					viewPager.setCurrentItem(3);
					break;
				default:
					break;
				}
			}
		});
		
		if (cartype == 0) {
			rb_car.setChecked(false);
			rb_nearcar.setChecked(false);
			rb_zhuanxian.setChecked(true);
		} else {
			rb_car.setChecked(true);
			rb_nearcar.setChecked(false);
			rb_zhuanxian.setChecked(false);
		}
		cb_isMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb_isMap.setText("查看地图");
					viewPager.setCurrentItem(3);
				}else{
					cb_isMap.setText("查看列表");
					viewPager.setCurrentItem(2);
				}
			}

		});
		
	}
	
	public android.support.v4.app.Fragment findFragmentByPosition(int position) {
		return getSupportFragmentManager().findFragmentByTag(
				"android:switcher:" + viewPager.getId() + ":"
						+ mSectionsPagerAdapter.getItemId(position));
	}

	private double centerLat=36.109159;
	private double cemterLng=120.415433;
	private List<PointInfo> list;
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
				return new FindCarsByDedicatedLine();
				
			case 1:
				return new FindCarsByListFragment();
				
			case 2:
				if(dataList==null)
				{
					dataList=new ArrayList<CarInfo>();
				}
				list=new ArrayList<PointInfo>();
				for (int i = 0; i < dataList.size(); i++) {
					PointInfo info=new PointInfo();
					info.setLat(dataList.get(i).getLatitude());
					info.setLng(dataList.get(i).getLongitude());
					info.setShowInfo(dataList.get(i).getPlatenum());
					info.setId(dataList.get(i).get_id());
					info.setType(C.INTENT_TYPE_FIND_NEAR_CAR);
					list.add(info);
				}
				return new ShowMapFragment(list);
				
			case 3:
				FindCarsByNearbyFragment nearFragment = new FindCarsByNearbyFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("cartype", cartype);
				nearFragment.setArguments(bundle);
				return nearFragment;
				
			default:
				return new FindCarsByListFragment();
			}
		}

		@Override
		public int getCount() {
			// 设置主页tab数量
			return 4;
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
			cb_isMap.setVisibility(View.GONE);
			break;
		case 2:
			cb_isMap.setVisibility(View.VISIBLE);
			break;
		case 3:
			cb_isMap.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}

}
