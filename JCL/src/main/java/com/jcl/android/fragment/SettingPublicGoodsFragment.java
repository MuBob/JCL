package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.MyWebViewActivity;
import com.jcl.android.activity.PublicGoodsActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;

/**
 * 列表查询货物页面
 * 
 * @author msz 需实现两个接口，刷新相关
 *
 */
public class SettingPublicGoodsFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;
	private List<FindGoodsListBean.Goods> dataList;
	private FindGoodsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private int type;

	public static SettingPublicGoodsFragment newInstance(int position) {
		SettingPublicGoodsFragment f = new SettingPublicGoodsFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getArguments().getInt("position");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_setting_public_goods,
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
		private String sort;// {"key1":"asc/desc"}排序
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数
		private String EQ_userid;
		private String filters;

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.type = "2000";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
			this.filters = filters;
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		pagenum = 1;
		loadData();
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
//		Log.e("syl", "type==>"+ type);
		// 拼接filter中数据
//		String filters = new Gson().toJson(new GoodsListFilters(JCLApplication.getInstance().getUserId(),type+""));
		String filters = "userid:"
				+ JCLApplication.getInstance().getUserId() + ",status:" + type;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
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

		@SuppressLint("ResourceAsColor") @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.listitem_setting_public_goods, null);
			}
			final ViewHolder holder = ViewHolder.getViewHolder(convertView);
			switch (type) {
			//无人报价
			case 0:
				holder.ll_bottom.setVisibility(View.VISIBLE);
				holder.tv_alreadyOk.setVisibility(View.GONE);
				holder.tv_qiang.setVisibility(View.GONE);
				break;
				//有报价
			case 1:
				holder.ll_bottom.setVisibility(View.VISIBLE);
				holder.tv_delete.setVisibility(View.GONE);
				holder.tv_update.setVisibility(View.GONE);
				if (dataList.get(position).getStatus() != null) {
					switch (dataList.get(position).getStatus()) {
					case "5":
						holder.tv_alreadyOk.setVisibility(View.VISIBLE);
						break;
					case "2":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("查看\n报价");
						break;

					default:
						break;
					}
				}
				break;
				//被删除
			case 9:
				holder.ll_bottom.setVisibility(View.GONE);
				holder.tv_alreadyOk.setVisibility(View.GONE);
				holder.tv_qiang.setVisibility(View.GONE);
				holder.tv_delete.setVisibility(View.GONE);
				holder.tv_refresh.setVisibility(View.GONE);
				holder.tv_top.setVisibility(View.GONE);
				holder.tv_update.setVisibility(View.VISIBLE);
//				holder.tv_qiang.setText("联系\n客服");
				break;

			default:
				break;
			}
			
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());//出发地
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());//目的地
			if (dataList.get(position).getPonum() == null 
					|| dataList.get(position).getPonum().equals("")) {
				holder.tv_chufashijian.setVisibility(View.GONE);
				} else {
				holder.tv_chufashijian.setText("po#/货物编号："+dataList.get(position)//Po号
						.getPonum());
			}
			
			holder.tv_num.setText("货源编码："+dataList.get(position).get_id());
			String chechang = dataList.get(position).getCarlength();
