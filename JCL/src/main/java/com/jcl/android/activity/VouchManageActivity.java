package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.GoodsListBean;
import com.jcl.android.bean.VouchsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 担保交易记录
 * 
 * @author syl
 * 
 *
 */
public class VouchManageActivity extends BaseActivity implements
		OnClickListener,OnRefreshListener, OnLoadListener {
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private MyUINavigationView uINavigationView;
	private ListView lv_vouchs;// 担保记录列表
	private List<VouchsListBean.VouchInfo> dataList;
	private VouchLisrAdapter vouchListrAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vouchs);
		initView();
		initNavigation();
	}


	private void initView() {
		lv_vouchs = (ListView) findViewById(R.id.lv_vouchs);
		srLayout = (BaseSwipeRefreshLayout)findViewById(R.id.sr_layout);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
		dataList = new ArrayList<>();
		vouchListrAdapter = new VouchLisrAdapter();
		lv_vouchs.setAdapter(vouchListrAdapter);
		
	}

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getInfo();
	}

	public class VouchListRequest {
		private String filters = "";
		private String type = "4007";
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public VouchListRequest(int pagenum,String filters) {
			this.filters = filters;
			this.pagenum = pagenum + "";
			this.pagesize = C.PAGE_LIMIT;
		}
	}

	public class VouchListFilters {
		private String EQ_userid;

		public VouchListFilters(String EQ_userid) {
			this.EQ_userid = EQ_userid;
		}
	}

	String vouchListFilters;
	String jsonRequest;

	private void getInfo() {
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
			}
		showLD("加载中...");
		vouchListFilters = "EQ_userid:"+JCLApplication.getInstance().getUserId();
		jsonRequest = new Gson().toJson(new VouchListRequest(pagenum,vouchListFilters));
		
		executeRequest(new GsonRequest<VouchsListBean>(Request.Method.GET,
				UrlCat.getPageSearchUrl(jsonRequest), VouchsListBean.class, null,
				null, new Listener<VouchsListBean>() {
					@Override
					public void onResponse(VouchsListBean arg0) {
						// 清除刷新小图标
						srLayout.setRefreshing(false);
						cancelLD();
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								if (arg0 != null) {
									setList(arg0.getData());
								}
							} else {
								Toast.makeText(VouchManageActivity.this,
										arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(VouchManageActivity.this, "暂无数据！",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						srLayout.setRefreshing(false);
						cancelLD();
						Toast.makeText(VouchManageActivity.this, "无法连接服务器！",
								Toast.LENGTH_SHORT).show();
					}
				}));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}


	public void setList(List<VouchsListBean.VouchInfo> list) {
		if (dataList != null) {
			dataList.clear();
			dataList = list;
		}
		
		vouchListrAdapter.notifyDataSetChanged();
	}

	class VouchLisrAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
				holder = new ViewHolder();
				convertView = LayoutInflater.from(VouchManageActivity.this)
						.inflate(R.layout.listitem_vouch_record, null);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_jine = (TextView) convertView
						.findViewById(R.id.tv_jine);
				holder.tv_status = (TextView) convertView
						.findViewById(R.id.tv_status);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			} else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
				holder = (ViewHolder) convertView.getTag();
			}
			VouchsListBean.VouchInfo vouchInfo = dataList.get(position);
			holder.tv_title.setText(vouchInfo.getStartarea()+" 至  "+vouchInfo.getEndarea());
			holder.tv_time.setText(vouchInfo.getCreatetime());
			holder.tv_jine.setText(vouchInfo.getPrice());
			String vouchstatus = "";
			switch (vouchInfo.getStatus()) {
			case "0":
				vouchstatus = "正常";
				break;
			case "1":
				vouchstatus = "完成";
				break;
			case "2":
				vouchstatus = "等待审核退款";//申请退款事件
				break;
			case "3":
				vouchstatus = "审核退款成功";
				break;
			case "4":
				vouchstatus = "审核退款失败";
				break;

			default:
				break;
			}
			holder.tv_status.setText(vouchstatus);

			return convertView;
		}

		public final class ViewHolder {

			public TextView tv_time;
			public TextView tv_jine;
			public TextView tv_status;
			public TextView tv_title;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList != null && dataList.size() > 0 ? dataList.size()
					: 0;
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

	}

	@Override
	public void onLoad() {
		pagenum++;
		getInfo();
	}


	@Override
	public void onRefresh() {
		pagenum = 1;
		getInfo();
	}
}
