package com.jcl.android.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcl.android.R;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.utils.NetUtil;

public class WebViewFragment extends BaseFragment {

	private View root;
	private WebView wb_faxian;
	private String url;
	private TextView no_net;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		url = bundle.getString("webUrl");
		if (TextUtils.isEmpty(url)) {
			url = "http://www.baidu.com";
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_webview, container, false);
		initView();
		return root;
	}

	private static final String TAG = "WebViewFragment";
	public static WebViewFragment newInstance(String url) {
		WebViewFragment f = new WebViewFragment();
		Bundle args = new Bundle();
		args.putString("webUrl", url);
		Log.i(TAG, "WebViewFragment.newInstance: url="+url);
		f.setArguments(args);
		return f;
	}

	private void initView() {
		
		no_net=(TextView) root.findViewById(R.id.no_net);
		
		wb_faxian = (WebView) root.findViewById(R.id.wb_faxian);
		
		
		if(!NetUtil.isNetConnected(getActivity()))
		{
			no_net.setVisibility(View.VISIBLE);
			wb_faxian.setVisibility(View.GONE);
		}
		
		WebSettings webSettings = wb_faxian.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setDomStorageEnabled(true);// 允许DCOM
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT
				| WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// 启用地理定位
		webSettings.setGeolocationEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		wb_faxian.setScrollbarFadingEnabled(true);
		wb_faxian.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		wb_faxian.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "WebViewFragment.shouldOverrideUrlLoading: url="+url);
				if(url!=null&&url.startsWith("baidumap:")){
				    
				}else {
					view.loadUrl(url); // 在当前的webview中跳转到新的url
				}
				return true;
			}
		});
		wb_faxian.setWebChromeClient(new WebChromeClient() {
			// 配置权限（同样在WebChromeClient中实现）
			public void onGeolocationPermissionsShowPrompt(String origin,
					GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
                Log.i(TAG, "WebViewFragment.onGeolocationPermissionsShowPrompt: origin="+origin);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});
		wb_faxian.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& wb_faxian.canGoBack()) { // 表示按返回键 时的操作
						wb_faxian.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		wb_faxian.getSettings().setJavaScriptEnabled(true);
		wb_faxian.loadUrl(url);
	}

}
