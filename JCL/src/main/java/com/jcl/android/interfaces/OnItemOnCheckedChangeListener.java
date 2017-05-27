package com.jcl.android.interfaces;

import android.view.View;

public interface OnItemOnCheckedChangeListener {

	/**
	 * adapter中item组件点击事件回调
	 * Created by pang on 15/5/4.
	 */
	public void onItemBtnClick(View v,int position, boolean isChecked);

}
