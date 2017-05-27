package com.jcl.android.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.GoodsDetilBean;
import com.jcl.android.bean.GoodsListBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.popupwindow.SelectPicPopupWindow;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;

/**
 * 发布货源
 * 
 * @author msz
 *
 */
public class PublicGoodsActivity extends BaseActivity implements
		OnClickListener, OnImageComparessionListener {

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;

	private TextView tv_chufadi, tv_mudidi, tv_fahuoshijian_left,tv_fahuoshijian_right,
			 tv_last_baojiashijian, tv_xinxiyouxiaoqi,tv_chexing;
	private WhSpinner ws_huowuleixing, ws_maoyileixing, ws_fufeifangshi,ws_pingtaixuanze,
			ws_baozhuangfangshi, ws_qianshouhuidan, ws_fapiao,ws_chechang,ws_fukuanfang,
			ws_jinjichendu,ws_dun;

	private CheckBox cb_buchonghuowuinfo, cb_zengzhifuwu, cb_changqihuoyuan;
//	private CheckBox cb_hejia, cb_shihuo, cb_jihuo, cb_yuanjiajia;
	private EditText et_pinming, et_mubiaojialeft, et_mubiaojiaright,
			tv_fahuoren, tv_fahuoren_tel, tv_shouhuoren, tv_shouhuoren_tel,
			et_teshubeizhu, et_jianshu, et_huowuzhongliang,et_newpo,et_car_num,
			et_huowutiji, et_chaochicun, et_hangbanhao, et_tidanhao,et_guixingguiliang;
//	private CheckBox cb_qiuheihuo, cb_qiupeihuo, cb_qiushuangbei,
//			cb_qiuzhengche;
//	private CheckBox cb_jizhuangxiang, cb_lengcangche, cb_morecar;
	private CheckBox cb_baoxian, cb_baoguan, cb_baojian, cb_guoneihaiyun,
			cb_guoneikongyun, cb_guojihaiyun, cb_guojikongyun,cb_dashoufuwu;

	private RadioButton cb_paohuo, cb_zhonghuo;
	private ImageView iv_update_pic,iv_other_picone,iv_other_pictwo,iv_other_picthree;
	private Button btn_public, btn_server;
	private LinearLayout ll_cheliangxinxi,ll_ponum;
//	,et_jizhuangxiang_length,et_lengcangche_length,et_morecar_length;
	private TextView tv_zuiwansongda,tv_zhuanghuoshijian;

	private List<WhSpinner.Item> huowuleixing;
	private List<WhSpinner.Item> maoyileixing;
	private List<WhSpinner.Item> fufeifangshi;
	private List<WhSpinner.Item> baozhuangfangshi;
	private List<WhSpinner.Item> qianshouhuidan;
	private List<WhSpinner.Item> chechang;
	private List<WhSpinner.Item> fukuanfang;
	private List<WhSpinner.Item> pingtaixuanze;
	private List<WhSpinner.Item> jinjichendu;
	private List<WhSpinner.Item> dunlist;
	
	private LinearLayout ll_select_po;
//	,ll_jizhuangxiang_length,ll_lengcang_length,ll_more_length;
	private String goodsBitmap,otherBitmap1,otherBitmap2,otherBitmap3;
	private ImageCompression imageCompression;
	
	private List<HwData> goodsInfoList;
	
//	private GoodsListBean.GoodsInfo info;
	private String huoyuanid;
	private List<String> listcarlength;
	private MyUINavigationView uINavigationView;
	
	boolean[] flags;//初始复选情况
    private String[] cartypeitems=null;
    private StringBuffer result;//显示所选车型
    private StringBuffer chexing;//车型  1选中  0未选中	
    private String [] cartype = null;
    private boolean isSelect;
    private CheckBox cb_carlength,cb_beizhu;
    private String carlengthabove,ishowremark;
    
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    private Context mContext;
    
    public void initWhSpinnerData() {
		huowuleixing = new ArrayList<WhSpinner.Item>();
		maoyileixing = new ArrayList<WhSpinner.Item>();
		fufeifangshi = new ArrayList<WhSpinner.Item>();
		dunlist = new ArrayList<WhSpinner.Item>();
		baozhuangfangshi = new ArrayList<WhSpinner.Item>();
		qianshouhuidan = new ArrayList<WhSpinner.Item>();
		chechang = new ArrayList<WhSpinner.Item>();
		fukuanfang = new ArrayList<WhSpinner.Item>();
		pingtaixuanze = new ArrayList<WhSpinner.Item>();
		jinjichendu = new ArrayList<WhSpinner.Item>();
		String[] huowuleixings = getResources().getStringArray(
				R.array.huowuleixing);
		for (int i = 0; i < huowuleixings.length; i++) {
			huowuleixing.add(new WhSpinner.Item(huowuleixings[i], i));
		}
		ws_huowuleixing.setItems(huowuleixing, 0);
		
		String[] chechangs = getResources().getStringArray(
				R.array.chechang);
		for (int i = 0; i < chechangs.length; i++) {
			chechang.add(new WhSpinner.Item(chechangs[i], i));
		}
		ws_chechang.setItems(chechang, 0);
		String[] fukuanfangs = getResources().getStringArray(
				R.array.fukuanfang);
		for (int i = 0; i < fukuanfangs.length; i++) {
			fukuanfang.add(new WhSpinner.Item(fukuanfangs[i], i));
		}
		ws_fukuanfang.setItems(fukuanfang, 0);
		
		String[] pingtais = getResources().getStringArray(
				R.array.pingtaixuanze);
		for (int i = 0; i < pingtais.length; i++) {
			pingtaixuanze.add(new WhSpinner.Item(pingtais[i], i));
		}
		ws_pingtaixuanze.setItems(pingtaixuanze, 0);
		
		String[] jinji = getResources().getStringArray(
				R.array.jinjichengdu);
		for (int i = 0; i < jinji.length; i++) {
			jinjichendu.add(new WhSpinner.Item(jinji[i], i));
		}
		ws_jinjichendu.setItems(jinjichendu, 0);
		
		maoyileixing.add(new WhSpinner.Item("出口", "0"));
		maoyileixing.add(new WhSpinner.Item("进口", "1"));
		maoyileixing.add(new WhSpinner.Item("内贸", "2"));
		ws_maoyileixing.setItems(maoyileixing, 0);

		fufeifangshi.add(new WhSpinner.Item("运费担保交易", "0"));
//		fufeifangshi.add(new WhSpinner.Item("运费直接给付", "1"));
		ws_fufeifangshi.setItems(fufeifangshi, 0);

		baozhuangfangshi.add(new WhSpinner.Item("纸箱", "0"));
		baozhuangfangshi.add(new WhSpinner.Item("托盘", "1"));
		baozhuangfangshi.add(new WhSpinner.Item("卷", "2"));
		baozhuangfangshi.add(new WhSpinner.Item("液袋", "3"));
		baozhuangfangshi.add(new WhSpinner.Item("其他包装", "4"));
		ws_baozhuangfangshi.setItems(baozhuangfangshi, 0);

		qianshouhuidan.add(new WhSpinner.Item("不需要", "0"));
		qianshouhuidan.add(new WhSpinner.Item("需要", "1"));
		ws_qianshouhuidan.setItems(qianshouhuidan, 0);
		ws_fapiao.setItems(qianshouhuidan, 0);
		
		String[] duns = getResources().getStringArray(
				R.array.dungongjin);
		for (int i = 0; i < duns.length; i++) {
			dunlist.add(new WhSpinner.Item(duns[i], i));
		}
		ws_dun.setItems(dunlist, 0);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_public_goods);
		mContext = PublicGoodsActivity.this;

		flags = new boolean[]
				{false,false,false,false,false,false,false,false,false,false,false,false};
		chexing = new StringBuffer();
		result = new StringBuffer();
		initNavigation();
		initView();
		initConent();
		imageCompression = ImageCompression.getInstance(this);
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		datePickerPopupwindow = new DatePickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		initWhSpinnerData();
//		initPoData();
		goodsInfoList = new ArrayList<HwData>();
		
	}
	
	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setVisibility(View.GONE);
	}

	class GetGoodsRequest {
		private String type;
		private String filters;

		public GetGoodsRequest(String filters) {
			this.type = "2001";
			this.filters = filters;
		}
	}

	class Filters {
		private String userid;
		private String _id;

		public Filters(String userid,String _id) {
			this.userid = userid;
			this._id = _id;
		}
		
		private String EQ_userid;
		public Filters(String EQ_userid) {
			this.EQ_userid = EQ_userid;
		}
	}

	class GetPoRequest {
		private String type;
		private String filters;

		public GetPoRequest(String filters) {
			this.type = "1002";
			this.filters = filters;
		}
	}
	
	
	
	class GoodsInfo extends BaseBean {
		private Goods data;
		
//		public GoodsInfo(Goods goods){
//			data = goods;
//		}
		
		public List<HwData> getData() {
			return data!=null?data.getGoods():null;
		}

		public void setData(List<HwData> data) {
			this.data.setGoods(data);
		}

	}
	
	class Goods{
		List<HwData> goods;

		public List<HwData> getGoods() {
			return goods;
		}

		public void setGoods(List<HwData> goods) {
			this.goods = goods;
		}
		
	}
	
	String carListFilters;
	String jsonRequest;
	
	public class GoodsListRequest {
		private String Filters = "";
		private String type = "2001";

		public GoodsListRequest(String Filters) {
			this.Filters = Filters;
		}
	}

	public class GoodsListFilters {
		private String userid;
		private String _id;

		public GoodsListFilters(String userid,String _id) {
			this.userid = userid;
			this._id = _id;
		}
	}

	//修改显示信息
	private void initGoodsData() {
//		HwData hwData = new HwData("123", "1", "2", "222", "12345555", "23", "124", "125555");
//		List<HwData> data = new ArrayList<HwData>();
//		Goods goods = new Goods();
//		data.add(hwData);
//		goods.setGoods(data);
//		GoodsInfo goodsInfo = new GoodsInfo(goods);
//		LogUtil.logWrite("MSG_JSON_TEST", new Gson().toJson(goodsInfo));
		showLD("数据加载中...");
		carListFilters = new Gson().toJson(new GoodsListFilters(JCLApplication
				.getInstance().getUserId(),huoyuanid));
		jsonRequest = new Gson().toJson(new GoodsListRequest(carListFilters));
		executeRequest(new GsonRequest<GoodsDetilBean>(Request.Method.GET,
				UrlCat.getSearchUrl(jsonRequest), GoodsDetilBean.class, null,
				null, new Listener<GoodsDetilBean>() {
					@SuppressWarnings("unchecked")
					@Override
					public void onResponse(GoodsDetilBean arg0) {
						cancelLD();
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								if (arg0 != null) {
									tv_mudidi.setText(arg0.getData().getEndarea());//终点
									et_pinming.setText(arg0.getData().getDetailname());//品名
									et_huowuzhongliang.setText(arg0.getData().getGoodsweight());
									et_mubiaojialeft.setText(arg0.getData().getHaspricelow());//目标价
									tv_fahuoshijian_left.setText(arg0.getData().getExfhstarttime());//发货时间
									tv_fahuoshijian_right.setText(arg0.getData().getExfhendtime());//发货时间
									tv_zuiwansongda.setText(arg0.getData().getLastarrivetime());//装货时间
									tv_zhuanghuoshijian.setText(arg0.getData().getLoadingtime());//送达时间
									ws_baozhuangfangshi.setItems(baozhuangfangshi, 
											Integer.parseInt(arg0.getData().getBztype()));//包装方式
									et_newpo.setText(arg0.getData().getPonum());//编号
									ws_maoyileixing.setItems(maoyileixing, 
											Integer.parseInt(arg0.getData().getMytype()));//贸易类型
									ws_chechang.setItems(chechang, 
											Integer.parseInt(arg0.getData().getCarlength()));//车长
									//货物种类
									if (arg0.getData().getPztype().equals("1")
											|| arg0.getData().getHwtype() == "1") {
										cb_paohuo.setChecked(true);
										cb_zhonghuo.setChecked(false);
									} else if (cb_zhonghuo.isChecked()) {
										cb_paohuo.setChecked(false);
										cb_zhonghuo.setChecked(true);
									}
									
									ws_huowuleixing.setItems(huowuleixing, 
											Integer.parseInt(arg0.getData().getHwtype()));//货物类型
									//长期货源
									if (arg0.getData().getIsLongTermSource().equals("1")) {
										cb_changqihuoyuan.setChecked(true);
									} else {
										cb_changqihuoyuan.setChecked(false);
									}
									et_jianshu.setText(arg0.getData().getNum());//件数
									et_huowutiji.setText(arg0.getData().getGoodstj());//体积
									et_chaochicun.setText(arg0.getData().getGoodstj());//超尺寸
									et_guixingguiliang.setText(arg0.getData().getGoodstj());//柜型柜量
									ws_pingtaixuanze.setItems(pingtaixuanze, 
											Integer.parseInt(arg0.getData().getPtchoose()));//平台选择
									ws_jinjichendu.setItems(jinjichendu, 
											Integer.parseInt(arg0.getData().getJjdegree()));//紧急程度
									et_mubiaojiaright.setText(arg0.getData().getHaspriceheight());//目标价
									tv_chexing.setText(arg0.getData().getCarname());//车型
									ws_fufeifangshi.setItems(fufeifangshi, 
											Integer.parseInt(arg0.getData().getFftype()));//付费方式
									ws_fukuanfang.setItems(fukuanfang, 
											Integer.parseInt(arg0.getData().getFftype()));//付费方式
									ws_fapiao.setItems(qianshouhuidan, 
											Integer.parseInt(arg0.getData().getNeedinvoice()));//发票
									ws_qianshouhuidan.setItems(qianshouhuidan, 
											Integer.parseInt(arg0.getData().getSignorder()));//签收回单
									tv_fahuoren.setText(arg0.getData().getFhlinkman());//发货人
									tv_fahuoren_tel.setText(arg0.getData().getFhlinkmantel());//发货人电话
									tv_shouhuoren.setText(arg0.getData().getShlinkman());//收货人
									tv_shouhuoren_tel.setText(arg0.getData().getShlinkmantel());//收货人电话
									et_hangbanhao.setText(arg0.getData().getOneremark());//航班号
									et_tidanhao.setText(arg0.getData().getTworemark());//提单号
									et_teshubeizhu.setText(arg0.getData().getRemark());//备注
									tv_last_baojiashijian.setText(arg0.getData().getLastbjtime());//最晚报价时间
									//车型
									String[] chexing1 = arg0.getData().getCartype()
											.split(",");
									for (int i = 0; i < chexing1.length; i++) {
										chexing.append(chexing1[i]+",");
										if (chexing1[i].equals("1") || chexing1[i] == "1") {
											flags[i] = true;
										} else {
											flags[i] = false;
										}
										
									}
									
									
									//增值服务
									String[] zenzhifuwu = arg0.getData().getIncrementerm()
											.split(",");
									if (TextUtils.equals(zenzhifuwu[0], "1")) {
										cb_baoxian.setChecked(true);
									}
									if (TextUtils.equals(zenzhifuwu[1], "1")) {
										cb_baoguan.setChecked(true);
									}
									if (TextUtils.equals(zenzhifuwu[2], "1")) {
										cb_baojian.setChecked(true);
									}
									if (TextUtils.equals(zenzhifuwu[3], "1")) {
										cb_guoneihaiyun.setChecked(true);
									}
									if (TextUtils.equals(zenzhifuwu[4], "1")) {
										cb_guoneikongyun.setChecked(true);
									}
									if (TextUtils.equals(zenzhifuwu[5], "1")) {
										cb_guojihaiyun.setChecked(true);
									}
									if (TextUtils.equals(zenzhifuwu[6], "1")) {
										cb_guojikongyun.setChecked(true);
									}
//									if (TextUtils.equals(zenzhifuwu[7], "1")) {
//										cb_dashoufuwu.setChecked(true);
//									}
									
								}
							} else {
								Toast.makeText(PublicGoodsActivity.this,
										arg0.getMsg(), Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(PublicGoodsActivity.this, "暂无数据！",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						cancelLD();
						Toast.makeText(PublicGoodsActivity.this, "无法连接服务器！",
								Toast.LENGTH_SHORT).show();
					}
				}));
	}

	//货物编号
	private void initPoData(){
//		showLD("数据加载...");
		String postStr = new Gson()
				.toJson(new GetPoRequest(new Gson().toJson(new Filters(
						JCLApplication.getInstance().getUserId()))));
		executeRequest(new GsonRequest<GoodsInfo>(Request.Method.GET,
				UrlCat.getInfoUrl(postStr), GoodsInfo.class, null, null,
				new Listener<GoodsInfo>() {

					@Override
					public void onResponse(GoodsInfo arg0) {
//						cancelLD();
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								Type type = new TypeToken<ArrayList<HwData>>() {
								}.getType();
								goodsInfoList = arg0.getData();
								if (goodsInfoList.size() == 0) {
									ll_select_po.setVisibility(View.GONE);
								} else {
									ll_select_po.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
//						cancelLD();
					}
				}));
	}
	private void initView() {
		tv_chufadi = (TextView) findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) findViewById(R.id.tv_mudidi);
		tv_chexing = (TextView) findViewById(R.id.tv_chexing);
		tv_fahuoshijian_left = (TextView) findViewById(R.id.tv_fahuoshijian_left);
		tv_fahuoshijian_right = (TextView) findViewById(R.id.tv_fahuoshijian_right);
		tv_last_baojiashijian = (TextView) findViewById(R.id.tv_last_baojiashijian);
//		tv_xinxiyouxiaoqi = (TextView) findViewById(R.id.tv_xinxiyouxiaoqi);
		ws_huowuleixing = (WhSpinner) findViewById(R.id.ws_huowuleixing);
		ws_chechang = (WhSpinner) findViewById(R.id.ws_chechang);
		ws_fukuanfang  = (WhSpinner) findViewById(R.id.ws_fukuanfang);
		ws_pingtaixuanze = (WhSpinner) findViewById(R.id.ws_pingtaixuanze);
		ws_jinjichendu = (WhSpinner) findViewById(R.id.ws_jinjichendu);
		ws_maoyileixing = (WhSpinner) findViewById(R.id.ws_maoyileixing);
		ws_fufeifangshi = (WhSpinner) findViewById(R.id.ws_fufeifangshi);
		ws_qianshouhuidan = (WhSpinner) findViewById(R.id.ws_qianshouhuidan);
		ws_baozhuangfangshi = (WhSpinner) findViewById(R.id.ws_baozhuangfangshi);
		ws_fapiao = (WhSpinner) findViewById(R.id.ws_fapiao);
		ws_dun = (WhSpinner) findViewById(R.id.ws_dun);
		cb_buchonghuowuinfo = (CheckBox) findViewById(R.id.cb_buchonghuowuinfo);
//		cb_zengzhifuwu = (CheckBox) findViewById(R.id.cb_zengzhifuwu);
		findViewById(R.id.ll_zengzhifuwu).setVisibility(View.VISIBLE);
		cb_baoguan = (CheckBox) findViewById(R.id.cb_baoguan);
		cb_baojian = (CheckBox) findViewById(R.id.cb_baojian);
		cb_guoneihaiyun = (CheckBox) findViewById(R.id.cb_guoneihaiyun);
		cb_guoneikongyun = (CheckBox) findViewById(R.id.cb_guoneikongyun);
		cb_guojihaiyun = (CheckBox) findViewById(R.id.cb_guojihaiyun);
		cb_guojikongyun = (CheckBox) findViewById(R.id.cb_guojikongyun);
		cb_dashoufuwu = (CheckBox) findViewById(R.id.cb_daishoufuwu);
		cb_baoxian = (CheckBox) findViewById(R.id.cb_baoxian);
		et_pinming = (EditText) findViewById(R.id.et_pinming);
		et_mubiaojialeft = (EditText) findViewById(R.id.et_mubiaojialeft);
		et_mubiaojiaright = (EditText) findViewById(R.id.et_mubiaojiaright);
		et_jianshu = (EditText) findViewById(R.id.et_jianshu);
		tv_fahuoren = (EditText) findViewById(R.id.tv_fahuoren);
		tv_fahuoren_tel = (EditText) findViewById(R.id.tv_fahuoren_tel);
		tv_fahuoren_tel.setText(SharePerfUtil.getLoginName());
		tv_fahuoren.setText(SharePerfUtil.getLinkMan());
		tv_shouhuoren = (EditText) findViewById(R.id.tv_shouhuoren);
		tv_shouhuoren_tel = (EditText) findViewById(R.id.tv_shouhuoren_tel);
		et_huowuzhongliang = (EditText) findViewById(R.id.et_huowuzhongliang);
		et_huowutiji = (EditText) findViewById(R.id.et_huowutiji);
		et_chaochicun = (EditText) findViewById(R.id.et_chaochicun);
		et_guixingguiliang = (EditText) findViewById(R.id.et_guixingguiliang);
		et_teshubeizhu = (EditText) findViewById(R.id.et_teshubeizhu);
		et_hangbanhao = (EditText) findViewById(R.id.et_hangbanhao);
		et_tidanhao = (EditText) findViewById(R.id.et_tidanhao);
		//修改页面  把货物编号放在最上面
		ll_ponum = (LinearLayout) findViewById(R.id.ll_ponum); 
		ll_ponum.setVisibility(View.GONE);
		et_newpo = (EditText) findViewById(R.id.et_newpo);
//		et_jizhuangxiang_length = (EditText) findViewById(R.id.et_jizhuangxiang_length);
//		et_lengcangche_length = (EditText) findViewById(R.id.et_lengcangche_length);
//		et_morecar_length = (EditText) findViewById(R.id.et_morecar_length);
		cb_paohuo = (RadioButton) findViewById(R.id.cb_paohuo);
		cb_zhonghuo = (RadioButton) findViewById(R.id.cb_zhonghuo);
//		cb_qiuheihuo = (CheckBox) findViewById(R.id.cb_qiuheihuo);
//		cb_qiupeihuo = (CheckBox) findViewById(R.id.cb_qiupeihuo);
//		cb_qiushuangbei = (CheckBox) findViewById(R.id.cb_qiushuangbei);
//		cb_qiuzhengche = (CheckBox) findViewById(R.id.cb_qiuzhengche);
//		cb_jizhuangxiang = (CheckBox) findViewById(R.id.cb_jizhuangxiang);
//		cb_lengcangche = (CheckBox) findViewById(R.id.cb_lengcangche);
		cb_changqihuoyuan = (CheckBox) findViewById(R.id.cb_changqihuoyuan);
//		cb_morecar = (CheckBox) findViewById(R.id.cb_morecar);
		iv_update_pic = (ImageView) findViewById(R.id.iv_update_pic);
		iv_other_picone = (ImageView) findViewById(R.id.iv_other_picone);
		iv_other_pictwo = (ImageView) findViewById(R.id.iv_other_pictwo);
		iv_other_picthree = (ImageView) findViewById(R.id.iv_other_picthree);
		btn_public = (Button) findViewById(R.id.btn_public);
		btn_server = (Button) findViewById(R.id.btn_server);

//		ll_cheliangxinxi = (LinearLayout) findViewById(R.id.ll_cheliangxinxi);
		et_car_num = (EditText) findViewById(R.id.et_car_num);
		tv_zuiwansongda = (TextView) findViewById(R.id.tv_zuiwansongda);
		tv_zhuanghuoshijian = (TextView) findViewById(R.id.tv_zhuanghuoshijian);
		ll_select_po = (LinearLayout) findViewById(R.id.ll_select_po);
		
		cb_carlength=(CheckBox) findViewById(R.id.cb_carlength);
		cb_carlength.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				switch (buttonView.getId()) {
				case R.id.cb_carlength:
					if(isChecked)
					{
						carlengthabove="1";
					}else{
						carlengthabove="0";
					}
					break;

				default:
					break;
				}
				
			}
		});
		cb_beizhu =(CheckBox) findViewById(R.id.cb_beizhu);
		cb_beizhu.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				switch (buttonView.getId()) {
				case R.id.cb_beizhu:
					if(isChecked)
					{
						ishowremark = "1";
					}else{
						ishowremark = "0";
					}
					break;

				default:
					break;
				}
				
			}
		});
		
		tv_fahuoshijian_left.setText(Utils.getCurDate());
		tv_fahuoshijian_right.setText(Utils.getCurDate());

