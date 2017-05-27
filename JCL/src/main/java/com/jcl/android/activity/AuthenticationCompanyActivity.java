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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 企业认证
 * 
 * @author pb
 *
 */
public class AuthenticationCompanyActivity extends BaseActivity implements
		OnClickListener, OnImageComparessionListener {

	private MyUINavigationView uINavigationView;

	protected String imagePath;
	private static final int IV_YYZZ = 0x0004;
	private static final int IV_ZHJG = 0x0005;
	private static final int IV_SWDJ = 0x0006;
	private static final int IV_DLXK = 0x0007;

	ImageView iv_yyzz, iv_zhjg, iv_swdj, iv_dlxk;
	private String yyzzBitmap, zhjgBitmap, swdjBitmap, dlxkBitmap;
	private ImageCompression imageCompression;
	private Button btn_commit;

	protected void init() {
		iv_yyzz = (ImageView) findViewById(R.id.iv_yyzz);
		iv_zhjg = (ImageView) findViewById(R.id.iv_zhjg);
		iv_swdj = (ImageView) findViewById(R.id.iv_swdj);
		iv_dlxk = (ImageView) findViewById(R.id.iv_dlxk);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		iv_yyzz.setOnClickListener(this);
		iv_zhjg.setOnClickListener(this);
		iv_swdj.setOnClickListener(this);
		iv_dlxk.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		imageCompression = ImageCompression.getInstance(this);
	}

	Intent intent;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_yyzz:
			intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, IV_YYZZ);
			break;
		case R.id.iv_zhjg:
			intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, IV_ZHJG);
			break;
		case R.id.iv_swdj:
			intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, IV_SWDJ);
			break;
		case R.id.iv_dlxk:
			intent = new Intent(this, SelectPicActivity.class);
			startActivityForResult(intent, IV_DLXK);
			break;
		case R.id.btn_commit:

			if (TextUtils.isEmpty(yyzzBitmap) || TextUtils.isEmpty(zhjgBitmap)
					|| TextUtils.isEmpty(swdjBitmap)
					|| TextUtils.isEmpty(dlxkBitmap)) {
				MyToast.showToast(this, "请选取相关认证所需图片");
				return;
			}
			String jsonData = new Gson().toJson(new AuthData(JCLApplication
					.getInstance().getUserId(), zhjgBitmap, yyzzBitmap,
					dlxkBitmap, swdjBitmap));
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
											AuthenticationCompanyActivity.this,
											"提交成功");
									finish();
								} else {
									MyToast.showToast(
											AuthenticationCompanyActivity.this,
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
//									AuthenticationCompanyActivity.this,
//									arg0.getMessage());
							cancelLD();
						}
					}));

			break;
		default:
			break;
		}
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
					case IV_YYZZ:
						yyzzBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "yBitmap = "
								+ yyzzBitmap);
						break;
					case IV_DLXK:
						dlxkBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "nBitmap = "
								+ dlxkBitmap);
						break;
					case IV_SWDJ:
						swdjBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "carBitmap = "
								+ swdjBitmap);
						break;
					case IV_ZHJG:
						zhjgBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("AUTH_BITMAP", "carBitmap = "
								+ zhjgBitmap);
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
					AuthenticationCompanyActivity.this, srcPath);
			switch (requestCode) {
			case IV_YYZZ:
				iv_yyzz.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IV_DLXK:
				iv_dlxk.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IV_SWDJ:
				iv_swdj.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IV_ZHJG:
				iv_zhjg.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case 12121:
				cancelLD();
				MyToast.showToast(AuthenticationCompanyActivity.this, "图片加载失败");
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticationl_company);
		initNavigation();
		init();
	}

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		uINavigationView.getTv_title().setText("个人认证");
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	class AuthRequest {
		private String operate;
		private String type;
		private String data;

		public AuthRequest(String data) {
			this.operate = "A";
			this.type = "0007";
			this.data = data;
		}
	}

	class AuthData {
		private String userid;
		private String zzjgdmz;
		private String yyzz;
		private String dlysxkz;
		private String swdjz;

		/**
		 * 
		 * @param userid
		 * @param zzjgdmz
		 * @param yyzz
		 * @param dlysxkz
		 * @param swdjz
		 */
		public AuthData(String userid, String zzjgdmz, String yyzz,
				String dlysxkz, String swdjz) {
			this.userid = userid;
			this.zzjgdmz = zzjgdmz;
			this.yyzz = yyzz;
			this.dlysxkz = dlysxkz;
			this.swdjz = swdjz;
		}
	}

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
