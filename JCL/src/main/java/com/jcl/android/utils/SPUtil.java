package com.jcl.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jcl.android.C;
import com.jcl.android.application.JCLApplication;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float,
 * Long类型的参数 同样调用getParam就能获取到保存在手机里面的数据
 */
public class SPUtil {
	private static final String FILE_NAME = C.SHAREPREFREENCE;
	private static final String PACK_NAME = C.PACKAGE_NAME;

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * @param key			键名
	 * @param object		值
	 */
	@SuppressLint("InlinedApi")
	public static boolean set(String key, Object object) {
		try {
			Context otherAppsContext = JCLApplication.getContext().createPackageContext(PACK_NAME, Context.CONTEXT_IGNORE_SECURITY);
			String type = object.getClass().getSimpleName();
			SharedPreferences sp = otherAppsContext.getSharedPreferences(FILE_NAME, Context.MODE_MULTI_PROCESS);
			SharedPreferences.Editor editor = sp.edit();

			if ("String".equals(type)) {
				editor.putString(key, (String) object);
			} else if ("Integer".equals(type)) {
				editor.putInt(key, (Integer) object);
			} else if ("Boolean".equals(type)) {
				editor.putBoolean(key, (Boolean) object);
			} else if ("Float".equals(type)) {
				editor.putFloat(key, (Float) object);
			} else if ("Long".equals(type)) {
				editor.putLong(key, (Long) object);
			}
			return editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean remove(String key){
		Context otherAppsContext;
		try {
			otherAppsContext = JCLApplication.getContext().createPackageContext(PACK_NAME, Context.CONTEXT_IGNORE_SECURITY);
			SharedPreferences sp = otherAppsContext.getSharedPreferences(FILE_NAME, Context.MODE_MULTI_PROCESS);
			SharedPreferences.Editor editor = sp.edit();
			editor.remove(key);
			return editor.commit();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	@SuppressLint("InlinedApi")
	public static Object get(String key, Object defaultObject) {
		try {
			String type = defaultObject.getClass().getSimpleName();
			Context otherAppsContext = JCLApplication.getContext().createPackageContext(PACK_NAME, Context.CONTEXT_IGNORE_SECURITY);
			SharedPreferences sp = otherAppsContext.getSharedPreferences(FILE_NAME, Context.MODE_MULTI_PROCESS);
			if ("String".equals(type)) {
				return sp.getString(key, (String) defaultObject);
			} else if ("Integer".equals(type)) {
				return sp.getInt(key, (Integer) defaultObject);
			} else if ("Boolean".equals(type)) {
				return sp.getBoolean(key, (Boolean) defaultObject);
			} else if ("Float".equals(type)) {
				return sp.getFloat(key, (Float) defaultObject);
			} else if ("Long".equals(type)) {
				return sp.getLong(key, (Long) defaultObject);
			}
			return defaultObject;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultObject;
		}
	}
}