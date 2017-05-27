package com.jcl.android.fragment;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.obj.PointInfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShowMapFragment extends BaseFragment {
	
	
	List<PointInfo> plist;
	public ShowMapFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShowMapFragment(List<PointInfo> list) {
		super();
		// TODO Auto-generated constructor stub
		plist=list;
	}

	
	private View root;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.fragment_show_map, container,false);
		initMapView();
		if(plist!=null)
		{
			drawPoints(plist);
		}
		markerListener();
		return root;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
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
	
	
	private double centerLat=36.109159;
	private double cemterLng=120.415433;
	LatLng cenpt;
	public void initMapView()
	{
		mMapView = (MapView) root.findViewById(R.id.bmapView); 
		mBaiduMap =mMapView.getMap();
		
		//设定中心点坐标 
		if("".equals(JCLApplication.getInstance().pick_lat)
				||"".equals(JCLApplication.getInstance().pick_lng))
		{
			if(JCLApplication.getInstance().getMyLocation()!=null)
			{
				cenpt=new LatLng(JCLApplication.getInstance().getMyLocation().getLatitude(),
						JCLApplication.getInstance().getMyLocation().getLongitude());
			}else
			{
				cenpt= new LatLng(centerLat,cemterLng); 
			}
		}else{
			cenpt=new LatLng(Double.parseDouble(JCLApplication.getInstance().pick_lat)
					,Double.parseDouble(JCLApplication.getInstance().pick_lng));
		}
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
        .target(cenpt)
        .zoom(15)
        .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
	}
	
	
	
	List<Marker> markerList;
	public void drawPoints(List<PointInfo> list)
	{
		if(list==null)
		{
			return;
		}
		markerList=new ArrayList<Marker>();
		for (int i = 0; i < list.size(); i++) {
			if(!TextUtils.isEmpty(list.get(i).getLat())&&!TextUtils.isEmpty(list.get(i).getLng()))
			{
				//定义Maker坐标点  
				LatLng point = new LatLng( Double.valueOf(list.get(i).getLat()), Double.valueOf(list.get(i).getLng()));
				markerList.add(initMarker(point,R.drawable.icon_marka));
			}
			
			
		}
	}
	public Marker initMarker(LatLng point,int drawable)
	{
		if(point==null)
		{
			return null;
		}
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.icon_marka);  
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(point)  
		    .icon(bitmap);  
		//在地图上添加Marker，并显示  
		Marker marker = (Marker) (mBaiduMap.addOverlay(option));
		return marker;
		
	}
	
	
	private InfoWindow mInfoWindow;
	 public void markerListener()
	 {
		 mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO Auto-generated method stub
				Button button = new Button(getActivity());
				button.setTextColor(getResources().getColor(R.color.black)); 
				button.setBackgroundResource(R.drawable.popup);
				OnInfoWindowClickListener listener = null;
				for (int i = 0; i < markerList.size(); i++) {
					if(markerList.get(i)==arg0)
					{
						button.setText(plist.get(i).getShowInfo());
						LatLng ll = arg0.getPosition();
						mInfoWindow = new InfoWindow(button, ll, -47);
						mBaiduMap.showInfoWindow(mInfoWindow);
						
						
						startActivity(DetailFindActivity.newInstance(getActivity(),
								plist.get(i).getType(),plist.get(i).getId()));
					}
					button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							mBaiduMap.hideInfoWindow();
						}
					});
				}
				return true;
			}
		});
	 }
	
	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}
	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
	}

	@Override
	public void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.onDestroy();
		super.onDestroy();
	}
	
	
}
