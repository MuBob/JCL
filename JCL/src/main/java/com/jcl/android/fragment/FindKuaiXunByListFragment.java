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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.DetilFindKuaiXunActivity;
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

public class FindKuaiXunByListFragment extends BaseFragment implements
    OnRefreshListener, OnLoadListener, OnClickListener{
	
	private View root;
	private ListView lv_find_by_list;

	private List<KuaiXunListBean.KuaiXunInfo> dataList;
	private CollectOthersAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private List<WhSpinner.Item> leibie;
	private EditText et_keyword;
	private Button btn_search,btn_today;
	private String fbtype = "",description = "",todaytime = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_find_kuaixun,
				container, false);
		initView();
		fbtype = "EQ_fbtype:0,";
		return root;
	}

	
	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		et_keyword = (EditText) root.findViewById(R.id.et_keyword);
		btn_search = (Button) root.findViewById(R.id.btn_search);
		btn_today = (Button) root.findViewById(R.id.btn_today);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<KuaiXunListBean.KuaiXunInfo>();
		mAdapter = new CollectOthersAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData(fbtype, description,todaytime);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String title,info,remark,tel;
								if (dataList != null && dataList.size() - 1 >= position) {
//					startActivity(DetailFindActivity.newInstance(getActivity(),
//							C.INTENT_TYPE_FIND_KUAIXUN, dataList.get(position)
//									.get_id()));
					if (dataList.get(position).getIshowremark().equals("1")) {
						//显示
						remark = "\n\n备注：" + dataList.get(position).getRemark();
					} else {
						remark = "";
					}
					tel = InfoUtils.formatPhone(dataList.get(position).getPublisher().getMobile());
									title = dataList.get(position).getDescription() + remark; 
									info = "发布时间："+dataList.get(position).getCreatetime()
											+"\n发布人："+dataList.get(position).getPublisher().getUname()
											+"\n发布人电话："+tel
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
//		String filters = "EQ_userid:"
//				+ JCLApplication.getInstance().getUserId();
		String filters = fbtype + description + createtime;
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
		loadData(fbtype, description,todaytime);
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
			if (!Utils.isEmpty(dataList.get(position).getFbtype()) && 
					dataList.get(position).getFbtype().equals("0")) {
				fabutype = "货源";
			} else {
				fabutype = "车源";
			}
			holder.tv_description.setText(dataList.get(position).getCreatetime()+"["+fabutype+"]"+
					dataList.get(position).getDescription());
			
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_description,tv_update,
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
		loadData(fbtype,description,todaytime);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			description = "LK_description:" + et_keyword.getText().toString() + ",";
			loadData(fbtype, description,todaytime);
			break;
			
		case R.id.btn_today:
			todaytime = "LK_createtime:"+Utils.getCurDate();
			loadData(fbtype, description,todaytime);
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
