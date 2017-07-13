package com.jcl.android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.OfferOnlineActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.DetailFindGoodsBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.PayTypePopupwindow;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.utils.InfoUtils;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;

/**
 * 货源详情
 */
public class DetailFindGoodsFragment extends BaseFragment implements
		OnClickListener {

	private View root;
	private String goodsId;
	private ImageView iv_share,iv_collect,iv_back;
	private String userid;
	private TextView tv_chufadi, tv_mudidi, tv_goods_info, tv_chexing,tv_from,
			tv_zhuanghuoshijian,tv_songdashijian,tv_baojiashijian,tv_fabushijian, 
			tv_pay_tel, tv_pay_online, tv_maoyileixing,tv_jj,tv_ispc,tv_saying,
			tv_zonglicheng, tv_fukuanfangshi,tv_mark,tv_fahuoren,tv_fahuorenxinxi, tv_pay_now;
	private ImageView iv_goods_pic1, iv_goods_pic2,iv_goods_pic3,iv_goods_pic4;
	private LinearLayout ll_pic;
	private boolean isChecked;
	private SharePopupwindow sharePopupwindow;
	private String telNum,payphone;
	private PayTypePopupwindow payTypePopupwindow;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments(); 
		goodsId = bundle.getString("id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_detail_find_goods, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		iv_share = (ImageView) root.findViewById(R.id.iv_share);
		iv_back = (ImageView) root.findViewById(R.id.iv_back);
		iv_collect = (ImageView) root.findViewById(R.id.iv_collect);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_goods_info = (TextView) root.findViewById(R.id.tv_goods_info);
		tv_jj = (TextView) root.findViewById(R.id.tv_jj);
		tv_chexing = (TextView) root.findViewById(R.id.tv_chexing);
		tv_zhuanghuoshijian = (TextView) root.findViewById(R.id.tv_zhuanghuoshijian);
		tv_songdashijian = (TextView) root.findViewById(R.id.tv_songdashijian);
		tv_baojiashijian = (TextView) root.findViewById(R.id.tv_baojiashijian);
		tv_fabushijian = (TextView) root.findViewById(R.id.tv_fabushijian);
		tv_pay_tel = (TextView) root.findViewById(R.id.tv_pay_tel);
		tv_pay_online = (TextView) root.findViewById(R.id.tv_pay_online);
		tv_maoyileixing = (TextView) root.findViewById(R.id.tv_maoyileixing);
		tv_zonglicheng = (TextView) root.findViewById(R.id.tv_zonglicheng);
		tv_fukuanfangshi = (TextView) root.findViewById(R.id.tv_fukuanfangshi);
		tv_mark = (TextView) root.findViewById(R.id.tv_mark);
		tv_fahuoren = (TextView) root.findViewById(R.id.tv_fahuoren);
		tv_fahuorenxinxi = (TextView) root.findViewById(R.id.tv_fahuorenxinxi);
		tv_pay_now = (TextView) root.findViewById(R.id.tv_pay_now);
		tv_from = (TextView) root.findViewById(R.id.textView1);
		tv_ispc = (TextView) root.findViewById(R.id.tv_ispc);
		tv_saying = (TextView) root.findViewById(R.id.tv_saying);

		iv_goods_pic1 = (ImageView) root.findViewById(R.id.iv_goods_pic1);
		iv_goods_pic2 = (ImageView) root.findViewById(R.id.iv_goods_pic2);
		iv_goods_pic3 = (ImageView) root.findViewById(R.id.iv_goods_pic3);
		iv_goods_pic4 = (ImageView) root.findViewById(R.id.iv_goods_pic4);


		ll_pic = (LinearLayout) root.findViewById(R.id.ll_pic);
		iv_share.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		tv_pay_online.setOnClickListener(this);//在线报价
		tv_pay_tel.setOnClickListener(this);//电话联系
		iv_back.setOnClickListener(this);
		tv_pay_now.setOnClickListener(this); //支付信息费
	}

	class DetailGoodsCollectRequest {
		private String filters;
		private String type;
		private String operate;
		private String data;

		public DetailGoodsCollectRequest(String data,String operate) {
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
			this.type = "2";
			this.ppid = goodsId;
			this.userid = JCLApplication.getInstance().getUserId();
		}

	}

	public static DetailFindGoodsFragment newInstance(String id) {
		DetailFindGoodsFragment f = new DetailFindGoodsFragment();
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
			this.type = "2001";
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String title= "hello, 誉畅物流";
		String content = "我在[誉畅手机配货软件]上发现一条货源，推荐给你，地址：http://www.chinajuchang.com/goods/detailed?id="+goodsId;
		String url = "http://www.chinajuchang.com/goods/detailed?id="+goodsId;
		sharePopupwindow = new SharePopupwindow(getActivity(), root.findViewById(R.id.ll_parent), null, null,title,content,url);
		if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
			userid = JCLApplication.getInstance().getUserId();
			iv_collect.setVisibility(View.VISIBLE);
		} else {
			userid = "";
			iv_collect.setVisibility(View.GONE);
		}
		loadData();
	}
	
	
