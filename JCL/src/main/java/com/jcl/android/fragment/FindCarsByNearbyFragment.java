package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.GetLatLotActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.CarInfo;
import com.jcl.android.bean.CarListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.InfoUtils;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.ScrollListenerHorizontalScrollView.ScrollType;
import com.jcl.android.view.ScrollListenerHorizontalScrollView.ScrollViewListener;

public class FindCarsByNearbyFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private View ll_header;
	private List<CarInfo> dataList;
	private FindCarsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private TextView tv_noinfo;
	private TextView tv_changzhudi;
	private TextView tv_chexing;
	private TextView tv_carlength;
	private TextView tv_juli;
	private TextView tv_serch_car_location;//按车辆当前位置查找
	private com.jcl.android.view.ScrollListenerHorizontalScrollView  hsv_tab;
	private CityPickerPopupwindow cityPickerPopupwindow;
	
	private String chexing,chechang,changzhudi,juli;
	private String chexingcode,chechangcode,changzhudicode,julicode;
	private int cartype;
	private Bundle bundle;
	
	String[] chexingArr; //车型数组
	String[] chechangArr;//车长数组
	String[] juliArr;//距离数组
	
	
	private ImageButton img_more;
	private View ll_more;//
	private boolean isRight=false;
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) { 
             
             }   
             super.handleMessage(msg);   
        }   
   };
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_find_by_nearby, container,
				false);
		cartype = getArguments().getInt("cartype");
		initView();
		return root;
	}

	private void initView() {
		ll_more=root.findViewById(R.id.ll_more);
		ll_more.setOnClickListener(this);
		img_more=(ImageButton)root.findViewById(R.id.img_more);
		img_more.setOnClickListener(this);
		hsv_tab=(com.jcl.android.view.ScrollListenerHorizontalScrollView) 
				root.findViewById(R.id.hsv_tab);
		hsv_tab.setHandler(myHandler);
		hsv_tab.setOnScrollStateChangedListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(ScrollType scrollType) {
				// TODO Auto-generated method stub
				switch (scrollType) {
				case IDLE:
					View view = (View) hsv_tab.getChildAt(hsv_tab
                			.getChildCount() - 1);
                	int viewRight=view.getRight();
                	int subViewWidth = hsv_tab.getRight();

                	int hsv_tab_x = hsv_tab.getScrollX();
                	

                	if(hsv_tab_x+subViewWidth<viewRight-20)
                	{
                		isRight=true;
                		img_more.setImageDrawable(getResources().getDrawable(R.drawable.right));
                	}else{
                		isRight=false;
                		img_more.setImageDrawable(getResources().getDrawable(R.drawable.left));
                	}
                    break; 

				default:
					break;
				}
			}
			

		});
		ll_header=root.findViewById(R.id.ll_header);
		ll_header.setVisibility(View.VISIBLE);
		//数组数据初始化
		chexingArr=getResources().getStringArray(R.array.chexing);
		chechangArr=getResources().getStringArray(R.array.chechang);
		juliArr=getResources().getStringArray(R.array.juli);
		
		cityPickerPopupwindow = new CityPickerPopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), mHandler, null);
		
		tv_changzhudi=(TextView) root.findViewById(R.id.tv_changzhudi);
		tv_chexing=(TextView) root.findViewById(R.id.tv_chexing);
		tv_carlength=(TextView) root.findViewById(R.id.tv_carlength);
		tv_juli=(TextView) root.findViewById(R.id.tv_juli);
		tv_changzhudi.setOnClickListener(this);
		tv_chexing.setOnClickListener(this);
		tv_carlength.setOnClickListener(this);
		tv_juli.setOnClickListener(this);
		tv_serch_car_location=(TextView) root.findViewById(R.id.tv_serch_car_location);
		tv_serch_car_location.setOnClickListener(this);
		
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		
		tv_noinfo=(TextView)root.findViewById(R.id.tv_noinfo);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_NEAR_CAR,dataList.get(position).get_id()));
				}
			}
		});
		
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
		switch (cartype) {
		case 4:
			chexingcode = "0";
			tv_chexing.setText("集装箱车");
			break;
			
		case 5:
			chexingcode = "7";
			tv_chexing.setText("高栏车");
			break;
			
		case 6:
			chexingcode = "1";
			tv_chexing.setText("冷藏车");
			break;
			
		case 7:
			chexingcode = "4";
			tv_chexing.setText("平板车");
			break;
			
		case 8:
			chexingcode = "3";
			tv_chexing.setText("保温车");
			break;
			
		case 9:
			chexingcode = "2";
			tv_chexing.setText("厢式货车");
			break;

		default:
			break;
		}
		
		Log.e("syl", chexingcode+"   chexingcode");
		Log.e("syl", tv_chexing.getText().toString()+"   tv_chexing");
		loadData(changzhudicode,chexingcode,chechang,julicode,
				JCLApplication.getInstance().getMyLocation().getLongitude()+"",
				JCLApplication.getInstance().getMyLocation().getLatitude()+"");
		
	}
	
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = null;
			String info = "";
			if (msg.obj instanceof Bundle) {
				bundle = (Bundle) msg.obj;
				if (bundle != null)
					info = bundle.getString("cityName");
			} else {
				info = (String) msg.obj;
			}
			switch (msg.what) {
			case 1:
				changzhudicode = bundle.getString("cityCode");
				tv_changzhudi.setText(info);
				LogUtil.logWrite("MSZ_TAG", "chufadiCode==>" + changzhudicode);
				onRefresh();
				break;

			default:
				break;
			}
		}

	};

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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<CarInfo>();
		mAdapter = new FindCarsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
	
		
	}

	
	private String lng="",lat="";
	/**
	 * 列表查询数据
	 * 
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	
	private void loadData(String EQ_officeplacecode,String EQ_cartypecode,
			String EQ_carlength,String EQ_juli,String longitude,String latitude) {
		
		String filters = "EQ_officeplacecode:"+EQ_officeplacecode+",EQ_distancecode:"+EQ_juli
				+",EQ_cartypecode:"+EQ_cartypecode+",EQ_carlength:"+EQ_carlength
				+ ",longitude:" + longitude+ ",latitude:" + latitude;
		JCLApplication.getInstance().filters=filters;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}
		executeRequest(new GsonRequest<CarListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, CarListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<CarListBean>() {

					@Override
					public void onResponse(CarListBean arg0) {
						// 清除刷新小图标
						srLayout.setRefreshing(false);
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								if (isFromTop) {
									dataList.clear();
									dataList = arg0.getData();	
									mAdapter.notifyDataSetChanged();
									
								} else {
									dataList.addAll(arg0.getData());
									mAdapter.notifyDataSetChanged();
								}
								srLayout.setLoading(false);
								if(dataList==null)
								{
									tv_noinfo.setVisibility(View.VISIBLE);
								}
							} else {
								MyToast.showToast(getActivity(), "服务端异常");
							}
						} else {
							MyToast.showToast(getActivity(), "服务端异常");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						srLayout.setRefreshing(false);
//						MyToast.showToast(getActivity(), arg0.getMessage());
					}
				}));
	}

	/**
	 * 下拉刷新 page置为1 筛选完数据也执行该方法
	 */
	@Override
	public void onRefresh() {
		pagenum = 1;
		if("".equals(lng)||"".equals(lat)||lng==null||lat==null)
		{
			lng=JCLApplication.getInstance().getMyLocation().getLongitude()+"";
			lat=JCLApplication.getInstance().getMyLocation().getLatitude()+"";
		}
		loadData(changzhudicode,chexingcode,chechang,julicode,
				lng,lat);
	}
	class FindCarsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList!=null?dataList.size():0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.listitem_find_car, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_creattime.setText(dataList.get(position).getCreatetime());
			holder.tv_chufadi.setText(dataList.get(position).getOfficeplace()+"(常驻地)");
			holder.view_line.setVisibility(View.GONE);
			holder.tv_mudidi.setVisibility(View.GONE);
			holder.tv_gone.setVisibility(View.GONE);
			holder.tv_starttime.setText(dataList.get(position).getDistance());
			holder.tv_endtime.setVisibility(View.GONE);
			holder.view_line2.setVisibility(View.GONE);
			holder.tv_name.setText(dataList.get(position).getCompanyname());
			holder.tv_car_code.setText(InfoUtils.formatCarCode(dataList.get(position).getPlatenum()));
			LatLng m_start=new LatLng( Double.parseDouble(dataList.get(position).getLatitude()),
					Double.parseDouble(dataList.get(position).getLongitude()));
			
			LatLng m_end="".equals(JCLApplication.getInstance().pick_lat)||
					"".equals(JCLApplication.getInstance().pick_lng)
					?new LatLng(JCLApplication.getInstance().getMyLocation().getLatitude(), 
							JCLApplication.getInstance().getMyLocation().getLongitude()):
					new LatLng(Double.parseDouble(JCLApplication.getInstance().pick_lat),
							Double.parseDouble(JCLApplication.getInstance().pick_lng));			
			
			Double distance= (Utils.getDistance(m_start,m_end))/1000*1.3;
			holder.tv_price.setText("距离:"+new java.text.DecimalFormat("#.00").format(distance)+"km");
			
			
			holder.ll_tag.setVisibility(View.GONE);
			
			holder.btn_delete.setVisibility(View.GONE);
			holder.btn_resend.setVisibility(View.GONE);
			holder.btn_edit.setVisibility(View.GONE);
			ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+dataList.get(position).getCarimage1(), holder.img_car_header);
			
			String chexing = "";
				chexing=dataList.get(position).getCartype();
				if("集装箱车".equals(chexing))
				{
					holder.tv_chexing.setText(chexing);
				}else
				{
					holder.tv_chexing.setText(chexing + "\t"
							+ dataList.get(position).getCarlength() + "米");
				}
			
			
			if("1".equals(dataList.get(position).getIsauth())){
//				holder.iv_name_check.setVisibility(View.GONE);
				holder.iv_name_check.setImageDrawable(getResources().getDrawable(R.drawable.shi));
			}
			
			if("1".equals(dataList.get(position).getIscheck())){
				holder.iv_car_check.setImageDrawable(getResources().getDrawable(R.drawable.car));
//				holder.iv_car_check.setVisibility(View.GONE);
			}
			
			if(TextUtils.isEmpty(dataList.get(position).getPosition())){
				holder.tv_dangqian.setText("当前位置：用户没有上传当前位置");
			}else{
//				holder.tv_dangqian.setText("当前位置：("+dataList.get(position).getLongitude()+","+dataList.get(position).getLatitude()+")");
				holder.tv_dangqian.setText("当前位置："+dataList.get(position).getPosition());
			}
			return convertView;
		}

	}

	static class ViewHolder {
		TextView tv_dangqian;//当前位置

		TextView tv_name,tv_car_code,tv_price,tv_starttime,tv_endtime,tv_gone;
		TextView tv_chufadi, tv_mudidi, tv_chexing,tv_creattime;
		TextView btn_delete,btn_resend,btn_edit;
		ImageView img_car_header,img_call,img_check1,img_check2,iv_name_check,iv_car_check;
		View view_line,ll_tag,view_line2;
		TextView tv_jjdegrees,tv_cartype,tv_pingtai;
		/**
		 *封装holder获取方法
		 * @param convertView
		 * @return
		 */
		public static ViewHolder getViewHolder(View convertView) {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				convertView.setTag(holder);
			}
			
			holder.tv_dangqian = (TextView) convertView
			.findViewById(R.id.tv_dangqian);
			holder.tv_starttime = (TextView) convertView
					.findViewById(R.id.tv_starttime);
			holder.tv_endtime = (TextView) convertView
					.findViewById(R.id.tv_endtime);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_car_code = (TextView) convertView
					.findViewById(R.id.tv_car_code);
			holder.tv_chufadi = (TextView) convertView
					.findViewById(R.id.tv_chufadi);
			holder.tv_mudidi = (TextView) convertView
					.findViewById(R.id.tv_mudidi);
			holder.tv_chexing = (TextView) convertView
					.findViewById(R.id.tv_chexing);
			holder.tv_creattime = (TextView) convertView
					.findViewById(R.id.tv_creattime);
			holder.img_car_header = (ImageView) convertView
					.findViewById(R.id.img_car_header);
			holder.img_call = (ImageView) convertView
					.findViewById(R.id.img_call);
//			holder.img_check1 = (ImageView) convertView
//					.findViewById(R.id.img_check1);
//			holder.img_check2 = (ImageView) convertView
//					.findViewById(R.id.img_check2);
			holder.btn_delete = (TextView) convertView
					.findViewById(R.id.btn_delete);
			holder.btn_resend = (TextView) convertView
					.findViewById(R.id.btn_resend);
			holder.btn_edit = (TextView) convertView
					.findViewById(R.id.btn_edit);
			holder.tv_gone = (TextView) convertView
					.findViewById(R.id.tv_gone);
			
			holder.tv_jjdegrees = (TextView) convertView
					.findViewById(R.id.tv_jjdegrees);
			holder.tv_cartype = (TextView) convertView
					.findViewById(R.id.tv_cartype);
			holder.tv_pingtai = (TextView) convertView
					.findViewById(R.id.tv_pingtai);
			holder.view_line=convertView.findViewById(R.id.view_line);
			holder.view_line2=convertView.findViewById(R.id.view_line2);
			holder.ll_tag=convertView.findViewById(R.id.ll_tag);
			
			holder.iv_name_check = (ImageView) convertView
					.findViewById(R.id.img_check1);
			holder.iv_car_check = (ImageView) convertView
					.findViewById(R.id.img_check2);
			
			return holder;
		}

	}

	/**
	 * 上拉刷新 page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		if("".equals(lng)||"".equals(lat)||lng==null||lat==null)
		{
			lng=JCLApplication.getInstance().getMyLocation().getLongitude()+"";
			lat=JCLApplication.getInstance().getMyLocation().getLatitude()+"";
		}
		loadData(changzhudicode,chexingcode,chechang,julicode,
				lng,
				lat);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_changzhudi:
			if (cityPickerPopupwindow != null) 
				cityPickerPopupwindow.show(1,"search");
			break;
		case R.id.tv_chexing:
			
			showView("车型选择",v.getId(),chexingArr);
			break;
		case R.id.tv_carlength:
			
			showView("车长选择",v.getId(),chechangArr);
			break;
			
		case R.id.tv_juli:
			
			showView("距离选择",v.getId(),juliArr);
			break;
		case R.id.tv_serch_car_location:
			if("".equals(lng)||"".equals(lat)||lng==null||lat==null)
			{
				startActivityForResult(new Intent(getActivity(),GetLatLotActivity.class)
						.putExtra("lat", JCLApplication.getInstance().getMyLocation().getLatitude()+"").
						putExtra("lnt", JCLApplication.getInstance().getMyLocation().getLongitude()+"").
						putExtra("hasDialog", "1"), 10);
			}else{
				startActivityForResult(new Intent(getActivity(),GetLatLotActivity.class)
						.putExtra("lat", lat).
						putExtra("lnt", lng)
						.putExtra("hasDialog", "1"), 10);
			}
				
				
			
			
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 10:
			if(Activity.RESULT_OK==resultCode)
			{
				tv_serch_car_location.setText(data.getStringExtra("addr"));
				lng=data.getStringExtra("Lng");
				lat=data.getStringExtra("Lat");
				JCLApplication.getInstance().pick_lat=lat;
				JCLApplication.getInstance().pick_lng=lng;
				onLoad();
			}
			pagenum = 1;
			if("".equals(lng)||"".equals(lat)||lng==null||lat==null)
			{
				lng=JCLApplication.getInstance().getMyLocation().getLongitude()+"";
				lat=JCLApplication.getInstance().getMyLocation().getLatitude()+"";
			}
			loadData(changzhudicode,chexingcode,chechang,julicode,
					lng,lat);
			break;

		default:
			break;
		}
	}
	
	private String title = "选择";
	private Builder builder = null;

	private void showView(String title,final int viewId,String... po) {

		if (builder == null) {
			builder = new AlertDialog.Builder(getActivity());
		}

		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle(title)
				.setSingleChoiceItems(po, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								switch (viewId) {
								case R.id.tv_chexing:
									chexingcode = (item) + "";
									chexing=chexingArr[item];
									tv_chexing.setText(chexingArr[item]);
									onRefresh();
									break;
								case R.id.tv_carlength:
									chechangcode=(item)+"";
									chechang=chechangArr[item].substring(0, chechangArr[item].length()-1);
									if("不限".equals(chechangArr[item])){
										chechang="0";
									}
									
									tv_carlength.setText(chechangArr[item]);
									onRefresh();
									break;
								case R.id.tv_juli:
									julicode=(item)+"";
									tv_juli.setText(juliArr[item]);
									onRefresh();
									break;
								default:
									break;
								}
								
								dialog.dismiss();
							}
						}).show();
	}
}