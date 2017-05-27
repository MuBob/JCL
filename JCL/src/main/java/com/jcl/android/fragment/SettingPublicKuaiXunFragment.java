package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
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
import com.jcl.android.activity.DetilFindKuaiXunActivity;
import com.jcl.android.activity.PublicKuaiXunActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.KuaiXunListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.InfoUtils;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.WhSpinner;

public class SettingPublicKuaiXunFragment extends BaseFragment implements
    OnRefreshListener, OnLoadListener, OnClickListener{

	private View root;
	private ListView lv_find_by_list;

	private List<KuaiXunListBean.KuaiXunInfo> dataList;
	private CollectOthersAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	
	private WhSpinner ws_leibie;
	private List<WhSpinner.Item> leibie;
	private EditText et_keyword;
	private Button btn_search,btn_today;
	private String fbtype,description,createtime;
	private LinearLayout ll_search;

	public static SettingPublicKuaiXunFragment newInstance() {
		SettingPublicKuaiXunFragment f = new SettingPublicKuaiXunFragment();
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
		root = inflater.inflate(R.layout.fragment_setting_public_kuaixun,
				container, false);
		initView();
		initWhSpinnerData();
		return root;
	}

	public void initWhSpinnerData() {
		leibie = new ArrayList<WhSpinner.Item>();
		String[] fabuleibies = getResources().getStringArray(
				R.array.kuaixun);
		for (int i = 0; i < fabuleibies.length; i++) {
			leibie.add(new WhSpinner.Item(fabuleibies[i], i));
		}
		ws_leibie.setItems(leibie, 0);
	}

	
	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		et_keyword = (EditText) root.findViewById(R.id.et_keyword);
		btn_search = (Button) root.findViewById(R.id.btn_search);
		btn_today = (Button) root.findViewById(R.id.btn_today);
		ws_leibie = (WhSpinner) root.findViewById(R.id.ws_leibie);
		ll_search = (LinearLayout) root.findViewById(R.id.ll_search);
//		ll_search.setVisibility(View.GONE);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		btn_search.setOnClickListener(this);
		btn_today.setOnClickListener(this); 
		
	}
 
	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.filters = filters;
			this.type = "6000";
			this.pagesize = C.PAGE_LIMIT;
		}

	}
	
	class GetFilters {
		private String EQ_userid;// 对应表名
		private String EQ_fbtype;// 过滤条件
		private String LK_description;// 1,页码
		private String LK_createtime;// 10每页显示的条数

		public GetFilters(String EQ_userid,String EQ_fbtype,
				String LK_description,String LK_createtime) {
			this.EQ_userid = EQ_userid;
			this.EQ_fbtype = EQ_fbtype;
			this.LK_description = LK_description;
			this.LK_createtime = LK_createtime;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<KuaiXunListBean.KuaiXunInfo>();
		mAdapter = new CollectOthersAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData("","","");
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title,info,remark;
								if (dataList != null && dataList.size() - 1 >= position) {
					if (dataList.get(position).getIshowremark().equals("1")) {
						//显示
						remark = "\n\n备注：" + dataList.get(position).getRemark();
					} else {
						remark = "";
					}
									
									title = dataList.get(position).getDescription() + remark; 
									info = "发布时间："+dataList.get(position).getCreatetime()
											+"\n发布人："+dataList.get(position).getPublisher().getUname()
											+"\n发布人电话："+InfoUtils.formatPhone(dataList.get(position).getPublisher().getMobile())
											+"\n发布人ID："+dataList.get(position).getPublisher().getId()
											+"\n发布地址："+dataList.get(position).getPublisher().getAddress()
											+"\n注册类型："+dataList.get(position).getPublisher().getType()
											+"\n会员级别："+dataList.get(position).getPublisher().getLevel()+"    "+
											dataList.get(position).getPublisher().getIsauth();

					Intent intent = new Intent(getActivity(),DetilFindKuaiXunActivity.class);
					intent.putExtra("title",title);
					intent.putExtra("info", info);
					intent.putExtra("tel", dataList.get(position).getPublisher().getMobile());
					startActivity(intent);

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
	private void loadData(String fbtype,String description,String createtime) {
		// 拼接filter中数据
		String filters = "EQ_userid:"
				+ JCLApplication.getInstance().getUserId()
				+ ",EQ_fbtype:"+fbtype + ",LK_description:"+description ;
//		String filters = new Gson().toJson(new GetFilters
//				(JCLApplication.getInstance().getUserId(),
//						fbtype, description, createtime));
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}

		executeRequest(new GsonRequest<KuaiXunListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, KuaiXunListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<KuaiXunListBean>() {

					@Override
					public void onResponse(KuaiXunListBean arg0) {
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
		loadData("","","");
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
						R.layout.listitem_find_kuaixun, null);
			}
			final ViewHolder holder = ViewHolder.getViewHolder(convertView);
			String remark,fabutype;
//			if (dataList.get(position).getRemark().isEmpty()
//					|| dataList.get(position).getRemark().equals("null")
//					|| dataList.get(position).getRemark() == "null") {
//				remark = "";
//			} else {
//				remark = "     备注："+dataList.get(position).getRemark();
//			}
			if (dataList.get(position).getFbtype().equals("0")) {
				fabutype = "货源";
			} else {
				fabutype = "车源";
			}
			holder.tv_description.setText(dataList.get(position).getCreatetime()+"["+fabutype+"]"+
					dataList.get(position).getDescription());
			holder.ll_bottom.setVisibility(View.VISIBLE);
			
			holder.tv_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),
							PublicKuaiXunActivity.class);
					intent.putExtra("id", dataList.get(position).get_id());
					intent.putExtra("fbtype", dataList.get(position).getFbtype());
					intent.putExtra("description", dataList.get(position).getDescription());
					intent.putExtra("remark", dataList.get(position).getRemark());
					intent.putExtra("type", "3");
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
											String jsonRequest = new Gson().toJson(publicGoodsRequest);
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

		TextView tv_description,tv_update,
		tv_delete;
		LinearLayout ll_bottom;

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
			holder.tv_update = (TextView) convertView
					.findViewById(R.id.tv_update);
			holder.tv_delete = (TextView) convertView
					.findViewById(R.id.tv_delete);
			holder.ll_bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);
			
			return holder;
		}

	}

	/**
	 * 上拉刷新 page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		loadData(fbtype,description,createtime);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_search:
//			lv_find_by_list.removeAllViews();
//			dataList.clear();
			fbtype = ws_leibie.getChoicePosition()+"";
			description = et_keyword.getText().toString();
			loadData(fbtype, description,createtime);
			break;
			
		case R.id.btn_today:
			createtime = Utils.getCurDate();
			loadData(fbtype, description,createtime);
			break;
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
			this.type = "6001";
			this.key = key;
		}
	}

}
