package com.jcl.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.GoodsListBean;
import com.jcl.android.bean.GoodsListBean.GoodsInfo;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.jcl.android.view.WhSpinner;

/**
 * 添加货物
 * 
 * @author msz
 * 
 */
public class AddGoodsActivity extends BaseActivity implements OnClickListener {

	//et_company_name
	private EditText et_po, et_huowuzhongliang, et_huowutiji, et_chaochicun,
			et_jianshu, et_pinming,et_guixingguiliang;
	private WhSpinner ws_maoyileixing, ws_baozhuangfangshi,ws_dun;
	private CheckBox cb_changqihuoyuan;
	private Button btn_commit, btn_next;
	private List<WhSpinner.Item> maoyileixing;
	private List<WhSpinner.Item> baozhuangfangshi;
	private GoodsListBean.GoodsInfo info;
	private List<WhSpinner.Item> dunlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addgoods);
		initNavigation();
		initView();
		initWhSpinnerData();
		initConent();
	}

	public void initWhSpinnerData() {
		maoyileixing = new ArrayList<WhSpinner.Item>();
		dunlist = new ArrayList<WhSpinner.Item>();
		baozhuangfangshi = new ArrayList<WhSpinner.Item>();
		maoyileixing.add(new WhSpinner.Item("出口", "0"));
		maoyileixing.add(new WhSpinner.Item("进口", "1"));
		maoyileixing.add(new WhSpinner.Item("内贸", "2"));
		ws_maoyileixing.setItems(maoyileixing, 0);

		baozhuangfangshi.add(new WhSpinner.Item("纸箱", "0"));
		baozhuangfangshi.add(new WhSpinner.Item("托盘", "1"));
		baozhuangfangshi.add(new WhSpinner.Item("卷", "2"));
		baozhuangfangshi.add(new WhSpinner.Item("液袋", "3"));
		baozhuangfangshi.add(new WhSpinner.Item("其他包装", "4"));
		ws_baozhuangfangshi.setItems(baozhuangfangshi, 0);
		
		String[] duns = getResources().getStringArray(
				R.array.dungongjin);
		for (int i = 0; i < duns.length; i++) {
			dunlist.add(new WhSpinner.Item(duns[i], i));
		}
		ws_dun.setItems(dunlist, 0);
	}

	private void initView() {
		et_po = (EditText) findViewById(R.id.et_po);
		et_huowuzhongliang = (EditText) findViewById(R.id.et_huowuzhongliang);
		et_huowutiji = (EditText) findViewById(R.id.et_huowutiji);
		et_chaochicun = (EditText) findViewById(R.id.et_chaochicun);
		et_jianshu = (EditText) findViewById(R.id.et_jianshu);
//		et_company_name = (EditText) findViewById(R.id.et_company_name);
		et_pinming = (EditText) findViewById(R.id.et_pinming);
		et_guixingguiliang = (EditText) findViewById(R.id.et_guixingguiliang);
		ws_baozhuangfangshi = (WhSpinner) findViewById(R.id.ws_baozhuangfangshi);
		ws_dun = (WhSpinner) findViewById(R.id.ws_dun);
		ws_maoyileixing = (WhSpinner) findViewById(R.id.ws_maoyileixing);
		cb_changqihuoyuan = (CheckBox) findViewById(R.id.cb_changqihuoyuan);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_commit.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		cb_changqihuoyuan.setVisibility(View.GONE);
	}

	void initConent() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null && bundle.getString("type").equals("2")){
			btn_next.setVisibility(View.GONE);
		}
			
		info = (GoodsInfo) getIntent()
				.getSerializableExtra("info");
		if (info == null) {
			return;
		}
		et_po.setText(info.getPonum());
		et_pinming.setText(info.getDetailname());
//		int k = 0;
//		for (int i = 0; i < maoyileixing.size(); i++) {
//			if (maoyileixing.get(i).val.equals(info.getMytype())) {
//				k = i;
//				break;
//			}
//		}
		ws_maoyileixing.setItems(maoyileixing, Integer.parseInt(info.getMytype()));
		
//		int m = 0;
//		for (int i = 0; i < baozhuangfangshi.size(); i++) {
//			if (baozhuangfangshi.get(i).val.equals(info.getBztype())) {
//				m = i;
//				break;
//			}
//		}
		ws_baozhuangfangshi.setItems(baozhuangfangshi, Integer.parseInt(info.getBztype()));
		
		et_huowuzhongliang.setText(info.getGoodsweight());
		et_huowutiji.setText(info.getGoodstj());
		et_jianshu.setText(info.getNum());
		et_chaochicun.setText(info.getMaxgoodssize());
		et_guixingguiliang.setText(info.getGxgl());
