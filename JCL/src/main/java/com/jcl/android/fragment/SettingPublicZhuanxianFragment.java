package com.jcl.android.fragment;

import java.text.SimpleDateFormat;
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
import android.widget.ImageView;
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
import com.jcl.android.activity.PublicOtherActivity;
import com.jcl.android.activity.PublicZhuanxianActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.FindCarsListBean;
import com.jcl.android.bean.FindDedicateCarsListBean;
import com.jcl.android.bean.FindGoodsListBean;
import com.jcl.android.fragment.FindCarsByListFragment.FindCarsAdapter;
import com.jcl.android.fragment.FindCarsByListFragment.GetStr;
import com.jcl.android.fragment.FindCarsByListFragment.ViewHolder;
import com.jcl.android.fragment.SettingPublicGoodsFragment.HwData;
import com.jcl.android.fragment.SettingPublicGoodsFragment.HyData;
import com.jcl.android.fragment.SettingPublicGoodsFragment.PublicGoodsRequest;
import com.jcl.android.fragment.SettingPublicGoodsFragment.UpdataGoods;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;

/**
 * 列表查询
 * 
 * @author pb 需实现两个接口，刷新相关
 *
 */
public class SettingPublicZhuanxianFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private TextView tv_chufadi, tv_mudidi, tv_zhuanghuoshijian, tv_chexing;
	private List<FindDedicateCarsListBean.Line> dataList;
	private FindCarsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String chufadi = "", mudidi = "", zhuanghuoshijian = "",
			linetype = "";
	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;
	private TextView tv_noinfo;
	private int position;

	public static SettingPublicZhuanxianFragment newInstance(int position) {
		SettingPublicZhuanxianFragment f = new SettingPublicZhuanxianFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt("position");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_setting_public_zhuanxian,
				container, false);
		initView();
		return root;
	}

	private void initView() {
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		tv_noinfo=(TextView)root.findViewById(R.id.tv_noinfo);
		//添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
		cityPickerPopupwindow = new CityPickerPopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), mHandler, null);
		datePickerPopupwindow = new DatePickerPopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), mHandler, null);
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
			this.type = "2012";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindDedicateCarsListBean.Line>();
		mAdapter = new FindCarsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData(chufadi, mudidi, linetype);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
