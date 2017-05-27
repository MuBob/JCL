package com.jcl.android.view;

/**
 * 排序方式选择监听器
 * @author pang
 *
 */
public interface WhSpinnerOnItemChangedListener {
	/**
	 * 按价格升排序
	 */
	public abstract void onItemChanged(WhSpinner whSpinner,int item);
}
