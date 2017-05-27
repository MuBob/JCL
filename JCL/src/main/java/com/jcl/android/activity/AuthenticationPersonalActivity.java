package com.jcl.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 个人认证
 * 
 * @author pb
 *
 */
public class AuthenticationPersonalActivity extends BaseActivity implements
		OnClickListener, OnImageComparessionListener {

	private MyUINavigationView uINavigationView;
	protected String imagePath;

	private static final int IV_USERCARD_Y = 0x0001;
	private static final int IV_USERCARD_N = 0x0002;
	private static final int IV_CARCARD = 0x0003;
	private static final int IV_SHOUCHICARD = 0x0004;

	ImageView iv_usercard_y, iv_usercard_n, iv_carcard,iv_shouchicard;

	private EditText et_truename, et_usercardnum;
	private Button btn_commit;

	private ImageCompression imageCompression;

	private String yBitmap, nBitmap, carBitmap,shouchiBitmap;

	protected void init() {
		iv_usercard_y = (ImageView) findViewById(R.id.iv_usercard_y);
		iv_usercard_n = (ImageView) findViewById(R.id.iv_usercard_n);
		iv_carcard = (ImageView) findViewById(R.id.iv_carcard);
		iv_shouchicard = (ImageView) findViewById(R.id.iv_shouchicard);

		et_truename = (EditText) findViewById(R.id.et_truename);
		et_usercardnum = (EditText) findViewById(R.id.et_usercardnum);

		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener(this);

		iv_usercard_y.setOnClickListener(this);
		iv_usercard_n.setOnClickListener(this);
		iv_carcard.setOnClickListener(this);
		iv_shouchicard.setOnClickListener(this);
		imageCompression = ImageCompression.getInstance(this);
	}

	Intent intent;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_usercard_y:
			intent = new Intent(AuthenticationPersonalActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IV_USERCARD_Y);
			break;
		case R.id.iv_usercard_n:
			intent = new Intent(AuthenticationPersonalActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IV_USERCARD_N);
			break;
		case R.id.iv_carcard:
			intent = new Intent(AuthenticationPersonalActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IV_CARCARD);
			break;
		case R.id.iv_shouchicard:
			intent = new Intent(AuthenticationPersonalActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IV_SHOUCHICARD);
			break;
		case R.id.btn_commit:
			if (TextUtils.isEmpty(et_truename.getText().toString())) {
				MyToast.showToast(this, "请输入真实姓名");
				return;
			}
			if (TextUtils.isEmpty(et_usercardnum.getText().toString())) {
				MyToast.showToast(this, "请输入身份证号");
				return;
			}

			if (et_usercardnum.getText().toString().length() < 18) {
				MyToast.showToast(this, "请输入18位身份证号");
				return;
			}
			if (TextUtils.isEmpty(yBitmap) || TextUtils.isEmpty(nBitmap)) {
				MyToast.showToast(this, "请选取相关认证所需图片");
				return;
			}
			String userid = SharePerfUtil.getUserId();
			String jsonData = new Gson().toJson(new AuthData(userid, et_truename.getText()
					.toString().trim(), et_usercardnum.getText().toString()
					.trim(), yBitmap, nBitmap, carBitmap,shouchiBitmap));
//			String jsonData = new Gson().toJson(new AuthData(userid, et_truename.getText()
//			.toString().trim(), et_usercardnum.getText().toString()
//			.trim(), "tu1", "tu2", "tu3"));
			String jsonRequest = new Gson().toJson(new AuthRequest(jsonData));
			showLD("正在提交，请稍候...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(jsonRequest),
					new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							// TODO Auto-generated method stub
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(
											AuthenticationPersonalActivity.this,
											"提交成功");
									finish();
								} else {
									MyToast.showToast(
											AuthenticationPersonalActivity.this,
											arg0.getMsg());
								}
							}
							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
//							MyToast.showToast(
//									AuthenticationPersonalActivity.this,
//									arg0.getMessage());
							cancelLD();
						}
					}));

			break;
		default:
			break;
		}
	}

	class AuthRequest {
		private String operate;
		private String type;
		private String data;

		public AuthRequest(String data) {
			this.operate = "A";
			this.type = "0006";
			this.data = data;
		}
	}

	class AuthData {
		private String userid;
		private String idcard;
		private String idimagefront;
		private String idimagenegative;
		private String driveimage;
		private String name;
		private String idimagebyhander;

		public AuthData(String personid, String truename, String idcard,
				String idimagefront, String idimagenegative, 
				String driveimage,String idimagebyhander) {
			this.userid = personid;
			this.idcard = idcard;
			this.idimagefront = idimagefront;
			this.idimagenegative = idimagenegative;
			this.driveimage = driveimage;
			this.name = truename;
			this.idimagebyhander = idimagebyhander;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticationl_personal);
		init();
		initNavigation();
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

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		if (C.SELECT_PIC_RESULT == resultCode) {
			showLD("正在处理，请稍候...");
			final String imagePath = data.getStringExtra("imagePath");
			new Thread(new Runnable() {

				@Override
				public void run() {
					switch (requestCode) {
					case IV_USERCARD_Y:
						yBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "yBitmap = " + yBitmap);
						break;
					case IV_USERCARD_N:
						nBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "nBitmap = " + nBitmap);
						break;
					case IV_CARCARD:
						carBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "carBitmap = "
								+ carBitmap);
						break;
						
					case IV_SHOUCHICARD:
						shouchiBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "carBitmap = "
								+ carBitmap);
						break;
					default:
						break;
					}
				}
			}).start();

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int requestCode = msg.what;
			String srcPath = (String) msg.obj;
			Bitmap smallBitmap = FileUtils.getSmallBitmap(
					AuthenticationPersonalActivity.this, srcPath);
			switch (requestCode) {
			case IV_USERCARD_Y:
				iv_usercard_y.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IV_USERCARD_N:
				iv_usercard_n.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IV_CARCARD:
				iv_carcard.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IV_SHOUCHICARD:
				iv_shouchicard.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case 12121:
				cancelLD();
				MyToast.showToast(AuthenticationPersonalActivity.this, "图片加载失败");
				break;
			default:
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
		handler.sendEmptyMessage(12121);

	}

}
