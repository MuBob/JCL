package com.jcl.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.CarManageActivity;
import com.jcl.android.activity.CompanyInfoActivity;
import com.jcl.android.activity.GoodsManageActivity;
import com.jcl.android.activity.LoginActivity;
import com.jcl.android.activity.MyWebViewActivity;
import com.jcl.android.activity.PersonalInfoActivity;
import com.jcl.android.activity.PublicGoodsActivity;
import com.jcl.android.activity.PublicKuaiXunActivity;
import com.jcl.android.activity.PublicOtherActivity;
import com.jcl.android.activity.PublicZhuanxianActivity;
import com.jcl.android.activity.PublishCarActivity;
import com.jcl.android.activity.QuickPublicGoodsActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.utils.SharePerfUtil;
/**
 * 发布页面
 * @author msz
 *
 */
public class PublicFragment extends BaseFragment implements OnClickListener {
	private View root;
	private TextView tv_public_goods;
	private TextView tv_public_car;
	private TextView tv_public_zhuanxian;
	private TextView tv_public_yunjia;
	private TextView tv_public_youshiluxian;
	private TextView tv_public_carmaimai;
	private TextView tv_public_other;
	private TextView tv_quick_public;
	private TextView tv_public_cheliang,tv_public_huowu,tv_quick_cheliang;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.fragment_public, container, false);
		initView();
		return root;
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_public_goods = (TextView) root.findViewById(R.id.tv_public_goods);
		tv_public_goods.setOnClickListener(this);
		tv_public_car = (TextView) root.findViewById(R.id.tv_public_car);
		tv_public_car.setOnClickListener(this);
		tv_public_zhuanxian = (TextView) root.findViewById(R.id.tv_public_zhuanxian);
		tv_public_zhuanxian.setOnClickListener(this);
		tv_public_yunjia = (TextView) root.findViewById(R.id.tv_public_yunjia);
		tv_public_yunjia.setOnClickListener(this);
		tv_public_youshiluxian = (TextView) root.findViewById(R.id.tv_public_youshiluxian);
		tv_public_youshiluxian.setOnClickListener(this);
		tv_public_carmaimai = (TextView) root.findViewById(R.id.tv_public_carmaimai);
		tv_public_carmaimai.setOnClickListener(this);
		tv_public_other = (TextView) root.findViewById(R.id.tv_public_other);
		tv_quick_public = (TextView) root.findViewById(R.id.tv_quick_public);
		tv_public_cheliang = (TextView) root.findViewById(R.id.tv_public_cheliang);
		tv_quick_cheliang = (TextView) root.findViewById(R.id.tv_quick_cheliang);
		tv_public_huowu = (TextView) root.findViewById(R.id.tv_public_huowu);
		tv_public_other.setOnClickListener(this);
		tv_quick_public.setOnClickListener(this);
		tv_public_cheliang.setOnClickListener(this);
		tv_public_huowu.setOnClickListener(this);
		tv_quick_cheliang.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//发布货源
		case R.id.tv_public_goods:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), QuickPublicGoodsActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			
			break;
		case R.id.tv_public_car:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), PublishCarActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			
			break;
		case R.id.tv_quick_public:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(),PublicKuaiXunActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			
			break;
		case R.id.tv_public_zhuanxian:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), PublicZhuanxianActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
		case R.id.tv_public_yunjia:
			Toast.makeText(getActivity(), "功能开发中， 敬请期待！",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.tv_public_youshiluxian:
			Toast.makeText(getActivity(), "功能开发中， 敬请期待！",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.tv_public_carmaimai:
			Toast.makeText(getActivity(), "功能开发中， 敬请期待！",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.tv_public_other:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(),PublicOtherActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			
			break;
			
		case R.id.tv_public_cheliang:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(),CarManageActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.tv_public_huowu:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(),GoodsManageActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				if (SharePerfUtil.getSubmittype().equals("0")) {
					startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				} else {
					startActivity(new Intent(getActivity(), CompanyInfoActivity.class));
				}
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.tv_quick_cheliang:
			Intent intent = new Intent(getActivity(),MyWebViewActivity.class);
			intent.putExtra("weburl", C.BASE_URL+"/push_buy/manager_p?_id="+JCLApplication.getInstance().getUserId());
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}

}
