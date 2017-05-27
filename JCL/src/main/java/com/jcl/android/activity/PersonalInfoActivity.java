package com.jcl.android.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.jcl.android.bean.LoginBean;
import com.jcl.android.bean.PersonalInfoBean;
import com.jcl.android.bean.UpdataPersonalInfoBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyUINavigationView;

/**
 * 个人资料
 * 
 * @author pb
 *
 */
public class PersonalInfoActivity extends BaseActivity implements
		OnClickListener,OnImageComparessionListener{

	private MyUINavigationView uINavigationView;

	private ImageView img_header;// 头像
	private EditText edt_name;// 昵称
	private TextView edt_tel;// 手机
	private EditText edt_signaname;// 我的签名
	private TextView tv_LV,edt_type,tv_iscompany;// 我的等级
	private TextView tv_industry;// 行业
	private EditText edt_wechat;// 微信
	private TextView tv_job;// 公司职务
	private TextView tv_real_name;// 实名认证
	private EditText edt_qq;// 我的qq
	private View view_industry;
	private TextView tv_address;
	private EditText edt_alladdress;
	private String authstatus = "";
	private String username,alladdress,qq,weixin;
	private int publishnum = 1;
	private String type;
	private LinearLayout ll_company;

	private TextView tv_invitation_code;//邀请码
	private static final int IMG_ERROR = 12121;
	private ImageCompression imageCompression;
	private CityPickerPopupwindow cityPickerPopupwindow;
	private String addresscode = "";
	private boolean isheadpath = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		initNavigation();
		initView();
		getPersonalInfo();
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), handler, null);
	}

	private void initView() {
		imageCompression = ImageCompression.getInstance(this);
		view_industry = findViewById(R.id.view_industry);
		img_header = (ImageView) findViewById(R.id.img_header);
		edt_name = (EditText) findViewById(R.id.edt_name);
		edt_tel = (TextView) findViewById(R.id.edt_tel);
		tv_address = (TextView) findViewById(R.id.tv_address);
		edt_alladdress = (EditText) findViewById(R.id.edt_alladdress);
		edt_signaname = (EditText) findViewById(R.id.edt_signature);
		tv_LV = (TextView) findViewById(R.id.tv_LV);
		tv_industry = (TextView) findViewById(R.id.tv_industry);
		tv_iscompany = (TextView) findViewById(R.id.tv_iscompany);
		edt_wechat = (EditText) findViewById(R.id.edt_wechat);
		tv_job = (TextView) findViewById(R.id.tv_job);
		tv_real_name = (TextView) findViewById(R.id.tv_real_name);
		edt_qq = (EditText) findViewById(R.id.edt_qq);
		tv_invitation_code=(TextView) findViewById(R.id.tv_invitation_code);
		ll_company = (LinearLayout) findViewById(R.id.ll_company);

		view_industry.setOnClickListener(this);
		img_header.setOnClickListener(this);
		tv_LV.setOnClickListener(this);
		tv_industry.setOnClickListener(this);
		tv_real_name.setOnClickListener(this);
		tv_address.setOnClickListener(this);
		ll_company.setOnClickListener(this);
		String userinfoJson = SharePerfUtil.getLoginUserInfo();
		LoginBean loginBean = new Gson()
				.fromJson(userinfoJson, LoginBean.class);
		type = loginBean.getData().getType();
		if("0".equals(type)){
			type = "国内物流企业/车主";
		}else if("1".equals(type)){
			type = "发货企业/货主";
		}else if("2".equals(type)){
			type = "配货站";
		}else if("3".equals(type)){
			type = "国际物流企业/货代";
		}else if("4".equals(type)){
			type = "货车生产商";
		}else{
			type = "仓储";
		}
		edt_type=(TextView)findViewById(R.id.edt_type);
		edt_type.setText(type);
	}

	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setVisibility(View.GONE);
