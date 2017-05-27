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
import com.jcl.android.activity.OfferOnlineActivity.BjData;
import com.jcl.android.activity.OfferOnlineActivity.OfferOnlineRequest;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 对车主评价
 * 
 * @author xueleilin
 *
 */
public class EvaluteActivity extends BaseActivity implements OnClickListener {

	private MyUINavigationView uINavigationView;
	private RatingBar mRatingBar;
	private EditText edt_evalute;
	private String goodsid;
	private Button btn_evalute;
	private ImageView iv_back;
	private TextView tv_count;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evalute);
		initNavigation();
		initView();
		Intent intent = this.getIntent();
		goodsid = intent.getStringExtra("goodsid");
	}

	private void initView()
	{
		
		mRatingBar=(RatingBar) findViewById(R.id.ratingBar);
		edt_evalute = (EditText)findViewById(R.id.edt_evalute);
		mRatingBar.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());
	    
		btn_evalute = (Button) this.findViewById(R.id.btn_evalute);
	    btn_evalute.setOnClickListener(this);
	    tv_count = (TextView)this.findViewById(R.id.tv_count);
//		iv_back = (ImageView) this.findViewById(R.id.iv_back);
//		iv_back.setOnClickListener(this);

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
	
	class EvaluteRequest {
		private String operate;
		private String type;
		private String data;

		public EvaluteRequest(String data) {
			this.operate = "A";
			this.type = "5001";
			this.data = data;
		}
	}
	
	class EvaluteData {
		private String goodsid;//货源id
		private String evalutescore;
		private String evaluteremark;
		
		public EvaluteData(String goodsid,String evalutescore,String evaluteremark){
			this.goodsid = goodsid;
			this.evalutescore = evalutescore;
			this.evaluteremark = evaluteremark;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.btn_evalute://验证内容为空，发送报价
			
			//得到RatingBar现在的等级
		    float currentRating=mRatingBar.getRating();
		   
		   
			if (TextUtils.isEmpty(edt_evalute.getText().toString())) {
				MyToast.showToast(this, "请输入评价内容");
				return;
			}
			EvaluteData evaluteData = new EvaluteData(goodsid,currentRating+"",edt_evalute.getText().toString());
			String evaluteDataJson = new Gson().toJson(evaluteData);
			EvaluteRequest evaluteRequest = new EvaluteRequest(evaluteDataJson);
			String postStr = new Gson().toJson(evaluteRequest);
			showLD("提交中...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(EvaluteActivity.this,
											"提交成功");
									finish();
								} else {
									MyToast.showToast(EvaluteActivity.this,
											"提交失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(EvaluteActivity.this, "提交失败");
						}
					}));
			break;
		default:
			break;
	}
	}

	
	
	  //注意onRatingChanged方法中的最后一个参数boolean fromUser:
	  //若是由用户触摸手势或方向键轨迹球移动触发RatingBar的等级改变,返回true
	  //若是由编程触发RatingBar的等级改变,返回false
	  private class RatingBarChangeListenerImpl implements OnRatingBarChangeListener{
	    @Override
	    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
	      tv_count.setText(rating+"分");
	      System.out.println("现在的等级为 rating="+rating+",是否是用户触发 fromUser="+fromUser);
	    }
	    
	  }

	

}
