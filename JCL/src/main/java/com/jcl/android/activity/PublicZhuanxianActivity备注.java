package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;

/**
 * 发布专线
 * 
 * @author msz
 *
 */
/*public class PublicZhuanxianActivity备注 extends BaseActivity {

	private LinearLayout ll_zhida, ll_zhongzhuan, ll_jingting;
	private ImageView iv_add_zd, iv_add_zz;

	private List<View> zdViewList, zzViewList;
	private HashMap<Integer, List<View>> jtViewList;
	private int zzIndex = 0;
	private HashMap<Integer, View> indexList;

	private Button btn_public;
	private EditText et_company_name, et_name, et_car_num, et_lianxiren,
			et_tel;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_public_zhuanxian);
		zdViewList = new ArrayList<View>();
		zzViewList = new ArrayList<View>();
		jtViewList = new HashMap<Integer, List<View>>();
		indexList = new HashMap<Integer, View>();
		initView();
	}

	private void initView() {
		ll_zhida = (LinearLayout) findViewById(R.id.ll_zhida);
		ll_zhongzhuan = (LinearLayout) findViewById(R.id.ll_zhongzhuan);
		btn_public = (Button) findViewById(R.id.btn_public);
		et_company_name = (EditText) findViewById(R.id.et_company_name);
		et_name = (EditText) findViewById(R.id.et_name);
		et_car_num = (EditText) findViewById(R.id.et_car_num);
		et_lianxiren = (EditText) findViewById(R.id.et_lianxiren);
		et_tel = (EditText) findViewById(R.id.et_tel);

		zdViewList.add(getLayoutInflater().inflate(
				R.layout.child_public_zx_zhida, null));
		zzViewList.add(getLayoutInflater().inflate(
				R.layout.child_public_zx_zhongzhuan, null));
		List<View> cacheList = new ArrayList<View>();
		cacheList.add(getLayoutInflater().inflate(
				R.layout.child_public_zx_jingting, null));
		jtViewList.put(0, cacheList);

		ll_zhida.addView(zdViewList.get(0));
		ll_zhongzhuan.addView(zzViewList.get(0));
		ll_jingting = (LinearLayout) zzViewList.get(0).findViewById(
				R.id.ll_jingting);
		ll_jingting.addView(jtViewList.get(zzIndex).get(0));
		jtViewList.get(0).get(0).findViewById(R.id.iv_add_jingting)
				.setVisibility(View.VISIBLE);
		indexList.put(0,
				jtViewList.get(0).get(0).findViewById(R.id.iv_add_jingting));
		indexList.get(0).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getLayoutInflater().inflate(
						R.layout.child_public_zx_jingting, null);
				jtViewList.get(0).add(view);
				((LinearLayout) zzViewList.get(0)
						.findViewById(R.id.ll_jingting)).addView(view);
			}
		});

		iv_add_zd = (ImageView) zdViewList.get(0).findViewById(R.id.iv_add_zd);
		iv_add_zz = (ImageView) zzViewList.get(0).findViewById(R.id.iv_add_zz);

		iv_add_zd.setVisibility(View.VISIBLE);
		iv_add_zz.setVisibility(View.VISIBLE);

		iv_add_zd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getLayoutInflater().inflate(
						R.layout.child_public_zx_zhida, null);
				zdViewList.add(view);
				ll_zhida.addView(view);
			}
		});
		iv_add_zz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = getLayoutInflater().inflate(
						R.layout.child_public_zx_zhongzhuan, null);
				zzViewList.add(view);
				ll_zhongzhuan.addView(view);
				List<View> cacheList = new ArrayList<View>();
				cacheList.add(getLayoutInflater().inflate(
						R.layout.child_public_zx_jingting, null));
				zzIndex++;
				final int cacheIndex = zzIndex;
				jtViewList.put(zzIndex, cacheList);
				((LinearLayout) zzViewList.get(zzIndex).findViewById(
						R.id.ll_jingting)).addView(jtViewList.get(zzIndex).get(
						0));
				zzViewList.get(zzIndex).findViewById(R.id.ll_jingting)
						.findViewById(R.id.iv_add_jingting)
						.setVisibility(View.VISIBLE);
				indexList.put(cacheIndex, jtViewList.get(cacheIndex).get(0)
						.findViewById(R.id.iv_add_jingting));
				indexList.get(cacheIndex).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								View view = getLayoutInflater()
										.inflate(
												R.layout.child_public_zx_jingting,
												null);
								jtViewList.get(cacheIndex).add(view);
								((LinearLayout) zzViewList.get(cacheIndex)
										.findViewById(R.id.ll_jingting))
										.addView(view);
							}
						});
			}
		});

		btn_public.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(et_company_name.getText().toString()))
				{
					MyToast.showToast(
							PublicZhuanxianActivity备注.this,
							"请填写公司");
					return;
				}
				if(TextUtils.isEmpty(et_lianxiren.getText().toString()))
				{
					MyToast.showToast(
							PublicZhuanxianActivity备注.this,
							"请填写姓名");
					return;
				}
				if(TextUtils.isEmpty(et_name.getText().toString()))
				{
					MyToast.showToast(
							PublicZhuanxianActivity备注.this,
							"请填写联系人");
					return;
				}
				if(TextUtils.isEmpty(et_car_num.getText().toString()))
				{
					MyToast.showToast(
							PublicZhuanxianActivity备注.this,
							"请填写车牌号");
					return;
				}
				if(TextUtils.isEmpty(et_tel.getText().toString()))
				{
					MyToast.showToast(
							PublicZhuanxianActivity备注.this,
							"请填写联系电话");
					return;
				}
				showLD("发布中...");
				List<Line> lineList = new ArrayList<Line>();
				for (int i = 0; i < zdViewList.size(); i++) {
					View view = zdViewList.get(i);
					Line line = new Line("0", ((EditText) view
							.findViewById(R.id.et_zd_left)).getText()
							.toString(), ((EditText) view
							.findViewById(R.id.et_zd_right)).getText()
							.toString(), null);
					lineList.add(line);
				}
				for (int i = 0; i < zzViewList.size(); i++) {
					View view = zzViewList.get(i);
					List<View> cacheList = jtViewList.get(i);
					int cacheSize = cacheList.size();
					String[] cacheJingting = new String[cacheSize];
					for (int j = 0; j < cacheList.size(); j++) {
						cacheJingting[j] = ((EditText) cacheList.get(j)
								.findViewById(R.id.et_jingting)).getText()
								.toString();
					}
					Line line = new Line("1", ((EditText) view
							.findViewById(R.id.et_zz_left)).getText()
							.toString(), ((EditText) view
							.findViewById(R.id.et_zz_right)).getText()
							.toString(), cacheJingting);
					lineList.add(line);
				}
				Data data = new Data(et_company_name.getText().toString(),
						et_name.getText().toString(), et_car_num.getText()
								.toString(), et_lianxiren.getText().toString(),
						et_tel.getText().toString(), lineList, JCLApplication
								.getInstance().getUserId());
				String postStr = new Gson().toJson(new PublicZhuanxianRequest(
						new Gson().toJson(data)));
				executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
						UrlCat.URL_SUBMIT, BaseBean.class, null, ParamsBuilder
								.submitParams(postStr),
						new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								if (arg0 != null) {
									if (TextUtils.equals(arg0.getCode(), "1")) {
										MyToast.showToast(
												PublicZhuanxianActivity备注.this,
												"发布成功");
										finish();
									} else {
										MyToast.showToast(
												PublicZhuanxianActivity备注.this,
												"发布失败");
									}
								}

								cancelLD();
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								cancelLD();
								MyToast.showToast(PublicZhuanxianActivity备注.this,
										"发布失败");
							}
						}));
			}
		});
	}

	class PublicZhuanxianRequest {
		private String operate;
		private String type;
		private String data;

		public PublicZhuanxianRequest(String data) {
			this.operate = "A";
			this.type = "1007";
			this.data = data;
		}
	}

	class Data {
		private String company;// 公司名称 基本信息
		private String name;// 姓名
		private String platenum;// 车牌号
		private String linkman;// 联系人
		private String phone;// 手机号
		private String userid;// 用户编码
		private List<Line> line;// (线路)数组对象

		public Data(String company, String name, String platenum,
				String linkman, String phone, List<Line> line, String userid) {
			this.company = company;
			this.name = name;
			this.platenum = platenum;
			this.linkman = linkman;
			this.phone = phone;
			this.userid = userid;
			this.line = line;

		}
	}

	class Line {
		private String linetype;// 线路类型 0:直达1:中转
		private String start;// 起点
		private String end;// 终点
		private String[] child;// 子节点 如果是中转站，则构建中转路线子数据

		public Line(String linetype, String start, String end, String... child) {
			this.linetype = linetype;
			this.start = start;
			this.end = end;
			this.child = child;
		}
	}
}
*/