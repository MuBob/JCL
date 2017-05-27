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
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.CheckSwitchButton;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;
import com.jcl.android.view.WhSpinnerOnItemChangedListener;
import com.jcl.android.view.WhSpinner.Item;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加车辆
 * @author pb
 * 
 */
public class AddCarActivity extends BaseActivity implements OnClickListener,OnImageComparessionListener,TextWatcher {
	private static final int IMG_XSLICENCE= 0x0001;
	private static final int IMG_YYLICENCE = 0x0002;
	private static final int IMG_BD= 0x0003;
	private static final int IMG_CARIMAGE1 = 0x0004;
	private static final int IMG_CARIMAGE2 = 0x0005;
	private static final int IMG_CARIMAGE3 = 0x0006;
	private static final int GET_LOCATION = 0x0007;
	private static final int IMG_ERROR = 12121;
	private ImageCompression imageCompression;
	private CityPickerPopupwindow cityPickerPopupwindow;
	
	private CheckSwitchButton csb_submit_location;//实时定位
	private CheckSwitchButton csb_load;
	
	private String load="2";
	
	private TextView tv_line1_from,tv_line1_to;//常跑线1
	private TextView tv_line2_from,tv_line2_to;//常跑线2
	private TextView tv_line3_from,tv_line3_to;//常跑线3
	private TextView tv_line4_from,tv_line4_to;//常跑线4
	
	private String linecode1;//长跑线编码 (370200,370100)
	private String linearea1;//长跑线(山东青岛,山东济南)
	
	private String linecode2;//长跑线编码 (370200,370100)
	private String linearea2;//长跑线(山东青岛,山东济南)
	
	private String linecode3;//长跑线编码 (370200,370100)
	private String linearea3;//长跑线(山东青岛,山东济南)
	
	private String linecode4;//长跑线编码 (370200,370100)
	private String linearea4;//长跑线(山东青岛,山东济南)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcar);
		afterCreate();
		initNavigation();
		initView();
		initWhSpinnerData();
		if(!TextUtils.isEmpty(carid))
		{
			getCarinfo();
		}
	}
	String carid="";
	private void afterCreate()
	{
		if(getIntent().hasExtra("carid"))
		{
			carid=getIntent().getStringExtra("carid");
		}
	}

	private MyUINavigationView uINavigationView;

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		if(TextUtils.isEmpty(carid))
		{
			uINavigationView.setStrTitle("添加车辆");
		}else
		{
			uINavigationView.setStrTitle("车辆编辑");
		}
		
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit();
			}
		});
	}
	
