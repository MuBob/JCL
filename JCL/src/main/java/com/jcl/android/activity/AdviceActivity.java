package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.activity.AddGoodsActivity.AddGoodsData;
import com.jcl.android.activity.AddGoodsActivity.AddGoodsRequest;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
/**
 * 投诉建议页面
 * syl
 * 
 * */
public class AdviceActivity extends BaseActivity implements OnClickListener{

	private ImageView iv_back,iv_tel;
	private EditText et_advice;
	private Button btn_tijiao;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_advice);
		initView();
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_tel = (ImageView) findViewById(R.id.iv_tel);
		et_advice = (EditText) findViewById(R.id.et_advice);
		btn_tijiao = (Button) findViewById(R.id.btn_tijiao);
		
		iv_back.setOnClickListener(this);
		iv_tel.setOnClickListener(this);
		btn_tijiao.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.btn_tijiao:
			submit();
			break;
			
        case R.id.iv_tel:
			
			break;
		default:
			break;
		}
	}
	
	public void submit() {
		if (TextUtils.isEmpty(et_advice.getText().toString().trim())) {
			MyToast.showToast(this, "请填写投诉建议");
			return;
		}
		
		String data = new Gson().toJson(new AdviceData(et_advice.getText().toString().trim()));
		String jsonRequest = new Gson().toJson(new AdviceRequest(data));
		showLD("提交中...");
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest),
				new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						cancelLD();
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
									Toast.makeText(AdviceActivity.this,
											"提交成功", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(AdviceActivity.this,
										arg0.getMsg(), Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(AdviceActivity.this, "暂无数据！", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
						Toast.makeText(AdviceActivity.this, "网络连接异常！", Toast.LENGTH_SHORT)
								.show();
					}
				}));
	}
	
	class AdviceRequest {
		
		private String type;
		private String data;
		private String operate;

		public AdviceRequest(String data) {
			this.operate = "A";
			this.type = "0012";
			this.data = data;
		}
	}
	
class AdviceData {
		
		private String userid;
		private String mobile;
		private String content;

		public AdviceData(String content) {
			this.userid = JCLApplication.getInstance().getUserId();
			this.mobile = "";
			this.content = content;
		}
	}

}
