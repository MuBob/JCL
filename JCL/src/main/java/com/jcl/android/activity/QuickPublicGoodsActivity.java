package com.jcl.android.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;
/**
 * 快捷发布
 * @author msz
 *
 */
public class QuickPublicGoodsActivity extends BaseActivity implements
		OnClickListener {

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;

	private TextView tv_chufadi, tv_mudidi, tv_fahuoshijian_left,tv_fahuoshijian_right,tv_chexing;
	private WhSpinner ws_huowuleixing,ws_pingtaixuanze,ws_chechang,ws_jinjichendu,ws_dun;

	private RadioButton cb_paohuo, cb_zhonghuo;
	private EditText et_car_num,et_teshubeizhu,tv_fahuoren, tv_fahuoren_tel,et_price,et_huowuname;
	private Button btn_public, btn_quick_public;
	private EditText et_huowuzhongliang,et_huowutiji,et_saying;

	private List<WhSpinner.Item> huowuleixing;
	private List<WhSpinner.Item> maoyileixing;
	private List<WhSpinner.Item> fufeifangshi;
	private List<WhSpinner.Item> baozhuangfangshi;
	private List<WhSpinner.Item> qianshouhuidan;
	private List<WhSpinner.Item> fapiao;
	private List<WhSpinner.Item> chechang;
	private List<WhSpinner.Item> pingtaixuanze;
	private List<WhSpinner.Item> jinjichendu;
	private List<WhSpinner.Item> dunlist;
	private MyUINavigationView uINavigationView;
	
	private List<String> listcarlength;
	private String title = "选择";
	private Builder builder = null;
	
	boolean[] flags;//初始复选情况
    private String[] cartypeitems=null;
    private StringBuffer result;//显示所选车型
    private StringBuffer chexing;//车型  1选中  0未选中	
    private String [] cartype = null;
    private boolean isSelect;
    private CheckBox cb_carlength,cb_beizhu;
    private String iscarlengthabove,ishowremark;
    private Bundle bundle;
    private int type;

	public void initWhSpinnerData() {
		huowuleixing = new ArrayList<WhSpinner.Item>();
		chechang = new ArrayList<WhSpinner.Item>();
		pingtaixuanze = new ArrayList<WhSpinner.Item>();
		jinjichendu = new ArrayList<WhSpinner.Item>();
		dunlist = new ArrayList<WhSpinner.Item>();
		String[] huowuleixings = getResources().getStringArray(
				R.array.huowuleixing);
		for (int i = 0; i < huowuleixings.length; i++) {
			huowuleixing.add(new WhSpinner.Item(huowuleixings[i], i));
		}
		ws_huowuleixing.setItems(huowuleixing, 0);

		String[] pingtais = getResources().getStringArray(
				R.array.pingtaixuanze);
		for (int i = 0; i < pingtais.length; i++) {
			pingtaixuanze.add(new WhSpinner.Item(pingtais[i], i));
		}
		ws_pingtaixuanze.setItems(pingtaixuanze, type);
		
		String[] chechangs = getResources().getStringArray(
				R.array.chechang);
		for (int i = 0; i < chechangs.length; i++) {
			chechang.add(new WhSpinner.Item(chechangs[i], i));
		}
		ws_chechang.setItems(chechang, 0);
		
		String[] jinji = getResources().getStringArray(
				R.array.jinjichengdu);
		for (int i = 0; i < jinji.length; i++) {
			jinjichendu.add(new WhSpinner.Item(jinji[i], i));
		}
		ws_jinjichendu.setItems(jinjichendu, 0);
		
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
		setContentView(R.layout.activity_quick_public_goods);
		initNavigation();
		flags = new boolean[]
				{false,false,false,false,false,false,false,false,false,false,false,false,false};
		chexing = new StringBuffer();
		result = new StringBuffer();
		initView();
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		datePickerPopupwindow = new DatePickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		initWhSpinnerData();
	}

	private void initNavigation() {
		// TODO Auto-generated method stub
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
	private void initView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		bundle = intent.getExtras();
		if (bundle != null) {
			type = bundle.getInt("type");
		} else {
			type = 0;
		}
		
		tv_chufadi = (TextView) findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) findViewById(R.id.tv_mudidi);
		tv_fahuoshijian_left = (TextView) findViewById(R.id.tv_fahuoshijian_left);
		tv_fahuoshijian_right = (TextView) findViewById(R.id.tv_fahuoshijian_right);
		ws_huowuleixing = (WhSpinner) findViewById(R.id.ws_huowuleixing);
		ws_pingtaixuanze = (WhSpinner) findViewById(R.id.ws_pingtaixuanze);
		ws_jinjichendu = (WhSpinner) findViewById(R.id.ws_jinjichendu);
		ws_chechang = (WhSpinner) findViewById(R.id.ws_chechang);
		tv_chexing = (TextView) findViewById(R.id.tv_chexing);
		ws_dun = (WhSpinner) findViewById(R.id.ws_dun);
		et_teshubeizhu = (EditText) findViewById(R.id.et_teshubeizhu);
		et_price = (EditText) findViewById(R.id.et_price);
		et_huowuname = (EditText) findViewById(R.id.et_huowuname);
		et_huowuzhongliang = (EditText) findViewById(R.id.et_huowuzhongliang);
		et_huowutiji = (EditText) findViewById(R.id.et_huowutiji);
		et_saying = (EditText) findViewById(R.id.et_saying);

		et_car_num = (EditText) findViewById(R.id.et_car_num);
		cb_paohuo = (RadioButton) findViewById(R.id.cb_paohuo);
		cb_zhonghuo = (RadioButton) findViewById(R.id.cb_zhonghuo);
		btn_public = (Button) findViewById(R.id.btn_public);
		btn_quick_public = (Button) findViewById(R.id.btn_quick_public);
		
		tv_fahuoren = (EditText) findViewById(R.id.tv_fahuoren);
		tv_fahuoren_tel = (EditText) findViewById(R.id.tv_fahuoren_tel);
		tv_fahuoren_tel.setText(SharePerfUtil.getLoginName());
		tv_fahuoren.setText(SharePerfUtil.getLinkMan());
		
		cb_carlength = (CheckBox) findViewById(R.id.cb_carlength);
		cb_beizhu = (CheckBox) findViewById(R.id.cb_beizhu);
		cb_carlength.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switch (buttonView.getId()) {
				case R.id.cb_carlength:
					if(isChecked)
					{
						iscarlengthabove = "1";
					}else{
						iscarlengthabove = "0";
					}
					break;

				default:
					break;
				}
				
			}
		});
		cb_beizhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

