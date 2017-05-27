package com.jcl.android.net;

import java.util.HashMap;
import java.util.Map;

import com.jcl.android.C;

public class ParamsBuilder {
	/**
	 * 定义的规范：KEY_xxxS
	 * 
	 */
	private static final String KEY_POST_STR = "postStr";
	private static final String KEY_GETPAGE_STR = "getPageStr";
	private static final String KEY_GET_STR = "getStr";
	
	
	/**
	 * 提交postStr
	 * @param postStr 
	 * @return
	 */
	public static Map<String, String> submitParams(String postStr) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(KEY_POST_STR, postStr);
		return resultMap;
	}
	
	/**
	 * 提交postStr
	 * @param postStr 
	 * @return
	 */
	public static Map<String, String> pageSearchParams(String postStr) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(KEY_GETPAGE_STR, postStr);
		return resultMap;
	}
	
	/**
	 * 提交postStr
	 * @param getStr 
	 * @return
	 */
	public static Map<String, String> getStrParams(String postStr) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(KEY_GET_STR, postStr);
		return resultMap;
	}

}