//		ll_jizhuangxiang_length = (LinearLayout) findViewById(R.id.ll_jizhuangxiang_length);
//		ll_lengcang_length = (LinearLayout) findViewById(R.id.ll_lengcang_length);
//		ll_more_length = (LinearLayout) findViewById(R.id.ll_more_length);
//		
//		ll_jizhuangxiang_length.setOnClickListener(this);
//		ll_lengcang_length.setOnClickListener(this);
//		ll_more_length.setOnClickListener(this);
		ll_select_po.setOnClickListener(this);
		iv_update_pic.setOnClickListener(this);
		iv_other_picone.setOnClickListener(this);
		iv_other_pictwo.setOnClickListener(this);
		iv_other_picthree.setOnClickListener(this);
		tv_zuiwansongda.setOnClickListener(this);
		tv_zhuanghuoshijian.setOnClickListener(this);
		tv_chexing.setOnClickListener(this);

		// cb_buchonghuowuinfo
		// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// if (isChecked) {
		// findViewById(R.id.ll_buchonghuowuinfo)
		// .setVisibility(View.VISIBLE);
		// } else {
		// findViewById(R.id.ll_buchonghuowuinfo)
		// .setVisibility(View.GONE);
		// }
		// }
		// });
//		cb_zengzhifuwu
//				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//					@Override
//					public void onCheckedChanged(CompoundButton buttonView,
//							boolean isChecked) {
//						if (isChecked) {
//							findViewById(R.id.ll_zengzhifuwu).setVisibility(
//									View.VISIBLE);
//						} else {
//							findViewById(R.id.ll_zengzhifuwu).setVisibility(
//									View.GONE);
//						}
//					}
//				});

		tv_chufadi.setOnClickListener(this);
		tv_mudidi.setOnClickListener(this);
		tv_fahuoshijian_left.setOnClickListener(this);
		tv_fahuoshijian_right.setOnClickListener(this);
		tv_last_baojiashijian.setOnClickListener(this);
