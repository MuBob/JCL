package com.jcl.android.activity;

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
import com.jcl.android.bean.CollectNumBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 我的收藏主页面
 * 
 * @author msz
 *
 */
public class MyCollectActivity extends BaseActivity implements OnClickListener {

	private TextView tv_goods_num, tv_car_num, tv_zhuanxian_num,
			tv_gjyunjia_num, tv_gjxianlu_num, tv_carmm_num, tv_other_num;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_collect);
		initNavigation();
		initView();
		getCollectNum();
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
	class GetStr {
		private String type;// 对应表名
		private String filters;// 过滤条件
		public GetStr(String filters) {
			this.filters = filters;
			this.type = "2014";
		}

	}
	class filters 
	{
		private String userid;
		public filters(String userid)
		{
			this.userid=userid;
		}
	}
	
	String beforNum="已收藏";
	String afterNum="条信息";
	
	private void getCollectNum()
	{

		
		String filters=new Gson().toJson(new filters(JCLApplication.getInstance().getUserId()));
		String getStr = new Gson().toJson(new GetStr( filters));
		executeRequest(new GsonRequest<CollectNumBean>(Request.Method.POST,
				UrlCat.URL_SEARCH, CollectNumBean.class, null,
				ParamsBuilder.getStrParams(getStr),
				new Listener<CollectNumBean>() {

					@Override
					public void onResponse(CollectNumBean arg0) {
						// 清除刷新小图标
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								for (int i = 0; i < arg0.getData().getTypes().size(); i++) {
									if(TextUtils.equals(arg0.getData().getTypes().get(i).getType(),"1"))
									{
										tv_car_num.setText(
											beforNum+arg0.getData().getTypes().get(i).getCount()+afterNum);
									}
									if(TextUtils.equals(arg0.getData().getTypes().get(i).getType(),"2"))
									{
										tv_goods_num.setText(
												beforNum+arg0.getData().getTypes().get(i).getCount()+afterNum);
									}
									if(TextUtils.equals(arg0.getData().getTypes().get(i).getType(),"3"))
									{
										tv_other_num.setText(
												beforNum+arg0.getData().getTypes().get(i).getCount()+afterNum);
									}
									if(TextUtils.equals(arg0.getData().getTypes().get(i).getType(),"4"))
									{
										tv_zhuanxian_num.setText(
												beforNum+arg0.getData().getTypes().get(i).getCount()+afterNum);
									}
								}
							} else {
								MyToast.showToast(MyCollectActivity.this, "服务端异常");
							}
						} else {
							MyToast.showToast(MyCollectActivity.this, "服务端异常");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
//						MyToast.showToast(MyCollectActivity.this, arg0.getMessage());
					}
				}));
	}

	private void initView() {
		findViewById(R.id.ll_collect_goods).setOnClickListener(this);
		findViewById(R.id.ll_collect_car).setOnClickListener(this);
		findViewById(R.id.ll_collect_other).setOnClickListener(this);
		findViewById(R.id.ll_collect_zhuanxian).setOnClickListener(this);

		tv_goods_num = (TextView) findViewById(R.id.tv_goods_num);
		tv_car_num = (TextView) findViewById(R.id.tv_car_num);
		tv_zhuanxian_num = (TextView) findViewById(R.id.tv_zhuanxian_num);
		tv_gjyunjia_num = (TextView) findViewById(R.id.tv_gjyunjia_num);
		tv_gjxianlu_num = (TextView) findViewById(R.id.tv_gjxianlu_num);
		tv_carmm_num = (TextView) findViewById(R.id.tv_carmm_num);
		tv_other_num = (TextView) findViewById(R.id.tv_other_num);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_collect_goods:
			startActivity(CollectChildActivity.newInstance(this,
					C.INTENT_TYPE_COLLECT_GOODS));
			break;
		case R.id.ll_collect_car:
			startActivity(CollectChildActivity.newInstance(this,
					C.INTENT_TYPE_COLLECT_CAR));
			break;
			
		case R.id.ll_collect_other:
			startActivity(CollectChildActivity.newInstance(this,
					C.INTENT_TYPE_COLLECT_OTHER));
			break;
		case R.id.ll_collect_zhuanxian:
			startActivity(CollectChildActivity.newInstance(this,
					C.INTENT_TYPE_COLLECT_ZHUANXIAN));
			break;

		default:
			break;
		}

	}
}
