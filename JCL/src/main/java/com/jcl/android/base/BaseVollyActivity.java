package com.jcl.android.base;



import android.support.v4.app.FragmentActivity;

import com.android.volley.Request;
import com.jcl.android.net.RequestManager;

public class BaseVollyActivity extends FragmentActivity{
	
	
	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}

	protected <T> void executeRequest(Request<T> request) {
		RequestManager.addRequest(request, this);

	}

}