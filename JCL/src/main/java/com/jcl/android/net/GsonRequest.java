package com.jcl.android.net;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jcl.android.C;
import com.jcl.android.utils.LogUtil;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by
 * Gson.
 */
public class GsonRequest<T> extends Request<T> {

	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private final Map<String, String> headers;
	private final Listener<T> listener;
	private final Map<String, String> params;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 *
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param headers
	 *            Map of request headers
	 */
	public GsonRequest(int method, String url, Class<T> clazz,
			Map<String, String> headers, Map<String, String> params,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		if (method == Request.Method.GET) {
			LogUtil.logWrite("HTTP_GET", url);
		}else{
			LogUtil.logWrite("HTTP_POST", url+"?"+params.toString());
		}
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
		this.params = params;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return params != null ? params : super.getParams();
	}

	@Override
	protected void deliverResponse(T response) {
		// if(P.CRASH_ON_ERROR){
		// listener.onResponse(response);
		// }else{
		try {
			listener.onResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			LogUtil.logWrite(C.TAG_GSON, json);
			return Response.success(gson.fromJson(json, clazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return Response.error(new ParseError(e));
		}
	}
}