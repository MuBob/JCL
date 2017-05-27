package com.jcl.android.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.MessageDetilBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

public class MessageDetilActivity extends BaseActivity implements OnClickListener{

	private TextView tv_time,tv_content;
	private ImageView iv_pic;
	private String messageid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detil);
		initView();
		initNavigation();
	}
	
	private void initView() {
		messageid = getIntent().getExtras().getString("messageid");
		tv_time = (TextView) findViewById(R.id.textView1);
		tv_content = (TextView) findViewById(R.id.textView2);
		loadData();
		
	}
	
	private MyUINavigationView uINavigationView;
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

	private void loadData() {
		//拼接filter中数据
				String filters =  new Gson().toJson(new GetFilter(messageid));
				String getStr = new Gson().toJson(new GetStr(filters));
				executeRequest(new GsonRequest<MessageDetilBean>(Request.Method.POST,
						UrlCat.URL_SEARCH, MessageDetilBean.class, null,
						ParamsBuilder.getStrParams(getStr),
						new Listener<MessageDetilBean>() {
							@Override
							public void onResponse(MessageDetilBean arg0) {
								if (arg0 != null) { 
									if (TextUtils.equals(arg0.getCode(), "1")) {
											tv_time.setText(arg0.getData().getCreatedate());
											tv_content.setText(arg0.getData().getContent());
											final String huoyuanid = arg0.getData().getBizid();
											final int type = Integer.valueOf(arg0.getData().getType());
											
											switch (type) {
											case 1:
												tv_content.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View arg0) {
														startActivity(DetailFindActivity.newInstance(MessageDetilActivity.this,
																type,huoyuanid));		
													}
												});
												break;
											case 2:
												tv_content.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View arg0) {
														startActivity(DetailFindActivity.newInstance(MessageDetilActivity.this,
																type,huoyuanid));		
													}
												});
												break;

											default:
												break;
											}
									} else {
										MyToast.showToast(MessageDetilActivity.this, "服务端异常");
									}
								} else {
									MyToast.showToast(MessageDetilActivity.this, "服务端异常");
								}
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
//								MyToast.showToast(MessageDetilActivity.this, arg0.getMessage());
							}
						}));
	}


	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件

		public GetStr(String filters) {
			this.filters = filters;
			this.type = "00111";
		}

	}
	
	class GetFilter {
		private String _id;

		public GetFilter(String _id) {
			this._id = _id;
		}

	}
	@Override
	public void onClick(View arg0) {
		
	}

}