//	//开启高德地图
//    private void startGaode() {
//        // 构造导航参数
//        NaviPara naviPara = new NaviPara();
//        // 设置终点位置
//        LatLng target = new LatLng(endLatlng.getLatitude(), endLatlng.getLongitude());
//        naviPara.setTargetPoint(target);
//        // 设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
//        try {
//            // 调起高德地图导航
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
//        } catch (com.amap.api.maps.AMapException e) {
//            // 如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(getApplicationContext());
//        }
//    }

	private void loadData() {
		String filters = new Gson().toJson(new Filters(goodsId, userid));
		String getStr = new Gson().toJson(new DetailFindGoodsRequest(filters));
		showLD("加载中...");
		executeRequest(new GsonRequest<DetailFindGoodsBean>(Request.Method.GET,
				UrlCat.getSearchUrl(getStr), DetailFindGoodsBean.class, null,
				null, new Listener<DetailFindGoodsBean>() {

					@Override
					public void onResponse(DetailFindGoodsBean arg0) {
						cancelLD();
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								tv_chufadi.setText(arg0.getData().getStartarea());
								tv_mudidi.setText(arg0.getData().getEndarea());
								if (Utils.isEmpty(arg0.getData().getFhlinkmantel())) {
									telNum = "";
									payphone = "";
								} else {
									telNum = InfoUtils.formatPhone(arg0.getData().getFhlinkmantel());
									payphone = arg0.getData().getFhlinkmantel();
								}
								if (Utils.isEmpty(arg0.getData().getSaying())) {
									tv_saying.setText("");
								} else {
									tv_saying.setText("给司机捎句话：" + arg0.getData().getSaying());
								}
								
								if (Utils.isEmpty(arg0.getData().getHaspricelow())){
										tv_maoyileixing.setText("可议价");
									} else {
										if (Utils.isEmpty(arg0.getData().getHaspriceheight())) {
											tv_maoyileixing.setText("预期价：" + arg0.getData().getHaspricelow()+"元");
										} else {
											tv_maoyileixing.setText("目标价：" + arg0.getData().getHaspricelow()+"元——"
													+arg0.getData().getHaspriceheight()+"元");
										}
										
									}
								
								if (arg0.getData().getPublishstatus().equals("0")) {
									tv_from.setText("快捷发布  " + arg0.getData().get_id());
								} else {
									tv_from.setText("精准发布  " + arg0.getData().get_id());
								}
								if(!Utils.isEmpty(arg0.getData().getIspc())){
								switch (arg0.getData().getIspc()) {
								case "0":
									tv_ispc.setText("(电脑端发布)");
									break;
									
								case "1":
									tv_ispc.setText("(手机端发布)");
									break;

								default:
									break;
								}
								}else{
									tv_ispc.setText("");
								}
								String chechang = arg0.getData().getCarlength();
								String guixing = "";
//								String[] chechangs = getResources().getStringArray(
//										R.array.chechang);
//								ArrayList<String> carlength = new ArrayList<String>();
//								for (int i = 0; i < chechangs.length; i++) {
//									carlength.add(chechangs[i]);
//								}
//								for (int i = 0; i < carlength.size(); i++) {
//									if (arg0.getData().getCarlength().equals(i+"")
//										|| Integer.valueOf(arg0.getData().getCarlength()) == i) {
//										chechang = carlength.get(i);
//									}
//								}
								
								if (arg0.getData().getGxgl() == null
										||arg0.getData().getGxgl().equals("")) {
									    guixing = "";
									} else {
										guixing = "   ("+arg0.getData().getGxgl()+")";
									}
								if (arg0.getData().getCarlengthabove() != null
										&& arg0.getData().getCarlengthabove().equals("1")) {
									
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
								String pingtaixuanze = "";
								if (arg0.getData().getPtchoose() == null) {
									pingtaixuanze = "";
								} else {
									try {
										switch (arg0.getData().getPtchoose()) {
										case "0":
											pingtaixuanze = "       【整车配货】";
											break;
										case "1":
											pingtaixuanze = "       【零担配货】";
											break;
										case "2":
											pingtaixuanze = "       【零担/整车配货】";
											break;
										case "3":
											pingtaixuanze = "       【集装箱双背】";
											break;


										default:
											break;
										}
									} catch (Exception e) {

									}
								}

								if (arg0.getData().getCarname().equals("集装箱车")) {
									tv_chexing.setText("求"+arg0.getData().getCarname() + guixing
											+ pingtaixuanze);
								} else {
									tv_chexing.setText("求"+arg0.getData().getCarname()+
											chechang + guixing + pingtaixuanze);
								}
							
									tv_fahuoren.setText("发布人："+arg0.getData().getPublisher().getUname()+
									"\n发布人电话："+telNum+
									"\n发布人ID："+arg0.getData().getPublisher().getId()+
									"\n发布地址："+arg0.getData().getPublisher().getAddress()+
									"\n注册类型："+arg0.getData().getPublisher().getType()+
									"\n会员级别："+arg0.getData().getPublisher().getLevel()+"    "+
									arg0.getData().getPublisher().getIsauth());
									
								String maoyileixing = "";
								if (arg0.getData().getMytype() == null) {
									maoyileixing = "";
								} else {
									try {
										switch (arg0.getData().getMytype()) {
										case "0":
											maoyileixing = "  出口";
											break;
										case "1":
											maoyileixing = "  进口";
											break;
										case "2":
											maoyileixing = "  内贸";
											break;

										default:
											break;
										}
									} catch (Exception e) {

									}
								}
								
								String[] jinji = getResources().getStringArray(
										R.array.jinjichengdu);	
								int jinjichengdu = Integer.valueOf(arg0.getData().getJjdegree());
								String myjinji = "";
								String goodsname = "",goodweight = "",goodtj = "";
								if (arg0.getData().getDetailname() == null
									||arg0.getData().getDetailname().equals("")) {
									goodsname = " ";
								} else {
									goodsname = "品名："+arg0.getData().getDetailname();
								}
								
								if (arg0.getData().getGoodsweight() == null
										||arg0.getData().getGoodsweight().equals("")) {
										goodweight = "";
									} else {
										goodweight = "   "+arg0.getData().getGoodsweight()+arg0.getData().getUnit();
									}
								
								if (arg0.getData().getGoodstj() == null
										||arg0.getData().getGoodstj().equals("")) {
									   goodtj = "";
									} else {
										goodtj = "    "+arg0.getData().getGoodstj() + "立方米   ";
									}
								if (jinjichengdu == 0) {
									myjinji = "<font color='#FFA500'>"+jinji[jinjichengdu]+"</font>    ";
								} else if (jinjichengdu == 1) {
									myjinji = "<font color='#32CD32'>"+jinji[jinjichengdu]+"</font>    ";
								} else{
									myjinji = "<font color='#F04848'>"+jinji[jinjichengdu]+"</font>    ";
								}
								
								String songda = "",baojiashijain = "",zhuanghuishijian = "";
								tv_jj.setText(Html.fromHtml(myjinji));
								
								if (arg0.getData().getPublishstatus().equals("0")) {
									tv_baojiashijian.setVisibility(View.GONE);
									tv_songdashijian.setVisibility(View.GONE);
								} else {
									if (arg0.getData().getLastbjtime().equals("")
											|| arg0.getData().getLastbjtime().equals("null")) {
										baojiashijain = "";
										} else {
											baojiashijain = "\n最晚报价时间："
													+ arg0.getData().getLastbjtime();
										}
									tv_baojiashijian.setText(baojiashijain);
									if (arg0.getData().getLastarrivetime().equals("")
											|| arg0.getData().getLastarrivetime().equals("null")) {
											songda = "";
										} else {
											songda = "送达时间：" + arg0.getData().getLastarrivetime();
										}
									tv_songdashijian.setText(songda);
								}
								
								if (arg0.getData().getExfhstarttime().equals("")
										|| arg0.getData().getExfhstarttime().equals("null")) {
									zhuanghuishijian = "";
									} else {
										zhuanghuishijian = "装货时间："+ arg0.getData().getExfhstarttime()
										+ " - " + arg0.getData().getExfhendtime();
									}
								tv_zhuanghuoshijian.setText(zhuanghuishijian);
								tv_fabushijian.setText("发布时间：" + arg0.getData().getCreatetime());
								
								LatLng m_start, m_end;
								m_start=new LatLng( Double.parseDouble
								(arg0.getData().getLatitude()), Double.parseDouble(arg0.getData().getLongitude()));
								m_end=new LatLng(Double.parseDouble
								(arg0.getData().getEndlatitude()), Double.parseDouble(arg0.getData().getEndlongitude()));
								Double distance= Utils.getDistance(m_start,m_end);
								tv_zonglicheng.setText("总里程数：" + new java.text.DecimalFormat("#.00").format(distance)+"米");
																
								String fukuanfangshi = "";
								try {
									switch (arg0.getData().getFftype()) {
									case "0":
										fukuanfangshi = "运费担保交易";
										break;
									case "1":
										fukuanfangshi = "运费直接给付";
										break;
									default:
										fukuanfangshi = "";
										break;
									}
								} catch (Exception e) {

								}
								String isNeedInvoice = "";
								try {
									switch (arg0.getData().getNeedinvoice()) {
									case "0":
										isNeedInvoice = "不需要发票";
										break;
									case "1":
										isNeedInvoice = "需要发票";
										break;

									default:
										isNeedInvoice = "";
										break;
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								String isSignorder = "";
								try {
									switch (arg0.getData().getNeedinvoice()) {
									case "0":
										isSignorder = "不需要回单";
										break;
									case "1":
										isSignorder = "需要回单";
										break;

									default:
										isSignorder = "";
										break;
									}
								} catch (Exception e) {
								}
								tv_fukuanfangshi.setText(fukuanfangshi + "  "
										+ isNeedInvoice + "   " + isSignorder);
								
								String huowuid = "",jianshu = "";
								if (arg0.getData().getPonum() == null || 
										arg0.getData().getPonum().equals("")) {
									huowuid = "\n暂无货物编号";
								} else {
									huowuid = "\n货物编号：" + arg0.getData().getPonum() ;
								}
								
								String yuqiprice,saytocarman;
								
								if (arg0.getData().getNum() == null || 
										arg0.getData().getNum().equals("") 
										|| arg0.getData().getNum().equals("null") ) {
									jianshu = "";
								} else {
									jianshu = arg0.getData().getNum() + "件  ";
								}

									tv_goods_info.setText( goodsname
													+ goodweight
													+ goodtj
													+ jianshu
													+ maoyileixing
													+ huowuid);
								
								
								try {
									if (arg0.getData().getSourceimage().equals("")) {
										iv_goods_pic1.setVisibility(View.GONE);
									} else {
										iv_goods_pic1.setVisibility(View.VISIBLE);
										ImageLoaderUtil
										.getInstance(getActivity())
										.loadImage(C.BASE_URL+ arg0.getData().getSourceimage(),
												iv_goods_pic1);
									}
									
								} catch (Exception e) {
									// TODO: handle exception
								}
								if (TextUtils.equals(arg0.getData()
										.getIfcollect(), "1")) {
									iv_collect.setImageResource(R.drawable.icon_toorbar_collect_checked);
									isChecked = true;
								} else {
									iv_collect.setImageResource(R.drawable.icon_toolbar_collect_nomal);
									isChecked = false;
								}
								
								if (arg0.getData().getIshowremark() == null
										|| arg0.getData().getIshowremark().equals("0")) {
									//不显示
									tv_mark.setText("");	
								} else {
									tv_mark.setText(arg0.getData().getRemark());	
								}
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
			if(sharePopupwindow!=null&&!sharePopupwindow.isShowing())
				sharePopupwindow.show();
			break;
		case R.id.iv_back:
			getActivity().finish();
			break;
		case R.id.iv_collect:
			if (!isChecked) {
				String data = new Gson().toJson(new Data(goodsId));
				String getStr = new Gson()
						.toJson(new DetailGoodsCollectRequest(data,"A"));
				showLD("收藏中...");
				executeRequest(new GsonRequest<BaseBean>(
						Request.Method.GET, UrlCat
								.getSubmitPoststrUrl(getStr),
						BaseBean.class, null, null,
						new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(
											arg0.getCode(), "1")) {
										MyToast.showToast(
												getActivity(), "收藏成功");
										isChecked = true;
										iv_collect.setImageResource(R.drawable.icon_toorbar_collect_checked);
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
			}else{
				String data = new Gson().toJson(new Data(goodsId));
				String getStr = new Gson()
						.toJson(new DetailGoodsCollectRequest(data,"R"));
				showLD("取消收藏中...");
				executeRequest(new GsonRequest<BaseBean>(
						Request.Method.GET, UrlCat
								.getSubmitPoststrUrl(getStr),
						BaseBean.class, null, null,
						new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(
											arg0.getCode(), "1")) {
										MyToast.showToast(
												getActivity(), "取消收藏成功");
										iv_collect.setImageResource(R.drawable.icon_toolbar_collect_nomal);
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
		case R.id.tv_pay_online:
            String chufadi = tv_chufadi.getText().toString();
            String mudidi = tv_mudidi.getText().toString();
            String goodsinfo = tv_goods_info.getText().toString();
            String chexing = tv_chexing.getText().toString();
            String chufashijian = tv_zhuanghuoshijian.getText().toString()+"  "+
            tv_songdashijian.getText().toString() + "  " + tv_baojiashijian.getText().toString();
            String maoyileixing = tv_maoyileixing.getText().toString();
            String zonglichengshu = tv_zonglicheng.getText().toString();
//			startActivity(OfferOnlineActivity.newInstance(getActivity(),goodsId,chufadi,mudidi,goodsinfo,chexing,chufashijian,maoyileixing,zonglichengshu));
            Intent intent = new Intent();
            intent.setClass(getActivity(), OfferOnlineActivity.class);
            intent.putExtra("goodsid", goodsId);
            intent.putExtra("chufadi", chufadi);
            intent.putExtra("mudidi", mudidi);
            intent.putExtra("goodsinfo", goodsinfo);
            intent.putExtra("chexing", chexing);
            intent.putExtra("chufashijian", chufashijian);
            intent.putExtra("maoyileixing", maoyileixing);
            intent.putExtra("zonglichengshu", zonglichengshu);
            startActivity(intent);
            break;
            
		case R.id.tv_pay_tel:
			Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ payphone));
			startActivity(intent2);
			break;

			case R.id.tv_pay_now:
				if(payTypePopupwindow==null){

				    payTypePopupwindow=new PayTypePopupwindow(DetailFindGoodsFragment.this.getActivity(), DetailFindGoodsFragment.this.getView(), null,
							goodsId, "empty", payphone, null);
				}
				payTypePopupwindow.show();
				break;
		default:
			break;
		}

	}
}
