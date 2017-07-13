package com.jcl.android.net;

import com.jcl.android.C;

/**
 * 网络访问的字符串拼接的位置，所有的网络字符串都必须啊在此处进行拼接
 */
public class UrlCat {

	/**
	 * 命名规范 URL_XXXX-XXX...
	 * 
	 *
	 */
	// 注册/登录/个人认证
	public static final String URL_SUBMIT_POSTSTR = C.BASE_URL
			+ "/submit?postStr=%1$s";
	// 短信获取验证码
	public static final String URL_CHECK_CODE = C.BASE_URL
			+ "/message?tel=%1$s";

	public static final String URL_SUBMIT = C.BASE_URL + "/submit";
	// 查询接口
	public static final String URL_SEARCH_GETSTR = C.BASE_URL
			+ "/search?getStr=%1$s";
	// 列表查询接口
	public static final String URL_SEARCH_GETPAGESTR = C.BASE_URL
			+ "/search?getPageStr=%1$s";

	//支付接口
	public static final String URL_MY_PAY=C.BASE_URL+"/submit?postStr=?1$s";

	// 注册接口,短信获取验证码接口,个人认证
	public static String getSubmitPoststrUrl(String postStr) {
		return String.format(URL_SUBMIT_POSTSTR, postStr);
	}

	// 短信获取验证码接口
	public static String getCheckCodeUrl(String postStr) {
		return String.format(URL_CHECK_CODE, postStr);
	}

	// 获取个人资料接口
	public static String getInfoUrl(String postStr) {
		return String.format(URL_SEARCH_GETSTR, postStr);
	}
	
	public static final String URL_SEARCH = C.BASE_URL
			+ "/search";
	
	
	public static String getCheckCode(String postStr) {
		return String.format(URL_CHECK_CODE, postStr);
	}
	
	// 查询接口
	public static String getSearchUrl(String getStr) {
			return String.format(URL_SEARCH_GETSTR, getStr);
		}
	// 列表查询接口
	public static String getPageSearchUrl(String postStr) {
		return String.format(URL_SEARCH_GETPAGESTR, postStr);
	}

	//支付接口
	public static String getMyPayUrl(String postStr){
		return String.format(URL_MY_PAY, postStr);
	}
	
}