package com.jcl.android.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jcl.android.bean.FindDedicateCarsListBean;
import com.jcl.android.bean.FindDedicateCarsListBean.Line;
import com.jcl.android.bean.WangDianBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.popupwindow.DatePickerPopupwindow;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;
import com.jcl.android.view.WhSpinnerOnCheckedListener;

/**
 * 发布专线
 * 
 * @author lxl
 * 
 */
public class PublicZhuanxianActivity extends BaseActivity implements
		OnClickListener,OnImageComparessionListener {

	private CityPickerPopupwindow cityPickerPopupwindow;
	private DatePickerPopupwindow datePickerPopupwindow;

	private WhSpinner ws_linetype;
	private List<WhSpinner.Item> linetype;

	private Button btn_public,btn_chufaadd,btn_mudiadd;
	private EditText et_zhonghuo, et_name, et_qinghuo, et_zuidiyipiao,
			 et_shixiao,et_remark;
	private TextView tv_chufadi, tv_mudidi,tv_chufawangdian,tv_mudiwangdian,tv_endtime;
	private LinearLayout llstopname;
	private MyUINavigationView uINavigationView;
	private ImageView upload_img;
	private EditText et_miaoshu,et_tejia;
	private String zx_image,chufawangdian,mudiwangdain;
	private ImageCompression imageCompression;
	private FindDedicateCarsListBean.Line lineinfo;
	private Bundle mybundle;
	private CheckBox cb_zijian;
	private String one_self;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_public_zhuanxian);
		initView();
		initWhSpinnerData();
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		datePickerPopupwindow = new DatePickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
		
		initContant();
	}

	private void initContant() {
		Intent intent = getIntent();
		mybundle = intent.getExtras();
		if (mybundle != null) {
		lineinfo = (Line) getIntent()
				.getSerializableExtra("info");
		et_zhonghuo.setText(lineinfo.getW_price());
		et_qinghuo.setText(lineinfo.getL_price());
		et_zuidiyipiao.setText(lineinfo.getM_price());
		et_shixiao.setText(lineinfo.getZx_sx());
		et_miaoshu.setText(lineinfo.getXl_description());
		et_tejia.setText(lineinfo.getSale());
		et_remark.setText(lineinfo.getRemark());
		ws_linetype.setItems(linetype, Integer.valueOf(lineinfo.getLinetype()));
		
		tv_chufadi.setText(lineinfo.getStartarea());
		tv_chufawangdian.setText(lineinfo.getWd_address_cf());
		tv_endtime.setText(lineinfo.getEnd_date());
		tv_mudidi.setText(lineinfo.getEndarea());
		tv_mudiwangdian.setText(lineinfo.getWd_address_md());
		ImageLoaderUtil.getInstance(PublicZhuanxianActivity.this).
		loadImage(C.BASE_URL+lineinfo.getZx_image(),upload_img);
		}
	}

	public void initWhSpinnerData() {
		linetype = new ArrayList<WhSpinner.Item>();
		linetype.add(new WhSpinner.Item("直达线路", "0"));
		linetype.add(new WhSpinner.Item("中转线路", "1"));
		ws_linetype.setItems(linetype, 0);
	}

	public void initView() {
		imageCompression = ImageCompression.getInstance(this);
		et_zhonghuo = (EditText) findViewById(R.id.et_zhonghuo);
		et_qinghuo = (EditText) findViewById(R.id.et_qinghuo);
		et_zuidiyipiao = (EditText) findViewById(R.id.et_zuidiyipiao);
		et_shixiao = (EditText) findViewById(R.id.et_shixiao);
		et_remark = (EditText) findViewById(R.id.et_remark);
		btn_public = (Button) findViewById(R.id.btn_public);
		tv_chufadi = (TextView) findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) findViewById(R.id.tv_mudidi);
		ws_linetype = (WhSpinner) findViewById(R.id.ws_linetype);
		tv_chufawangdian = (TextView) findViewById(R.id.tv_chufawangdian);
		tv_mudiwangdian = (TextView) findViewById(R.id.tv_mudiwangdian);
		upload_img = (ImageView) findViewById(R.id.upload_img);
		et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
		et_tejia = (EditText) findViewById(R.id.et_tejia);
		tv_endtime = (TextView) findViewById(R.id.tv_endtime);
		btn_chufaadd = (Button) findViewById(R.id.btn_chufaadd);
		btn_mudiadd = (Button) findViewById(R.id.btn_mudiadd);
		cb_zijian = (CheckBox) findViewById(R.id.cb_zijian);
