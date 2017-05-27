package com.jcl.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.PublicGoodsActivity.Data;
import com.jcl.android.activity.PublicGoodsActivity.PublicGoodsRequest;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.fragment.DetailFindCarsFragment;
import com.jcl.android.fragment.DetailFindDistributionFragment;
import com.jcl.android.fragment.DetailFindGoodsFragment;
import com.jcl.android.fragment.DetailFindLogisticFragment;
import com.jcl.android.fragment.DetailFindStorageFragment;
import com.jcl.android.fragment.DetailZhuanxianFragment;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 在线报价
 * 
 * @author xuelei
 *
 */
public class OfferOnlineActivity extends BaseActivity implements OnClickListener {
	
	private TextView tv_chufadi, tv_mudidi, tv_goods_info, tv_chexing,
	tv_chufashijian, tv_maoyileixing,
	tv_zonglicheng, tv_fukuanfangshi;
	private EditText edt_tel,edt_linkman,edt_baojia;
	private TextView tv_pay_price;
	
	private ImageView iv_back;
	
	private String goodsid;
	private String chufadi;
	private String mudidi;
	private String goodsinfo;
	private String chexing;
	private String chufashijian;
	private String maoyileixing;
	private String zonglichengshu;
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_offer_online_goods);
		int type = getIntent().getIntExtra("type", 0);
		String id = getIntent().getStringExtra("id");
		goodsid = getIntent().getStringExtra("goodsid");
		chufadi = getIntent().getStringExtra("chufadi");
		mudidi = getIntent().getStringExtra("mudidi");
		goodsinfo = getIntent().getStringExtra("goodsinfo");
		chexing = "求"+getIntent().getStringExtra("chexing");
		chufashijian = getIntent().getStringExtra("chufashijian");
		maoyileixing = getIntent().getStringExtra("maoyileixing");
		zonglichengshu = getIntent().getStringExtra("zonglichengshu");
		initView();
		
		
		
		
	}
	public static Intent newInstance(Context context,String goodsid,
			String chufadi,String mudidi,String goodsinfo,String chexing,String chufashijian,String maoyileixing,String zonglichengshu) {
		Intent intent = new Intent(context, DetailFindActivity.class);
		intent.putExtra("goodsid", goodsid);
		intent.putExtra("chufadi", chufadi);
		intent.putExtra("mudidi", mudidi);
		intent.putExtra("goodsinfo", goodsinfo);
		intent.putExtra("chexing", chexing);
		intent.putExtra("chufashijian", chufashijian);
		intent.putExtra("maoyileixing", maoyileixing);
		intent.putExtra("zonglichengshu", zonglichengshu);
		return intent;
	}
	private void initView() {
		tv_chufadi = (TextView) this.findViewById(R.id.tv_chufadi);
		tv_chufadi.setText(chufadi);
		tv_mudidi = (TextView) this.findViewById(R.id.tv_mudidi);
		tv_mudidi.setText(mudidi);
		tv_goods_info = (TextView) this.findViewById(R.id.tv_goods_info);
		tv_goods_info.setText(goodsinfo);
		tv_chexing = (TextView) this.findViewById(R.id.tv_chexing);
		tv_chexing.setText(chexing);
		tv_chufashijian = (TextView) this.findViewById(R.id.tv_chufashijian);
		tv_chufashijian.setText(chufashijian);
		tv_maoyileixing = (TextView) this.findViewById(R.id.tv_maoyileixing);
		tv_maoyileixing.setText(maoyileixing);
		tv_zonglicheng = (TextView) this.findViewById(R.id.tv_zonglicheng);
		tv_zonglicheng.setText(zonglichengshu);
		tv_fukuanfangshi = (TextView) this.findViewById(R.id.tv_fukuanfangshi);
		
		
		edt_tel = (EditText) this.findViewById(R.id.edt_tel);
		edt_tel.setText(SharePerfUtil.getLoginName());
		edt_linkman = (EditText) this.findViewById(R.id.edt_linkman);
		edt_linkman.setText(SharePerfUtil.getLinkMan());
		edt_baojia = (EditText) this.findViewById(R.id.edt_baojia);
		
		tv_pay_price = (TextView) this.findViewById(R.id.tv_pay_price);
		
		tv_pay_price.setOnClickListener(this);
		iv_back = (ImageView) this.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

	}
	
	class OfferOnlineRequest {
		private String operate;
		private String type;
		private String data;

		public OfferOnlineRequest(String data) {
			this.operate = "A";
			this.type = "3001";
			this.data = data;
		}
	}
	
	class BjData {
		private String vanuserid;//车主id
		private String phone;//电话
		private String goodsid;//货源id
		private String price;//报价
		private String linkman;//联系人
		private String submittype;//车主注册类型  企业或者个人
		
		public BjData(String vanuserid,String phone,String goodsid,String price,String linkman,String submittype){
			this.vanuserid = vanuserid;
			this.phone = phone;
			this.goodsid = goodsid;
			this.price = price;
			this.linkman = linkman;
			this.submittype = submittype;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			this.finish();
			break;
		case R.id.tv_pay_price://验证内容为空，发送报价
			if (TextUtils.isEmpty(edt_tel.getText().toString())) {
				MyToast.showToast(this, "请输入联系电话");
				return;
			}
			if (TextUtils.isEmpty(edt_linkman.getText().toString())) {
				MyToast.showToast(this, "请输入联系人");
				return;
			}
			if (TextUtils.isEmpty(edt_baojia.getText().toString())) {
				MyToast.showToast(this, "请输入报价");
				return;
			}
			BjData bjdata = new BjData(JCLApplication.getInstance().getUserId(),edt_tel.getText().toString(),goodsid,edt_baojia.getText().toString(),edt_linkman.getText().toString(),SharePerfUtil.getSubmittype());
			String bjDataJson = new Gson().toJson(bjdata);
			OfferOnlineRequest offeronlineRequest = new OfferOnlineRequest(bjDataJson);
			String postStr = new Gson().toJson(offeronlineRequest);
			showLD("提交中...");
			executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
					UrlCat.URL_SUBMIT, BaseBean.class, null,
					ParamsBuilder.submitParams(postStr),
					new Listener<BaseBean>() {

						@Override
						public void onResponse(BaseBean arg0) {
							if (arg0 != null) {
								if (TextUtils.equals(arg0.getCode(), "1")) {
									MyToast.showToast(OfferOnlineActivity.this,
											"提交成功");
									finish();
								} else {
									MyToast.showToast(OfferOnlineActivity.this,
											"提交失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(OfferOnlineActivity.this, "提交失败");
						}
					}));
			break;
		default:
			break;
	}
	}
		

}
