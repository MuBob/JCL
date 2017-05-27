package com.jcl.android.fragment;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.base.BaseFragment;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.fragment.DetailFindCarsFragment.Filters;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.ImageLoaderUtil;
import com.jcl.android.net.ParamsBuilder;
import com.jcl.android.net.UrlCat;
import com.jcl.android.request.DetailFindCarsRequest;
import com.jcl.android.utils.InfoUtils;
import com.jcl.android.utils.MyApplicationUtils;
import com.jcl.android.view.MyUINavigationView;
import com.umeng.socialize.media.GooglePlusShareContent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加车辆
 * 
 * @author pb
 * 
 */
public class DetailFindCarsNearFragment extends BaseFragment implements OnClickListener{
	private View root;
	private String Id;
	
	private TextView tv_changzhudi;//常驻的
	private TextView tv_position;//当前位置
	private ImageView img_1;//车头	
	private ImageView img_2;//45°
	private ImageView img_3;//车尾
	private TextView tv_carcode;//车牌号
	private TextView tv_juli;//距离
	private TextView tv_chexing;//车型
	private TextView tv_carweight;//载重
	private TextView tv_carlength;//车长
	private TextView tv_company;//车辆所属

	private TextView tv_pay_tel;//

	private TextView tv_authentication_state;//认证状态
	private TextView linearea1;//常跑线1
	private TextView linearea2;//常跑线2
	private TextView linearea3;//常跑线3
	private TextView linearea4;//常跑线4
	
	private TextView tv_load_state;//车辆装载状态

	
	private ImageView img_back;
	public static Fragment newInstance(String id) {
		// TODO Auto-generated method stub
		DetailFindCarsNearFragment f = new DetailFindCarsNearFragment();
		Bundle args = new Bundle();
		args.putString("id", id);
		f.setArguments(args);
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		Id = bundle.getString("id");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_detail_find_cheliang, container,
				false);
//		afterCreate();
		
		 
       
        
		
