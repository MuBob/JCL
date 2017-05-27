package com.jcl.android.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jcl.android.bean.WangDianBean;
import com.jcl.android.bean.WangDianBean.AddressList;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;

import java.text.SimpleDateFormat;

public class AddContactsActivity extends BaseActivity implements OnClickListener{

	private ImageView iv_back;
	private EditText et_name,et_company,et_tel,et_phone,
	et_address,et_fapiao,et_email,et_qq,et_weixin;
	private TextView tv_areaselect;
	private String name,tel,address;
	private Button btn_save;
	private CityPickerPopupwindow cityPickerPopupwindow;
	private String chufadiCode, chufaCityLat,chufaCityLnt;
	
	private WangDianBean.AddressList dataInfo;
	private Bundle bundle;
	private String operate,key;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_addcontacts);
		initView();
		cityPickerPopupwindow = new CityPickerPopupwindow(this,
				findViewById(R.id.ll_parent), mHandler, null);
	}
	
	private void initView() {
		Intent intent = getIntent();
		bundle = intent.getExtras();
		et_name = (EditText) findViewById(R.id.et_name);
		et_company = (EditText) findViewById(R.id.et_company);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_address = (EditText) findViewById(R.id.et_address);
		et_fapiao = (EditText) findViewById(R.id.et_fapiao);
		et_email = (EditText) findViewById(R.id.et_email);
		et_qq = (EditText) findViewById(R.id.et_qq);
		et_weixin = (EditText) findViewById(R.id.et_weixin);
		tv_areaselect = (TextView) findViewById(R.id.tv_areaselect);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_save = (Button) findViewById(R.id.btn_save);
		
		iv_back.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		tv_areaselect.setOnClickListener(this);
		if (bundle != null) {
			dataInfo = (AddressList) bundle.get("info");
			initContant();
			operate = "M";
		} else {
			operate = "A";
		}
	}
	
	private void initContant() {
		et_name.setText(dataInfo.getPname());
		et_company.setText(dataInfo.getA_company());
		et_tel.setText(dataInfo.getA_mobile());
		et_phone.setText(dataInfo.getA_phone());
		et_address.setText(dataInfo.getAddress_xx());
		et_fapiao.setText(dataInfo.getInvoice_d());
		et_email.setText(dataInfo.getEmail());
		et_qq.setText(dataInfo.getQq());
		et_weixin.setText(dataInfo.getWeixin());
	}

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
				tv_areaselect.setText(info);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_areaselect:
			if (cityPickerPopupwindow != null)
				cityPickerPopupwindow.show(1,"submit");
			break;
			
		case R.id.btn_save:
			if (Utils.isEmpty(et_name.getText().toString())) {
				MyToast.showToast(AddContactsActivity.this, "请输入姓名");
				return;
			} else {
				name = et_name.getText().toString();
			}
			
			if (Utils.isEmpty(et_tel.getText().toString())) {
				MyToast.showToast(AddContactsActivity.this, "请输入手机号");
				return;
			} else {
				tel = et_tel.getText().toString();
			}
			
			if (Utils.isEmpty(et_address.getText().toString())) {
				MyToast.showToast(AddContactsActivity.this, "请输入地址");
				return;
			} else {
				address = et_address.getText().toString();
			}
			
			if (Utils.isEmpty(chufadiCode)) {
				MyToast.showToast(AddContactsActivity.this, "请选择省市区");
				return;
			}
			
			
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");       
			String date = sDateFormat.format(new java.util.Date());  
			
			Data data = new Data(et_company.getText().toString(), chufadiCode, et_phone.getText().toString(),
					"0", JCLApplication.getInstance().getUserId(), chufaCityLnt,
					"0", "0", date,tel,
					et_fapiao.getText().toString(),name,"",
					address,chufaCityLat,et_email.getText().toString(),
					et_qq.getText().toString(),et_weixin.getText().toString());
			String dataJson = new Gson().toJson(data);
			String key;
			if (bundle != null) {
				key = dataInfo.get_id();
			} else {
				key = "";
			}
			PublicGoodsRequest publicGoodsRequest = new PublicGoodsRequest(
					dataJson,operate,key);
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
											AddContactsActivity.this,
											"添加成功");
									finish();
								} else {
									MyToast.showToast(
											AddContactsActivity.this,
											"添加失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(AddContactsActivity.this,
									"发布失败");
						}
					}));
			
			break;

		default:
			break;
		}
	}
	
	class PublicGoodsRequest {
		private String operate;
		private String type;
		private String data;
		private String key;

		public PublicGoodsRequest(String data,String operate,String key) {
			this.operate = operate;
			this.type = "11001";
			this.data = data;
			this.key = key;
		}
	}

	class Data {
		
		private String a_company;//
		private String startcode;
		private String a_phone;
		private String ispc;
		private String userid;
		private String lng;
		private String type;
		private String submittype;
		private String createtime;
		private String a_mobile;
		private String invoice_d;
		private String pname;
		private String startarea;
		private String address_xx;
		private String lat;
		private String email;
		private String qq;
		private String weixin;
		
		public Data(String a_company, String startcode, String a_phone,
				String ispc, String userid, String lng,
				String type, String submittype, String createtime,
				String a_mobile, String invoice_d,String pname,String startarea,
				String address_xx,String lat,String email,String qq,String weixin) {

			this.a_company = a_company;
			this.ispc = ispc;
			this.startcode = startcode;
			this.a_phone = a_phone;
			this.startarea = startarea;
			this.userid = userid;
			this.lng = lng;
			this.a_mobile = a_mobile;
			this.invoice_d = invoice_d;
			this.pname = pname;
			this.address_xx = address_xx;
			this.lat = lat;
			this.createtime = createtime;
			this.email = email;
			this.qq = qq;
			this.weixin = weixin;

		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getQq() {
			return qq;
		}

		public void setQq(String qq) {
			this.qq = qq;
		}

		public String getWeixin() {
			return weixin;
		}

		public void setWeixin(String weixin) {
			this.weixin = weixin;
		}

		public String getA_company() {
			return a_company;
		}

		public void setA_company(String a_company) {
			this.a_company = a_company;
		}

		public String getStartcode() {
			return startcode;
		}

		public void setStartcode(String startcode) {
			this.startcode = startcode;
		}

		public String getA_phone() {
			return a_phone;
		}

		public void setA_phone(String a_phone) {
			this.a_phone = a_phone;
		}

		public String getIspc() {
			return ispc;
		}

		public void setIspc(String ispc) {
			this.ispc = ispc;
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getLng() {
			return lng;
		}

		public void setLng(String lng) {
			this.lng = lng;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSubmittype() {
			return submittype;
		}

		public void setSubmittype(String submittype) {
			this.submittype = submittype;
		}

		public String getCreatetime() {
			return createtime;
		}

		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public String getA_mobile() {
			return a_mobile;
		}

		public void setA_mobile(String a_mobile) {
			this.a_mobile = a_mobile;
		}

		public String getInvoice_d() {
			return invoice_d;
		}

		public void setInvoice_d(String invoice_d) {
			this.invoice_d = invoice_d;
		}

		public String getPname() {
			return pname;
		}

		public void setPname(String pname) {
			this.pname = pname;
		}

		public String getStartarea() {
			return startarea;
		}

		public void setStartarea(String startarea) {
			this.startarea = startarea;
		}

		public String getAddress_xx() {
			return address_xx;
		}

		public void setAddress_xx(String address_xx) {
			this.address_xx = address_xx;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

	}

}
