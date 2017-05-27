package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 列表查询货物页面
 * 
 * @author syl 需实现两个接口，刷新相关
 *
 */
public class MyCollectGoodsFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;
	private List<FindGoodsListBean.Goods> dataList;
	private FindGoodsAdapter mAdapter;
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
	
	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
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
	
	class getFilters{
		private String EQ_userid;
		private String EQ_type;
		
		public getFilters(String EQ_type) {
			this.EQ_userid = JCLApplication.getInstance().getUserId();
			this.EQ_type = EQ_type;
			
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindGoodsListBean.Goods>();
		mAdapter = new FindGoodsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData();
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_GOODS, dataList.get(position)
									.get_id()));
				}
			}
		});
	}

	/**
	 * 列表查询数据
	 * 
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData() {
		//拼接filter中数据
//				String filters = new Gson().toJson(new getFilters("2"));
		String filters =  "userid:" + JCLApplication.getInstance().getUserId()
				+ ",type:"+ "2";
				String getStr = new Gson().toJson(new GetStr(pagenum, filters));
				//检测是否是第一页
				final boolean isFromTop = pagenum == 1;
				//添加刷新小图标显示
				if (!srLayout.isRefreshing()) {
					srLayout.setRefreshing(true);
				}
		executeRequest(new GsonRequest<FindGoodsListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, FindGoodsListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<FindGoodsListBean>() {

					@Override
					public void onResponse(FindGoodsListBean arg0) {
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
		loadData();
	}

	class FindGoodsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList != null ? dataList.size() : 0;
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
						R.layout.listitem_find_goods, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_chufashijian.setText("装货时间："+dataList.get(position)
					.getExfhstarttime()+ " - " + dataList.get(position).getExfhendtime());
			String chexing = "";
			if (TextUtils.isEmpty(dataList.get(position).getCartype())
					|| TextUtils.isEmpty(dataList.get(position).getCarlength())) {
				holder.tv_chexing.setVisibility(View.GONE);
			} else {
				holder.tv_chexing.setVisibility(View.VISIBLE);
			}
			
			String chechang = "";
			String[] chechangs = getResources().getStringArray(
					R.array.chechang);
			ArrayList<String> carlength = new ArrayList<String>();
			for (int i = 0; i < chechangs.length; i++) {
				carlength.add(chechangs[i]);
			}
			for (int i = 0; i < carlength.size(); i++) {
				if (dataList.get(position).getCarlength().equals(i+"")
					|| Integer.valueOf(dataList.get(position).getCarlength()) == i) {
					chechang = carlength.get(i);
				}
			}
			
			if (!TextUtils.isEmpty(dataList.get(position).getCarname())) {
				holder.tv_chexing.setVisibility(View.VISIBLE);
				holder.tv_chexing.setText(dataList.get(position)
						.getCarname()
						+ "   "
						+ "车长"+chechang);
			} else {
				holder.tv_goodsinfo.setVisibility(View.INVISIBLE);
			}

			if (!TextUtils.isEmpty(dataList.get(position).getDetailname())) {
				holder.tv_goodsinfo.setVisibility(View.VISIBLE);
				holder.tv_goodsinfo.setText(dataList.get(position)
						.getDetailname()
						+ " "
						+ dataList.get(position).getGoodsweight()
						+ "公斤  "
						+ dataList.get(position).getGoodstj()+"立方米");
			} else {
				holder.tv_goodsinfo.setVisibility(View.GONE);
			}
			
			holder.tv_time.setText(dataList.get(position).getCreatetime());
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi, tv_chufashijian, tv_chexing,
				tv_goodsinfo,tv_time;

		/**
		 * 封装holder获取方法
		 * 
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
			holder.tv_chufashijian = (TextView) convertView
					.findViewById(R.id.tv_chufashijian);
			holder.tv_chexing = (TextView) convertView
					.findViewById(R.id.tv_chexing);
			holder.tv_goodsinfo = (TextView) convertView
					.findViewById(R.id.tv_goodsinfo);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			return holder;
		}

	}

	/**
	 * 上拉刷新 page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		loadData();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		}

	}

}
