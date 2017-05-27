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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.KuaiXunListBean;
import com.jcl.android.bean.KuaiXunListBean.KuaiXunInfo;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.Utils;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.WhSpinner;

public class PublicKuaiXunActivity extends BaseActivity implements
     OnClickListener {
	
	private WhSpinner ws_fabuleibie;
	private List<WhSpinner.Item> fabuleibie;
	private EditText et_public_faq, et_public_info;
	private Button btn_public,btn_public_goods;
	private String key;
	private ImageView iv_back;
	private boolean isChecked;
	private CheckBox cb_beizhu;
    private String ishowremark;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_public_kuaixun);
		initView();
		initWhSpinnerData();
		initConent();
	}

	public void initWhSpinnerData() {
		fabuleibie = new ArrayList<WhSpinner.Item>();
		String[] fabuleibies = getResources().getStringArray(
				R.array.kuaixun);
		for (int i = 0; i < fabuleibies.length; i++) {
			fabuleibie.add(new WhSpinner.Item(fabuleibies[i], i));
		}
		ws_fabuleibie.setItems(fabuleibie);
		ws_fabuleibie.setChoiceText("");
	}

	private void initView() {
		// TODO Auto-generated method stub
		ws_fabuleibie = (WhSpinner) findViewById(R.id.ws_fabuleibie);
		et_public_faq = (EditText) findViewById(R.id.et_public_faq);
		et_public_info = (EditText) findViewById(R.id.et_public_info);
		btn_public = (Button) findViewById(R.id.btn_public);
		btn_public_goods = (Button) findViewById(R.id.btn_public_goods);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_public.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		btn_public_goods.setOnClickListener(this);
		cb_beizhu =(CheckBox) findViewById(R.id.cb_beizhu);
		cb_beizhu.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				switch (buttonView.getId()) {
				case R.id.cb_beizhu:
					if(isChecked)
					{
						ishowremark = "1";
					}else{
						ishowremark = "0";
					}
					break;

				default:
					break;
				}
				
			}
		});
	}
	
	void initConent() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null && bundle.getString("type").equals("3")) {
			
		key = bundle.getString("id");
		et_public_info.setText(bundle.getString("description"));//标题
		et_public_faq.setText(bundle.getString("remark"));//详情
		ws_fabuleibie.setItems(fabuleibie, Integer.valueOf(bundle.getString("fbtype")));
		
		}else{
			return;
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
			this.type = "6001";
			this.data = data;
		}
		
		public PublicOtherRequest(String data,String key) {
				//修改
			this.operate = "M";
			this.type = "6001";
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
		private String description;// 描述 　
		private String remark;// 具体需求 　
		private String userid;// 用户编码 　
		private String ishowremark;
		private String ispc;

		public Data(String fbtype, String description,String remark,
				String userid,String ishowremark,String ispc) {
			this.fbtype = fbtype;
			this.description = description;
//			this.saydetal = saydetal;
//			this.status = "";
//			this.createtime = "";
//			this.effectiveflag = "";
//			this.effectivetime = "";
			this.remark = remark;
			this.userid = userid;
			this.ishowremark = ishowremark;
			this.ispc = ispc;

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_public:

			String info = "";
			if (et_public_info.getText().toString().length() < 4) {
				MyToast.showToast(PublicKuaiXunActivity.this, "最少输入四个字");
				return;
			}
			
			if (Utils.isEmpty(ws_fabuleibie.getChoiceText())) {
				MyToast.showToast(PublicKuaiXunActivity.this, "请选择发布类型");
				return;
			}
			info = et_public_info.getText().toString();
			showLD("发布中...");
			Data data = new Data(ws_fabuleibie.getChoicePosition()+"", info , et_public_faq.getText()
							.toString(), JCLApplication.getInstance()
							.getUserId(),ishowremark,"1");

			String postStr = "";
			String data1 = new Gson().toJson(data);
			if (key == null || key == "") {
				postStr = new Gson().toJson(new PublicOtherRequest(data1));
			} else {
//				Key key = new Key(info.get_id());
//			    postStr = new Gson().toJson(new PublicOtherRequest(
//						new Gson().toJson(data),new Gson().toJson(key)));
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
									MyToast.showToast(PublicKuaiXunActivity.this,
											"发布成功");
									finish();
								} else {
									MyToast.showToast(PublicKuaiXunActivity.this,
											"发布失败");
								}
							}

							cancelLD();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							cancelLD();
							MyToast.showToast(PublicKuaiXunActivity.this, "发布失败");
						}
					}));
			
			break;

		case R.id.iv_back:
			finish();
			break;
			
		case R.id.btn_public_goods:
			startActivity(new Intent(PublicKuaiXunActivity.this,PublicGoodsActivity.class));
			finish();
			break;
				default:
			break;
		}
		
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
