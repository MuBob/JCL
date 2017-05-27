package com.jcl.android.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.HuidanInfoBean;
import com.jcl.android.bean.PersonalInfoBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.CityPickerPopupwindow;
import com.jcl.android.utils.FileUtils;
import com.jcl.android.utils.LogUtil;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 查看回单
 * 
 * @author xueleilin
 *
 */
public class HuidanInfoActivity extends BaseActivity {

	private MyUINavigationView uINavigationView;
	private ImageView img_huizhidan;//回执单
	private String goodsid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huizhidan_info);
		initNavigation();
		initView();
		Intent intent = this.getIntent();
		goodsid = intent.getStringExtra("goodsid");
		getHuidanInfo();
	}

	private void initView()
	{
		
		img_huizhidan=(ImageView)findViewById(R.id.img_huizhidan);

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

	class HuidaninfoRequest {
		private String filters;
		private String type;
		private String sorts;

		public HuidaninfoRequest(String filters, String sorts) {
			this.filters = filters;
			this.type = "3009";
			this.sorts = sorts;
		}
	}

	class HuidaninfoFilters {
		private String goodsid;

		public HuidaninfoFilters(String goodsid) {
			this.goodsid = goodsid;
		}

	}


	private HuidaninfoRequest userinfoRequest;
	String filters;
	String sorts;
	String jsonRequest;

	public void getHuidanInfo() {
		filters = new Gson().toJson(new HuidaninfoFilters(goodsid));
		jsonRequest = new Gson().toJson(new HuidaninfoRequest(filters, sorts));

		executeRequest(new GsonRequest<HuidanInfoBean>(Request.Method.GET,
				UrlCat.getInfoUrl(jsonRequest), HuidanInfoBean.class,
				null, null, new Listener<HuidanInfoBean>() {
					@Override
					public void onResponse(HuidanInfoBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(HuidanInfoActivity.this, arg0.getMsg(),
										1000).show();
								ImageLoaderUtil.getInstance(HuidanInfoActivity.this)
									.loadImage(C.BASE_URL+"/"+arg0.getData().getReceiptimage(), img_huizhidan);
								
							}else{
								Toast.makeText(HuidanInfoActivity.this, arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(HuidanInfoActivity.this, "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(HuidanInfoActivity.this, "网络连接异常！",
								1000).show();
					}
				}));
	}

	

}
