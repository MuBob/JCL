package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.jcl.android.activity.AddCarActivity;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.PublishCarActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.FindCarsListBean;
import com.jcl.android.interfaces.OnItemBtnClickListener;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 列表查询
 * 
 * @author pb 需实现两个接口，刷新相关
 *
 */
public class SettingPublicCarsFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private TextView tv_chufadi, tv_mudidi, tv_zhuanghuoshijian, tv_chexing;
	private List<FindCarsListBean.Cars> dataList;
	private FindCarsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String chufadi = "", mudidi = "", zhuanghuoshijian = "",
			chexing = "";
	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;
	private TextView tv_noinfo;
	private int position;

	public static SettingPublicCarsFragment newInstance(int position) {
		SettingPublicCarsFragment f = new SettingPublicCarsFragment();
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
		root = inflater.inflate(R.layout.fragment_setting_public_cars,
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
			this.type = "2002";
			this.sort = "";
			this.pagesize = C.PAGE_LIMIT;
		}

	}

	public static int EDIT_CARS=0X00001;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindCarsListBean.Cars>();
		mAdapter = new FindCarsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		mAdapter.setOnItemBtnClickListener(new OnItemBtnClickListener() {
			
			@Override
			public void onItemBtnClick(View v, int position) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_delete:
					delete(dataList.get(position).get_id());
					break;
				case R.id.btn_resend:
					resend(dataList.get(position).get_id());				
					break;
				case R.id.btn_edit:
					startActivityForResult(new Intent(getActivity(),PublishCarActivity.class)
						.putExtra("id", dataList.get(position).get_id()), EDIT_CARS);
					break;

				default:
					break;
				}
			}
		});
		loadData(chufadi, mudidi, zhuanghuoshijian, chexing);
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataList != null && dataList.size() - 1 >= position) {
					startActivity(DetailFindActivity.newInstance(getActivity(),
							C.INTENT_TYPE_FIND_CAR,dataList.get(position).get_id()));
				}
			}
		});
	}
	
	String resendjsonRequest;
	class ResendData{
		private String vanid;
		public ResendData(String vanid)
		{
			this.vanid=vanid;
		}
	}
	private void resend(String id){
		resendjsonRequest= new Gson().toJson(new PublishCarRequest(new Gson().toJson(new ResendData(id)), "", "A","1018"));
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(resendjsonRequest),
				new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(getActivity(), "重发成功!",
										1000).show();
								onRefresh();
							}else{
								Toast.makeText(getActivity(), arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(getActivity(), "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "网络连接异常！",
								1000).show();
					}
				}));
	}
	
	
	class PublishCarRequest {
		private String type;
		private String data;
		private String operate;
		private String key;

		public PublishCarRequest(String data) {
			this.type = "1006";
			this.data = data;
		}
		
		public PublishCarRequest(String data,String key,String operate) {
			this.type = "1006";
			this.data = data;
			this.key=key;
			this.operate=operate;
		}
		
		public PublishCarRequest(String data,String key,String operate,String type) {
			this.type = type;
			this.data = data;
			this.key=key;
			this.operate=operate;
		}
	}
	
	String deletejsonRequest;
	private void delete(String id)
	{
		deletejsonRequest= new Gson().toJson(new PublishCarRequest("{}", id, "R"));
		
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(deletejsonRequest), new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(getActivity(), arg0.getMsg(),
										1000).show();
								onRefresh();
							}else{
								Toast.makeText(getActivity(), arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(getActivity(), "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "网络连接异常！",
								1000).show();
					}
				}));

	}
	
	
	
	/**
	 * 列表查询数据
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData(String EQ_startareacode, String EQ_endareacode,
			String EQ_exfhstarttime, String LK_cartype) {
		String longitude = "", latitude = "";
		//拼接filter中数据
		String filters = "userid:"+SharePerfUtil.getUserId()+ ",EQ_startareacode:" + EQ_startareacode + ",EQ_endareacode:"
				+ EQ_endareacode + ",EQ_exfhstarttime:" + EQ_exfhstarttime
				+ ",EQ_cartype:" + LK_cartype + ",longitude:" + longitude
				+ ",latitude:" + latitude;
		String getStr = new Gson().toJson(new GetStr(pagenum, filters));
		//检测是否是第一页
		final boolean isFromTop = pagenum == 1;
		//添加刷新小图标显示
		if (!srLayout.isRefreshing()) {
			srLayout.setRefreshing(true);
		}
		executeRequest(new GsonRequest<FindCarsListBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, FindCarsListBean.class, null,
				ParamsBuilder.pageSearchParams(getStr),
				new Listener<FindCarsListBean>() {

					@Override
					public void onResponse(FindCarsListBean arg0) {
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
//						MyToast.showToast(getActivity(), arg0.getMessage());
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
		loadData(chufadi, mudidi, zhuanghuoshijian, chexing);
	}

	class GoneData{
		private String gone;
		public GoneData(String gone)
		{
			this.gone=gone;
		}
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
		private OnItemBtnClickListener onItemBtnClickListener;
	    public void setOnItemBtnClickListener(OnItemBtnClickListener onItemBtnClickListener) {
	        this.onItemBtnClickListener = onItemBtnClickListener;
	    }

		@SuppressLint("ResourceAsColor") @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_settingcar, null);
			}
			final ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_starttime.setText(dataList.get(position).getEmptytimestart());
			holder.tv_endtime.setText(dataList.get(position).getEmptytimeend());
			holder.tv_name.setText(dataList.get(position).getLinkman());
			holder.tv_car_code.setText(dataList.get(position).getPlatenum());
			if (Utils.isEmpty(dataList.get(position).getExpectfee())) {
				holder.tv_price.setText("电询");
			} else {
				holder.tv_price.setText(dataList.get(position).getExpectfee());
			}
			
			holder.tv_jjdegrees.setText(dataList.get(position).getJjdegree());
			holder.tv_cartype.setText(dataList.get(position).getCytype());
			holder.tv_pingtai.setText(dataList.get(position).getCommony());
			ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+dataList.get(position).getCarimage1(), holder.img_car_header);
			String chexing = "";
				chexing=dataList.get(position).getCartype();
			holder.tv_chexing.setText(chexing + "\t"
					+ dataList.get(position).getCarlength() + "米");
			holder.tv_creattime.setText(dataList.get(position).getCreatetime());
			
			if(TextUtils.equals("1", dataList.get(position).getIscheck()))
            {
           	 holder.img_check1.setVisibility(View.VISIBLE);
            }else{
           	 holder.img_check1.setVisibility(View.GONE);
            }
			if(TextUtils.equals("2", dataList.get(position).getIscheck()))
            {
           	 holder.img_check2.setVisibility(View.VISIBLE);
            }else{
           	 holder.img_check2.setVisibility(View.GONE);
            }

			if(dataList.get(position).getGone()!=null){
				if (dataList.get(position).getGone().equals("0")) {
					holder.btn_cheyizou.setText("发车");
					holder.btn_cheyizou.setTextColor(Color.RED);
				} else {
					holder.btn_cheyizou.setText("车已走");
				}

			}
			
			if (!Utils.isEmpty(dataList.get(position).getGone()) && dataList.get(position).getGone().equals("0")) {
//				holder.tv_huoyifa.setTextColor(Color.RED);
			} else {
				holder.btn_cheyizou.setBackgroundColor(R.color.gray);
				holder.btn_cheyizou.setTextColor(R.color.white);
				holder.btn_cheyizou.setClickable(false);
				holder.btn_cheyizou.setEnabled(false);
				holder.tv_chufadi.setTextColor(R.color.defult_text);
				holder.tv_mudidi.setTextColor(R.color.defult_text);
				holder.tv_starttime.setTextColor(R.color.defult_text);
				holder.tv_endtime.setTextColor(R.color.defult_text);
				holder.tv_name.setTextColor(R.color.defult_text);
				holder.tv_car_code.setTextColor(R.color.defult_text);
			}
			
			final int p=position;
			holder.btn_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onItemBtnClickListener!=null){
                        onItemBtnClickListener.onItemBtnClick(v,p);
                    }
				}
			});
			holder.btn_resend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onItemBtnClickListener!=null){
                        onItemBtnClickListener.onItemBtnClick(v,p);
                    }
				}
			});
			holder.btn_edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onItemBtnClickListener!=null){
                        onItemBtnClickListener.onItemBtnClick(v,p);
                    }
				}
			});

			holder.btn_cheyizou.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String jsonRequest= new Gson().toJson
							(new PublishCarRequest(new Gson().toJson
									(new GoneData("1")), 
									dataList.get(position).get_id(), "M","1006"));
					executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
							UrlCat.URL_SUBMIT, BaseBean.class, null,
							new ParamsBuilder().submitParams(jsonRequest),
							new Listener<BaseBean>() {
								@Override
								public void onResponse(BaseBean arg0) {
									// TODO Auto-generated method stub
									if(arg0!=null){
										if ("1".equals(arg0.getCode())) {
											Toast.makeText(getActivity(), "车已开走，对外通知该车源已失效!",
													1000).show();
											holder.tv_chufadi.setTextColor(R.color.defult_text);
											holder.tv_mudidi.setTextColor(R.color.defult_text);
											holder.tv_starttime.setTextColor(R.color.defult_text);
											holder.tv_endtime.setTextColor(R.color.defult_text);
											holder.tv_name.setTextColor(R.color.defult_text);
											holder.tv_car_code.setTextColor(R.color.defult_text);
											onRefresh();
										}else{
											Toast.makeText(getActivity(), arg0.getMsg(),
													1000).show();
										}
									}else{
										Toast.makeText(getActivity(), "暂无数据！",
												1000).show();
									}
								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError arg0) {
									// TODO Auto-generated method stub
									Toast.makeText(getActivity(), "网络连接异常！",
											1000).show();
								}
							}));	
				}
			});
			
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_name,tv_car_code,tv_price,tv_starttime,tv_endtime;
		TextView tv_chufadi, tv_mudidi, tv_chexing,tv_creattime;
		TextView btn_delete,btn_resend,btn_edit,btn_cheyizou;
		ImageView img_car_header,img_call,img_check1,img_check2;
		TextView tv_jjdegrees,tv_cartype,tv_pingtai;
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
			holder.tv_starttime = (TextView) convertView
					.findViewById(R.id.tv_starttime);
			holder.tv_endtime = (TextView) convertView
					.findViewById(R.id.tv_endtime);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_car_code = (TextView) convertView
					.findViewById(R.id.tv_car_code);
			holder.tv_chufadi = (TextView) convertView
					.findViewById(R.id.tv_chufadi);
			holder.tv_mudidi = (TextView) convertView
					.findViewById(R.id.tv_mudidi);
			holder.tv_chexing = (TextView) convertView
					.findViewById(R.id.tv_chexing);
			holder.tv_creattime = (TextView) convertView
					.findViewById(R.id.tv_creattime);
			holder.img_car_header = (ImageView) convertView
					.findViewById(R.id.img_car_header);
			holder.img_call = (ImageView) convertView
					.findViewById(R.id.img_call);
			holder.img_check1 = (ImageView) convertView
					.findViewById(R.id.img_check1);
			holder.img_check2 = (ImageView) convertView
					.findViewById(R.id.img_check2);
			holder.btn_delete = (TextView) convertView
					.findViewById(R.id.btn_delete);
			holder.btn_resend = (TextView) convertView
					.findViewById(R.id.btn_resend);
			holder.btn_edit = (TextView) convertView
					.findViewById(R.id.btn_edit);
			holder.btn_cheyizou = (TextView) convertView
					.findViewById(R.id.btn_cheyizou);
			
			
			holder.tv_jjdegrees = (TextView) convertView
					.findViewById(R.id.tv_jjdegrees);
			holder.tv_cartype = (TextView) convertView
					.findViewById(R.id.tv_cartype);
			holder.tv_pingtai = (TextView) convertView
					.findViewById(R.id.tv_pingtai);
			
			return holder;
		}

	}
	/**
	 * 上拉刷新
	 * page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
		loadData(chufadi, mudidi, zhuanghuoshijian, chexing);
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
			case 3:
				zhuanghuoshijian = info;
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
								chexing = (item + 1) + "";
								onRefresh();
								dialog.dismiss();
							}
						}).show();
	}

	@Override
	public void onClick(View v) {

	}


}
