package com.jcl.android.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.AdviceActivity;
import com.jcl.android.activity.CarManageActivity;
import com.jcl.android.activity.CompanyInfoActivity;
import com.jcl.android.activity.GoodsManageActivity;
import com.jcl.android.activity.HelpActivity;
import com.jcl.android.activity.LoginActivity;
import com.jcl.android.activity.ModifyPasswordActivity;
import com.jcl.android.activity.MyCollectActivity;
import com.jcl.android.activity.MyInviteActivity;
import com.jcl.android.activity.MyWalletActivity;
import com.jcl.android.activity.PersonalInfoActivity;
import com.jcl.android.activity.PointActivity;
import com.jcl.android.activity.SecureActivity;
import com.jcl.android.activity.SettingActivity;
import com.jcl.android.activity.SettingContactsActivity;
import com.jcl.android.activity.SettingLogisticsCircleActivity;
import com.jcl.android.activity.SettingOrderActivity;
import com.jcl.android.activity.SettingPublicActivity;
import com.jcl.android.activity.ToolBoxActivity;
import com.jcl.android.activity.ToolsActivity;
import com.jcl.android.activity.UserServiceActivity;
import com.jcl.android.activity.VouchManageActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.bean.LoginBean;
import com.jcl.android.fragment.SettingPublicGoodsFragment.PublicGoodsRequest;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;

/**
 * 个人（企业）中心
 * 
 * @author msz
 *
 */
public class UserCenterFragment extends BaseFragment implements OnClickListener {

	private View root;

	private TextView tv_truename, tv_tel, tv_company, tv_hasauth,tv_info;
	private ImageView iv_header;

	private RelativeLayout rl_setpublic, rl_myinfo, rl_mycollect, rl_mypromote,
			rl_setbid, rl_mysafe, rl_mywallet, rl_myplay,rl_lianxiren,
			rl_feedback, rl_setting, rl_help, rl_share, rl_about, rl_vip,
			rl_setgoods, rl_setcar, rl_toolbox, rl_setorder,rl_logistics_circle,rl_info;
	private TextView tv_myinfo, tv_setgoods, tv_setcar, tv_setsafe;

	private BDLocation myLocation;

	private void initBaseInfo() {

		String userinfoJson = SharePerfUtil.getLoginUserInfo();
		LoginBean loginBean = new Gson()
				.fromJson(userinfoJson, LoginBean.class);
		if (loginBean != null) {
            //个人注册
			if (TextUtils.equals(SharePerfUtil.getSubmittype(), "0")) {
				tv_info.setText("个人资料");
				if (loginBean != null) {
					if (TextUtils.equals(loginBean.getData().getIsauth(), "1")) {
						//认证通过
						tv_company.setVisibility(View.VISIBLE);
						tv_tel.setVisibility(View.VISIBLE);
						tv_hasauth.setVisibility(View.GONE);
						tv_tel.setText(loginBean.getData().getMobile());
						tv_truename.setText(loginBean.getData().getName());
						tv_company
								.setText(loginBean.getData().getCompanyname());
						ImageLoaderUtil.getInstance(getActivity()).loadImage(
								C.BASE_URL+"/"+loginBean.getData().getHead(), iv_header);
						
					} else {
						//未通过
						tv_company.setVisibility(View.GONE);
						tv_tel.setVisibility(View.GONE);
						tv_hasauth.setVisibility(View.VISIBLE);
						tv_truename.setText(loginBean.getData().getMobile());
						ImageLoaderUtil.getInstance(getActivity()).loadImage(
								C.BASE_URL+"/"+loginBean.getData().getHead(), iv_header);
					}
				}
			} else {//企业注册
				tv_info.setText("企业资料");
//				tv_setgoods.setText("货物管理");
//				tv_setcar.setText("车辆管理");
//				tv_setsafe.setText("保险管理");
//				rl_mysafe.setVisibility(View.GONE);
				//rl_mywallet.setVisibility(View.GONE);
				//rl_vip.setVisibility(View.VISIBLE);
				//rl_mypromote.setVisibility(View.VISIBLE);
				rl_setbid.setVisibility(View.VISIBLE);//担保交易记录
//				rl_mywallet.setVisibility(View.GONE);//我的钱包
				rl_setbid.setVisibility(View.VISIBLE);//报价管理
				rl_myinfo.setVisibility(View.VISIBLE);//账户资料
				rl_vip.setVisibility(View.VISIBLE);//会员服务
				if (TextUtils.equals(loginBean.getData().getIsauth(), "1")) {
					//认证
					tv_company.setVisibility(View.VISIBLE);
					tv_tel.setVisibility(View.VISIBLE);
					tv_hasauth.setVisibility(View.GONE);
					tv_tel.setText(loginBean.getData().getMobile());
					tv_truename.setText(loginBean.getData().getName());
					tv_company.setText(loginBean.getData().getCompanyname());
					ImageLoaderUtil.getInstance(getActivity()).loadImage(
							C.BASE_URL+"/"+loginBean.getData().getHead(), iv_header);
				} else {
					//未认证
					tv_company.setVisibility(View.GONE);
					tv_truename.setText(loginBean.getData().getZhname());
					tv_hasauth.setVisibility(View.VISIBLE);
					if (loginBean.getData() != null) {
						tv_tel.setText(loginBean.getData().getMobile());
					}

				}
			}
		}

	}

