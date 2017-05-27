package com.jcl.android.base;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;

import com.jcl.android.view.MyProgressDialog;
/**
 * activity基类
 * @author msz
 *
 */
public class BaseActivity extends BaseVollyActivity {
	
	public Resources resource;
	public String pkgName;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		super.onCreate(arg0);
		resource = this.getResources();
        pkgName = this.getPackageName();
	}
	
	protected void showLD(String dialogInfo) {
		if (!isFinishing()) {
			progressDialog = MyProgressDialog.GetDialog(this, dialogInfo);
		}
	}

	protected void cancelLD() {
		if (progressDialog == null) {
			return;
		}
		progressDialog.dismiss();
	}

}
