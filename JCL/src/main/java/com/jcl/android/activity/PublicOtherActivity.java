package com.jcl.android.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.jcl.android.bean.OtherListBean;
import com.jcl.android.bean.OtherListBean.OtherInfo;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.ImageCompression;
import com.jcl.android.utils.ImageCompression.OnImageComparessionListener;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.WhSpinner;
/**
 * 发布其他
 * @author msz
 *
 */
public class PublicOtherActivity extends BaseActivity implements
		OnClickListener,OnImageComparessionListener{

	private WhSpinner ws_fabuleibie, ws_yewuleibie;
	private List<WhSpinner.Item> fabuleibie;
	private List<WhSpinner.Item> yewuleibie;
	private EditText et_public_faq, et_public_info;
	private Button btn_public;
	private OtherListBean.OtherInfo info;
	private String key;
	private ImageView iv_back,iv_other_picone;
	private boolean isChecked;
	private String otherBitmap1;
	private ImageCompression imageCompression;
	private static final int IMG_ERROR = 12121;
    

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_public_other);
		initView();
		imageCompression = ImageCompression.getInstance(this);
		initWhSpinnerData();
		initConent();
	}

	public void initWhSpinnerData() {
		fabuleibie = new ArrayList<WhSpinner.Item>();
		yewuleibie = new ArrayList<WhSpinner.Item>();
		String[] fabuleibies = getResources().getStringArray(
				R.array.fabuleibie);
		for (int i = 0; i < fabuleibies.length; i++) {
			fabuleibie.add(new WhSpinner.Item(fabuleibies[i], i));
		}
		ws_fabuleibie.setItems(fabuleibie, 0);
		String[] yewuleibies = getResources().getStringArray(
				R.array.yewuleibie);
		for (int i = 0; i < yewuleibies.length; i++) {
			yewuleibie.add(new WhSpinner.Item(yewuleibies[i], i));
		}
		ws_yewuleibie.setItems(yewuleibie, 0);
	}

	private void initView() {
		// TODO Auto-generated method stub
		ws_fabuleibie = (WhSpinner) findViewById(R.id.ws_fabuleibie);
		ws_yewuleibie = (WhSpinner) findViewById(R.id.ws_yewuleibie);
		et_public_faq = (EditText) findViewById(R.id.et_public_faq);
		et_public_info = (EditText) findViewById(R.id.et_public_info);
		btn_public = (Button) findViewById(R.id.btn_public);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_other_picone = (ImageView) findViewById(R.id.iv_other_picone);
		btn_public.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_other_picone.setOnClickListener(this);
		otherBitmap1 = "";
	}
	
	void initConent() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null && bundle.getString("type").equals("3")) {
			btn_public.setVisibility(View.GONE);
			et_public_faq.setFocusable(false);
			et_public_info.setFocusable(false);
			ws_fabuleibie.setEnabled(false);
			ws_yewuleibie.setEnabled(false);
			
		}
		info = (OtherInfo) getIntent()
				.getSerializableExtra("info");
		if (info == null) {
			key = "";
			return;
		}else{
		key = info.get_id();
		et_public_faq.setText(info.getDescription());//标题
		et_public_info.setText(info.getSaydetal());//详情
//		int k = 0,m= 0;
//		for (int i = 0; i < fabuleibie.size(); i++) {
//			if (fabuleibie.get(i).key.equals(info.getFbtype())) {
//				k = i;
//				break;
//			}
//		}
		ws_fabuleibie.setItems(fabuleibie, Integer.valueOf(info.getFbtype()));
		
//		for (int i = 0; i < yewuleibie.size(); i++) {
//			if (yewuleibie.get(i).key.equals(info.getYwtype())) {
//				m = i;
//				break;
//			}
//		}
		ws_yewuleibie.setItems(yewuleibie, Integer.valueOf(info.getYwtype()));
		
		ImageLoaderUtil.getInstance(
				PublicOtherActivity.this).loadImage(
				C.BASE_URL+info.getOther_image(), iv_other_picone);
		}
	}

	class PublicOtherRequest {
		private String operate;
		private String type;
		private String data;
		private String key;

		public PublicOtherRequest(String data) {
				//发布
			this.operate = "A";
			this.type = "1010";
			this.data = data;
		}
		
		public PublicOtherRequest(String data,String key) {
				//修改
			this.operate = "M";
			this.type = "1010";
			this.data = data;
			this.key = key;
		}
	}

	class Key{
		private String _id;
		public Key(String _id){
			this._id = _id;
		}
	}
	class Data {
		private String fbtype;// 发布类别 　
		private String ywtype;// 业务类别 　
		private String description;// 描述 　
		private String saydetal;// 具体需求 　
		private String status;// 发布状态 0关,1开
		private String createtime;// 创建时间 　
		private String effectivetime;// 有效时间 　
		private String effectiveflag;// 有效flag 1有效、0无效
		private String userid;// 用户编码 　
		private String other_image;

		public Data(String fbtype, String ywtype, String description,
				String saydetal, String userid,String other_image) {
			this.fbtype = fbtype;
			this.ywtype = ywtype;
			this.description = description;
			this.saydetal = saydetal;
			this.status = "";
			this.createtime = "";
			this.effectiveflag = "";
			this.effectivetime = "";
			this.userid = userid;
			this.other_image =other_image;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.iv_other_picone:
//			Intent intent2 = new Intent(this, SelectPicActivity.class);
//			startActivityForResult(intent2, 2);
			Intent intent = new Intent(PublicOtherActivity.this,
					SelectPicActivity.class);
			startActivityForResult(intent, 111);
			break;
			
		case R.id.btn_public:
			
			String publicinfo = "",faq = "";
			if (et_public_info.getText().toString().length() < 4) {
				MyToast.showToast(PublicOtherActivity.this, "最少输入四个字");
				return;
			} else {
				publicinfo = et_public_info.getText().toString();
			}
			
			if (et_public_faq.getText().toString().length() < 4) {
				MyToast.showToast(PublicOtherActivity.this, "最少输入四个字");
				return;
			} else {
				faq = et_public_faq.getText().toString();
			showLD("发布中...");
			Data data = new Data(ws_fabuleibie.getChoicePosition()+"",
					ws_yewuleibie.getChoicePosition()+"", faq , publicinfo, JCLApplication.getInstance()
							.getUserId(),otherBitmap1);

			String postStr = "";
			String data1 = new Gson().toJson(data);
			if (info == null) {
				postStr = new Gson().toJson(new PublicOtherRequest(data1));
			} else {
//				Key key = new Key(info.get_id());
//			    postStr = new Gson().toJson(new PublicOtherRequest(
//						new Gson().toJson(data),new Gson().toJson(key)));
				String key = info.get_id();
			    postStr = new Gson().toJson(new PublicOtherRequest(data1,key));
			}
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(PublicOtherActivity.this,
											"发布成功");
									finish();
								} else {
									MyToast.showToast(PublicOtherActivity.this,
											"发布失败");
								}
							}
							cancelLD();
						}
					}, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(PublicOtherActivity.this, "发布失败");
						}
					}));
			}
			break;

		case R.id.iv_back:
			finish();
			break;
			
				default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
		if (C.SELECT_PIC_RESULT == resultCode) {
//			showLD("正在处理，请稍候...");
			final String imagePath = data.getStringExtra("imagePath");
			new Thread(new Runnable() {
				@Override
				public void run() {
					switch (requestCode) {
					case 111:
						otherBitmap1 = imageCompression.getImage(imagePath,
								requestCode);
						
						break;

					}
				}
			}).start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	String srcPath;
	Bitmap smallBitmap;
	String lnt,lat;
	int requestCode;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = null;
			String info = "";
			if (msg.obj instanceof Bundle) {
				bundle = (Bundle) msg.obj;
				if (bundle != null)
					info = bundle.getString("cityName");
			} else {
				info = (String) msg.obj;
			}
			int requestCode = msg.what;
				switch (requestCode) {
				case 111:
					srcPath = (String) msg.obj;
					smallBitmap = FileUtils.getSmallBitmap(
							PublicOtherActivity.this, srcPath);
					iv_other_picone.setImageBitmap(smallBitmap);
					cancelLD();
					break;
			}
				}
	};
	
	@Override
	public void onSuccess(String srcPath, int requestCode) {
		Message msg = handler.obtainMessage();
		msg.what = requestCode;
		msg.obj = srcPath;
		handler.sendMessage(msg);
	}


	@Override
	public void onFaild(String srcPath, int requestCode) {
		handler.sendEmptyMessage(IMG_ERROR);
	}
	
	class DetailGoodsCollectRequest {
		private String filters;
		private String type;
		private String operate;
		private String data;

		public DetailGoodsCollectRequest(String data,String operate) {
			this.data = data;
			this.operate = operate;
			this.type = "1011";
		}
	}
	
	class CollectData {
		private String userid;
		private String type;// 收藏类型 1车源2货源 可扩展
		private String ppid;// 目标ID 比如车源收藏，则是车源ID

		public CollectData() {
			this.type = "3";
			this.ppid = key;
			this.userid = JCLApplication.getInstance().getUserId();
		}

	}
	
}
