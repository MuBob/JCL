package com.jcl.android.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.DetailFindGoodsBean;
import com.jcl.android.bean.DetailFindStorageBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.jcl.android.view.MyUINavigationView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
/**
 * 仓储详情
 * @author xuelei
 *
 */
public class DetailFindStorageFragment extends BaseFragment implements
		OnClickListener {

	private View root;
	private String storageId;
	private ImageView iv_share,iv_collect,iv_back;
	private String userid;
	private TextView tv_storagename, tv_storagelinkman, tv_storageaddress, tv_storagecontent;
	
	private boolean isChecked;
	private SharePopupwindow sharePopupwindow;
	private MyUINavigationView uINavigationView;
	private TextView tv_chufadi, tv_mudidi, tv_xianlu;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		storageId = bundle.getString("id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_detail_find_storage, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		iv_share = (ImageView) root.findViewById(R.id.iv_share);
		iv_collect = (ImageView) root.findViewById(R.id.iv_collect);
		iv_collect.setVisibility(View.GONE);
		iv_share.setVisibility(View.GONE);
		tv_storagename = (TextView) root.findViewById(R.id.tv_storagename);
		tv_storagelinkman = (TextView) root.findViewById(R.id.tv_storagelinkman);
		tv_storageaddress = (TextView) root.findViewById(R.id.tv_storageaddress);
		tv_storagecontent = (TextView) root.findViewById(R.id.tv_storagecontent);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_xianlu = (TextView) root
				.findViewById(R.id.tv_xianlu);
		iv_share.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		iv_back = (ImageView) root.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
	}
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) root.findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish();
			}
		});
	}
	class DetailStorageCollectRequest {
		private String filters;
		private String type;
		private String operate;
		private String data;

		public DetailStorageCollectRequest(String data,String operate) {
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
			this.type = "3";//仓储3
			this.ppid = storageId;
			this.userid = JCLApplication.getInstance().getUserId();
		}

	}

	public static DetailFindStorageFragment newInstance(String id) {
		DetailFindStorageFragment f = new DetailFindStorageFragment();
		Bundle args = new Bundle();
		args.putString("id", id);
		f.setArguments(args);
		return f;
	}

	class DetailFindStroageRequest {
		private String filters;
		private String type;
		private String sorts;

		public DetailFindStroageRequest(String filters) {
			this.filters = filters;
			this.type = "2011";
			this.sorts = "";
		}
	}

	class Filters {
		private String _id;
		//private String userid;

		public Filters(String _id) {
			this._id = _id;
//			this.userid = userid;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String title= "hello, 誉畅物流";
		String content = "誉畅物流 让移动物流快速整合，http://www.chinajuchang.com";
		String url = "http://www.chinajuchang.com/d";
		sharePopupwindow = new SharePopupwindow(getActivity(), root.findViewById(R.id.ll_parent), null, null,title,content,url);
		if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
			userid = JCLApplication.getInstance().getUserId();
			iv_collect.setVisibility(View.GONE);
		} else {
			userid = "";
			iv_collect.setVisibility(View.GONE);
		}
		loadData();
	}

	private void loadData() {
		String filters = new Gson().toJson(new Filters(storageId));
		String getStr = new Gson().toJson(new DetailFindStroageRequest(filters));
		showLD("加载中...");
		executeRequest(new GsonRequest<DetailFindStorageBean>(Request.Method.GET,
				UrlCat.getSearchUrl(getStr), DetailFindStorageBean.class, null,
				null, new Listener<DetailFindStorageBean>() {

					@Override
					public void onResponse(DetailFindStorageBean arg0) {
						cancelLD();
						if (arg0 != null) {
							if (TextUtils.equals(arg0.getCode(), "1")) {
								tv_storagename.setText(arg0.getData()
										.getZhname());
								String linkman = arg0.getData()
										.getLinkman();
								String phone = arg0.getData()
										.getPhone();
								tv_storagelinkman.setText((linkman == null ? "":linkman)+" "+(phone == null ? "":phone));
								tv_storageaddress.setText(arg0.getData()
										.getAddress());
								tv_storagecontent.setText(arg0.getData()
										.getContent());
								
								if (TextUtils.equals(arg0.getData()
										.getIfcollect(), "1")) {
									iv_collect.setImageResource(R.drawable.icon_toorbar_collect_checked);
									isChecked = true;
								} else {
									iv_collect.setImageResource(R.drawable.icon_toolbar_collect_nomal);
									isChecked = false;
								}
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
			if(sharePopupwindow!=null&&!sharePopupwindow.isShowing())
				sharePopupwindow.show();
			break;
		case R.id.iv_back:
			getActivity().finish();
			break;
		case R.id.iv_collect:
			if (!isChecked) {
				String data = new Gson().toJson(new Data(storageId));
				String getStr = new Gson()
						.toJson(new DetailStorageCollectRequest(data,"A"));
				showLD("收藏中...");
				executeRequest(new GsonRequest<BaseBean>(
						Request.Method.GET, UrlCat
								.getSubmitPoststrUrl(getStr),
						BaseBean.class, null, null,
						new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(
											arg0.getCode(), "1")) {
										MyToast.showToast(
												getActivity(), "收藏成功");
										isChecked = true;
										iv_collect.setImageResource(R.drawable.icon_toorbar_collect_checked);
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
			}else{
				String data = new Gson().toJson(new Data(storageId));
				String getStr = new Gson()
						.toJson(new DetailStorageCollectRequest(data,"R"));
				showLD("取消收藏中...");
				executeRequest(new GsonRequest<BaseBean>(
						Request.Method.GET, UrlCat
								.getSubmitPoststrUrl(getStr),
						BaseBean.class, null, null,
						new Listener<BaseBean>() {

							@Override
							public void onResponse(BaseBean arg0) {
								cancelLD();
								if (arg0 != null) {
									if (TextUtils.equals(
											arg0.getCode(), "1")) {
										MyToast.showToast(
												getActivity(), "取消收藏成功");
										iv_collect.setImageResource(R.drawable.icon_toolbar_collect_nomal);
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
