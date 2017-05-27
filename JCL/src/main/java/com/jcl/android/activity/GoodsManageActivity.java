package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.GoodsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 管理货物页面
 * 
 * @author syl
 * 
 *
 */
public class GoodsManageActivity extends BaseActivity implements
		OnClickListener,OnRefreshListener, OnLoadListener {
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private MyUINavigationView uINavigationView;
	private ListView lv_goods;// 货物列表
	private List<GoodsListBean.GoodsInfo> dataList;
	private View no_list;
	private Button add_goods;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mygoods);
		afterCreate();
		initView();
		initNavigation();
	}

	boolean isForCode =false;
	public void afterCreate()
	{
		if(getIntent().hasExtra("key"))
		{
			if(C.FOR_CAR_CODE==getIntent().getIntExtra("key", 0))
			{
				isForCode=true;
			}
		}
	}

	private void initView() {
		lv_goods = (ListView) findViewById(R.id.lv_goods);
		srLayout = (BaseSwipeRefreshLayout)findViewById(R.id.sr_layout);
		no_list = findViewById(R.id.no_list);
		add_goods = (Button) findViewById(R.id.add_goods);
		add_goods.setOnClickListener(this);
		// 添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
		dataList = new ArrayList<>();
				
			}

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Utils.isEmpty(SharePerfUtil.getLinkMan())){
					if (SharePerfUtil.getSubmittype().equals("0")) {
						startActivity(new Intent(GoodsManageActivity.this, PersonalInfoActivity.class));
					} else {
						startActivity(new Intent(GoodsManageActivity.this, CompanyInfoActivity.class));
					}
					Toast.makeText(GoodsManageActivity.this, "请先完善信息，完善后才能发布信息",
							Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent(GoodsManageActivity.this,
							AddGoodsActivity.class);
					intent.putExtra("type", "1");
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getInfo();
	}

	public class GoodsListRequest {
		private String Filters = "";
		private String type = "1002";
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GoodsListRequest(int pagenum,String Filters) {
			this.Filters = Filters;
			this.pagenum = pagenum + "";
			this.pagesize = C.PAGE_LIMIT;
		}
	}

	public class GoodsListFilters {
		private String EQ_userid;

		public GoodsListFilters(String EQ_userid) {
			this.EQ_userid = EQ_userid;
		}
	}

	String carListFilters;
	String jsonRequest;

	private void getInfo() {
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
			}
		showLD("加载中...");
		carListFilters = new Gson().toJson(new GoodsListFilters(JCLApplication
				.getInstance().getUserId()));
		jsonRequest = new Gson().toJson(new GoodsListRequest(pagenum,carListFilters));
		
		executeRequest(new GsonRequest<GoodsListBean>(Request.Method.GET,
				UrlCat.getSearchUrl(jsonRequest), GoodsListBean.class, null,
				null, new Listener<GoodsListBean>() {
					@Override
					public void onResponse(GoodsListBean arg0) {
						// 清除刷新小图标
						srLayout.setRefreshing(false);
						cancelLD();
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
									setList(arg0.getData());
							} else {
								Toast.makeText(GoodsManageActivity.this,
										arg0.getMsg(), 1000).show();
							}
							
						} else {
							Toast.makeText(GoodsManageActivity.this, "暂无数据！",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						srLayout.setRefreshing(false);
						cancelLD();
						Toast.makeText(GoodsManageActivity.this, "无法连接服务器！",
								Toast.LENGTH_SHORT).show();
					}
				}));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_goods:
			if(Utils.isEmpty(SharePerfUtil.getLinkMan())){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(GoodsManageActivity.this, PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(GoodsManageActivity.this, CompanyInfoActivity.class));
				}
				Toast.makeText(GoodsManageActivity.this, "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				Intent intent = new Intent(GoodsManageActivity.this,
						AddGoodsActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	private GoodsLisrAdapter goodsLisrAdapter;

	public void setList(List<GoodsListBean.GoodsInfo> list) {
		if (list.size() > 0) {
			if (dataList != null) {
				dataList.clear();
				dataList = list;
				goodsLisrAdapter = new GoodsLisrAdapter();
				lv_goods.setAdapter(goodsLisrAdapter);
				
			}else{
				goodsLisrAdapter.notifyDataSetChanged();
			}
			no_list.setVisibility(View.GONE);
		} else {
			no_list.setVisibility(View.VISIBLE);
		}
		
		lv_goods.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(isForCode)
				{
					Intent data=new Intent();
					data.putExtra("goodsinfo", (GoodsListBean.GoodsInfo)goodsLisrAdapter.getItem(position));
					setResult(RESULT_OK, data);
					finish();
				}				
			}
		});
	}

	class GoodsLisrAdapter extends BaseAdapter {

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
				holder = new ViewHolder();
				convertView = LayoutInflater.from(GoodsManageActivity.this)
						.inflate(R.layout.listitem_goods_manager, null);
				holder.iv_goods = (ImageView) convertView
						.findViewById(R.id.iv_goods);
				holder.iv_next = (ImageView) convertView
						.findViewById(R.id.iv_next);
				holder.tv_ponum = (TextView) convertView
						.findViewById(R.id.tv_ponum);
				holder.tv_goods_type = (TextView) convertView
						.findViewById(R.id.tv_goods_type);
				holder.tv_goods_info = (TextView) convertView
						.findViewById(R.id.tv_goods_info);
				holder.tv_update = (TextView) convertView.findViewById(R.id.tv_update);
				holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
				convertView.setTag(holder);
			} else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
				holder = (ViewHolder) convertView.getTag();
			}
			GoodsListBean.GoodsInfo goodsInfo = dataList.get(position);

			holder.tv_ponum.setText("PO#:" + goodsInfo.getPonum());
			String goodsMyType = "";
			switch (goodsInfo.getMytype()) {
			case "0":
				goodsMyType = "出口";
				break;
			case "1":
				goodsMyType = "进口";
				break;
			case "2":
				goodsMyType = "内贸";
				break;

			default:
				break;
			}
			holder.tv_goods_type.setText(goodsInfo.getDetailname() + "   "
					+ goodsMyType);
			holder.tv_goods_info.setText(goodsInfo.getGoodsweight() + goodsInfo.getUnit()
					+ goodsInfo.getGoodstj() + "m³");
			holder.tv_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (dataList != null && dataList.size() - 1 >= position) {
						Intent intent = new Intent(GoodsManageActivity.this,
								AddGoodsActivity.class);
						intent.putExtra("info", dataList.get(position));
						intent.putExtra("type", "2");
						startActivity(intent);
					}
									
				}
			});
			holder.tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					String key = dataList.get(position).get_id();
					final String jsonRequest = new Gson().toJson(new DeleteGoodsRequest(key));
					final AlertDialog.Builder d=new AlertDialog.Builder(GoodsManageActivity.this);
					d.setTitle("删除 "+dataList.get(position).getDetailname()+"?")
					.setMessage("po#:"+dataList.get(position).getPonum()+
							"   "+dataList.get(position).getNum()+"件    "+"重量："
							+dataList.get(position).getGoodsweight()
							+"   体积："+dataList.get(position).getGoodstj())
					.setPositiveButton("确定", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
									UrlCat.URL_SUBMIT, BaseBean.class, null,
									new ParamsBuilder().submitParams(jsonRequest),
									new Listener<BaseBean>() {
										@Override
										public void onResponse(BaseBean arg0) {
											// TODO Auto-generated method stub
											cancelLD();
											if (arg0 != null) {
												if ("1".equals(arg0.getCode())) {
													MyToast.showToast(GoodsManageActivity.this,"删除成功");
														dataList.remove(position);
														goodsLisrAdapter.notifyDataSetChanged();
												} else {
													Toast.makeText(GoodsManageActivity.this,
															arg0.getMsg(), 1000).show();
												}
											} else {
												MyToast.showToast(GoodsManageActivity.this,"删除失败");
											}
										}
									}, new ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError arg0) {
											cancelLD();
											MyToast.showToast(GoodsManageActivity.this,"网络连接异常");
										}
									}));
						}
						
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).create().show();
					
				}
			});
			return convertView;
		}

		public final class ViewHolder {
			public ImageView iv_goods;
			public ImageView iv_next;
			public TextView tv_ponum;
			public TextView tv_goods_type;
			public TextView tv_goods_info,tv_update,tv_delete;
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
	
	class DeleteGoodsRequest {
		private String type;
		private String key;
		private String operate;
		

		public DeleteGoodsRequest(String key) {
			this.type = "1000";
			this.operate = "R";
			this.key = key;
			
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
