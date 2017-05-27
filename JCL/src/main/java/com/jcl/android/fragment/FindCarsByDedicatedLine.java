package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.FindDedicateCarsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.WhSpinner;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * zhuanxian
 * @author msz
 * 需实现两个接口，刷新相关
 *
 */
public class FindCarsByDedicatedLine extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private TextView tv_chufadi, tv_mudidi, tv_linetype;
	private List<FindDedicateCarsListBean.Line> dataList;
	private FindCarsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String chufadi = "", mudidi = "", linetype = "";

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;
	private TextView tv_noinfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_find_by_dedicated_line, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_linetype = (TextView) root.findViewById(R.id.tv_linetype);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		tv_noinfo=(TextView)root.findViewById(R.id.tv_noinfo);
		//添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
		tv_chufadi.setOnClickListener(this);
		tv_mudidi.setOnClickListener(this);
		tv_linetype.setOnClickListener(this);
		cityPickerPopupwindow = new CityPickerPopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), mHandler, null);
		datePickerPopupwindow = new DatePickerPopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), mHandler, null);
		
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_ZHUANXIAN,dataList.get(position).get_id()));
				}
				
			}
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
			this.type = "2012";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindDedicateCarsListBean.Line>();
		mAdapter = new FindCarsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData(chufadi, mudidi, linetype);
	}
	/**
	 * 列表查询数据
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData(String EQ_startareacode, String EQ_endareacode, String EQ_linetype) {
		//拼接filter中数据
		String filters =  "EQ_startcode:" + EQ_startareacode + ",EQ_endcode:"
				+ EQ_endareacode +  ",EQ_linetype:" + EQ_linetype ;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		//检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		//添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}
		executeRequest(new GsonRequest<FindDedicateCarsListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, FindDedicateCarsListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<FindDedicateCarsListBean>() {

					@Override
					public void onResponse(FindDedicateCarsListBean arg0) {
						//清除刷新小图标
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
 * 下拉刷新 page置为1
 * 筛选完数据也执行该方法
 */
	@Override
	public void onRefresh() {
		pagenum = 1;
		loadData(chufadi, mudidi, linetype);
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
						R.layout.listitem_find_lines, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			String lineType="";
			String jingTing="";
			holder.btn_delete.setVisibility(View.GONE);
			holder.btn_edit.setVisibility(View.GONE);
			holder.btn_top.setVisibility(View.GONE);
			holder.btn_resend.setVisibility(View.GONE);
			if(TextUtils.equals(dataList.get(position).getLinetype(), "0"))
			{
				lineType="直达:";
				jingTing="";
			}else{
				lineType="中转:";
				jingTing="经停:"+(dataList.get(position).getStopnames()==null?
						"":dataList.get(position).getStopnames());
			}
			holder.tv_chufadi.setText(lineType+dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_miaoshu.setText(dataList.get(position).getXl_description());
			holder.tv_name.setText(dataList.get(position).getLinkman());
			holder.tv_chengjiao.setText("0单");
			holder.tv_pingjia.setText("评价：暂无");
			if (Utils.isEmpty(dataList.get(position).getSale())) {
				holder.tv_tejia.setText("暂无");
			} else {
				holder.tv_tejia.setText(dataList.get(position).getSale());
			}
			

			if(TextUtils.isEmpty(dataList.get(position).getIsauth())){
				holder.tv_renzheng.setText("未认证");
			}else{
				if(!Utils.isEmpty(dataList.get(position).getIsauth())&&

					dataList.get(position).getIsauth().equals("2")){
	             holder.tv_renzheng.setText("已认证");
	        	}else if(!Utils.isEmpty(dataList.get(position).getIsauth())&&
						dataList.get(position).getIsauth().equals("1")){
	        		holder.tv_renzheng.setText("认证中");
	        	}else if(!Utils.isEmpty(dataList.get(position).getIsauth())&&
						dataList.get(position).getIsauth().equals("3")){
	        		holder.tv_renzheng.setText("未通过认证");
	        	}else{
	        		holder.tv_renzheng.setText("未认证");
	        	}
			}
			holder.tv_huiyuan.setText("普通用户");
			LatLng m_start = null, m_end = null;
			String distance;
			if (!Utils.isEmpty(dataList.get(position).getChufa_lat())&&!Utils.isEmpty(dataList.get(position).getMudi_lat())) {
				m_start=new LatLng( Double.parseDouble
						(dataList.get(position).getChufa_lat()), 
						Double.parseDouble(dataList.get(position).getChufa_lng()));
				m_end=new LatLng(Double.parseDouble
						(dataList.get(position).getMudi_lat()),
						Double.parseDouble(dataList.get(position).getMudi_lng()));
				distance= new java.text.DecimalFormat("#.00").format(Utils.getDistance(m_start,m_end))+"米";
			}else{
				distance = "0.00米";
			}
			if (!Utils.isEmpty(dataList.get(position).getAdminid()) 
					&& !dataList.get(position).getAdminid().equals("0")) {
				holder.img_quanzi.setVisibility(View.VISIBLE);
			}else{
				holder.img_quanzi.setVisibility(View.GONE);
			}
			if (!Utils.isEmpty(dataList.get(position).getOne_self()) && 
					dataList.get(position).getOne_self().equals("1")) {
				holder.iv_zijian.setVisibility(View.VISIBLE);
			} else {
				holder.iv_zijian.setVisibility(View.GONE);
			}
			holder.tv_juli.setText(distance);
			String usertype = "";
			if (Utils.isEmpty(dataList.get(position).getUser_type())) {
				usertype = "";
			} else {
				switch (dataList.get(position).getUser_type()) {
				case "0":
					usertype = "国内物流企业/车主";
					break;
				case "1":
					usertype = "发货企业/货主";
					break;
				case "2":
					usertype = "配货站";
					break;
				case "3":
					usertype = "国际物流企业/货代";
					break;
				case "4":
					usertype = "货车生产商";
					break;
				case "5":
					usertype = "仓储";
					break;


				default:
					break;
				}
			}
			holder.tv_leixing.setText(usertype);
			ImageLoaderUtil.getInstance(getActivity()).
			loadImage(C.BASE_URL+dataList.get(position).getZx_image(),
					holder.iv_pic);
			return convertView;
		}
	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi, tv_miaoshu,tv_name,tv_juli,
		tv_tejia,tv_chengjiao,tv_pingjia,tv_huiyuan,tv_renzheng,tv_leixing,
		btn_delete,btn_edit,btn_top,btn_resend;
		ImageView img_call,iv_pic,img_quanzi,iv_zijian;
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
			holder.tv_chufadi = (TextView) convertView
					.findViewById(R.id.tv_chufadi);
			holder.tv_mudidi = (TextView) convertView
					.findViewById(R.id.tv_mudidi);
			holder.tv_miaoshu = (TextView) convertView
					.findViewById(R.id.tv_miaoshu);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_tejia = (TextView) convertView
					.findViewById(R.id.tv_tejia);
			holder.tv_chengjiao = (TextView) convertView
					.findViewById(R.id.tv_chengjiao);
			holder.tv_juli = (TextView) convertView
					.findViewById(R.id.tv_juli);
			holder.tv_pingjia = (TextView) convertView
					.findViewById(R.id.tv_pingjia);
			holder.tv_huiyuan = (TextView) convertView
					.findViewById(R.id.tv_huiyuan);
			holder.tv_leixing = (TextView) convertView
					.findViewById(R.id.tv_leixing);
			holder.tv_renzheng = (TextView) convertView
					.findViewById(R.id.tv_renzheng);
			holder.img_call = (ImageView) convertView
					.findViewById(R.id.img_call);
			holder.img_quanzi = (ImageView) convertView
					.findViewById(R.id.img_quanzi);
			holder.iv_pic = (ImageView) convertView
					.findViewById(R.id.iv_pic);
			holder.btn_delete = (TextView) convertView
					.findViewById(R.id.btn_delete);
			holder.btn_edit = (TextView) convertView
					.findViewById(R.id.btn_edit);
			holder.btn_top = (TextView) convertView
					.findViewById(R.id.btn_top);
			holder.btn_resend = (TextView) convertView
					.findViewById(R.id.btn_resend);
			holder.iv_zijian = (ImageView) convertView
					.findViewById(R.id.iv_zijian);
			
			return holder;
		}

	}
	/**
	 * 上拉刷新
	 * page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		loadData(chufadi, mudidi, linetype);
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
				chufadi = bundle.getString("cityCode");
				LogUtil.logWrite("MSZ_TAG", "chufadiCode==>" + chufadi);
				tv_chufadi.setText(info);
				onRefresh();
				break;
			case 2:
				mudidi = bundle.getString("cityCode");
				LogUtil.logWrite("MSZ_TAG", "mudidiCode==>" + mudidi);
				tv_mudidi.setText(info);
				onRefresh();
				break;

			default:
				break;
			}
		}

	};

	private String title = "选择";
	private Builder builder = null;

	private void showView(String... po) {

		if (builder == null) {
			builder = new AlertDialog.Builder(getActivity());
		}

		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle(title)
				.setSingleChoiceItems(po, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								linetype = (item ) + "";
								if (item == 0) {
									tv_linetype.setText("直达");
								} else {
									tv_linetype.setText("中转");
								}
								onRefresh();
								dialog.dismiss();
							}
						}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.tv_chufadi:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"search");
			
			break;
		case R.id.tv_mudidi:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(2,"search");
			break;
		case R.id.tv_linetype:
			showView(new String[] { "直达", "中转"});
			break;

		default:
			break;
		}

	}

}
