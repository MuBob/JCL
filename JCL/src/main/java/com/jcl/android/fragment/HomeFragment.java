package com.jcl.android.fragment;

import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jcl.android.R;
import com.jcl.android.SP;

import com.jcl.android.activity.AddCarActivity;
import com.jcl.android.activity.BlogActivity;

import com.jcl.android.activity.CarManageActivity;
import com.jcl.android.activity.DaiShouActivity;
import com.jcl.android.activity.FindCarsActivity;
import com.jcl.android.activity.FindDistributionActivity;
import com.jcl.android.activity.FindGoodsActivity;
import com.jcl.android.activity.FindKuaiXunActivity;
import com.jcl.android.activity.FindLogisticsActivity;
import com.jcl.android.activity.FindOthersActivity;
import com.jcl.android.activity.FindStrorageActivity;
import com.jcl.android.activity.LoginActivity;
import com.jcl.android.activity.PersonalInfoActivity;
import com.jcl.android.activity.PublishCarActivity;
import com.jcl.android.activity.QuickPublicGoodsActivity;
import com.jcl.android.activity.SecureActivity;
import com.jcl.android.activity.SettingCarsOrderActivity;
import com.jcl.android.activity.SettingGoodsOrderActivity;
import com.jcl.android.activity.SettingPublicActivity;
import com.jcl.android.activity.SettingPublicCarsActivity;
import com.jcl.android.activity.SettingPublicGoodsActivity;
import com.jcl.android.activity.ToolBoxActivity;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import com.ta.utdid2.core.persistent.MySharedPreferences;

/**
 * 首页
 * 
 * @author msz
 *
 */
public class HomeFragment extends BaseFragment implements OnClickListener {

	private View root;
	private TextView tv_find_huo,tv_find_che,tv_find_cangchu,tv_find_more,
	tv_find_peihuozhan,tv_find_wuliu,tv_mycar,tv_mygoods,iv_ju,
	tv_server,tv_mycarsource,tv_mygoodssource,
	tv_baoxian,tv_shunfengche,tv_licheng,tv_daishouhuokuan,
	tv_cheliangguanli,tv_huichengche;
	private PopupWindow popupWindow;
	
