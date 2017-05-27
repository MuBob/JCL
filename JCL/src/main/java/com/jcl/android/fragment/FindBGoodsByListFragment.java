package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import com.jcl.android.SP;
import com.jcl.android.activity.AddCarActivity;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.activity.FindGoodsActivity;
import com.jcl.android.activity.LoginActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.base.BaseSwipeRefreshLayout;
import com.jcl.android.base.BaseSwipeRefreshLayout.OnLoadListener;
import com.jcl.android.bean.FindGoodsListBean;
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
import com.jcl.android.view.ScrollListenerHorizontalScrollView.ScrollType;
import com.jcl.android.view.ScrollListenerHorizontalScrollView.ScrollViewListener;

/**
 * 列表查询货物页面
 * 
 * @author msz 需实现两个接口，刷新相关
 *
 */
@SuppressLint("ResourceAsColor")
public class FindBGoodsByListFragment extends BaseFragment implements
		OnRefreshListener, OnLoadListener, OnClickListener {
	private View root;
	private ListView lv_find_by_list;

	private TextView tv_chufadi, tv_mudidi, tv_zhuanghuoshijian, tv_chexing,tv_carlength;
	private TextView tv_jj;//紧急程度
	private TextView tv_cartype;//车源类型
	private TextView tv_pingtai;//平台选择
	private List<FindGoodsListBean.Goods> dataList;
	private FindGoodsAdapter mAdapter;
	private BaseSwipeRefreshLayout srLayout;
	private int pagenum = 1;
	private String chufadi = "", mudidi = "", zhuanghuoshijian = "",
			chexing = "",chechang="",jinji="",pingtai="",cartype="";
	private String chufadicode = "", mudidicode = "", chechangcode="",
			chexingcode = "",jinjicode="",pingtaicode="",cartypecode="";

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;

	private ImageButton img_more;
	private View ll_more;//
	private boolean isRight=false;
	String[] chexingArr; //车型数组
	String[] chechangArr;//车长数组
	String[] jinjichengduArr;//紧急程度数组
	String[] pingtaiArr;//平台选择数组
	String[] carTypeArr;//车源类型数组
	private com.jcl.android.view.ScrollListenerHorizontalScrollView  hsv_tab;
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
		jinjichengduArr=getResources().getStringArray(R.array.jinjichengdu);
		pingtaiArr=getResources().getStringArray(R.array.pingtaixuanze);
		carTypeArr=getResources().getStringArray(R.array.cartype);
		
		
		
		lv_find_by_list = (ListView) root.findViewById(R.id.lv_find_by_list);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_zhuanghuoshijian = (TextView) root
				.findViewById(R.id.tv_zhuanghuoshijian);
		tv_zhuanghuoshijian.setText("装货时间");
		tv_chexing = (TextView) root.findViewById(R.id.tv_chexing);
		
		srLayout = (BaseSwipeRefreshLayout) root.findViewById(R.id.sr_layout);
		srLayout.setEnabled(false);
		
		
		tv_carlength=(TextView) root.findViewById(R.id.tv_carlength);
		tv_jj=(TextView) root.findViewById(R.id.tv_jj);
		tv_cartype=(TextView) root.findViewById(R.id.tv_cartype);
		tv_cartype.setVisibility(View.GONE);
		tv_pingtai=(TextView) root.findViewById(R.id.tv_pingtai);
		
		ll_more=root.findViewById(R.id.ll_more);
		ll_more.setOnClickListener(this);
		img_more=(ImageButton)root.findViewById(R.id.img_more);
		img_more.setOnClickListener(this);
		// 添加刷新事件
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
			this.type = "2000";
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
		loadData(chufadicode, mudidicode, zhuanghuoshijian, chexing,chechang,jinjicode,cartypecode,pingtaicode);
		lv_find_by_list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if (arg1 == 0) {
					srLayout.setEnabled(true);
				} else {
					srLayout.setEnabled(false);
				}
			}
		});
		lv_find_by_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
					MyToast.showToast(getActivity(), "请先登陆");
					startActivity(new Intent(getActivity(),LoginActivity.class));
					return;
				} else {
					if (dataList != null && dataList.size() - 1 >= position) {
						startActivity(DetailFindActivity.newInstance(getActivity(),
								C.INTENT_TYPE_FIND_GOODS,dataList.get(position).get_id()));
					}
				}
			}
		});
	}

	/**
	 * 列表查询数据
	 * 
	 * @param EQ_startcode
	 * @param EQ_endcode
	 * @param EQ_exfhstarttime
	 * @param LK_cartype
	 */
	private void loadData(String EQ_startareacode, String EQ_endareacode,
			String EQ_exfhstarttime, String LK_cartype,String EQ_chechang,
			String EQ_jjdegreecode,String EQ_cytypecode,String EQ_commonycode) {
		String longitude = "", latitude = "";
		// 拼接filter中数据
		String filters = "EQ_userid:" + ",EQ_startcode:" + EQ_startareacode
				+ ",EQ_endcode:" + EQ_endareacode + ",EQ_exfhstarttime:"
				+ EQ_exfhstarttime + ",LK_carname:" + LK_cartype.toString()+",EQ_carlength:"+EQ_chechang
				+",EQ_jjdegree:"+EQ_jjdegreecode+",EQ_cartype:"+EQ_cytypecode
				+",EQ_ptchoose:"+EQ_commonycode
				+ ",longitude:" + longitude + ",latitude:" + latitude;
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
//		pagenum = 1;
//		loadData(chufadicode, mudidicode, zhuanghuoshijian, chexingcode);
		
		pagenum = 1;
		loadData(chufadicode, mudidicode, zhuanghuoshijian, chexing,chechang,jinjicode,cartypecode,pingtaicode);
		srLayout.setRefreshing(true);
		( new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
				srLayout.setRefreshing(false);
			}
		}, 3000);
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
						R.layout.listitem_find_goods, null);
			}
			ViewHolder holder = ViewHolder.getViewHolder(convertView);
			holder.tv_chufadi.setText(dataList.get(position).getStartarea());
			holder.tv_mudidi.setText(dataList.get(position).getEndarea());
			holder.tv_chufashijian.setText("装货："+dataList.get(position)
					.getExfhstarttime() + "~" + dataList.get(position).getExfhendtime());
			
			String chechang = dataList.get(position).getCarlength();
