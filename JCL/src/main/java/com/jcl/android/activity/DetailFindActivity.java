package com.jcl.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.DetailFindCarsFragment;
import com.jcl.android.fragment.DetailFindCarsNearFragment;
import com.jcl.android.fragment.DetailFindDistributionFragment;
import com.jcl.android.fragment.DetailFindGoodsFragment;
import com.jcl.android.fragment.DetailFindLogisticFragment;
import com.jcl.android.fragment.DetailFindStorageFragment;
import com.jcl.android.fragment.DetailZhuanxianFragment;
import com.jcl.android.fragment.DetilFindOtherFragment;

/**
 * 找货详情页
 * 
 * @author msz
 *
 */
public class DetailFindActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_detail_find);
		int type = getIntent().getIntExtra("type", 0);
		String id = getIntent().getStringExtra("id");
		initFragment(type, id);
	}

	public static Intent newInstance(Context context, int type, String id) {
		Intent intent = new Intent(context, DetailFindActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("id", id);
		return intent;
	}
	private void initFragment(int type, String id) {
		switch (type) {
		case C.INTENT_TYPE_FIND_GOODS://货物详情
			getSupportFragmentManager()
					.beginTransaction()
					.replace(
							R.id.container,
							DetailFindGoodsFragment
									.newInstance(id))
					.commit();
			break;
			
		case C.INTENT_TYPE_FIND_ZHUANXIAN://专线详情
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetailZhuanxianFragment
							.newInstance(id))
			.commit();
			break;
		case C.INTENT_TYPE_FIND_CAR://车辆详情
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetailFindCarsFragment
							.newInstance(id))
			.commit();
			break;
		case C.INTENT_TYPE_FIND_STORAGE://仓储详情
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetailFindStorageFragment
							.newInstance(id))
			.commit();
			break;
		case C.INTENT_TYPE_FIND_LOGISTIC://物流公司详情
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetailFindLogisticFragment
							.newInstance(id))
			.commit();
			break;
		case C.INTENT_TYPE_FIND_DISTRIBUTION://配货站详情
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetailFindDistributionFragment
							.newInstance(id))
			.commit();
			break;
			
		case C.INTENT_TYPE_FIND_OTHER://其他详情
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetilFindOtherFragment
							.newInstance(id))
			.commit();
			break;
		case C.INTENT_TYPE_FIND_NEAR_CAR://附近车辆
			getSupportFragmentManager()
			.beginTransaction()
			.replace(
					R.id.container,
					DetailFindCarsNearFragment
							.newInstance(id))
			.commit();
			break;
			
//		case C.INTENT_TYPE_FIND_KUAIXUN://快讯详情
//			getSupportFragmentManager()
//			.beginTransaction()
//			.replace(
//					R.id.container,
//					DetilFindKuaiXunActivity
//							.newInstance(id))
//			.commit();
//			break;

		default:
			break;
		}

	}

}