//		et_company_name.setText(info.getCompanyname());
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
				finish();
			}
		});
		btnRightText.setVisibility(View.GONE);
	}

	class AddGoodsRequest {
		private String type;
		private String data;
		private String operate;
		private String key;

		public AddGoodsRequest(String data) {
			if (info == null) {
				this.operate = "A";
			}else{
				this.operate = "M";
				this.key = info.get_id();
			}
			
			this.type = "1000";
			this.data = data;
		}
	}

	class AddGoodsData {
		private String userid;
		private String companyname;
		private String ponum;// PO# 　
		private String mytype;// 贸易类型 　出口0，进口1，内贸2
		private String detailname;// 具体品名 　
		private String bztype;// 包装方式 　纸箱0，托盘1，卷2，液袋3，其他包装4
		private String num;// 件数 　
		private String goodsweight;// 货物总重量 　
		private String goodstj;// 货物总体积 　　
		private String maxgoodssize;// 超长尺寸货物 
		private String gxgl;//柜型柜量　
		private String unit;

		public AddGoodsData(String userid, String companyname, String ponum,
				String mytype, String detailname, String bztype, String num,
				String goodsweight, String goodstj, String maxgoodssize,
				String gxgl,String unit) {
			this.unit = unit;
			this.userid = userid;
			this.companyname = companyname;
			this.ponum = ponum;
			this.mytype = mytype;
			this.detailname = detailname;
			this.bztype = bztype;
			this.num = num;
			this.goodsweight = goodsweight;
			this.goodstj = goodstj;
			this.maxgoodssize = maxgoodssize;
			this.gxgl = gxgl;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_commit:
			submit(1);
			break;
		case R.id.btn_next:
			submit(2);

			break;

		default:
			break;
		}

	}

	public void submit(final int type) {
		if (TextUtils.isEmpty(et_pinming.getText().toString().trim())) {
			MyToast.showToast(this, "请填写品名");
			return;
		}
		
		if (TextUtils.isEmpty(et_po.getText().toString().trim())) {
			MyToast.showToast(this, "请填写货物编号");
			return;
		}
		
		if (TextUtils.isEmpty(et_huowuzhongliang.getText().toString().trim())) {
			MyToast.showToast(this, "请填写货物重量");
			return;
		}
		
		if (TextUtils.isEmpty(et_huowutiji.getText().toString().trim())) {
			MyToast.showToast(this, "请填写货物体积");
			return;
		}
		
		String data = new Gson().toJson(new AddGoodsData(JCLApplication
				.getInstance().getUserId(), "", et_po.getText().toString(), ws_maoyileixing
				.getChoicePosition() + "", et_pinming.getText().toString(),
				ws_baozhuangfangshi.getChoicePosition() + "", et_jianshu
						.getText().toString(), et_huowuzhongliang.getText()
						.toString(), et_huowutiji.getText().toString(),
				et_chaochicun.getText().toString(),
				et_guixingguiliang.getText().toString(),
				ws_dun.getChoiceText()));
		String jsonRequest = new Gson().toJson(new AddGoodsRequest(data));
		showLD("提交中...");
		executeRequest(new GsonRequest<BaseBean>(Request.Method.POST,
				UrlCat.URL_SUBMIT, BaseBean.class, null,
				new ParamsBuilder().submitParams(jsonRequest),
				new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// TODO Auto-generated method stub
						cancelLD();
						if (arg0 != null) {
							if ("1".equals(arg0.getCode())) {
								if (type == 1) {
									Toast.makeText(AddGoodsActivity.this,
											arg0.getMsg(), 1000).show();
								} else {
									startActivity(new Intent(
											AddGoodsActivity.this,
											QuickPublicGoodsActivity.class));
								}
								finish();
							} else {
								Toast.makeText(AddGoodsActivity.this,
										arg0.getMsg(), 1000).show();
							}
						} else {
							Toast.makeText(AddGoodsActivity.this, "暂无数据！", 1000)
									.show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
						Toast.makeText(AddGoodsActivity.this, "网络连接异常！", 1000)
								.show();
					}
				}));
	}

}
