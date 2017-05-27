package com.jcl.android.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.push.SpeechPushInfoActivity;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyUINavigationView;

/**
 * 个人中心  设置页
 * 
 * @author pb
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	
	private Button btn_push;//推送控制按钮
	private Button btn_speech;//播报控制按钮
	private	View ll_select_place,ll_updata_pass,ll_exist;//常驻地 布局
	private TextView tv_place;//常驻地  选择显示
	private Button btn_push_mode;//接收模式
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initNavigation();
		initView();
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
		btnRightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	
	
	
	private void initView()
	{
		btn_push=(Button) findViewById(R.id.btn_push);
		btn_push.setOnClickListener(this);
//		btn_speech=(Button) findViewById(R.id.btn_speech);
//		btn_speech.setOnClickListener(this);
		btn_push_mode=(Button) findViewById(R.id.btn_push_mode);
		btn_push_mode.setOnClickListener(this);
		ll_updata_pass = findViewById(R.id.ll_updata_pass);
		ll_exist = findViewById(R.id.ll_exist);
		ll_updata_pass.setOnClickListener(this);
		ll_exist.setOnClickListener(this);
	}


	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_push:
			if(TextUtils.equals(btn_push.getText().toString(),"开启"))
			{
				btn_push.setText("关闭");
				btn_push.setBackgroundResource(R.drawable.icon_open);
				SharePerfUtil.put(SP.SP_IS_OPEN_PUSH, true);
			}else{
				btn_push.setText("开启");
				btn_push.setBackgroundResource(R.drawable.icon_close);
				SharePerfUtil.put(SP.SP_IS_OPEN_PUSH, false);
			}
			break;
		/*case R.id.btn_speech:
			if(TextUtils.equals(btn_speech.getText().toString(),"开启"))
			{
				btn_speech.setText("关闭");
				btn_speech.setBackgroundResource(R.drawable.icon_open);
				SharePerfUtil.put(SP.SP_IS_OPEN_SPEECH, true);
			}else{
				btn_speech.setText("开启");
				btn_speech.setBackgroundResource(R.drawable.icon_close);
				SharePerfUtil.put(SP.SP_IS_OPEN_SPEECH, false);
			}
			break;*/
		case R.id.btn_push_mode:
			startActivity(new Intent(SettingActivity.this,SpeechPushInfoActivity.class));
	
			break;
			//修改密码
		case R.id.ll_updata_pass:
			startActivity(new Intent(SettingActivity.this, ModifyPasswordActivity.class));
			break;

			//退出账号
		case R.id.ll_exist:
			final AlertDialog.Builder d=new AlertDialog.Builder(SettingActivity.this);
			d.setTitle("退出").setMessage("确定退出当前账号？").setPositiveButton
			("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharePerfUtil.loginOut();
					JCLApplication.getInstance().setUserId("");
					startActivity(new Intent(SettingActivity.this,LoginActivity.class));
					finish();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create().show();
			break;

		default:
			break;
		}
	}
	
	
}