//		tv_xinxiyouxiaoqi.setOnClickListener(this);
		btn_public.setOnClickListener(this);
		btn_server.setOnClickListener(this);
	}

	
	void initConent() {
		huoyuanid = (String) getIntent().getSerializableExtra("huoyuanid");
		if (huoyuanid == null) {
			return;
		}else{
			initGoodsData();
		}
	}
	
	private String chufadiCode, mudidiCode,chufaCityLat,chufaCityLnt,mudiCityLat,mudiCityLnt;
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
				chufadiCode = bundle.getString("cityCode");
				chufaCityLat = bundle.getString("CityLat");
				chufaCityLnt = bundle.getString("CityLnt");
				tv_chufadi.setText(info);
				break;
			case 2:
				mudidiCode = bundle.getString("cityCode");
				mudiCityLat = bundle.getString("CityLat");
				mudiCityLnt = bundle.getString("CityLnt");

				tv_mudidi.setText(info);
				break;
			case 3:
				tv_fahuoshijian_left.setText(info);
				break;
			case 4:
				tv_fahuoshijian_right.setText(info);
				break;
			case 5:
				tv_last_baojiashijian.setText(info);
				break;
			case 6:
				tv_xinxiyouxiaoqi.setText(info);
				break;
			case 7:
				tv_zuiwansongda.setText(info);
				break;
			case 8:
				tv_zhuanghuoshijian.setText(info);
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
			builder = new AlertDialog.Builder(this);
		}

		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle(title)
				.setSingleChoiceItems(po, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								et_newpo.setText(goodsInfoList.get(item).ponum);
								ws_maoyileixing.setItems(maoyileixing, Integer.parseInt(goodsInfoList
										.get(item).mytype));
								ws_baozhuangfangshi.setItems(maoyileixing, Integer.parseInt(goodsInfoList
										.get(item).bztype));
								et_jianshu.setText(goodsInfoList.get(item).num);
								et_huowuzhongliang.setText(goodsInfoList
										.get(item).goodsweight);
								et_huowutiji.setText(goodsInfoList.get(item).goodstj);
								et_chaochicun.setText(goodsInfoList.get(item).maxgoodssize);
								et_guixingguiliang.setText(goodsInfoList.get(item).gxgl);
								dialog.dismiss();
							}
						}).show();
	}

	
	protected Dialog onCreateDialog(int type){
		cartypeitems = getResources().getStringArray(R.array.chexing);
		result = new StringBuffer();
    	chexing = new StringBuffer();
		Dialog dialog=null;
		tv_chexing.setText("");
		Builder builder=new android.app.AlertDialog.Builder(this);
//        //设置对话框的图标
//        builder.setIcon(R.drawable.header);
        //设置对话框的标题
        builder.setTitle("车型选择");
        builder.setMultiChoiceItems(cartypeitems, null, new DialogInterface.OnMultiChoiceClickListener(){
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                flags[which] = isChecked;
                if (isChecked) {
                	isSelect = true;
				} else {
					isSelect = false;
				}
            }
        });
        
        //添加一个确定按钮
        builder.setPositiveButton("确   定 ", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            	result = new StringBuffer();
            	cartype = new String[] {"0","0","0","0","0","0","0","0","0","0","0","0"};
            	if (isSelect) {
                    for (int i = 0; i < flags.length; i++) {
                        if(flags[i]){
                        	result.append(cartypeitems[i]+",");
                            cartype [i] = "1";
                        }else{
                        	cartype [i] = "0";
                        }
                    }
                	tv_chexing.setText(result.toString().substring(0, result.length()-1));
                	for (int i = 0; i < cartype.length; i++) {
    					chexing.append(cartype[i]+",");
    				}	
				}else{
					MyToast.showToast(PublicGoodsActivity.this, "请选择车型");
					tv_chexing.setText("车型选择");
				}
            	
            }
            
        });
        
      //添加一个取消按钮
        builder.setNegativeButton("取  消", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            	tv_chexing.setText("车型选择(可多选)");
            	MyToast.showToast(PublicGoodsActivity.this, "请选择车型");
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        return dialog;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_chufadi:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"submit");
			break;

		case R.id.tv_mudidi:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(2,"submit");
			break;
			
		case R.id.tv_chexing:
			tv_chexing.setText("");
			showDialog(1);
			break;

		case R.id.tv_fahuoshijian_left:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(3);
			break;
		case R.id.tv_fahuoshijian_right:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(4);
			break;
			
		case R.id.tv_last_baojiashijian:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(5);
			break;
