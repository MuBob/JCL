package com.jcl.android.view;

import android.content.Context;
import android.widget.Toast;

/**
 * @author 
 *封装Toast
 */
public class MyToast {

	/**
	 * 显示toast
	 */
	public static void showToast(Context context,int stringId) {
		Toast.makeText(context,
				stringId,
				Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, String string) {
		Toast.makeText(context,
				string,
				Toast.LENGTH_SHORT).show();
	}
}