//			String[] chechangs = getResources().getStringArray(
//					R.array.chechang);
//			ArrayList<String> carlength = new ArrayList<String>();
//			for (int i = 0; i < chechangs.length; i++) {
//				carlength.add(chechangs[i]);
//			}
//			for (int i = 0; i < carlength.size(); i++) {
//				if (dataList.get(position).getCarlength().equals(i+"")
//					|| Integer.valueOf(dataList.get(position).getCarlength()) == i) {
//					chechang = carlength.get(i);
//				}
//			}
			
			if (!TextUtils.isEmpty(dataList.get(position).getCarname())) {
				holder.tv_goodsinfo.setVisibility(View.VISIBLE);
				if (dataList.get(position).getCarlengthabove() != null
						&&dataList.get(position).getCarlengthabove().equals("1")) {
					if (chechang.equals("0")) {
						chechang = "   车长：不限";
					} else {
						chechang = "   车长："+chechang+"米以上";
					}
				} else {
					if (chechang.equals("0")) {
						chechang = "   车长：不限";
					} else {
						chechang = "   车长："+chechang + "米";
					}
				}
				if (dataList.get(position).getCarname().equals("集装箱车")) {
					holder.tv_goodsinfo.setText("求"+dataList.get(position)
							.getCarname()+"   \n"
							+ dataList.get(position).getCreatetime());
				} else {
					holder.tv_goodsinfo.setText("求"+dataList.get(position)
							.getCarname()
							+ chechang+"   \n"
							+ dataList.get(position).getCreatetime());
				}
							} else {
				holder.tv_goodsinfo.setVisibility(View.INVISIBLE);
			}
			if (dataList.get(position).getPublishstatus().equals("0")) {
				//快捷发布
				holder.tv_publicstatus.setText("快捷发布");
			} else {
				holder.tv_publicstatus.setText("精准发布");
			}
						
			if (!Utils.isEmpty(dataList.get(position).getGone()) && dataList.get(position).getGone().equals("0")) {
//				holder.tv_huoyifa.setTextColor(Color.RED);
			} else {
				holder.tv_huoyifa.setBackgroundColor(R.color.gray);
				holder.tv_huoyifa.setTextColor(R.color.white);
				holder.tv_huoyifa.setClickable(false);
				holder.tv_huoyifa.setEnabled(false);
				holder.tv_chufashijian.setTextColor(R.color.defult_text);
				holder.tv_publicstatus.setTextColor(R.color.defult_text);
				holder.tv_goodsinfo.setTextColor(R.color.defult_text);
				holder.tv_num.setTextColor(R.color.defult_text);
				holder.tv_chufadi.setTextColor(R.color.defult_text);
				holder.tv_mudidi.setTextColor(R.color.defult_text);
			}
			
			String status = "";
			switch (Integer.parseInt(dataList.get(position).getStatus())) {
			case 1:
				status = "未采用报价";
				break;
				
			case 2:
				status = "已采用报价";
				break;
				
			case 3:
				status = "已承运";
				break;
				
			case 4:
				status = "货物已送达";
				break;
				
			case 5:
				status = "已成交";
				break;

			default:
				holder.tv_status.setVisibility(View.GONE);
				break;
			}
			holder.tv_status.setText(status);
			final AlertDialog.Builder d=new AlertDialog.Builder(getActivity());
			holder.tv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					d.setTitle("删除").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String key = dataList.get(position).get_id();
							String type = "1003";
							String operate = "R";
							PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(key,type,operate,"");
							String jsonRequest = new Gson().toJson(publicGoodsRequest);
							showLD("删除中...");
							executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
									UrlCat.URL_SUBMIT, BaseBean.class, null,
									ParamsBuilder.submitParams(jsonRequest),
									new Listener<BaseBean>() {
										@Override
										public void onResponse(BaseBean arg0) {
											if (arg0 != null) {
												if (TextUtils.equals(arg0.getCode(), "1")) {
													MyToast.showToast(getActivity(),"删除成功");
													dataList.remove(position);
													notifyDataSetChanged();
												} else {
													MyToast.showToast(getActivity(),"删除失败");
												}
											}

											cancelLD();
										}
									}, new ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError arg0) {
											cancelLD();
											MyToast.showToast(getActivity(),"删除失败");
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
			
			holder.tv_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (dataList.get(position).getStatus().equals("")) {
						
					} else {

					}
					startActivity(new Intent(getActivity(),
							PublicGoodsActivity.class).putExtra("huoyuanid", dataList.get(position).get_id()));
				}
			});
			
			//刷新
			holder.tv_refresh.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String goodsid = dataList.get(position).get_id();
					String type = "1016";
					String operate = "M";
//					UpdataGoods updatagoods = new UpdataGoods(goodsid);
//					String data = new Gson().toJson(updatagoods);
					PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(goodsid,type,operate,"");
					String jsonRequest = new Gson().toJson(publicGoodsRequest);
					showLD("刷新中...");
					executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
							UrlCat.URL_SUBMIT, BaseBean.class, null,
							ParamsBuilder.submitParams(jsonRequest),
							new Listener<BaseBean>() {
								@Override
								public void onResponse(BaseBean arg0) {
									if (arg0 != null) {
										if (TextUtils.equals(arg0.getCode(), "1")) {
											MyToast.showToast(getActivity(),"刷新成功");
											notifyDataSetChanged();
										} else {
											MyToast.showToast(getActivity(),"刷新失败");
										}
									}

									cancelLD();
								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
									cancelLD();
									MyToast.showToast(getActivity(),"刷新失败");
								}
							}));
				}
			});
			
			//置顶

            holder.tv_top.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String goodsid = dataList.get(position).get_id();
					String type = "1015";
					String operate = "M";