//		switch (type) {
//		case 4:
//			cartype = new String[] {"1","0","0","0","0","0","0","0","0","0","0","0","0"};
//			tv_chexing.setText("集装箱车");
//			for (int i = 0; i < cartype.length; i++) {
//				chexing.append(cartype[i]+",");
//			}
//			break;
//			
//		case 5:
//			cartype = new String[] {"0","0","0","0","0","0","0","1","0","0","0","0","0"};
//			tv_chexing.setText("高栏车");
//			for (int i = 0; i < cartype.length; i++) {
//				chexing.append(cartype[i]+",");
//			}
//			break;
//			
//		case 6:
//			cartype = new String[] {"0","1","0","0","0","0","0","0","0","0","0","0","0"};
//			tv_chexing.setText("冷藏车");
//			for (int i = 0; i < cartype.length; i++) {
//				chexing.append(cartype[i]+",");
//			}
//			break;
//			
//		case 7:
//			cartype = new String[] {"0","0","0","0","1","0","0","0","0","0","0","0","0"};
//			tv_chexing.setText("平板车");
//			for (int i = 0; i < cartype.length; i++) {
//				chexing.append(cartype[i]+",");
//			}
//			break;
//			
//		case 8:
//			cartype = new String[] {"0","0","0","1","0","0","0","0","0","0","0","0","0"};
//			tv_chexing.setText("保温车");
//			for (int i = 0; i < cartype.length; i++) {
//				chexing.append(cartype[i]+",");
//			}
//			break;
//			
//		case 9:
//			cartype = new String[] {"0","0","1","0","0","0","0","0","0","0","0","0","0"};
//			tv_chexing.setText("厢式货车");
//			for (int i = 0; i < cartype.length; i++) {
//				chexing.append(cartype[i]+",");
//			}
//			break;
//
//		default:
//			
//			break;
//		}

		tv_chufadi.setOnClickListener(this);
		tv_mudidi.setOnClickListener(this);
		tv_fahuoshijian_left.setOnClickListener(this);
		tv_fahuoshijian_right.setOnClickListener(this);

		btn_public.setOnClickListener(this);
		btn_quick_public.setOnClickListener(this);
		tv_chexing.setOnClickListener(this);
	}

	protected Dialog onCreateDialog(int type){
		cartypeitems = getResources().getStringArray(R.array.chexing);
		Dialog dialog=null;
		tv_chexing.setText("");
		Builder builder=new android.app.AlertDialog.Builder(this);
//        //设置对话框的图标
//        builder.setIcon(R.drawable.header);
        //设置对话框的标题
        builder.setTitle("车型选择(可多选)");
        builder.setMultiChoiceItems(R.array.chexing, flags, new DialogInterface.OnMultiChoiceClickListener(){
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
            	cartype = new String[] {"0","0","0","0","0","0","0","0","0","0","0","0","0"};
            	if (isSelect) {
                    for (int i = 0; i < flags.length-1; i++) {
                        if(flags[i]){
                        	result.append(cartypeitems[i]+",");
                            cartype [i] = "1";
                        }else{
                        	cartype [i] = "0";
                        }
                    }
                	
                	for (int i = 0; i < cartype.length; i++) {
    					chexing.append(cartype[i]+",");
    				}	
                	tv_chexing.setText(result.toString().substring(0, result.length()-1));
                	
				}else{
					MyToast.showToast(QuickPublicGoodsActivity.this, "请选择车型");
					tv_chexing.setText("车型选择");
				}
            }
        });
        
      //添加一个取消按钮
        builder.setNegativeButton("取  消", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tv_chexing.setText("车型选择");
                result = new StringBuffer();
            	cartype = new String[] {"0","0","0","0","0","0","0","0","0","0","0","0","0"};
            }
        });
        dialog = builder.create();
        return dialog;
	}
	
	private String chufadiCode, mudidiCode,chufaCityLat,chufaCityLnt,mudiCityLat,mudiCityLnt;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
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
			default:
				break;
			}
		}

	};
	
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

		case R.id.btn_quick_public:
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

			if (chexing.length() == 0) {
				MyToast.showToast(this, "请选择车型");
				return;
			}
			if (TextUtils.isEmpty(tv_fahuoren.getText().toString())) {
				MyToast.showToast(this, "请输入发货人");
				return;
			}
			if (TextUtils.isEmpty(tv_fahuoren_tel.getText().toString())) {
				MyToast.showToast(this, "请输入发货人");
				return;
			}
			if (TextUtils.isEmpty(et_price.getText().toString())) {
				MyToast.showToast(this, "请输入期望价格");
				return;
			}
			if (TextUtils.isEmpty(et_huowuname.getText().toString())) {
				MyToast.showToast(this, "请输入货物名称");
				return;
			}
			String carlenth = "";
			if (ws_chechang.getChoiceText().equals("不限")) {
				carlenth = "0";
			} else {
				carlenth = ws_chechang.getChoiceText().substring(0,ws_chechang.getChoiceText().length() - 1);
			}
			Data data = new Data(ws_jinjichendu.getChoicePosition()+"", chufadi, mudidi,
					fahuoshijianleft, fahuoshijianright, ws_huowuleixing.getChoicePosition()+"",
					huowuleixing_type, ws_pingtaixuanze.getChoicePosition()+"", 
					chexing.substring(0, chexing.length()-1),
					et_car_num.getText().toString(), carlenth,
					JCLApplication.getInstance().getUserId(),chufadiCode,mudidiCode,
					et_teshubeizhu.getText().toString(),chufaCityLnt,chufaCityLat,mudiCityLnt,
					mudiCityLat,tv_chexing.getText().toString(),
					ws_huowuleixing.getChoiceText().toString(),iscarlengthabove,
					et_huowuzhongliang.getText().toString(),et_huowutiji.getText().toString(),
					tv_fahuoren.getText().toString(),tv_fahuoren_tel.getText().toString(),
					ishowremark,"1","0",et_saying.getText().toString(),et_price.getText().toString(),
					et_huowuname.getText().toString(),ws_dun.getChoiceText());
			String dataJson = new Gson().toJson(data);
			PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(
					dataJson);
			String jsonRequest = new Gson().toJson(publicGoodsRequest);
			showLD("提交中...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(jsonRequest),
					new Listener<BaseBean>() {
						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(
											QuickPublicGoodsActivity.this,
											"发布成功");
									finish();
									startActivity(new Intent(QuickPublicGoodsActivity.this,SettingPublicGoodsActivity.class));
								} else {
									MyToast.showToast(
											QuickPublicGoodsActivity.this,
											"发布失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(QuickPublicGoodsActivity.this,
									"发布失败");
						}
					}));
			break;

		case R.id.btn_public:
			submit();
			
			startActivity(new Intent(this, PublicGoodsActivity.class));
			break;

		default:
			break;
		}

	}
	String jinjichengdu = "";
	String chufadi = "";
	String mudidi = "";
	String fahuoshijianleft = "";
	String fahuoshijianright = "";
	String huowuleixing_type = "";
	String huowuleixingstr ="";
	public void submit(){
				
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
		
		if (TextUtils.isEmpty(tv_fahuoshijian_left.getText().toString())) {
			MyToast.showToast(this, "请选择预计发货时间");
			return;
		}
		fahuoshijianleft = tv_fahuoshijian_left.getText().toString();
		
		if (cb_paohuo.isChecked()) {
			huowuleixing_type = "1";
		} else if (cb_zhonghuo.isChecked()) {
			huowuleixing_type = "2";
		}
		if (chexing.length() == 0) {
			MyToast.showToast(this, "请选择车型");
			return;
		}
	}

	class PublicGoodsRequest {
		private String operate;
		private String type;
		private String data;

		public PublicGoodsRequest(String data) {
			this.operate = "A";
			this.type = "1001";
			this.data = data;
		}
	}

	class Data {
		private String jjdegree; // 紧急程度
		private String startarea; // 开始地区
		private String endarea; // 目的地区
		private String exfhstarttime; // 预计发货时间开始
		private String exfhendtime; // 预计发货时间结束
		private String hwtype; // 货物类型
		private String pztype; // 泡重 1泡，2重
		private String detailname; // 具体品名
		private String ptchoose; // 平台选择
		private String cartype; // 车型车长
		private String needcarnum; // 需要车数量
		private String publishstatus; // 发布状态 0关,1开
		private String createtime; // 创建时间
		private String effectivetime;// 信息有效时间
		private String effectiveflag; // 有效flag 1有效、0无效
		private String userid; // 用户编码
		private String carlength;//车长
		private String startcode;
		private String endcode;
		private String remark;// 备注
		private String carname;// 车名
		
		private String longitude; // 出发地经度
		private String latitude; // 出发地纬度
		private String endlongitude;//目的地经度
		private String endlatitude;//目的地纬度
		private String hytypename;//货物类型名称
		private String carlengthabove;
		private String status;
		
		private String goodsweight;// 货物总重量 　
		private String goodstj;// 货物总体积 
		private String fhlinkman;// 发货人
		private String fhlinkmantel;// 发货人手机号
		private String ishowremark;//是否显示备注
		private String ispc;
		private String gone;
		private String saying;
		private String haspricelow;
		private String unit;

		public Data(String jjdegree, String startarea, String endarea,
				String exfhstarttime, String exfhendtime, String hwtype,
				String pztype, String ptchoose, String cartype,
				String needcarnum, String carlength,String userid,String startcode,
				String endcode,String remark,String longitude,String latitude,
				String endlongitude,String endlatitude,String carname,
				String hytypename,String carlengthabove,
				String goodsweight, String goodstj,String fhlinkman,
				String fhlinkmantel,String ishowremark,String ispc,
				String gone,String saying,String haspricelow,
				String detailname,String unit) {

			this.unit = unit;
			this.detailname = detailname;
			this.saying = saying;
			this.gone = gone;
			this.ispc = ispc;
			this.carlength = carlength;
			this.jjdegree = jjdegree;
			this.startarea = startarea;
			this.endarea = endarea;
			this.exfhstarttime = exfhstarttime;
			this.exfhendtime = exfhendtime;
			this.hwtype = hwtype;
			this.pztype = pztype;
			this.detailname = "";
			this.ptchoose = ptchoose;
			this.cartype = cartype;
			this.needcarnum = needcarnum;
			this.publishstatus = "0";
			this.createtime = "";
			this.effectivetime = "";
			this.effectiveflag = "";
			this.userid = userid;
			this.remark = remark;
			
			this.startcode = startcode;
			this.endcode = endcode;
			this.longitude = longitude;
			this.latitude = latitude;
			this.endlongitude = endlongitude;
			this.endlatitude = endlatitude;
			this.carname = carname;
			this.hytypename = hytypename;
			this.carlengthabove = carlengthabove;
			this.status = "0";
			this.fhlinkman  = fhlinkman;
			this.fhlinkmantel = fhlinkmantel;
			this.goodsweight = goodsweight;
			this.goodstj = goodstj;
			this.ishowremark = ishowremark;
			this.haspricelow = haspricelow;

		}

	}

}
