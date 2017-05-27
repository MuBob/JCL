package com.jcl.android.popupwindow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.android.R;
import com.jcl.android.R.color;
import com.jcl.android.extend.citywheel.model.CityModel;
import com.jcl.android.extend.citywheel.model.DistrictModel;
import com.jcl.android.extend.citywheel.model.ProvinceModel;
import com.jcl.android.extend.citywheel.service.XmlParserHandler;

public class CityPickerPopupwindow extends PopupWindow implements
		OnClickListener {

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
	
	/**
	 * key - 省 value - 省编码
	 */
	protected Map<String, String> mProvinceCodeMap = new HashMap<String, String>();
	/**
	 * key - 省 value - 省经度
	 */
	protected Map<String, String> mProvinceLntMap = new HashMap<String, String>();
	/**
	 * key - 省 value - 省纬度
	 */
	protected Map<String, String> mProvinceLatMap = new HashMap<String, String>();
	/**
	 * key - 市 values - 市编码
	 */
	protected Map<String, String> mCitisCodeMap = new HashMap<String, String>();
	/**
	 * key - 市 values - 市经度
	 */
	protected Map<String, String> mCitisLntMap = new HashMap<String, String>();
	/**
	 * key - 市 values - 市纬度
	 */
	protected Map<String, String> mCitisLatMap = new HashMap<String, String>();
	/**
	 * key - 区 values - 区编码
	 */
	protected Map<String, String> mDistrictCodeMap = new HashMap<String, String>();
	/**
	 * key - 区 values - 区经度
	 */
	protected Map<String, String> mDistrictLntMap = new HashMap<String, String>();
	/**
	 * key - 区 values - 区纬度
	 */
	protected Map<String, String> mDistrictLatMap = new HashMap<String, String>();
	
	
	
	
	
	
	

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前省的经度
	 */
	protected String mCurrentProviceLat;
	/**
	 * 当前省的纬度
	 */
	protected String mCurrentProviceLnt;
	/**
	 * 当前省的code
	 */
	protected String mCurrentProviceCode;
	
	
	
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前市的经度
	 */
	protected String mCurrentCityLat;
	/**
	 * 当前市的纬度
	 */
	protected String mCurrentCityLnt;
	/**
	 * 当前市的code
	 */
	protected String mCurrentCityCode;
	
	
	
	
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";
	/**
	 * 当前区的经度
	 */
	protected String mCurrentDistrictLat;
	/**
	 * 当前区的纬度
	 */
	protected String mCurrentDistrictLnt;
	/**
	 * 当前区的code
	 */
	protected String mCurrentDistrictCode;

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";
	

	/**
	 * 解析省市区的XML数据
	 */

	private ListView mViewProvince;
	private ListView mViewCity;
	private ListView mViewDistrict;
	private Button mBtnConfirm, mBtnCancel;

	private Context context;
	private View parentView;
	private Handler myHandler;
	private View root;
	private int type;
	private String searchorsubmit;

	private CityAdapter provinceAdapter, cityAdapter, districtAdapter;
	class CityAdapter extends BaseAdapter {
		int mSelect = -1; // 选中项
		private int state = 0;
		
		public void changeSelected(int positon) { // 刷新方法
			state = 1;
			Log.i("firststate", state+"");
			if (positon != mSelect) {
				mSelect = positon;
				notifyDataSetChanged();
			}
		}
		private String[] infos;
		public CityAdapter(String... info) {
			this.infos = info;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.length;
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return infos[0];
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem_city_picker, null);
			TextView tv_info = (TextView) convertView
					.findViewById(R.id.tv_info);
			tv_info.setText(infos[position]);
			Log.i("secondstate", state+"");
			if (state == 1 && position == mSelect && !TextUtils.isEmpty(infos[position])) {
				tv_info.setBackgroundColor(context.getResources().getColor(
						color.gray));
			} else {
				tv_info.setBackgroundColor(Color.TRANSPARENT);
			}
			return convertView;
		}

	}

	public CityPickerPopupwindow(Context context, View parentView,
			final Handler myHandler, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.parentView = parentView;
		this.myHandler = myHandler;
		this.context = context;
		this.parentView = parentView;
		setTouchable(true);
		setOutsideTouchable(true);
		setFocusable(true);
		root = LayoutInflater.from(context).inflate(
				R.layout.popupwindow_city_picker, null);
		setUpViews();//init
		setUpListener();//监听
//		setUpData();
		initProvinceDatas();
		setContentView(root);
		initLayout();
		ColorDrawable dw = new ColorDrawable(0000000);
		setBackgroundDrawable(dw);
	}

	// 弹出动画、高度等设置
	private void initLayout() {
		// setAnimationStyle(R.style.anim_issue_popwindow);
		setHeight(LayoutParams.WRAP_CONTENT);
		setWidth(LayoutParams.MATCH_PARENT);
	}

	

	private void setUpViews() {
		mViewProvince = (ListView) root.findViewById(R.id.id_province);
		mViewCity = (ListView) root.findViewById(R.id.id_city);
		mViewDistrict = (ListView) root.findViewById(R.id.id_district);
		mBtnConfirm = (Button) root.findViewById(R.id.btn_confirm);
		mBtnCancel = (Button) root.findViewById(R.id.btn_cancel);
	}

	
	
	
	private void setUpListener() {
		// 添加change事件
		mViewProvince.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				updateCities(position,"ngcurr");
				provinceAdapter.changeSelected(position);
				mViewDistrict.setAdapter(null);
				//将当前市区置为空
				mCurrentDistrictName = "";
				mCurrentCityName="";
			}
		});
		// 添加change事件
		mViewCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				updateAreas(position,"");
				cityAdapter.changeSelected(position);
			}
		});
		// 添加change事件
		mViewDistrict.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[position];
				mCurrentDistrictLat=mDistrictLatMap.get(mCurrentDistrictName);
				mCurrentDistrictLnt=mDistrictLntMap.get(mCurrentDistrictName);
				mCurrentDistrictCode=mDistrictCodeMap.get(mCurrentDistrictName);
				districtAdapter.changeSelected(position);

			}
		});
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
	}

	public void show(int type,String searchorsubmit) {
		this.type = type;
		this.searchorsubmit = searchorsubmit;
		mCurrentDistrictCode=null;
		mCurrentCityCode=null;
		setUpData();
		showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
				0, 0);
	}
	
	/**
	 * 默认选中第一个省份，二级三级默认不选中，但确定的时候必须选中第一级和第二级，第三级可以不选
	 */
	private void setUpData() {
		Log.i("mZipcodeDatasMap:",mZipcodeDatasMap+"");
//		if (mZipcodeDatasMap != null) {
//			mZipcodeDatasMap.clear();
//		}
		//初始化
		provinceAdapter = new CityAdapter(mProvinceDatas);
		mViewProvince.setAdapter(provinceAdapter);
		mViewDistrict.setAdapter(null);
		mViewCity.setAdapter(null);
		//初始化省
//		updateProvince(0,"");
		//初始化市
//		updateCities(0,"");
		//updateAreas(0,"");
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas(int position,String flag) {
		
		
//		if(mCurrentProviceName == "" || mCurrentProviceName == null){
//			mCurrentProviceName = "北京";
//			position = 0;
//			mCurrentCityName="东城区";
//		}
//		Log.i("cityselect", "position:"+position+","+"mCurrentProviceName:"+mCurrentProviceName+",mCurrentCityName"+mCurrentCityName);
		String currcityname="";
		if(!"ngcurr".equals(flag)){
			mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[position];
			mCurrentCityLat=mCitisLatMap.get(mCurrentCityName);
			mCurrentCityLnt=mCitisLntMap.get(mCurrentCityName);
			mCurrentCityCode=mCitisCodeMap.get(mCurrentCityName);
		}else{
			mCurrentCityName ="";
			mCurrentCityLat="";
			mCurrentCityLnt="";
		}
		currcityname= mCitisDatasMap.get(mCurrentProviceName)[position];
		String[] areas = mDistrictDatasMap.get(currcityname);

		if (areas == null) {
			areas = new String[] { "" };
		}
		districtAdapter = new CityAdapter(areas);
		mViewDistrict.setAdapter(districtAdapter);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities(int position,String flag) {
		mCurrentProviceName = mProvinceDatas[position];
		mCurrentProviceLat=mProvinceLatMap.get(mCurrentProviceName);
		mCurrentProviceLnt=mProvinceLntMap.get(mCurrentProviceName);
		mCurrentProviceCode=mProvinceCodeMap.get(mCurrentProviceName);
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		cityAdapter = new CityAdapter(cities);
		mViewCity.setAdapter(cityAdapter);
		//updateAreas(0,flag);
	}
	/**
	 * 初始化省
	 */
	private void updateProvince(int position,String flag) {
		mCurrentProviceName = mProvinceDatas[position];
		mCurrentProviceLat=mProvinceLatMap.get(mCurrentProviceName);
		mCurrentProviceLnt=mProvinceLntMap.get(mCurrentProviceName);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			//判断至少选择省市两级
			if(TextUtils.isEmpty(mCurrentCityName)&&"submit".equals(searchorsubmit)){
				Toast.makeText(
						context,"至少选择省市两级,谢谢",Toast.LENGTH_SHORT).show();
				return;
			}
			mCurrentZipCode = mCurrentDistrictCode;
			if (TextUtils.isEmpty(mCurrentDistrictName)) {
				mCurrentZipCode = mCurrentCityCode;
				if(TextUtils.isEmpty(mCurrentCityCode)){
					mCurrentZipCode = mCurrentProviceCode;
				}
				mCurrentDistrictLnt=mCurrentCityLnt;
				mCurrentDistrictLat=mCurrentCityLat;
			}
			if (myHandler != null) {
				Message msg = myHandler.obtainMessage();
				msg.what = type;
				Bundle bundle = new Bundle();
				bundle.putString("cityName", mCurrentProviceName
						+ mCurrentCityName + mCurrentDistrictName);
				bundle.putString("cityCode", mCurrentZipCode);
				bundle.putString("sheng", mCurrentProviceName);
				bundle.putString("shi", mCurrentCityName);
				bundle.putString("qu", mCurrentDistrictName);
				
				bundle.putString("ProviceLnt", mCurrentProviceLnt);
				bundle.putString("ProviceLat", mCurrentProviceLat);
				bundle.putString("CityLnt", mCurrentCityLnt);
				bundle.putString("CityLat", mCurrentCityLat);
				bundle.putString("DistrictLnt", mCurrentDistrictLnt);
				bundle.putString("DistrictLat", mCurrentDistrictLat);
				msg.obj = bundle;
				myHandler.sendMessage(msg);
				mCurrentProviceName = "";
				mCurrentCityName = "";
				mCurrentDistrictName = "";
			}
			dismiss();
			break;

		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			dismiss();
			break;
		}
	}

	private void showSelectedResult() {
		Toast.makeText(
				context,
				"当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
						+ mCurrentDistrictName + "," + mCurrentZipCode,
				Toast.LENGTH_SHORT).show();
	}

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = context.getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				// 省的纬度，保存到mCitisLatMap
				mProvinceLatMap.put(provinceList.get(i).getName(),
						provinceList.get(i).getLatitude());
				// 省的经度，保存到mCitisLntMap
				mProvinceLntMap.put(provinceList.get(i).getName(),
						provinceList.get(i).getLongitude());
				//省code
				mProvinceCodeMap.put(provinceList.get(i).getName(),
						provinceList.get(i).getId());
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					// 市的纬度，保存到mCitisLatMap
					mCitisLatMap.put(cityList.get(j).getName(),
							cityList.get(j).getLatitude());
					// 市的经度，保存到mCitisLntMap
					mCitisLntMap.put(cityList.get(j).getName(),
							cityList.get(j).getLongitude());
					//市code
					mCitisCodeMap.put(cityList.get(j).getName(),
							cityList.get(j).getId());
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getId(),districtList
										.get(k).getLatitude(),districtList.get(k).getLongitude());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getId());
						// 区/县的纬度，保存到mCitisLatMap
						mDistrictLatMap.put(districtList.get(k).getName(),
								districtList.get(k).getLatitude());
						// 区/县的经度，保存到mCitisLntMap
						mDistrictLntMap.put(districtList.get(k).getName(),
								districtList.get(k).getLongitude());
						// 区/县的code，
						mDistrictCodeMap.put(districtList.get(k).getName(),
								districtList.get(k).getId());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					/*if (districtList != null && districtList.size() > 0) {
						if (TextUtils.isEmpty(districtList.get(0).getName())) {
							// 区/县对于的邮编，保存到mZipcodeDatasMap
							mZipcodeDatasMap.put(cityList.get(j).getName(),
									cityList.get(j).getId());
						}
					}*/
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
					
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
				
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

}