//		case R.id.tv_xinxiyouxiaoqi:
//			if (datePickerPopupwindow != null)
//				datePickerPopupwindow.show(6);
//			break;
		case R.id.tv_zuiwansongda:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(7);
			break;

		case R.id.tv_zhuanghuoshijian:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(8);
			break;
			
		case R.id.iv_update_pic:
			Intent intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, 1);
			break;
			
		case R.id.iv_other_picone:
			Intent intent2 = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent2, 2);
			
//			menuWindow = new SelectPicPopupWindow(mContext,PublicGoodsActivity.this); 
//			menuWindow.showAsDropDown(findViewById(R.id.action_bar), Gravity.CENTER, 0, 0);
			break;
		case R.id.iv_other_pictwo:
			Intent intent3 = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent3, 3);
			break;
		case R.id.iv_other_picthree:
			Intent intent4 = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent4, 4);
			break;
			
		case R.id.ll_select_po:
//			String[] goodsPo = new String[goodsInfoList.size()];
//			for (int i = 0; i < goodsInfoList.size(); i++) {
//				goodsPo[i] = goodsInfoList.get(i).ponum;
//			}
//			showView(goodsPo);
			Intent inte=new Intent(PublicGoodsActivity.this,GoodsManageActivity.class);
			startActivityForResult(inte.putExtra("key", C.FOR_CAR_CODE), C.FOR_CAR_CODE);
			break;

		case R.id.btn_public:
			
			String zengzhifuwu = "";
			if (cb_baoxian.isChecked()) {
				zengzhifuwu = "1,";
			}else{
				zengzhifuwu =  "0,";
			}
			if (cb_baoguan.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			if (cb_baojian.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			if (cb_guoneihaiyun.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			if (cb_guoneikongyun.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			if (cb_guojihaiyun.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			if (cb_guojikongyun.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			if (cb_dashoufuwu.isChecked()) {
				zengzhifuwu =  zengzhifuwu + "1,";
			}else{
				zengzhifuwu =  zengzhifuwu + "0,";
			}
			
			String chufadi = "";
			String mudidi = "";
			if (TextUtils.isEmpty(tv_chufadi.getText().toString())) {
				MyToast.showToast(this, "请选择出发地");
				return;
			}
			chufadi = tv_chufadi.getText().toString();
			if (TextUtils.isEmpty(tv_mudidi.getText().toString())) {
				MyToast.showToast(this, "请选择目的地");
				return;
			}
			mudidi = tv_mudidi.getText().toString();
			String fahuoshijianleft = "";
			String fahuoshijianright = "";
			if (TextUtils.isEmpty(tv_fahuoshijian_left.getText().toString())) {
				MyToast.showToast(this, "请选择预计发货时间");
				return;
			}else{
				fahuoshijianleft = tv_fahuoshijian_left.getText().toString();
			}
			if (TextUtils.isEmpty(tv_fahuoshijian_right.getText().toString())) {
				fahuoshijianright = Utils.addData(fahuoshijianleft);
			}else{
				fahuoshijianright = tv_fahuoshijian_right.getText().toString();
			}
			String zuiwansongda = "";
			if (TextUtils.isEmpty(tv_zuiwansongda.getText().toString())
					|| TextUtils.isEmpty(tv_zuiwansongda.getText()
							.toString())) {
				MyToast.showToast(this, "请选择最晚送达时间");
				return;
			}
			
			zuiwansongda = tv_zuiwansongda.getText().toString();
			String huowuleixing = ws_huowuleixing.getChoicePosition()+"";
			String huowuleixing_type = "";
			if (cb_paohuo.isChecked()) {
				huowuleixing_type = "1";
			} else if (cb_zhonghuo.isChecked()) {
				huowuleixing_type = "2";
			}
			if (TextUtils.isEmpty(et_pinming.getText().toString())) {
				MyToast.showToast(this, "请输入具体品名");
				return;
			}
			String pinming = et_pinming.getText().toString();

			if (chexing.length() == 0) {
				MyToast.showToast(this, "请选择车型");
				return;
			}
//			if (tv_chexing.getText().toString().trim().equals("")) {
//				MyToast.showToast(this, "请选择车型");
//				return;
//			}
			if (TextUtils.isEmpty(tv_fahuoren.getText().toString())) {
				MyToast.showToast(this, "请输入发货人");
				return;
			}
			if (TextUtils.isEmpty(tv_fahuoren_tel.getText().toString())) {
				MyToast.showToast(this, "请输入发货人");
				return;
			}

			String carlenth = "";
			if (ws_chechang.getChoiceText().equals("不限")) {
				carlenth = "0";
			} else {
				carlenth = ws_chechang.getChoiceText().substring(0,ws_chechang.getChoiceText().length() - 1);
			}
			
			HwData hwData = new HwData(et_newpo.getText().toString(),
					ws_maoyileixing.getChoicePosition()+"",
					ws_baozhuangfangshi.getChoicePosition()+"", et_jianshu
							.getText().toString(), et_huowuzhongliang.getText()
							.toString(), et_huowutiji.getText().toString(),
					et_chaochicun.getText().toString(), JCLApplication
							.getInstance().getUserId(),
							et_guixingguiliang.getText().toString(),
							ws_dun.getChoiceText());
			String hwDataJson = new Gson().toJson(hwData);
			HyData hyData = new HyData(ws_jinjichendu.getChoicePosition()+"", chufadi, mudidi,
					fahuoshijianleft, fahuoshijianright, huowuleixing,
					huowuleixing_type, pinming, et_newpo.getText().toString(), "",
					et_hangbanhao.getText().toString(), et_tidanhao.getText()
							.toString(), "",
					cb_changqihuoyuan.isChecked() ? "1" : "0", ws_pingtaixuanze.getChoicePosition()+"",
					chexing.substring(0, chexing.length()-1), et_car_num.getText().toString(),
					zuiwansongda, et_mubiaojialeft
							.getText().toString(), et_mubiaojiaright.getText()
							.toString(), tv_last_baojiashijian.getText()
							.toString(), ws_fufeifangshi.getChoicePosition()+"", ws_qianshouhuidan.getChoicePosition()+"",
							ws_fapiao.getChoicePosition()+"",tv_fahuoren.getText().toString(), tv_shouhuoren.getText()
							.toString(), tv_fahuoren_tel.getText().toString(),
					tv_shouhuoren_tel.getText().toString(), zengzhifuwu, goodsBitmap,
					et_teshubeizhu.getText().toString(), "0", chufadiCode,
					mudidiCode, carlenth,JCLApplication.getInstance().getUserId(),tv_zhuanghuoshijian.getText().toString(),
					ws_fukuanfang.getChoicePosition()+"",otherBitmap1,otherBitmap2,otherBitmap3,tv_chexing.getText().toString(),
					chufaCityLnt,chufaCityLat,mudiCityLnt,mudiCityLat,ws_huowuleixing.getChoiceItem()+"",
					carlengthabove,ishowremark,
					"1","0");
			String hyDataJson = new Gson().toJson(hyData);
			PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(
					new Gson().toJson((new Data(hyDataJson, hwDataJson))));
			String postStr = new Gson().toJson(publicGoodsRequest);
			showLD("发布中...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(PublicGoodsActivity.this,
											"发布成功");
									finish();
									startActivity(new Intent(PublicGoodsActivity.this,SettingPublicGoodsActivity.class));
								} else {
									MyToast.showToast(PublicGoodsActivity.this,
											"发布失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(PublicGoodsActivity.this, "发布失败");
						}
					}));
			break;

		case R.id.btn_server:

			break;

		default:
			break;
		}

	}

	//发布
	class PublicGoodsRequest {
		private String operate;
		private String type;
		private String data;
		private String key;

		public PublicGoodsRequest(String data) {
			if (huoyuanid == null) {
				this.operate = "A";
			}else{
				this.operate = "M";
				this.key = huoyuanid;
			}
			
			this.type = "1003";
			this.data = data;
		}
	}

	
	class Data {
		private String hydata;
		private String hwdata;

		public Data(String hydata, String hwdata) {
			this.hydata = hydata;
			this.hwdata = hwdata;
		}
	}
	//发布的数据
	class HwData {
		private String ponum;// PO# 　
		private String mytype;// 贸易类型 　　
		private String bztype;// 包装方式 　
		private String num;// 件数 　
		private String goodsweight;// 货物总重量 　
		private String goodstj;// 货物总体积 　
		private String maxgoodssize;// 超长尺寸货物
		private String userid;// 用户编码 　
		private String gxgl;//柜型柜量
		private String unit;

		public HwData(String ponum, String mytype, String bztype, String num,
				String goodsweight, String goodstj, String maxgoodssize,
				String userid,String gxgl,String unit) {

			this.unit = unit;
			this.ponum = ponum;
			this.mytype = mytype;
			this.bztype = bztype;
			this.num = num;
			this.goodsweight = goodsweight;
			this.goodstj = goodstj;
			this.maxgoodssize = maxgoodssize;
			this.userid = userid;
			this.gxgl = gxgl;

		}
	}

	class HyData {
		private String jjdegree;// 紧急程度
		private String startarea;// 开始地区
		private String endarea;// 目的地区
		private String exfhstarttime;// 预计发货时间开始
		private String exfhendtime;// 预计发货时间结束
		private String hwtype;// 货物类型
		private String pztype;// 泡重 1泡，2重
		private String detailname;// 具体品名
		private String ponum;// PO#
		private String bchwdetail;// 补充货物详情 　
		private String oneremark;// 船名航次 　
		private String tworemark;// 提单号 　
		private String mytype;// 贸易类型
		private String isLongTermSource;// 是否长期货源 0否，1是
		private String ptchoose;// 平台选择
		private String cartype;// 车型车长
		private String needcarnum;// 需要车辆数
		private String lastarrivetime;// 最晚到达时间
		private String haspricelow;// 有目标价低
		private String haspriceheight;// 有目标价高
		private String lastbjtime;// 最晚报价时间
		private String fftype;// 付费方式
		private String signorder;// 签收回单
		private String needinvoice;// 发票
		private String fhlinkman;// 发货人
		private String shlinkman;// 收货人
		private String fhlinkmantel;// 发货人手机号
		private String shlinkmantel;// 收货人手机号
		private String incrementerm;// 增值服务
		private String sourceimage;// 上传照片
		private String remark;// 备注
		private String goodsid;// 货物id 如果直接精准发布时，写”0”
		private String userid;// 用户编码
		private String startcode; // 出发地code
		private String endcode; // 目的地code
		private String carlength;//车长
		private String carname;
		private String publishstatus; // 发布状态 0关,1开
		
		private String longitude; // 出发地经度
		private String latitude; // 出发地纬度
		private String endlongitude;//目的地经度
		private String endlatitude;//目的地纬度
		
		private String loadingtime;//装货时间
		private String payer;//付款方
		private String sourceimage1,sourceimage2,sourceimage3;//其他照片
		private String hytypename;
		private String carlengthabove;
		private String status;
		private String ishowremark;
		private String ispc;
		private String gone;

		// data2对象：(货物参数)

		public HyData(String jjdegree, String startarea, String endarea,
				String exfhstarttime, String exfhendtime, String hwtype,
				String pztype, String detailname, String ponum,
				String bchwdetail, String oneremark, String tworemark,
				String mytype, String isLongTermSource, String ptchoose,
				String cartype, String needcarnum, String lastarrivetime,
				String haspricelow, String haspriceheight, String lastbjtime,
				String fftype, String signorder, String needinvoice,
				String fhlinkman, String shlinkman, String fhlinkmantel,
				String shlinkmantel, String incrementerm, String sourceimage,
				String remark, String goodsid, String startcode,
				String endcode,String carlength, String userid,String loadingtime,
		        String payer,String sourceimage1,String sourceimage2,String sourceimage3,
		        String carname,String longitude,String latitude,String endlongitude,
		        String endlatitude,String hytypename,String carlengthabove,String ishowremark,
		        String ispc,String gone) {
			this.gone = gone;
			this.ispc = ispc;
			this.carlength = carlength;
			this.startcode = startcode;
			this.endcode = endcode;
			this.jjdegree = jjdegree;
			this.startarea = startarea;
			this.endarea = endarea;
			this.exfhstarttime = exfhstarttime;
			this.exfhendtime = exfhendtime;
			this.hwtype = hwtype;
			this.pztype = pztype;
			this.detailname = detailname;
			this.ponum = ponum;
			this.bchwdetail = bchwdetail;
			this.oneremark = oneremark;
			this.tworemark = tworemark;
			this.mytype = mytype;
			this.isLongTermSource = isLongTermSource;
			this.ptchoose = ptchoose;
			this.cartype = cartype;
			this.needcarnum = needcarnum;
			this.lastarrivetime = lastarrivetime;
			this.haspricelow = haspricelow;
			this.haspriceheight = haspriceheight;
			this.lastbjtime = lastbjtime;
			this.fftype = fftype;
			this.signorder = signorder;
			this.needinvoice = needinvoice;
			this.fhlinkman = fhlinkman;
			this.shlinkman = shlinkman;
			this.fhlinkmantel = fhlinkmantel;
			this.shlinkmantel = shlinkmantel;
			this.incrementerm = incrementerm;
			this.sourceimage = sourceimage;
			this.remark = remark;
			this.goodsid = goodsid;
			this.userid = userid;
			this.loadingtime = loadingtime;
			this.payer = payer;
			this.sourceimage1 = sourceimage1;
			this.sourceimage2 = sourceimage2;
			this.sourceimage3 = sourceimage3;
			this.carname = carname;
			this.longitude = longitude;
			this.latitude = latitude;
			this.endlongitude = endlongitude;
			this.endlatitude = endlatitude;
			this.publishstatus = "1";
			this.hytypename = hytypename;
			this.carlengthabove = carlengthabove;
			this.status = "0";
			this.ishowremark = ishowremark;

		}
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			Intent data) {
		switch (requestCode) {
		case C.FOR_CAR_CODE:
			if(RESULT_OK==resultCode)
			{
				GoodsListBean.GoodsInfo goodsInfoList=(GoodsListBean.GoodsInfo)data.getSerializableExtra("goodsinfo");
				et_newpo.setText(goodsInfoList.getPonum());
				ws_maoyileixing.setItems(maoyileixing, Integer.parseInt(goodsInfoList
						.getMytype()));
				ws_baozhuangfangshi.setItems(baozhuangfangshi, Integer.parseInt(goodsInfoList
						.getBztype()));
				et_jianshu.setText(goodsInfoList.getNum());
				et_huowuzhongliang.setText(goodsInfoList
						.getGoodsweight());
				et_huowutiji.setText(goodsInfoList.getGoodstj());
				et_chaochicun.setText(goodsInfoList.getMaxgoodssize());
				et_guixingguiliang.setText(goodsInfoList.getGxgl());
				et_pinming.setText(goodsInfoList.getDetailname());
				int intDun = 0;
				if (goodsInfoList.getUnit().endsWith("吨")) {
					intDun = 0;
				} else {
					intDun = 1;
				}
				ws_dun.setItems(dunlist,intDun);

			}
			break;
			
		case C.SELECT_PIC_RESULT:

			showLD("正在处理，请稍候...");
			final String imagePath = data.getStringExtra("imagePath");
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					switch (requestCode) {
					case 1:
						goodsBitmap = imageCompression.getImage(imagePath,
								requestCode);
						
						break;
					case 2:
						otherBitmap1 = imageCompression.getImage(imagePath,
								requestCode);
						iv_other_picone.setImageBitmap(Utils.getLoacalBitmap(imagePath));
						break;
					case 3:
						otherBitmap2 = imageCompression.getImage(imagePath,
								requestCode);
						break;
					case 4:
						otherBitmap3 = imageCompression.getImage(imagePath,
								requestCode);
						break;
					default:
						break;
					}
				}
			}).start();

			break;
			
		case 0:
			Log.e("syl", data.getData()+"");
			Bitmap smallBitmap = FileUtils.getSmallBitmap(
					PublicGoodsActivity.this, data.getData().toString());
			iv_other_picone.setImageBitmap(smallBitmap);
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int requestCode = msg.what;
			String srcPath = (String) msg.obj;
			Bitmap smallBitmap = FileUtils.getSmallBitmap(
					PublicGoodsActivity.this, srcPath);
			switch (requestCode) {
			case 1:
				iv_update_pic.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case 2:
				iv_other_picone.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case 3:
				iv_other_pictwo.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case 4:
				iv_other_picthree.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case 12121:
				cancelLD();
				MyToast.showToast(PublicGoodsActivity.this, "图片加载失败");
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onSuccess(String srcPath, int requestCode) {
		// TODO Auto-generated method stub

		Message msg = handler.obtainMessage();
		msg.what = requestCode;
		msg.obj = srcPath;
		handler.sendMessage(msg);

	}

	@Override
	public void onFaild(String srcPath, int requestCode) {
		handler.sendEmptyMessage(12121);

	}
}
