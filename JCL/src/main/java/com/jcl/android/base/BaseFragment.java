package com.jcl.android.base;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;


import com.android.volley.Request;
import com.jcl.android.net.RequestManager;
import com.jcl.android.view.MyProgressDialog;
/**
 * fragment基类
 * @author msz
 *
 */
public class BaseFragment extends Fragment {
	
	private ProgressDialog progressDialog;
	
	@Override
	public void onDestroy() {
        RequestManager.cancelAll(this);
        super.onDestroy();
	}

	protected <T> void executeRequest(Request<T> request) {
		RequestManager.addRequest(request, this);
		
	}
	protected void showLD(String dialogInfo) {
		if (!getActivity().isFinishing()) {
			progressDialog = MyProgressDialog.GetDialog(getActivity(), dialogInfo);
		}
	}

	protected void cancelLD() {
		if (progressDialog == null) {
			return;
		}
		progressDialog.dismiss();
	}
}
