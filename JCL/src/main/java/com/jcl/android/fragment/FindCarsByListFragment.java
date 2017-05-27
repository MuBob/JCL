package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.jcl.android.bean.FindCarsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.ScrollListenerHorizontalScrollView.ScrollType;
import com.jcl.android.view.ScrollListenerHorizontalScrollView.ScrollViewListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 列表查询货物页面
 * @author msz
 * 需实现两个接口，刷新相关
 *
 */
@SuppressLint("ResourceAsColor")
public class FindCarsByListFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;
	private com.jcl.android.view.ScrollListenerHorizontalScrollView  hsv_tab;
	private TextView tv_chufadi, tv_mudidi, tv_zhuanghuoshijian, tv_chexing,tv_carlength;
	private TextView tv_jj;//紧急程度
	private TextView tv_cartype;//车源类型
	private TextView tv_pingtai;//平台选择
	private List<FindCarsListBean.Cars> dataList;
	private FindCarsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String chufadi = "", mudidi = "", zhuanghuoshijian = "",
			chexing = "",chechang="",jinji="",pingtai="",cartype="";
	private String chufadicode = "", mudidicode = "", chexingcode = "",
			chechangcode="",jinjicode="",pingtaicode="",cartypecode="";

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;
	private TextView tv_noinfo;
	
	
	String[] chexingArr; //车型数组
	String[] chechangArr;//车长数组
	String[] jinjichengduArr;//紧急程度数组
	String[] pingtaiArr;//平台选择数组
	String[] carTypeArr;//车源类型数组
	
	private ImageButton img_more;
	private View ll_more;//
	private boolean isRight=false;
	
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) { 
             
             
             }   
             super.handleMessage(msg);   
        }   
   };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_find_by_list, container,
				false);
		initView();
		return root;
	}

	private void initView() {

		hsv_tab=(com.jcl.android.view.ScrollListenerHorizontalScrollView ) root.findViewById(R.id.hsv_tab);
		hsv_tab.setHandler(myHandler);
		hsv_tab.setOnScrollStateChangedListener(new ScrollViewListener() {

			@Override
			public void onScrollChanged(ScrollType scrollType) {
				// TODO Auto-generated method stub
				switch (scrollType) {
				case IDLE:
					View view = (View) hsv_tab.getChildAt(hsv_tab
                			.getChildCount() - 1);
                	int viewRight=view.getRight();
                	int subViewWidth = hsv_tab.getRight();

                	int hsv_tab_x = hsv_tab.getScrollX();
                	

                	if(hsv_tab_x+subViewWidth<viewRight-20)
                	{
                		isRight=true;
                		img_more.setImageDrawable(getResources().getDrawable(R.drawable.right));
                	}else{
                		isRight=false;
                		img_more.setImageDrawable(getResources().getDrawable(R.drawable.left));
                	}
                    break; 

				default:
					break;
				}
			}
			

		});
		//数组数据初始化
		chexingArr=getResources().getStringArray(R.array.chexing);
		chechangArr=getResources().getStringArray(R.array.chechang);
		jinjichengduArr=getResources().getStringArray(R.array.jinjichengdu2);
		pingtaiArr=getResources().getStringArray(R.array.pingtaixuanze);
		carTypeArr=getResources().getStringArray(R.array.cartype);
		
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_zhuanghuoshijian = (TextView) root
				.findViewById(R.id.tv_zhuanghuoshijian);
		tv_zhuanghuoshijian.setText("空车时间");
		tv_chexing = (TextView) root.findViewById(R.id.tv_chexing);
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		tv_noinfo=(TextView)root.findViewById(R.id.tv_noinfo);
		
		tv_carlength=(TextView) root.findViewById(R.id.tv_carlength);
		tv_jj=(TextView) root.findViewById(R.id.tv_jj);
		tv_cartype=(TextView) root.findViewById(R.id.tv_cartype);
		tv_pingtai=(TextView) root.findViewById(R.id.tv_pingtai);
		ll_more=root.findViewById(R.id.ll_more);
		ll_more.setOnClickListener(this);
		img_more=(ImageButton)root.findViewById(R.id.img_more);
		img_more.setOnClickListener(this);
		//添加刷新事件
		srLayout.setOnRefreshListener(this);
		srLayout.setOnLoadListener(this);
		
		tv_chufadi.setOnClickListener(this);
		tv_mudidi.setOnClickListener(this);
		tv_zhuanghuoshijian.setOnClickListener(this);
		tv_chexing.setOnClickListener(this);
		tv_carlength.setOnClickListener(this);
		tv_jj.setOnClickListener(this);
		tv_cartype.setOnClickListener(this);
		tv_pingtai.setOnClickListener(this);
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

	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dataList = new ArrayList<FindCarsListBean.Cars>();
		mAdapter = new FindCarsAdapter();
		lv_find_by_list.setAdapter(mAdapter);
		loadData(chufadi, mudidi, zhuanghuoshijian, chexingcode,chechang,jinjicode,cartypecode,pingtaicode);
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
	/**
	 * 列表查询数据
	 * @param EQ_startareacode
	 * @param EQ_endareacode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData(String EQ_startareacode, String EQ_endareacode,
			String EQ_exfhstarttime, String LK_cartype,String EQ_chechang,
			String EQ_jjdegreecode,String EQ_cytypecode,String EQ_commonycode) {
		String longitude = "", 
				latitude = "";
		//拼接filter中数据
		String filters =  "EQ_startcode:" + EQ_startareacode + ",EQ_endcode:"
				+ EQ_endareacode + ",EQ_exfhstarttime:" + EQ_exfhstarttime
				+ ",EQ_cartypecode:" + LK_cartype+",EQ_carlength:"+EQ_chechang 
				+",EQ_jjdegreecode:"+EQ_jjdegreecode+",EQ_cytypecode:"+EQ_cytypecode
				+",EQ_commonycode:"+EQ_commonycode+ ",longitude:" + longitude
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
						MyToast.showToast(getActivity(), arg0.getMessage());
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
		loadData(chufadi, mudidi, zhuanghuoshijian, chexingcode,chechang,jinjicode,cartypecode,pingtaicode);
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.listitem_find_car, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_starttime.setText(dataList.get(position).getEmptytimestart());
			holder.tv_endtime.setText("".equals(dataList.get(position).getEmptytimeend())
					?Utils.addData(dataList.get(position).getEmptytimestart())
							:dataList.get(position).getEmptytimeend());
			
			holder.tv_name.setText(dataList.get(position).getLinkman());
			
			holder.tv_price.setText(TextUtils.isEmpty(dataList.get(position).getExpectfee())?"面议":dataList.get(position).getExpectfee()+"元");
			
			holder.btn_delete.setVisibility(View.GONE);
			holder.btn_resend.setVisibility(View.GONE);
			holder.btn_edit.setVisibility(View.GONE);
			if("加急".equals(dataList.get(position).getJjdegree()))
			{
				holder.tv_jjdegrees.setText(dataList.get(position).getJjdegree());
				holder.tv_jjdegrees.setBackgroundResource(R.drawable.btn_red_selector);
			}else{
				holder.tv_jjdegrees.setText(dataList.get(position).getJjdegree());
				holder.tv_jjdegrees.setBackgroundResource(R.drawable.btn_login_selector);
			}
			
			holder.tv_cartype.setText(dataList.get(position).getCytype());
			holder.tv_pingtai.setText(dataList.get(position).getCommony());
			holder.tv_creattime.setText(dataList.get(position).getCreatetime());
			
			ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+dataList.get(position).getCarimage1(), holder.img_car_header);
			String chexing = "";
				chexing=TextUtils.isEmpty(dataList.get(position).getCartype())?"暂无":dataList.get(position).getCartype();
			if(!"集装箱车".equals(chexing))
			{
				holder.tv_chexing.setText(chexing + "\t"
						+ (TextUtils.isEmpty(dataList.get(position).getCarlength())?"暂无":(dataList.get(position).getCarlength() + "米")));
			}else
			{
				holder.tv_chexing.setText(chexing );
			}
			if(TextUtils.equals("1", dataList.get(position).getIscheck()))
            {
			 holder.tv_car_code.setText(dataList.get(position).getPlatenum());
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
			
			if (!Utils.isEmpty(dataList.get(position).getGone())
					&& dataList.get(position).getGone().equals("0")) {
				holder.tv_gone.setVisibility(View.GONE);
			} else {
				holder.tv_gone.setVisibility(View.VISIBLE);
			}
			
			
			if(TextUtils.isEmpty(dataList.get(position).getPosition())){
				holder.tv_dangqian.setText("当前位置：用户没有上传当前位置");
			}else{
				holder.tv_dangqian.setText("当前位置："+dataList.get(position).getPosition());
			}
			
			final int p=position;
			
			return convertView;
		}

	}

	static class ViewHolder {

		TextView tv_name,tv_car_code,tv_price,tv_starttime,tv_endtime;
		TextView tv_chufadi, tv_mudidi, tv_chexing;
		TextView btn_delete,btn_resend,btn_edit;
		ImageView img_car_header,img_call,img_check1,img_check2;
		
		TextView tv_jjdegrees,tv_cartype,tv_pingtai,tv_gone;
		TextView tv_creattime;
		TextView tv_dangqian;//当前位置
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
			holder.tv_dangqian = (TextView) convertView
					.findViewById(R.id.tv_dangqian);
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
			holder.tv_gone = (TextView) convertView
					.findViewById(R.id.tv_gone);
			
			
			holder.tv_jjdegrees = (TextView) convertView
					.findViewById(R.id.tv_jjdegrees);
			holder.tv_cartype = (TextView) convertView
					.findViewById(R.id.tv_cartype);
			holder.tv_pingtai = (TextView) convertView
					.findViewById(R.id.tv_pingtai);
			holder.tv_creattime=(TextView)convertView
					.findViewById(R.id.tv_creattime);
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
		loadData(chufadi, mudidi, zhuanghuoshijian, chexingcode,chechang,jinjicode,cartypecode,pingtaicode);
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
				tv_chufadi.setText(info);
				LogUtil.logWrite("MSZ_TAG", "chufadiCode==>" + chufadi);
				onRefresh();
				break;
			case 2:
				mudidi = bundle.getString("cityCode");
				tv_mudidi.setText(info);
				LogUtil.logWrite("MSZ_TAG", "mudidiCode==>" + mudidi);
				onRefresh();
				break;
			case 3:
				zhuanghuoshijian = info;
				tv_zhuanghuoshijian.setText(info);
				onRefresh();
				break;

			default:
				break;
			}
		}
	};

	private String title = "选择";
	private Builder builder = null;

	private void showView(String title,final int viewId,String... po) {

		if (builder == null) {
			builder = new AlertDialog.Builder(getActivity());
		}

		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle(title)
				.setSingleChoiceItems(po, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								switch (viewId) {
								case R.id.tv_chexing:
									chexingcode = (item) + "";
									chexing=chexingArr[item];
									tv_chexing.setText(chexingArr[item]);
									onRefresh();
									break;
								case R.id.tv_carlength:
									chechangcode=(item)+"";
									chechang=chechangArr[item].substring(0, chechangArr[item].length()-1);
									if("不限".equals(chechangArr[item])){
										chechang="0";
									}
									tv_carlength.setText(chechangArr[item]);
									onRefresh();
									break;
								case R.id.tv_cartype:
									cartypecode=(item)+"";
									cartype=carTypeArr[item];
									tv_cartype.setText(carTypeArr[item]);
									onRefresh();
									break;
								case R.id.tv_jj:
									jinjicode=(item)+"";
									jinji=jinjichengduArr[item];
									tv_jj.setText(jinjichengduArr[item]);
									onRefresh();
									break;
								case R.id.tv_pingtai:
									pingtaicode=(item)+"";
									pingtai=pingtaiArr[item];
									tv_pingtai.setText(pingtaiArr[item]);
									onRefresh();
									break;
								default:
									break;
								}
								
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
		case R.id.tv_zhuanghuoshijian:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(3);
			break;
		case R.id.tv_chexing:
			
			showView("车型选择",v.getId(),chexingArr);
			break;
		case R.id.tv_carlength:
			
			showView("车长选择",v.getId(),chechangArr);
			break;
		case R.id.tv_cartype:
	
			showView("类型",v.getId(),carTypeArr);
			break;
		case R.id.tv_jj:
	
			showView("紧急程度选择",v.getId(),jinjichengduArr);
			break;
		case R.id.tv_pingtai:
	
			showView("平台选择",v.getId(),pingtaiArr);
			break;
		case R.id.ll_more:
		case R.id.img_more:
        	int subViewWidth = hsv_tab.getRight();
        	int y=(int)hsv_tab.getY();
        	
        	if(isRight){
        		hsv_tab.scrollTo(subViewWidth, y);
        		isRight=false;
        		img_more.setImageDrawable(getResources().getDrawable(R.drawable.left));
        	}else
        	{
        		hsv_tab.scrollTo(0, y);
        		isRight=true;
        		img_more.setImageDrawable(getResources().getDrawable(R.drawable.right));
        	}
        	
			
			
			break;
		default:
			break;
		}

	}

}
