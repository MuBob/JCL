package com.jcl.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.PersonalInfoActivity.SetUserinfoRequest;
import com.jcl.android.activity.PersonalInfoActivity.UserinfoData;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.LoginBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.MD5;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 上传回执单
 * 
 * @author xueleilin
 * 
 */
public class AddHuizhidanActivity extends BaseActivity implements OnClickListener,OnImageComparessionListener {
	private static final int IMG_HUIZHIDAN= 0x0001;
	
	private static final int IMG_ERROR = 12121;
	private ImageCompression imageCompression;
	private String goodsid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addhuizhidan);
		Intent intent = getIntent();
		goodsid = intent.getStringExtra("goodsid");
		initNavigation();
		initView();
	}

	private MyUINavigationView uINavigationView;

	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText = uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRightText.setVisibility(View.GONE);
	}
	
	
	private ImageView img_huizhidan;//回执单
	private Button btn_submit;//提交
	
	private void initView()
	{
		imageCompression = ImageCompression.getInstance(this);
		img_huizhidan=(ImageView)findViewById(R.id.img_huizhidan);
		img_huizhidan.setOnClickListener(this);
		btn_submit=(Button)findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);

	}

	
	
	public void setListener()
	{
	}

	class AddHuizhidanRequest {
		private String type;
		private String data;

		public AddHuizhidanRequest(String data) {
			this.type = "3008";
			this.data = data;
		}
	}

	class AddHuizhidanData {
		private String vanuserid;
		private String goodsid;
		private String receiptimage;
		public AddHuizhidanData(String vanuserid,String goodsid,String receiptimage){
			this.vanuserid = vanuserid;
			this.goodsid = goodsid;
			this.receiptimage = receiptimage;
		}
		
	}
	
	String data;
	String jsonRequest;
	
	private String huizhidanBitmap;
	
	String carSize = "";
	
	Intent intent;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			submit();
			break;
		case R.id.img_huizhidan:
			intent = new Intent(AddHuizhidanActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, IMG_HUIZHIDAN);
			break;

		default:
			break;
		}

	}
	//上传回执单
	public void submit()
	{
		if(huizhidanBitmap==null)
		{
			Toast.makeText(AddHuizhidanActivity.this,"请上传回执单",
					1000).show();
			return;
		}
		
		data = new Gson().toJson(new AddHuizhidanData(SharePerfUtil.getUserId(),goodsid,huizhidanBitmap));
		jsonRequest = new Gson().toJson(new AddHuizhidanRequest(data));
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest), new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(AddHuizhidanActivity.this, arg0.getMsg(),
										1000).show();
								finish();
							}else{
								Toast.makeText(AddHuizhidanActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(AddHuizhidanActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(AddHuizhidanActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}
	
	
	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		if (C.SELECT_PIC_RESULT == resultCode) {
//			showLD("正在处理，请稍候...");
			final String imagePath = data.getStringExtra("imagePath");
			new Thread(new Runnable() {

				@Override
				public void run() {
					switch (requestCode) {
					case IMG_HUIZHIDAN:
						huizhidanBitmap = imageCompression.getImage(imagePath,
								requestCode);
						LogUtil.logWrite("ADDCAR_BITMAP", "huizhidanBitmap = " + huizhidanBitmap);
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
					AddHuizhidanActivity.this, srcPath);
			switch (requestCode) {
			case IMG_HUIZHIDAN:
				img_huizhidan.setImageBitmap(smallBitmap);
				cancelLD();
				break;
			case IMG_ERROR:
				cancelLD();
				MyToast.showToast(AddHuizhidanActivity.this, "图片加载失败");
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
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(IMG_ERROR);
	}

}