//	private EditText edt_company;//公司名称
	private EditText edt_platenum;//车牌号
	private com.jcl.android.view.WhSpinner tv_cartype;//车型
	private com.jcl.android.view.WhSpinner tv_wx;//外形
	private com.jcl.android.view.WhSpinner tv_juli;//距离
	private com.jcl.android.view.WhSpinner edt_carlength;//车长
	private EditText edt_approveweight;//载重
	private EditText edt_carsize_l;//内长
	private EditText edt_carsize_w;//内宽
	private EditText edt_carsize_h;//内高
	private TextView tv_volume;//体积
	private EditText edt_bcintroduce;//补充说明
	private CheckBox cb_show;
	private String isshow="true";
	private TextView tv_officeplace;//常驻地
	private ImageView img_xslicence;//行驶证
	private ImageView img_yylicence;//营运证
	private ImageView img_bd;//保单
	private ImageView img_carimage1;//车头照
	private ImageView img_carimage2;//45°照
	private ImageView img_carimage3;//车尾照
	private Button btn_submit;//提交
	
	private EditText edt_link;//联系人
	private EditText edt_phone;//电话
	private TextView tv_car_locatin;//手动定位
	
	
	private List<WhSpinner.Item> chexing;
	private List<WhSpinner.Item> waixing;
	private List<WhSpinner.Item> juli;
	private List<WhSpinner.Item> chechang;
	
	private String carlengthcode;
	
	private TextView tv_carlength_lab;//
	private TextView tv_mi;
	public void initWhSpinnerData() {
		chexing = new ArrayList<WhSpinner.Item>();
		String[] chexingArr=getResources().getStringArray(R.array.chexing);
		for (int i = 0; i < chexingArr.length; i++) {
			chexing.add(new Item(chexingArr[i],i));
		}
		tv_cartype.setItems(chexing);
		tv_cartype.setChoiceText("");

		
		waixing = new ArrayList<WhSpinner.Item>();
		String[] waixingArr=getResources().getStringArray(R.array.waixing);
		for (int i = 0; i < waixingArr.length; i++) {
			waixing.add(new Item(waixingArr[i],i));
		}
		tv_wx.setItems(waixing);
		tv_wx.setChoiceText("");

		juli= new ArrayList<WhSpinner.Item>();
		String[] juliArr=getResources().getStringArray(R.array.juli);
		for (int i = 0; i < juliArr.length; i++) {
			juli.add(new Item(juliArr[i],i));
		}
		tv_juli.setItems(juli);
		tv_juli.setChoiceText("");
		
		chechang= new ArrayList<WhSpinner.Item>();
		String[] chechangArr=getResources().getStringArray(R.array.chechang);
		for (int i = 0; i < chechangArr.length; i++) {
			if(!"不限".equals(chechangArr[i]))
			{
			chechang.add(new Item(chechangArr[i],i));
			}
		}
		edt_carlength.setItems(chechang);
		edt_carlength.setChoiceText("");
		
	}
	
	class CarinfoRequest{
		private String type;
		private CarinfoData filters;
		
		
		public CarinfoRequest(CarinfoData filters) {
			// TODO Auto-generated constructor stub
			this.type="100501";
			this.filters=filters;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public CarinfoData getFilters() {
			return filters;
		}
		public void setFilters(CarinfoData filters) {
			this.filters = filters;
		}
		
		
	}
	
	class CarinfoData{
		private String _id;

		public CarinfoData(String id) {
			this._id=id;
		}

		public String get_id() {
			return _id;
		}

		public void set_id(String _id) {
			this._id = _id;
		}
	}
	
	
	String carjsonrequest;
	private void getCarinfo()
	{
		CarinfoData data=new CarinfoData(carid);
		carjsonrequest=new Gson().toJson(new CarinfoRequest(data));
		
		executeRequest(new GsonRequest<CarinfoBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, CarinfoBean.class, null,
				new ParamsBuilder().getStrParams(carjsonrequest),
				new Listener<CarinfoBean>() {
					@Override
					public void onResponse(CarinfoBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(AddCarActivity.this, arg0.getMsg(),
										1000).show();
								setCarInfo(arg0.getData());
							}else{
								Toast.makeText(AddCarActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(AddCarActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(AddCarActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}
	
	
	private void setCarInfo(AddCarData carinfo)
	{
		edt_platenum.setText(carinfo.getPlatenum());
		try {
			tv_cartype.setItems(chexing, Integer.parseInt(carinfo.getCartypecode()));
			if(0==Integer.parseInt(carinfo.getCartypecode()))
			{
				tv_carlength_lab.setVisibility(View.GONE);
				edt_carlength.setVisibility(View.VISIBLE);
				tv_mi.setVisibility(View.VISIBLE);
				edt_carlength.setChoiceText("12米");
				edt_carlength.closeOnClick();
				edt_carlength.setVisibility(View.GONE);
				isjizhuangxiang=true;
			}else
			{
				tv_carlength_lab.setVisibility(View.VISIBLE);
				edt_carlength.setVisibility(View.VISIBLE);
				edt_carlength.openOnClick();
				tv_mi.setVisibility(View.VISIBLE);
				isjizhuangxiang=false;
			}
			
//			tv_wx.setItems(waixing, Integer.parseInt(carinfo.getWxcode()));
			tv_juli.setItems(juli, Integer.parseInt(carinfo.getDistancecode()));
			edt_carlength.setChoiceText(carinfo.getCarlength()+"米");
		} catch (Exception e) {
			// TODO: handle exception
		}
		edt_approveweight.setText(carinfo.getApproveweight());
		tv_car_locatin.setText(carinfo.getPosition());
		
		try {
			String []strs=carinfo.getCarsize().split(",");
			edt_carsize_l.setText(strs[0]);
			edt_carsize_w.setText(strs[1]);
			edt_carsize_h.setText(strs[2]);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		tv_officeplace.setText(carinfo.getOfficeplace());
		tv_line1_from.setText(carinfo.getOfficeplace());
		tv_line2_from.setText(carinfo.getOfficeplace());
		tv_line3_from.setText(carinfo.getOfficeplace());
		tv_line4_from.setText(carinfo.getOfficeplace());
		
		btn_submit=(Button)findViewById(R.id.btn_submit);
		ImageLoaderUtil
		.getInstance(AddCarActivity.this)
		.loadImage(C.BASE_URL+ carinfo.getXslicence(),
				img_xslicence);
		
		ImageLoaderUtil
		.getInstance(AddCarActivity.this)
		.loadImage(C.BASE_URL+carinfo.getYylicence(),
				img_yylicence);
		
		ImageLoaderUtil
		.getInstance(AddCarActivity.this)
		.loadImage(C.BASE_URL+ carinfo.getBd(),
				img_bd);
		ImageLoaderUtil
		.getInstance(AddCarActivity.this)
		.loadImage(C.BASE_URL+ carinfo.getCarimage1(),
				img_carimage1);
		ImageLoaderUtil
		.getInstance(AddCarActivity.this)
		.loadImage(C.BASE_URL+ carinfo.getCarimage2(),
				img_carimage2);
		ImageLoaderUtil
		.getInstance(AddCarActivity.this)
		.loadImage(C.BASE_URL+ carinfo.getCarimage3(),
				img_carimage3);
		if(carid.equals(SharePerfUtil.getCarid()))
		{
			csb_submit_location.setChecked(SharePerfUtil.getUpdataLoaction());
		}
		if("1".equals(carinfo.getLoad()))
		{
			csb_load.setChecked(true);
		}else{
			csb_load.setChecked(false);
		}

		linearea1=carinfo.getLinearea1();
		linecode1=carinfo.getLinecode1();
		
		linearea2=carinfo.getLinearea2();
		linecode2=carinfo.getLinecode2();
		
		linearea3=carinfo.getLinearea3();
		linecode3=carinfo.getLinecode3();
		
		linearea4=carinfo.getLinearea4();
		linecode4=carinfo.getLinecode4();
		
//		String line1[] = linearea1.split(",");
//		String code1[] = linecode1.split(","); 
//		
//		String line2[] = linearea2.split(",");
//		String code2[] = linecode2.split(","); 
//		
//		String line3[] = linearea3.split(",");
//		String code3[] = linecode3.split(","); 
//		
//		String line4[] = linearea4.split(",");
//		String code4[] = linecode4.split(",");  		
//		tv_line1_from.setText(line1[0]);
		
		tv_line1_to.setText(carinfo.getLinearea1());
		tv_line1_to.setTag(linecode1);
//		tv_line2_from.setText(line2[0]);
		tv_line2_to.setText(carinfo.getLinearea2());
		tv_line2_to.setTag(linecode2);
//		tv_line3_from.setText(line3[0]);
		tv_line3_to.setText(carinfo.getLinearea3());
		tv_line3_to.setTag(linecode3);
//		tv_line4_from.setText(line4[0]);
		tv_line4_to.setText(carinfo.getLinearea4());
		tv_line4_to.setTag(linecode4);
	}
	
	
	private void initView()
	{
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), handler, null);
	
		tv_line1_from=(TextView) findViewById(R.id.tv_line1_from);
		tv_line1_to=(TextView) findViewById(R.id.tv_line1_to);
		
		tv_line2_from=(TextView) findViewById(R.id.tv_line2_from);
		tv_line2_to=(TextView) findViewById(R.id.tv_line2_to);
		
		tv_line3_from=(TextView) findViewById(R.id.tv_line3_from);
		tv_line3_to=(TextView) findViewById(R.id.tv_line3_to);
		
		tv_line4_from=(TextView) findViewById(R.id.tv_line4_from);
		tv_line4_to=(TextView) findViewById(R.id.tv_line4_to);
		
		tv_line1_from.setTag("");
		tv_line1_to.setTag("");
		tv_line2_from.setTag("");
		tv_line2_to.setTag("");
		tv_line3_from.setTag("");
		tv_line3_to.setTag("");
		tv_line4_from.setTag("");
		tv_line4_to.setTag("");
		
		
//		tv_line1_from.setOnClickListener(this);
		tv_line1_to.setOnClickListener(this);
//		tv_line2_from.setOnClickListener(this);
		tv_line2_to.setOnClickListener(this);
//		tv_line3_from.setOnClickListener(this);
		tv_line3_to.setOnClickListener(this);
//		tv_line4_from.setOnClickListener(this);
		tv_line4_to.setOnClickListener(this);
		
		imageCompression = ImageCompression.getInstance(this);
		edt_platenum=(EditText)findViewById(R.id.edt_platenum);
		tv_cartype=(com.jcl.android.view.WhSpinner)findViewById(R.id.tv_cartype);
		
		tv_cartype.setWhSpinnerOnItemChangedListener(new WhSpinnerOnItemChangedListener() {
			
			@Override
			public void onItemChanged(WhSpinner whSpinner, int item) {
				// TODO Auto-generated method stub
				if(0==item)
				{
					tv_carlength_lab.setVisibility(View.GONE);
					edt_carlength.setVisibility(View.GONE);
					edt_carlength.setChoiceText(12+"米");
					edt_carlength.setChoicePosition("10");
					tv_mi.setVisibility(View.GONE);
					isjizhuangxiang=true;
				}else
				{
					tv_carlength_lab.setVisibility(View.VISIBLE);
					edt_carlength.setVisibility(View.VISIBLE);
					edt_carlength.openOnClick();
					tv_mi.setVisibility(View.VISIBLE);
					isjizhuangxiang=false;
				}
			}
		});
		tv_wx=(com.jcl.android.view.WhSpinner)findViewById(R.id.tv_wx);
		tv_juli=(com.jcl.android.view.WhSpinner)findViewById(R.id.tv_juli);
		edt_bcintroduce=(EditText) findViewById(R.id.edt_bcintroduce);
		cb_show=(CheckBox) findViewById(R.id.cb_show);
		tv_carlength_lab=(TextView) findViewById(R.id.tv_carlength_lab);
		tv_mi=(TextView) findViewById(R.id.tv_mi);
		tv_car_locatin=(TextView) findViewById(R.id.tv_car_locatin);
		tv_car_locatin.setOnClickListener(this);
		
		cb_show.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					isshow="true";
				}else{
					isshow="false";
				}
				
			}
		});
		
		edt_approveweight=(EditText)findViewById(R.id.edt_approveweight);
		edt_approveweight.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String temp = s.toString();
		        int posDot = temp.indexOf(".");
		        if (posDot <= 0) return;
		        if (temp.length() - posDot - 1 > 2)
		        {
		            s.delete(posDot + 3, posDot + 4);
		        }
			}
		});
		edt_carlength=(com.jcl.android.view.WhSpinner)findViewById(R.id.edt_carlength);
		
		edt_carsize_l=(EditText)findViewById(R.id.edt_carsize_l);
		edt_carsize_l.addTextChangedListener(this);
		edt_carsize_w=(EditText)findViewById(R.id.edt_carsize_w);
		edt_carsize_w.addTextChangedListener(this);
		edt_carsize_h=(EditText)findViewById(R.id.edt_carsize_h);
		edt_carsize_h.addTextChangedListener(this);
		tv_volume=(TextView)findViewById(R.id.tv_volume);
		tv_officeplace=(TextView)findViewById(R.id.tv_officeplace);
		tv_officeplace.setOnClickListener(this);
		img_xslicence=(ImageView)findViewById(R.id.img_xslicence);
		img_xslicence.setOnClickListener(this);
		img_yylicence=(ImageView)findViewById(R.id.img_yylicence);
		img_yylicence.setOnClickListener(this);
		img_bd=(ImageView)findViewById(R.id.img_bd);
		img_bd.setOnClickListener(this);
		img_carimage1=(ImageView)findViewById(R.id.img_carimage1);
		img_carimage1.setOnClickListener(this);
		img_carimage2=(ImageView)findViewById(R.id.img_carimage2);
		img_carimage2.setOnClickListener(this);
		img_carimage3=(ImageView)findViewById(R.id.img_carimage3);
		img_carimage3.setOnClickListener(this);
		btn_submit=(Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		
		edt_link=(EditText)findViewById(R.id.edt_link);
		edt_phone=(EditText)findViewById(R.id.edt_phone);
		edt_link.setText(SharePerfUtil.getLinkMan());
		edt_phone.setText(SharePerfUtil.getLoginName());
		csb_load=(CheckSwitchButton) findViewById(R.id.csb_load);
		csb_load.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					load="1";
				}else{
					load="0";
				}
				if(TextUtils.isEmpty(carid))
				{
					return;
				}
				submit();
				
			}
		});
		csb_submit_location=(CheckSwitchButton) findViewById(R.id.csb_submit_location);
		
		csb_submit_location.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					if("".equals(carid))
					{
						return;
					}
					Toast.makeText(AddCarActivity.this, "已开启", 1000).show();
					SharePerfUtil.updataLoaction(isChecked);
					SharePerfUtil.saveCarid(carid);
					JCLApplication.getInstance().checkLocationSetting();
				}else{
					if("".equals(carid))
					{
						return;
					}
					Toast.makeText(AddCarActivity.this, "已关闭", 1000).show();
					SharePerfUtil.saveCarid(carid);
					SharePerfUtil.updataLoaction(isChecked);
					
					JCLApplication.getInstance().checkLocationSetting();
				}
			}
		});
		
	}
	
	String officeplaceCode;//常驻地编码
	
	public void setListener()
	{
		
	}

	class AddCarRequest {
		private String type;
		private String data;
		private String operate;
		private String key;

		public AddCarRequest(String data) {
			this.type = "1004";
			this.data = data;
		}
		public AddCarRequest(String data,String key,String operate) {
			this.type = "1004";
			this.data = data;
			this.key=key;
			this.operate=operate;
		}
	}
	
	class CarinfoBean extends BaseBean{
		private AddCarData data;

		public AddCarData getData() {
			return data;
		}

		public void setData(AddCarData data) {
			this.data = data;
		}
		
	}

	class AddCarData{
		private String userid;
		private String platenum;
		private String cartype;
		private String cartypecode;
		private String wx;
		private String wxcode;
		private String approveweight;
		private String carsize;
		private String carlength;
		private String officeplace;
		private String officeplacecode;

		private String xslicence;
		private String yylicence;
		private String bd;
		private String carimage1;//车头
		private String carimage2;//45°
		private String carimage3;//车尾

		private String status;
		private String createtime;
		private String effectivetime;
		private String effectiveflag;
		private String distance;
		private String distancecode;
		private String longitude;
		private String latitude;
		private String link;
		private String phone;
		private String isshow;//是否显示备注信息
		private String position;//当前位置
		private String load;//装载状态    开始装载为1
		
		private String linecode1;//长跑线编码 (370200,370100)
		private String linearea1;//长跑线(山东青岛,山东济南)
		
		private String linecode2;//长跑线编码 (370200,370100)
		private String linearea2;//长跑线(山东青岛,山东济南)
		
		private String linecode3;//长跑线编码 (370200,370100)
		private String linearea3;//长跑线(山东青岛,山东济南)
		
		private String linecode4;//长跑线编码 (370200,370100)
		private String linearea4;//长跑线(山东青岛,山东济南)
		
		
		public AddCarData(String userid,  String platenum,
				String cartype,String cartypecode,  String wx,String wxcode, 
				String approveweight, String carlength,
				String carsize,String officeplace,String officeplacecode,
				String carimage1, String carimage2,String carimage3,
				String xslicence, String yylicence, String bd,String distance,String distancecode,
				String longitude,String latitude,String link,String phone,String isshow,
				String position,String load
				,String linecode1,String linearea1
				,String linecode2,String linearea2
				,String linecode3,String linearea3
				,String linecode4,String linearea4) {
			this.userid = userid;
			this.platenum = platenum;
			this.cartype = cartype;
			this.wx = wx;
			this.approveweight = approveweight;
			this.carsize = carsize;
			this.carimage1 = carimage1;
			this.carimage2 = carimage2;
			this.carimage3 = carimage3;
			this.carlength = carlength;
			this.officeplace = officeplace;
			this.officeplacecode=officeplacecode;
			this.xslicence = xslicence;
			this.yylicence = yylicence;
			this.bd = bd;
			this.distance=distance;
			this.distancecode=distancecode;
			this.wxcode=wxcode;
			this.cartypecode=cartypecode;
			this.longitude=longitude;
			this.latitude=latitude;
			this.link = link;
			this.phone = phone;
			this.isshow=isshow;
			this.position=position;
			this.load=load;
			
			this.linecode1=linecode1;
			this.linearea1=linearea1;
			this.linecode2=linecode2;
			this.linearea2=linearea2;
			this.linecode3=linecode3;
			this.linearea3=linearea3;
			this.linecode4=linecode4;
			this.linearea4=linearea4;
		}
		public String getUserid() {
			return userid;
		}


		public void setUserid(String userid) {
			this.userid = userid;
		}



		public String getLinecode1() {
			return linecode1;
		}
		public void setLinecode1(String linecode1) {
			this.linecode1 = linecode1;
		}
		public String getLinearea1() {
			return linearea1;
		}
		public void setLinearea1(String linearea1) {
			this.linearea1 = linearea1;
		}
		public String getLinecode2() {
			return linecode2;
		}
		public void setLinecode2(String linecode2) {
			this.linecode2 = linecode2;
		}
		public String getLinearea2() {
			return linearea2;
		}
		public void setLinearea2(String linearea2) {
			this.linearea2 = linearea2;
		}
		public String getLinecode3() {
			return linecode3;
		}
		public void setLinecode3(String linecode3) {
			this.linecode3 = linecode3;
		}
		public String getLinearea3() {
			return linearea3;
		}
		public void setLinearea3(String linearea3) {
			this.linearea3 = linearea3;
		}
		public String getLinecode4() {
			return linecode4;
		}
		public void setLinecode4(String linecode4) {
			this.linecode4 = linecode4;
		}
		public String getLinearea4() {
			return linearea4;
		}
		public void setLinearea4(String linearea4) {
			this.linearea4 = linearea4;
		}


		public String getPlatenum() {
			return platenum;
		}


		public void setPlatenum(String platenum) {
			this.platenum = platenum;
		}


		public String getCartype() {
			return cartype;
		}


		public void setCartype(String cartype) {
			this.cartype = cartype;
		}


		public String getWx() {
			return wx;
		}


		public void setWx(String wx) {
			this.wx = wx;
		}


		public String getApproveweight() {
			return approveweight;
		}


		public void setApproveweight(String approveweight) {
			this.approveweight = approveweight;
		}


		public String getCarsize() {
			return carsize;
		}


		public void setCarsize(String carsize) {
			this.carsize = carsize;
		}


		public String getCartypecode() {
			return cartypecode;
		}


		public void setCartypecode(String cartypecode) {
			this.cartypecode = cartypecode;
		}


		public String getWxcode() {
			return wxcode;
		}


		public void setWxcode(String wxcode) {
			this.wxcode = wxcode;
		}


		public String getDistancecode() {
			return distancecode;
		}


		public void setDistancecode(String distancecode) {
			this.distancecode = distancecode;
		}


		public String getCarlength() {
			return carlength;
		}


		public void setCarlength(String carlength) {
			this.carlength = carlength;
		}


		public String getOfficeplace() {
			return officeplace;
		}


		public void setOfficeplace(String officeplace) {
			this.officeplace = officeplace;
		}


		public String getXslicence() {
			return xslicence;
		}


		public void setXslicence(String xslicence) {
			this.xslicence = xslicence;
		}


		public String getYylicence() {
			return yylicence;
		}


		public void setYylicence(String yylicence) {
			this.yylicence = yylicence;
		}


		public String getBd() {
			return bd;
		}


		public void setBd(String bd) {
			this.bd = bd;
		}


		public String getCarimage1() {
			return carimage1;
		}


		public void setCarimage1(String carimage1) {
			this.carimage1 = carimage1;
		}


		public String getCarimage2() {
			return carimage2;
		}


		public void setCarimage2(String carimage2) {
			this.carimage2 = carimage2;
		}


		public String getCarimage3() {
			return carimage3;
		}


		public void setCarimage3(String carimage3) {
			this.carimage3 = carimage3;
		}


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
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


		public String getDistance() {
			return distance;
		}


		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getOfficeplacecode() {
			return officeplacecode;
		}


		public void setOfficeplacecode(String officeplacecode) {
			this.officeplacecode = officeplacecode;
		}
		
		
		
		public String getLink() {
			return link;
		}


		public void setLink(String link) {
			this.link = link;
		}


		public String getPhone() {
			return phone;
		}


		public void setPhone(String phone) {
			this.phone = phone;
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


		public String getIsshow() {
			return isshow;
		}


		public void setIsshow(String isshow) {
			this.isshow = isshow;
		}
		

		public String getPosition() {
			return position;
		}


		public void setPosition(String position) {
			this.position = position;
		}
		public String getLoad() {
			return load;
		}


		public void setLoad(String load) {
			this.load = load;
		}

		
		


		
		
	}
	
	String data;
	String jsonRequest;
	
	private String xslicenceBitmap, yylicenceBitmap, bdBitmap;
	private String carimage1Bitmap = "", carimage2Bitmap = "", carimage3Bitmap="";
	
	String carSize = "";
	
	Intent intent;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			submit();
			break;
		case R.id.img_xslicence:
			intent = new Intent(AddCarActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_XSLICENCE);
			break;
		case R.id.img_yylicence:
			intent = new Intent(AddCarActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_YYLICENCE);
			break;
		case R.id.img_bd:
			intent = new Intent(AddCarActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_BD);
			break;
		case R.id.img_carimage1:
			intent = new Intent(AddCarActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_CARIMAGE1);
			break;
		case R.id.img_carimage2:
			intent = new Intent(AddCarActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_CARIMAGE2);
			break;
		case R.id.img_carimage3:
			intent = new Intent(AddCarActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_CARIMAGE3);
			break;
		case R.id.tv_officeplace:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0010,"submit");
			break;
		case R.id.tv_line1_from:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0011,"submit");
			break;
		case R.id.tv_line1_to:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0012,"submit");
			break;
		case R.id.tv_line2_from:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0013,"submit");
			break;
		case R.id.tv_line2_to:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0014,"submit");
			break;
		case R.id.tv_line3_from:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0015,"submit");
			break;
		case R.id.tv_line3_to:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0016,"submit");
			break;
		case R.id.tv_line4_from:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0017,"submit");
			break;
		case R.id.tv_line4_to:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(0x0018,"submit");
			break;
		case R.id.tv_car_locatin:
			startActivityForResult(new Intent(AddCarActivity.this,
					GetLatLotActivity.class)
					.putExtra("lat", JCLApplication.getInstance().getMyLocation().getLatitude()+"").
					putExtra("lnt", JCLApplication.getInstance().getMyLocation().getLongitude()+"")
					, GET_LOCATION);
			break;
		default:
			break;
		}

	}
	private boolean isjizhuangxiang=false;
	String volume="0.00";
	float l;
	float w;
	float h;
	public void submit()
	{
		try {
			l=Integer.parseInt(edt_carsize_l.getText().toString());
			w=Integer.parseInt(edt_carsize_w.getText().toString());
			h=Integer.parseInt(edt_carsize_h.getText().toString());
			volume =""+(l*w*h);
		} catch (Exception e) {
			// TODO: handle exception
		}
		carSize=edt_carsize_l.getText().toString()+","+edt_carsize_w.getText().toString()+","+edt_carsize_h.getText().toString();
		
		tv_volume.setText(volume);
		
		if(TextUtils.isEmpty(edt_platenum.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请填写车牌号",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(tv_cartype.getChoiceText()))
		{
			Toast.makeText(AddCarActivity.this,"请填写车型",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(edt_approveweight.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请填写载重量",
					1000).show();
			return;
		}
//		if(TextUtils.isEmpty(edt_carlength.getChoiceText()))
//		{
//			Toast.makeText(AddCarActivity.this,"请填写车长",
//					1000).show();
//			return;
//		}
		if(isjizhuangxiang){
			edt_carlength.setChoiceText(12+"米");
		}else{
			if(TextUtils.isEmpty(edt_carlength.getChoiceText()))
			{
				Toast.makeText(AddCarActivity.this,"请填写车长",
						1000).show();
				return;
			}
		}
		
		if(TextUtils.isEmpty(edt_carsize_l.getText().toString())||
				TextUtils.isEmpty(edt_carsize_w.getText().toString())||
				TextUtils.isEmpty(edt_carsize_h.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请填写车辆尺寸",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(tv_officeplace.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请填写常驻地",
					1000).show();
			return;
		}

		if(TextUtils.isEmpty(edt_phone.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请填写联系电话",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(tv_line1_to.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请选择常跑线",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(tv_line2_to.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请选择常跑线",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(tv_line3_to.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请选择常跑线",
					1000).show();
			return;
		}
		if(TextUtils.isEmpty(tv_line4_to.getText().toString()))
		{
			Toast.makeText(AddCarActivity.this,"请选择常跑线",
					1000).show();
			return;
		}
		String position;
		if(TextUtils.isEmpty(tv_car_locatin.getText().toString())){
			position=JCLApplication.getInstance().getMyLocation2Str();
		}else{
			position=tv_car_locatin.getText().toString();
		}
		
//		if(xslicenceBitmap==null)
//		{
//			Toast.makeText(AddCarActivity.this,"请填写行驶证正本",
//					1000).show();
//			return;
//		}
//		if(yylicenceBitmap==null)
//		{
//			Toast.makeText(AddCarActivity.this,"请填写运营证",
//					1000).show();
//			return;
//		}
//		if(bdBitmap==null)
//		{
//			Toast.makeText(AddCarActivity.this,"请填写保单",
//					1000).show();
//			return;
//		}
//		if(carimage1Bitmap=="")
//		{
//			Toast.makeText(AddCarActivity.this,"请填车头照",
//					1000).show();
//			return;
//		}
//		if(carimage2Bitmap=="")
//		{
//			Toast.makeText(AddCarActivity.this,"请填45°照",
//					1000).show();
//			return;
//		}
//		if(carimage3Bitmap=="")
//		{
//			Toast.makeText(AddCarActivity.this,"请填车尾照",
//					1000).show();
//			return;
//		}
//		linecode1=tv_line1_from.getTag().toString()+","+tv_line1_to.getTag().toString();
//		linearea1=tv_line1_from.getText().toString()+","+tv_line1_to.getText().toString();
//		linecode2=tv_line2_from.getTag().toString()+","+tv_line2_to.getTag().toString();
//		linearea2=tv_line2_from.getText().toString()+","+tv_line2_to.getText().toString();
//		linecode3=tv_line3_from.getTag().toString()+","+tv_line3_to.getTag().toString();
//		linearea3=tv_line3_from.getText().toString()+","+tv_line3_to.getText().toString();
//		linecode4=tv_line4_from.getTag().toString()+","+tv_line4_to.getTag().toString();
//		linearea4=tv_line4_from.getText().toString()+","+tv_line4_to.getText().toString();
		
		linecode1=tv_line1_to.getTag().toString();
		linearea1=tv_line1_to.getText().toString();
		linecode2=tv_line2_to.getTag().toString();
		linearea2=tv_line2_to.getText().toString();
		linecode3=tv_line3_to.getTag().toString();
		linearea3=tv_line3_to.getText().toString();
		linecode4=tv_line4_to.getTag().toString();
		linearea4=tv_line4_to.getText().toString();
		
		data = new Gson().toJson(new AddCarData(SharePerfUtil.getUserId(),
				edt_platenum.getText().toString(),tv_cartype.getChoiceText(),tv_cartype.getChoiceValue().toString(),tv_wx.getChoiceText(),
				tv_wx.getChoiceValue().toString(),edt_approveweight.getText().toString(),
				edt_carlength.getChoiceText().substring(0,edt_carlength.getChoiceText().length()-1),
				carSize,tv_officeplace.getText().toString(),officeplaceCode,
				carimage1Bitmap,carimage2Bitmap,carimage3Bitmap,xslicenceBitmap,yylicenceBitmap,bdBitmap,tv_juli.getChoiceText(),tv_juli.getChoiceValue().toString()
				,lnt,lat,edt_link.getText().toString(),edt_phone.getText().toString(),isshow,tv_car_locatin.getText().toString(),load
				,linecode1,linearea1
				,linecode2,linearea2
				,linecode3,linearea3
				,linecode4,linearea4));
		if(TextUtils.isEmpty(carid))
		{
			jsonRequest = new Gson().toJson(new AddCarRequest(data));
		}else{
			jsonRequest = new Gson().toJson(new AddCarRequest(data,carid,"M"));
		}
		
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest), new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								
								
								MyToast.showToast(AddCarActivity.this, "提交成功");

								setResult(RESULT_OK);
								finish();
								
				
								
							}else{
								Toast.makeText(AddCarActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(AddCarActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(AddCarActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}
	
	
	
	
	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			Intent data) {
		if(RESULT_OK==resultCode)
		{
			switch (requestCode) {
			case GET_LOCATION:
				
				tv_car_locatin.setText(data.getStringExtra("addr"));
				lnt=data.getStringExtra("Lng");
				lat=data.getStringExtra("Lat");
				if(carid!=null&!"".equals(carid))
				{
					return;
				}
				JCLApplication.getInstance().updataCarLatLnt(carid,
						lnt,
						lat, 
						tv_car_locatin.getText().toString());
				break;
			default:
				break;
			}
		}
		
		if (C.SELECT_PIC_RESULT == resultCode) {
//			showLD("正在处理，请稍候...");
			final String imagePath = data.getStringExtra("imagePath");
			new Thread(new Runnable() {

				@Override
				public void run() {
					switch (requestCode) {
					case IMG_XSLICENCE:
						xslicenceBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "xslicenceBitmap = " + xslicenceBitmap);
						break;
					case IMG_YYLICENCE:
						yylicenceBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "yylicenceBitmap = " + yylicenceBitmap);
						break;
					case IMG_BD:
						bdBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "bdBitmap = "
								+ bdBitmap);
						break;
					case IMG_CARIMAGE1:
						carimage1Bitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "carimage1Bitmap = " + carimage1Bitmap);
						break;
					case IMG_CARIMAGE2:
						carimage2Bitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "carimage2Bitmap = " + carimage2Bitmap);
						break;
					case IMG_CARIMAGE3:
						carimage3Bitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "V = "
								+ carimage3Bitmap);
						break;
					default:
						break;
					}
				}
			}).start();

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	String srcPath;
	Bitmap smallBitmap;
	String lnt,lat;
	Handler handler = new Handler() {

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
			int requestCode = msg.what;
			switch (requestCode) {
			case 0x0010:
			case 0x0011:
			case 0x0012:
			case 0x0013: 
			case 0x0014:
			case 0x0015:
			case 0x0016:
			case 0x0017:
			case 0x0018:
				break;
			default:
				srcPath = (String) msg.obj;
				smallBitmap = FileUtils.getSmallBitmap(
						AddCarActivity.this, srcPath);
				break;
			}
//			if(requestCode!=0x0010)
//			{
//			srcPath = (String) msg.obj;
//			smallBitmap = FileUtils.getSmallBitmap(
//					AddCarActivity.this, srcPath);
//			}
			switch (requestCode) {
			case IMG_XSLICENCE:
				img_xslicence.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_YYLICENCE:
				img_yylicence.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_BD:
				img_bd.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_CARIMAGE1:
				img_carimage1.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_CARIMAGE2:
				img_carimage2.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_CARIMAGE3:
				img_carimage3.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_ERROR:
				cancelLD();
				MyToast.showToast(AddCarActivity.this, "图片加载失败");
				break;
			case 0x0010:
				officeplaceCode = bundle.getString("cityCode");
				tv_officeplace.setText(info);
				tv_line1_from.setText(info);
				tv_line2_from.setText(info);
				tv_line3_from.setText(info);
				tv_line4_from.setText(info);
				tv_line1_from.setTag(bundle.getString("cityCode"));
				tv_line2_from.setTag(bundle.getString("cityCode"));
				tv_line3_from.setTag(bundle.getString("cityCode"));
				tv_line4_from.setTag(bundle.getString("cityCode"));
				lnt = bundle.getString("DistrictLnt");
				lat = bundle.getString("DistrictLat");
				break;
			case 0x0011:
				tv_line1_from.setText(info);
				tv_line1_from.setTag(bundle.getString("cityCode"));
				break;
			case 0x0012:
				tv_line1_to.setText(info);
				tv_line1_to.setTag(bundle.getString("cityCode"));
				break;
			case 0x0013:
				tv_line2_from.setText(info);
				tv_line2_from.setTag(bundle.getString("cityCode"));
				break;
			case 0x0014:
				tv_line2_to.setText(info);
				tv_line2_to.setTag(bundle.getString("cityCode"));
				break;
			case 0x0015:
				tv_line3_from.setText(info);
				tv_line3_from.setTag(bundle.getString("cityCode"));
				break;
			case 0x0016:
				tv_line3_to.setText(info);
				tv_line3_to.setTag(bundle.getString("cityCode"));
				break;
			case 0x0017:
				tv_line4_from.setText(info);
				tv_line4_from.setTag(bundle.getString("cityCode"));
				break;
			case 0x0018:
				tv_line4_to.setText(info);
				tv_line4_to.setTag(bundle.getString("cityCode"));
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
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(IMG_ERROR);
	}

	
	
	//文本编辑 监听

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		
	}

	float length;
	float weigth;
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		System.out.println("CharSequence="+s+",start="+start+",before="+before+",count="+count);
		
		String strl=edt_carsize_l.getText().toString();
		String strw=edt_carsize_w.getText().toString();
		String strh=edt_carsize_h.getText().toString();
		
		
		if(TextUtils.isEmpty(edt_carsize_l.getText().toString()))
		{
			
			l=0f;
		}else{
			if(TextUtils.equals(strl.substring(strl.length()-1,strl.length()),"."))
			{
				return;
			}
			l=Float.parseFloat(edt_carsize_l.getText().toString());
		}
		
		if(TextUtils.isEmpty(edt_carsize_w.getText().toString()))
		{
			w=0f;
		}else{
			if(TextUtils.equals(strw.substring(strw.length()-1,strw.length()),"."))
			{
				return;
			}
			w=Float.parseFloat(edt_carsize_w.getText().toString());
		}
		
		if(TextUtils.isEmpty(edt_carsize_h.getText().toString()))
		{
			h=0f;
		}else{
			if(TextUtils.equals(strh.substring(strh.length()-1,strh.length()),"."))
			{
				return;
			}
			h=Float.parseFloat(edt_carsize_h.getText().toString());
		}
//		volume=""+(l*w*h);
		volume=""+new java.text.DecimalFormat("#.00").format(l*w*h);
		if(TextUtils.equals(volume, ".00"))
		{
			tv_volume.setText("0.00");
		}else{
			tv_volume.setText(volume);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

}
