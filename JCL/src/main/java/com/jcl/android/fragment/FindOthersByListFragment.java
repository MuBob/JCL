package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.jcl.android.bean.OtherListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 列表查询其他页面
 * 
 *  需实现两个接口，刷新相关
 *
 */
public class FindOthersByListFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private TextView tv_fabutype, tv_yewutype;
	private List<OtherListBean.OtherInfo> dataList;
	private FindOthersAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String fabutype = "", yewutype = "";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_findothers_by_list, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		tv_yewutype = (TextView) root.findViewById(R.id.tv_yewutype);
		tv_fabutype = (TextView) root.findViewById(R.id.tv_fabutype);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);

		tv_fabutype.setOnClickListener(this);
		tv_yewutype.setOnClickListener(this);
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
			this.type = "2004";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}
	}
	
	class GetFilters {
		private String EQ_userid;
		private String EQ_fbtype;
		private String EQ_ywtype;

		public GetFilters(String EQ_fbtype, String EQ_ywtype) {
			this.EQ_userid = JCLApplication.getInstance().getUserId();
			this.EQ_fbtype = EQ_fbtype;
			this.EQ_ywtype = EQ_ywtype;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<OtherListBean.OtherInfo>();
		mAdapter = new FindOthersAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData(fabutype,yewutype);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_OTHER,dataList.get(position).get_id()));
//					Intent intent = new Intent(getActivity(),PublicOtherActivity.class);
//					intent.putExtra("info", dataList.get(position));
//					intent.putExtra("type", "3");
//					startActivity(intent);
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
	private void loadData(String EQ_fbtype, String EQ_ywtype) {
		String longitude = "", latitude = "";
		// 拼接filter中数据
		String filters = "EQ_userid:" + ",EQ_fbtype:" + EQ_fbtype
				+ ",EQ_ywtype:" + EQ_ywtype;
//		String filters = new Gson().toJson(new GetFilters(EQ_fbtype, EQ_ywtype));
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
		loadData(fabutype,yewutype);
	}

	class FindOthersAdapter extends BaseAdapter {

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
						R.layout.listitem_find_other, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_description.setText(dataList.get(position).getDescription());
//			String[] fabuleibies = getResources().getStringArray(
//					R.array.fabuleibie);
			String[] yewuleibies = getResources().getStringArray(
					R.array.yewuleibie);
			if (Utils.isEmpty(dataList.get(position).getYwtype())) {
				holder.tv_ywtype.setText("");
			}else{
			holder.tv_ywtype.setText(yewuleibies[Integer.valueOf(dataList.get(position).getYwtype())]);
			}
			holder.tv_content.setText(dataList.get(position).getDescription());
			holder.tv_time.setText(dataList.get(position).getCreatetime());
			holder.ll_bottom.setVisibility(View.GONE);
			return convertView;
		}
	}

	static class ViewHolder {

		TextView tv_description, tv_ywtype, tv_content,tv_time;
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
			holder.tv_ywtype = (TextView) convertView
					.findViewById(R.id.tv_ywtype);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.ll_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_bottom);
			
			return holder;
		}

	}

	/**
	 * 上拉刷新 page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		loadData(fabutype,yewutype);
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
			

			default:
				break;
			}
		}

	};

	private String title = "选择";
	private Builder builder = null;

	private void showView(final int type,final boolean isMore, final String... po) {

		if (builder == null) {
			builder = new AlertDialog.Builder(getActivity());
		}

		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle(title)
				.setSingleChoiceItems(po, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (!isMore) {
									switch (type) {
									case 1:
										fabutype = item + "";
										tv_fabutype.setText(po[item]);
										loadData(fabutype,yewutype);
										break;
										
									case 2:
										yewutype = item + "";
										tv_yewutype.setText(po[item]);
										loadData(fabutype,yewutype);
										break;

									default:
										break;
									}
									
								} else {
									// TO-DO
								}
								onRefresh();
								dialog.dismiss();
							}
						}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_fabutype:
			showView(1,false, new String[] { "服务", "需求" });
			break;
		case R.id.tv_yewutype:
			showView(2,false, getResources().getStringArray(R.array.yewuleibie));
			break;

		default:
			break;
		}

	}

}
