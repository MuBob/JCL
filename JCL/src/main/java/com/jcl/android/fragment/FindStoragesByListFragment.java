package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.bean.FindStoragesListBean;
import com.jcl.android.fragment.FindStoragesByNearbyFragment.FindStoragesAdapter;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;

/**
 * 列表查询仓储公司页面
 * 
 * @author xuelei 需实现两个接口，刷新相关
 *
 */
public class FindStoragesByListFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private EditText et_companyname;
	private List<FindStoragesListBean.Storage> dataList;
	private FindStoragesAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String companyname;

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;

	private ImageView iv_search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_findstorage_by_list, container,
				false);
		initView();
		companyname = "";
		loadData(companyname);
		return root;
	}

	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		et_companyname = (EditText) root.findViewById(R.id.et_companyname);
		iv_search = (ImageView) root.findViewById(R.id.iv_search);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		iv_search.setOnClickListener(this);
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
			this.type = "2010";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindStoragesListBean.Storage>();
		mAdapter = new FindStoragesAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_STORAGE,dataList.get(position).get_id()));
				}
			}
		});
	}

	/**
	 * 列表查询数据
	 */
	private void loadData(String LK_company) {
		String longitude = "", latitude = "";
		// 拼接filter中数据
		String filters;
		if (Utils.isEmpty(LK_company)) {
			filters = "LK_company:";
		} else {
			filters = "LK_company:" + LK_company;
		}
				
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}
		executeRequest(new GsonRequest<FindStoragesListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, FindStoragesListBean.class,
				null,ParamsBuilder.pageSearchParams(getStr),
				new Listener<FindStoragesListBean>() {

					@Override
					public void onResponse(FindStoragesListBean arg0) {
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
		loadData(companyname);
	}
	
	
    /**
     * 仓储列表适配器
     * @author xuelei
     *
     */
	class FindStoragesAdapter extends BaseAdapter {

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
						R.layout.listitem_find_storage, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_name.setText(dataList.get(position).getZhname());
			holder.tv_address.setText(dataList.get(position).getAddress());
			holder.tv_linkman.setText(dataList.get(position).getLinkman());
			holder.tv_phone.setText(dataList.get(position).getPhone());
			final String phone = dataList.get(position).getPhone();
			holder.img_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));  
	                startActivity(intent);
				}
			});
			return convertView;
		}

	}
    /**
     * 仓储列表适配器
     * @author xuelei
     *
     */
	static class ViewHolder {

		TextView tv_name, tv_address, tv_linkman, tv_phone;
		ImageView img_call;

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
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.tv_linkman = (TextView) convertView
					.findViewById(R.id.tv_linkman);
			holder.tv_phone = (TextView) convertView
					.findViewById(R.id.tv_phone);
			holder.img_call = (ImageView)convertView.findViewById(R.id.img_call);
			return holder;
		}

	}

	/**
	 * 上拉刷新 page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		loadData(companyname);
	}

	//查询
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			if (et_companyname.getText().toString().isEmpty()) {
				Toast.makeText(getActivity(), "请输入公司名称", Toast.LENGTH_SHORT).show();
			} else {
				companyname = et_companyname.getText().toString();
				loadData(companyname);
				onRefresh();
			}
			
			break;

		default:
			break;
		}

	}

}
