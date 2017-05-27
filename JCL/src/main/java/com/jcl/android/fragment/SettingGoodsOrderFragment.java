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
import android.widget.ImageView;
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
import com.jcl.android.activity.EvaluteActivity;
import com.jcl.android.activity.FindOfferPriceActivity;
import com.jcl.android.activity.HuidanInfoActivity;
import com.jcl.android.activity.OfferOnlineActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.fragment.SettingCarsOrderFragment.BjData;
import com.jcl.android.fragment.SettingCarsOrderFragment.GoodsComeRequest;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.view.MyToast;

/**
 * 管理货单子页
 * 
 * @author msz 需实现两个接口，刷新相关
 *
 */
public class SettingGoodsOrderFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener {
	private View root;
	private ListView lv_find_by_list;

	private List<FindGoodsListBean.Goods> dataList;
	private FindGoodsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;

	private int type;
	
	
	

	public static SettingGoodsOrderFragment newInstance(int position) {
		SettingGoodsOrderFragment f = new SettingGoodsOrderFragment();
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
			this.type = "3004";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindGoodsListBean.Goods>();
		mAdapter = new FindGoodsAdapter();
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
							C.INTENT_TYPE_FIND_GOODS, dataList.get(position).get_id()));
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
        
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.listitem_setting_public_goodsorder, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			String vanorderid = dataList.get(position).getGoodorder();
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_chufashijian.setText(dataList.get(position).getExfhstarttime());
			String zffs = dataList.get(position).getZffs()+"";
			final String goodsid = dataList.get(position).get_id();
			if("1".equals(zffs)){
				holder.tv_tuoguanyunfei.setVisibility(View.VISIBLE);
			}
			String chexing = "";
			if(3==type){
				holder.tv_fangqi.setVisibility(View.GONE);
			}
			if(0==type){
				holder.tv_chexing.setVisibility(View.INVISIBLE);
				holder.img_call.setVisibility(View.GONE);
				holder.tv_chengyunprice.setVisibility(View.GONE);
				holder.tv_fangqi.setVisibility(View.GONE);
			}else{
				holder.tv_chexing.setText("车主："+dataList.get(position).getLinkman()+" 联系电话："+dataList.get(position).getPhone());
				holder.tv_chengyunprice.setText("承运价格: ￥"+dataList.get(position).getPrice());
				final String number = dataList.get(position).getPhone(); 
				holder.img_call.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						  //用intent启动拨打电话  
		                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
		                startActivity(intent); 
					}
				});
			}
			holder.tv_fangqi.setOnClickListener(new QuxiaodingdanOnclickListnener(goodsid));
			//查看回单
			String status = dataList.get(position).getStatus()+"";
			if("4".equals(status) || "5".equals(status)){
				holder.tv_chakanhuidan.setVisibility(View.VISIBLE);
				holder.tv_chakanhuidan.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), HuidanInfoActivity.class);
						intent.putExtra("goodsid", goodsid);
						startActivity(intent);
					}
				});
				//评价车主
				holder.img_evalute.setVisibility(View.VISIBLE);
				holder.img_evalute.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), EvaluteActivity.class);
						intent.putExtra("goodsid", goodsid);
						startActivity(intent);
					}
				});
				
			}
			

			if (!TextUtils.isEmpty(dataList.get(position).getDetailname())) {
				holder.tv_goodsinfo.setVisibility(View.VISIBLE);
				holder.tv_goodsinfo.setText(dataList.get(position)
						.getDetailname()
						+ " "
						+ dataList.get(position).getGoodsweight()+"吨"
						);
			} else {
				holder.tv_goodsinfo.setVisibility(View.INVISIBLE);
			}
			
			
			switch (type) {
			case 0://有报价
				String chufadi = dataList.get(position).getStartarea();
	            String mudidi = dataList.get(position).getEndarea();
	            String chufashijian = dataList.get(position).getExfhstarttime();
	            //查看报价
				holder.tv_qiang.setOnClickListener(new MyListnener(chufadi,mudidi,chexing,chufashijian,goodsid,dataList.get(position)
						.getDetailname()));
				break;
			case 1://进行中
				if (dataList.get(position).getStatus() != null) {
					switch (dataList.get(position).getStatus()) {
					case "3":
						//holder.tv_alreadyOk.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("已\n承运");
						break;
					case "2":
						holder.tv_qiang.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("待\n承运");
						break;

					default:
						break;
					}
				}
				break;
			case 2://待付款
				if (dataList.get(position).getStatus() != null) {
					switch (dataList.get(position).getStatus()) {
					case "4":
						//holder.tv_alreadyOk.setVisibility(View.VISIBLE);
						holder.tv_qiang.setText("确认\n付款");
						holder.tv_qiang.setOnClickListener(new QuerenfukuanOnclickListnener(goodsid,vanorderid));
						
						break;

					default:
						break;
					}
				}
				break;
			case 3://已完成
				//holder.tv_alreadyOk.setVisibility(View.GONE);
				holder.tv_qiang.setVisibility(View.VISIBLE);
				holder.tv_qiang.setText("联系\n客服");
				break;

			default:
				break;
			}
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi, tv_chufashijian, tv_chexing,
				tv_goodsinfo, tv_qiang, tv_alreadyOk, tv_ji,tv_chengyunprice,tv_fangqi,tv_tuoguanyunfei,tv_chakanhuidan,img_evalute;
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
			holder.tv_chufashijian = (TextView) convertView
					.findViewById(R.id.tv_chufashijian);
			holder.tv_chexing = (TextView) convertView
					.findViewById(R.id.tv_chexing);
			holder.tv_goodsinfo = (TextView) convertView
					.findViewById(R.id.tv_goodsinfo);
			holder.tv_qiang = (TextView) convertView
					.findViewById(R.id.tv_qiang);
			
			holder.tv_alreadyOk = (TextView) convertView
					.findViewById(R.id.tv_alreadyOk);
			holder.tv_ji = (TextView) convertView.findViewById(R.id.tv_ji);
			holder.tv_chengyunprice = (TextView) convertView.findViewById(R.id.tv_chengyunprice);
			holder.img_call = (ImageView) convertView.findViewById(R.id.img_call);
			holder.tv_fangqi = (TextView) convertView.findViewById(R.id.tv_fangqi);
			holder.tv_tuoguanyunfei = (TextView) convertView.findViewById(R.id.tv_tuoguanyunfei);
			holder.tv_chakanhuidan = (TextView) convertView.findViewById(R.id.tv_chakanhuidan);
			holder.img_evalute = (TextView) convertView.findViewById(R.id.img_evalute);
			return holder;
		}

	}
	/**
	 * 自定义点击事件
	 * @author xuelei
	 *
	 */
	public class MyListnener implements OnClickListener{
		String chufadi;
		String mudidi;
		String chexing;
		String chufashijian;
		String goodsid;
		String detailgood;
        public MyListnener(String chufadi,String mudidi,String chexing,String chufashijian,String goodsid,String detailgood){
        	this.chufadi = chufadi;
        	this.mudidi = mudidi;
        	this.chexing = chexing;
        	this.chufashijian = chufashijian;
        	this.goodsid = goodsid;
        	this.detailgood = detailgood;
        }
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
            intent.setClass(getActivity(), FindOfferPriceActivity.class);
            intent.putExtra("goodsid", goodsid);
            intent.putExtra("chufadi", chufadi);
            intent.putExtra("mudidi", mudidi);
            intent.putExtra("chexing", chexing);
            intent.putExtra("chufashijian", chufashijian);
            intent.putExtra("detailgood", detailgood);
            startActivityForResult(intent,1);
		}
		
	}
	
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode == 1){
			 switch (requestCode){
			 case 1:
				 pagenum = 1;
				 loadData();
				 break;
			 }
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
	//取消订单
		class QuxiaodingdanRequest {
			private String operate;
			private String type;
			private String data;

			public QuxiaodingdanRequest(String data) {
				this.operate = "A";
				this.type = "30101";
				this.data = data;
			}
		}
		class QuxiaodingdanData {
			private String goodsid;//货源id
			
			public QuxiaodingdanData(String goodsid){
				this.goodsid = goodsid;
			}
		}
		public class QuxiaodingdanOnclickListnener implements OnClickListener{
			String goodsid;
	        public QuxiaodingdanOnclickListnener(String goodsid){
	        	this.goodsid = goodsid;
	        }
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new Builder(getActivity());
				  builder.setMessage("是否确认取消订单？");  
				  builder.setTitle("消息提示!"); 
				  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			           @Override
			           public void onClick(DialogInterface dialog, int which) {
			        	   quxiaodingdan(goodsid);
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
		public void quxiaodingdan(String goodsid){
			QuxiaodingdanData bjdata = new QuxiaodingdanData(goodsid);
			String bjDataJson = new Gson().toJson(bjdata);
			QuxiaodingdanRequest offeronlineRequest = new QuxiaodingdanRequest(bjDataJson);
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
	//确认付款
	class QueRenFukuanRequest {
		private String operate;
		private String type;
		private String data;

		public QueRenFukuanRequest(String data) {
			this.operate = "A";
			this.type = "3010";
			this.data = data;
		}
	}
	class BjData {
		private String goodsid;//货源id
		private String vanorderid;//车单id
		
		public BjData(String goodsid,String vanorderid){
			this.goodsid = goodsid;
			this.vanorderid = vanorderid;
		}
	}
	public class QuerenfukuanOnclickListnener implements OnClickListener{
		String goodsid;
		String vanorderid;
        public QuerenfukuanOnclickListnener(String goodsid,String vanorderid){
        	this.goodsid = goodsid;
        	this.vanorderid = vanorderid;
        }
		@Override
		public void onClick(View arg0) {
			AlertDialog.Builder builder = new Builder(getActivity());
			  builder.setMessage("是否确认付款？");  
			  builder.setTitle("消息提示!"); 
			  builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		        	   querenfukuan(goodsid,vanorderid);
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
	//
	public void querenfukuan(String goodsid,String vanorderid){
		BjData bjdata = new BjData(goodsid,vanorderid);
		String bjDataJson = new Gson().toJson(bjdata);
		QueRenFukuanRequest offeronlineRequest = new QueRenFukuanRequest(bjDataJson);
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
