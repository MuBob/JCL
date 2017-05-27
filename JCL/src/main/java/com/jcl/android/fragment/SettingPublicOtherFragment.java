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
import com.jcl.android.bean.OtherListBean;
import com.jcl.android.fragment.SettingPublicGoodsFragment.PublicGoodsRequest;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;

/**
 * 列表查询其他页面
 * 
 * @author syl
 * 
 */

public class SettingPublicOtherFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private List<OtherListBean.OtherInfo> dataList;
	private CollectOthersAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;

	public static SettingPublicOtherFragment newInstance() {
		SettingPublicOtherFragment f = new SettingPublicOtherFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_setting_public_others,
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
			this.type = "2004";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<OtherListBean.OtherInfo>();
		mAdapter = new CollectOthersAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData();
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_OTHER, dataList.get(position)
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
		// 拼接filter中数据
		String filters = "EQ_userid:"
				+ JCLApplication.getInstance().getUserId();
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}

		executeRequest(new GsonRequest<OtherListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, OtherListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<OtherListBean>() {

					@Override
					public void onResponse(OtherListBean arg0) {
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
						R.layout.listitem_find_other, null);
			}
			final ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_description.setText(dataList.get(position)
					.getDescription());
			holder.tv_ywtype.setText(dataList.get(position).getYwtype());
			holder.tv_content.setText(dataList.get(position).getDescription());
			holder.tv_time.setText(dataList.get(position).getCreatetime());

			holder.tv_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),
							PublicOtherActivity.class);
					intent.putExtra("info", dataList.get(position));
					intent.putExtra("type", "2");
					startActivity(intent);
				}
			});

			final AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
			holder.tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					d.setTitle("删除")
							.setMessage("确定删除？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String key = dataList.get(position)
													.get_id();
											PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(
													key);
											String jsonRequest = new Gson()
													.toJson(publicGoodsRequest);
											showLD("删除中...");
											executeRequest(new GsonRequest<BaseBean>(
													Request.Method.POST,
													UrlCat.URL_SUBMIT,
													BaseBean.class,
													null,
													ParamsBuilder
															.submitParams(jsonRequest),
													new Listener<BaseBean>() {
														@Override
														public void onResponse(
																BaseBean arg0) {
															if (arg0 != null) {
																if (TextUtils
																		.equals(arg0
																				.getCode(),
																				"1")) {
																	MyToast.showToast(
																			getActivity(),
																			"删除成功");
																	dataList.remove(position);
																	mAdapter.notifyDataSetChanged();
																} else {
																	MyToast.showToast(
																			getActivity(),
																			"删除失败");
																}
															}

															cancelLD();
														}
													}, new ErrorListener() {

														@Override
														public void onErrorResponse(
																VolleyError arg0) {
															cancelLD();
															MyToast.showToast(
																	getActivity(),
																	"删除失败");
														}
													}));
										}

									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									}).create().show();

				}
			});

			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_description, tv_ywtype, tv_content, tv_time, tv_update,
				tv_delete;

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
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.tv_ywtype = (TextView) convertView
					.findViewById(R.id.tv_ywtype);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_update = (TextView) convertView
					.findViewById(R.id.tv_update);
			holder.tv_delete = (TextView) convertView
					.findViewById(R.id.tv_delete);
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
