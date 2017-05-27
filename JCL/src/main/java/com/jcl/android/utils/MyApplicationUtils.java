package com.jcl.android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class MyApplicationUtils {
	
	/**
	 * 将dp类型的尺寸转换成px类型的尺寸
	 * 
	 * @author Msz 2014/5/20
	 * @param size
	 * @param context
	 * @return
	 */
	public static int DPtoPX(Context context, int dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	
	/**
	 * 获取设备宽度
	 * 
	 * @return
	 */
	public static int getDeviceWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int w = mDisplayMetrics.widthPixels;
		return w;
	}

	/**
	 * 获取设备高度
	 * 
	 * @return
	 */
	public static int getDeviceHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int h = mDisplayMetrics.heightPixels;
		return h;
	}
	
	/**
	 * 判断email格式是否正确
	 * 
	 * @author Msz 2014/5/20
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证手机号码
	 * 
	 * @author Msz 2014/5/20
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (TextUtils.isEmpty(phone))
			return false;
		String pattern = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$";

		return phone.matches(pattern);
	}

	
	/**
	 * 车牌号验证
	 * 
	 * @author pb 2014/5/20
	 * @param carNum
	 * @return
	 */
	public static boolean isCarNum(String carNum)
	{
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
		Matcher matcher = pattern.matcher(carNum);
		return matcher.matches();
	}
}