		initNavigation();
		initView();
		getCarinfo();
		return root;
	}
	
	private void initView()
	{
		tv_changzhudi=(TextView) root.findViewById(R.id.tv_changzhudi);
		tv_position=(TextView) root.findViewById(R.id.tv_position);
		tv_load_state=(TextView) root.findViewById(R.id.tv_load_state);
		img_1=(ImageView) root.findViewById(R.id.img_1);
		img_2=(ImageView) root.findViewById(R.id.img_2);
		img_3=(ImageView) root.findViewById(R.id.img_3);
		tv_carcode=(TextView) root.findViewById(R.id.tv_carcode);
		tv_juli=(TextView) root.findViewById(R.id.tv_juli);
		tv_chexing=(TextView) root.findViewById(R.id.tv_chexing);
		tv_carweight=(TextView) root.findViewById(R.id.tv_carweight);
		tv_carlength=(TextView) root.findViewById(R.id.tv_carlength);
		tv_company=(TextView) root.findViewById(R.id.tv_company);

		linearea1=(TextView) root.findViewById(R.id.linearea1);
		linearea2=(TextView) root.findViewById(R.id.linearea2);
		linearea3=(TextView) root.findViewById(R.id.linearea3);
		linearea4=(TextView) root.findViewById(R.id.linearea4);
		
		tv_pay_tel=(TextView) root.findViewById(R.id.tv_pay_tel);
		tv_pay_tel.setOnClickListener(this);
		tv_authentication_state=(TextView) root.findViewById(R.id.tv_authentication_state);
	}
	private void initNavigation() {
		// TODO Auto-generated method stub
		img_back=(ImageView) root.findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
	}
	
	
	
	
	class CarinfoRequest{
		private String type;
		private String filters;
		
		
		public CarinfoRequest(String filters) {
			// TODO Auto-generated constructor stub
			this.type="100501";
			this.filters=filters;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getFilters() {
			return filters;
		}
		public void setFilters(String filters) {
			this.filters = filters;
		}
		
		
	}
	
	class CarinfoData{
		private String _id;

		public CarinfoData(String id) {
			this._id=id;
		}

		public String get_id() {
			return _id;
		}

		public void set_id(String _id) {
			this._id = _id;
		}
	}
	
	
	String carjsonrequest;
	String tel,payphone;
	private void getCarinfo()
	{
		CarinfoData data=new CarinfoData(Id);
		String filters = new Gson().toJson(data);
		String getStr = new Gson().toJson(new CarinfoRequest(filters));
		executeRequest(new GsonRequest<CarinfoBean>(Request.Method.GET,
				UrlCat.getSearchUrl(getStr), CarinfoBean.class, null,
				null,
				new Listener<CarinfoBean>() {
					@Override
					public void onResponse(CarinfoBean arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							if ("1".equals(arg0.getCode())) {
								Toast.makeText(getActivity(), arg0.getMsg(),
										1000).show();
								tv_changzhudi.setText(arg0.getData().getOfficeplace());
								
								
								if(arg0.getData().getPosition()!=null&&!"".equals(arg0.getData().getPosition()))
								{
									tv_position.setText(arg0.getData().getPosition());
									
								}else{
//									tv_position.setText(arg0.getData().getOfficeplace());
									tv_position.setText("用户没有上传当前位置");
								}
								
								
								
								ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+arg0.getData().getCarimage1(), img_1);
								ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+arg0.getData().getCarimage2(), img_2);
								ImageLoaderUtil.getInstance(getActivity()).loadImage(C.BASE_URL+arg0.getData().getCarimage3(), img_3);
								tv_carcode.setText(InfoUtils.formatCarCode(arg0.getData().getPlatenum()));
								tv_juli.setText(arg0.getData().getDistance());
								tv_chexing.setText(arg0.getData().getCartype());
								tv_carweight.setText(arg0.getData().getApproveweight());
								tv_carlength.setText(arg0.getData().getCarlength()+"m");
								tv_company.setText(arg0.getData().getCompanyname());
								tel=InfoUtils.formatPhone(arg0.getData().getMobile());
								payphone = arg0.getData().getMobile();
								if(arg0.getData().getLinearea1()!=null){
									linearea1.setText("".equals(arg0.getData().getLinearea1())?"":arg0.getData().getLinearea1().replace(",", "--"));
								}
								if(arg0.getData().getLinearea2()!=null){
									linearea2.setText("".equals(arg0.getData().getLinearea2())?"":arg0.getData().getLinearea2().replace(",", "--"));
								}
								if(arg0.getData().getLinearea3()!=null){
									linearea3.setText("".equals(arg0.getData().getLinearea3())?"":arg0.getData().getLinearea3().replace(",", "--"));
								}
								if(arg0.getData().getLinearea4()!=null){
									linearea4.setText("".equals(arg0.getData().getLinearea4())?"":arg0.getData().getLinearea4().replace(",", "--"));
								}
								if("0".equals(arg0.getData().getLoad())){
									tv_load_state.setText("空车");
								}else if("1".equals(arg0.getData().getLoad())){
									tv_load_state.setText("装载");
								}else{
									tv_load_state.setText("未知状态");
								}
								
							}else{
								Toast.makeText(getActivity(),arg0.getMsg(),
										1000).show();
							}
						}else{
							Toast.makeText(getActivity(), "暂无数据！",
									1000).show();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "网络连接异常！",
								1000).show();
					}
				}));
	}
	
	
	
	
	
	String officeplaceCode;
	

	
	class CarinfoBean extends BaseBean{
		private AddCarData data;

		public AddCarData getData() {
			return data;
		}

		public void setData(AddCarData data) {
			this.data = data;
		}
		
	}

	class AddCarData{
		private String userid;
		private String load;//装载状态    开始装载为1
		public String getLoad() {
			return load;
		}


		public void setLoad(String load) {
			this.load = load;
		}


		public String getUserid() {
			return userid;
		}


		public void setUserid(String userid) {
			this.userid = userid;
		}


		


		public String getPlatenum() {
			return platenum;
		}


		public void setPlatenum(String platenum) {
			this.platenum = platenum;
		}


		public String getCartype() {
			return cartype;
		}


		public void setCartype(String cartype) {
			this.cartype = cartype;
		}


		public String getWx() {
			return wx;
		}


		public void setWx(String wx) {
			this.wx = wx;
		}


		public String getApproveweight() {
			return approveweight;
		}


		public void setApproveweight(String approveweight) {
			this.approveweight = approveweight;
		}


		public String getCarsize() {
			return carsize;
		}


		public void setCarsize(String carsize) {
			this.carsize = carsize;
		}


		public String getCartypecode() {
			return cartypecode;
		}


		public void setCartypecode(String cartypecode) {
			this.cartypecode = cartypecode;
		}


		public String getWxcode() {
			return wxcode;
		}


		public void setWxcode(String wxcode) {
			this.wxcode = wxcode;
		}


		public String getDistancecode() {
			return distancecode;
		}


		public void setDistancecode(String distancecode) {
			this.distancecode = distancecode;
		}


		public String getCarlength() {
			return carlength;
		}


		public void setCarlength(String carlength) {
			this.carlength = carlength;
		}


		public String getOfficeplace() {
			return officeplace;
		}


		public void setOfficeplace(String officeplace) {
			this.officeplace = officeplace;
		}


		public String getXslicence() {
			return xslicence;
		}


		public void setXslicence(String xslicence) {
			this.xslicence = xslicence;
		}


		public String getYylicence() {
			return yylicence;
		}


		public void setYylicence(String yylicence) {
			this.yylicence = yylicence;
		}


		public String getBd() {
			return bd;
		}


		public void setBd(String bd) {
			this.bd = bd;
		}


		public String getCarimage1() {
			return carimage1;
		}


		public void setCarimage1(String carimage1) {
			this.carimage1 = carimage1;
		}


		public String getCarimage2() {
			return carimage2;
		}


		public void setCarimage2(String carimage2) {
			this.carimage2 = carimage2;
		}


		public String getCarimage3() {
			return carimage3;
		}


		public void setCarimage3(String carimage3) {
			this.carimage3 = carimage3;
		}


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public String getCreatetime() {
			return createtime;
		}


		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}


		public String getEffectivetime() {
			return effectivetime;
		}


		public void setEffectivetime(String effectivetime) {
			this.effectivetime = effectivetime;
		}


		public String getEffectiveflag() {
			return effectiveflag;
		}


		public void setEffectiveflag(String effectiveflag) {
			this.effectiveflag = effectiveflag;
		}


		public String getDistance() {
			return distance;
		}


		public void setDistance(String distance) {
			this.distance = distance;
		}

		public String getOfficeplacecode() {
			return officeplacecode;
		}


		public void setOfficeplacecode(String officeplacecode) {
			this.officeplacecode = officeplacecode;
		}
		
		public String getCompanyname() {
			return companyname;
		}


		public void setCompanyname(String companyname) {
			this.companyname = companyname;
		}
		public String getMobile() {
			return mobile;
		}


		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		
		private String platenum;
		private String cartype;
		private String cartypecode;
		private String wx;
		private String wxcode;
		private String approveweight;
		private String carsize;
		private String carlength;
		private String officeplace;
		private String officeplacecode;

		private String xslicence;
		private String yylicence;
		private String bd;
		private String carimage1;//车头
		private String carimage2;//45°
		private String carimage3;//车尾

		private String status;
		private String createtime;
		private String effectivetime;
		private String effectiveflag;
		private String distance;
		private String distancecode;
		private String companyname;//公司名称
		
		private String mobile;//电话
		private String[] location;//坐标
		private String position;
		private String linearea1;
		private String linearea2;
		private String linearea3;
		private String linearea4;

		public String getLinearea1() {
			return TextUtils.isEmpty(linearea1)?"":linearea1;
		}


		public void setLinearea1(String linearea1) {
			this.linearea1 = linearea1;
		}


		public String getLinearea2() {
			
			return TextUtils.isEmpty(linearea2)?"":linearea2;
		}


		public void setLinearea2(String linearea2) {
			this.linearea2 = linearea2;
		}


		public String getLinearea3() {
			return TextUtils.isEmpty(linearea3)?"":linearea3;
		}


		public void setLinearea3(String linearea3) {
			this.linearea3 = linearea3;
		}


		public String getLinearea4() {
			return TextUtils.isEmpty(linearea4)?"":linearea4;
		}


		public void setLinearea4(String linearea4) {
			this.linearea4 = linearea4;
		}


		public String getPosition() {
			return position;
		}


		public void setPosition(String position) {
			this.position = position;
		}


		public String[] getLocation() {
			return location;
		}


		public void setLocation(String[] location) {
			this.location = location;
		}


		public AddCarData(String userid,  String platenum,
				String cartype,String cartypecode,  String wx,String wxcode, String approveweight, String carlength,
				String carsize,String officeplace,String officeplacecode,
				String carimage1, String carimage2,String carimage3,
				String xslicence, String yylicence, String bd,String distance,String distancecode) {
			this.userid = userid;
			this.platenum = platenum;
			this.cartype = cartype;
			this.wx = wx;
			this.approveweight = approveweight;
			this.carsize = carsize;
			this.carimage1 = carimage1;
			this.carimage2 = carimage2;
			this.carimage3 = carimage3;
			this.carlength = carlength;
			this.officeplace = officeplace;
			this.xslicence = xslicence;
			this.yylicence = yylicence;
			this.bd = bd;
			this.distance=distance;
			this.distancecode=distancecode;
			this.wxcode=wxcode;
			this.cartypecode=cartypecode;
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_pay_tel:
			
			Intent Int_call = new Intent();
            Int_call.setAction("android.intent.action.CALL");
            Int_call.setData(Uri.parse("tel:"+payphone));
            //使用Intent时，还需要设置其category，不过
            //方法内部会自动为Intent添加类别：android.intent.category.DEFAULT
            startActivity(Int_call);
        	break;
		default:
			break; 
		}
	}
	

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			 // 释放地理编码检索实例  
		}

	
		
	
	

}
