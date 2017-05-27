package com.jcl.android.view;


import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author lifei
 *自定义进度提示框
 */
public class MyProgressDialog {
	public static ProgressDialog GetDialog(Context context) {
		ProgressDialog progressDialog = ProgressDialog.show(context,
				"",	"数据加载中. 请稍后",true);
		progressDialog.setCancelable(false);
		return progressDialog;
	}
	public static ProgressDialog GetDialog(Context context,String message) {
		ProgressDialog progressDialog = ProgressDialog.show(context,
				"",	message,true);
		progressDialog.setCancelable(false);
		return progressDialog;
	}
	
}
