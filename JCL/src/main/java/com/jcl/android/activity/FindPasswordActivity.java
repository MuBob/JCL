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
import com.jcl.android.bean.VerifyCodeBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.MD5;
import com.jcl.android.utils.MyApplicationUtils;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 找回密码
 * @author syl
 * */
public class FindPasswordActivity extends BaseActivity implements OnClickListener{
	private EditText edt_tel;
	private EditText edt_password;
	private EditText edt_password_repeat;
	private EditText edt_checkCode;
	private Button btn_checkcode;
	private Button btn_yanzheng,btn_post;
	private CheckCodeData checkCodeRequest;
	private RegisterData registerRequest;
	private LinearLayout ll_yanzhengma,ll_post;
	private MyUINavigationView uINavigationView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_find_password);
		initNavigation();
		initView();
	}

	private void initView(){
		edt_tel=(EditText)findViewById(R.id.edt_tel);
		edt_tel.setInputType(InputType.TYPE_CLASS_PHONE);
		edt_password_repeat=(EditText)findViewById(R.id.edt_password_repeat);
		edt_password=(EditText)findViewById(R.id.edt_password);
		edt_checkCode=(EditText)findViewById(R.id.edt_checkCode);
		btn_checkcode=(Button)findViewById(R.id.btn_checkcode);
		btn_yanzheng =(Button)findViewById(R.id.btn_yanzheng);
		btn_post =(Button)findViewById(R.id.btn_post);
		ll_yanzhengma = (LinearLayout) findViewById(R.id.ll_yanzhenma);
		ll_post = (LinearLayout) findViewById(R.id.ll_post);
		btn_checkcode.setOnClickListener(this);
		btn_yanzheng.setOnClickListener(this);
		btn_post.setOnClickListener(this);
	}
	
	private void initNavigation() {
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private Timer timer;
	// 定义Handler
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				// Handler处理消息
				if (msg.what > 0) {
					btn_checkcode.setText("重新获取(" + msg.what + ")");
				} else if (msg.what == 0) {
					// 结束Timer计时器
					timer.cancel();
					btn_checkcode.setText("重新获取");
					btn_checkcode.setClickable(true);
				}
			}
		};
		
		private String random="";//接口返回验证码字符串
		String data ;
		String jsonRequest;
		
		class CheckCodeRequest {
			private String data;

			public CheckCodeRequest(String data) {
				this.data = data;
			}
		}
		class CheckCodeData {
			private String tel;

			public CheckCodeData(String tel) {
				this.tel = tel;
			}

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
				this.userid = edt_tel.getText().toString().trim();
				this.newpass = newpass;
				this.type = type;
				this.oldpass = oldpass;
			}

		}
		

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		case R.id.btn_checkcode:
			if(TextUtils.isEmpty(edt_tel.getText()))
			{
				Toast.makeText(FindPasswordActivity.this, "请填写电话号码！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(!MyApplicationUtils.isPhone(edt_tel.getText().toString())){
				MyToast.showToast(FindPasswordActivity.this, "请输入正确的手机号码！");
				return;
			}
			timer = new Timer();
			// 创建一个TimerTask
			TimerTask timerTask = new TimerTask() {
				// 倒数10秒
				int i = 60;
				@Override
				public void run() {
					// 定义一个消息传过去
					Message msg = new Message();
					msg.what = i--;
					handler.sendMessage(msg);
				}
			};
			// TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
			timer.schedule(timerTask, 0, 1000);
			btn_checkcode.setClickable(false);
			checkCodeRequest = new CheckCodeData(edt_tel.getText().toString());
			data = new Gson().toJson(checkCodeRequest);
			jsonRequest = new Gson().toJson(new CheckCodeRequest(data));
			executeRequest(new GsonRequest<VerifyCodeBean>(Request.Method.GET,
					UrlCat.getCheckCodeUrl(jsonRequest), VerifyCodeBean.class, null,
					null, new Listener<VerifyCodeBean>() {
						/**
						 * @param arg0
						 */
						@SuppressWarnings("unused")
						@Override
						public void onResponse(VerifyCodeBean arg0) {
							// TODO Auto-generated method stub
							LogUtil.logWrite("CHECK——code ==>", arg0.toString());
							if(arg0!=null){
								if ("1".equals(arg0.getCode())) {
									random=arg0.getData();
									Toast.makeText(FindPasswordActivity.this, arg0.getMsg(),
											Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(FindPasswordActivity.this, arg0.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(FindPasswordActivity.this, "暂无数据！",
										Toast.LENGTH_SHORT).show();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							LogUtil.logWrite("CHECK——code ==>", arg0.getMessage());
							Toast.makeText(FindPasswordActivity.this, "暂无数据！",
									Toast.LENGTH_SHORT).show();
						}
					}));
			break;
			
		case R.id.btn_yanzheng:
			if(!edt_checkCode.getText().toString().equals(random)
				|| edt_checkCode.getText().toString().equals("")
				|| edt_checkCode.getText().toString() == ""){
				Toast.makeText(FindPasswordActivity.this, "验证码错误！",
						Toast.LENGTH_SHORT).show();
				return;
			}else{
				ll_yanzhengma.setVisibility(View.GONE);
				ll_post.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.btn_post:
			if(TextUtils.isEmpty(edt_password.getText()))
			{
				Toast.makeText(FindPasswordActivity.this, "请填写密码！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(edt_password_repeat.getText()))
			{
				Toast.makeText(FindPasswordActivity.this, "请确认密码！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(!edt_password.getText().toString().equals((edt_password_repeat.getText()).toString()))
			{
				Toast.makeText(FindPasswordActivity.this, "俩次输入密码不一致！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			registerRequest = new RegisterData(MD5.getMD5(edt_password.getText().toString()),"0","");
			data = new Gson().toJson(registerRequest);
			jsonRequest = new Gson().toJson(new RegisterRequest(data));
			executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
					UrlCat.getSubmitPoststrUrl(jsonRequest), BaseBean.class, null,
					null, new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if(arg0!=null){
								if ("1".equals(arg0.getCode())) {
									Toast.makeText(FindPasswordActivity.this, "新密码设置成功",
											Toast.LENGTH_SHORT).show();
									FindPasswordActivity.this.finish();
								}else{
									Toast.makeText(FindPasswordActivity.this, arg0.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(FindPasswordActivity.this, "暂无数据！",
										Toast.LENGTH_SHORT).show();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							Toast.makeText(FindPasswordActivity.this, "网络连接异常！",
									Toast.LENGTH_SHORT).show();
						}
					}));
			
			break;
		default:
			break;
		}
	}

}