	private void initView() {
		rl_setpublic = (RelativeLayout) root.findViewById(R.id.rl_setpublic);
		rl_myinfo = (RelativeLayout) root.findViewById(R.id.rl_myinfo);
		rl_mycollect = (RelativeLayout) root.findViewById(R.id.rl_mycollect);
		rl_mypromote = (RelativeLayout) root.findViewById(R.id.rl_mypromote);
		rl_setbid = (RelativeLayout) root.findViewById(R.id.rl_setbid);
		rl_setbid.setOnClickListener(this);
		rl_mywallet = (RelativeLayout) root.findViewById(R.id.rl_mywallet);
		rl_mywallet.setOnClickListener(this);
		rl_myplay = (RelativeLayout) root.findViewById(R.id.rl_myplay);
		rl_lianxiren = (RelativeLayout) root.findViewById(R.id.rl_lianxiren);
		rl_mysafe = (RelativeLayout) root.findViewById(R.id.rl_mysafe);
		rl_feedback = (RelativeLayout) root.findViewById(R.id.rl_feedback);
		rl_setting = (RelativeLayout) root.findViewById(R.id.rl_setting);
		rl_help = (RelativeLayout) root.findViewById(R.id.rl_help);
		rl_share = (RelativeLayout) root.findViewById(R.id.rl_share);
		rl_about = (RelativeLayout) root.findViewById(R.id.rl_about);
		rl_setorder = (RelativeLayout) root.findViewById(R.id.rl_setorder);
		rl_vip = (RelativeLayout) root.findViewById(R.id.rl_vip);
		rl_setgoods = (RelativeLayout) root.findViewById(R.id.rl_setgoods);
		rl_setcar = (RelativeLayout) root.findViewById(R.id.rl_setcar);
		rl_setcar.setOnClickListener(this);
		rl_toolbox = (RelativeLayout) root.findViewById(R.id.rl_toolbox);
		rl_logistics_circle = (RelativeLayout) root.findViewById(R.id.rl_logistics_circle);

		personal_set = root.findViewById(R.id.personal_set);
		tv_truename = (TextView) root.findViewById(R.id.tv_truename);
		rl_info = (RelativeLayout) root.findViewById(R.id.rl_info);
		tv_info = (TextView)root.findViewById(R.id.tv_info);
		tv_tel = (TextView) root.findViewById(R.id.tv_tel);
		tv_company = (TextView) root.findViewById(R.id.tv_company);
		tv_myinfo = (TextView) root.findViewById(R.id.tv_myinfo);
		tv_setgoods = (TextView) root.findViewById(R.id.tv_setgoods);
		tv_setcar = (TextView) root.findViewById(R.id.tv_setcar);
		iv_header = (ImageView) root.findViewById(R.id.iv_header);
		tv_hasauth = (TextView) root.findViewById(R.id.tv_hasauth);

		rl_help.setOnClickListener(this);
		rl_setpublic.setOnClickListener(this);
		rl_setgoods.setOnClickListener(this);
		rl_mycollect.setOnClickListener(this);
		rl_setorder.setOnClickListener(this);
		rl_logistics_circle.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_myplay.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		rl_mysafe.setOnClickListener(this);
		rl_setting.setOnClickListener(this);
		rl_toolbox.setOnClickListener(this);
		rl_vip.setOnClickListener(this);
		rl_lianxiren.setOnClickListener(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_usercenter, container, false);
		initView();
		initBaseInfo();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListener();
		setUserListener();
	}

