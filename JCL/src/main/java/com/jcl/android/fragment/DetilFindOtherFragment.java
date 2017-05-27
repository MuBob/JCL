package com.jcl.android.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.PersonalInfoActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.DetilFindOtherBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetilFindOtherFragment extends BaseFragment implements
		OnClickListener {

	private View root;
	private String goodsId;
	private ImageView iv_share, iv_collect, iv_back,iv_img;
	private String userid;
	private TextView tv_title, tv_fabu, tv_yewu, tv_detil, tv_time;
	private boolean isChecked;
	private SharePopupwindow sharePopupwindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		goodsId = bundle.getString("id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_detail_other, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		iv_share = (ImageView) root.findViewById(R.id.iv_share);
		iv_back = (ImageView) root.findViewById(R.id.iv_back);
		iv_img = (ImageView) root.findViewById(R.id.iv_img);
		iv_collect = (ImageView) root.findViewById(R.id.iv_collect);
		tv_title = (TextView) root.findViewById(R.id.tv_title);
		tv_fabu = (TextView) root.findViewById(R.id.tv_fabu);
		tv_yewu = (TextView) root.findViewById(R.id.tv_yewu);
		tv_detil = (TextView) root.findViewById(R.id.tv_detil);
		tv_time = (TextView) root.findViewById(R.id.tv_time);

		iv_share.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	class DetailGoodsCollectRequest {
		private String filters;
		private String type;
		private String operate;
		private String data;

		public DetailGoodsCollectRequest(String data, String operate) {
			this.data = data;
			this.operate = operate;
			this.type = "1011";
		}
	}

	class Data {
		private String userid;
		private String type;// 收藏类型 1车源2货源 可扩展
		private String ppid;// 目标ID 比如车源收藏，则是车源ID
		private String name;// 名称
		private String remark;// 备注
		private String effectiveflag;// 有效flag 0无效,1有效

		public Data(String ppid) {
			this.remark = "";
			this.name = "";
			this.effectiveflag = "";
			this.type = "3";
			this.ppid = goodsId;
			this.userid = JCLApplication.getInstance().getUserId();
		}

	}

	public static DetilFindOtherFragment newInstance(String id) {
		DetilFindOtherFragment f = new DetilFindOtherFragment();
		Bundle args = new Bundle();
		args.putString("id", id);
		f.setArguments(args);
		return f;
	}

	class DetailFindGoodsRequest {
		private String filters;
		private String type;
		private String sorts;

		public DetailFindGoodsRequest(String filters) {
			this.filters = filters;
			this.type = "2005";
			this.sorts = "";
		}
	}

	class Filters {
		private String _id;
		private String userid;

		public Filters(String _id, String userid) {
			this._id = _id;
			this.userid = userid;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String title = "hello, 誉畅物流";
		String content = "誉畅物流 让移动物流快速整合，http://www.chinajuchang.com";
		String url = "http://www.chinajuchang.com/d";
		sharePopupwindow = new SharePopupwindow(getActivity(),
				root.findViewById(R.id.ll_parent), null, null, title, content,
				url);
		if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
			userid = JCLApplication.getInstance().getUserId();
			iv_collect.setVisibility(View.VISIBLE);
		} else {
			userid = "";
			iv_collect.setVisibility(View.GONE);
		}
		loadData();
	}

	private void loadData() {
		String filters = new Gson().toJson(new Filters(goodsId, userid));
		String getStr = new Gson().toJson(new DetailFindGoodsRequest(filters));
		showLD("加载中...");
		executeRequest(new GsonRequest<DetilFindOtherBean>(Request.Method.GET,
				UrlCat.getSearchUrl(getStr), DetilFindOtherBean.class, null,
				null, new Listener<DetilFindOtherBean>() {

					@Override
					public void onResponse(DetilFindOtherBean arg0) {
						cancelLD();
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								tv_title.setText(arg0.getData()
										.getDescription());
								tv_time.setText(arg0.getData().getCreatetime());
								tv_detil.setText(arg0.getData().getSaydetal());
								String[] fabuleibies = getResources()
										.getStringArray(R.array.fabuleibie);
								String[] yewuleibies = getResources()
										.getStringArray(R.array.yewuleibie);
								tv_fabu.setText(fabuleibies[Integer
										.valueOf(arg0.getData().getFbtype())]);
								tv_yewu.setText(yewuleibies[Integer
										.valueOf(arg0.getData().getYwtype())]);
								if (arg0.getData().getIfcollect().equals("1")
										|| arg0.getData().getIfcollect() == "1") {
									iv_collect
											.setImageResource(R.drawable.icon_toorbar_collect_checked);
									isChecked = true;
								} else {
									iv_collect
											.setImageResource(R.drawable.icon_toolbar_collect_nomal);
									isChecked = false;
								}
								
								ImageLoaderUtil.getInstance(getActivity()).loadImage(
										C.BASE_URL+arg0.getData().getOther_image(), iv_img);
							}
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						cancelLD();
					}
				}));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_share:
			// if(sharePopupwindow!=null&&!sharePopupwindow.isShowing())
			// sharePopupwindow.show();
			break;
		case R.id.iv_back:
			getActivity().finish();
			break;
		case R.id.iv_collect:
			if (!isChecked) {
				String data = new Gson().toJson(new Data(goodsId));
				String getStr = new Gson()
						.toJson(new DetailGoodsCollectRequest(data, "A"));
				showLD("收藏中...");
				executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
						UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
						null, null, new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(arg0.getCode(), "1")) {
										MyToast.showToast(getActivity(), "收藏成功");
										isChecked = true;
										iv_collect
												.setImageResource(R.drawable.icon_toorbar_collect_checked);
									}
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								// TODO Auto-generated method stub
								cancelLD();
							}
						}));
			} else {
				String data = new Gson().toJson(new Data(goodsId));
				String getStr = new Gson()
						.toJson(new DetailGoodsCollectRequest(data, "R"));
				showLD("取消收藏中...");
				executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
						UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class,
						null, null, new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(arg0.getCode(), "1")) {
										MyToast.showToast(getActivity(),
												"取消收藏成功");
										iv_collect
												.setImageResource(R.drawable.icon_toolbar_collect_nomal);
										isChecked = false;
									}
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError arg0) {
								// TODO Auto-generated method stub
								cancelLD();
							}
						}));
			}

			break;

		default:
			break;
		}

	}
}