	private TextView tv_zhengche,tv_lingdan,tv_shuangbei,
	tv_jizhuangxiang,tv_gaolan,tv_lengcang,tv_pingban,tv_baowen,tv_xiangshi;
	private View ll_daishouhuokuan,ll_cheliangguanli,my_publish;
	private String shenfen;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTipsPopupWindow();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		root = inflater.inflate(R.layout.fragment_home, container, false);
		initView();
		return root;
	}

	private void initView() {
		tv_find_huo = (TextView) root.findViewById(R.id.tv_find_huo);
		tv_find_huo.setOnClickListener(this);
		tv_find_che = (TextView) root.findViewById(R.id.tv_find_che);
		tv_find_che.setOnClickListener(this);
		iv_ju = (TextView) root.findViewById(R.id.iv_ju);
		iv_ju.setOnClickListener(this);
		tv_find_cangchu = (TextView) root.findViewById(R.id.tv_find_cangchu);
		tv_find_cangchu.setOnClickListener(this);
		tv_find_more = (TextView) root.findViewById(R.id.tv_find_more);
		tv_find_more.setOnClickListener(this);
		tv_find_peihuozhan = (TextView) root.findViewById(R.id.tv_find_peihuozhan);
		tv_find_peihuozhan.setOnClickListener(this);
		tv_find_wuliu = (TextView) root.findViewById(R.id.tv_find_wuliu);
		tv_find_wuliu.setOnClickListener(this);
		tv_mycar = (TextView) root.findViewById(R.id.tv_mycar);
		tv_mycar.setOnClickListener(this);
		tv_mygoods = (TextView) root.findViewById(R.id.tv_mygoods);
		tv_mygoods.setOnClickListener(this);
		tv_mycarsource = (TextView) root.findViewById(R.id.tv_mycarsource);
		tv_mycarsource.setOnClickListener(this);
		tv_mygoodssource = (TextView) root.findViewById(R.id.tv_mygoodssource);
		tv_mygoodssource.setOnClickListener(this);
//		tv_server = (TextView)root.findViewById(R.id.tv_server);
//		tv_server.setOnClickListener(this);
		tv_baoxian= (TextView) root.findViewById(R.id.tv_baoxian);
		tv_baoxian.setOnClickListener(this);
		tv_shunfengche= (TextView) root.findViewById(R.id.tv_shunfengche);
		tv_shunfengche.setOnClickListener(this);
		tv_licheng= (TextView) root.findViewById(R.id.tv_licheng);
		tv_licheng.setOnClickListener(this);
		ll_daishouhuokuan=root.findViewById(R.id.tv_daishouhuokuan);
		ll_daishouhuokuan.setOnClickListener(this);
		tv_daishouhuokuan= (TextView) root.findViewById(R.id.tv_daishouhuokuan);
		tv_daishouhuokuan.setOnClickListener(this);
		ll_cheliangguanli=root.findViewById(R.id.tv_daishouhuokuan);
		ll_cheliangguanli.setOnClickListener(this);
		my_publish=root.findViewById(R.id.my_publish);
		my_publish.setOnClickListener(this);
		tv_cheliangguanli= (TextView) root.findViewById(R.id.tv_cheliangguanli);
		tv_cheliangguanli.setOnClickListener(this);
		tv_huichengche= (TextView) root.findViewById(R.id.tv_huichengche);
		tv_huichengche.setOnClickListener(this);
		
		tv_zhengche = (TextView) root.findViewById(R.id.tv_zhengche);
		tv_lingdan = (TextView) root.findViewById(R.id.tv_lingdan);
		tv_shuangbei = (TextView) root.findViewById(R.id.tv_shuangbei);
		tv_jizhuangxiang = (TextView) root.findViewById(R.id.tv_jizhuangxiang);
		tv_gaolan = (TextView) root.findViewById(R.id.tv_gaolan);
		tv_lengcang = (TextView) root.findViewById(R.id.tv_lengcang);
		tv_pingban = (TextView) root.findViewById(R.id.tv_pingban);
		tv_baowen = (TextView) root.findViewById(R.id.tv_baowen);
		tv_xiangshi = (TextView) root.findViewById(R.id.tv_xiangshi);
		
		tv_zhengche.setOnClickListener(this);
		tv_lingdan.setOnClickListener(this);
		tv_shuangbei.setOnClickListener(this);
		tv_jizhuangxiang.setOnClickListener(this);
		tv_gaolan.setOnClickListener(this);
		tv_lengcang.setOnClickListener(this);
		tv_pingban.setOnClickListener(this);
		tv_baowen.setOnClickListener(this);
		tv_xiangshi.setOnClickListener(this);
		
		if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
			switch (SharePerfUtil.getType()) {
			case "0":
				shenfen = "国内物流企业/车主";
				break;
				
			case "1":
				shenfen = "发货企业/货主";
				break;
				
			case "2":
				shenfen = "配货站";
				break;
				
			case "3":
				shenfen = "国际物流企业/货代";
				break;
				
			case "4":
				shenfen = "货车生产/销售商";
				break;
				
			case "5":
				shenfen = "仓储/物流园/停车场";
				break;
				
			case "6":
				shenfen = "报关报检";
				break;
				
			case "7":
				shenfen = "其他类型";
				break;
				

			default:
				break;
			}
		} 
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_find_huo://货源
//			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
//				MyToast.showToast(getActivity(), "请先登陆");
//			} else {
//			if (SharePerfUtil.getType().equals("0")) {
//				//车主
//				if (SharePerfUtil.getIcount().equals("0")) {
//					//未发布过车辆
//					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
//					startActivity(new Intent(getActivity(), AddCarActivity.class));
//				} else {
//					startActivity(new Intent(getActivity(), FindGoodsActivity.class));
//				}
//			} else {
//				//非车主
//				if (SharePerfUtil.getIcount().equals("0")) {
//					//未发布过信息
//					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
//				} else {
//					startActivity(new Intent(getActivity(), FindGoodsActivity.class));
//				}
//			  }
//			}

			startActivity(new Intent(getActivity(), FindGoodsActivity.class));
			break;
		case R.id.tv_find_che://车源
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {
			if (SharePerfUtil.getType().equals("0")) {
				//车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过车辆
					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
					startActivity(new Intent(getActivity(), AddCarActivity.class));
				} else {
					startActivity(new Intent(getActivity(),
							FindCarsActivity.class).putExtra("cartype", 0));
				}
			} else {
				//非车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过信息
					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
				} else {
					startActivity(new Intent(getActivity(),
							FindCarsActivity.class).putExtra("cartype", 0));
				}
			}
			}
			break;
		case R.id.iv_ju://聚   声音
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {
			if (SharePerfUtil.getType().equals("0")) {
				//车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过车辆
					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
					startActivity(new Intent(getActivity(), AddCarActivity.class));
				} else {
					startActivity(new Intent(getActivity(), FindKuaiXunActivity.class));
				}
			} else {
				//非车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过信息
					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
				} else {
					startActivity(new Intent(getActivity(), FindKuaiXunActivity.class));
				}
			 }
			}
			break;
			
		case R.id.tv_find_cangchu://仓储
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {
			if (SharePerfUtil.getType().equals("0")) {
				//车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过车辆
					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
					startActivity(new Intent(getActivity(), AddCarActivity.class));
				} else {
					startActivity(new Intent(getActivity(), FindStrorageActivity.class));
				}
			} else {
				//非车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过信息
					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
				} else {
					startActivity(new Intent(getActivity(), FindStrorageActivity.class));
				}
			}
			}
			break;
			
		case R.id.tv_find_wuliu://物流
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {
			if (SharePerfUtil.getType().equals("0")) {
				//车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过车辆
					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
					startActivity(new Intent(getActivity(), AddCarActivity.class));
				} else {
					startActivity(new Intent(getActivity(), FindLogisticsActivity.class));
				}
			} else {
				//非车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过信息
					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
				} else {
					startActivity(new Intent(getActivity(), FindLogisticsActivity.class));
				}
			 }
			}
			break;
			
		case R.id.tv_find_peihuozhan://配货站
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {
			if (SharePerfUtil.getType().equals("0")) {
				//车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过车辆
					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
					startActivity(new Intent(getActivity(), AddCarActivity.class));
				} else {
					startActivity(new Intent(getActivity(), FindDistributionActivity.class));
				}
			} else {
				//非车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过信息
					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
				} else {
					startActivity(new Intent(getActivity(), FindDistributionActivity.class));
				}
			}
			}
			break;
			
		case R.id.tv_find_more://其他
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {
			if (SharePerfUtil.getType().equals("0")) {
				//车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过车辆
					MyToast.showToast(getActivity(), "您注册的身份是车主/物流公司，为系统自动匹配货源，请先登记车辆信息。如有疑问可拨打客服电话4006054077咨询");
					startActivity(new Intent(getActivity(), AddCarActivity.class));
				} else {
					startActivity(new Intent(getActivity(), FindOthersActivity.class));
				}
			} else {
				//非车主
				if (SharePerfUtil.getIcount().equals("0")) {
					//未发布过信息
					MyToast.showToast(getActivity(), "您注册的身份" + shenfen + ",请先发布任意一条信息后查看相应数据");
				} else {
					startActivity(new Intent(getActivity(), FindOthersActivity.class));
				}
			}
			}
			break;
			
		case R.id.tv_mycar://我的车单
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), SettingCarsOrderActivity.class));
			}else{
				startActivity(new Intent(getActivity(),LoginActivity.class));
			}
			break;
		case R.id.tv_mygoods://我的货单
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), SettingGoodsOrderActivity.class));
			}else{
				startActivity(new Intent(getActivity(),LoginActivity.class));
			}
			break;
		case R.id.tv_mycarsource://我的车源
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), SettingPublicCarsActivity.class));
			}else{
				startActivity(new Intent(getActivity(),LoginActivity.class));
			}
			break;
		case R.id.tv_mygoodssource://我的货源
			if (SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				startActivity(new Intent(getActivity(), SettingPublicGoodsActivity.class));
			}else{
				startActivity(new Intent(getActivity(),LoginActivity.class));
			}
			break;