//		ws_linetype.setWhSpinnerOnCheckedListener(new WhSpinnerOnCheckedListener() {
//			@Override
//			public void onChecked(WhSpinner whSpinner, int item) {
//				String position = whSpinner.getChoicePosition()+"";
//				if(position.equals("0")){
//					llstopname.setVisibility(View.GONE);
//				}else{
//					llstopname.setVisibility(View.VISIBLE);
//				}
//			}
//		});
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv_chufadi.setOnClickListener(this);
		tv_mudidi.setOnClickListener(this);
		btn_public.setOnClickListener(this);
		tv_chufawangdian.setOnClickListener(this);
		tv_mudiwangdian.setOnClickListener(this);
		upload_img.setOnClickListener(this);
		tv_endtime.setOnClickListener(this);
		btn_chufaadd.setOnClickListener(this);
		btn_mudiadd.setOnClickListener(this);
		one_self = "1";
		cb_zijian.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					one_self = "1";
				} else {
					one_self = "0";
				}
			}
		});
	}

	class PublicZhuanxianRequest {
		private String operate;
		private String type;
		private String data;
		private String key;

		public PublicZhuanxianRequest(String operate,String data,String key) {
			this.operate = operate;
			this.type = "10071";
			this.data = data;
			this.key = key;
		}
	}

	class Data {
		private String name;// 姓名
		private String l_price;// 轻货
		private String m_price;// 最低一票
		private String phone;// 手机号
		private String userid;// 用户编码
		private String startarea;// 出发地
		private String startcode;//
		private String endarea;//
		private String endcode;//
		private String stopnames;// 经停站 
		private String linetype;// 类型
		private String zx_sx;//价格
		private String remark;//说明
		private String xl_description;//描述
		private String sale;//特价
		private String zx_image;//图片
		private String end_date;//截止时间
		private String wd_address_cf;//出发网点
		private String wd_address_md;//目的网点
		private String w_price;//重货
		private String one_self;
		private String createtime;//发布时间

		public Data(String startarea, String startcode, String endarea,
				String endcode, String linetype,
				String w_price, String l_price, String m_price, 
				String userid,String zx_sx,String remark,String xl_description,
				String sale,String zx_image,String end_date,String wd_address_cf,
				String wd_address_md,String one_self,String createtime) {
			this.createtime = createtime;
			this.one_self = one_self;
			this.startarea = startarea;
			this.startcode = startcode;
			this.endarea = endarea;
			this.endcode = endcode;
			this.linetype = linetype;
			this.w_price = w_price;
			this.l_price = l_price;
			this.m_price = m_price;
			this.userid = userid;
			this.zx_sx = zx_sx;
			this.remark = remark;
			this.xl_description = xl_description;
			this.sale = sale;
			this.zx_image = zx_image;
			this.end_date = end_date;
			this.wd_address_cf = wd_address_cf;
			this.wd_address_md = wd_address_md;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_chufaadd:
			startActivity(new Intent(PublicZhuanxianActivity.this,AddContactsActivity.class));
			break;
			
        case R.id.btn_mudiadd:
        	startActivity(new Intent(PublicZhuanxianActivity.this,AddContactsActivity.class));
			break;
		
		case R.id.tv_endtime:
			if (datePickerPopupwindow != null)
				datePickerPopupwindow.show(3);
			break;
		
		case R.id.upload_img:
			Intent intent = new Intent(PublicZhuanxianActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, 111);
			break;
			
		case R.id.tv_chufadi:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"submit");
			break;

		case R.id.tv_mudidi:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(2,"submit");
			break;
		case R.id.btn_public:
			// 发布
			// 判断
//			if (TextUtils.isEmpty(et_company_name.getText().toString())) {
//				MyToast.showToast(this, "请填写公司名称");
//				return;
//			}
//			if (TextUtils.isEmpty(et_car_num.getText().toString())) {
//				MyToast.showToast(this, "请填写车牌号");
//				return;
//			}
//			if (TextUtils.isEmpty(et_lianxiren.getText().toString())) {
//				MyToast.showToast(this, "请填写联系人");
//				return;
//			}
//			if (TextUtils.isEmpty(et_tel.getText().toString())) {
//				MyToast.showToast(this, "请填写联系电话");
//				return;
//			}
//			if (TextUtils.isEmpty(et_price.getText().toString())) {
//				MyToast.showToast(this, "请填写价格");
//				return;
//			}
			if (TextUtils.isEmpty(tv_chufadi.getText().toString())) {
				MyToast.showToast(this, "请选择出发地");
				return;
			}
			if (TextUtils.isEmpty(tv_mudidi.getText().toString())) {
				MyToast.showToast(this, "请选择目的地");
				return;
			}
			String endtime;
			if (TextUtils.isEmpty(tv_endtime.getText().toString())) {
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        endtime = df.format(c.getTime());
			}else{
				endtime = tv_endtime.getText().toString();
			}
			
			String line = ws_linetype.getChoicePosition() + "";
			
			String chufadi = "";
			String mudidi = "";
			chufadi = tv_chufadi.getText().toString();
			mudidi = tv_mudidi.getText().toString();
			String linetype = ws_linetype.getChoicePosition() + "";
			Calendar cc = Calendar.getInstance();
			SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			Data data = new Data(chufadi, chufadiCode, mudidi, mudidiCode,
					linetype, et_zhonghuo.getText().toString(),
					et_qinghuo.getText().toString(), et_zuidiyipiao.getText().toString(),
					JCLApplication.getInstance().getUserId(),
					et_shixiao.getText().toString(),et_remark.getText().toString(),
					et_miaoshu.getText().toString(),et_tejia.getText().toString(),
					zx_image,endtime,chufawangdian,mudiwangdain,one_self,dff.format(cc.getTime()));
			String opterate,key;
			if (mybundle != null) {
				opterate = "M";
				key = lineinfo.get_id();
			} else {
				opterate = "A";
				key = "";
			}
			PublicZhuanxianRequest publicZhuanxianRequest = new PublicZhuanxianRequest(opterate,
					new Gson().toJson((data)),key);
			String postStr = new Gson().toJson(publicZhuanxianRequest);
			showLD("发布中...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {
						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(PublicZhuanxianActivity.this,
											"发布成功");
									finish();
								} else {
									MyToast.showToast(PublicZhuanxianActivity.this,
											"发布失败");
								}
							}
							cancelLD();
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(PublicZhuanxianActivity.this, "发布失败");
						}
					}));
			break;
			
		case R.id.tv_chufawangdian:
			if (tv_chufadi.getText().toString().isEmpty()) {
				MyToast.showToast(PublicZhuanxianActivity.this, "请输入出发地");
				return;
			} else {
				loadWangDian("1",chufadiCode,tv_chufawangdian);
			}
			break;
			
		case R.id.tv_mudiwangdian:
			if (tv_mudidi.getText().toString().isEmpty()) {
				MyToast.showToast(PublicZhuanxianActivity.this, "请输入目的地");
				return;
			} else {
				loadWangDian("2",mudidiCode,tv_mudiwangdian);
			}
			break;
			
		default:
			break;
		}
	}

	private String chufadiCode, mudidiCode;
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
				LogUtil.logWrite("MSZ_TAG", "chufadiCode==>" + chufadiCode);
				tv_chufadi.setText(info);
				break;
				
			case 2:
				mudidiCode = bundle.getString("cityCode");
				LogUtil.logWrite("MSZ_TAG", "mudidiCode==>" + mudidiCode);
				tv_mudidi.setText(info);
				break;

			case 3:
				tv_endtime.setText(info);
				break;
				
			default:
				break;
			}
		}
	};
	
	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		private String sorts;
		public GetStr(String filters) {
			this.filters = filters;
			this.type = "7000";
			this.sorts = sorts;
		}

	}
	class filters 
	{
		private String EQ_userid;
		private String EQ_startcode;
		private String EQ_type;
		public filters(String EQ_userid,String EQ_startcode)
		{
			this.EQ_userid = EQ_userid;
			this.EQ_startcode = EQ_startcode;
			this.EQ_type = "0";
		}
	}
	
	private void loadWangDian(final String strtype,String sorts,final TextView tv){
		String filters=new Gson().toJson(new filters(JCLApplication.getInstance().getUserId(),sorts));
		String getStr = new Gson().toJson(new GetStr(filters));
		
		executeRequest(new GsonRequest<WangDianBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, WangDianBean.class, null,
				ParamsBuilder.getStrParams(getStr),
				new Listener<WangDianBean>() {
					@SuppressWarnings("null")
					@Override
					public void onResponse(final WangDianBean arg0) {
						// 清除刷新小图标
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								AlertDialog.Builder builder = new AlertDialog.Builder(PublicZhuanxianActivity.this);
				                builder.setIcon(R.drawable.logo);
				                builder.setTitle("请选择网点");
				                final String[] wangdianlist = new String [arg0.getData().getAddresslist().size()];
				                
				                for (int i = 0; i < arg0.getData().getAddresslist().size(); i++) {
									wangdianlist[i] = arg0.getData().getAddresslist().get(i).getAddress_xx();
								}
				                //    设置一个单项选择下拉框
				                /**
				                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合
				                 * 第二个参数代表索引，指定默认哪一个单选框被勾选上
				                 * 第三个参数给每一个单选项绑定一个监听器
				                 */
				                builder.setSingleChoiceItems(wangdianlist, 0, new DialogInterface.OnClickListener()
				                {
				                    @Override
				                    public void onClick(DialogInterface dialog, int which)
				                    {
				                        Toast.makeText(PublicZhuanxianActivity.this, "网点为：" 
				                    + wangdianlist[which], Toast.LENGTH_SHORT).show();
				                        tv.setText(wangdianlist[which]);
				                        if (strtype.equals("1")) {
											chufawangdian = arg0.getData().getAddresslist().get(which).get_id();
										} else {
											mudiwangdain = arg0.getData().getAddresslist().get(which).get_id();
										}
				                        dialog.dismiss();
				                    }
				                });
//				                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//				                {
//				                    @Override
//				                    public void onClick(DialogInterface dialog, int which)
//				                    {
//				                       
//				                    }
//				                });
//				                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
//				                {
//				                    @Override
//				                    public void onClick(DialogInterface dialog, int which)
//				                    {
//				                        
//				                    }
//				                });
				                builder.show();
							} else {
								MyToast.showToast(PublicZhuanxianActivity.this, "服务端异常");
							}
						} else {
							MyToast.showToast(PublicZhuanxianActivity.this, "服务端异常");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
//						MyToast.showToast(MyCollectActivity.this, arg0.getMessage());
					}
				}));
	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
		if (C.SELECT_PIC_RESULT == resultCode) {
//			showLD("正在处理，请稍候...");
			final String imagePath = data.getStringExtra("imagePath");
			new Thread(new Runnable() {
				@Override
				public void run() {
					switch (requestCode) {
					case 111:
						zx_image = imageCompression.getImage(imagePath,
								requestCode);
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
	int requestCode;
	Handler handle = new Handler() {
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
			int requestCode = msg.what;
				switch (requestCode) {
				case 111:
					srcPath = (String) msg.obj;
					smallBitmap = FileUtils.getSmallBitmap(
							PublicZhuanxianActivity.this, srcPath);
					upload_img.setImageBitmap(smallBitmap);
					cancelLD();
					break;
				
			}
				}
	};
	
	@Override
	public void onSuccess(String srcPath, int requestCode) {
		Message msg = handle.obtainMessage();
		msg.what = requestCode;
		msg.obj = srcPath;
		handle.sendMessage(msg);
	}

	@Override
	public void onFaild(String srcPath, int requestCode) {
		handle.sendEmptyMessage(12121);
	}
}
