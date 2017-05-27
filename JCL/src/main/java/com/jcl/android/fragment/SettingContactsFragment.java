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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.AddContactsActivity;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.PublicOtherActivity;
import com.jcl.android.activity.PublicZhuanxianActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.WangDianBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;

public class SettingContactsFragment extends BaseFragment implements
      OnRefreshListener, OnLoadListener, OnClickListener{

	private View root;
	private ListView lv_find_by_list;

	private List<WangDianBean.AddressList> dataList;
	private ContactsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;

	public static SettingContactsFragment newInstance() {
		SettingContactsFragment f = new SettingContactsFragment();
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
		private String sorts;
		public GetStr(String filters) {
			this.filters = filters;
			this.type = "7000";
			this.sorts = sorts;
		}

	}
	class Filters 
	{
		private String EQ_userid;
		public Filters(String EQ_userid)
		{
			this.EQ_userid = EQ_userid;
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<WangDianBean.AddressList>();
		mAdapter = new ContactsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData();
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
//					startActivity(DetailFindActivity.newInstance(getActivity(),
//							C.INTENT_TYPE_FIND_OTHER, dataList.get(position)
//									.get_id()));
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
		String filters=new Gson().toJson
				(new Filters(JCLApplication.getInstance().getUserId()));
		String getStr = new Gson().toJson(new GetStr(filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}
		executeRequest(new GsonRequest<WangDianBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, WangDianBean.class, null,
				ParamsBuilder.getStrParams(getStr),
				new Listener<WangDianBean>() {
					@SuppressWarnings("null")
					@Override
					public void onResponse(final WangDianBean arg0) {
						// 清除刷新小图标
						if (arg0 != null) {
							srLayout.setRefreshing(false);
							if (TextUtils.equals(arg0.getCode(), "1")) {
								dataList = arg0.getData().getAddresslist();
								mAdapter = new ContactsAdapter();
								lv_find_by_list.setAdapter(mAdapter);
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
//						MyToast.showToast(MyCollectActivity.this, arg0.getMessage());
					}
				}));
	}

	/**
	 * 下拉刷新 page置为1 筛选完数据也执行该方法
	 */
	@Override
	public void onRefresh() {
		loadData();
	}

	class ContactsAdapter extends BaseAdapter {

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
						R.layout.listitem_setting_contacts, null);
			}
			final ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_name.setText(dataList.get(position)
					.getPname());
			holder.tv_tel.setText(dataList.get(position).getA_mobile());
			holder.tv_address.setText(dataList.get(position).getAddress_xx());

			holder.tv_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),
							AddContactsActivity.class);
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

		TextView  tv_address, tv_tel, tv_name, tv_update,
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
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_tel = (TextView) convertView
					.findViewById(R.id.tv_tel);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
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
//		pagenum++;
//		loadData();
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
			this.type = "11001";
			this.key = key;
		}
	}

}
