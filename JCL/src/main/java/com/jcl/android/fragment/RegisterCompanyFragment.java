package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.activity.PersonalInfoActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.VerifyCodeBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.MD5;
import com.jcl.android.utils.MyApplicationUtils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.WhSpinner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterCompanyFragment extends BaseFragment implements OnClickListener{
	
private View root;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.fragment_register_company, container,false);
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initWhSpinnerData();
	}

	private EditText edt_company_name;
	private EditText edt_mail;
	private EditText edt_tel;
	private EditText edt_password;
	private EditText edt_password_repeat;
	private EditText edt_checkCode;
	private EditText edt_invitation_code;//邀请码
	private Button btn_checkcode;
	com.jcl.android.view.WhSpinner user_type;
	private List<WhSpinner.Item> userType;
	public void initWhSpinnerData()
	{
		userType=new ArrayList<WhSpinner.Item>();
		userType.add(new WhSpinner.Item("国内物流企业/车主","0"));
		userType.add(new WhSpinner.Item("发货企业/货主","1"));
		userType.add(new WhSpinner.Item("配货站","2"));
		userType.add(new WhSpinner.Item("国际物流企业/货代","3"));
		userType.add(new WhSpinner.Item("货车生产/销售商","4"));
		userType.add(new WhSpinner.Item("仓储/物流园/停车场","5"));
		userType.add(new WhSpinner.Item("报关报检","6"));
		userType.add(new WhSpinner.Item("其他类型","7"));
		user_type.setItems(userType, 0);
	}
	private void initView()
	{
		edt_company_name=(EditText)root.findViewById(R.id.edt_company_name);
		edt_mail=(EditText)root.findViewById(R.id.edt_mail);
		edt_tel=(EditText)root.findViewById(R.id.edt_tel);
		edt_tel.setInputType(InputType.TYPE_CLASS_PHONE);
		edt_password_repeat=(EditText)root.findViewById(R.id.edt_password_repeat);
		user_type=(com.jcl.android.view.WhSpinner)root.findViewById(R.id.user_type);
		edt_password=(EditText)root.findViewById(R.id.edt_password);
		edt_checkCode=(EditText)root.findViewById(R.id.edt_checkCode);
		btn_checkcode=(Button)root.findViewById(R.id.btn_checkcode);
		btn_checkcode.setOnClickListener(this);
		root.findViewById(R.id.btn_register).setOnClickListener(this);
	}

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
		private String operate;
		private String type;
		private String data;

		public RegisterRequest(String data) {
			this.operate = "A";
			this.type = "0001";
			this.data = data;
		}
	}

	class RegisterData {
		private String mobile;
		private String password;
		private String type;
		private String invitecode;
		private String zhname;
		private String email;
		private String submittype="1"; 
		

		public RegisterData(String mobile, String password, String type,
				String invitecode,String zhname,String email) {
			this.mobile = mobile;
			this.password = password;
			this.type = type;
			this.invitecode = invitecode;
			this.zhname = zhname;
			this.email = email;
		}

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
	private RegisterData registerRequest;
	private CheckCodeData checkCodeRequest;
	private String random;//接口返回验证码字符串
	String data ;
	String jsonRequest;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_register:
			if(TextUtils.isEmpty(edt_company_name.getText()))
			{
				Toast.makeText(getActivity(), "请填写公司名称！",
						1000).show();
				return;
			}
			if(TextUtils.isEmpty(edt_mail.getText()))
			{
				Toast.makeText(getActivity(), "请填写email！",
						1000).show();
				return;
			}
			if(!MyApplicationUtils.isEmail(edt_mail.getText().toString())){
				MyToast.showToast(getActivity(), "请输入正确的email！");
				return;
			}
			if(TextUtils.isEmpty(edt_tel.getText()))
			{
				Toast.makeText(getActivity(), "请填写电话号码！",
						1000).show();
				return;
			}
			if(!MyApplicationUtils.isPhone(edt_tel.getText().toString())){
				MyToast.showToast(getActivity(), "请输入正确的手机号码！");
				return;
			}
			if(TextUtils.isEmpty(edt_password.getText()))
			{
				Toast.makeText(getActivity(), "请填写密码！",
						1000).show();
				return;
			}
			if(TextUtils.isEmpty(edt_password_repeat.getText()))
			{
				Toast.makeText(getActivity(), "请确认密码！",
						1000).show();
				return;
			}
			if(!edt_password.getText().toString().equals((edt_password_repeat.getText()).toString()))
			{
				Toast.makeText(getActivity(), "俩次输入密码不一致！",
						1000).show();
				return;
			}
			if(TextUtils.isEmpty(edt_checkCode.getText()))
			{
				Toast.makeText(getActivity(), "请填写验证码！",
						1000).show();
				return;
			}
			if(!edt_checkCode.getText().toString().equals(random))
			{
				Toast.makeText(getActivity(), "验证码错误！",
						1000).show();
				return;
			}
			if(TextUtils.isEmpty(edt_invitation_code.getText()))
			{
				Toast.makeText(getActivity(), "请输入邀请码！",
						1000).show();
				return;
			}
			
			registerRequest = new RegisterData(edt_tel.getText().toString(),
					MD5.getMD5(edt_password.getText().toString()),  
					user_type.getChoiceValue().toString(),
					TextUtils.isEmpty(edt_invitation_code.getText())?"":edt_invitation_code.getText().toString()
					,edt_company_name.getText().toString()
					,edt_mail.getText().toString());
			data = new Gson().toJson(registerRequest);
			jsonRequest = new Gson().toJson(new RegisterRequest(data));
			executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
					UrlCat.getSubmitPoststrUrl(jsonRequest), BaseBean.class, null,
					null, new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							// TODO Auto-generated method stub
							if(arg0!=null){
								if ("1".equals(arg0.getCode())) {
									Toast.makeText(getActivity(),"请完善个人信息" ,
											1000).show();
//									getActivity().finish();
									startActivity(new Intent(getActivity(),PersonalInfoActivity.class));
								}else{
									Toast.makeText(getActivity(), arg0.getMsg(),
											1000).show();
								}
							}else{
								Toast.makeText(getActivity(), "暂无数据！",
										1000).show();
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "网络连接异常！",
									1000).show();
						}
					}));
			break;
		case R.id.btn_checkcode:
			if(TextUtils.isEmpty(edt_tel.getText()))
			{
				Toast.makeText(getActivity(), "请填写电话号码！",
						1000).show();
				return;
			}
			if(!MyApplicationUtils.isPhone(edt_tel.getText().toString())){
				MyToast.showToast(getActivity(), "请输入正确的手机号码！");
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
						@Override
						public void onResponse(VerifyCodeBean arg0) {
							// TODO Auto-generated method stub
							LogUtil.logWrite("CHECK——code ==>", arg0.toString());
							if(arg0!=null){
								if ("1".equals(arg0.getCode())) {
									random=arg0.getData();
									Toast.makeText(getActivity(), arg0.getMsg(),
											1000).show();
								}else{
									Toast.makeText(getActivity(), arg0.getMsg(),
											1000).show();
								}
							}else{
								Toast.makeText(getActivity(), "暂无数据！",
										1000).show();
							}
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							LogUtil.logWrite("CHECK——code ==>", arg0.getMessage());
							Toast.makeText(getActivity(), "暂无数据！",
									1000).show();
						}
					}));
			break;
		default:
			break;
		}
	}
	
	
	
}