	private View personal_set;

	public void setListener() {
		personal_set.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.equals("0", SharePerfUtil.getSubmittype())) {
					getActivity().startActivityForResult(
							new Intent(getActivity(),
									PersonalInfoActivity.class), 0);
				} else if (TextUtils.equals("1", SharePerfUtil.getSubmittype())) {
					getActivity()
							.startActivityForResult(
									new Intent(getActivity(),
											CompanyInfoActivity.class), 0);
				} else {

				}

			}
		});
	}
	public void setUserListener() {
		rl_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.equals("0", SharePerfUtil.getSubmittype())) {
					getActivity().startActivityForResult(
							new Intent(getActivity(),
									PersonalInfoActivity.class), 0);
				} else if (TextUtils.equals("1", SharePerfUtil.getSubmittype())) {
					getActivity()
							.startActivityForResult(
									new Intent(getActivity(),
											CompanyInfoActivity.class), 0);
				} else {

				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_setcar:
			startActivity(new Intent(getActivity(), CarManageActivity.class));
			break;

		case R.id.rl_help:
			startActivity(new Intent(getActivity(), HelpActivity.class));
			break;

			//管理发布
		case R.id.rl_setpublic:
			startActivity(new Intent(getActivity(), SettingPublicActivity.class));
			break;

			//货物信息
		case R.id.rl_setgoods:
			startActivity(new Intent(getActivity(), GoodsManageActivity.class));
			break;

		case R.id.rl_mycollect://我的收藏
			startActivity(new Intent(getActivity(), MyCollectActivity.class));
			break;
			
		case R.id.rl_setorder://我的订单
			startActivity(new Intent(getActivity(), SettingOrderActivity.class));
			break;
			
		case R.id.rl_mywallet://我的钱包
			startActivity(new Intent(getActivity(), MyWalletActivity.class));
			break;

			//我的物流圈
		case R.id.rl_logistics_circle:
			startActivity(new Intent(getActivity(), SettingLogisticsCircleActivity.class));
//			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
			
			//设置
		case R.id.rl_setting:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
			
			//工具箱
		case R.id.rl_toolbox:
			startActivity(new Intent(getActivity(), ToolsActivity.class));
			break;
			
			//会员中心
		case R.id.rl_vip:
			startActivity(new Intent(getActivity(), UserServiceActivity.class));
			break;
			
			//投诉建议
		case R.id.rl_feedback:
			startActivity(new Intent(getActivity(), AdviceActivity.class));
			break;
			//担保交易
		case R.id.rl_setbid:
			startActivity(new Intent(getActivity(), VouchManageActivity.class));
			break;
			//积分商城
		case R.id.rl_myplay:
			startActivity(new Intent(getActivity(), PointActivity.class));
			break;
			//保险
		case R.id.rl_mysafe:
			startActivity(new Intent(getActivity(), SecureActivity.class));
			break;	
			//我的邀请
		case R.id.rl_share:
			startActivity(new Intent(getActivity(), MyInviteActivity.class));
			break;
			//联系人管理
		case R.id.rl_lianxiren:
			startActivity(new Intent(getActivity(), SettingContactsActivity.class));
			break;
		default:
			break;
		}

	}
}
