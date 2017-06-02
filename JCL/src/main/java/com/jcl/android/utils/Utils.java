package com.jcl.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import com.baidu.mapapi.model.LatLng;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;



public class Utils {
    public static final String TAG = "PushDemoActivity";
    public static final String RESPONSE_METHOD = "method";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";

    public static String logStringCache = "";

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
    public static boolean hasBind(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String flag = sp.getString("bind_flag", "");
        if ("ok".equalsIgnoreCase(flag)) {
            return true;
        } 
        return false;
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    public static List<String> getTagsList(String originalText) {
        if (originalText == null || originalText.equals("")) {
            return null;
        }
        List<String> tags = new ArrayList<String>();
        int indexOfComma = originalText.indexOf(',');
        String tag;
        while (indexOfComma != -1) {
            tag = originalText.substring(0, indexOfComma);
            tags.add(tag);

            originalText = originalText.substring(indexOfComma + 1);
            indexOfComma = originalText.indexOf(',');
        }

        tags.add(originalText);
        return tags;
    }

    public static String getLogText(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("log_text", "");
    }

    public static void setLogText(Context context, String text) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString("log_text", text);
        editor.commit();
    }
    
    /** 
     * 计算两点之间距离 
     * @param start 
     * @param end 
     * @return 米 
     */  
    public static double getDistance(LatLng start,LatLng end){  
        double lat1 = (Math.PI/180)*start.latitude;  
        double lat2 = (Math.PI/180)*end.latitude;  
          
        double lon1 = (Math.PI/180)*start.longitude;  
        double lon2 = (Math.PI/180)*end.longitude;  
          
        //地球半径  
        double R = 6371;  
          
        //两点间距离 km，如果想要米的话，结果*1000就可以了  
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;  
          
        return d*1000;  
    }  
    

    /** 
     * 计算日期加7
     * @param data
     */  
    public static String addData(String leftdate){  
    	String adddata = "";
    	Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    try {
		 date = sdf.parse(leftdate);
		} catch (ParseException e) {
			e.printStackTrace();
		} 

//	    Log.e("````````````````", date.toString());
	    Log.e("leftdate````````````````", leftdate);
	    GregorianCalendar gc=new GregorianCalendar(); 
	    gc.setTime(date); 
	    gc.add(4,+1); 
	    gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
	    return sdf.format(gc.getTime());

    }  
    /** 
     * 计算日期加7
     * @param data
     */
    public static Date addDate(Date d,long day) throws ParseException {
    	  long time = d.getTime(); 
    	  day = day*24*60*60*1000; 
    	  time+=day; 
    	  return new Date(time);
    	  }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0 || "null".equals(str));
    }

    /**
     * 获取当前时间
     * */
    public static String getCurDate(){
       SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd");     
       Date curDate =  new Date(System.currentTimeMillis());
	   return formatter.format(curDate);  
    
    }
    
    /**
	* 加载本地图片
	* http://bbs.3gstdy.com
	* @param url
	* @return
	*/
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	
	 /**
		* 图片转base64
		* http://bbs.3gstdy.com
		* @param url
		* @return
		*/
	public static String bitmaptoString(Bitmap bitmap) {  
        // 将Bitmap转换成字符串  
        String string = null;  
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();  
        bitmap.compress(CompressFormat.PNG, 100, bStream);  
        byte[] bytes = bStream.toByteArray();  
        string = Base64.encodeToString(bytes, Base64.DEFAULT);  
        return string;  
  
}  
	


    /** 
     * 将时间戳转为代表"距现在多久之前"的字符串 
     * @param timeStr   时间戳 
     * @return 
     */  
    public static String getStandardDate(String timeStr) {  
      
        StringBuffer sb = new StringBuffer();  
      
        long t = Long.parseLong(timeStr);  
        long time = System.currentTimeMillis() - (t*1000);  
        long mill = (long) Math.ceil(time /1000);//秒前  
      
        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前  
      
        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时  
      
        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前  
      
        if (day - 1 > 0) {  
            sb.append(day + "天");  
        } else if (hour - 1 > 0) {  
            if (hour >= 24) {  
                sb.append("1天");  
            } else {  
                sb.append(hour + "小时");  
            }  
        } else if (minute - 1 > 0) {  
            if (minute == 60) {  
                sb.append("1小时");  
            } else {  
                sb.append(minute + "分钟");  
            }  
        } else if (mill - 1 > 0) {  
            if (mill == 60) {  
                sb.append("1分钟");  
            } else {  
                sb.append(mill + "秒");  
            }  
        } else {  
            sb.append("刚刚");  
        }  
        if (!sb.toString().equals("刚刚")) {  
            sb.append("前");  
        }  
        return sb.toString();  
    }  
    
    /**
	 * 返回文字描述的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText(Date date) {
		final long minute = 60 * 1000;// 1分钟
		final long hour = 60 * minute;// 1小时
		final long day = 24 * hour;// 1天
		final long month = 31 * day;// 月
		final long year = 12 * month;// 年
		if (date == null) {
			return null;
		}
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if (diff > year) {
			r = (diff / year);
			return r + "年前";
		}
		if (diff > month) {
			r = (diff / month);
			return r + "个月前";
		}
		if (diff > day) {
			r = (diff / day);
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}
	
	 public static Date stringToDate(String str) {  
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   ParsePosition pos = new ParsePosition(0);
		   Date strtodate = formatter.parse(str, pos);
		   return strtodate;
	    }  

  //字符串转时间戳
    public static String getTime(String timeString){
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch(ParseException e){
            e.printStackTrace();
        }
        return timeStamp;
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }
    public static final String ALLCHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateRandom(int length){
        Random random=new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }
    
}