//			String[] chechangs = getResources().getStringArray(
//					R.array.chechang);
//			ArrayList<String> carlength = new ArrayList<String>();
//			for (int i = 0; i < chechangs.length; i++) {
//				carlength.add(chechangs[i]);
//			}
//			for (int i = 0; i < carlength.size(); i++) {
//				if (dataList.get(position).getCarlength().equals(i+"")) {
//					chechang = carlength.get(i);
//				}
//			}
			String[] jinji = getResources().getStringArray(R.array.jinjichengdu);
			holder.tv_ji.setText(jinji[Integer.valueOf(dataList.get(position).getJjdegree())]);
			String jjdegree = dataList.get(position).getJjdegree()+"";
			if("0".equals(jjdegree)){
				holder.tv_ji.setBackgroundResource(R.drawable.btn_login_selector);
			}else if("1".equals(jjdegree)){
				holder.tv_ji.setBackgroundResource(R.drawable.btn_green_selector);
			}else {
				holder.tv_ji.setBackgroundResource(R.drawable.btn_red_selector);
			}
			
			if (!TextUtils.isEmpty(dataList.get(position).getCarname())) {
				holder.tv_chexing.setVisibility(View.VISIBLE);
				
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
					holder.tv_chexing.setText("求"+dataList.get(position)
							.getCarname());
				} else {
					holder.tv_chexing.setText("求"+dataList.get(position)
							.getCarname()
							+chechang);
				}
				
			} else {
				holder.tv_chexing.setVisibility(View.INVISIBLE);
			}

			if (!TextUtils.isEmpty(dataList.get(position).getDetailname())) {
				holder.tv_goodsinfo.setVisibility(View.VISIBLE);
				holder.tv_goodsinfo.setText(dataList.get(position)
						.getDetailname()
						+ " "
						+ dataList.get(position).getGoodsweight()
						+ "公斤  "
						+ dataList.get(position).getGoodstj() + "立方米");
			} else {
				holder.tv_goodsinfo.setVisibility(View.GONE);
			}
			holder.tv_time.setText(Utils.getTimeFormatText(Utils.stringToDate(dataList.get(position).getCreatetime())));
			
			ImageLoaderUtil.getInstance(getActivity()).
			 loadImage(C.BASE_URL+dataList.get(position).
					getSourceimage(), holder.iv_head);
			
			if (!Utils.isEmpty(dataList.get(position).getGone())
					&& dataList.get(position).getGone().equals("0")) {
				holder.tv_qiang.setBackgroundResource(R.drawable.yuan_qiang);
			} else {
				holder.tv_qiang.setBackgroundResource(R.drawable.yuan_huoyizou);
			}
			return convertView;
		}
	}

	static class ViewHolder {

		TextView tv_chufadi, tv_mudidi, tv_chufashijian, tv_chexing,
				tv_goodsinfo,tv_time,tv_ji,tv_qiang;
		ImageView iv_head;

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
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.tv_ji = (TextView) convertView
					.findViewById(R.id.tv_ji);
			holder.tv_qiang = (TextView) convertView
					.findViewById(R.id.tv_qiang);
			holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);

			return holder;
		}

	}

	/**
	 * 上拉刷新 page++
	 */
	@Override
	public void onLoad() {
		pagenum++;
//		loadData(chufadicode, mudidicode, zhuanghuoshijian, chexingcode);
		loadData(chufadicode, mudidicode, zhuanghuoshijian, chexing,chechang,jinjicode,cartypecode,pingtaicode);
		
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
				chufadicode = bundle.getString("cityCode");
				tv_chufadi.setText(bundle.getString("cityName"));
				LogUtil.logWrite("syl", "chufadiCode==>" + chufadicode);
				onRefresh();
				break;
			case 2:
				mudidicode = bundle.getString("cityCode");
				tv_mudidi.setText(bundle.getString("cityName"));
				LogUtil.logWrite("syl", "mudidiCode==>" + mudidicode);
				onRefresh();
				break;
			case 3:
				zhuanghuoshijian = info;
				tv_zhuanghuoshijian.setText(zhuanghuoshijian);
				onRefresh();
				break;

			default:
				break;
			}
		}

	};

	private String title = "选择";
	private Builder builder = null;

	private void showView(String title,final int viewId,String... po){
		
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
									chechang=chechangArr[item].substring(0,chechangArr[item].length()-1);
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
									tv_jj.setText(jinjichengduArr[item]);
									onRefresh();
									break;
								case R.id.tv_pingtai:
									pingtaicode=(item)+"";
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
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
				if (cityPickerPopupwindow != null)
					cityPickerPopupwindow.show(1,"search");
			}
			
			break;
		case R.id.tv_mudidi:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(2,"search");
			}
			break;
		case R.id.tv_zhuanghuoshijian:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(3);
			}
			break;
		case R.id.tv_chexing:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			showView("车型选择",v.getId(),chexingArr);
			}
			break;
		case R.id.tv_carlength:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			showView("车长选择",v.getId(),chechangArr);
			}
			break;
		case R.id.tv_cartype:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			showView("类型选择",v.getId(),carTypeArr);
			}
			break;
		case R.id.tv_jj:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			showView("紧急程度选择",v.getId(),jinjichengduArr);
			}
			break;
		case R.id.tv_pingtai:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
				startActivity(new Intent(getActivity(),LoginActivity.class));
				return;
			} else {
			showView("平台选择",v.getId(),pingtaiArr);
			}
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
