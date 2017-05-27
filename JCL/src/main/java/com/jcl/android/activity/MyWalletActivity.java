package com.jcl.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.BundAlipayActivity.BundAlipayRequest;
import com.jcl.android.activity.BundAlipayActivity.Data;
import com.jcl.android.activity.PersonalInfoActivity.UserinfoFilters;
import com.jcl.android.activity.PersonalInfoActivity.UserinfoRequest;
import com.jcl.android.activity.PersonalInfoActivity.UserinfoSorts;
import com.jcl.android.alipay.PayResult;
import com.jcl.android.alipay.PayUtils;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.PersonalInfoBean;
import com.jcl.android.bean.UserAccountBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.ChonzhiOnlinePopupwindow;
import com.jcl.android.popupwindow.TixianPopupwindow;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

/**
 * 我的钱包
 * 
 * @author xueleilin
 *
 */
public class MyWalletActivity extends BaseActivity implements
		OnClickListener {
	
	private TextView tv_chongzhi,tv_tixian,tv_chongzhijilu,tv_tixianjilu,tv_xiaofeijilu,tv_shourujilu,tv_bundalipay,tv_myyue;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_mywallet);
		initView();
		initNavigation();
		loadUserAccount();
	}

	private void initView() {
		tv_myyue = (TextView) findViewById(R.id.tv_myyue);
		findViewById(R.id.tv_chongzhi).setOnClickListener(this);
		findViewById(R.id.tv_tixian).setOnClickListener(this);
		findViewById(R.id.tv_chongzhijilu).setOnClickListener(this);
		findViewById(R.id.tv_tixianjilu).setOnClickListener(this);
		findViewById(R.id.tv_xiaofeijilu).setOnClickListener(this);
		findViewById(R.id.tv_shourujilu).setOnClickListener(this);
		findViewById(R.id.tv_bundalipay).setOnClickListener(this);
	}
	private MyUINavigationView uINavigationView;
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView)findViewById(R.id.action_bar);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_chongzhi:
			new ChonzhiOnlinePopupwindow(MyWalletActivity.this, v, mHandler, null).show();
			break;
		case R.id.tv_tixian:
			new TixianPopupwindow(MyWalletActivity.this, v, mHandler, null).show();
			break;
		case R.id.tv_chongzhijilu:
			startActivity(new Intent(this, TradingRecordActivity.class));
			break;
		case R.id.tv_tixianjilu:
			startActivity(new Intent(this, TradingRecordActivity.class));
			break;
		case R.id.tv_xiaofeijilu:
			startActivity(new Intent(this, TradingRecordActivity.class));
			break;
		case R.id.tv_shourujilu:
			startActivity(new Intent(this, TradingRecordActivity.class));
			break;
		case R.id.tv_bundalipay:
			startActivity(new Intent(this, BundAlipayActivity.class));
			break;
		default:
			break;
		}

	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: //确认充值
				String jine = (String) msg.obj;
				//调用支付宝支付
				new PayUtils().pay(MyWalletActivity.this, mHandler, jine,"0",null);
				break;
			case 1://支付宝支付返回
                PayResult payResult = new PayResult((String) msg.obj);
				
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(MyWalletActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					loadUserAccount();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(MyWalletActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(MyWalletActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			case 5: //确认提现
				String tixianjine = (String) msg.obj;
				//调用提交提现申请
				tixianApply(tixianjine);
				
				break;
			}}};
			//提现申请
			class TixianApplyRequest {
				private String operate;
				private String type;
				private String data;

				public TixianApplyRequest(String data) {
					this.operate = "A";
					this.type = "4005";
					this.data = data;
				}
			}

			class Data {
				private String price;// 金额　
				private String userid;// 用户编码 　

				public Data(String price, String userid) {
					this.price = price;
					this.userid = userid;

				}
			}
			public void tixianApply(String jine){
				showLD("提交中...");
				Data data = new Data( jine, JCLApplication.getInstance().getUserId());
				String postStr = new Gson().toJson(new TixianApplyRequest(
						new Gson().toJson(data)));
				executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
						UrlCat.URL_SUBMIT, BaseBean.class, null,
						ParamsBuilder.submitParams(postStr),
						new Listener<BaseBean>() {
							@Override
							public void onResponse(BaseBean arg0) {
								if (arg0 != null) {
									if (TextUtils.equals(arg0.getCode(), "1")) {
										MyToast.showToast(MyWalletActivity.this,
												"提交成功");
										loadUserAccount();
									} else {
										MyToast.showToast(MyWalletActivity.this,
												"提交失败");
									}
								}

								cancelLD();
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								cancelLD();
								MyToast.showToast(MyWalletActivity.this, "提交失败");
							}
						}));
			}
			//账户金额
			class UserAccountRequest {
				private String filters;
				private String type;

				public UserAccountRequest(String filters) {
					this.filters = filters;
					this.type = "4002";
				}
			}

			class UserAccountFilters {
				private String userid;

				public UserAccountFilters(String userid) {
					this.userid = userid;
				}

			}
			private UserinfoRequest userinfoRequest;
			String filters;
			String sorts;
			String jsonRequest;
			/**
			 * 获取用户资金账户
			 */
			public void loadUserAccount(){
				filters = new Gson().toJson(new UserAccountFilters(SharePerfUtil
						.getUserId()));
				jsonRequest = new Gson().toJson(new UserAccountRequest(filters));

				executeRequest(new GsonRequest<UserAccountBean>(Request.Method.GET,
						UrlCat.getInfoUrl(jsonRequest), UserAccountBean.class,
						null, null, new Listener<UserAccountBean>() {
							@Override
							public void onResponse(UserAccountBean arg0) {
								if (arg0 != null) {
									tv_myyue.setText(arg0.getData().getPrice());
								} else {
									Toast.makeText(MyWalletActivity.this, "暂无数据！",
											1000).show();
								}
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								Toast.makeText(MyWalletActivity.this, "网络连接异常！",
										1000).show();
							}
						}));
			}
			
}
