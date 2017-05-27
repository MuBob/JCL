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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jcl.android.activity.AddHuizhidanActivity;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.FindOfferPriceActivity;
import com.jcl.android.activity.MyWalletActivity;
import com.jcl.android.activity.OfferOnlineActivity;
import com.jcl.android.alipay.PayResult;
import com.jcl.android.alipay.PayUtils;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.FindCarOrderListBean;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.fragment.OfferPriceListFragment.BjData;
import com.jcl.android.fragment.OfferPriceListFragment.OfferOnlineRequest;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.BaojiaOnlinePopupwindow;
import com.jcl.android.popupwindow.ChonzhiOnlinePopupwindow;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.view.MyToast;

/**
 * 管理车单子页
 * 
 * @author xueleilin 需实现两个接口，刷新相关
 *
 */
public class SettingCarsOrderFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener {
	private View root;
	private ListView lv_find_by_list;

	private List<FindCarOrderListBean.Vanorders> dataList;
	private FindCarOrderAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;

	private int type;
	
	
	

	public static SettingCarsOrderFragment newInstance(int position) {
		SettingCarsOrderFragment f = new SettingCarsOrderFragment();
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
		private String filters;// 过滤条件
		private String sort;// {"key1":"asc/desc"}排序
		private String pagenum;// 1,页码
		private String pagesize;// 10每页显示的条数

		public GetStr(int pagenum, String filters) {
			this.pagenum = pagenum + "";
			this.filters = filters;
			this.type = "3005";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindCarOrderListBean.Vanorders>();
		mAdapter = new FindCarOrderAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		//loadData();
		//设置取消预加载
		setUserVisibleHint(true);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_GOODS, dataList.get(position).getGoodsid()));
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
		String filters = "userid:"
				+ JCLApplication.getInstance().getUserId() + ",status:" + type;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		// 检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		// 添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}
		executeRequest(new GsonRequest<FindCarOrderListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, FindCarOrderListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<FindCarOrderListBean>() {

					@Override
					public void onResponse(FindCarOrderListBean arg0) {
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

	class FindCarOrderAdapter extends BaseAdapter {

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
						R.layout.listitem_setting_public_carsorder, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			String zffs = dataList.get(position).getZffs()+"";
			final String priceid = dataList.get(position).get_id();
			if("1".equals(zffs)){
				holder.tv_tuoguanyunfei.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(dataList.get(position).getDetailname())) {
				holder.tv_goodsinfo.setVisibility(View.VISIBLE);
				holder.tv_goodsinfo.setText(dataList.get(position)
						.getDetailname()
						+ " "
						);
			} else {
				holder.tv_goodsinfo.setVisibility(View.INVISIBLE);
			}
			holder.tv_mybaojia.setText("我的报价:"+dataList.get(position).getPrice()+"元");
			
			holder.tv_linkman.setText("联系人:"+dataList.get(position).getLinkman()+" "+dataList.get(position).getPhone());
			final String number = dataList.get(position).getPhone(); 
			holder.img_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
	                //用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
	                startActivity(intent); 
				}
			});
			switch (type) {
			case 0:
				holder.tv_tuoguanyunfei.setVisibility(View.GONE);
				holder.tv_chongxinbaojia.setVisibility(View.VISIBLE);//重新报价
				holder.tv_chongxinbaojia.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						new BaojiaOnlinePopupwindow(getActivity(), arg0, mHandler, null,priceid).show();
					}
				});
				holder.tv_fangqibaojia.setVisibility(View.VISIBLE);//放弃报价
				holder.tv_fangqibaojia.setOnClickListener(new FangqibaojiaOnclickListnener(dataList.get(position).get_id()));
				if (dataList.get(position).getStatus() != null) {
					switch (dataList.get(position).getStatus()) {
					case "1":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("未\n中标");
						//holder.tv_qiang.setOnClickListener(new GoodsComeOnclickListnener(dataList.get(position).get_id(),dataList.get(position).getGoodsid()));
						holder.tv_chongxinbaojia.setVisibility(View.GONE);//重新报价
						holder.tv_fangqibaojia.setVisibility(View.GONE);//放弃报价
						break;

					default:
						break;
					}
				}
				break;
			case 1:
				if (dataList.get(position).getStatus() != null) {
					switch (dataList.get(position).getStatus()) {
					case "4":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("货物\n送达");
						holder.tv_qiang.setOnClickListener(new GoodsComeOnclickListnener(dataList.get(position).get_id(),dataList.get(position).getGoodsid()));
						break;
					case "3":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("开始\n承运");
						//开始承运事件
						holder.tv_qiang.setOnClickListener(new ChengYunOnclickListnener(dataList.get(position).get_id(),dataList.get(position).getGoodsid()));
						break;

					default:
						break;
					}
				}
				break;
			case 2:
				if (dataList.get(position).getStatus() != null) {
					switch (dataList.get(position).getStatus()) {
					case "5":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("上传\n回单");
						//上传回单
						//holder.tv_qiang.ShangchuanhuizhiclickListnener(new ChengYunOnclickListnener(dataList.get(position).get_id(),dataList.get(position).getGoodsid()));
						holder.tv_qiang.setOnClickListener(new ShangchuanhuizhiclickListnener(dataList.get(position).getGoodsid()));
						break;
					case "6":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("已\n完成");
						break;

					default:
						break;
					}}
				break;
			case 3:
				holder.tv_qiang.setVisibility(View.INVISIBLE);
				break;

			default:
				break;
			}
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi,tv_goodsinfo, tv_qiang, tv_alreadyOk,tv_mybaojia,
		tv_linkman,tv_tuoguanyunfei,tv_chongxinbaojia,tv_fangqibaojia;
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
			holder.tv_chufadi = (TextView) convertView
					.findViewById(R.id.tv_chufadi);
			holder.tv_mudidi = (TextView) convertView
					.findViewById(R.id.tv_mudidi);
			holder.tv_goodsinfo = (TextView) convertView
					.findViewById(R.id.tv_goodsinfo);
			holder.tv_qiang = (TextView) convertView
					.findViewById(R.id.tv_qiang);
			
			holder.tv_alreadyOk = (TextView) convertView
					.findViewById(R.id.tv_alreadyOk);
			holder.tv_mybaojia = (TextView) convertView
					.findViewById(R.id.tv_mybaojia);
			holder.tv_linkman = (TextView) convertView
					.findViewById(R.id.tv_linkman);
			holder.img_call = (ImageView) convertView
					.findViewById(R.id.img_call);
			holder.tv_tuoguanyunfei = (TextView) convertView
					.findViewById(R.id.tv_tuoguanyunfei);
			holder.tv_chongxinbaojia = (TextView) convertView
					.findViewById(R.id.tv_chongxinbaojia);
			holder.tv_fangqibaojia = (TextView) convertView
					.findViewById(R.id.tv_fangqibaojia);
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
	//重新报价
	class ChongxinbaojiaRequest {
		private String operate;
		private String type;
		private String data;

		public ChongxinbaojiaRequest(String data) {
			this.operate = "A";
			this.type = "30011";
			this.data = data;
		}
	}
	
	class ChongxinbaojiaData {
		private String priceid;//
		private String price;//
		
		public ChongxinbaojiaData(String priceid,String price){
			this.priceid = priceid;
			this.price = price;
		}
	}
	
	public void chongxinbaojia(String priceid,String price){
		ChongxinbaojiaData bjdata = new ChongxinbaojiaData(priceid,price);
		String bjDataJson = new Gson().toJson(bjdata);
		ChongxinbaojiaRequest offeronlineRequest = new ChongxinbaojiaRequest(bjDataJson);
		String postStr = new Gson().toJson(offeronlineRequest);
		showLD("提交中...");
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				ParamsBuilder.submitParams(postStr),
				new Listener<BaseBean>() {

					@Override
					public void onResponse(BaseBean arg0) {
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								MyToast.showToast(getActivity(),
										"提交成功");
								//刷新数据
								 pagenum = 1;
								loadData();
							} else {
								MyToast.showToast(getActivity(),
										"提交失败");
							}
						}

						cancelLD();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
						MyToast.showToast(getActivity(), "提交失败");
					}
				}));
	}
	//放弃报价
		class FangqibaojiaRequest {
			private String operate;
			private String type;
			private String data;

			public FangqibaojiaRequest(String data) {
				this.operate = "A";
				this.type = "30012";
				this.data = data;
			}
		}
		
		class FangqibaojiaData {
			private String priceid;//
			
			public FangqibaojiaData(String priceid){
				this.priceid = priceid;
			}
		}
		public class FangqibaojiaOnclickListnener implements OnClickListener{
			String priceid;
	        public FangqibaojiaOnclickListnener(String priceid){
	        	this.priceid = priceid;
	        }
			@Override
			public void onClick(View arg0) {
				  AlertDialog.Builder builder = new Builder(getActivity());
				  builder.setMessage("是否确认放弃报价？");  
				  builder.setTitle("消息提示!"); 
				  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			           @Override
			           public void onClick(DialogInterface dialog, int which) {
			        	    fangqibaojia(priceid);
			                dialog.dismiss();
			                pagenum = 1;
							loadData();
//			                getActivity().finish();
			           }
			       });  
				  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			           @Override
			           public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			           }
			       });  
				  builder.create().show();
				
			}
			
		}
		public void fangqibaojia(String priceid){
			FangqibaojiaData bjdata = new FangqibaojiaData(priceid);
			String bjDataJson = new Gson().toJson(bjdata);
			FangqibaojiaRequest offeronlineRequest = new FangqibaojiaRequest(bjDataJson);
			String postStr = new Gson().toJson(offeronlineRequest);
			showLD("提交中...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(getActivity(),
											"提交成功");
									//刷新数据
									loadData();
								} else {
									MyToast.showToast(getActivity(),
											"提交失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(getActivity(), "提交失败");
						}
					}));
		}
	
	//开始承运	
	class OfferOnlineRequest {
		private String operate;
		private String type;
		private String data;

		public OfferOnlineRequest(String data) {
			this.operate = "A";
			this.type = "3006";
			this.data = data;
		}
	}
	
	class BjData {
		private String vanorderid;//
		private String vanuserid;//
		private String goodsid;//
		
		public BjData(String vanorderid,String vanuserid,String goodsid){
			this.vanorderid = vanorderid;
			this.vanuserid = vanuserid;
			this.goodsid = goodsid;
		}
	}
	public class ChengYunOnclickListnener implements OnClickListener{
		String vanorderid;
		String goodsid;
        public ChengYunOnclickListnener(String vanorderid,String goodsid){
        	this.vanorderid = vanorderid;
        	this.goodsid = goodsid;
        }
		@Override
		public void onClick(View arg0) {
			  AlertDialog.Builder builder = new Builder(getActivity());
			  builder.setMessage("是否确认开始承运？");  
			  builder.setTitle("消息提示!"); 
			  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		        	   startChengxun(vanorderid,goodsid);
		                dialog.dismiss();
		                pagenum = 1;
						loadData();
//		                getActivity().finish();
		           }
		       });  
			  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		           }
		       });  
			  builder.create().show();
			
		}
		
	}
	public void startChengxun(String vanorderid,String goodsid){
		BjData bjdata = new BjData(vanorderid,JCLApplication.getInstance().getUserId(),goodsid);
		String bjDataJson = new Gson().toJson(bjdata);
		OfferOnlineRequest offeronlineRequest = new OfferOnlineRequest(bjDataJson);
		String postStr = new Gson().toJson(offeronlineRequest);
		showLD("提交中...");
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				ParamsBuilder.submitParams(postStr),
				new Listener<BaseBean>() {

					@Override
					public void onResponse(BaseBean arg0) {
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								MyToast.showToast(getActivity(),
										"提交成功");
								//刷新数据
								loadData();
							} else {
								MyToast.showToast(getActivity(),
										"提交失败");
							}
						}

						cancelLD();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
						MyToast.showToast(getActivity(), "提交失败");
					}
				}));
	}
	//货物已送达
	class GoodsComeRequest {
		private String operate;
		private String type;
		private String data;

		public GoodsComeRequest(String data) {
			this.operate = "A";
			this.type = "3007";
			this.data = data;
		}
	}
	public class GoodsComeOnclickListnener implements OnClickListener{
		String vanorderid;
		String goodsid;
        public GoodsComeOnclickListnener(String vanorderid,String goodsid){
        	this.vanorderid = vanorderid;
        	this.goodsid = goodsid;
        }
		@Override
		public void onClick(View arg0) {
			 AlertDialog.Builder builder = new Builder(getActivity());
			  builder.setMessage("是否确认货物已送达？");  
			  builder.setTitle("消息提示!"); 
			  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		        	   songDa(vanorderid,goodsid);
		                dialog.dismiss();
		                pagenum = 1;
						loadData();
//		                getActivity().finish();
		           }
		       });  
			  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		           }
		       });  
			  builder.create().show();
			
		}
		
	}
	public void songDa(String vanorderid,String goodsid){
		BjData bjdata = new BjData(vanorderid,JCLApplication.getInstance().getUserId(),goodsid);
		String bjDataJson = new Gson().toJson(bjdata);
		GoodsComeRequest offeronlineRequest = new GoodsComeRequest(bjDataJson);
		String postStr = new Gson().toJson(offeronlineRequest);
		showLD("提交中...");
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				ParamsBuilder.submitParams(postStr),
				new Listener<BaseBean>() {

					@Override
					public void onResponse(BaseBean arg0) {
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								MyToast.showToast(getActivity(),
										"提交成功");
								//刷新数据
								loadData();
							} else {
								MyToast.showToast(getActivity(),
										"提交失败");
							}
						}

						cancelLD();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
						MyToast.showToast(getActivity(), "提交失败");
					}
				}));
	}
	//上传回执单
	public class ShangchuanhuizhiclickListnener implements OnClickListener{
		String goodsid;
        public ShangchuanhuizhiclickListnener(String goodsid){
        	this.goodsid = goodsid;
        }
		@Override
		public void onClick(View arg0) {
			
			Intent intent = new Intent();
			intent.putExtra("goodsid", goodsid);
			intent.setClass(getActivity(),AddHuizhidanActivity.class );
			startActivity(intent);
			
		}
		
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: //重新报价
				String jine = (String) msg.obj;
				Bundle bundle = msg.getData();
				String priceid = bundle.getString("priceid");
				//调用重新报价
				chongxinbaojia(priceid,jine);
				break;
			
			}}};
			
			//禁止预加载
			@Override
			    public void setUserVisibleHint(boolean isVisibleToUser) {
				super.setUserVisibleHint(isVisibleToUser);
			            if (isVisibleToUser && srLayout != null) {
			               // async http request here
			            	loadData();
			            }else{
			            	
			            }
			        
			    }

}
