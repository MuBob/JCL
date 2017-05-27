package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.CarInfo;
import com.jcl.android.bean.CarListBean;
import com.jcl.android.bean.DetailFindCarBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.request.DetailFindCarsRequest;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;
import com.jcl.android.view.WhSpinnerOnItemChangedListener;
import com.jcl.android.view.WhSpinner.Item;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PublishCarActivity extends BaseActivity implements OnClickListener {
	private MyUINavigationView uINavigationView;
	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;
	private DatePickerPopupwindow calendarPopupwindow;

	private EditText tv_platenum;// 车牌号
	private EditText edt_linkman;// 联系人
	private EditText edt_tel;// 手机
	private TextView tv_startarea;// 出发地
	private TextView tv_endarea;// 目的地
	private EditText tv_tjplace;// 途经点
	private TextView tv_emptytime_from;// 空闲时间
	private TextView tv_emptytime_to;// 空闲时间
	private EditText edt_expectfee;// 期望费用
	private EditText edt_bcintroduce;// 补充说明
	private CheckBox cb_show;
	private EditText edt_company;// 发布企业
	private com.jcl.android.view.WhSpinner tv_cartype;// 车型
	private List<WhSpinner.Item> chexing;
	private com.jcl.android.view.WhSpinner ws_pingtaixuanze;// 平台选择

	private com.jcl.android.view.WhSpinner tv_jjdegree;// 紧急程度

	private com.jcl.android.view.WhSpinner ws_cytype;// 车源类型
	private TextView tv_carlength_title;
	private com.jcl.android.view.WhSpinner edt_carlength;// 车长

	private Button btn_submit;// 提交
	private Button btn_car;// 选择车辆

	// private Button btn_preview;//预览

	private CheckBox cb_longterm;// 长期货源
	private String ishowremark = "0";// 0不显示 1显示
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_publish_car);
		afterCreate();
		initNavigation();
		initView();
		initWhSpinnerData();
		if (!TextUtils.isEmpty(id)) {
			loadData();
		}
		if(getIntent().hasExtra("carinfo")){
			CarInfo carInfo = (CarInfo) getIntent().getSerializableExtra("carinfo");
			tv_platenum.setText(carInfo.getPlatenum());
			edt_carlength.setChoiceText(carInfo.getCarlength());
			edt_carlength.closeOnClick();
			tv_cartype.setChoiceText(carInfo.getCartype());
			tv_cartype.setChoicePosition(carInfo.getCartypecode());
			tv_cartype.closeOnClick();
			for (int i = 0; i < chechang.size() - 1; i++) {
				if (carInfo.getCarlength().equals(chechang.get(i)))
					edt_carlength.setChoiceText(chechang.get(i).toString());
			}
			vanid = carInfo.get_id();
		}
	}

	String id;
	private void afterCreate() {
		if (getIntent().hasExtra("id")) {
			id = getIntent().getStringExtra("id");
		}
		
		
		
	}

	private void initView() {
		Intent intent = getIntent();
		bundle = intent.getExtras();
		cityPickerPopupwindow = new CityPickerPopupwindow(this, findViewById(R.id.ll_parent), mHandler, null);
		// datePickerPopupwindow = new DatePickerPopupwindow(this,
		// findViewById(R.id.ll_parent), mHandler, null);
		calendarPopupwindow = new DatePickerPopupwindow(this, findViewById(R.id.ll_parent), mHandler, null);

		tv_platenum = (EditText) findViewById(R.id.tv_platenum);
		tv_platenum.setOnClickListener(this);
		btn_car = (Button) findViewById(R.id.btn_car);
		btn_car.setOnClickListener(this);

		edt_linkman = (EditText) findViewById(R.id.edt_linkman);
		edt_linkman.setText(SharePerfUtil.getLinkMan());

		edt_company = (EditText) findViewById(R.id.edt_company);
		edt_company.setText(SharePerfUtil.getCompanyName());
		edt_tel = (EditText) findViewById(R.id.edt_tel);
		edt_tel.setText(SharePerfUtil.getLoginName());
		tv_startarea = (TextView) findViewById(R.id.tv_startarea);
		tv_startarea.setOnClickListener(this);
		tv_endarea = (TextView) findViewById(R.id.tv_endarea);
		tv_endarea.setOnClickListener(this);
		tv_tjplace = (EditText) findViewById(R.id.tv_tjplace);
		tv_emptytime_from = (TextView) findViewById(R.id.tv_emptytime_from);
		tv_emptytime_from.setOnClickListener(this);
		tv_emptytime_to = (TextView) findViewById(R.id.tv_emptytime_to);
		tv_emptytime_to.setOnClickListener(this);

		edt_expectfee = (EditText) findViewById(R.id.edt_expectfee);
		edt_bcintroduce = (EditText) findViewById(R.id.edt_bcintroduce);
		cb_show = (CheckBox) findViewById(R.id.cb_show);
		cb_show.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					ishowremark = "1";
				} else {
					ishowremark = "0";
				}

			}
		});

		tv_jjdegree = (com.jcl.android.view.WhSpinner) findViewById(R.id.tv_jjdegree);
		ws_pingtaixuanze = (com.jcl.android.view.WhSpinner) findViewById(R.id.ws_pingtaixuanze);
		ws_cytype = (com.jcl.android.view.WhSpinner) findViewById(R.id.ws_cytype);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);

		// btn_preview=(Button) findViewById(R.id.btn_preview);
		// btn_preview.setOnClickListener(this);

		cb_longterm = (CheckBox) findViewById(R.id.cb_longterm);
		cb_longterm.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				switch (buttonView.getId()) {
				case R.id.cb_longterm:
					if (isChecked) {
						islongterm = "1";
					} else {
						islongterm = "0";
					}
					break;

				default:
					break;
				}
			}
		});
		tv_carlength_title = (TextView) findViewById(R.id.tv_carlength_title);
		edt_carlength = (com.jcl.android.view.WhSpinner) findViewById(R.id.edt_carlength);
		tv_cartype = (com.jcl.android.view.WhSpinner) findViewById(R.id.tv_cartype);
		tv_cartype.setWhSpinnerOnItemChangedListener(new WhSpinnerOnItemChangedListener() {

			@Override
			public void onItemChanged(WhSpinner whSpinner, int item) {
				// TODO Auto-generated method stub
				if (item == 0) {
					tv_carlength_title.setVisibility(View.VISIBLE);
					edt_carlength.setVisibility(View.VISIBLE);
					edt_carlength.setChoiceText("12米");
					edt_carlength.closeOnClick();
					isjizhuangxiang = true;
				} else {
					tv_carlength_title.setVisibility(View.VISIBLE);
					edt_carlength.setVisibility(View.VISIBLE);
					edt_carlength.openOnClick();
					isjizhuangxiang = false;
				}
			}
		});
	}

	private boolean isjizhuangxiang = false;

	private List<WhSpinner.Item> jjdegrees;
	private List<WhSpinner.Item> pingtaixuanze;
	private List<WhSpinner.Item> cytypes;
	private List<WhSpinner.Item> chechang;

	public void initWhSpinnerData() {

		chexing = new ArrayList<WhSpinner.Item>();
		String[] chexingArr = getResources().getStringArray(R.array.chexing);
		for (int i = 0; i < chexingArr.length; i++) {
			chexing.add(new Item(chexingArr[i], i));
		}
		tv_cartype.setItems(chexing);
		tv_cartype.setChoiceText("");

		jjdegrees = new ArrayList<WhSpinner.Item>();
		jjdegrees.add(new WhSpinner.Item("实时", "1"));
		jjdegrees.add(new WhSpinner.Item("加急", "0"));

		tv_jjdegree.setItems(jjdegrees);
		tv_jjdegree.setChoiceText("");

		pingtaixuanze = new ArrayList<WhSpinner.Item>();
		String[] pingtais = getResources().getStringArray(R.array.pingtaixuanze);
		for (int i = 0; i < pingtais.length; i++) {
			pingtaixuanze.add(new WhSpinner.Item(pingtais[i], i));
		}
		ws_pingtaixuanze.setItems(pingtaixuanze);
		ws_pingtaixuanze.setChoiceText("");

		cytypes = new ArrayList<WhSpinner.Item>();
		cytypes.add(new WhSpinner.Item("顺风车", "0"));
		cytypes.add(new WhSpinner.Item("回程车", "1"));
		cytypes.add(new WhSpinner.Item("本地车", "2"));

		if (bundle != null && bundle.getString("huichengche") != null && bundle.getString("huichengche").equals("1")) {
			ws_cytype.setItems(cytypes, 1);
		} else {
			ws_cytype.setItems(cytypes);
			ws_cytype.setChoiceText("");
		}
		if (bundle != null && bundle.getString("shunfengche") != null && bundle.getString("shunfengche").equals("2")) {
			ws_cytype.setItems(cytypes, 0);
		} else {
			ws_cytype.setItems(cytypes);
			ws_cytype.setChoiceText("");
		}

		chechang = new ArrayList<WhSpinner.Item>();
		String[] chechangArr = getResources().getStringArray(R.array.chechang);
		for (int i = 0; i < chechangArr.length; i++) {
			if (!"不限".equals(chechangArr[i])) {
				chechang.add(new Item(chechangArr[i], i));
			}
		}
		edt_carlength.setItems(chechang);
		edt_carlength.setChoiceText("");

	}

	Intent intent;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_startarea:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1, "submit");
			break;

		case R.id.tv_endarea:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(2, "submit");
			break;
		case R.id.tv_emptytime_from:
			// if (datePickerPopupwindow != null)
			// datePickerPopupwindow.show(3);
			if (calendarPopupwindow != null)
				calendarPopupwindow.show(3);

			break;
		case R.id.tv_emptytime_to:
			// if (datePickerPopupwindow != null)
			// datePickerPopupwindow.show(4);
			if (calendarPopupwindow != null)
				calendarPopupwindow.show(4);
			break;
		case R.id.tv_tjplace:
			// if (cityPickerPopupwindow != null)
			// cityPickerPopupwindow.show(5);
			break;
		case R.id.btn_car:
			intent = new Intent(PublishCarActivity.this, CarManageActivity.class);
			startActivityForResult(intent.putExtra("key", C.FOR_CAR_CODE), C.FOR_CAR_CODE);
			break;
		case R.id.btn_submit:
			submit();
			break;
		/*
		 * case R.id.btn_preview: preview(); break;
		 */
		default:
			break;
		}

	}

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
			}
		});
		if (TextUtils.isEmpty(id)) {
			uINavigationView.getTv_title().setText("添加车源");
		} else {
			uINavigationView.getTv_title().setText("车源编辑");
		}
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

		public PublishCarRequest(String data, String key, String operate) {
			this.type = "1006";
			this.data = data;
			this.key = key;
			this.operate = operate;
		}
	}

	class CarData {
		public CarData(String userid, String platenum, String linkman, String companyname, String tel, String startarea,
				String startcode, String endarea, String endcode, String tjplace, String expectfee,
				String emptytimestart, String emptytimeend, String commony, String jjdegree, String bcintroduce,
				String vanid, String cytype, String isLongTermSource, String carlength, String cartype,
				String cartypecode, String isshow, String gone, String commonycode, String jjdegreecode) {
			this.gone = gone;
			this.userid = userid;
			this.platenum = platenum;
			this.linkman = linkman;
			this.companyname = companyname;
			this.tel = tel;
			this.startarea = startarea;
			this.startcode = startcode;
			this.endarea = endarea;
			this.endcode = endcode;
			this.tjplace = tjplace;
			this.expectfee = expectfee;
			this.emptytimestart = emptytimestart;
			this.emptytimeend = emptytimeend;
			this.commony = commony;
			this.commonycode = commonycode;
			this.jjdegree = jjdegree;
			this.jjdegreecode = jjdegreecode;
			this.bcintroduce = bcintroduce;
			this.vanid = vanid;
			this.cytype = cytype;
			this.latitude = "" + JCLApplication.getInstance().getMyLocation().getLatitude();
			this.longitude = "" + JCLApplication.getInstance().getMyLocation().getLongitude();
			this.isLongTermSource = isLongTermSource;
			this.carlength = carlength;
			this.cartype = cartype;
			this.cartypecode = cartypecode;
			this.isshow = isshow;
		}

		private String userid;
		private String platenum;// 车牌号b
		private String linkman;// 联系人
		private String tel;// 手机号
		private String startarea;// 出发地
		private String startcode;// 出发地编码
		private String endarea;// 目的地
		private String endcode;// 目的地编码
		private String tjplace;// 途径地点
		private String expectfee;// 期望运费
		private String emptytimestart;// 空车时间开始
		private String emptytimeend;// 空车时间结束
		private String isRealPrice;// 是否实价保证----------
		private String emptytime;// 空车时间（废弃）
		private String commony;// 平台选择
		private String commonycode;// 平台选择编码
		private String jjdegree;// 紧急程度
		private String jjdegreecode;
		private String bcintroduce;// 补充说明
		private String vanid;// 车辆id
		private String cytype;// 车源类型
		private String cytypecode;// 车源类型code
		private String companyname;// 公司名称
		private String longitude;// 经度
		private String latitude;// 纬度
		private String createtime;// 创建时间
		private String effectivetime;// 有效时间
		private String gone;
		private String effectiveflag;// 有效flag(1有效、0无效)
		private String status;// 发布状态（0关,1开）
		private String isLongTermSource;// 长期货源
		private String cartype;
		private String cartypecode;
		private String carlength;
		private String carlengthcode;
		private String isshow;

		public String getUserid() {
			return userid;
		}

		public String getCarlengthcode() {
			return carlengthcode;
		}

		public void setCarlengthcode(String carlengthcode) {
			this.carlengthcode = carlengthcode;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getTjplace() {
			return tjplace;
		}

		public void setTjplace(String tjplace) {
			this.tjplace = tjplace;
		}

		public String getVanid() {
			return vanid;
		}

		public void setVanid(String vanid) {
			this.vanid = vanid;
		}

		public String getGone() {
			return gone;
		}

		public void setGone(String gone) {
			this.gone = gone;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getPlatenum() {
			return platenum;
		}

		public void setPlatenum(String platenum) {
			this.platenum = platenum;
		}

		public String getLinkman() {
			return linkman;
		}

		public void setLinkman(String linkman) {
			this.linkman = linkman;
		}

		public String getStartarea() {
			return startarea;
		}

		public void setStartarea(String startarea) {
			this.startarea = startarea;
		}

		public String getStartcode() {
			return startcode;
		}

		public void setStartcode(String startcode) {
			this.startcode = startcode;
		}

		public String getEndarea() {
			return endarea;
		}

		public void setEndarea(String endarea) {
			this.endarea = endarea;
		}

		public String getEndcode() {
			return endcode;
		}

		public void setEndcode(String endcode) {
			this.endcode = endcode;
		}

		public String getExpectfee() {
			return expectfee;
		}

		public void setExpectfee(String expectfee) {
			this.expectfee = expectfee;
		}

		public String getEmptytimestart() {
			return emptytimestart;
		}

		public void setEmptytimestart(String emptytimestart) {
			this.emptytimestart = emptytimestart;
		}

		public String getEmptytimeend() {
			return emptytimeend;
		}

		public void setEmptytimeend(String emptytimeend) {
			this.emptytimeend = emptytimeend;
		}

		public String getIsRealPrice() {
			return isRealPrice;
		}

		public void setIsRealPrice(String isRealPrice) {
			this.isRealPrice = isRealPrice;
		}

		public String getEmptytime() {
			return emptytime;
		}

		public void setEmptytime(String emptytime) {
			this.emptytime = emptytime;
		}

		public String getCommony() {
			return commony;
		}

		public void setCommony(String commony) {
			this.commony = commony;
		}

		public String getCommonycode() {
			return commonycode;
		}

		public void setCommonycode(String commonycode) {
			this.commonycode = commonycode;
		}

		public String getJjdegree() {
			return jjdegree;
		}

		public void setJjdegree(String jjdegree) {
			this.jjdegree = jjdegree;
		}

		public String getJjdegreecode() {
			return jjdegreecode;
		}

		public void setJjdegreecode(String jjdegreecode) {
			this.jjdegreecode = jjdegreecode;
		}

		public String getBcintroduce() {
			return bcintroduce;
		}

		public void setBcintroduce(String bcintroduce) {
			this.bcintroduce = bcintroduce;
		}

		public String getCytypecode() {
			return cytypecode;
		}

		public void setCytypecode(String cytypecode) {
			this.cytypecode = cytypecode;
		}

		public String getCompanyname() {
			return companyname;
		}

		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getCreatetime() {
			return createtime;
		}

		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public String getEffectivetime() {
			return effectivetime;
		}

		public void setEffectivetime(String effectivetime) {
			this.effectivetime = effectivetime;
		}

		public String getEffectiveflag() {
			return effectiveflag;
		}

		public void setEffectiveflag(String effectiveflag) {
			this.effectiveflag = effectiveflag;
		}

		public String getIsLongTermSource() {
			return isLongTermSource;
		}

		public void setIsLongTermSource(String isLongTermSource) {
			this.isLongTermSource = isLongTermSource;
		}

		public String getCartypecode() {
			return cartypecode;
		}

		public void setCartypecode(String cartypecode) {
			this.cartypecode = cartypecode;
		}

		public String getIsshow() {
			return isshow;
		}

		public void setIsshow(String isshow) {
			this.isshow = isshow;
		}

		public String getCartype() {
			return cartype;
		}

		public void setCartype(String cartype) {
			this.cartype = cartype;
		}

		public String getCarlength() {
			return carlength;
		}

		public void setCarlength(String carlength) {
			this.carlength = carlength;
		}

		public String getCytype() {
			return cytype;
		}

		public void setCytype(String cytype) {
			this.cytype = cytype;
		}

	}

	String data;
	String jsonRequest;

	String commonyStr;// 平台选择
	String jjdegreeStr;// 紧急程度字符
	String vanid;
	String islongterm = "0";

	private void preview() {

	}

	private void submit() {

		if (TextUtils.isEmpty(tv_platenum.getText().toString())) {
			Toast.makeText(PublishCarActivity.this, "请填写车牌号", 1000).show();
			return;
		}
		if (TextUtils.isEmpty(tv_cartype.getChoiceText())) {
			Toast.makeText(PublishCarActivity.this, "请填写车型", 1000).show();
			return;
		}
		if (isjizhuangxiang) {
			edt_carlength.setChoiceText(12 + "米");
		} else {
			if (TextUtils.isEmpty(edt_carlength.getChoiceText())) {
				Toast.makeText(PublishCarActivity.this, "请填写车长", 1000).show();
				return;
			}
		}

		if (TextUtils.isEmpty(tv_startarea.getText().toString())) {
			Toast.makeText(PublishCarActivity.this, "请填写出发地", 1000).show();
			return;
		}
		if (TextUtils.isEmpty(tv_endarea.getText().toString())) {
			Toast.makeText(PublishCarActivity.this, "请填写目的地", 1000).show();
			return;
		}
		String emptytime_from, emptytime_to;
		if (TextUtils.isEmpty(tv_emptytime_from.getText().toString())) {
			Toast.makeText(PublishCarActivity.this, "请填空车开始时间", 1000).show();
			return;
		} else {
			emptytime_from = tv_emptytime_from.getText().toString().trim();
		}
		if (TextUtils.isEmpty(tv_emptytime_to.getText().toString())) {
			emptytime_to = Utils.addData(emptytime_from);
		} else {
			emptytime_to = tv_emptytime_to.getText().toString().trim();
		}

		// if(TextUtils.isEmpty(edt_expectfee.getText().toString()))
		// {
		// Toast.makeText(PublishCarActivity.this, "请填写期望运费",
		// 1000).show();
		// return;
		// }
		if (TextUtils.isEmpty(tv_jjdegree.getChoiceText())) {
			Toast.makeText(PublishCarActivity.this, "请填选择紧急程度", 1000).show();
			return;
		}
		if (TextUtils.isEmpty(ws_cytype.getChoiceText())) {
			Toast.makeText(PublishCarActivity.this, "请填选择车源类型", 1000).show();
			return;
		}
		if (TextUtils.isEmpty(ws_pingtaixuanze.getChoiceText())) {
			Toast.makeText(PublishCarActivity.this, "请填选择平台", 1000).show();
			return;
		}
		// if(TextUtils.isEmpty(edt_linkman.getText().toString()))
		// {
		// Toast.makeText(PublishCarActivity.this, "请填写联系人",
		// 1000).show();
		// return;
		// }
		if (TextUtils.isEmpty(edt_tel.getText().toString())) {
			Toast.makeText(PublishCarActivity.this, "请填写联系人电话", 1000).show();
			return;
		}

		String carlength = "";
		if (TextUtils.isEmpty(edt_carlength.getChoiceText())) {
			carlength = "";
		} else {
			carlength = edt_carlength.getChoiceText().substring(0, edt_carlength.getChoiceText().length() - 1);
		}

		data = new Gson().toJson(new CarData(SharePerfUtil.getUserId(), tv_platenum.getText().toString(),
				edt_linkman.getText().toString(), edt_company.getText().toString(), edt_tel.getText().toString(),
				tv_startarea.getText().toString(), chufadiCode, tv_endarea.getText().toString(), mudidiCode,
				tv_tjplace.getText().toString(), edt_expectfee.getText().toString(), emptytime_from, emptytime_to,
				ws_pingtaixuanze.getChoiceText(), tv_jjdegree.getChoiceText(), edt_bcintroduce.getText().toString(),
				vanid, ws_cytype.getChoiceText(), islongterm, carlength, tv_cartype.getChoiceText(),
				tv_cartype.getChoiceValue().toString(), ishowremark, "0", ws_pingtaixuanze.getChoiceValue().toString(),
				tv_jjdegree.getChoiceValue().toString()));
		if (TextUtils.isEmpty(id)) {
			jsonRequest = new Gson().toJson(new PublishCarRequest(data));
		} else {
			jsonRequest = new Gson().toJson(new PublishCarRequest(data, id, "M"));
		}

		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST, UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest), new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								MyToast.showToast(PublishCarActivity.this, "提交成功");
								startActivity(new Intent(PublishCarActivity.this,SettingPublicCarsActivity.class));
								finish();
							} else {
								Toast.makeText(PublishCarActivity.this, arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(PublishCarActivity.this, "暂无数据！", 1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(PublishCarActivity.this, "网络连接异常！", 1000).show();
					}
				}));
	}

	private void clear() {
		tv_platenum.setText("");
		edt_linkman.setText("");
		edt_tel.setText("");
		tv_startarea.setText("");
		tv_endarea.setText("");
		tv_tjplace.setText("");
		tv_emptytime_from.setText("");
		tv_emptytime_to.setText("");
		edt_expectfee.setText("");
		edt_bcintroduce.setText("");
		ws_pingtaixuanze.setItems(pingtaixuanze, 0);
		tv_jjdegree.setItems(jjdegrees, 0);
	}

	private String chufadiCode = "", mudidiCode = "", tjCode = "";
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
				tv_startarea.setText(info);
				break;
			case 2:
				mudidiCode = bundle.getString("cityCode");
				tv_endarea.setText(info);
				break;
			case 3:
				tv_emptytime_from.setText(info);
				break;
			case 4:
				tv_emptytime_to.setText(info);
				break;
			case 5:
				tjCode = bundle.getString("cityCode");
				tv_tjplace.setText(info);
				break;

			default:
				break;
			}
		}

	};

	class Filters {
		private String _id;

		public Filters(String _id) {
			this._id = _id;
		}

	}

	private void loadData() {
		String filters = new Gson().toJson(new Filters(id));
		String getStr = new Gson().toJson(new DetailFindCarsRequest(filters));
		showLD("加载中...");
		executeRequest(new GsonRequest<DetailFindCarBean>(Request.Method.GET, UrlCat.getSearchUrl(getStr),
				DetailFindCarBean.class, null, null, new Listener<DetailFindCarBean>() {

					@Override
					public void onResponse(DetailFindCarBean arg0) {
						cancelLD();
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								tv_platenum.setText(arg0.getData().getPlatenum());
								edt_linkman.setText(arg0.getData().getLinkman());
								edt_tel.setText(arg0.getData().getTel());
								tv_startarea.setText(arg0.getData().getStartarea());
								tv_endarea.setText(arg0.getData().getEndarea());
								tv_tjplace.setText(arg0.getData().getTjplace());
								tv_emptytime_from.setText(arg0.getData().getEmptytimestart());
								tv_emptytime_to.setText(arg0.getData().getEmptytimeend());
								edt_expectfee.setText(arg0.getData().getExpectfee());
								edt_bcintroduce.setText(arg0.getData().getBcintroduce());
								tv_tjplace.setText(arg0.getData().getTjplace());
								try {

									if (arg0.getData().getJjdegreecode() != null
											&& !"".equals(arg0.getData().getJjdegreecode())) {
										tv_jjdegree.setItems(jjdegrees,
												Integer.parseInt(arg0.getData().getJjdegreecode()));
									}
									if (arg0.getData().getCartypecode() != null
											&& !"".equals(arg0.getData().getCartypecode())) {
										tv_cartype.setItems(chexing, Integer.parseInt(arg0.getData().getCartypecode()));
									}
									if (arg0.getData().getCarlength() != null
											&& !"".equals(arg0.getData().getCarlength())) {
										edt_carlength.setChoiceText(arg0.getData().getCarlength() + "米");
									}
									if (arg0.getData().getCytype() != null && !"".equals(arg0.getData().getCytype())) {
										ws_cytype.setChoiceText(arg0.getData().getCytype());
									}
									if (arg0.getData().getExpectfee() != null
											&& !"".equals(arg0.getData().getExpectfee())) {
										edt_expectfee.setText(arg0.getData().getExpectfee());
									}
									if (arg0.getData().getCommonycode() != null
											&& !"".equals(arg0.getData().getCommonycode())) {
										ws_pingtaixuanze.setItems(pingtaixuanze,
												Integer.parseInt(arg0.getData().getCommonycode()));
										// ws_pingtaixuanze.setItems(pingtaixuanze,
										// Integer.parseInt(arg0.getData().getWxcode()));
									}
									if (arg0.getData().getJjdegreecode() != null
											&& !"".equals(arg0.getData().getJjdegreecode())) {
										tv_jjdegree.setItems(jjdegrees,
												Integer.parseInt(arg0.getData().getJjdegreecode()));
									}
									if (arg0.getData().getBcintroduce() != null
											&& !"".equals(arg0.getData().getBcintroduce())) {
										edt_bcintroduce.setText(arg0.getData().getBcintroduce());
									}

								} catch (Exception e) {
									Log.e("ws_pingtaixuanze", e.toString());
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case C.FOR_CAR_CODE:
			if (RESULT_OK == resultCode) {
				CarInfo carInfo = (CarInfo) data.getSerializableExtra("carinfo");
				tv_platenum.setText(carInfo.getPlatenum());
				edt_carlength.setChoiceText(carInfo.getCarlength());
				edt_carlength.closeOnClick();
				tv_cartype.setChoiceText(carInfo.getCartype());
				tv_cartype.setChoicePosition(carInfo.getCartypecode());
				tv_cartype.closeOnClick();
				for (int i = 0; i < chechang.size() - 1; i++) {
					if (carInfo.getCarlength().equals(chechang.get(i)))
						edt_carlength.setChoiceText(chechang.get(i).toString());
				}
				vanid = carInfo.get_id();
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
