package com.jcl.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcl.android.BuildConfig;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.LoginBean;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.MD5;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 登录页
 * 
 * @author pb
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initNavigation();
		initView();
	}

	private MyUINavigationView uINavigationView;
	EditText edt_loginName;// 账号
	EditText edt_loginPassword;// 密码
	CheckBox cb_savePwd;// 保存密码
	Button btn_login;// 登录按钮
	TextView tv_register;
	TextView tv_savePwd;
	TextView tv_find_password;

	private void initView() {
		edt_loginName = (EditText) findViewById(R.id.edt_loginName);
		edt_loginPassword = (EditText) findViewById(R.id.edt_loginPassword);
		cb_savePwd = (CheckBox) findViewById(R.id.cb_savePwd);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_savePwd = (TextView) findViewById(R.id.tv_savePwd);
		tv_find_password = (TextView) findViewById(R.id.tv_find_password);

		tv_register.setOnClickListener(this);
		tv_savePwd.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_find_password.setOnClickListener(this);

		if(BuildConfig.DEBUG){
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setMessage("Debug测试账号登陆");
			builder.setPositiveButton("账号一", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					edt_loginName.setText("15315320776");
					edt_loginPassword.setText("123456");
				}
			});
			builder.setNegativeButton("账号二", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					edt_loginName.setText("13585717689");
					edt_loginPassword.setText("000000");
				}
			});
			builder.setCancelable(false);
			builder.show();
		}
	}

	private void setListener()
	{
		cb_savePwd.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked&&SharePerfUtil.getBoolean(SP.SP_ISLOGIN))
				{
					edt_loginName.setText(SharePerfUtil.getLoginName());
					edt_loginPassword.setText(SharePerfUtil.getLoginPwd());
				}
			}
		});
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

	public class LoginRequest {
		private String data;
		private String operate = "";
		private String type = "0002";

		public LoginRequest(String data) {
			this.data = data;
		}
	}

	public class LoginData {
		private String loginname;
		private String loginpwd;
		private String channelid;

		public LoginData(String loginname, String loginpwd,String channelId) {
			this.loginname = loginname;
			this.loginpwd = loginpwd;
			this.channelid=channelId;
		}
	}

	String data;
	String jsonRequest;
	private LoginData loginRequest;

	private void login() {
		loginRequest = new LoginData(edt_loginName.getText().toString(),
				MD5.getMD5(edt_loginPassword.getText().toString()),JCLApplication.getInstance().getChannelId());
		data = new Gson().toJson(loginRequest);
		jsonRequest = new Gson().toJson(new LoginRequest(data));
		executeRequest(new StringRequest(Request.Method.GET,
				UrlCat.getSubmitPoststrUrl(jsonRequest),
				new Listener<String>() {
					/**
					 * @param arg0
					 */
					@Override
					public void onResponse(String arg0) {
						LoginBean loginBean = new Gson().fromJson(arg0,
								LoginBean.class);
						if (loginBean != null) {
							if ("1".equals(loginBean.getCode())) {
								SharePerfUtil.put(SP.SP_ISLOGIN, true);
								SharePerfUtil.saveLoginUserInfo(arg0);
								SharePerfUtil.saveSubmittype(loginBean
										.getData().getSubmittype());
								SharePerfUtil.saveInvitecode(loginBean
										.getData().getInvitecode());
								SharePerfUtil.saveIsauth(loginBean
										.getData().getIsauth());
								SharePerfUtil.saveUserId(loginBean.getData()
										.getUserid());
								SharePerfUtil.saveIcount(loginBean.getData()
										.getIcount());
								SharePerfUtil.saveType(loginBean.getData()
										.getType());
								JCLApplication.getInstance().setUserId(loginBean.getData()
										.getUserid());
								if(TextUtils.equals(loginBean.getData().getSubmittype(),"0"))
								{
									SharePerfUtil.saveLinkMan(loginBean.getData().getName());
									SharePerfUtil.saveCompanyName(loginBean.getData().getCompanyname());
								}else{
									SharePerfUtil.saveLinkMan(loginBean.getData().getZhname());
									SharePerfUtil.saveCompanyName(loginBean.getData().getZhname());
								}
									
								
								SharePerfUtil.saveLoginName(edt_loginName.getText().toString());
								SharePerfUtil.saveLoginPwd(edt_loginPassword.getText().toString());
								Toast.makeText(LoginActivity.this,
										loginBean.getMsg(), Toast.LENGTH_SHORT).show();
								
								if (loginBean.getData().getSubmittype().equals("0") &&
										Utils.isEmpty(loginBean.getData().getName())) {
									//个人
									startActivity(new Intent(LoginActivity.this,PersonalInfoActivity.class));
								} else if (loginBean.getData().getSubmittype().equals("1") &&
										Utils.isEmpty(loginBean.getData().getZhname())){
//									公司
									startActivity(new Intent(LoginActivity.this,CompanyInfoActivity.class));
								} else{
									startActivity(new Intent(LoginActivity.this,HomeActivity.class));
								}
								
								finish();
							} else {
								Toast.makeText(LoginActivity.this,
										loginBean.getMsg(), Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(LoginActivity.this, "暂无数据！", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						LogUtil.logWrite("CHECK——code ==>", arg0.getMessage());
						Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT)
								.show();
					}
				}));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			if (TextUtils.isEmpty(edt_loginName.getText().toString().trim())) {
				MyToast.showToast(this, "请输入用户名");
				return;
			}
			if (TextUtils
					.isEmpty(edt_loginPassword.getText().toString().trim())) {
				MyToast.showToast(this, "请输入登录密码");
				return;
			}
			login();
			break;
		case R.id.tv_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.tv_savePwd:
			break;
			
		case R.id.tv_find_password:
			startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
		    break;
		    
		default:
			break;
		}

	}

}
