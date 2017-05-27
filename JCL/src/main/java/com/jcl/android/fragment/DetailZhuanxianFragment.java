package com.jcl.android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.DetailZhuanxianBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.PayOnlinePopupwindow;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.utils.InfoUtils;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;

public class DetailZhuanxianFragment extends BaseFragment implements
		OnClickListener {

	private View root;
	private String goodsId;
	private ImageView iv_share, iv_collect,iv_back,upload_img,iv_zijian;
	private String userid;
	private TextView tv_company, tv_car_num, tv_tejia, tv_xianlu,tv_xinxi,
			tv_chufadi, tv_mudidi, tv_miaoshu;
	private LinearLayout ll_bottom;
	private boolean isChecked;
	private SharePopupwindow sharePopupwindow;
	private PayOnlinePopupwindow payOnlinePopupwindow;
	private TextView tv_pay_tel,tv_pay_online;
	private String telNum,payphone;

	private TextView tv_fahuoren,tv_fahuorenxinxi;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		goodsId = bundle.getString("id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_detail_zhuanxian, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		iv_share = (ImageView) root.findViewById(R.id.iv_share);
		iv_collect = (ImageView) root.findViewById(R.id.iv_collect);
		tv_company = (TextView) root.findViewById(R.id.tv_company);
		tv_car_num = (TextView) root.findViewById(R.id.tv_car_num);
		tv_miaoshu = (TextView) root.findViewById(R.id.tv_miaoshu);
		tv_xianlu = (TextView) root.findViewById(R.id.tv_xianlu);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_tejia = (TextView) root.findViewById(R.id.tv_tejia);
		tv_xinxi = (TextView) root.findViewById(R.id.tv_xinxi);
		tv_pay_tel = (TextView) root.findViewById(R.id.tv_pay_tel);
		tv_pay_online = (TextView) root.findViewById(R.id.tv_pay_online);
		ll_bottom = (LinearLayout) root.findViewById(R.id.ll_bottom);
		upload_img = (ImageView) root.findViewById(R.id.upload_img);
		iv_share.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		tv_pay_tel.setOnClickListener(this);
		tv_pay_online.setOnClickListener(this);
		iv_back = (ImageView) root.findViewById(R.id.iv_back);
		iv_zijian  = (ImageView) root.findViewById(R.id.iv_zijian);
		iv_back.setOnClickListener(this);
		
		tv_fahuoren = (TextView) root.findViewById(R.id.tv_fahuoren);
		tv_fahuorenxinxi = (TextView) root.findViewById(R.id.tv_fahuorenxinxi);
	}

	class DetailGoodsCollectRequest {
		private String filters;
		private String type;
		private String operate;
		private String data;

		public DetailGoodsCollectRequest(String data, String operate) {
			this.data = data;
			this.operate = operate;
			this.type = "1011";
		}
	}

	class Data {
		private String userid;
		private String type;// 收藏类型 1车源2货源 可扩展
		private String ppid;// 目标ID 比如车源收藏，则是车源ID
		private String name;// 名称
		private String remark;// 备注
		private String effectiveflag;// 有效flag 0无效,1有效

		public Data(String ppid) {
			this.remark = "";
			this.name = "";
			this.effectiveflag = "";
			this.type = "4";
			this.ppid = goodsId;
			this.userid = JCLApplication.getInstance().getUserId();
		}

	}

	public static DetailZhuanxianFragment newInstance(String id) {
		DetailZhuanxianFragment f = new DetailZhuanxianFragment();
		Bundle args = new Bundle();
		args.putString("id", id);
		f.setArguments(args);
		return f;
	}

	class DetailFindGoodsRequest {
		private String filters;
		private String type;
		private String sorts;

		public DetailFindGoodsRequest(String filters) {
			this.filters = filters;
			this.type = "2013";
			this.sorts = "";
		}
	}

	class Filters {
		private String _id;
		private String userid;

		public Filters(String _id, String userid) {
			this._id = _id;
			this.userid = userid;
		}

	}
	
	//在线报价----------------------------------------
	class PayOnlineRequest {
		private String type;
		private String operate;
		private String data;

		public PayOnlineRequest(String data) {
			this.data = data;
			this.operate = "A";
			this.type = "3001";
		}
	}

	class PayOnlineData {
		private String vanuserid;//车主id
		private String phone;// 联系电话
		private String goodsid;// 货主id
		private String price;// 价格

		public PayOnlineData(String vanuserid,String phone,String goodsid,String price) {
			this.vanuserid=vanuserid;
			this.phone=phone;
			this.goodsid=goodsid;
			this.price=price;
		}

	}

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if(msg.obj!=null)
				{
					String price=msg.obj.toString();
					String data = new Gson().toJson(new PayOnlineData(goodsId,telNum,"",price));
					String getStr = new Gson().toJson(new PayOnlineRequest(data));
					showLD("提交中...");
					executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
							UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
							null, null, new Listener<BaseBean>() {
								@Override
								public void onResponse(BaseBean arg0) {
									cancelLD();
									if (arg0 != null) {
										if (TextUtils.equals(arg0.getCode(), "1")) {
											MyToast.showToast(getActivity(),
													"在线报价成功");
										}
									}

								}
							}, new ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError arg0) {
									MyToast.showToast(getActivity(),
											"在线报价失败");
								}
							}));
					
					
				}
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String title= "hello, 誉畅物流";
		String content = "誉畅物流 让移动物流快速整合，http://www.chinajuchang.com";
		String url = "http://www.chinajuchang.com/d";
		sharePopupwindow = new SharePopupwindow(getActivity(), root.findViewById(R.id.ll_parent), null, null,title,content,url);
		payOnlinePopupwindow = new PayOnlinePopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), myHandler, null);
		if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
			userid = JCLApplication.getInstance().getUserId();
			iv_collect.setVisibility(View.VISIBLE);
		} else {
			userid = "";
			iv_collect.setVisibility(View.GONE);
		}
		loadData();
	}

	private void loadData() {
		String filters = new Gson().toJson(new Filters(goodsId, userid));
		String getStr = new Gson().toJson(new DetailFindGoodsRequest(filters));
		showLD("加载中...");
		executeRequest(new GsonRequest<DetailZhuanxianBean>(Request.Method.GET,
				UrlCat.getSearchUrl(getStr), DetailZhuanxianBean.class, null,
				null, new Listener<DetailZhuanxianBean>() {

					@Override
					public void onResponse(DetailZhuanxianBean arg0) {
						cancelLD();
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {

								if (TextUtils.equals(arg0.getData()
										.getIfcollect(), "1")) {
									iv_collect
											.setImageResource(R.drawable.icon_toorbar_collect_checked);
									isChecked = true;
								} else {
									iv_collect
											.setImageResource(R.drawable.icon_toolbar_collect_nomal);
									isChecked = false;
								}
								tv_company.setText(arg0.getData().getCompany());
								tv_car_num
										.setText(arg0.getData().getPlatenum());
								tv_miaoshu.setText(arg0.getData().getSale());
								if (Utils.isEmpty(arg0.getData().getXl_description())) {
									tv_tejia.setText(Html.fromHtml("<font color=\"#FF4040\">今日特价:</font>"+"暂无"));
								} else {
									tv_tejia.setText(Html.fromHtml("<font color=\"#ff000\">今日特价:</font>"+arg0.getData().getXl_description()));
								}
								String wprice,lprice,mprice;
								if (Utils.isEmpty(arg0.getData().getW_price())) {
									wprice = "";
								} else {
									wprice =arg0.getData().getW_price();
								}
								
								if (Utils.isEmpty(arg0.getData().getL_price())) {
									lprice = "";
								} else {
									lprice =arg0.getData().getL_price();
								}
								
								if (Utils.isEmpty(arg0.getData().getM_price())) {
									mprice = "";
								} else {
									mprice =arg0.getData().getM_price();
								}
								tv_xinxi.setText(Html.fromHtml("<font color=\"#1284B0\">价格:</font>"+
								        "重货："+wprice+"元/公斤<br>"+
										"轻货："+lprice+"元/立方<br>"+
										"最低一票："+mprice+"元"));
								tv_chufadi.setText(arg0.getData().getStartarea());
								tv_mudidi.setText(arg0.getData().getEndarea());
								telNum = InfoUtils.formatPhone(arg0.getData().getPhone());
								payphone = arg0.getData().getPhone();
								if ("1".equals(arg0.getData().getLinetype())) {
									tv_xianlu.setText("中转线路");
									if ("0".equals(arg0.getData().getIsstop())) {
										ll_bottom.setVisibility(View.VISIBLE);
									} else {
										ll_bottom.setVisibility(View.GONE);
									}
								} else {
									tv_xianlu.setText("直达线路");
									ll_bottom.setVisibility(View.GONE);
								}
								
								if (!Utils.isEmpty(arg0.getData().getOne_self()) && 
										arg0.getData().getOne_self().equals("1")) {
									iv_zijian.setVisibility(View.VISIBLE);
								} else {
									iv_zijian.setVisibility(View.GONE);
								}
								ImageLoaderUtil.getInstance(getActivity()).
								loadImage(C.BASE_URL+arg0.getData().getZx_image(),
										upload_img);
								tv_fahuoren.setText(
										"\n发布人："+arg0.getData().getPublisher().getUname()+
										"\n发布人电话："+InfoUtils.formatPhone(arg0.getData().getPublisher().getMobile())+
										"\n发布人ID："+arg0.getData().getPublisher().getId()+
										"\n发布地址："+arg0.getData().getPublisher().getAddress()+
										"\n注册类型："+arg0.getData().getPublisher().getType()+
										"\n会员级别："+arg0.getData().getPublisher().getLevel()+"    "+
										arg0.getData().getPublisher().getIsauth() +
										"\n成交：0单  \n评价：暂无");
							}
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
					}
				}));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_share:
			if (sharePopupwindow != null && !sharePopupwindow.isShowing()
					&& payOnlinePopupwindow != null
					&& !payOnlinePopupwindow.isShowing())
				sharePopupwindow.show();
			break;
		case R.id.iv_back:
			getActivity().finish();
			break;
		case R.id.iv_collect:
			if (!isChecked) {
				String data = new Gson().toJson(new Data(goodsId));
				String getStr = new Gson()
						.toJson(new DetailGoodsCollectRequest(data, "A"));
				showLD("收藏中...");
				executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
						UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
						null, null, new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(arg0.getCode(), "1")) {
										MyToast.showToast(getActivity(), "收藏成功");
										isChecked = true;
										iv_collect
												.setImageResource(R.drawable.icon_toorbar_collect_checked);
									}
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								// TODO Auto-generated method stub
								cancelLD();
							}
						}));
			} else {
				String data = new Gson().toJson(new Data(goodsId));
				String getStr = new Gson()
						.toJson(new DetailGoodsCollectRequest(data, "R"));
				showLD("取消收藏中...");
				executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
						UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
						null, null, new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(arg0.getCode(), "1")) {
										MyToast.showToast(getActivity(),
												"取消收藏成功");
										iv_collect
												.setImageResource(R.drawable.icon_toolbar_collect_nomal);
										isChecked = false;
									}
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								// TODO Auto-generated method stub
								cancelLD();
							}
						}));
			}

			break;

		case R.id.tv_pay_tel:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ payphone));
			startActivity(intent);
			break;

		case R.id.tv_pay_online:
			if (sharePopupwindow != null && !sharePopupwindow.isShowing()
					&& payOnlinePopupwindow != null
					&& !payOnlinePopupwindow.isShowing())
				payOnlinePopupwindow.show();
			break;

		default:
			break;
		}

	}
}
