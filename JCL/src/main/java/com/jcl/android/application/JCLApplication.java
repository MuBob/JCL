package com.jcl.android.application;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.SP;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.getui.DemoIntentService;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.RequestManager;
import com.jcl.android.net.UrlCat;
import com.jcl.android.push.Utils;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * application
 * @author msz
 *
 */
public class JCLApplication extends Application {
	
	private static Context context;
	private String userId;
	private Resources resource;
	private String pkgName;
	
	private String channelId;//push
	private String push_userId;//push
	
	public String filters="";
	public String pick_lng="";
	public String pick_lat="";
	
	public int homeItme=0;
	
	private String inviteCode;//邀请码
	
//	private static DemoHandler handler;
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	private static final String LTAG = "ShowMapFragment";
	private SDKReceiver mReceiver;
	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s);
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(getApplicationContext(), "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
						1000).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getApplicationContext(), "网络出错",
						1000).show();
			}
		}
	}

	
	private static JCLApplication instance; 
	public synchronized static JCLApplication getInstance() { 
        if (null == instance) { 
            instance = new JCLApplication(); 
        } 
        return instance; 
    } 
	public String getUserId() {
		if(TextUtils.isEmpty(userId)){
			userId = SharePerfUtil.getUserId();
		}
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Resources getResource() {
		return resource;
	}
	public String getPkgName() {
		return pkgName;
	}

	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getPush_userId() {
		return push_userId;
	}
	public void setPush_userId(String push_userId) {
		this.push_userId = push_userId;
	}
	public static int localVersion = 0;// 本地安装版本

	public static int serverVersion = 0;// 服务器版本

	public static String downloadDir = "jcl/upload/";// 安装目录
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		resource = this.getResources();
        pkgName = this.getPackageName();
		
		context = getApplicationContext();
		//个推初始化
		com.igexin.sdk.PushManager.getInstance().initialize(this.getApplicationContext(), com.jcl.android.getui.DemoPushService.class);
		// 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
		com.igexin.sdk.PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
		
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		startLocation();
		initPush(); 
		
		try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	
//		if (handler == null) {
//            handler = new DemoHandler();
//        }
	}
	

	/**
	 * 初始化  定位
	 * */
	private static LocationClient mLocationClient = null;
	public static BDLocation myLocation;
	
	
	/**
	 * 获取当前位置  BDLocation
	 * */
	public BDLocation getMyLocation() {
		return this.myLocation;
	}
	
	
	/**
	 * 获取当前位置  BDLocation 反地理编码
	 * */
	public String getMyLocation2Str() {
		return this.myLocation.getAddrStr();
	}
	
	/***
	 * 
	 * @return DefaultLocationClientOption
	 */
	public LocationClientOption getDefaultLocationClientOption(){
		if(option == null){
			option = new LocationClientOption();
			option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
			option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
			option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
			option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
			option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
			option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
			option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死   
			option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
			option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
			option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//		    option.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
		}
		return option;
	}
	LocationClientOption option;
	private void startLocation(){
		
		mLocationClient = new LocationClient(getApplicationContext()); 
		mLocationClient.setLocOption(getDefaultLocationClientOption());
		mLocationClient.registerLocationListener(new BDLocationListener() { 
            @Override 
            public void onReceiveLocation(BDLocation location) { 
                if (location == null) {
                    Log.v("locData", "null");
                    return;
                } 
                myLocation=location;
            } 
             
            public void onReceivePoi(BDLocation location){ 
                //return ; 
            	if (location == null) {
                    Log.v("locData", "null");
                    return;
                } 
                myLocation=location;
            } 
        }); 
		mLocationClient.start();
	}
	
	
	public void closeGPS()
	{
		mLocationClient.stop();
	}
	
	public static Context getContext(){
		return context;
	}
	
	
	
	
	
	public void initPush()
	{
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
//        ！！ 请将AndroidManifest.xml 128 api_key 字段值修改为自己的 api_key 方可使用 ！！
//        ！！ ATTENTION：You need to modify the value of api_key to your own at row 128 in AndroidManifest.xml to use this Demo !!
        PushManager.startWork(getApplicationContext(),

                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(this, "api_key"));
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier(
                        "notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", pkgName));
        cBuilder.setNotificationSound(Uri.withAppendedPath(
                Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(this, 1, cBuilder);
	}

	
	
	Message msg;
	public void checkLocationSetting()
	{
		if("".equals(SharePerfUtil.getCarid()))
		{
			return;
		}
		
		msg = new Message();
		if(SharePerfUtil.getBoolean(SP.SP_UPDATA_LOCATION))
		{
			timer = new Timer();
			// 创建一个TimerTask
			TimerTask timerTask = new TimerTask() {
				
				@Override
				public void run() {
					// 定义一个消息传过去
					
					msg.what=1;
					msg.obj=SharePerfUtil.getCarid();
					timehandler.sendMessage(msg);
				}
			};
			// TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程  5分钟执行一次
			timer.schedule(timerTask, 0, 60*1000);
			
		}else{

			if(timer!=null)
			{
				msg.what=0;
				timehandler.sendMessage(msg);
			}
		}
	}
	
	
	protected <T> void executeRequest(Request<T> request) {
		RequestManager.addRequest(request, this);
	}
	public static int getVersionCode(Context context)//获取版本号(内部识别号)
	{
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	class Carlatlntrequest{
		private String type="1019";

		private String operate="A";

		private CarlatlntData data;

		public Carlatlntrequest(CarlatlntData data) {
			this.data=data;
			
		}
		
	}
	
	
	private Timer timer;
	// 定义Handler
		Handler timehandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				// Handler处理消息
				if (msg.what > 0) {
					Log.i("getMyLocation()", getMyLocation().getLongitude()+":"+
							getMyLocation().getLatitude()+":"+
							getMyLocation().getAddrStr());
					updataCarLatLnt(SharePerfUtil.getCarid(),
							JCLApplication.getInstance().getMyLocation().getLongitude()+"",
							JCLApplication.getInstance().getMyLocation().getLatitude()+"",
							JCLApplication.getInstance().getMyLocation().getAddrStr());
				} else if (msg.what == 0) {
				
					timer.cancel();
					
				}
			}
		};
	class CarlatlntData{
		private String vid;
		private String longitude;
		private String latitude;
		private String position;
		public CarlatlntData(String vid ,String longitude,String latitude,String position) {
			this.vid=vid;
			this.longitude=longitude;
			this.latitude=latitude;
			this.position=position;
		}
		
	}
    /**
	 * 上传车辆经纬度及位置
	 * @param carid 车辆id
	 */
	public void updataCarLatLnt(String carid,String longitude,String latitude,String position)
	{
		String getStr=new Gson().toJson(new Carlatlntrequest(new CarlatlntData(carid,longitude, latitude,position)));
		
		executeRequest(new GsonRequest<BaseBean>(Request.Method.GET,
				UrlCat.getSubmitPoststrUrl(getStr), BaseBean.class, null,
				null,
				new Listener<BaseBean>() {
					@Override
					public void onResponse(BaseBean arg0) {
						// 清除刷新小图标
						if (arg0 != null) {
//							MyToast.showToast(context, arg0.getMsg());
						} else {
							MyToast.showToast(context, "服务端异常");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						MyToast.showToast(context, arg0.getMessage());
					}
				}));
	}
	

}
