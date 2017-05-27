package com.jcl.android.utils;

public class InfoUtils {
	
	public static String formatPhone(String phone){
		return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
	}
	
	public static String formatCarCode(String carCode){
		return carCode.substring(0,2) + "***" 
	         + carCode.substring(carCode.length()-2,carCode.length());
	}

}
