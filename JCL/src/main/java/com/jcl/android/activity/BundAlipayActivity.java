package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.OtherListBean;
import com.jcl.android.bean.GoodsListBean.GoodsInfo;
import com.jcl.android.bean.OtherListBean.OtherInfo;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.WhSpinner;
/**
 * 绑定支付宝账号
 * @author xueleilin
 *
 */
public class BundAlipayActivity extends BaseActivity implements
		OnClickListener {

	private EditText et_alipay;
	private Button btn_public;
	private OtherListBean.OtherInfo info;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_bund_alipay);
		initView();
		//initConent();
	}


	private void initView() {
		// TODO Auto-generated method stub
		et_alipay = (EditText) findViewById(R.id.et_alipay);
		btn_public = (Button) findViewById(R.id.btn_public);
		btn_public.setOnClickListener(this);
	}
	

	class BundAlipayRequest {
		private String operate;
		private String type;
		private String data;

		public BundAlipayRequest(String data) {
			this.operate = "A";
			this.type = "4001";
			this.data = data;
		}
	}

	class Data {
		private String type;// 账号类型 　
		private String account;// 账号 　
		private String userid;// 用户编码 　

		public Data(String type, String account, String userid) {
			this.type = type;
			this.account = account;
			this.userid = userid;

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_public) {
			showLD("提交中...");
			Data data = new Data("0", et_alipay.getText().toString(), JCLApplication.getInstance().getUserId());
			String postStr = new Gson().toJson(new BundAlipayRequest(
					new Gson().toJson(data)));
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {
						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(BundAlipayActivity.this,
											"提交成功");
									finish();
								} else {
									MyToast.showToast(BundAlipayActivity.this,
											"提交失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(BundAlipayActivity.this, "提交失败");
						}
					}));
		}
	}
}