//					UpdataGoods updatagoods = new UpdataGoods(goodsid);
//					String data = new Gson().toJson(updatagoods);
					PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(goodsid,type,operate,"");
					String jsonRequest = new Gson().toJson(publicGoodsRequest);
					showLD("置顶中...");
					executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
							UrlCat.URL_SUBMIT, BaseBean.class, null,
							ParamsBuilder.submitParams(jsonRequest),
							new Listener<BaseBean>() {
								@Override
								public void onResponse(BaseBean arg0) {
									if (arg0 != null) {
										if (TextUtils.equals(arg0.getCode(), "1")) {
											MyToast.showToast(getActivity(),"置顶成功");
											notifyDataSetChanged();
										} else {
											MyToast.showToast(getActivity(),"置顶失败");
										}
									}

									cancelLD();
								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
									cancelLD();
									MyToast.showToast(getActivity(),"置顶失败");
								}
							}));
				}
			});
            
            holder.tv_huoyifa.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String goodsid = dataList.get(position).get_id();
					String type = "1003";
					String operate = "M";
//					UpdataGoods updatagoods = new UpdataGoods(goodsid);
//					String data = new Gson().toJson(updatagoods);
					HyData hydata = new HyData("1");
					String gone = new Gson().toJson(hydata);
					String hwdata = new Gson().toJson(new HwData());
					String data = new Gson().toJson(new UpdataGoods(gone,hwdata));
					PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(goodsid,type,operate,data);
					String jsonRequest = new Gson().toJson(publicGoodsRequest);
					showLD("加载中...");
					executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
							UrlCat.URL_SUBMIT, BaseBean.class, null,
							ParamsBuilder.submitParams(jsonRequest),
							new Listener<BaseBean>() {

								@Override
								public void onResponse(BaseBean arg0) {
									if (arg0 != null) {
										if (TextUtils.equals(arg0.getCode(), "1")) {
											MyToast.showToast(getActivity(),
													"该货物已被承运，通知承运商勿扰！");
											holder.tv_huoyifa.setBackgroundColor(R.color.gray);
											holder.tv_huoyifa.setTextColor(R.color.white);
											holder.tv_huoyifa.setClickable(false);
											holder.tv_chufashijian.setTextColor(R.color.defult_text);
											holder.tv_publicstatus.setTextColor(R.color.defult_text);
											holder.tv_goodsinfo.setTextColor(R.color.defult_text);
											holder.tv_num.setTextColor(R.color.defult_text);
											holder.tv_chufadi.setTextColor(R.color.defult_text);
											holder.tv_mudidi.setTextColor(R.color.defult_text);
										} else {
											MyToast.showToast(getActivity(),
													"货物发送失败");
										}
									}

									cancelLD();
								}
							}, new ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError arg0) {
									cancelLD();
									MyToast.showToast(getActivity(), "货物发送失败");
								}
							}));
				}
			});

            holder.tv_pipei.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),MyWebViewActivity.class);
					intent.putExtra("weburl",C.BASE_URL + "/mobile/matching?_id=" + dataList.get(position).get_id());
					intent.putExtra("title", "匹配");
					getActivity().startActivity(intent);
				}
			});
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi, tv_chufashijian,
				tv_goodsinfo, tv_qiang, tv_alreadyOk, tv_ji,tv_pipei;
		TextView tv_refresh, tv_top,tv_status,tv_num,tv_publicstatus,tv_huoyifa;
		LinearLayout ll_bottom;
	    TextView tv_update,tv_delete;//修改和删除按钮

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
			holder.tv_goodsinfo = (TextView) convertView
					.findViewById(R.id.tv_goodsinfo);
			holder.tv_qiang = (TextView) convertView
					.findViewById(R.id.tv_qiang);
			holder.tv_alreadyOk = (TextView) convertView
					.findViewById(R.id.tv_alreadyOk);
			holder.tv_pipei = (TextView) convertView
			        .findViewById(R.id.tv_pipei);
			holder.tv_ji = (TextView) convertView.findViewById(R.id.tv_ji);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.ll_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_bottom);
			
			holder.tv_refresh = (TextView) convertView.findViewById(R.id.tv_refresh);
			holder.tv_top = (TextView) convertView.findViewById(R.id.tv_top);
			holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
			holder.tv_update = (TextView) convertView.findViewById(R.id.tv_update);
			holder.tv_publicstatus = (TextView) convertView.findViewById(R.id.tv_publicstatus);
			holder.tv_huoyifa = (TextView) convertView.findViewById(R.id.tv_huoyifa);

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
		private String data;

		public PublicGoodsRequest
		(String key,String type,String operate,String data) {
			this.operate = operate;
			this.type = type;
			this.key = key;
			this.data = data;
		}
	}
	
	class UpdataGoods{
		private String hydata;
		private String hwdata;
		public UpdataGoods(String hydata,String hwdata) {
			this.hydata = hydata;
			this.hwdata = hwdata;
		}
	}
	
	 class HyData {
		 private String gone;
			public HyData(String gone) {
				this.gone = gone;
			}
	 }
	 
	 class HwData {
			public HwData() {
				
			}
	 }

}
