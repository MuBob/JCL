package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.AddGoodsActivity;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.GoodsManageActivity;
import com.jcl.android.activity.PublicOtherActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.EvaluteListBean;
import com.jcl.android.bean.EvaluteListBean.EvaluteInfo;
import com.jcl.android.bean.OtherListBean;
import com.jcl.android.fragment.SettingPublicGoodsFragment.PublicGoodsRequest;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;

/**
 * 列表查询评价
 * 
 * @author syl
 * 
 */

public class FindEvalutesFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private List<EvaluteInfo> dataList;
	private CollectOthersAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String vanuserid;

	public static FindEvalutesFragment newInstance(String vanuserid) {
		FindEvalutesFragment f = new FindEvalutesFragment();
		Bundle args = new Bundle();
		args.putString("fbrid", vanuserid);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		vanuserid = bundle.getString("fbrid");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_find_evalutes,
				container, false);
		initView();
		return root;
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
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.filters = filters;
			this.type = "5001";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<EvaluteListBean.EvaluteInfo>();
		mAdapter = new CollectOthersAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData();
//		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (dataList != null && dataList.size() - 1 >= position) {
//					startActivity(DetailFindActivity.newInstance(getActivity(),
//							C.INTENT_TYPE_FIND_OTHER, dataList.get(position)
//									.get_id()));
//				}
//			}
//		});

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
		// 拼接filter中数据
		String filters = "EQ_vanuserid:"
				+ vanuserid;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}

		executeRequest(new GsonRequest<EvaluteListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, EvaluteListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<EvaluteListBean>() {

					@Override
					public void onResponse(EvaluteListBean arg0) {
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
						// MyToast.showToast(getActivity(), arg0.getMessage());
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

	class CollectOthersAdapter extends BaseAdapter {

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.listitem_find_evalute, null);
			}
			final ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_name.setText(dataList.get(position)
					.getName());
			holder.tv_pjnr.setText(dataList.get(position).getEvaluteremark());
			holder.tv_fs.setText(dataList.get(position).getEvalutescore()+"分");
			holder.tv_time.setText(dataList.get(position).getCreatedate());

			


			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_name, tv_pjnr, tv_fs, tv_time;

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
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_pjnr = (TextView) convertView
					.findViewById(R.id.tv_pjnr);
			holder.tv_fs = (TextView) convertView
					.findViewById(R.id.tv_fs);
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

	class PublicGoodsRequest {
		private String operate;
		private String type;
		private String key;

		public PublicGoodsRequest(String key) {
			this.operate = "R";
			this.type = "1010";
			this.key = key;
		}
	}

}
