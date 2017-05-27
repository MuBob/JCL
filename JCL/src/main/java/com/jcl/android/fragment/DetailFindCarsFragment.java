package com.jcl.android.fragment;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.FindEvalutesActivity;
import com.jcl.android.activity.PublicOtherActivity;
import com.jcl.android.activity.SettingPublicOtherActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.DetailFindCarBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.UrlCat;
import com.jcl.android.popupwindow.SharePopupwindow;
import com.jcl.android.request.DetailFindCarsRequest;
import com.jcl.android.utils.InfoUtils;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailFindCarsFragment extends BaseFragment implements
		OnClickListener {

	private View root;
	private String Id;
	private String userid;
	private TextView tv_publish_time;//发布时间
	private TextView tv_chufadi, tv_mudidi, tv_time,
	tv_carcode, tv_chexing, tv_carweight, tv_price,tv_pay_tel,tv_pay_online,tv_tel,tv_evalute;
	private ImageView img_1, img_2,img_3;
	private ImageView iv_share,iv_collect;
	private ImageView img_back;
	private SharePopupwindow sharePopupwindow;
	private boolean isChecked;
	private TextView tv_type0;//车源类型
	private TextView tv_type1;//紧急程度
	private TextView tv_type2;//装车方式
	private TextView tv_laiyuan;//来源
	private TextView tv_laiyuanxinxi;//来源信息
	private TextView tv_dangqian;//当前位置
	private String fbrid,payphone;
	private TextView linearea1;//常跑线1
	private TextView linearea2;//常跑线2
	private TextView linearea3;//常跑线3
	private TextView linearea4;//常跑线4
	private TextView tv_point;//途经点

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		Id = bundle.getString("id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_detail_find_car, container,
				false);
		initView();
		return root;
	}

	private void initView() {
		tv_laiyuanxinxi=(TextView) root.findViewById(R.id.tv_laiyuanxinxi);
		tv_laiyuan=(TextView) root.findViewById(R.id.tv_laiyuan);
		tv_publish_time=(TextView) root.findViewById(R.id.tv_publish_time);
		tv_chufadi = (TextView) root.findViewById(R.id.tv_chufadi);
		tv_mudidi = (TextView) root.findViewById(R.id.tv_mudidi);
		tv_time = (TextView) root.findViewById(R.id.tv_time);
		tv_chexing = (TextView) root.findViewById(R.id.tv_chexing);
		tv_carcode = (TextView) root.findViewById(R.id.tv_carcode);
		tv_tel=(TextView) root.findViewById(R.id.tv_tel);
		
		tv_carweight = (TextView) root.findViewById(R.id.tv_carweight);
		tv_price = (TextView) root.findViewById(R.id.tv_price);
		tv_pay_tel = (TextView) root.findViewById(R.id.tv_pay_tel);
		tv_pay_tel.setOnClickListener(this);
		tv_evalute = (TextView) root.findViewById(R.id.tv_evalute);
		tv_evalute.setOnClickListener(this);
		
		tv_pay_online = (TextView) root.findViewById(R.id.tv_pay_online);

		img_1 = (ImageView) root.findViewById(R.id.img_1);
		img_2 = (ImageView) root.findViewById(R.id.img_2);
		img_3 = (ImageView) root.findViewById(R.id.img_3);
		iv_share = (ImageView) root.findViewById(R.id.iv_share);
		iv_collect = (ImageView) root.findViewById(R.id.iv_collect);
		img_back = (ImageView) root.findViewById(R.id.img_back);
		
		linearea1=(TextView) root.findViewById(R.id.linearea1);
		linearea2=(TextView) root.findViewById(R.id.linearea2);
		linearea3=(TextView) root.findViewById(R.id.linearea3);
		linearea4=(TextView) root.findViewById(R.id.linearea4);
		
		tv_point=(TextView) root.findViewById(R.id.tv_point);
		iv_share.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		img_back.setOnClickListener(this);

		tv_type0=(TextView) root.findViewById(R.id.tv_type0);
		tv_type1=(TextView) root.findViewById(R.id.tv_type1);
		tv_type2=(TextView) root.findViewById(R.id.tv_type2);
		
		tv_dangqian=(TextView) root.findViewById(R.id.tv_dangqian);
	}

	class DetailCarsCollectRequest {
		private String filters;
		private String type;
		private String operate;
		private String data;

		public DetailCarsCollectRequest(String data,String operate) {
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
			this.type = "1";
			this.ppid = Id;
			this.userid = SharePerfUtil.getUserId();
		}

	}

	public static DetailFindCarsFragment newInstance(String id) {
		DetailFindCarsFragment f = new DetailFindCarsFragment();
		Bundle args = new Bundle();
		args.putString("id", id);
		f.setArguments(args);
		return f;
	}


	class Filters {
		private String _id;
		private String userid=SharePerfUtil.getUserId();

		public Filters(String _id) {
			this._id = _id;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String title= "hello, 誉畅物流";
		String content = "我在[誉畅手机配货软件]上发现一条车源，推荐给你，地址：http://www.chinajuchang.com/cars/cydetailed?id="+Id;
		String url = "http://www.chinajuchang.com/cars/cydetailed?id="+Id;
		sharePopupwindow = new SharePopupwindow(getActivity(), root.findViewById(R.id.ll_parent), null, null,title,content,url);
		if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
			userid = SharePerfUtil.getUserId();
			iv_collect.setVisibility(View.VISIBLE);
		} else {
			userid = "";
			iv_collect.setVisibility(View.GONE);
		}
		loadData();
	}

	private DetailFindCarBean.Data carinfo;
	private void loadData() {
		String filters = new Gson().toJson(new Filters(Id));
		String getStr = new Gson().toJson(new DetailFindCarsRequest(filters));
		showLD("加载中...");
		executeRequest(new GsonRequest<DetailFindCarBean>(Request.Method.GET,
				UrlCat.getSearchUrl(getStr), DetailFindCarBean.class, null,
				null, new Listener<DetailFindCarBean>() {

					@Override
					public void onResponse(DetailFindCarBean arg0) {
						cancelLD();
						if (arg0 != null) {
							
							if (TextUtils.equals(arg0.getCode(), "1")) {
								
								carinfo=arg0.getData();
								if(TextUtils.isEmpty(carinfo.getPosition())){
									tv_dangqian.setText("当前位置：用户没有上传当前位置");
								}else{
									tv_dangqian.setText("当前位置："+carinfo.getPosition());
								}
								tv_publish_time.setText(arg0.getData().getCreatetime());
								tv_chufadi.setText(arg0.getData().getStartarea());
								tv_mudidi.setText(arg0.getData().getEndarea());
								tv_time.setText(arg0.getData().getEmptytimestart()+"至"+arg0.getData().getEmptytimeend());
								if(!"集装箱车".equals(arg0.getData().getCartype()))
								{
									tv_chexing.setText((TextUtils.isEmpty(arg0.getData().getCartype())?"暂无":arg0.getData().getCartype())
											+"("+(TextUtils.isEmpty(arg0.getData().getCarlength())?"暂无)":(arg0.getData().getCarlength()+"米)")));
								}else
								{
									tv_chexing.setText((TextUtils.isEmpty(arg0.getData().getCartype())?"暂无":arg0.getData().getCartype())
											);
								}
								if(!TextUtils.isEmpty(arg0.getData().getPlatenum())){
									tv_carcode .setText(InfoUtils.formatCarCode(arg0.getData().getPlatenum()));
								}
								
								if(!TextUtils.isEmpty(arg0.getData().getApproveweight())){
									tv_carweight .setText(arg0.getData().getApproveweight()+"吨");
								}
								
								if(TextUtils.isEmpty(carinfo.getCytype())){
									tv_type0.setVisibility(View.GONE);
								}
								tv_type0.setText(carinfo.getCytype());
								tv_type1.setText(carinfo.getJjdegree());
								tv_type2.setText(carinfo.getCommony());
								tv_price .setText(TextUtils.isEmpty(arg0.getData().getExpectfee())?"面议":arg0.getData().getExpectfee()+"元");
								
								tv_point.setText(carinfo.getTjplace());
//								linearea1.setText(text);
//								linearea2.setText(text);
//								linearea3.setText(text);
//								linearea4.setText(text);
								
								ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+arg0.getData().getCarimage1(), img_1);
								ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+arg0.getData().getCarimage2(), img_2);
								ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+arg0.getData().getCarimage3(), img_3);
								
								if(TextUtils.equals("0", arg0.getData().getIfcollect()))
								{
									isChecked=false;
									iv_collect.setImageResource(R.drawable.icon_toolbar_collect_nomal);
								}else{
									isChecked=true;
									iv_collect.setImageResource(R.drawable.icon_toorbar_collect_checked);
								}
								
								
								tv_tel.setText(InfoUtils.formatPhone(arg0.getData().getTel()));
								payphone = arg0.getData().getPublisher().getMobile();
								
								tv_laiyuan.setText("发布人："+arg0.getData().getPublisher().getUname()+
										"\n发布人电话："+InfoUtils.formatPhone(arg0.getData().getPublisher().getMobile())+
										"\n发布人ID："+arg0.getData().getPublisher().getId()+
										"\n发布地址："+arg0.getData().getPublisher().getAddress()+
										"\n注册类型："+arg0.getData().getPublisher().getType()+
										"\n会员级别："+arg0.getData().getPublisher().getLevel()+"    "+
										arg0.getData().getPublisher().getIsauth());
								fbrid = arg0.getData().getPublisher().getId();
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
		case R.id.img_back:
			getActivity().finish();
			break;
		case R.id.tv_pay_tel:
			
			Intent Int_call = new Intent();
            Int_call.setAction("android.intent.action.CALL");
            Int_call.setData(Uri.parse("tel:"+payphone));
            //使用Intent时，还需要设置其category，不过
            //方法内部会自动为Intent添加类别：android.intent.category.DEFAULT
            startActivity(Int_call);
        	break;
        case R.id.tv_evalute:
			
			Intent Int_evalute = new Intent(getActivity(),FindEvalutesActivity.class);
			Int_evalute.putExtra("fbrid", fbrid);
			startActivity(Int_evalute);
        	break;
		case R.id.iv_collect:
			if (!isChecked) {
				String data = new Gson().toJson(new Data(Id));
				String getStr = new Gson()
						.toJson(new DetailCarsCollectRequest(data,"A"));
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
				String data = new Gson().toJson(new Data(Id));
				String getStr = new Gson()
						.toJson(new DetailCarsCollectRequest(data,"R"));
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