//					startActivity(DetailFindActivity.newInstance(getActivity(),
//							C.INTENT_TYPE_FIND_CAR,dataList.get(position).get_id()));
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_ZHUANXIAN,dataList.get(position).get_id()));
				}
			}
		});
	}
	/**
	 * 列表查询数据
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData(String EQ_startareacode, String EQ_endareacode, String EQ_linetype) {
		//拼接filter中数据
				String filters =  "EQ_userid:"+JCLApplication.getInstance().getUserId();
				String getStr = new Gson().toJson(new GetStr(pagenum, filters));
				//检测是否是第一页
				final boolean isFromTop = pagenum == 1;
				//添加刷新小图标显示
				if (!srLayout.isRefreshing()) {
					srLayout.setRefreshing(true);
				}
				executeRequest(new GsonRequest<FindDedicateCarsListBean>(Request.Method.POST,
						UrlCat.URL_SEARCH, FindDedicateCarsListBean.class, null,
						ParamsBuilder.pageSearchParams(getStr),
						new Listener<FindDedicateCarsListBean>() {

							@Override
							public void onResponse(FindDedicateCarsListBean arg0) {
								//清除刷新小图标
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
										if(dataList==null)
										{
											tv_noinfo.setVisibility(View.VISIBLE);
										}
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
//								MyToast.showToast(getActivity(), arg0.getMessage());
							}
						}));
			}
		/**
		 * 下拉刷新 page置为1
		 * 筛选完数据也执行该方法
		 */
			@Override
			public void onRefresh() {
				pagenum = 1;
				loadData(chufadi, mudidi, linetype);
			}

			class FindCarsAdapter extends BaseAdapter {

				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return dataList!=null?dataList.size():0;
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
				public View getView(final int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = LayoutInflater.from(getActivity()).inflate(
								R.layout.listitem_find_specialcar, null);
					}
					ViewHolder holder = ViewHolder.getViewHolder(convertView);
					String lineType="";
					String jingTing="";
					if(TextUtils.equals(dataList.get(position).getLinetype(), "0"))
					{
						lineType="直达线路:";
						jingTing="";
					}else{
						lineType="中转线路:";
						jingTing="经停:"+(dataList.get(position).getStopnames()==null?
								"":dataList.get(position).getStopnames());
					}
					holder.tv_chufadi.setText(lineType+dataList.get(position).getStartarea());
					holder.tv_mudidi.setText(dataList.get(position).getEndarea());
					holder.tv_jingting.setText(jingTing);
					
					if (Utils.isEmpty(dataList.get(position).getZx_image())) {
						
					} else {
					ImageLoaderUtil.getInstance(getActivity()).
					loadImage(C.BASE_URL+dataList.get(position).getZx_image(),
							holder.iv_pic);
					}
					final AlertDialog.Builder d=new AlertDialog.Builder(getActivity());
					holder.btn_delete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							d.setTitle("删除").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener(){
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String key = dataList.get(position).get_id();
									String operate = "R";
									PublicZhuanxianRequest publicGoodsRequest = new 
											PublicZhuanxianRequest(operate, "", key,"10074");
											
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
					//编辑
					holder.btn_edit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(),
									PublicZhuanxianActivity.class);
							intent.putExtra("info", dataList.get(position));
							startActivity(intent);
						}
					});
					//置顶
					holder.btn_top.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String _id = dataList.get(position).get_id();
							String operate = "M";
							Data hydata = new Data("1","");
							PublicZhuanxianRequest publicZhuanxianRequest = 
							new PublicZhuanxianRequest(operate,new Gson().toJson(hydata),_id,"10071");
							String jsonRequest = new Gson().toJson(publicZhuanxianRequest);
							showLD("置顶中...");
							executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
									UrlCat.URL_SUBMIT, BaseBean.class, null,
									ParamsBuilder.submitParams(jsonRequest),
									new Listener<BaseBean>() {

										@Override
										public void onResponse(BaseBean arg0) {
											if (arg0 != null) {
												if (TextUtils.equals(arg0.getCode(), "1")) {
													MyToast.showToast(getActivity(),
															"置顶成功");
												} else {
													MyToast.showToast(getActivity(),
															"置顶失败");
												}
											}

											cancelLD();
										}
									}, new ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError arg0) {
											cancelLD();
											MyToast.showToast(getActivity(), "发布失败");
										}
									}));
						}
					});
					//重发
					holder.btn_resend.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String _id = dataList.get(position).get_id();
							String operate = "M";
							SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");       
							String date = sDateFormat.format(new java.util.Date());  
							Data hydata = new Data("",date);
							PublicZhuanxianRequest publicZhuanxianRequest = 
							new PublicZhuanxianRequest(operate,new Gson().toJson(hydata),_id,"10071");
							String jsonRequest = new Gson().toJson(publicZhuanxianRequest);
							showLD("重发中...");
							executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
									UrlCat.URL_SUBMIT, BaseBean.class, null,
									ParamsBuilder.submitParams(jsonRequest),
									new Listener<BaseBean>() {

										@Override
										public void onResponse(BaseBean arg0) {
											if (arg0 != null) {
												if (TextUtils.equals(arg0.getCode(), "1")) {
													MyToast.showToast(getActivity(),
															"重发成功");
												} else {
													MyToast.showToast(getActivity(),
															"重发失败");
												}
											}

											cancelLD();
										}
									}, new ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError arg0) {
											cancelLD();
											MyToast.showToast(getActivity(), "发布失败");
										}
									}));
						}
					});
					return convertView;
				}

			}

			static class ViewHolder {

				TextView tv_chufadi, tv_mudidi, tv_jingting,
				btn_delete,btn_edit,btn_top,btn_resend;
				ImageView img_call,iv_pic;
				/**
				 *封装holder获取方法
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
					holder.tv_jingting = (TextView) convertView
							.findViewById(R.id.tv_jingting);
					holder.img_call = (ImageView) convertView
							.findViewById(R.id.img_call);
					holder.iv_pic = (ImageView) convertView
							.findViewById(R.id.iv_pic);
					holder.btn_delete = (TextView) convertView
							.findViewById(R.id.btn_delete);
					holder.btn_edit = (TextView) convertView
							.findViewById(R.id.btn_edit);
					holder.btn_top = (TextView) convertView
							.findViewById(R.id.btn_top);
					holder.btn_resend = (TextView) convertView
							.findViewById(R.id.btn_resend);
					
					return holder;
				}

			}
			
			class PublicZhuanxianRequest {
				private String operate;
				private String type;
				private String data;
				private String key;

				public PublicZhuanxianRequest(String operate,String data,String key,String type) {
					this.operate = operate;
					this.type = type;
					this.data = data;
					this.key = key;
				}
			}

			class Data {
				private String is_top;// 
				private String createtime;//
				public Data(String is_top,String createtime){
					this.is_top = is_top;
					this.createtime = createtime;
					}
				}
			/**
			 * 上拉刷新
			 * page++
			 */
			@Override
			public void onLoad() {
				pagenum++;
				loadData(chufadi, mudidi, linetype);
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
					case 1:
						chufadi = bundle.getString("cityCode");
						LogUtil.logWrite("MSZ_TAG", "chufadiCode==>" + chufadi);
						onRefresh();
						break;
					case 2:
						mudidi = bundle.getString("cityCode");
						LogUtil.logWrite("MSZ_TAG", "mudidiCode==>" + mudidi);
						onRefresh();
						break;

					default:
						break;
					}
				}

			};

			private String title = "选择";
			private Builder builder = null;

			private void showView(String... po) {

				if (builder == null) {
					builder = new AlertDialog.Builder(getActivity());
				}

				@SuppressWarnings("unused")
				AlertDialog dialog = builder
						.setTitle(title)
						.setSingleChoiceItems(po, 0,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int item) {
										linetype = (item ) + "";
										onRefresh();
										dialog.dismiss();
									}
								}).show();
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_chufadi:
					if (cityPickerPopupwindow != null)
						cityPickerPopupwindow.show(1,"search");
					break;
				case R.id.tv_mudidi:
					if (cityPickerPopupwindow != null)
						cityPickerPopupwindow.show(2,"search");
					break;
				case R.id.tv_linetype:
					showView(new String[] { "直达", "中转"});
					break;

				default:
					break;
				}

			}

}
