package com.jcl.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.fragment.DetailFindCarsFragment;
import com.jcl.android.fragment.DetailFindGoodsFragment;
import com.jcl.android.fragment.DetailZhuanxianFragment;
import com.jcl.android.fragment.MyCollectCarsFragment;
import com.jcl.android.fragment.MyCollectDedicatedLineFragment;
import com.jcl.android.fragment.MyCollectGoodsFragment;
import com.jcl.android.fragment.MyCollectOtherFragment;

/**
 * 收藏子页面
 * 
 * @author msz
 *
 */
public class CollectChildActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_detail_find);
		int type = getIntent().getIntExtra("type", 0);
		initFragment(type);
	}

	public static Intent newInstance(Context context, int type) {
		Intent intent = new Intent(context, CollectChildActivity.class);
		intent.putExtra("type", type);
		return intent;
	}

	private void initFragment(int type) {
		switch (type) {
		case C.INTENT_TYPE_COLLECT_GOODS:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new MyCollectGoodsFragment())
					.commit();
			break;

		case C.INTENT_TYPE_COLLECT_ZHUANXIAN:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new MyCollectDedicatedLineFragment())
					.commit();
			break;
		case C.INTENT_TYPE_COLLECT_CAR:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new MyCollectCarsFragment())
					.commit();
			break;
			
		case C.INTENT_TYPE_COLLECT_OTHER:
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, new MyCollectOtherFragment())
					.commit();
			break;

		default:
			break;
		}

	}

}
