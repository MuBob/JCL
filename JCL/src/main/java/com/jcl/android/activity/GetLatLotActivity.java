package com.jcl.android.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jcl.android.R;
import com.jcl.android.base.BaseActivity;
import com.jcl.android.view.MyUINavigationView;

/**
 * 登录页
 * 
 * @author 获取经纬度页面
 *
 */
public class GetLatLotActivity extends BaseActivity implements OnMapClickListener,OnGetGeoCoderResultListener{

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_get_lat_lot);
		afterCreate();
		initNavigation();
		initView();
		
		initMapView();
		
	}

	private MyUINavigationView uINavigationView;
	private String lat;
	private String lnt;
	private String addr;
	private Builder builder = null;
	
	private void afterCreate()
	{
		if(getIntent().hasExtra("lat")&&getIntent().hasExtra("lnt"))
		{
			lat=getIntent().getStringExtra("lat");
			lnt=getIntent().getStringExtra("lnt");
		}
		
	}

	private void initView() {
		
	}
	
	
	
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	/**
	 * MapView 初始化选项
	 * */
	private BaiduMapOptions mapOptions;
	/**
	 * 定义 BaiduMap 地图对象的操作方法与接口
	 * */
	public BaiduMap mBaiduMap;
	
	
	GeoCoder mSearch;
	
	MapStatus status;
	public void initMapView()
	{
		mMapView = (MapView) findViewById(R.id.bmapView); 
		mBaiduMap =mMapView.getMap();
		
		//创建地理编码检索实例
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		//设定中心点坐标 
//        LatLng cenpt = new LatLng(36.109159,120.415433); 
		LatLng cenpt = new LatLng(Double.parseDouble(lat),Double.parseDouble(lnt));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
        .target(cenpt)
        .zoom(15)
        .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setOnMapClickListener(this);
        if(getIntent().hasExtra("hasDialog")&&("1".equals(getIntent().getStringExtra("hasDialog")))){
        	// 用户添加标准操作提示
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("操作提示")
					.setMessage("地图上点选您要检索位置后保存，以便查看该区域附近车辆。")
					.setPositiveButton("知道了",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
			alert.create().show();
        }
	}
	private void initNavigation() {
		// TODO Auto-generated method stub
		uINavigationView = (MyUINavigationView) findViewById(R.id.action_bar);
		ImageButton btnLeftText = uINavigationView.getBtn_left();
		Button btnRightText=uINavigationView.getBtn_right();
		btnLeftText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRightText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				submit();
			}
		});
	}
	
	private void submit()
	{
		if(point==null)
		{
			Toast.makeText(this, "请选择位置后再提交！",
					1000).show();
			return;
		}
		Intent data=new Intent();
		data.putExtra("Lat", point.latitude+"");
		data.putExtra("Lng", point.longitude+"");
		data.putExtra("addr", addr);
		setResult(RESULT_OK, data);
		finish();
	}
	
	
	Marker marker;
	LatLng point;
	@Override
	public void onMapClick(LatLng arg0) {
		if(point==null)
		{
			//定义Maker坐标点  
			point =arg0;
			//构建Marker图标  
			BitmapDescriptor bitmap = BitmapDescriptorFactory  
			    .fromResource(R.drawable.icon_marka);  
			//构建MarkerOption，用于在地图上添加Marker  
			OverlayOptions option = new MarkerOptions()  
			    .position(point)  
			    .icon(bitmap);  
			//在地图上添加Marker，并显示  
			marker = (Marker) (mBaiduMap.addOverlay(option));
			//开启反地理编码检索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
		}else{
			point =arg0;
			marker.setPosition(arg0);
		}
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//地理编码监听返回
	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	//反地理编码监听返回
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {  
            //没有找到检索结果  
			return;
        }  
        //获取反向地理编码结果
		addr=arg0.getAddress();
		Log.i("addr----------", addr);
	}


}