//		case R.id.tv_server://一键客服
//			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//					+ C.BASE_SERVICE_TEL));
//			startActivity(intent);
//			break;
		case R.id.tv_baoxian:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
        		startActivity(new Intent(getActivity(),SecureActivity.class));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
        case R.id.tv_shunfengche:
        	if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				Intent intent = new Intent(getActivity(),PublishCarActivity.class);
				intent.putExtra("shunfengche", "2");
        		startActivity(intent);
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
        case R.id.tv_licheng:
        	startActivity(new Intent(getActivity(),ToolBoxActivity.class));
	        break;
        case R.id.tv_daishouhuokuan:
        	startActivity(new Intent(getActivity(),DaiShouActivity.class));
	         break;
		case R.id.tv_cheliangguanli:
		case R.id.ll_cheliangguanli:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
        		startActivity(new Intent(getActivity(),CarManageActivity.class)
        		.putExtra("fromhome", "fromhome"));
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.my_publish:
			if (!SharePerfUtil.getBoolean(SP.SP_ISLOGIN)) {
				MyToast.showToast(getActivity(), "请先登陆");
			} else {startActivity(new Intent(getActivity(), SettingPublicActivity.class));}
			break;
		case R.id.tv_huichengche:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				Intent intent = new Intent(getActivity(),PublishCarActivity.class);
				intent.putExtra("huichengche", "1");
        		startActivity(intent);
        		
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.tv_zhengche:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
				intent.putExtra("type", 0);
        		startActivity(intent);
        		
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.tv_lingdan:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
				intent.putExtra("type", 1);
        		startActivity(intent);
        		
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.tv_shuangbei:
			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
				intent.putExtra("type", 3);
        		startActivity(intent);
        		
			}else if(SharePerfUtil.getLinkMan().isEmpty()){
				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
						Toast.LENGTH_LONG).show();
			}else{
				startActivity(new Intent(getActivity(), LoginActivity.class));
			}
			break;
			
		case R.id.tv_jizhuangxiang:
//			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
//				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
//				intent.putExtra("type", 4);
//        		startActivity(intent);
//        		
//			}else if(SharePerfUtil.getLinkMan().isEmpty()){
//				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
//				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
//						Toast.LENGTH_LONG).show();
//			}else{
//				startActivity(new Intent(getActivity(), LoginActivity.class));
//			}
			
			Intent intent4 = new Intent(getActivity(),FindCarsActivity.class);
			intent4.putExtra("cartype", 4);
    		startActivity(intent4);
			break;
			
		case R.id.tv_gaolan:
//			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
//				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
//				intent.putExtra("type", 5);
//        		startActivity(intent);
//        		
//			}else if(SharePerfUtil.getLinkMan().isEmpty()){
//				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
//				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
//						Toast.LENGTH_LONG).show();
//			}else{
//				startActivity(new Intent(getActivity(), LoginActivity.class));
//			}
			Intent intent5 = new Intent(getActivity(),FindCarsActivity.class);
			intent5.putExtra("cartype", 5);
    		startActivity(intent5);
			break;
			
		case R.id.tv_lengcang:
//			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
//				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
//				intent.putExtra("type", 6);
//        		startActivity(intent);
//        		
//			}else if(SharePerfUtil.getLinkMan().isEmpty()){
//				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
//				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
//						Toast.LENGTH_LONG).show();
//			}else{
//				startActivity(new Intent(getActivity(), LoginActivity.class));
//			}
			Intent intent6 = new Intent(getActivity(),FindCarsActivity.class);
			intent6.putExtra("cartype", 6);
    		startActivity(intent6);
			break;
			
		case R.id.tv_pingban:
//			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
//				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
//				intent.putExtra("type", 7);
//        		startActivity(intent);
//			}else if(SharePerfUtil.getLinkMan().isEmpty()){
//				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
//				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
//						Toast.LENGTH_LONG).show();
//			}else{
//				startActivity(new Intent(getActivity(), LoginActivity.class));
//			}
			
			Intent intent7 = new Intent(getActivity(),FindCarsActivity.class);
			intent7.putExtra("cartype", 7);
    		startActivity(intent7);
			break;
			
		case R.id.tv_baowen:
//			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
//				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
//				intent.putExtra("type", 8);
//        		startActivity(intent);
//        		
//			}else if(SharePerfUtil.getLinkMan().isEmpty()){
//				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
//				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
//						Toast.LENGTH_LONG).show();
//			}else{
//				startActivity(new Intent(getActivity(), LoginActivity.class));
//			}
			Intent intent8 = new Intent(getActivity(),FindCarsActivity.class);
			intent8.putExtra("cartype", 8);
    		startActivity(intent8);
			break;
			
		case R.id.tv_xiangshi:
//			if(SharePerfUtil.getBoolean(SP.SP_ISLOGIN)){
//				Intent intent = new Intent(getActivity(),QuickPublicGoodsActivity.class);
//				intent.putExtra("type", 9);
//        		startActivity(intent);
//        		
//			}else if(SharePerfUtil.getLinkMan().isEmpty()){
//				startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
//				Toast.makeText(getActivity(), "请先完善信息，完善后才能发布信息",
//						Toast.LENGTH_LONG).show();
//			}else{
//				startActivity(new Intent(getActivity(), LoginActivity.class));
//			}
			Intent intent9 = new Intent(getActivity(),FindCarsActivity.class);
			intent9.putExtra("cartype", 9);
    		startActivity(intent9);
			break;
		default:
			break;
		}
	}

	private void initTipsPopupWindow() {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View window = inflater.inflate(R.layout.popu_home_tips, null);
		popupWindow = new PopupWindow(window, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ImageView iv_tips = (ImageView) window.findViewById(R.id.imageView1);
		ImageView iv_close = (ImageView) window.findViewById(R.id.iv_close);
		InputStream is = this.getResources().openRawResource(R.drawable.tips);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1; 
		Bitmap btp =BitmapFactory.decodeStream(is,null,options);
		iv_tips.setImageBitmap(btp);
		iv_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
		// 必须添加，否OnItemClickListener事件无法响应
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		// 点击popupWindow的外部，自动消失
		popupWindow.setBackgroundDrawable(new ColorDrawable(-00000));
		backgroundAlpha(0.7f);
		popupWindow.update();
		popupWindow.showAtLocation(window, Gravity.CENTER, 0, 0);
		// popupWindow的消失监听事件
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
	}
	
	
	 public void backgroundAlpha(float bgAlpha)  
	    {  
	        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();  
	        lp.alpha = bgAlpha; //0.0-1.0  
	        getActivity().getWindow().setAttributes(lp);  
	    }  
}
