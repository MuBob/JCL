package com.jcl.android.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.MD5;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyUINavigationView;

public class ModifyPasswordActivity extends BaseActivity implements OnClickListener{
	private EditText edt_old_password;
	private EditText edt_password;
	private EditText edt_password_repeat;
	private Button btn_post;
	private RegisterData registerRequest;
	private MyUINavigationView uINavigationView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_modify_pass);
		initNavigation();
		initView();
	}

	private void initView(){
		edt_password_repeat=(EditText)findViewById(R.id.edt_password_repeat);
		edt_password=(EditText)findViewById(R.id.edt_password);
		edt_old_password = (EditText)findViewById(R.id.edt_old_password);
		btn_post =(Button)findViewById(R.id.btn_post);
		btn_post.setOnClickListener(this);
	}
	
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
		
		class RegisterRequest {
			private String type;
			private String data;

			public RegisterRequest(String data) {
				this.type = "0008";
				this.data = data;
			}
		}

		class RegisterData {
			private String userid;
			private String newpass;
			private String type;
			private String oldpass;

			public RegisterData(String newpass, String type,
					String oldpass) {
				this.userid = SharePerfUtil.getLoginName();
				this.newpass = newpass;
				this.type = type;
				this.oldpass = oldpass;
			}

		}
		

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		case R.id.btn_post:
			if(TextUtils.isEmpty(edt_old_password.getText()))
			{
				Toast.makeText(ModifyPasswordActivity.this, "请填写原密码！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(edt_password.getText()))
			{
				Toast.makeText(ModifyPasswordActivity.this, "请填写密码！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(edt_password_repeat.getText()))
			{
				Toast.makeText(ModifyPasswordActivity.this, "请填写确认密码！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(!edt_password.getText().toString().equals((edt_password_repeat.getText()).toString()))
			{
				Toast.makeText(ModifyPasswordActivity.this, "俩次输入密码不一致！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			//原密码  type 新密码
			registerRequest = new RegisterData(MD5.getMD5(edt_password.getText().toString()),"1",
					MD5.getMD5(edt_old_password.getText().toString()));
			String data = new Gson().toJson(registerRequest);
			String jsonRequest = new Gson().toJson(new RegisterRequest(data));
			executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
					UrlCat.getSubmitPoststrUrl(jsonRequest), BaseBean.class, null,
					null, new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if(arg0!=null){
								if ("1".equals(arg0.getCode())) {
									Toast.makeText(ModifyPasswordActivity.this, "新密码设置成功",
											Toast.LENGTH_SHORT).show();
									ModifyPasswordActivity.this.finish();
								}else{
									Toast.makeText(ModifyPasswordActivity.this, arg0.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(ModifyPasswordActivity.this, "暂无数据！",
										Toast.LENGTH_SHORT).show();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							Toast.makeText(ModifyPasswordActivity.this, "网络连接异常！",
									Toast.LENGTH_SHORT).show();
						}
					}));
			
			break;
		default:
			break;
		}
	}

}
