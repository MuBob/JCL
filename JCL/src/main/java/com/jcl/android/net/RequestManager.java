package com.jcl.android.net;

import java.io.File;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.utils.FileUtils;

public class RequestManager {

	private static Context context;
	public static RequestQueue mRequestQueue = newRequesQueue();

	private static DiskBasedCache mDiskCache = (DiskBasedCache) mRequestQueue
			.getCache();

	public RequestManager() {
	}

	private static RequestQueue newRequesQueue() {
		context = JCLApplication.getContext();
		HttpStack stack;
		stack = new HurlStack();

		RequestQueue requestQueue = new RequestQueue(openCache(),
				new BasicNetwork(stack));
		requestQueue.start();
		return requestQueue;
	}

	private static Cache openCache() {
		File temp = new File(FileUtils.getVollyCachePath());
		if (temp == null)
			temp = JCLApplication.getContext().getCacheDir();
		return new DiskBasedCache(temp, 10 * 1024 * 1024);
	}

	public static <T> void addRequest(Request<T> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		mRequestQueue.add(request);
	}

	public static void cancelAll(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	public static File getCachedImageFile(String url) {
		return mDiskCache.getFileForKey(url);
	}

	protected static String getCacheKey(String url, int maxWidth, int maxHeight) {
		return new StringBuilder(url.length() + 12).append("#W")
				.append(maxWidth).append("#H").append(maxHeight).append(url)
				.toString();
	}
}
