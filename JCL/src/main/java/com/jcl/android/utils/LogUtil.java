package com.jcl.android.utils;

import android.util.Log;

import com.jcl.android.P;

public class LogUtil {
	private static boolean switcher = P.LOG_ON;// ＬＯＧ开关　True->Show False->Hide

	public static void logWrite(String tag, String message) {
		if (message == null)
			message = "null";
		if (switcher) {
			try {
				Log.i(tag, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}