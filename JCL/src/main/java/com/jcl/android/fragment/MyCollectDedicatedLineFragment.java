package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.FindDedicateCarsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * zhuanxian
 * @author msz
 * 需实现两个接口，刷新相关
 *
 */
public class MyCollectDedicatedLineFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private List<FindDedicateCarsListBean.Line> dataList;
	private FindCarsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_my_collect, container,
				false);
		initView();
		initNavigation();
		return root;
	}

	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		//添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
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
	
	private MyUINavigationView uINavigationView;
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView)root.findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
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
			this.type = "2016";
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
		loadData();
	}
	/**
	 * 列表查询数据
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData() {
		//拼接filter中数据
		String filters =  "userid:" + JCLApplication.getInstance().getUserId()
				+ ",type:"+ "4";
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
		loadData();
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
						R.layout.listitem_find_specialcar, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			String lineType="";
			String jingTing="";
			holder.btn_delete.setVisibility(View.GONE);
			holder.btn_edit.setVisibility(View.GONE);
			if(TextUtils.equals(dataList.get(position).getLinetype(), "0"))
			{
				lineType="直达线路:";
				jingTing="";
			}else{
				lineType="中转线路:";
				jingTing="经停:"+(dataList.get(position).getStopnames()==null?
						"":dataList.get(position).getStopnames());
			}
			holder.tv_chufadi.setText(lineType+dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_jingting.setText(jingTing);
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi, tv_jingting,btn_delete,btn_edit;
		ImageView img_call;
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
			holder.tv_jingting = (TextView) convertView
					.findViewById(R.id.tv_jingting);
			holder.img_call = (ImageView) convertView
					.findViewById(R.id.img_call);
			holder.btn_delete = (TextView) convertView
					.findViewById(R.id.btn_delete);
			holder.btn_edit = (TextView) convertView
					.findViewById(R.id.btn_edit);
			
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
		loadData();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}




}
