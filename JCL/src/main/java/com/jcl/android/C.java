package com.jcl.android;

import android.os.Environment;

/**
 * 常量存储类
 * @author msz
 *
 */
public class C {
	
	public final static String JCL_VOLLY_CACHE = "/JCLcache/volly";// Volly缓存
	public final static String SDCARD_PATH_CAN_CLEAR = Environment
			.getExternalStorageDirectory().getPath() + "/JCLcache/img";

    public final static String JCL_CACHE_PATH = "/JCLcache";
	

	public static final String TAG_GSON = "JCL_GSON";
	
	 //OTHERS
    public static final String SHAREPREFREENCE="sp_jcl";
    public static final String PACKAGE_NAME="com.jcl.android";
	
	//------------------URL---------------
    public final static String BASE_URL = "http://www.chinajuchang.net/hsdata";
//    public final static String BASE_URL = "http://115.28.164.187:8080/hsdata";
//    public final static String BASE_IMG_URL = "http://115.28.164.187:8080/hsdata";
//	public final static String BASE_URL = "http://192.168.1.106:8081/hsdata";
    public final static String BLOG_URL = "http://www.chinajuchang.net/hsdata/blog";
    
    public final static String BASE_SERVICE_TEL="4006054077";
    
	//选择图片  resultcode
	public static final int SELECT_PIC_RESULT = 120;
    public static final int CAMERA_REQUEST_PHOTOHRAPH = 111;
    public static final int CAMERA_REQUEST_PHOTOZOOM = 112;
    public static final int CAMERA_REQUEST_PHOTORESOULT = 113;
    
    public static final int FROM_CARMANAGE=0x00011;
    public static final int FOR_CAR_CODE=0x00012; 
    //加载数据条数
    public static final String PAGE_LIMIT="10"; 
    
    //---------查找跳转详情页intent type
    public static final int INTENT_TYPE_FIND_GOODS=1;//货源详情
    public static final int INTENT_TYPE_FIND_CAR=2;//车源详情
    public static final int INTENT_TYPE_FIND_ZHUANXIAN=3;//专线详情
    public static final int INTENT_TYPE_FIND_OTHER=4;//其他详情
    public static final int INTENT_TYPE_FIND_STORAGE=5;//仓储详情
    public static final int INTENT_TYPE_FIND_LOGISTIC=6;//物流公司详情
    public static final int INTENT_TYPE_FIND_DISTRIBUTION=7;//配货站详情
    public static final int INTENT_TYPE_FIND_NEAR_CAR=8;//车源详情
    public static final int INTENT_TYPE_FIND_KUAIXUN=9;//快讯详情
    //---------收藏intent type
    public static final int INTENT_TYPE_COLLECT_GOODS=1;//收藏的货物
    public static final int INTENT_TYPE_COLLECT_CAR=2;//收藏的车辆
    public static final int INTENT_TYPE_COLLECT_ZHUANXIAN=3;//收藏的专线
    public static final int INTENT_TYPE_COLLECT_OTHER=4;//收藏的其他
    //---------企业资料  获取经纬度
    public static final int GET_LATLOT=1;//收藏的货物
}
