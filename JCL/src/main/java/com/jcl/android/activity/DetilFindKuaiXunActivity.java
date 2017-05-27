package com.jcl.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.DetilFindOtherBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;

public class DetilFindKuaiXunActivity extends BaseActivity implements
     OnClickListener{

	private ImageView iv_share, iv_collect, iv_back;
	private TextView tv_title, tv_info;
	private Bundle bundle;
	private String info,title,tel;
	private Button btn_tel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail_kuaixun);
		initView();
			}

	private void initView() {
		Intent intent = getIntent();
		bundle = intent.getExtras();
//		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_back = (ImageView) findViewById(R.id.iv_back);
//		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_info = (TextView) findViewById(R.id.tv_info);
		btn_tel = (Button) findViewById(R.id.btn_tel);

		info = bundle.getString("info");
		title = bundle.getString("title");
		tel = bundle.getString("tel");
//		Log.e("title", title);
//		Log.e("info", info);

		tv_info.setText(info);
        tv_title.setText(title);
		
//		iv_share.setOnClickListener(this);
//		iv_collect.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		btn_tel.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_share:
			// if(sharePopupwindow!=null&&!sharePopupwindow.isShowing())
			// sharePopupwindow.show();
			break;
		case R.id.btn_tel:
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel));  
            startActivity(intent);  
		    break;
		case R.id.iv_back:
			finish();
			break;
//		case R.id.iv_collect:
//			if (!isChecked) {
//				String data = new Gson().toJson(new Data(goodsId));
//				String getStr = new Gson()
//						.toJson(new DetailGoodsCollectRequest(data, "A"));
//				showLD("收藏中...");
//				executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
//						UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
//						null, null, new Listener<BaseBean>() {
//
//							@Override
//							public void onResponse(BaseBean arg0) {
//								cancelLD();
//								if (arg0 != null) {
//									if (TextUtils.equals(arg0.getCode(), "1")) {
//										MyToast.showToast(getActivity(), "收藏成功");
//										isChecked = true;
//										iv_collect
//												.setImageResource(R.drawable.icon_toorbar_collect_checked);
//									}
//								}
//
//							}
//						}, new ErrorListener() {
//
//							@Override
//							public void onErrorResponse(VolleyError arg0) {
//								// TODO Auto-generated method stub
//								cancelLD();
//							}
//						}));
//			} else {
//				String data = new Gson().toJson(new Data(goodsId));
//				String getStr = new Gson()
//						.toJson(new DetailGoodsCollectRequest(data, "R"));
//				showLD("取消收藏中...");
//				executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
//						UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
//						null, null, new Listener<BaseBean>() {
//
//							@Override
//							public void onResponse(BaseBean arg0) {
//								cancelLD();
//								if (arg0 != null) {
//									if (TextUtils.equals(arg0.getCode(), "1")) {
//										MyToast.showToast(getActivity(),
//												"取消收藏成功");
//										iv_collect
//												.setImageResource(R.drawable.icon_toolbar_collect_nomal);
//										isChecked = false;
//									}
//								}
//
//							}
//						}, new ErrorListener() {
//
//							@Override
//							public void onErrorResponse(VolleyError arg0) {
//								// TODO Auto-generated method stub
//								cancelLD();
//							}
//						}));
//			}
//
//			break;

		default:
			break;
		}

	}

}