//		btnLeftText.setOnClickListener(new OnClickListener () {
//			@Override
//			public void onClick(View v) {
//				if (SharePerfUtil.getLinkMan().isEmpty()
//						|| SharePerfUtil.getLinkMan() == null) {
//					final AlertDialog.Builder d=new AlertDialog.Builder(PersonalInfoActivity.this);
//					d.setTitle("未填写姓名").setMessage("亲，您没有填写姓名，不能发不信息。").setPositiveButton
//					("马上完善", new DialogInterface.OnClickListener(){
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							
//						}
//					}).setNegativeButton("暂不完善", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							
//								finish();
//						}
//					}).create().show();
//				} else {
//					finish();
//				}
//			}
//		});
		btnRightText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPersonalInfo();
			}
		});
	}

	private String[] industry_string = new String[] { "交通运输、仓储和邮政业", "制造业",
			"批发和零售业", "农、林、牧、渔业", "采矿业", "房地产业", "电力、热力、燃气及水生产和供应业", "建筑业",
			"住宿和餐饮业", "信息传输、软件和信息技术服务业", "金融业", "租赁和商务服务业", "科学研究和技术服务业",
			"水利、环境和公共设施管理业", "居民服务、修理和其他服务业", "教育", "卫生和社会工作", "文化、体育和娱乐业",
			"公共管理、社会保障和社会组织", "国际组织" };
	private Builder builder = null;
	private int checkItem = 0;// 默认选择项

	private void showView() {
		if (builder == null) {
			builder = new AlertDialog.Builder(PersonalInfoActivity.this);
		}
		@SuppressWarnings("unused")
		AlertDialog dialog = builder
				.setTitle("请选择")
				.setSingleChoiceItems(industry_string, checkItem,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								checkItem = item;
								tv_industry.setText(industry_string[item]);
								dialog.dismiss();
							}
						}).show();
	}

	class UserinfoRequest {
		private String filters;
		private String type;
		private String sorts;

		public UserinfoRequest(String filters, String sorts) {
			this.filters = filters;
			this.type = "0003";
			this.sorts = sorts;
		}
	}

	class UserinfoFilters {
		private String EQ__id;
		public UserinfoFilters(String EQ__id) {
			this.EQ__id = EQ__id;
		}
	}

	class UserinfoSorts {
		private String _id;
		public UserinfoSorts() {
			this._id = "asc";
		}
	}

	private UserinfoRequest userinfoRequest;
	String filters;
	String sorts;
	String jsonRequest;

	public void getPersonalInfo() {
		filters = new Gson().toJson(new UserinfoFilters(SharePerfUtil
				.getUserId()));
		sorts = new Gson().toJson(new UserinfoSorts());
		jsonRequest = new Gson().toJson(new UserinfoRequest(filters, sorts));

		executeRequest(new GsonRequest<PersonalInfoBean>(Request.Method.GET,
				UrlCat.getInfoUrl(jsonRequest), PersonalInfoBean.class,
				null, null, new Listener<PersonalInfoBean>() {
					@Override
					public void onResponse(PersonalInfoBean arg0) {
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(PersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();
								edt_tel.setText(arg0.getData().getMobile() + "");
								authstatus = arg0.getData().getIsauth()+"";
								String astr = "";
								if("0".equals(authstatus)){
									astr = "未认证";
								}else if("1".equals(authstatus)){
									astr = "认证中";
								}else if("2".equals(authstatus)){
									astr = "认证成功";
								}else if("3".equals(authstatus)){
									astr = "未通过认证";
								}
								tv_real_name.setText(astr);
								if (arg0.getData().getSubmittype().equals("0")) {
									edt_name.setText(arg0.getData().getNickname());
								} else {
									edt_name.setText(arg0.getData().getZhname());
								}
								
								if (!Utils.isEmpty(arg0.getData().getIad())) {
									tv_address.setText(arg0.getData().getIad());
									addresscode = arg0.getData().getIadcode();
								}
								
								if (!Utils.isEmpty(arg0.getData().getIdetail())) {
									edt_alladdress.setText(arg0.getData().getIdetail());
									alladdress = arg0.getData().getIdetail();
								}
								
								if (Utils.isEmpty(arg0.getData().getHead())) {
									//以前没有上传
									isheadpath = false;
								}else {
									isheadpath = true;
									headerPath = "";
								}
								
								ImageLoaderUtil.getInstance(
										PersonalInfoActivity.this).loadImage(
										C.BASE_URL+arg0.getData().getHead(), img_header);
								
								edt_signaname.setText(arg0.getData()
										.getSignname());
								tv_LV.setText(arg0.getData().getLevel());
								tv_industry.setText(arg0.getData().getTrade());
								edt_wechat.setText(arg0.getData().getWechat());
								tv_job.setText(arg0.getData().getPost());
								tv_invitation_code.setText(SharePerfUtil.getInvitecode());
								if("2".equals(SharePerfUtil.getIsauth())){
									tv_invitation_code.setVisibility(View.VISIBLE);
								}else{
									tv_invitation_code.setVisibility(View.INVISIBLE);
								}
								JCLApplication.getInstance().setInviteCode(arg0.getData().getInvitecode());
								
//								tv_real_name.setText(TextUtils.equals(arg0
//										.getData().getIsauth(), "0") ? ""
//										: arg0.getData().getName());
								edt_qq.setText(arg0.getData().getQq());
								
								if (!Utils.isEmpty(arg0.getData().getCompanyinfok()) ||
										arg0.getData().getCompanyinfok().equals("1")) {
									tv_iscompany.setText("已完善");
								} else {
									tv_iscompany.setText("未完善");
								}
							} else {
								Toast.makeText(PersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(PersonalInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(PersonalInfoActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}

	class SetUserinfoRequest {
		private String type;
		private String data;
		private String operate;
		private String key;

		public SetUserinfoRequest(String data) {
			this.data = data;
			this.type = "0004";
			this.operate = "M";
			this.key = "";
		}
	}

	class UserinfoData {
		private String userid;
		private String head;
		private String nickname;
		private String wechat;
		private String post;
		private String signname;
		private String level;
		private String trade;
		private String qq;
		private String name;
		private String iad;
		private String iadcode;
		private String idetail;

		public UserinfoData(String userid, String head, String nickname,
				String wechat, String post, String signname, String level,
				String trade, String qq, String name,String iad,
				String iadcode,String idetail) {
			this.userid = userid;
			this.head = head;
			this.nickname = nickname;
			this.wechat = wechat;
			this.post = post;
			this.signname = signname;
			this.level = level;
			this.trade = trade;
			this.qq = qq;
			this.name = name;
			this.iad = iad;
			this.iadcode = iadcode;
			this.idetail = idetail;
		}
		
		public UserinfoData(String userid, String nickname,
				String wechat, String post, String signname, String level,
				String trade, String qq, String name,String iad,
				String iadcode,String idetail) {
			this.userid = userid;
			this.nickname = nickname;
			this.wechat = wechat;
			this.post = post;
			this.signname = signname;
			this.level = level;
			this.trade = trade;
			this.qq = qq;
			this.name = name;
			this.iad = iad;
			this.iadcode = iadcode;
			this.idetail = idetail;
		}

	}

	String data;
	String headerPath = "";

	public void setPersonalInfo() {
		if (edt_name.getText().toString().isEmpty()) {
			Toast.makeText(PersonalInfoActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			username = edt_name.getText().toString();
		}
		
		if (edt_alladdress.getText().toString().isEmpty()) {
			Toast.makeText(PersonalInfoActivity.this, "详细地址不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			alladdress = edt_alladdress.getText().toString();
		}
		
		if (addresscode.isEmpty()) {
			Toast.makeText(PersonalInfoActivity.this, "请选择地址", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (edt_qq.getText().toString().isEmpty()) {
			Toast.makeText(PersonalInfoActivity.this, "QQ不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			qq = edt_qq.getText().toString();
		}
		
		if (edt_wechat.getText().toString().isEmpty()) {
			Toast.makeText(PersonalInfoActivity.this, "微信不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			weixin = edt_wechat.getText().toString();
		}
		
		if (!isheadpath) {
			//以前没上传头像
			if (Utils.isEmpty(headerPath)) {
				//没有选择头像，必须选择，上传头像
				isheadpath = false;
				Toast.makeText(PersonalInfoActivity.this, "请选择头像", Toast.LENGTH_SHORT).show();
				return;
			} else {
                //已经选择头像，上传
				isheadpath = true;
				data = new Gson().toJson(new UserinfoData(SharePerfUtil.getUserId(),
						headerPath,username , weixin, tv_job.getText().toString(), edt_signaname
								.getText().toString(), tv_LV.getText().toString()
								.trim(), tv_industry.getText().toString(), qq,
								edt_name.getText().toString(),
								tv_address.getText().toString(),addresscode,alladdress));
			}
		} else {
			//以前上传了
			isheadpath = true;
			if (Utils.isEmpty(headerPath)) {
				//并没有重新选择图片
				data = new Gson().toJson(new UserinfoData(SharePerfUtil.getUserId(),
				         username , weixin, tv_job.getText().toString(), edt_signaname
						.getText().toString(), tv_LV.getText().toString()
						.trim(), tv_industry.getText().toString(), qq, edt_name.getText().toString(),
						tv_address.getText().toString(),addresscode,alladdress));
			} else {
				//重新选择图片
				data = new Gson().toJson(new UserinfoData(SharePerfUtil.getUserId(),
						headerPath,username , weixin, tv_job.getText().toString(), edt_signaname
								.getText().toString(), tv_LV.getText().toString()
								.trim(), tv_industry.getText().toString(), qq, edt_name.getText().toString(),
								tv_address.getText().toString(),addresscode,alladdress));
			}
		}
		
		jsonRequest = new Gson().toJson(new SetUserinfoRequest(data));

		executeRequest(new GsonRequest<UpdataPersonalInfoBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, UpdataPersonalInfoBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest),
				new Listener<UpdataPersonalInfoBean>() {
					@Override
					public void onResponse(UpdataPersonalInfoBean arg0) {
						// TODO Auto-generated method stub
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								
								SharePerfUtil.saveLinkMan(edt_name.getText().toString());
//								SharePerfUtil.savePersonHead(url)
								Toast.makeText(PersonalInfoActivity.this,
										arg0.getMsg(), 1000).show();
								finish();
								} else {
									Toast.makeText(PersonalInfoActivity.this,
											arg0.getMsg(), 1000).show();
								} 
							} else {
							Toast.makeText(PersonalInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(PersonalInfoActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}

	// 修改头像
	public static final String IMAGE_UNSPECIFIED = "image/*";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_header:
			Intent intent = new Intent(PersonalInfoActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, 111);
			break;
			
		case R.id.tv_address:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"submit");
			break;
			
		case R.id.tv_company:
			startActivity(new Intent(PersonalInfoActivity.this,
					AuthenticationCompanyActivity.class));
			break;
			
		case R.id.ll_company:
			startActivity(new Intent(PersonalInfoActivity.this,
					CompanyInfoActivity.class));
			break;
			
		case R.id.tv_real_name:
			if(authstatus.equals("0") || authstatus.equals("3")){
				startActivity(new Intent(PersonalInfoActivity.this,
						AuthenticationPersonalActivity.class));
			}
			break;
		case R.id.tv_industry:
			
			break;
			
		case R.id.view_industry:
			showView();
			break;

		default:
			break;
		}

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
						headerPath = imageCompression.getImage(imagePath,
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
				case 111:
					srcPath = (String) msg.obj;
					smallBitmap = FileUtils.getSmallBitmap(
							PersonalInfoActivity.this, srcPath);
					img_header.setImageBitmap(smallBitmap);
					cancelLD();
					break;
				case 1:
					addresscode = bundle.getString("cityCode");
					tv_address.setText(info);
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

}
